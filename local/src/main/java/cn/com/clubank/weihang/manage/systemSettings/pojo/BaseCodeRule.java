package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 编号规则项目表
 * @author YangWei
 *
 */
public class BaseCodeRule {
	
	/**
	 * 预约单
	 */
	public static final String ITEM_CODE_YY = "YY";
	
	/**
	 * 接车单
	 */
	public static final String ITEM_CODE_JC = "JC";
	
	/**
	 * 维修单
	 */
	public static final String ITEM_CODE_WX = "WX";
	
	/**
	 * 洗车单
	 */
	public static final String ITEM_CODE_XC = "XC";
	
	/**
	 * 商品订单
	 */
	public static final String ITEM_CODE_SP = "SP";
	
	/**
	 * 优惠券码
	 */
	public static final String COUPON_CODE = "YHQ";
	
	/**
	 * 编码规则主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String ruleId;

    /**
     * 编码项目编号
     */
    private String itemCode;

    /**
     * 编码项目
     */
    private String item;

    /**
     * 名称
     */
    private String name;

    /**
     * 当前流水号
     */
    private String currentUumber;

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
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
     * 规则格式Json
     */
    private String ruleFormatjson;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCurrentUumber() {
        return currentUumber;
    }

    public void setCurrentUumber(String currentUumber) {
        this.currentUumber = currentUumber == null ? null : currentUumber.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId == null ? null : modifyUserId.trim();
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName == null ? null : modifyUserName.trim();
    }

    public String getRuleFormatjson() {
        return ruleFormatjson;
    }

    public void setRuleFormatjson(String ruleFormatjson) {
		this.ruleFormatjson = ruleFormatjson == null ? null : ruleFormatjson.trim();
	}
}