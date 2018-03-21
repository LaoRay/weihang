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
import cn.com.clubank.weihang.manage.repair.dao.WorkQueryMapper;

/**
 * 导出维修单信息
 * @author Liangwl
 *
 */
public class ExportRepairServiceOrderExcel extends TestBase {

	@Resource
	private WorkQueryMapper workQueryMapper;
	
	@Test
	public void exportRepairServiceOrderData(){
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("维修单数据");
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
		cell.setCellValue("维修单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);
        cell.setCellValue("车牌号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 4);  
        cell.setCellValue("服务顾问");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("维修师");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("下单时间");  
        cell.setCellStyle(style);

        
        Integer payStatus=null;
        String orderNo=null;
        Integer status=null;
        String consultantName=null; 
        String supervisorName=null;
        List<Map<String,String>> repairList=workQueryMapper.exportRepairServiceOrder(payStatus, orderNo, status, consultantName, supervisorName);
		
		for (short i = 0; i < repairList.size(); i++){
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(repairList.get(i).get("repairNo") == null ? "" : repairList.get(i).get("repairNo"));
			row.createCell(1).setCellValue(repairList.get(i).get("realname") == null ? "" : repairList.get(i).get("realname"));
			row.createCell(2).setCellValue(repairList.get(i).get("mobile") == null ? "" : repairList.get(i).get("mobile"));
			row.createCell(3).setCellValue(repairList.get(i).get("carNo") == null ? "" : repairList.get(i).get("carNo"));
			row.createCell(4).setCellValue(repairList.get(i).get("consultantName") == null ? "" : repairList.get(i).get("consultantName"));
			row.createCell(5).setCellValue(repairList.get(i).get("supervisorName") == null ? "" : repairList.get(i).get("supervisorName"));
			row.createCell(6).setCellValue(repairList.get(i).get("receiveDate") == null ? "" : repairList.get(i).get("receiveDate"));
		}
		
		try {
			//默认导出到E盘下
		    FileOutputStream out = new FileOutputStream("E://维修单信息表.xls");
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
