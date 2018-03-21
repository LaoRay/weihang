package cn.com.clubank.weihang.manage.special.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;
import cn.com.clubank.weihang.manage.special.dao.SpecialOrderMapper;
import cn.com.clubank.weihang.manage.special.dao.SpecialOrderPicMapper;
import cn.com.clubank.weihang.manage.special.pojo.SpecialOrder;
import cn.com.clubank.weihang.manage.special.pojo.SpecialOrderPic;
import cn.com.clubank.weihang.manage.special.service.ISpecialOrderService;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseCodeRule;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpecialOrderServiceImpl implements ISpecialOrderService {

	@Resource
	private SpecialOrderMapper specialOrderMapper;
	@Resource
	private BaseCodeRuleService baseCodeRuleService;
	@Resource
	private SpecialOrderPicMapper specialOrderPicMapper;
	@Resource
	private IMessageService iMessageService;
	
	@Override
	public CommonResult insertSpecialOrderInfo(JSONObject json) {
		if (json == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		SpecialOrder specialOrder=json.getObject("SpecialOrder", SpecialOrder.class);
		if (specialOrder == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		specialOrder.setOrderNo(baseCodeRuleService.getCode(BaseCodeRule.ITEM_CODE_TS));
		specialOrderMapper.insertSelective(specialOrder);
		JSONArray array=json.getJSONArray("SpecialOrderPicList");
		if(array!=null && !array.isEmpty()){
			List<SpecialOrderPic> list=JSON.parseArray(array.toJSONString(), SpecialOrderPic.class);
			for (SpecialOrderPic specialOrderPic : list) {
				specialOrderPic.setSpecialId(specialOrder.getSpecialId());
			}
			specialOrderPicMapper.insertBatch(list);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "提交成功");
	}

	@Override
	public CommonResult selectSpecialOrderList(String customerId, Integer status, Integer pageIndex, Integer pageSize) {
		PageObject<SpecialOrder> page = new PageObject<>(pageIndex, pageSize);
		int total=specialOrderMapper.selectSpecialOrderCount(customerId, status);
		List<SpecialOrder> list=specialOrderMapper.selectSpecialOrderList(customerId, status, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	@Override
	public CommonResult cancelSpecialOrder(String specialId) {
		SpecialOrder specialOrder=specialOrderMapper.selectByPrimaryKey(specialId);
		if(specialOrder==null){
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		//状态：1已申请 2待付款 3待收货 4已完成 5已驳回 6已取消
		specialOrder.setStatus(Constants.INT_6);
		specialOrderMapper.updateByPrimaryKeySelective(specialOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "取消成功");
	}

	@Override
	public CommonResult confirmReceiptSpecialOrder(String specialId) {
		SpecialOrder specialOrder=specialOrderMapper.selectByPrimaryKey(specialId);
		if(specialOrder==null){
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		//状态：1已申请 2待付款 3待收货 4已完成 5已驳回 6已取消
		specialOrder.setStatus(Constants.INT_4);
		//物流状态：1待发货 2已发货 3已签收
		specialOrder.setDeliveryStatus(Constants.INT_3);
		specialOrderMapper.updateByPrimaryKeySelective(specialOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "确认收货成功");
	}

	@Override
	public CommonResult selectSpecialOrderDetail(String specialId) {
		Map<String, Object> map = new HashMap<String, Object>();
		SpecialOrder specialOrder=specialOrderMapper.selectByPrimaryKey(specialId);
		if(specialOrder==null){
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		map.put("specialOrder", specialOrder);
		//商品附件图片
		List<SpecialOrderPic> list=specialOrderPicMapper.selectBySpecialId(specialId);
		map.put("specialOrderPicList", list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	@Override
	public CommonResult clientSelectSpecialOrderList(String orderTimeStart, String orderTimeEnd, Integer status,
			String customerName, Integer pageIndex, Integer pageSize) {
		PageObject<SpecialOrder> page = new PageObject<>(pageIndex, pageSize);
		int total=specialOrderMapper.clientSelectSpecialOrderCount(orderTimeStart, orderTimeEnd, customerName, status);
		List<SpecialOrder> list=specialOrderMapper.clientSelectSpecialOrderList(orderTimeStart, orderTimeEnd, customerName, status, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	@Override
	public CommonResult cilentEditSpecialOrder(String specialId, BigDecimal orderAmount, Date estimateTotime) {
		if (StringUtils.isBlank(specialId) || orderAmount == null || estimateTotime == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		SpecialOrder specialOrder=specialOrderMapper.selectByPrimaryKey(specialId);
		if(specialOrder==null){
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单信息不存在");
		}
		specialOrder.setOrderAmount(orderAmount);//订单金额
		specialOrder.setEstimateTotime(estimateTotime);//预计到货时间
		//状态：1已申请 2待付款 3待收货 4已完成 5已驳回 6已取消
		specialOrder.setStatus(Constants.INT_2);
		specialOrderMapper.updateByPrimaryKeySelective(specialOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public CommonResult clientDeliverySpecialOrder(String specialId) {
		if (StringUtils.isBlank(specialId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		SpecialOrder specialOrder=specialOrderMapper.selectByPrimaryKey(specialId);
		if (specialOrder == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单信息不存在");
		}
		// 发货时间
		specialOrder.setDeliveryTime(new Date());
		//物流状态：1待发货 2已发货 3已签收
		specialOrder.setDeliveryStatus(Constants.INT_2);
		//状态：1已申请 2待付款 3待收货 4已完成 5已驳回 6已取消
		specialOrder.setStatus(Constants.INT_3);
		specialOrderMapper.updateByPrimaryKeySelective(specialOrder);
		
		//消息推送
		String title = String.format("特殊订单发货通知");
		String content = String.format("【%s】特殊订单已发货，请您注意查收 。", specialOrder.getOrderNo());
		iMessageService.pushMessage(
				new MsgList(MsgList.MSGTYPE_STATION, title, content, specialOrder.getSpecialId()),
				specialOrder.getCustomerId());
		
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public CommonResult clientRejectSpecialOrder(String specialId, String turnDownDesc) {
		if (StringUtils.isBlank(specialId) || StringUtils.isBlank(turnDownDesc)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		SpecialOrder specialOrder=specialOrderMapper.selectByPrimaryKey(specialId);
		if(specialOrder == null){
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "订单信息不存在");
		}
		specialOrder.setTurnDownDesc(turnDownDesc);//驳回原因
		//状态：1已申请 2待付款 3待收货 4已完成 5已驳回 6已取消
		specialOrder.setStatus(Constants.INT_5);
		specialOrderMapper.updateByPrimaryKeySelective(specialOrder);
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public void handleSpecialOrderStatus() {
		//待付款订单三天内未支付，订单状态修改为已取消
		specialOrderMapper.handleDelayedPayOrderStatus();
		//已发货订单十天内未确认，订单状态变更为已完成
		specialOrderMapper.handleUnconfirmedReceiptOrderStatus();
	}

	@Override
	public void exportSpecialOrder(HttpServletRequest request, HttpServletResponse response) {

		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("特殊订单数据");
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
		cell.setCellValue("特殊订单号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);  
        cell.setCellValue("收货人");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("联系电话");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 3);  
        cell.setCellValue("商品名称");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("订单金额");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 5);  
        cell.setCellValue("支付方式");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("订单状态");  
        cell.setCellStyle(style);
        
        Integer status=request.getParameter("status")==""||request.getParameter("status")==null?null:Integer.parseInt(request.getParameter("status"));
		String orderTimeStart=request.getParameter("orderTimeStart");
		String orderTimeEnd=request.getParameter("orderTimeEnd");
        
		List<SpecialOrder> orderList=specialOrderMapper.exportSpecialOrder(status, orderTimeStart, orderTimeEnd);
		
        if(!orderList.isEmpty()){
        	for (short i = 0; i < orderList.size(); i++){
    			row = sheet.createRow(i + 1);
    			row.createCell(0).setCellValue(orderList.get(i).getOrderNo() == null ? "" : orderList.get(i).getOrderNo());
    			
    			String name = "";
    			String mobile = "";
    			if(StringUtils.isNotBlank(orderList.get(i).getDeliveryAddress())){
    				name = JSON.parseObject(orderList.get(i).getDeliveryAddress()).getString("name");
    				mobile = JSON.parseObject(orderList.get(i).getDeliveryAddress()).getString("mobile");
    			}
    			row.createCell(1).setCellValue(name == null ? "" : name);
    			row.createCell(2).setCellValue(mobile == null ? "" : mobile);
    			
    			row.createCell(3).setCellValue(orderList.get(i).getProductName() == null ? "" : orderList.get(i).getProductName());
    			row.createCell(4).setCellValue(orderList.get(i).getOrderAmount() == null ? "" : orderList.get(i).getOrderAmount().toString());
    			row.createCell(5).setCellValue(orderList.get(i).getPayType() == null ? "" : SpecialOrder.getPayType(orderList.get(i).getPayType()));
    			row.createCell(6).setCellValue(orderList.get(i).getStatus() == null ? "" : SpecialOrder.getOrderStatus(orderList.get(i).getStatus()));
    		}
        }
		
		try {
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName=new String(("特殊订单信息表"+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(),"iso-8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			
			OutputStream out=response.getOutputStream();
		    wb.write(out);
		    out.close();
		    wb.close();
		    log.info("导出特殊订单信息成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("导出特殊订单信息失败", e); 
		} catch (IOException e) {
		    e.printStackTrace();
		    log.error("导出特殊订单信息失败", e);
		}	
	}

}
