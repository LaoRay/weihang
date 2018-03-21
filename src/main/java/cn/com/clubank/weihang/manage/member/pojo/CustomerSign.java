package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签到流水表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSign {

	/**
	 * 签到表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String signId;

	/**
	 * 客户id
	 */
	private String customerId;

	/**
	 * 签到日期时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date signTime;

	/**
	 * 连续签到天数，用户前一天签到有则加1，无则清零
	 */
	private Integer sustainDay;

	/**
	 * 赠送积分
	 */
	private Integer bestowalIntegral;

	/**
	 * 备注
	 */
	private String description;
}