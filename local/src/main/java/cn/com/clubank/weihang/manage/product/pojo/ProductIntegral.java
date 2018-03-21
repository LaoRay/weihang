package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品积分表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductIntegral {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String productIntegralId;

	/**
	 * skuId
	 */
	private String skuId;

	/**
	 * 积分数
	 */
	private Integer integralValue;

	/**
	 * 状态 1有效 2无效
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
}