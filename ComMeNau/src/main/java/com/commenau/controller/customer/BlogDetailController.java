package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.BlogReviewDTO;
import com.commenau.log.LogService;
import com.commenau.model.Blog;
import com.commenau.model.BlogReview;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.BlogReviewService;
import com.commenau.service.BlogService;
import com.commenau.util.FormUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/blog-detail")
public class BlogDetailController extends HttpServlet {
    @Inject
    BlogService blogService;
    @Inject
    BlogReviewService blogReviewService;
    @Inject
    LogService logService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        try {
            int blogId = Integer.parseInt(idParam);
            Blog blog = blogService.getBlogById(blogId);
            List<Blog> listBlog = blogService.getListAllBlog();
            List<BlogReviewDTO> blreviews = blogReviewService.getBlogReviewToTime(blogId);
            if (blog != null) {
                req.setAttribute("blog", blog);
                Blog lastBlog = blogService.getLastBlog();
                Blog firstBlog = blogService.getFirstBlog();
                if (lastBlog.getId() == blogId) {
                    req.setAttribute("lastBlog", "end");
                }
                if (firstBlog.getId() == blogId) {
                    req.setAttribute("firstBlog", "end");
                }
                req.setAttribute("listBlogReview", blreviews);
                for(int i = 0; i < listBlog.size(); i++){
                    if (i == 0 && listBlog.get(0).getId() == blogId) {
                        req.setAttribute("nextblog", listBlog.get(i+1));
                    }else if (i == listBlog.size() - 1 && listBlog.get(listBlog.size() - 1).getId() == blogId) {
                        req.setAttribute("preblog", listBlog.get(i-1));
                    }else if (i != 0 && i != listBlog.size() - 1 && listBlog.get(i).getId() == blogId) {
                        req.setAttribute("nextblog", listBlog.get(i+1));
                        req.setAttribute("preblog", listBlog.get(i-1));
                        break;
                    }
                }
//                String currentPage = req.getRequestURL().toString();
//                req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
                req.setAttribute("lengthListBlogReview", blogReviewService.getAllBlogReviews(blogId));
                req.getRequestDispatcher("/customer/blog-detail.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.getWriter().write("ID không hợp lệ.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BlogReview blogReview = FormUtil.toModel(BlogReview.class, request);
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        int blogId = Integer.parseInt(request.getParameter("blogId"));
        blogReview.setUserId(user.getId());
        blogReview.setBlogId(blogId);
        if(blogReview != null){
            logService.save(LogLevel.INFO,"success",blogReview);
        }
        blogReviewService.insertBlogReview(blogReview);
        response.sendRedirect("blog-detail?id=" + blogId);
    }
}
