package cn.com.clubank.weihang.manage.message.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息关系表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgRelation {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String mrId;

	/**
	 * 客户或用户ID
	 */
	private String targetId;

	/**
	 * 消息表ID
	 */
	private String msgId;

	/**
	 * 删除标记
	 */
	private Integer deleteMark = 0;

	/**
	 * 状态：0 未读，1 已读
	 */
	private Integer status = 0;

	/**
	 * 是否公开：0 未公开（员工），1 公开（客户）
	 */
	private Integer isPublic;

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