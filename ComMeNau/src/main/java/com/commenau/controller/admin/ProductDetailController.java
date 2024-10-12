package com.commenau.controller.admin;

import com.commenau.dto.ProductViewDTO;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.Product;
import com.commenau.model.ProductImage;
import com.commenau.paging.PageRequest;
import com.commenau.service.CategoryService;
import com.commenau.service.ProductImageService;
import com.commenau.service.ProductService;
import com.commenau.util.FormUtil;
import com.commenau.util.WriteImage;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@MultipartConfig
@WebServlet("/admin/product-detail")
public class ProductDetailController extends HttpServlet {
    @Inject
    private ProductService productService;
    @Inject
    private ProductImageService imageService;
    @Inject
    private CategoryService categoryService;
    @Inject
    private LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdStr = request.getParameter("productId");
        ProductViewDTO product = new ProductViewDTO();
        // display data of product
        if (productIdStr != null) {
            int productId = 0;
            try {
                productId = Integer.parseInt(productIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            product = productService.getProductViewById(productId);
        }
        request.setAttribute("productActive", "");
        request.setAttribute("product", product);
        request.setAttribute("categories", categoryService.getAll(new PageRequest()));
        request.getRequestDispatcher("/admin/admin-product-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = FormUtil.toModel(Product.class, request);
        String realPath = request.getServletContext().getRealPath("/images/products");
        ProductViewDTO preProduct = productService.getProductViewById(product.getId());
        //validate
        boolean hasError = validate(product);
        if (!hasError) {
            int productId = productService.save(product);
            Map<String, String> preData = new HashMap<>();
            Map<String, String> data = new HashMap<>();
            preData.put("username", preProduct.getProductName());
            preData.put("phoneNumber", preProduct.getDiscount()+"");
            preData.put("status", preProduct.getPrice()+"");
            preData.put("status", preProduct.getAvailable()+"");

            data.put("username", product.getName());
            data.put("phoneNumber", product.getDiscount()+"");
            data.put("newStatus", product.getPrice()+"");
            data.put("newStatus", product.getAvailable()+"");
            logService.save(LogLevel.WARNING,"updateSuccess",preData,data);
            Collection<Part> parts = request.getParts();
            parts.forEach(image -> {
                // if image isn't null
                boolean isMainImg = image.getName().equals("mainImg");
                boolean isSecondImg = image.getName().equals("secondImg");
                if (productId > 0 && (isMainImg || isSecondImg)) {
                    boolean isImageProvided = !image.getSubmittedFileName().isEmpty();
                    if (isImageProvided) {
                        String newFileName = WriteImage.generateFileName(image.getSubmittedFileName(), realPath);
                        ProductImage productImage = ProductImage.builder().image(newFileName).productId(productId).build();
                        if (image.getName().equals("mainImg"))
                            productImage.setAvatar(true);
                        if (imageService.saveImage(productImage))
                            try {
                                WriteImage.writeImage(image.getInputStream(), realPath, newFileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }
            });
            response.setStatus(productId > 0 ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    private boolean validate(Product product) {
        //validate category
        if (Validator.isEmpty(String.valueOf(product.getCategoryId())) || product.getCategoryId() <= 0)
            return true;
        //validate status
        if (Validator.isEmpty(String.valueOf(product.isStatus())))
            return true;
        //validate name
        if (Validator.isEmpty(product.getName()))
            return true;
        //validate discount
        if (Validator.isEmpty(String.valueOf(product.getDiscount())))
            return true;
        else if (!Validator.between(product.getDiscount(), 0, 100))
            return true;
        //validate available
        if (Validator.isEmpty(String.valueOf(product.getAvailable())))
            return true;
        else if (!Validator.between(product.getAvailable(), 0, 500))
            return true;
        //validate price
        if (Validator.isEmpty(String.valueOf(product.getPrice())))
            return true;
        else return !Validator.between(product.getPrice(), 0, 10000000);
    }

}
