package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动商品购买记录表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MallActivityPurRecord {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String prId;

	/**
	 * 活动id
	 */
	private String activityId;

	/**
	 * 活动商品id
	 */
	private String activityGoodsId;

	/**
	 * 购买数量
	 */
	private Integer quantity = 0;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 客户id
	 */
	private String customerId;

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
}