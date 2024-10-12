package com.commenau.service;

import com.commenau.dao.CartDAO;
import com.commenau.dao.ProductDAO;
import com.commenau.dao.VoucherDAO;
import com.commenau.dto.CartItemDTO;
import com.commenau.model.Cart;
import com.commenau.model.CartItem;
import com.commenau.util.RoundUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class CartService {
    @Inject
    private CartDAO cartDAO;
    @Inject
    private ProductDAO productDAO;
    @Inject
    private ProductService productService;
    @Inject
    private VoucherDAO voucherDAO;

    public List<CartItemDTO> getCartByUserId(long userId) {
        return cartDAO.findCartItemByUserId(userId);
    }

    public Cart getCartById(long userId) {
        return cartDAO.findCartByUserId(userId);

    }

    public List<CartItemDTO> remainItems(List<CartItemDTO> items) {
        return items.stream().filter(n -> n.getProduct().getAvailable() > 0 && n.getProduct().isStatus()).toList();
    }

    public List<CartItemDTO> removeItems(List<CartItemDTO> items) {
        return items.stream().filter(n -> n.getProduct().getAvailable() == 0 || !n.getProduct().isStatus()).toList();
    }

    public long getTotalPrice(List<CartItemDTO> items) {
        long total = 0;
        for (CartItemDTO item : items) {
            double price = item.getProduct().getPrice();
            float discount = item.getProduct().getDiscount();
            total += RoundUtil.roundPrice(item.getQuantity()*(price * (1 - discount)));
        }
        return total;
    }

    public long totalPrice(List<CartItemDTO> items) {
        long total = 0;
        double voucherDiscount =  voucherDAO.getVoucherById(cartDAO.getCartFromIdItem(items.get(0).getId()).getVoucherId())==null ? 1 : voucherDAO.getVoucherById(cartDAO.getCartFromIdItem(items.get(0).getId()).getVoucherId()).getDiscount()/100;
        for (CartItemDTO item : items) {
            double price = item.getProduct().getPrice();
            float discount = item.getProduct().getDiscount();
            total += RoundUtil.roundPrice((price * (1 - discount)));
        }
        return (long) (total * (1-voucherDiscount));
    }

    public boolean addProductToCart(long userId, int productId, int quantity) {
        // If the product is out of stock or the product is out of business
        if (!productService.checkProductValid(productId, quantity))
            return false;

        Cart cart = cartDAO.findCartByUserId(userId);
        //if user doesn't have cart, this step is only performed once when the user add the first product to the cart
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart = cartDAO.save(cart);
        }
        CartItem itemEntity = cartDAO.findCartItemByProductAndCart(productId, cart.getId());
        // if product isn't exists in cart then create a new item
        if (itemEntity == null) {
            itemEntity = new CartItem();
            itemEntity.setProductId(productId);
            quantity = productService.checkAvailable(productId, quantity);
            itemEntity.setQuantity(quantity);
            itemEntity.setCartId(cart.getId());
            return cartDAO.saveCartItem(itemEntity);
        } else {
            //if it already has then update quantity
            quantity = productService.checkAvailable(productId, itemEntity.getQuantity() + quantity);
            itemEntity.setQuantity(quantity);
            return cartDAO.updateCartItem(itemEntity, userId);
        }
    }

    public boolean deleteProduct(int productId, long userId) {
        return cartDAO.deleteProduct(productId, userId);
    }

    public boolean deleteProduct(List<Integer> cartsId) {
        return cartDAO.deleteProduct(cartsId);
    }

    public boolean deleteAll(long userId) {
        return cartDAO.deleteAll(userId);
    }

    public boolean updateCart(Map<String, String> map, long userId) {
        try {
            boolean hasError = false;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                int productId = Integer.parseInt(entry.getKey());
                int quantity = Integer.parseInt(entry.getValue());
                if (!productService.checkProductValid(productId, quantity)) {
                    hasError = true;
                }
                quantity = productService.checkAvailable(productId, quantity);
                cartDAO.updateCartItem(CartItem.builder().productId(productId).quantity(quantity).build(), userId);

            }
            return !hasError;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean chooseVoucher(long userId, int voucherId) {
        int check = cartDAO.findCartByUserId(userId).getVoucherId();
        if( check == voucherId){
            return cartDAO.removeInvoice(userId);
        }else{
            return cartDAO.changeInvoice(userId,voucherId);
        }
    }
    public int getNumCartItem(long userId){
        return cartDAO.getNumCartItem(userId);
    }
}
