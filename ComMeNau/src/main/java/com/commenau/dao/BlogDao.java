package com.commenau.dao;

import com.commenau.model.Blog;
import com.commenau.paging.PageRequest;
import com.commenau.util.PagingUtil;

import java.util.List;

import com.commenau.connectionPool.ConnectionPool;

import java.util.Optional;
import java.util.stream.Collectors;

public class BlogDao {
    public Blog getBlogById(int id) {
        Optional<Blog> blog = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id, title, image, shortDescription, content, createdAt from blogs where id = ?").bind(0, id).mapToBean(Blog.class).stream().findFirst();
        });
        return blog.orElse(null);
    }

    public Blog getFirstBlog() {
        Optional<Blog> blog = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("SELECT id FROM blogs ORDER BY id ASC LIMIT 1;").mapToBean(Blog.class).stream().findFirst();
        });
        return blog.orElse(null);
    }

    public Blog getLastBlog() {
        Optional<Blog> blog = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("SELECT id FROM blogs ORDER BY id DESC LIMIT 1;").mapToBean(Blog.class).stream().findFirst();
        });
        return blog.orElse(null);
    }

    public List<Blog> get3BlogByDate() {
        List<Blog> blogList = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id, title, image, shortDescription, createdAt from blogs order by createdAt desc limit 3")
                    .mapToBean(Blog.class).collect(Collectors.toList());
        });
        return blogList;
    }

    public List<Blog> getAllBlogByDate() {
        List<Blog> blogList = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id from blogs")
                    .mapToBean(Blog.class).collect(Collectors.toList());
        });
        return blogList;
    }

    public List<Blog> getBlogs(int pageIndex, int pageSize) {
        List<Blog> blogList = ConnectionPool.getConnection().withHandle(handle -> {
            String sql1 = "select b.*, count(br.id) as numReviews from blogs b left join blog_reviews br on b.id = br.blogId " +
                    "group by b.id LIMIT ? OFFSET ?";
            return handle.createQuery(sql1)
                    .bind(0, pageSize)
                    .bind(1, (pageIndex - 1) * pageSize)
                    .mapToBean(Blog.class)
                    .list();
        });
        return blogList;
    }

    public int countAll() {
        int blogCount = ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("SELECT COUNT(id) FROM blogs")
                    .mapTo(Integer.class)
                    .one();
        });

        return blogCount;
    }

    public List<Blog> findBlogByInput(String input) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select id,title,image,shortDescription,createdAt from blogs where title like :title")
                    .bind("title", "%" + input + "%")
                    .mapToBean(Blog.class)
                    .list();
        });
    }

    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(id) FROM blogs WHERE title LIKE :keyWord OR shortDescription LIKE :keyWord OR content LIKE :keyWord";
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .mapTo(Integer.class)
                        .stream().findFirst().orElse(0)
        );
    }

    public List<Blog> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT id, image, title, shortDescription, content FROM blogs ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql).mapToBean(Blog.class).stream().toList());
    }

    public List<Blog> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT id, image, title, shortDescription, content FROM blogs ");
        builder.append("WHERE title LIKE :keyWord OR shortDescription LIKE :keyWord OR content LIKE :keyWord ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .mapToBean(Blog.class).stream().toList());
    }

    public boolean delete(int id) throws Exception {
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("DELETE FROM blogs WHERE id = :id")
                        .bind("id", id).execute()
        );
        return result > 0;
    }

    public boolean save(Blog blog) {
        String sql = "INSERT INTO blogs(id,title,shortDescription,content,image,createdBy) VALUES " +
                "(:id,:title,:shortDescription,:content,:image,:createdBy)";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql).bindBean(blog).execute());
        return result > 0;
    }

    public boolean update(Blog blog) {
        String sql;
        if (blog.getImage() == null)
            sql = "UPDATE blogs SET title=:title,shortDescription=:shortDescription,content=:content WHERE id=:id";
        else
            sql = "UPDATE blogs SET title=:title,shortDescription=:shortDescription,content=:content,image=:image WHERE id=:id";


        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql).bindBean(blog).execute());
        return result > 0;
    }


}
