/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import DAL.BorrowDAL;
import DAL.BorrowDetailDAL;
import DTO.BorrowDetailDTO;
import DTO.BorrowDTO;
import GUI.MenuleftGUI;

/**
 *
 * @author hoangtuan
 */
public class BorrowDetailBUS {

    BorrowDTO pmdal = new BorrowDTO();
    private ArrayList<BorrowDTO> listpm = new ArrayList<BorrowDTO>();

    public ArrayList<BorrowDetailDTO> getListBorrow() {
        return BorrowDetailDAL.getListBorrow();
    }

    //thong ke 
    public ArrayList<BorrowDetailDTO> statisticalBorrow() {

        return BorrowDetailDAL.statisticalBorrow();
    }

    public int addBorrowDetail(BorrowDetailDTO pm) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();

            if (checkQuantityBorrow(pm.getBorrow_id()) >= 5) {
                showMessage("Bạn không được mượn quá 5 cuốn sách");
                return -1;
            }

            if (pm.getBook_id() < 0) {
                showMessage("Mã sách không được bỏ trống");
                return -1;
            }

            Date borrowDate = sdf.parse(pm.getBorrow_date());
            Date payDate = sdf.parse(pm.getPay_day());

            if (borrowDate.after(payDate)) {
                showMessage("Ngày mượn không được lớn hơn ngày trả");
                return -1;
            }

            if (borrowDate.after(currentDate) || payDate.after(currentDate)) {
                showMessage("Ngày mượn và ngày trả không được lớn hơn ngày hiện tại");
                return -1;
            }

            if (BorrowDetailDAL.addBorrowDetail(pm) > 0) {
                showMessage("Thêm chi tiết thành công");
                return 1;
            } else {
                showMessage("Thêm chi tiết thất bại");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public int editBorrowDetail(BorrowDetailDTO pm) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            if (pm.getBook_id() < 0) {
                JOptionPane.showMessageDialog(null, "Mã sách không được bỏ trống");
                return -1;
            }
            if (sdf.parse(pm.getPay_day()).before(sdf.parse(pm.getBorrow_date()))) {
                JOptionPane.showMessageDialog(null, "Ngày trả không được bé hơn ngày mượn");
                return -1;
            }
            if (sdf.parse(pm.getBorrow_date()).after(sdf.parse(pm.getPay_day()))) {
                JOptionPane.showMessageDialog(null, "Ngày mượn không được lớn hơn ngày trả");
                return -1;
            }
            if (BorrowDetailDAL.editBorrowDetail(pm) > 0) {
                JOptionPane.showMessageDialog(null, "Sửa chi tiết thành công");
                return 1;
            }
            JOptionPane.showMessageDialog(null, "Sửa chi tiết thất bại");
            return -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int deleteBorrowDetail(BorrowDetailDTO pm) {
        if (BorrowDetailDAL.deleteBorrowDetail(pm) > 0) {
            JOptionPane.showMessageDialog(null, "Xoá chi tiết thành công");
            return 1;
        }
        JOptionPane.showMessageDialog(null, "Xoá chi tiết thất bại");
        return -1;
    }

    public int getQuantityBookBorrow() {
        return BorrowDetailDAL.getQuantityBookBorrow();

    }

    public static BorrowDetailBUS iBus = null;

    public static BorrowDetailBUS gI() {
        if (iBus == null) {
            iBus = new BorrowDetailBUS();
        }
        return iBus;
    }

    public static ArrayList<BorrowDTO> list = new ArrayList<BorrowDTO>();

    public int checkQuantityBorrow(int PM) {
        int count = 0;
        for (BorrowDTO ctpm : list) {
            if (ctpm.getBorrow_id() == PM) {
                count++;
                if (count >= 5) {
                    return count;
                }
            }
        }
        return count;
    }
}
