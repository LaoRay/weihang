package cn.com.clubank.weihang.manage.insurance.pojo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保险单
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceOrder {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String insuranceId;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 车辆主键
	 */
	private String carId;

	/**
	 * 车牌号
	 */
	private String carNo;

	/**
	 * 下单时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date subTime;

	/**
	 * 下单人id
	 */
	private String subId;

	/**
	 * 下单人名
	 */
	private String subName;

	/**
	 * 下单人电话
	 */
	private String subMobile;

	/**
	 * 固定保险项
	 */
	private String fixedAmount;

	/**
	 * 机动车第三方责任险
	 */
	private String changeAmount1;

	public String getChangeAmount1Name() {
		if (StringUtils.isBlank(changeAmount1)) {
			return null;
		}
		return "机动车第三方责任险";
	}

	/**
	 * 玻璃单独破碎险
	 */
	private String changeAmount2;

	public String getChangeAmount2Name() {
		if (StringUtils.isBlank(changeAmount2)) {
			return null;
		}
		return "玻璃单独破碎险";
	}

	/**
	 * 车身划痕损失险
	 */
	private String changeAmount3;

	public String getChangeAmount3Name() {
		if (StringUtils.isBlank(changeAmount3)) {
			return null;
		}
		return "车身划痕损失险";
	}

	/**
	 * 机动车车上人员责任保险（司机）
	 */
	private String changeAmount4;

	public String getChangeAmount4Name() {
		if (StringUtils.isBlank(changeAmount4)) {
			return null;
		}
		return "机动车车上人员责任保险（司机）";
	}

	/**
	 * 机动车车上人员责任保险（单名乘客）
	 */
	private String changeAmount5;

	public String getChangeAmount5Name() {
		if (StringUtils.isBlank(changeAmount5)) {
			return null;
		}
		return "机动车车上人员责任保险（单名乘客）";
	}

	/**
	 * 投保公司名称
	 */
	private String companyName;

	/**
	 * 状态 1已申请 2待验车 3待确认 4待付款 5待领取 6已完成 7已驳回 8已取消
	 */
	private Integer status = 1;

	/**
	 * 发货时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date deliveryTime;

	/**
	 * 支付时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

	/**
	 * 配送地址
	 */
	private String deliveryAddress;

	/**
	 * 配送类型 1自取 2邮寄
	 */
	private Integer deliveryType = 1;

	/**
	 * 物流状态 1待发货 2已发货 3已签收
	 */
	private Integer deliveryStatus;

	/**
	 * 实付金额
	 */
	private BigDecimal orderPayAmount = new BigDecimal(0);

	/**
	 * 保险金额（普）
	 */
	private BigDecimal amount1 = new BigDecimal(0);

	/**
	 * 保险金额（金）
	 */
	private BigDecimal amount2 = new BigDecimal(0);

	/**
	 * 保险金额（钻）
	 */
	private BigDecimal amount3 = new BigDecimal(0);

	/**
	 * 支付类型 1账户 2支付宝 3微信 4现金 5刷卡
	 */
	private Integer payType;

	/**
	 * 备注
	 */
	private String description;

	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private String createUserId;

	private String createUserName;

	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;

	private String modifyUserId;

	private String modifyUserName;
}