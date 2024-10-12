package com.commenau.controller.admin;

import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.paging.PageRequest;
import com.commenau.paging.Sorter;
import com.commenau.service.ProductService;
import com.commenau.util.HttpUtil;
import com.commenau.util.PagingUtil;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin/products")
public class ProductController extends HttpServlet {
    @Inject
    private ProductService productService;
    @Inject
    private LogService logService;
    private static final int maxPageItem = 7;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String keyWord = request.getParameter("keyWord");
        String sortBy = request.getParameter("sortBy");
        //paging
        int totalItem = productService.countByKeyWord(keyWord);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);

        //sort
        Sorter sorter = StringUtils.isBlank(sortBy) ? new Sorter("createdAt", "DESC") :
                new Sorter(sortBy.split("_")[0], sortBy.split("_")[1]);
        List<Sorter> sorters = List.of(sorter);
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("sort", initSortBy());
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("products", productService.getByKeyWord(pageRequest, keyWord));
        request.setAttribute("productActive", "");
        request.getRequestDispatcher("/admin/admin-product.jsp").forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        boolean isSuccess = productService.setDefaultAvailable(ids);
        response.setStatus(isSuccess ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        String realPath = request.getServletContext().getRealPath("/images/products");
        if(ids.length == 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        boolean isSuccess = productService.deleteByIds(ids, realPath);
        if(isSuccess){
            Map<String, String> data = new HashMap<>();
            data.put("listIdDeleted", Arrays.toString(ids));
            logService.save(LogLevel.WARNING, "deleteSuccess",data);
        }
        response.setStatus(isSuccess ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    private Map<String, String> initSortBy() {
        Map<String, String> sort = new LinkedHashMap<>();
        sort.put("createdAt_DESC", "Sản phẩm mới nhất");
        sort.put("createdAt_ASC", "Sản phẩm cũ nhất");
        sort.put("available_ASC", "Số lượng tăng dần");
        sort.put("available_DESC", "Số lượng giảm dần");
        sort.put("rate_ASC", "Đánh giá tăng dần");
        sort.put("rate_DESC", "Đánh giá giảm dần");
        sort.put("price_ASC", "Giá bán tăng dần");
        sort.put("price_DESC", "Giá bán giảm dần");
        return sort;
    }

}
