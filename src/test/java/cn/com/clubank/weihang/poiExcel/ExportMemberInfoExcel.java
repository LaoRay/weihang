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
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;

/**
 * 导出会员信息
 * @author Liangwl
 *
 */
public class ExportMemberInfoExcel extends TestBase {
	
	@Resource
	private CustomerInfoMapper customerInfoMapper;
	
	@Test
	public void exportMemberData(){
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("会员数据");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short)15);
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
		cell.setCellValue("会员名称"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("车牌号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);
        cell.setCellValue("车架号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 4); 
        cell.setCellValue("车辆等级");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 5);  
        cell.setCellValue("车辆余额");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("车辆品牌");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("车辆年限");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("发动机排量");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);  
        cell.setCellValue("发动机号");  
        cell.setCellStyle(style);
        
        String realname=null;
        String nickname=null;
        String carNo=null; 
        Integer status=null;
        List<Map<String,Object>> memberList=customerInfoMapper.exportMemberInfo(realname, nickname, carNo, status);
		
		for (short i = 0; i < memberList.size(); i++){
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(memberList.get(i).get("realname") == null ? "" : memberList.get(i).get("realname").toString());
			row.createCell(1).setCellValue(memberList.get(i).get("mobile") == null ? "" : memberList.get(i).get("mobile").toString());
			row.createCell(2).setCellValue(memberList.get(i).get("carNo") == null ? "" : memberList.get(i).get("carNo").toString());
			row.createCell(3).setCellValue(memberList.get(i).get("carVin") == null ? "" : memberList.get(i).get("carVin").toString());
			row.createCell(4).setCellValue(memberList.get(i).get("benefitName") == null ? "" : memberList.get(i).get("benefitName").toString());
			row.createCell(5).setCellValue(memberList.get(i).get("account") == null ? "" : memberList.get(i).get("account").toString());
			row.createCell(6).setCellValue(memberList.get(i).get("carBrand") == null ? "" : memberList.get(i).get("carBrand").toString());
			row.createCell(7).setCellValue(memberList.get(i).get("years") == null ? "" : memberList.get(i).get("years").toString());
			row.createCell(8).setCellValue(memberList.get(i).get("exhaustAmount") == null ? "" : memberList.get(i).get("exhaustAmount").toString());
			row.createCell(9).setCellValue(memberList.get(i).get("engineNumber") == null ? "" : memberList.get(i).get("engineNumber").toString());
		}
		
		//处理相同的数据合并单元格
		MergeCellsUtil.addMemberInfoMergedRegion(sheet, 1, 1, sheet.getLastRowNum(), wb);
		
		try {
			//默认导出到E盘下
		    FileOutputStream out = new FileOutputStream("E://会员信息表.xls");
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
