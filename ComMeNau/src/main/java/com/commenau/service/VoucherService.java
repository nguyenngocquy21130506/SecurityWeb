package com.commenau.service;

import com.commenau.dao.*;
import com.commenau.dto.VoucherDTO;
import com.commenau.model.Voucher;

import javax.inject.Inject;
import java.util.List;

public class VoucherService {
    @Inject
    private VoucherDAO voucherDAO;
    @Inject
    private CartDAO cartDAO;

    public List<Voucher> getAllVoucher() {
        return voucherDAO.getAllVoucher();
    }

    public List<VoucherDTO> getVoucherDTO() {
        return voucherDAO.getVoucherDTO();
    }


    public VoucherDTO getVoucherById(int voucherId) {
        return voucherDAO.getVoucherById(voucherId);
    }

    public boolean save(Voucher voucher) {
        return voucherDAO.save(voucher);
    }

    public boolean update(Voucher voucher) {
        return voucherDAO.update(voucher);
    }

    public boolean deleteVoucher(int idVoucher) {
        return voucherDAO.deleteVoucher(idVoucher);
    }

    public long applyVoucher(long totalPrice,String idVoucher) {
        long re = totalPrice;
        if (idVoucher != null) {
            if(!idVoucher.equals("0")){
                re = (long) ((1 - this.getVoucherById(Integer.parseInt(idVoucher)).getDiscount()/100) * totalPrice);
//                re = (long) (this.getVoucherById(Integer.parseInt(idVoucher)).getDiscount() - discount);
            }
        }
        return re;
    }

    public boolean changeStatusOfVoucher(long userId,int voucherId) {
        int currentStatus = this.getVoucherById(voucherId).getStatus();
        if(currentStatus == 0){
            return voucherDAO.changeStatusOfVoucher(voucherId,1);
        }else{
            return voucherDAO.changeStatusOfVoucher(voucherId,0);
        }
    }
}
