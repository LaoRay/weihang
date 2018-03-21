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
 * 车辆账户流水表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarAccountRecord {

	/**
	 * 车辆账户流水主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String recordId;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 期初金额
	 */
	private BigDecimal beginningAmount;

	/**
	 * 期末金额
	 */
	private BigDecimal finishAmount;

	/**
	 * 交易金额
	 */
	private BigDecimal dealAmount;

	/**
	 * 金额来源类型 1充值 2消费
	 */
	private Integer accountSourceType;

	/**
	 * 消费对应得订单id
	 */
	private String accountSource;

	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private String createUserId;

	/**
	 * 车辆id
	 */
	private String carId;

	/**
	 * 账户类型(金额变化类型)1增加 2减少
	 */
	private Integer accountType;

	/**
	 * 是否集团扣费
	 */
	private Boolean isGroup = false;

	private String carIdUser;
}