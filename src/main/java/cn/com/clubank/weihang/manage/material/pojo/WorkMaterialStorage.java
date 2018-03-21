package cn.com.clubank.weihang.manage.material.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 物料仓储表
 * @author Liangwl
 *
 */
public class WorkMaterialStorage {
    /**
     * @mbggenerated
     * 物料仓储表主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String wiId;

    /**
     * @mbggenerated
     * 分类ID
     */
    private String categoryId;

    /**
     * @mbggenerated
     * 编码
     */
    private String code;

    /**
     * @mbggenerated
     * 名称
     */
    private String itemName;

    /**
     * @mbggenerated
     * 成本价
     */
    private BigDecimal price;

    /**
     * @mbggenerated
     * 普卡价
     */
    private BigDecimal price1;

    /**
     * @mbggenerated
     * 金卡价
     */
    private BigDecimal price2;

    /**
     * @mbggenerated
     * 钻卡价
     */
    private BigDecimal price3;

    /**
     * @mbggenerated
     * 备注
     */
    private String description;

    /**
     * @mbggenerated
     * 创建日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    /**
     * @mbggenerated
     * 创建用户主键
     */
    private String createUserId;

    /**
     * @mbggenerated
     * 创建用户名
     */
    private String createUserName;

     
    public String getWiId() {
        return wiId;
    }

     
    public void setWiId(String wiId) {
        this.wiId = wiId == null ? null : wiId.trim();
    }

     
    public String getCategoryId() {
        return categoryId;
    }

     
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    
    public String getCode() {
        return code;
    }

     
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

     
    public String getItemName() {
        return itemName;
    }

     
    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    
    public BigDecimal getPrice() {
        return price;
    }

    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

     
    public BigDecimal getPrice1() {
        return price1;
    }

     
    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

     
    public BigDecimal getPrice2() {
        return price2;
    }
 
    
    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

     
    public BigDecimal getPrice3() {
        return price3;
    }

     
    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
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
}