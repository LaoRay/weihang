package cn.com.clubank.weihang.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 合并单元格
 * @author Liangwl
 *
 */
public class MergeCellsUtil {
	
	/**  
	 * 合并会员信息表中单元格  
	 * @param sheet 要合并单元格的excel 的sheet
	 * @param cellLine  要合并的列  
	 * @param startRow  要合并列的开始行  
	 * @param endRow    要合并列的结束行  
	 */  
    public static void addMemberInfoMergedRegion(HSSFSheet sheet, int cellLine, int startRow, int endRow,HSSFWorkbook workBook){
    	HSSFCellStyle style = workBook.createCellStyle(); // 样式对象    

    	//style.setAlignment(HorizontalAlignment.CENTER);//水平居中
    	style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

    	//获取第一行的数据,以便后面进行比较    
    	String s_will = sheet.getRow(startRow).getCell(cellLine).getStringCellValue();   

    	int a=0;
    	int b=0;//统计重复次数
    	int count = 0;
    	boolean flag = false;
    	for (int i = 2; i <= endRow; i++) {   
    		String s_current = sheet.getRow(i).getCell(cellLine).getStringCellValue(); 
    		if(s_will.equals(s_current)){
    			b++;
    			//如果第一行不等于第二行，且是第一次重复的，就让flag继续等于false
    			if(sheet.getRow(1).getCell(cellLine).getStringCellValue()!=sheet.getRow(2).getCell(cellLine).getStringCellValue()&&b==1){
    				flag=false;
    			}
    			s_will = s_current;
    			if(flag){
    				
    				if(startRow-count<startRow){
    					//合并第二列中重复的数据
    					sheet.addMergedRegion(new CellRangeAddress(startRow-count,startRow,cellLine,cellLine));
    					HSSFRow row = sheet.getRow(startRow-count);
        				String cellValueTemp = row.getCell(cellLine).getStringCellValue(); 
        				HSSFCell cell = row.createCell(cellLine);
        				cell.setCellValue(cellValueTemp); // 跨单元格显示的数据    
        				cell.setCellStyle(style); // 样式    
        				
        				//通过第二列合并第一列中重复的数据
        				sheet.addMergedRegion(new CellRangeAddress(startRow-count,startRow,0,0));
        				String cellValueTempOne = row.getCell(0).getStringCellValue();
        				HSSFCell cellOne = row.createCell(0);
        				cellOne.setCellValue(cellValueTempOne);
        				cellOne.setCellStyle(style);
        				
        				count = 0;
        				flag = false;
    				}
					
    			}
    			a=endRow-i;//最后有几个不重复的数据
    			startRow=i;
    			count++;          
    		}else{
    			flag = true;
    			s_will = s_current;
    		}

    		//由于上面循环中合并的单元放在有下一次相同单元格的时候做的，所以最后如果几行有相同单元格则要运行下面的合并单元格。
    		if(i==endRow&&count>0){
    				sheet.addMergedRegion(new CellRangeAddress(endRow-count-a,endRow-a,cellLine,cellLine)); 
        			HSSFRow row = sheet.getRow(startRow-count);
        			String cellValueTemp = row.getCell(cellLine).getStringCellValue(); 
        			HSSFCell cell = row.createCell(cellLine);
        			cell.setCellValue(cellValueTemp); // 跨单元格显示的数据    
        			cell.setCellStyle(style); // 样式   

        			sheet.addMergedRegion(new CellRangeAddress(endRow-count-a,endRow-a,0,0));
    				String cellValueTempOne = row.getCell(0).getStringCellValue();
    				HSSFCell cellOne = row.createCell(0);
    				cellOne.setCellValue(cellValueTempOne);
    				cellOne.setCellStyle(style);
    		}
    	}
    }
    
    
	/**  
	 * 合并商品订单信息表单元格  
	 * @param sheet 要合并单元格的excel 的sheet
	 * @param cellLine  要合并的列  
	 * @param startRow  要合并列的开始行  
	 * @param endRow    要合并列的结束行  
	 */  
    public static void addProductOrderMergedRegion(HSSFSheet sheet, int cellLine, int startRow, int endRow,HSSFWorkbook workBook){
    	HSSFCellStyle style = workBook.createCellStyle(); // 样式对象    

    	//style.setAlignment(HorizontalAlignment.CENTER);//水平居中
    	style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

    	//获取第一行的数据,以便后面进行比较    
    	String s_will = sheet.getRow(startRow).getCell(cellLine).getStringCellValue();   

    	int a=0;
    	int b=0;//统计重复次数
    	int count = 0;
    	boolean flag = false;
    	for (int i = 2; i <= endRow; i++) {   
    		String s_current = sheet.getRow(i).getCell(cellLine).getStringCellValue(); 
    		if(s_will.equals(s_current)){
    			b++;
    			//如果第一行不等于第二行，且是第一次重复的，就让flag继续等于false
    			if(sheet.getRow(1).getCell(cellLine).getStringCellValue()!=sheet.getRow(2).getCell(cellLine).getStringCellValue()&&b==1){
    				flag=false;
    			}
    			s_will = s_current;
    			if(flag){
    				
    				if(startRow-count<startRow){
    					//合并第一列中重复的数据
    					sheet.addMergedRegion(new CellRangeAddress(startRow-count,startRow,cellLine,cellLine));
    					HSSFRow row = sheet.getRow(startRow-count);
        				String cellValueTemp = row.getCell(cellLine).getStringCellValue(); 
        				HSSFCell cell = row.createCell(cellLine);
        				cell.setCellValue(cellValueTemp); // 跨单元格显示的数据    
        				cell.setCellStyle(style); // 样式    
        				
        				//通过第一列合并第二、三、四、五、六、七列中重复的数据
        				for(int j=1;j<=6;j++){
        					sheet.addMergedRegion(new CellRangeAddress(startRow-count,startRow,j,j));
            				String cellValueTempOne = row.getCell(j).getStringCellValue();
            				HSSFCell cellOne = row.createCell(j);
            				cellOne.setCellValue(cellValueTempOne);
            				cellOne.setCellStyle(style);
        				}
        				
        				count = 0;
        				flag = false;
    				}
					
    			}
    			a=endRow-i;//最后有几个不重复的数据
    			startRow=i;
    			count++;          
    		}else{
    			flag = true;
    			s_will = s_current;
    		}

    		//由于上面循环中合并的单元放在有下一次相同单元格的时候做的，所以最后如果几行有相同单元格则要运行下面的合并单元格。
    		if(i==endRow&&count>0){
    				sheet.addMergedRegion(new CellRangeAddress(endRow-count-a,endRow-a,cellLine,cellLine)); 
        			HSSFRow row = sheet.getRow(startRow-count);
        			String cellValueTemp = row.getCell(cellLine).getStringCellValue(); 
        			HSSFCell cell = row.createCell(cellLine);
        			cell.setCellValue(cellValueTemp); // 跨单元格显示的数据    
        			cell.setCellStyle(style); // 样式   

        			for(int j=1;j<=6;j++){
        				sheet.addMergedRegion(new CellRangeAddress(endRow-count-a,endRow-a,j,j));
        				String cellValueTempOne = row.getCell(j).getStringCellValue();
        				HSSFCell cellOne = row.createCell(j);
        				cellOne.setCellValue(cellValueTempOne);
        				cellOne.setCellStyle(style);
    				}
    			
    		}
    	}
    }
}
