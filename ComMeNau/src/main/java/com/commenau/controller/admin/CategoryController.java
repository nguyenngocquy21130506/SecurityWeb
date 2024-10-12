package com.commenau.controller.admin;

import com.commenau.log.LogService;
import com.commenau.model.Category;
import com.commenau.model.LogLevel;
import com.commenau.paging.PageRequest;
import com.commenau.service.CategoryService;
import com.commenau.util.HttpUtil;
import com.commenau.validate.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/categories")
public class CategoryController extends HttpServlet {
    @Inject
    private CategoryService categoryService;
    @Inject
    private LogService logService;
    private static final int maxPageItem = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPageStr = request.getParameter("page");
        int page = 1;
        if (nextPageStr == null || nextPageStr.isEmpty())
            nextPageStr = "1";
        try {
            page = Integer.parseInt(nextPageStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int totalItem = categoryService.getTotalItem();
        int maxPage = (totalItem % maxPageItem == 0) ? (totalItem / maxPageItem) : (totalItem / maxPageItem) + 1;
        if (page <= 0 || page > maxPage)
            page = 1;
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem).build();
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("categoryActive", "");
        request.setAttribute("categories", categoryService.getAll(pageRequest));
        request.getRequestDispatcher("/admin/admin-category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Category category = HttpUtil.of(request.getReader()).toModel(Category.class);
        boolean hasError = validate(category);
        if (!hasError) {
            if (category.getId() != 0) {
                // update category
                boolean result = categoryService.update(category);
                if(result){
                    logService.save(LogLevel.WARNING,"updateSuccess", category);
                }
                response.setStatus(result ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                //add new category
                boolean result = categoryService.save(category);
                response.setStatus(result ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        boolean result = categoryService.detele(ids);
        if(result){
            Map<String,String> data = new HashMap<>();
            data.put("listId",Arrays.toString(ids));
            logService.save(LogLevel.WARNING,"deleteSuccess", data);
        }
        objectMapper.writeValue(response.getOutputStream(), result);
    }

    private boolean validate(Category category) {
        return Validator.isEmpty(category.getName()) || Validator.isEmpty(category.getCode());
    }
}
