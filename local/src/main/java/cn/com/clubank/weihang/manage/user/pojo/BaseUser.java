package cn.com.clubank.weihang.manage.user.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {

	/**
	 * 服务主管
	 */
	public static final int DUTY_SERVICE_SUPERVISOR = 1;

	/**
	 * 服务顾问
	 */
	public static final int DUTY_SERVICE_CONSULTANT = 2;

	/**
	 * 车间组长
	 */
	public static final int DUTY_WORKSHOP_LEADER = 3;

	/**
	 * 洗车组长
	 */
	public static final int DUTY_WASH_CAR_LEADER = 4;

	/**
	 * 技师
	 */
	public static final int DUTY_TECHNICIAN = 5;

	/**
	 * 洗车师
	 */
	public static final int DUTY_WASH_CAR_DIVISION = 6;

	/**
	 * 库管
	 */
	public static final int DUTY_LIBRARY_MAMAGER = 7;

	/**
	 * @mbggenerated 用户主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String userId;
	/**
	 * @mbggenerated 登录账户
	 */
	private String account;
	/**
	 * @mbggenerated 登录密码
	 */
	private String pwd;
	/**
	 * @mbggenerated 真实姓名
	 */
	private String realname;
	/**
	 * @mbggenerated 头像
	 */
	private String headIcon;
	/**
	 * @mbggenerated 简拼
	 */
	private String simpleSpelling;
	/**
	 * @mbggenerated 性别 1男2女
	 */
	private Integer gender;
	/**
	 * @mbggenerated 生日
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date birthday;
	/**
	 * @mbggenerated 手机
	 */
	private String mobile;
	/**
	 * @mbggenerated 电话
	 */
	private String telephone;
	/**
	 * @mbggenerated 电子邮件
	 */
	private String mail;
	/**
	 * @mbggenerated QQ号
	 */
	private String qq;
	/**
	 * @mbggenerated 微信号
	 */
	private String wechat;
	/**
	 * @mbggenerated 主管主键
	 */
	private String managerId;
	/**
	 * @mbggenerated 部门主键
	 */
	private String departmentId;
	/**
	 * @mbggenerated 岗位：数据字典表
	 */
	private Integer post;
	/**
	 * @mbggenerated 职位：数据字典表
	 */
	private Integer duty = -1;
	/**
	 * @mbggenerated 工作组主键
	 */
	private String workGroupId;
	/**
	 * @mbggenerated 用户状态
	 */
	private Integer userStatus = 1;
	/**
	 * @mbggenerated 在线状态 1在线2离线
	 */
	private Integer userOnline;
	/**
	 * @mbggenerated 允许多用户同时登录 1是2否
	 */
	private Integer checkOnline;
	/**
	 * @mbggenerated 第一次访问时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date firstVisit;
	/**
	 * @mbggenerated 上一次访问时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date previousVisit;
	/**
	 * @mbggenerated 最后访问时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastVisit;
	/**
	 * @mbggenerated 登录次数
	 */
	private Integer loginCount;

	public String getDutyStr() {
		if (duty == 1) {
			return "服务主管";
		} else if (duty == 2) {
			return "服务顾问";
		} else if (duty == 3) {
			return "车间主管";
		} else if (duty == 4) {
			return "洗车组长";
		} else if (duty == 5) {
			return "技师";
		} else if (duty == 6) {
			return "洗车师";
		} else if (duty == 7) {
			return "库管";
		} else if (duty == 8) {
			return "财务";
		}
		return "未知";
	}
}