package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

	/**
	 * 好评
	 */
	public static final Integer HIGH_COMMENT = 1;
	/**
	 * 中评
	 */
	public static final Integer MEDIUM_COMMENT = 2;
	/**
	 * 差评
	 */
	public static final Integer LOW_COMMENT = 3;

	/**
	 * 评论id
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String commentId;

	/**
	 * 订单详情id
	 */
	private String orderDetailId;

	/**
	 * 客户id
	 */
	private String customerId;

	/**
	 * 产品id
	 */
	private String productId;

	/**
	 * 父id
	 */
	private String parentId;

	/**
	 * 审核人id
	 */
	private String adminId;

	/**
	 * 审核状态1待审核2已审核
	 */
	private Integer reviewStatus = 1;

	/**
	 * 审核结果 1通过2不通过
	 */
	private Integer reviewResult;

	/**
	 * 删除标记
	 */
	private Integer deleteMark = 0;

	/**
	 * 有效标记
	 */
	private Integer enabledMark = 1;

	/**
	 * 备注
	 */
	private String description;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	private String createUserId;

	private String createUserName;

	/**
	 * 评分
	 */
	private Integer score;

	/**
	 * 评价 1好评 2中评 3差评
	 */
	private Integer assessment;

	/**
	 * 评论时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date commentTime;

	/**
	 * 评论ip
	 */
	private String commentIp;

	/**
	 * 评价内容
	 */
	private String content;
}