package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积分流水表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegralRecord {

	/**
	 * 兑换
	 */
	public static final Integer EXCHANGE = 0;
	/**
	 * 签到
	 */
	public static final Integer SIGN = 1;
	/**
	 * 分享
	 */
	public static final Integer SHARE = 2;
	/**
	 * 消费
	 */
	public static final Integer CONSUME = 3;
	/**
	 * 充值
	 */
	public static final Integer RECHARGE = 4;
	/**
	 * 推荐
	 */
	public static final Integer RECOMMEND = 5;
	/**
	 * 注册
	 */
	public static final Integer REGISTER = 6;
	/**
	 * 广告
	 */
	public static final Integer ADVERT = 7;
	
	/**
	 * 签到-日常任务
	 */
	public static final Integer SIGN_DAILY = 10;
	/**
	 * 分享-日常任务
	 */
	public static final Integer SHARE_DAILY = 11;
	/**
	 * 推荐-日常任务
	 */
	public static final Integer RECOMMEND_DAILY = 12;
	/**
	 * 广告-日常任务
	 */
	public static final Integer ADVERT_DAILY = 13;
	/**
	 * 购买商品-日常任务
	 */
	public static final Integer BUY_DAILY = 14;

	/**
	 * 增加
	 */
	public static final Integer INCREASE = 1;
	/**
	 * 减少
	 */
	public static final Integer DECREASE = 2;

	/**
	 * 积分流水主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String recordId;

	/**
	 * 客户ID
	 */
	private String customerId;

	/**
	 * 期初积分
	 */
	private Integer beginningIntegral;

	/**
	 * 期末积分
	 */
	private Integer finishIntegral;

	/**
	 * 产生积分
	 */
	private Integer dealIntegral;

	/**
	 * 积分来源方式：0兑换 1签到 2分享 3消费 4充值 5推荐 6注册 7广告
	 */
	private Integer integralSourceType;

	/**
	 * 积分来源
	 */
	private String integralSource;

	/**
	 * 积分类型：1、增加2、减少
	 */
	private Integer integralType;

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

	public String getIntegralSourceName() {
		if (integralSourceType == EXCHANGE) {
			return "兑换";
		} else if (integralSourceType == SIGN) {
			return "签到";
		} else if (integralSourceType == SHARE) {
			return "分享";
		} else if (integralSourceType == CONSUME) {
			return "消费";
		} else if (integralSourceType == RECHARGE) {
			return "充值";
		} else if (integralSourceType == RECOMMEND) {
			return "推荐";
		} else if (integralSourceType == REGISTER) {
			return "注册";
		} else if (integralSourceType == ADVERT) {
			return "广告";
		}else if (integralSourceType == SIGN_DAILY) {
			return "签到日常任务";
		} else if (integralSourceType == SHARE_DAILY) {
			return "分享日常任务";
		}else if (integralSourceType == RECOMMEND_DAILY) {
			return "推荐日常任务";
		} else if (integralSourceType == BUY_DAILY) {
			return "商品购买日常任务";
		} else if (integralSourceType == ADVERT_DAILY) {
			return "广告日常任务";
		}
		return "";
	}
}