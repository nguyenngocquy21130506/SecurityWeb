package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.ProductService;
import com.commenau.service.WishlistService;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/menu/filter")
public class FilterProductController extends HttpServlet {
    @Inject
    ProductService productService;
    @Inject
    WishlistService wishlistService;

    Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        int id ;
        int size;
        int page ;
        if(req.getParameter("id") == null ){
            id = 0;
            size = 9;
            page = 1;
        }
        else{
             id = Integer.valueOf(req.getParameter("id"));
             size = Integer.valueOf(req.getParameter("size"));
             page = Integer.valueOf(req.getParameter("page"));
        }
        String sortBy = req.getParameter("sortBy");
        String sort = req.getParameter("sort");

        var result = productService.getPage(id,size,page,sortBy,sort);
        if(user != null){
            for(var x : result){
                x.setWishlist(wishlistService.existsItem((int) user.getId(),x.getId()));
            }
        }
        resp.getWriter().write(gson.toJson(result));
    }
}
