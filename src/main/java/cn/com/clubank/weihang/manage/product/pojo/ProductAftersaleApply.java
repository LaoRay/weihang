package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 售后申请表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAftersaleApply {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 申请时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date subTime;

	/**
	 * 申请人
	 */
	private String subBy;

	/**
	 * 订单详情id
	 */
	private String orderDetailId;

	/**
	 * 申请类型 1退 2换 3维修
	 */
	private Integer subType;

	/**
	 * 状态 1正在处理 2已处理 3驳回
	 */
	private Integer status = 1;

	/**
	 * 备注
	 */
	private String description;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	private String createUserId;

	private String createUserName;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	private String modifyUserId;

	private String modifyUserName;

	/**
	 * 处理人
	 */
	private String conductBy;

	/**
	 * 处理结果
	 */
	private String conductResult;

	/**
	 * 处理时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date conductTime;
	
	public static String getSubType(Integer subType){
		if(subType==1){
			return "退";
		}else if(subType==2){
			return "换";
		}else if(subType==3){
			return "维修";
		}
		return null;
	}
	
	public static String getStatus(Integer status){
		if(status==1){
			return "正在处理";
		}else if(status==2){
			return "已处理";
		}
		return null;
	}
}