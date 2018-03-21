package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户信息表
 * 
 * @author YangWei
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {

	/**
	 * 用户主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String cId;

	/**
	 * 客户状态 1正常 2失效
	 */
	private Integer status = 1;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 真实姓名
	 */
	private String realname;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 头像
	 */
	private String headicon;

	/**
	 * 简拼
	 */
	private String simpleSpelling;

	/**
	 * 性别 1男 2女
	 */
	private Integer gender;

	/**
	 * 生日
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;

	/**
	 * 手机
	 */
	private String mobile;

	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * QQ号
	 */
	private String qq;

	/**
	 * 微信号
	 */
	private String wechat;

	/**
	 * 允许多用户同时登录
	 */
	private Integer checkOnline;

	/**
	 * 第一次访问时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date firstVisit;

	/**
	 * 上一次访问时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date previousVisit;

	/**
	 * 最后访问时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastVisit;

	/**
	 * 推荐人id
	 */
	private String recommenderId;

	/**
	 * 推荐码
	 */
	private String recommendCode;

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
	 * 创建用户
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
	 * 修改用户
	 */
	private String modifyUserName;

	/**
	 * 积分账户
	 */
	private Integer integralAccount;

	/**
	 * 支付密码
	 */
	private String paymentPassword;
	
	/**
	 * 自定义字段：推荐人姓名
	 */
	private String recommenderName;
}