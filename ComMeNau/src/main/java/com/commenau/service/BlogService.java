package com.commenau.service;

import com.commenau.dao.BlogDao;
import com.commenau.dao.BlogReviewDao;
import com.commenau.model.Blog;
import com.commenau.paging.PageRequest;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public class BlogService {
    @Inject
    private BlogDao blogDao;
    @Inject
    private BlogReviewDao reviewDao;

    public Blog getBlogById(int id) {
        return blogDao.getBlogById(id);
    }

    public Blog getFirstBlog() {
        return blogDao.getFirstBlog();
    }

    public Blog getLastBlog() {
        return blogDao.getLastBlog();
    }

    public List<Blog> getListBlog() {
        return blogDao.get3BlogByDate();
    }

    public List<Blog> getListAllBlog() {
        return blogDao.getAllBlogByDate();
    }

    public List<Blog> findBlogsByInput(String input){
        return blogDao.findBlogByInput(input);
    }

    public int countAll() {
        return blogDao.countAll();
    }

    public int countByKeyWord(String keyWord) {
        if (StringUtils.isBlank(keyWord))
            return blogDao.countAll();
        return blogDao.countByKeyWord(keyWord);
    }

    public List<Blog> getListBlogPaging(int pageIndex, int pageSize) {
        return blogDao.getBlogs(pageIndex, pageSize);
    }


    public List<Blog> getAll(PageRequest pageRequest) {
        return blogDao.findAll(pageRequest);
    }

    public List<Blog> getByKeyWord(PageRequest pageRequest, String keyWord) {
        List<Blog> list = null;
        if (StringUtils.isBlank(keyWord)) {
            list = blogDao.findAll(pageRequest);
        } else {
            list = blogDao.findByKeyWord(pageRequest, keyWord.trim());
        }
        list.forEach(blog -> {
            String description = blog.getShortDescription();
            // display description only 400 character
            if (description.length() > 400) {
                int lastSpaceLast = description.indexOf(" ", 400);
                description = description.substring(0, lastSpaceLast) + "......";
                blog.setShortDescription(description);
            }
        });
        return list;
    }

    public boolean delete(Integer[] ids, String realPath) {
        try {
            for (Integer id : ids) {
                //delete blog in blog_reviews
                reviewDao.deleteByBlogId(id);

                //delete blog's image
                String imageName = getBlogById(id).getImage();
                File file = new File(realPath + File.separator + imageName);
                boolean delete = file.delete();

                //delete blog
                blogDao.delete(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean save(Blog blog) {
        return blogDao.save(blog);
    }

    public boolean update(Blog blog) {
        return blogDao.update(blog);
    }


}
