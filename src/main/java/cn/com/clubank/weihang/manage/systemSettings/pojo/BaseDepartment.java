package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 机构部门表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDepartment {
	/**
	 * 部门主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String dId;

	/**
	 * 父级主键
	 */
	private String parentId;

	/**
	 * 部门名称
	 */
	private String dName;

	/**
	 * 部门简称
	 */
	private String shortName;

	/**
	 * 负责人主键
	 */
	private String managerId;

	/**
	 * 负责人
	 */
	private String manager;

	/**
	 * 外线电话
	 */
	private String outerPhone;

	/**
	 * 内线电话
	 */
	private String innerPhone;

	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * 部门传真
	 */
	private String fax;

	/**
	 * 排序码
	 */
	private Integer sort;

	/**
	 * 成立时间
	 */

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date foundedTime;

	/**
	 * 经营范围
	 */
	private String businessScope;

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
	 * 公司主页
	 */
	private String webaddress;

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
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
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
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
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