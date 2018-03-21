package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积分规则
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegralRule {

	/**
	 * 注册积分key
	 */
	public static final String REGISTER_INTEGRAL = "RegisterIntegral";
	/**
	 * 签到积分key
	 */
	public static final String SIGN_INTEGRAL = "SignIntegral";
	/**
	 * 连续签到积分key
	 */
	public static final String SUSTAINSIGN_INTEGRAL = "SustainSignIntegral";
	/**
	 * 分享积分key
	 */
	public static final String SHARE_INTEGRAL = "ShareIntegral";
	/**
	 * 推荐注册积分key
	 */
	public static final String RECOMMENDRIGISTER_INTEGRAL = "RecommendRigisterIntegral";
	/**
	 * 消费积分key
	 */
	public static final String CONSUME_INTEGRAL = "ConsumeIntegral";
	/**
	 * 充值赠送积分key
	 */
	public static final String RECHARGE_INTEGRAL = "RechargeIntegral";
	/**
	 * 广告积分key
	 */
	public static final String ADVERT_INTEGRAL = "AdvertIntegral";

	/**
	 * 有效
	 */
	public static final Integer STATUS_YES = 1;

	/**
	 * 无效
	 */
	public static final Integer STATUS_NO = 2;

	/**
	 * 积分规则主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String integralRuleId;

	/**
	 * 规则码
	 */
	private String ruleCode;

	/**
	 * 规则名称
	 */
	private String ruleName;

	/**
	 * 积分数
	 */
	private Integer ruleValue;
	
	/**
	 * 积分基数
	 */
	private Integer baseValue;

	/**
	 * 规则状态 1有效 2无效
	 */
	private Integer ruleStatus;

	/**
	 * 规则描述
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