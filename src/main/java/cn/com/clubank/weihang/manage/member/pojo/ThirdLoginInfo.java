package cn.com.clubank.weihang.manage.member.pojo;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方登录信息
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdLoginInfo {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	/**
	 * 客户id
	 */
	private String customerId;

	/**
	 * 第三方id
	 */
	private String thirdId;

	/**
	 * 第三方应用类型1QQ2微信3微博
	 */
	private Integer thirdType;
}