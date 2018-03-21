package cn.com.clubank.weihang.manage.product.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.pay.service.WordRefundService;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductAftersaleApplyMapper;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.product.pojo.ProductAftersaleApply;
import cn.com.clubank.weihang.manage.product.service.IProductAftersaleApplyService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;

/**
 * 售后申请管理
 * 
 * @author LeiQY
 *
 */
@Slf4j
@Service
public class ProductAftersaleApplyServiceImpl implements IProductAftersaleApplyService {

	@Autowired
	private ProductAftersaleApplyMapper productAftersaleApplyMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private BaseCodeRuleService baseCodeRuleService;
	@Autowired
	private WordRefundService wordRefundService;
	@Autowired
	private OrderListMapper orderListMapper;

	/**
	 * 保存售后申请信息
	 */
	@Override
	public CommonResult insertApplyInfo(ProductAftersaleApply apply) {
		if (StringUtil.isBlank(apply.getOrderDetailId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		apply.setOrderNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_SH));
		productAftersaleApplyMapper.insert(apply);
		//修改订单详情已申请售后
		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(apply.getOrderDetailId());
		if (orderDetail != null) {
			orderDetail.setIsSaleService(true);
			orderDetailMapper.updateByPrimaryKey(orderDetail);
			//修改订单状态为售后申请
			OrderList order = orderListMapper.selectByPrimaryKey(orderDetail.getOrderId());
			if (order != null) {
				order.setOrderStatus(12);
				orderListMapper.updateByPrimaryKey(order);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "申请退货成功，工作人员会在近期联系您，请保持手机畅通，谢谢您的支持！");
	}

	/**
	 * 查询售后申请列表
	 */
	@Override
	public CommonResult findApplyInfoList(String customerId, Integer pageIndex, Integer pageSize) {
		List<Map<String, String>> applyInfoList = productAftersaleApplyMapper.selectListByCustomerId(customerId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询售后申请列表成功", applyInfoList);
	}

	/**
	 * 后台：查询售后申请列表
	 */
	@Override
	public CommonResult clientFindApplyInfoList(Integer pageIndex, Integer pageSize, Integer status, String startDate,
			String endDate) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		Integer total = productAftersaleApplyMapper.selectApplyCount(status, startDate, endDate);
		page.setRows(total);
		List<Map<String, String>> infoList = productAftersaleApplyMapper.selectApplyInfoList(page.getStart(),
				page.getPageSize(), status, startDate, endDate);
		page.setDataList(infoList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询售后申请列表成功", page);
	}

	/**
	 * 后台：处理售后申请
	 */
	@Override
	public CommonResult clientHandleApplyByApplyId(String conductBy, String applyId, String conductResult) {
		if (StringUtils.isBlank(applyId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		ProductAftersaleApply apply = productAftersaleApplyMapper.selectByPrimaryKey(applyId);
		if (apply.getSubType() == 1) {
			//如果是退货，则原路退款
			CommonResult refundResult = wordRefundService.refundOrderDetail(apply.getOrderDetailId());
			if (refundResult.getApiStatus() != ResultCode.SUCC.getValue()) {
				return refundResult;
			}
		}
		// 处理售后申请，将状态置为已处理
		apply.setStatus(Constants.INT_2);
		apply.setConductBy(conductBy);
		apply.setConductResult(conductResult);
		productAftersaleApplyMapper.updateByPrimaryKey(apply);
		return CommonResult.result(ResultCode.SUCC.getValue(), "处理成功");
	}
	
	@Override
	public CommonResult clientRejectApplyByApplyId(String conductBy, String applyId, String conductResult) {
		if (StringUtils.isBlank(applyId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		ProductAftersaleApply apply = productAftersaleApplyMapper.selectByPrimaryKey(applyId);
		if (apply == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "未找到售后申请数据");
		}
		// 处理售后申请，将状态置为已处理
		apply.setStatus(Constants.INT_3);
		apply.setConductBy(conductBy);
		apply.setConductResult(conductResult);
		productAftersaleApplyMapper.updateByPrimaryKey(apply);
		return CommonResult.result(ResultCode.SUCC.getValue(), "处理成功");
	}

	@Override
	public void exportAftersaleApplyInfo(HttpServletRequest request, HttpServletResponse response) {
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

        Integer status=request.getParameter("status")==""||request.getParameter("status")==null?null:Integer.parseInt(request.getParameter("status")); 
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		
        List<Map<String,Object>> aftersaleApplyList=productAftersaleApplyMapper.exportAftersaleApplyInfo(status, startDate, endDate);
		
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        if(!aftersaleApplyList.isEmpty()){
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
        }
		
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName=new String(("售后申请信息表"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),"iso-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			
			OutputStream out=response.getOutputStream();
		    wb.write(out);
		    out.close();
		    wb.close();
		    log.info("导出售后申请信息成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("导出售后申请信息失败", e);
		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("导出售后申请信息失败", e);
		}
	}
}
