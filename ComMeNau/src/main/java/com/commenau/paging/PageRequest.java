package com.commenau.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    private int page;
    private int maxPageItem;//or limit
    private List<Sorter> sorters;

    public int getOffset() {
        if (page != 0 && maxPageItem != 0)
            return (page - 1) * maxPageItem;
        else return 0;
    }


}
