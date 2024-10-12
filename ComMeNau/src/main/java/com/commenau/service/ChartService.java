package com.commenau.service;

import com.commenau.dao.ChartDAO;
import com.commenau.dto.ChartInfomationDTO;

import javax.inject.Inject;
import java.util.List;

public class ChartService {
    @Inject
    ChartDAO chartDAO;
    public List<ChartInfomationDTO> getData(String search,String condition , String day){
        if(!search.isEmpty()){
            return chartDAO.getSellProductBySearch(search , day);
        }
        else if(condition.equalsIgnoreCase("mostSell")){
            return chartDAO.getSellProduct(day , "desc");
        }
        else if(condition.equalsIgnoreCase("lessSell")){
            return chartDAO.getSellProduct(day , "asc");
        }
        else if(condition.equalsIgnoreCase("cancelSell")){
            return chartDAO.getCancelProduct(day);
        }
        else if(condition.equalsIgnoreCase("stockSell")){
            return chartDAO.getStockProduct(day);
        }
        else if(condition.equalsIgnoreCase("suggest")){
            return chartDAO.getSuggest(day);
        }
        else if(condition.equalsIgnoreCase("neverSell")){
            return chartDAO.neverSell(day);
        }
        else if(condition.equalsIgnoreCase("againSell")){
            return chartDAO.againSell(day);
        }
        return null;
    }
}
