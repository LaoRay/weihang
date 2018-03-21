package cn.com.clubank.weihang.manage.member.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券表
 * 
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponInventory {

	/**
	 * 优惠券主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String crId;

	/**
	 * 券名
	 */
	private String couponName;

	/**
	 * 券类型 1服务券 2兑换券 3抵扣券 4折扣券
	 */
	private Integer couponType;

	/**
	 * 服务类券类型
	 */
	private Integer serviceCouponType;

	/**
	 * 券数量
	 */
	private Integer couponQuantity = 0;

	/**
	 * 已领取数量
	 */
	private Integer couponQuantityGet = 0;

	/**
	 * 有效时间开始
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date validTimeStart;

	/**
	 * 有效时间结束
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date validTimeEnd;

	/**
	 * 面值
	 */
	private BigDecimal faceValue;

	/**
	 * 换购级别 1普通 2白金 3白钻 4蓝金 5蓝钻
	 */
	private Integer changeGrade = 1;

	/**
	 * 换购积分
	 */
	private Integer changeIntegral = 0;

	/**
	 * 小图
	 */
	private String picSmall;

	/**
	 * 大图
	 */
	private String picBig;

	/**
	 * 商品skuId
	 */
	private String skuId;

	/**
	 * 商品id
	 */
	private String productId;

	/**
	 * 折扣
	 */
	public BigDecimal discount;

	/**
	 * 删除标记
	 */
	private Integer deleteMark = 0;

	/**
	 * 有效标志 1待审核 2有效 3无效
	 */
	private Integer enabledMark = 1;

	/**
	 * 备注
	 */
	private String description;

	/**
	 * 创建日期
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;

	/**
	 * 修改用户主键
	 */
	private String modifyUserId;

	/**
	 * 修改用户名
	 */
	private String modifyUserName;

	/**
	 * 券说明
	 */
	private String couponExplain;

	public Integer getStatus() {
		if (new Date().before(validTimeStart)) {// 当前日期小于开始日期
			return 1;
		} else if (new Date().before(validTimeEnd)) {// 当前日期大于开始日期小于结束日期
			return 2;
		} else {
			return 3;
		}
	}
}