package cn.com.clubank.weihang.poiExcel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import cn.com.clubank.weihang.manage.product.dao.ProductAftersaleApplyMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductAftersaleApply;

/**
 * 导出售后申请信息
 * @author Liangwl
 *
 */
public class ExportAftersaleApplyInfoExcel extends TestBase {

	@Resource
	private ProductAftersaleApplyMapper productAftersaleApplyMapper;
	
	@Test
	public void exportAftersaleApplyData(){
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("售后申请数据");		
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
		cell.setCellValue("订单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);  
        cell.setCellValue("申请类型");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("申请时间");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("商品SKU名称");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 6);  
        cell.setCellValue("商品SKU数量");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("商品SKU价格");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("订单状态");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);  
        cell.setCellValue("处理时间");  
        cell.setCellStyle(style);

        
        Integer status=null;
        String startDate=null;
        String endDate=null;
        List<Map<String,Object>> aftersaleApplyList=productAftersaleApplyMapper.exportAftersaleApplyInfo(status, startDate, endDate);
		
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
		for (short i = 0; i < aftersaleApplyList.size(); i++){
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(aftersaleApplyList.get(i).get("orderNo") == null ? "" : aftersaleApplyList.get(i).get("orderNo").toString());
			row.createCell(1).setCellValue(aftersaleApplyList.get(i).get("realname") == null ? "" : aftersaleApplyList.get(i).get("realname").toString());
			row.createCell(2).setCellValue(aftersaleApplyList.get(i).get("mobile") == null ? "" : aftersaleApplyList.get(i).get("mobile").toString());
			row.createCell(3).setCellValue(aftersaleApplyList.get(i).get("subType") == null ? "" : ProductAftersaleApply.getSubType((Integer) aftersaleApplyList.get(i).get("subType")));
			row.createCell(4).setCellValue(aftersaleApplyList.get(i).get("subTime") == null ? "" : sdf.format(aftersaleApplyList.get(i).get("subTime")));
			row.createCell(5).setCellValue(aftersaleApplyList.get(i).get("skuName") == null ? "" : aftersaleApplyList.get(i).get("skuName").toString());
			row.createCell(6).setCellValue(aftersaleApplyList.get(i).get("quantity") == null ? "" : aftersaleApplyList.get(i).get("quantity").toString());
			row.createCell(7).setCellValue(aftersaleApplyList.get(i).get("price") == null ? "" : aftersaleApplyList.get(i).get("price").toString());
			row.createCell(8).setCellValue(aftersaleApplyList.get(i).get("status") == null ? "" : ProductAftersaleApply.getStatus((Integer) aftersaleApplyList.get(i).get("status")));
			row.createCell(9).setCellValue(aftersaleApplyList.get(i).get("conductTime") == null ? "" : sdf.format(aftersaleApplyList.get(i).get("conductTime")));
		}
		
		try {
			//默认导出到E盘下
		    FileOutputStream out = new FileOutputStream("E://售后申请信息表.xls");
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
