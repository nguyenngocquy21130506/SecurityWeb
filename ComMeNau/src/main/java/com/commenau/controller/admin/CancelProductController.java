package com.commenau.controller.admin;

import com.commenau.paging.PageRequest;
import com.commenau.paging.Sorter;
import com.commenau.service.CancelProductService;
import com.commenau.util.HttpUtil;
import com.commenau.util.PagingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/cancel-products")
public class CancelProductController extends HttpServlet {
    @Inject
    private CancelProductService cancelService;
    private static final int maxPageItem = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String keyWord = request.getParameter("keyWord");
        String sortBy = request.getParameter("sortBy");

        //paging
        int totalItem = cancelService.countByKeyWord(keyWord);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        //sort
        Sorter sorter = StringUtils.isBlank(sortBy) ? new Sorter("canceledAt", "DESC") :
                new Sorter(sortBy.split("_")[0], sortBy.split("_")[1]);
        List<Sorter> sorters = List.of(sorter);
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("sort", initSortBy());
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("cancelActive", "");
        request.setAttribute("products", cancelService.getByKeyWord(pageRequest, keyWord));
        request.getRequestDispatcher("/admin/admin-cancel.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        boolean result;
        if (ids.length != 0) {
            result = cancelService.delete(ids);
            response.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Map<String, String> initSortBy() {
        Map<String, String> sort = new LinkedHashMap<>();
        sort.put("canceledAt_DESC", "Ngày mới nhất");
        sort.put("canceledAt_ASC", "Ngày cũ nhất");
        sort.put("quantity_ASC", "Số lượng tăng dần");
        sort.put("quantity_DESC", "Số lượng giảm dần");
        return sort;
    }


}
