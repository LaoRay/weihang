package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告位
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MallAdvertPosition {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	/**
	 * 位置名称
	 */
	private String positionName;

	/**
	 * 宽
	 */
	private Integer adWidth;

	/**
	 * 高
	 */
	private Integer adHeight;

	/**
	 * 位置描述
	 */
	private String positionDesc;

	/**
	 * 位置类型  1-APP 2-PC
	 */
	private Integer positionType;

	/**
	 * 备注
	 */
	private String description;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	private String createUserId;

	private String createUserName;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	private String modifyUserId;

	private String modifyUserName;

	/**
	 * 广告位编号
	 */
	private String positionCode;
	
	/**
	 * 有效标记
	 */
	private Integer enabledMark = 1;
}