package cn.com.clubank.weihang.manage.member.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 券兑换信息表
 * 
 * @author YangWei
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponList {
	/**
	 * @mbggenerated 券兑换信息主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String couponId;

	/**
	 * @mbggenerated 券名
	 */
	private String couponName;

	/**
	 * @mbggenerated 有效时间开始
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date validTimeStart;

	/**
	 * @mbggenerated 有效时间结束
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date validTimeEnd;

	/**
	 * @mbggenerated 面值
	 */
	private BigDecimal faceValue;

	/**
	 * @mbggenerated 优惠券状态：1未使用 2已使用 3已过期
	 */
	private Integer couponStatus;

	/**
	 * @mbggenerated 换购级别：1普通 2白金 3白钻 4蓝金 5蓝钻
	 */
	private Integer changeGrade;

	/**
	 * @mbggenerated 换购积分
	 */
	private Integer changeIntegral;

	/**
	 * @mbggenerated 兑换时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date changeTime;

	/**
	 * @mbggenerated 删除标记
	 */
	private Integer deleteMark = 0;

	/**
	 * @mbggenerated 有效标志
	 */
	private Integer enabledMark;

	/**
	 * @mbggenerated 备注
	 */
	private String description;

	/**
	 * @mbggenerated 创建日期
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	/**
	 * @mbggenerated 创建用户主键
	 */
	private String createUserId;

	/**
	 * @mbggenerated 创建用户名
	 */
	private String createUserName;

	/**
	 * @mbggenerated 修改日期
	 */
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;

	/**
	 * @mbggenerated 修改用户主键
	 */
	private String modifyUserId;

	/**
	 * @mbggenerated 修改用户名
	 */
	private String modifyUserName;

	/**
	 * @mbggenerated 使用时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date userTime;

	/**
	 * @mbggenerated 订单号
	 */
	private String orderNo;

	/**
	 * @mbggenerated 客户ID
	 */
	private String customerId;

	/**
	 * @mbggenerated 订单类型：1正常 2退 3换
	 */
	private Integer orderType;

	/**
	 * @mbggenerated 券码
	 */
	private String couponCode;

	/**
	 * @mbggenerated 优惠券ID
	 */
	private String couponTypeId;

	/**
	 * @mbggenerated 券兑换说明
	 */
	private String couponExplain;
}