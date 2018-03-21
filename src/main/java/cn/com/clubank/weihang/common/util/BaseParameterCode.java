package cn.com.clubank.weihang.common.util;

/**
 * 数据字典类型代码
 * 
 * @author YangWei
 *
 */
public enum BaseParameterCode {

	/**
	 * 每位会员限制车辆数
	 */
	CUSTOMER_CAR_NUM("customer_car_num"),

	/**
	 * 日常任务可获得总积分
	 */
	DAILY_TASK_TOTAL_INTEGRAL("daily_task_total_integral");

	private BaseParameterCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	private String value;
}
