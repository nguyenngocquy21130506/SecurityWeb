package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.log.LogService;
import com.commenau.model.Blog;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.BlogService;
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


@MultipartConfig(
        maxFileSize = 1024 * 1024,// 1MB
        maxRequestSize = 1024 * 1024 * 10//10MB
)
@WebServlet("/admin/blog-detail")
public class BlogDetailController extends HttpServlet {
    @Inject
    private BlogService blogService;
    @Inject
    private LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String blogIdStr = request.getParameter("blogId");
        Blog blog = new Blog();
        // display data of blog
        if (blogIdStr != null) {
            int blogId = 0;
            try {
                blogId = Integer.parseInt(blogIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            blog = blogService.getBlogById(blogId);
        }
        request.setAttribute("blogActive", "");
        request.setAttribute("blog", blog);
        request.getRequestDispatcher("/admin/admin-blog-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        Blog blog = FormUtil.toModel(Blog.class, request);
        Part image = request.getPart("image");
        String realPath = request.getServletContext().getRealPath("/images/blogs");
        String newFileName = "";

        //validate
        boolean hasError = validate(blog);
        if (!hasError) {
            // if image isn't null
            boolean isImageProvided = !image.getSubmittedFileName().isEmpty();
            if (isImageProvided) {
                newFileName = WriteImage.generateFileName(image.getSubmittedFileName(), realPath);
                blog.setImage(newFileName);
            }
            blog.setCreatedBy(user.fullName());

            boolean isOperationSuccessful = blog.getId() != 0 ? blogService.update(blog) : blogService.save(blog);
            if (isOperationSuccessful && isImageProvided) {
                //write image
                WriteImage.writeImage(image.getInputStream(), realPath, newFileName);
            }
            if(blog.getId() != 0){ // update
                Blog preBlog = blogService.getBlogById(blog.getId());
                logService.save(LogLevel.INFO,"updateSuccess",preBlog,blog);
            }else{ // create
                logService.save(LogLevel.INFO,"addSuccess",blog);
            }
            int statusCode = isOperationSuccessful ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            response.setStatus(statusCode);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    private boolean validate(Blog blog) {
        if (Validator.isEmpty(blog.getTitle()) || Validator.isEmpty(blog.getShortDescription()) ||
                Validator.isEmpty(blog.getContent()))
            return true;
        return false;
    }
}
