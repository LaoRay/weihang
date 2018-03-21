package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 极光注册码信息表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpushInfo {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String uuId;

	/**
	 * 激光注册码
	 */
	private String jpushId;

	/**
	 * 手机序列号
	 */
	private String phoneId;

	/**
	 * 客户或员工ID
	 */
	private String userId;

	/**
	 * 删除标记
	 */
	private Integer deleteMark = 0;
	
	/**
	 * 平台类型1-IOS，2-安卓
	 */
	private Integer flatType = 0;

	/**
	 * 创建日期
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 修改日期
	 */
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date modifyTime;
}