package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收货地址表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeliveryAddress {

	/**
	 * 收货地址表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String daId;

	/**
	 * 客户ID
	 */
	private String customerId;

	/**
	 * 接收人
	 */
	private String consigneeName;

	/**
	 * 联系手机
	 */
	private String consigneeMobile;

	/**
	 * 省主键
	 */
	private String provinceId;

	/**
	 * 市主键
	 */
	private String cityId;

	/**
	 * 县/区主键
	 */
	private String countyId;

	/**
	 * 详细地址
	 */
	private String address;

	/**
	 * 地址全名
	 */
	private String addressAll;

	/**
	 * 邮编
	 */
	private String postcode;

	/**
	 * 是否默认
	 */
	private Boolean isDefault = false;

	/**
	 * 排序码
	 */
	private Integer sort;

	/**
	 * 删除标记
	 */
	private Integer deleteMark = 0;

	/**
	 * 有效标志
	 */
	private Integer enabledMark;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 创建日期
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;

	/**
	 * 修改用户主键
	 */
	private String modifyUserId;

	/**
	 * 修改用户名
	 */
	private String modifyUserName;
}