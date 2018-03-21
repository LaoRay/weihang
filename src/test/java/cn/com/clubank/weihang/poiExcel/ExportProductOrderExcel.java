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
import cn.com.clubank.weihang.common.util.MergeCellsUtil;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;

/**
 * 导出商品订单信息
 * @author Liangwl
 *
 */
public class ExportProductOrderExcel extends TestBase {

	@Resource
	private OrderListMapper orderListMapper;
	
	@Test
	public void exportProductOrderData(){
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("商品订单数据");		
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
		cell.setCellValue("商品订单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);
        cell.setCellValue("订单总额");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 4);  
        cell.setCellValue("支付总额");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("支付方式");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("订单状态");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("商品名称");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("商品SKU名称");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);  
        cell.setCellValue("商品SKU数量");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);  
        cell.setCellValue("商品SKU价格");  
        cell.setCellStyle(style);

        
        String orderNo=null;
        Integer orderStatus=null;
        String startDate=null;
        String endDate=null; 
        Integer orderCategory=null;
        List<Map<String,Object>> productOrderList=orderListMapper.exportProductOrder(orderNo, orderStatus, startDate, endDate, orderCategory);
		
		for (short i = 0; i < productOrderList.size(); i++){
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(productOrderList.get(i).get("orderNo") == null ? "" : productOrderList.get(i).get("orderNo").toString());
			row.createCell(1).setCellValue(productOrderList.get(i).get("realname") == null ? "" : productOrderList.get(i).get("realname").toString());
			row.createCell(2).setCellValue(productOrderList.get(i).get("mobile") == null ? "" : productOrderList.get(i).get("mobile").toString());
			row.createCell(3).setCellValue(productOrderList.get(i).get("Amount") == null ? "" : productOrderList.get(i).get("Amount").toString());
			row.createCell(4).setCellValue(productOrderList.get(i).get("payAmount") == null ? "" : productOrderList.get(i).get("payAmount").toString());
			row.createCell(5).setCellValue(productOrderList.get(i).get("payType") == null ? "" : OrderList.getPayType((Integer) productOrderList.get(i).get("payType")));
			row.createCell(6).setCellValue(productOrderList.get(i).get("orderStatus") == null ? "" : OrderList.getOrderStatus((Integer) productOrderList.get(i).get("orderStatus")));
			row.createCell(7).setCellValue(productOrderList.get(i).get("productName") == null ? "" : productOrderList.get(i).get("productName").toString());
			row.createCell(8).setCellValue(productOrderList.get(i).get("skuName") == null ? "" : productOrderList.get(i).get("skuName").toString());
			row.createCell(9).setCellValue(productOrderList.get(i).get("quantity") == null ? "" : productOrderList.get(i).get("quantity").toString());
			row.createCell(10).setCellValue(productOrderList.get(i).get("price") == null ? "" : productOrderList.get(i).get("price").toString());
		}
		
		//处理相同的数据合并单元格
		MergeCellsUtil.addProductOrderMergedRegion(sheet, 0, 1, sheet.getLastRowNum(), wb);
		
		try {
			//默认导出到E盘下
		    FileOutputStream out = new FileOutputStream("E://商品订单信息表.xls");
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
