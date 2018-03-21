package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MallAdvert {

	/**
	 * 主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	/**
	 * 位置类型 1-APP 2-PC
	 */
	private Integer positionType;

	/**
	 * 广告类型 1-链接广告、2-商品广告
	 */
	private Integer adType;

	/**
	 * 广告名称
	 */
	private String adName;

	/**
	 * 链接地址
	 */
	private String adLink;
	
	/**
	 * 对象id 商品广告时是商品id
	 */
	private String objId;

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
	 * 点击数
	 */
	private Integer clickCount = 0;

	/**
	 * 广告状态 1未开始 2进行中 3已结束 4已关闭
	 */
	private Integer status = 1;

	/**
	 * 联系人
	 */
	private String linkMan;

	/**
	 * 联系电话
	 */
	private String linkPhone;

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
	 * 图片
	 */
	private String img;

	/**
	 * 广告位编号
	 */
	private String positionCode;
}