package com.commenau.service;

import com.commenau.dao.*;
import com.commenau.dto.ChartInfomationDTO;
import com.commenau.dto.ProductRelativeViewDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.model.Product;
import com.commenau.paging.PageRequest;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements Pageable<ProductViewDTO> {

    @Inject
    private ProductDAO productDAO;
    @Inject
    private ProductReviewDAO reviewDAO;
    @Inject
    private InvoiceItemDAO itemDAO;
    @Inject
    private WishListDAO wishListDAO;
    @Inject
    private CartDAO cartDAO;
    @Inject
    private CategoryDAO categoryDao;
    @Inject
    private ProductImageDAO productImageDAO;
    @Inject
    private CancelProductDAO cancelDAO;


    public ProductViewDTO getByIdWithAvatar(int id) {
        Product product = productDAO.findOneById(id);
        ProductViewDTO productViewDTO =
                ProductViewDTO.builder()
                        .id(product.getId())
                        .productName(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .discount(product.getDiscount())
                        .view(product.getView())
                        .build();
        productViewDTO.setImages(List.of(productImageDAO.findAvatarByProductId(product.getId())));
        return productViewDTO;

    }

    public void updateAmountOfView(int id){
        productDAO.updateAmountOfView(id);
    }

    public int countProductsInCategory(int categoryId) {
        return productDAO.countProductsInCategory(categoryId);
    }

    public ProductViewDTO getProductViewById(int id) {
        Product product = productDAO.getById(id);
        ProductViewDTO productViewDTO =
                ProductViewDTO.builder()
                        .id(Math.toIntExact(product.getId()))
                        .categoryId(product.getCategoryId())
                        .productName(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .discount(product.getDiscount())
                        .available(product.getAvailable())
                        .status(product.isStatus())
                        .view(product.getView())
                        .build();
        productViewDTO.setImages(productImageDAO.getAllImageByProductId(id));
        productViewDTO.setAmountOfReview(countProductReviewsById(Math.toIntExact(product.getId())));
        productViewDTO.setRating(averageRating(id));

        return productViewDTO;

    }


    public int averageRating(int id) {
        return productDAO.averageRating(id);
    }

    public int countProductReviewsById(int id) {
        return productDAO.countProductReviewsById(id);
    }

    public List<ProductRelativeViewDTO> getNewRelativeProductView() {
        List<Product> products = productDAO.getNewRelativeProductView();
        List<ProductRelativeViewDTO> views = new ArrayList<>();
        for (var x : products) {
            ProductRelativeViewDTO productViewDTO =
                    ProductRelativeViewDTO.builder()
                            .id(Math.toIntExact(x.getId()))
                            .productName(x.getName())
                            .price(x.getPrice())
                            .discount(x.getDiscount())
                            .build();
            String categoryName = categoryDao.getNameById(Math.toIntExact(x.getCategoryId()));
            productViewDTO.setCategoryName(categoryName);
            productViewDTO.setRating(averageRating(Math.toIntExact(x.getId())));
            productViewDTO.setAmountOfReview(countProductReviewsById(Math.toIntExact(x.getId())));
            productViewDTO.setImage(productImageDAO.getAvatar(x.getId()));
            views.add(productViewDTO);
        }
        return views;
    }

    public List<ProductRelativeViewDTO> getRelativeProductViewByID(int productId) {
        List<Product> products = productDAO.getRelativeProductViewByID(productId);
        if(products == null) return null;
        List<ProductRelativeViewDTO> views = new ArrayList<>();
        for (var x : products) {
            ProductRelativeViewDTO productViewDTO =
                    ProductRelativeViewDTO.builder()
                            .id(Math.toIntExact(x.getId()))
                            .productName(x.getName())
                            .price(x.getPrice())
                            .discount(x.getDiscount())
                            .build();
            String categoryName = categoryDao.getNameById(Math.toIntExact(x.getCategoryId()));
            productViewDTO.setCategoryName(categoryName);
            productViewDTO.setRating(averageRating(Math.toIntExact(x.getId())));
            productViewDTO.setAmountOfReview(countProductReviewsById(Math.toIntExact(x.getId())));
            productViewDTO.setImage(productImageDAO.getAvatar(x.getId()));
            views.add(productViewDTO);
        }
        return views;
    }

    @Override
    public List<ProductViewDTO> getPage(int categoryId, int size, int page, String sortBy, String sort) {
        List<Product> products;
        if (sortBy == null) {
            products = productDAO.getProductViewPage(categoryId, size, page);
        } else if (!sortBy.equals("rate")) {
            products = productDAO.getProductViewPage(categoryId, size, page, sortBy, sort);
        } else {
            products = productDAO.getProductViewPageSortByRating(categoryId, size, page, sort);

        }
        List<ProductViewDTO> productViewDTOS = new ArrayList<>();

        for (var x : products) {

            ProductViewDTO productViewDTO =
                    ProductViewDTO.builder()
                            .id(Math.toIntExact(x.getId()))
                            .productName(x.getName())
                            .description(x.getDescription())
                            .price(x.getPrice())
                            .discount(x.getDiscount())
                            .rating((int) x.getRate())
                            .available(x.getAvailable())
                            .view(x.getView())
                            .build();
            String categoryName = categoryDao.getNameById(Math.toIntExact(x.getCategoryId()));
            productViewDTO.setCategoryName(categoryName);
            productViewDTO.setAmountOfReview(this.countProductReviewsById(x.getId()));
            productViewDTO.setAvatar(productImageDAO.getAvatar(x.getId()));
            productViewDTOS.add(productViewDTO);
        }


        return productViewDTOS;

    }

    public List<ProductViewDTO> getByKeyWord(PageRequest pageRequest, String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return getAll(pageRequest);
        else {
            List<ProductViewDTO> results = new ArrayList<>();
            List<Product> products = productDAO.findByKeyWord(pageRequest, keyWord.trim());
            products.forEach(product -> {
                ProductViewDTO dto = getByIdWithAvatar(product.getId());
                dto.setRating((int) product.getRate());
                dto.setStatus(product.isStatus());
                dto.setAvailable(product.getAvailable());
                dto.setCategoryName(categoryDao.getNameById(product.getCategoryId()));
                results.add(dto);
            });
            return results;
        }
    }

    @Override
    public List<ProductViewDTO> getPage(int id, int size, int page) {
        return this.getPage(id, size, page, null, null);
    }

    public int countAll() {
        return productDAO.countAll();
    }

    public int countByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return countAll();
        return productDAO.countByKeyWord(keyWord);
    }

    public List<ProductViewDTO> getAll(PageRequest pageRequest) {
        return productDAO.findAll(pageRequest);
    }
    public List<ProductViewDTO> getAll() {
        return productDAO.findAll();
    }

    public int save(Product product) {
        product.setDiscount(product.getDiscount() / 100);
        if (product.getId() == 0)
            return productDAO.save(product);
        else if (productDAO.update(product) > 0)
            return product.getId();
        return -1;
    }

    public boolean deleteByIds(Integer[] ids, String realPath) {
        try {
            for (Integer id : ids) {
                List<String> allImageByProductId = productImageDAO.getAllImageByProductId(id);

                cancelDAO.deleteByProductId(id);//delete products in cancel_products
                cartDAO.deleteByProductId(id);//delete product in product_images
                productImageDAO.deleteByProductId(id);//delete product in product_reviews
                reviewDAO.deleteByProductId(id);//delete product in wishlists
                wishListDAO.deleteByProductId(id);//delete product in invoice_items
                itemDAO.updateProductId(id);//delete product in products

                //delete product's images
                allImageByProductId.forEach(img -> {
                    File file = new File(realPath + File.separator + img);
                    boolean delete = file.delete();
                });

                productDAO.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Return false immediately if any product deletion fails
            return false;
        }
    }


    public int checkAvailable(int productId, int quantity) {
        Product product = productDAO.findOneById(productId);
        return (product.getAvailable() - quantity < 0) ? product.getAvailable() : quantity;
    }

    public boolean checkProductValid(int productId, int quantity) {
        Product product = productDAO.findOneById(productId);
        return product.getAvailable() > 0 && quantity >= 1 && product.isStatus() && product.getAvailable() >= quantity;
    }

    public boolean setDefaultAvailable(Integer[] ids) {
        if (ids.length == 0)
            return productDAO.setDefaultAvailable();
        else return productDAO.setDefaultAvailable(ids);
    }


    public void execute(String sql) {
        productDAO.execute(sql);
    }
}
