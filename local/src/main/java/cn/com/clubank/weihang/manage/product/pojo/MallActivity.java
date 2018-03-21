package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MallActivity {

	/**
	 * 活动ID
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	/**
	 * 添加时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date addtime;

	/**
	 * 活动状态1未开始 2即将开始(开始前1小时) 3进行中 4已结束 5已失效(结束后1小时)
	 */
	private Integer status = 1;

	/**
	 * 开始时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date beginTime;

	/**
	 * 结束时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 活动编号
	 */
	private String sequenceCode;

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
	 * 修改用户
	 */
	private String modifyUserName;

	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 活动等级 1普通 2白金3蓝金4白钻 5蓝钻
	 */
	private Integer grade = 1;
}