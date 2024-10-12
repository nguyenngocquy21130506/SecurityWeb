package com.commenau.service;

import com.commenau.dao.ChartDAO;
import com.commenau.dto.ChartInfomationDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.dto.RequestChartDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelService {
    @Inject
    ChartDAO chartDAO;

    @Inject
    ProductService productService;

    public File getFileProducts(String path) throws IOException {
        Date date = new Date();

        // Định dạng ngày giờ thành chuỗi "dd/mm/yyyy"
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        List<ProductViewDTO> dtos = productService.getAll();
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle lockedCellStyle = workbook.createCellStyle();
        lockedCellStyle.setLocked(true);
        Sheet sheet = workbook.createSheet("Dữ liệu");
        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("ID");
        headerRow.getCell(0).setCellStyle(lockedCellStyle);
        headerRow.createCell(1).setCellValue("Tên Sản Phẩm");
        headerRow.getCell(1).setCellStyle(lockedCellStyle);
        headerRow.createCell(2).setCellValue("Danh Mục");
        headerRow.getCell(2).setCellStyle(lockedCellStyle);
        headerRow.createCell(3).setCellValue("Số lượng còn lại");
        headerRow.getCell(3).setCellStyle(lockedCellStyle);
        headerRow.createCell(4).setCellValue("Giá");
        headerRow.getCell(4).setCellStyle(lockedCellStyle);
        headerRow.createCell(5).setCellValue("Discount");
        headerRow.getCell(5).setCellStyle(lockedCellStyle);
        headerRow.createCell(6).setCellValue("Đánh giá");
        headerRow.getCell(6).setCellStyle(lockedCellStyle);
        headerRow.createCell(7).setCellValue("Trạng thái");
        headerRow.getCell(7).setCellStyle(lockedCellStyle);

        int row = 1;
        for (var x : dtos) {
            Row rowInsert = sheet.createRow(row++);
            rowInsert.createCell(0).setCellValue(x.getId());
            rowInsert.getCell(0).setCellStyle(lockedCellStyle);
            rowInsert.createCell(1).setCellValue(x.getProductName());
            rowInsert.createCell(2).setCellValue(x.getCategoryName());
            rowInsert.getCell(2).setCellStyle(lockedCellStyle);
            rowInsert.createCell(3).setCellValue(x.getAvailable());
            rowInsert.createCell(4).setCellValue(x.getPrice());
            System.out.println(x.getDiscount());
            rowInsert.createCell(5).setCellValue(x.getDiscount());
            rowInsert.createCell(6).setCellValue(x.getRating());
            rowInsert.getCell(6).setCellStyle(lockedCellStyle);
            rowInsert.createCell(7).setCellValue(x.isStatus() ? "Đang kinh doanh" : "ngừng kinh doanh");
            rowInsert.getCell(7).setCellStyle(lockedCellStyle);
        }

        File tempFile = new File(path + "ProductsOf" + today + ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(tempFile);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        return tempFile;
    }

    public File getFile(RequestChartDTO dto, String path) throws IOException {

        Date date = new Date();

        // Định dạng ngày giờ thành chuỗi "dd/mm/yyyy"
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Tạo một trang trong workbook
        Sheet sheet = workbook.createSheet("Dữ liệu");
        Row headerRow = sheet.createRow(0);
        List<ChartInfomationDTO> list = null;
        headerRow.createCell(0).setCellValue("Tên Sản Phẩm");
        headerRow.createCell(1).setCellValue("Số lượng bán");
        String nameFile = "";
        String day = dto.getDay().replaceAll(" ", "-");
        if (!dto.getSearch().isEmpty()) {
            nameFile = "SellOf-" + dto.getSearch() + "-from-" + today + "-to-" + day + "-ago";
            headerRow.createCell(0).setCellValue("Ngày Bán");
            headerRow.createCell(1).setCellValue("Số lượng bán");
            list = chartDAO.getSellProductBySearch(dto.getSearch(), dto.getDay());
        } else if (dto.getCondition().equalsIgnoreCase("mostSell")) {
            nameFile = "MostSellSortDesc-from-" + today + "-to-" + day + "-ago";
            list = chartDAO.getSellProduct(dto.getDay(), "desc");
        } else if (dto.getCondition().equalsIgnoreCase("lessSell")) {
            nameFile = "MostSellSortAsc-" + day + "-from-" + today + "-to-" + day + "-ago";
            list = chartDAO.getSellProduct(dto.getDay(), "asc");
        } else if (dto.getCondition().equalsIgnoreCase("cancelSell")) {
            nameFile = "CancelSell-from-" + today + "-to-" + day + "-ago";
            list = chartDAO.getCancelProduct(dto.getDay());
        } else if (dto.getCondition().equalsIgnoreCase("stockSell")) {
            nameFile = "StockSell-from-" + today + "-to-" + day + "-ago";
            list = chartDAO.getStockProduct(dto.getDay());
        } else if (dto.getCondition().equalsIgnoreCase("suggest")) {
            list = chartDAO.getSuggest(dto.getDay());
        } else if (dto.getCondition().equalsIgnoreCase("neverSell")) {
            nameFile = "NeverSell-from-" + today + "-to-" + day + "-ago";
            list = chartDAO.neverSell(dto.getDay());
        } else if (dto.getCondition().equalsIgnoreCase("againSell")) {
            nameFile = "SellAgain--from-" + today + "-to-" + day + "-ago";
            list = chartDAO.againSell(dto.getDay());
        }

        if (list == null) return null;
        if (nameFile.isEmpty()) {
            nameFile = "Data";
        }
        int row = 1;
        for (var data : list) {
            Row rowInsert = sheet.createRow(row++);
            rowInsert.createCell(0).setCellValue(data.getName());
            rowInsert.createCell(1).setCellValue(data.getAmount());
        }
        File tempFile = new File(path + nameFile + ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(tempFile);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        return tempFile;
    }

    public void updateProducts(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        StringBuilder sql = new StringBuilder();
        boolean pass = true;
        for (var x : sheet) {
            if(pass) {
                pass = false;
                continue;
            }
            String set = String.format("update products set available = %s , price=%s , discount=%s where id = %s;", ((int) x.getCell(3).getNumericCellValue())+""
                    ,((int) x.getCell(4).getNumericCellValue())+"",
                    x.getCell(5).getNumericCellValue()+"",((int)x.getCell(0).getNumericCellValue())
            );
            sql.append(set);
        }
        productService.execute(sql.toString());
    }
}
