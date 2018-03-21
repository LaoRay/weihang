package cn.com.clubank.weihang.common.util;

/**
 * 数据字典类型代码
 * 
 * @author YangWei
 *
 */
public enum DataItemCode {
	
	/**
	 * 预约服务类型
	 */
	BESPEAK_TYPE("reservation_type"),
	
	/**
	 * 检查项目
	 */
	INSPECTION_ITEM("inspection_item"),
	
	/**
	 * 洗车类型
	 */
	WORK_WASH_TYPE("work_wash_type"),
	
	/**
	 * 编码规则项目
	 */
	CODE_RULE_ITEM("code_rule_item"),
	
	/**
	 * 固定投保项
	 */
	FIXED_INSURANCE_ITEM("fixed_insurance_item"),
	
	/**
	 * 保险公司
	 */
	INSURANCE_COMPANY("insurance_company"),
	
	/**
	 * 编码项目
	 */
	CODE_ITEM("code_item"),
	
	/**
	 * 性别
	 */
	SEX("sex");
	
	private DataItemCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	
	private String value;
}
