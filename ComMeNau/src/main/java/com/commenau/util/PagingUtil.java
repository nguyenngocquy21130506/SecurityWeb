package com.commenau.util;

import com.commenau.paging.PageRequest;
import com.commenau.paging.Sorter;

public class PagingUtil {
    public static String appendSortersAndLimit(StringBuilder sql, PageRequest pageRequest) {
        if (!pageRequest.getSorters().isEmpty()) {
            sql.append("ORDER BY ");
            int size = pageRequest.getSorters().size();
            for (int i = 0; i < size; i++) {
                Sorter sorter = pageRequest.getSorters().get(i);
                sql.append(sorter.getSortName() + " " + sorter.getSortBy());
                if (i < size - 1)
                    sql.append(", ");
            }
        }
        if (pageRequest.getMaxPageItem() != 0)
            sql.append(" LIMIT " + pageRequest.getOffset() + "," + pageRequest.getMaxPageItem());
        return sql.toString();
    }
    public static int maxPage(int totalItem, int maxPageItem) {
        return (totalItem % maxPageItem == 0) ? (totalItem / maxPageItem) : (totalItem / maxPageItem) + 1;
    }
    public static int convertToPageNumber(String pageStr, int maxPage) {
        int page = 1;
        if (pageStr == null || pageStr.isEmpty())
            pageStr = "1";
        try {
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (page <= 0 || page > maxPage)
            page = 1;
        return page;
    }
}
