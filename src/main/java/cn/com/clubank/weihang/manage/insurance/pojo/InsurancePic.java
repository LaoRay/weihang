package cn.com.clubank.weihang.manage.insurance.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保险单涉及的图片信息
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePic {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String picId;

	/**
	 * 保险单id
	 */
	private String insuranceId;

	/**
	 * 图片路径
	 */
	private String picUrl;

	/**
	 * 1-身份证正面 2-身份证反面 3-行驶证 4-车辆照片 5-保单照片
	 */
	private Integer type;

	/**
	 * 备注
	 */
	private String description;

	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private String createUserId;

	private String createUserName;
}