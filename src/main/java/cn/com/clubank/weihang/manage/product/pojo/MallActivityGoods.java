package cn.com.clubank.weihang.manage.product.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动商品
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MallActivityGoods {

	/**
	 * 活动商品主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	/**
	 * 添加时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date addtime;

	/**
	 * 状态 1有效 2无效
	 */
	private Integer status = 1;

	/**
	 * 活动价
	 */
	private BigDecimal price;

	/**
	 * SKUID
	 */
	private String skuId;

	/**
	 * 数量
	 */
	private Integer quantity = 0;

	/**
	 * 已售数量
	 */
	private Integer sellQuantity = 0;

	/**
	 * 活动ID
	 */
	private String activityId;

	/**
	 * 购买数量限制（普通）
	 */
	private Integer limit1;

	/**
	 * 购买数量限制（金卡）
	 */
	private Integer limit2;

	/**
	 * 购买数量限制（钻卡）
	 */
	private Integer limit3;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 创建日期
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	/**
	 * 创建用户主键
	 */
	private String createUserId;

	/**
	 * 创建用户名
	 */
	private String createUserName;

	/**
	 * 修改日期
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	/**
	 * 修改用户主键
	 */
	private String modifyUserId;

	/**
	 * 修改用户名
	 */
	private String modifyUserName;

	/**
	 * 获得剩余数量
	 * 
	 * @return
	 */
	public Integer getSurplusQuantity() {
		return quantity - sellQuantity;
	}
}