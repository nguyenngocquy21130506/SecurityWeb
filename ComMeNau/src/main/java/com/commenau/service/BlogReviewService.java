package com.commenau.service;

import com.commenau.dao.BlogReviewDao;
import com.commenau.dao.UserDAO;
import com.commenau.dto.BlogReviewDTO;
import com.commenau.model.Blog;
import com.commenau.model.BlogReview;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BlogReviewService {
    @Inject
    private BlogReviewDao blogReviewDao;
    @Inject
    private UserDAO userDAO;

//    public List<BlogReviewDTO> get10BlogReviewToTime(int blogId){
//        return blogReviewDao.get10BlogReviewToTime(blogId);
//    }
    public List<BlogReviewDTO> getBlogReviewToTime(int blogId){
        List<BlogReview> blogReviewList = blogReviewDao.getBlogReviewToTime(blogId);
        List<BlogReviewDTO> result = new ArrayList<>();
        for (BlogReview blr : blogReviewList) {
            BlogReviewDTO blrDTO = BlogReviewDTO.builder().content(blr.getContent())
                    .createdAt(blr.getCreatedAt())
                    .user(userDAO.getUserById(blr.getUserId())).build();
            result.add(blrDTO);
        }
        return result;
    }

    public int getAllBlogReviews(int blogId) {
        return blogReviewDao.getAllBlogReview(blogId).size();
    }

    public void insertBlogReview(BlogReview blogReview) {
        blogReviewDao.saveReviewByUser(blogReview);
    }

}
