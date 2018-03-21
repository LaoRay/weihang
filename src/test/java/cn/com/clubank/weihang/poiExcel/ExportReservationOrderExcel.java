package cn.com.clubank.weihang.poiExcel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Test;

import cn.com.clubank.weihang.TestBase;
import cn.com.clubank.weihang.manage.bespeak.dao.WorkReservationOrderMapper;
import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;

/**
 * 导出预约单信息
 * @author Liangwl
 *
 */
public class ExportReservationOrderExcel extends TestBase {

	@Resource
	private WorkReservationOrderMapper workReservationOrderMapper;
	
	@Test
	public void exportReservationOrderData(){
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("预约单数据");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short)20);
		//生成一个样式  
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);//样式字体水平居中
	    //设置前景填充色
	    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
	    //设置前景填充样式
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		//给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("预约单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("联系人");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);  
        cell.setCellValue("车牌号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 4);  
        cell.setCellValue("预约类型");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("预约状态");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("预约时间");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("备注");  
        cell.setCellStyle(style);
        
        String reservationDateStart=null;
        String reservationDateEnd=null;
        String carNo=null; 
        Integer orderStatus=null;
        List<Map<String,Object>> orderList=workReservationOrderMapper.exportReservationOrder(carNo, orderStatus, reservationDateStart, reservationDateEnd);
		
		for (short i = 0; i < orderList.size(); i++){
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(orderList.get(i).get("orderNo") == null ? "" : orderList.get(i).get("orderNo").toString());
			row.createCell(1).setCellValue(orderList.get(i).get("customerName") == null ? "" : orderList.get(i).get("customerName").toString());
			row.createCell(2).setCellValue(orderList.get(i).get("contactsMobile") == null ? "" : orderList.get(i).get("contactsMobile").toString());
			row.createCell(3).setCellValue(orderList.get(i).get("carNo") == null ? "" : orderList.get(i).get("carNo").toString());
			row.createCell(4).setCellValue(orderList.get(i).get("reservationType") == null ? "" : WorkReservationOrder.getType((Integer) orderList.get(i).get("reservationType")));
			row.createCell(5).setCellValue(orderList.get(i).get("status") == null ? "" : WorkReservationOrder.getStart((Integer) orderList.get(i).get("status")));
			row.createCell(6).setCellValue(orderList.get(i).get("reservationDate") == null ? "" : orderList.get(i).get("reservationDate").toString());
			row.createCell(7).setCellValue(orderList.get(i).get("description") == null ? "" : orderList.get(i).get("description").toString());
		}
		
		try {
			//默认导出到E盘下
		    FileOutputStream out = new FileOutputStream("E://预约单信息表.xls");
		    wb.write(out);
		    out.close();
		    wb.close();
		    JOptionPane.showMessageDialog(null, "导出成功!");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
		    e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
		    e.printStackTrace();
		}
	 }
}
