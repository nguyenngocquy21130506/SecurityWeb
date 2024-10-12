package com.commenau.controller.admin;

import com.commenau.paging.PageRequest;
import com.commenau.paging.Sorter;
import com.commenau.service.BlogService;
import com.commenau.util.HttpUtil;
import com.commenau.util.PagingUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/blogs")
public class BlogController extends HttpServlet {
    @Inject
    private BlogService blogService;
    private static final int maxPageItem = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String keyWord = request.getParameter("keyWord");

        //paging
        int totalItem = blogService.countByKeyWord(keyWord);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        List<Sorter> sorters = List.of(new Sorter("createdAt", "DESC"));
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("blogActive", "");
        request.setAttribute("blogs", blogService.getByKeyWord(pageRequest, keyWord));
        request.getRequestDispatcher("/admin/admin-blog.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        String realPath = request.getServletContext().getRealPath("/images/blogs");
        boolean result;
        if (ids.length != 0) {
            result = blogService.delete(ids, realPath);
            response.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
