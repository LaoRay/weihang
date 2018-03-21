package cn.com.clubank.weihang.manage.message.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgList {

	/**
	 * 站内通知
	 */
	public static final Integer MSGTYPE_STATION = 1;

	/**
	 * 商品通知
	 */
	public static final Integer MSGTYPE_PRODUCT = 2;

	/**
	 * 洗车单通知
	 */
	public static final Integer MSGTYPE_WASH = 3;

	/**
	 * 维修单通知
	 */
	public static final Integer MSGTYPE_REPAIR = 4;
	
	/**
	 * 接车单通知
	 */
	public static final Integer MSGTYPE_RECEIVE = 5;

	/**
	 * 消息表主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String msgId;

	/**
	 * 消息类型1、站内通知 2、商品通知 3、洗车单通知 4、维修单通知
	 */
	private Integer msgType;

	/**
	 * 标题
	 */
	private String title;

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
	 * 创建用户ID
	 */
	private String createUserId;

	/**
	 * 创建用户名
	 */
	private String createUserName;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 对象ID：洗车单id；维修单id；商品id
	 */
	private String objId;

	public MsgList(Integer msgType, String title, String content, String objId) {
		super();
		this.msgType = msgType;
		this.title = title;
		this.content = content;
		this.objId = objId;
	}
	
}