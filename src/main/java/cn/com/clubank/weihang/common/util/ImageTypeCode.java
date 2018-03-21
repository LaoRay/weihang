package cn.com.clubank.weihang.common.util;

/**
 * 图片类型及对应的路径
 * 
 * @author YangWei
 *
 */
public enum ImageTypeCode {

	/**
	 * 客户头像
	 */
	CUSTOMER(1, "customer"),

	/**
	 * 用户头像
	 */
	USER(2, "user"),

	/**
	 * 车辆状态
	 */
	CAR(3, "car"),

	/**
	 * 品牌logo
	 */
	BRAND(4, "brand"),

	/**
	 * 产品图片
	 */
	PRODUCT(5, "product"),

	/**
	 * 广告图片
	 */
	ADVERT(6, "advert"),

	/**
	 * 优惠券
	 */
	COUPON(7, "coupon"),

	/**
	 * 保险
	 */
	INSURANCE(8, "insurance"),
	
	/**
	 * 特殊订单
	 */
	SPECIAAL(9, "special");

	/**
	 * 用户头像
	 */
	private int type;
	private String path;

	private ImageTypeCode(int type, String path) {
		this.type = type;
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 根据类型返回类型的路径
	 */
	public static String getPathByType(int type) {
		for (ImageTypeCode info : ImageTypeCode.values()) {
			if (info.getType() == type) {
				return info.getPath();
			}
		}
		return "unknown";
	}
}
