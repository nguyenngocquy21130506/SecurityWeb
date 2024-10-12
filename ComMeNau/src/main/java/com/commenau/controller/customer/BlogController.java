package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.Blog;
import com.commenau.model.User;
import com.commenau.service.BlogReviewService;
import com.commenau.service.BlogService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/blogs")
public class BlogController extends HttpServlet {
    @Inject
    BlogService blogService;
    @Inject
    BlogReviewService blogReviewService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //newest
        List<Blog> list = blogService.getListBlog();
        req.setAttribute("listBlog", list);

        String findInput = req.getParameter("inputData") == null ? "" : req.getParameter("inputData");
        if (!findInput.equals("")) {
            List<Blog> listBlog = blogService.findBlogsByInput(findInput);
            req.setAttribute("inputKey", findInput);
            req.setAttribute("listBlog", blogService.getListBlog());
            req.setAttribute("listBlogs", listBlog);
            req.setAttribute("maxPage", 0);
            req.setAttribute("pageIndex", 1);
            req.getRequestDispatcher("customer/blog.jsp").forward(req, resp);
        } else {
            // paging
            int pageSize = 4;
            int totalItem = blogService.countAll();
            int maxPage = (totalItem % pageSize == 0) ? (totalItem / pageSize) : (totalItem / pageSize) + 1;
            req.setAttribute("maxPage", maxPage);

            String pageIndex = req.getParameter("pageIndex");
            int index = 0;
            if (pageIndex == null)
                pageIndex = "1";
            try {
                index = Integer.parseInt(pageIndex);
            } catch (IllegalArgumentException e) {
                index = 1;
            }
            if (index <= 0 || index > maxPage)
                index = 1;
            req.setAttribute("pageIndex", index);
            List<Blog> listBlogs = blogService.getListBlogPaging(index, pageSize);
            req.setAttribute("listBlogs", listBlogs);
            String currentPage = req.getRequestURL().toString();
            req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
            req.getRequestDispatcher("/customer/blog.jsp").forward(req, resp);
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String findInput = req.getParameter("inputData") == null ? "" : req.getParameter("inputData");
//        if (!findInput.equals("")) {
//            List<Blog> listBlog = blogService.findBlogsByInput(findInput);
//            req.setAttribute("listBlog", blogService.getListBlog());
//            req.setAttribute("listBlogs", listBlog);
//            req.getRequestDispatcher("customer/blog.jsp").forward(req, resp);
//        }
//    }
}
