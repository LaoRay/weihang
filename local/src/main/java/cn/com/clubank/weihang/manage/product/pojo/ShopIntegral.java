package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 积分商城商品表
 * @author Liangwl
 *
 */
public class ShopIntegral {
    /**
     * @mbggenerated
     * 积分商品主键
     */
    private String siId;

    /**
     * @mbggenerated
     * 父ID
     */
    private String parentId;

    /**
     * @mbggenerated
     * 商品类型1、虚拟商品 2、实物商品
     */
    private Integer typeId;

    /**
     * @mbggenerated
     * 积分商品名称
     */
    private String siName;

    /**
     * @mbggenerated
     * 兑换积分
     */
    private Integer changeIntegral;

    /**
     * @mbggenerated
     * 兑换等级
     */
    private Integer changeGrade;

    /**
     * @mbggenerated
     * 兑换时间
     */
    private Date changeTime;

    /**
     * @mbggenerated
     * 商品数量
     */
    private Integer quantity;

    /**
     * @mbggenerated
     * 已兑换数量
     */
    private Integer quantityGet;

    /**
     * @mbggenerated
     * 有效时间开始
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date validTimeStart;

    /**
     * @mbggenerated
     * 有效时间结束
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date validTimeEnd;

    /**
     * @mbggenerated
     * 小图
     */
    private String picSmall;

    /**
     * @mbggenerated
     * 大图
     */
    private String picBig;

    /**
     * @mbggenerated
     * 是否叶子（最底层）节点
     */
    private Boolean hasLeaf;

    /**
     * @mbggenerated
     * 积分商品描述
     */
    private String siDescribe;

     
    public String getSiId() {
        return siId;
    }

     
    public void setSiId(String siId) {
        this.siId = siId == null ? null : siId.trim();
    }

     
    public String getParentId() {
        return parentId;
    }

    
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

     
    public Integer getTypeId() {
        return typeId;
    }

     
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    
    public String getSiName() {
        return siName;
    }

     
    public void setSiName(String siName) {
        this.siName = siName == null ? null : siName.trim();
    }
 
    
    public Integer getChangeIntegral() {
        return changeIntegral;
    }

     
    public void setChangeIntegral(Integer changeIntegral) {
        this.changeIntegral = changeIntegral;
    }

     
    public Integer getChangeGrade() {
        return changeGrade;
    }

     
    public void setChangeGrade(Integer changeGrade) {
        this.changeGrade = changeGrade;
    }

     
    public Date getChangeTime() {
        return changeTime;
    }

     
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    
    public Integer getQuantity() {
        return quantity;
    }

     
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

     
    public Integer getQuantityGet() {
        return quantityGet;
    }

     
    public void setQuantityGet(Integer quantityGet) {
        this.quantityGet = quantityGet;
    }

     
    public Date getValidTimeStart() {
        return validTimeStart;
    }

     
    public void setValidTimeStart(Date validTimeStart) {
        this.validTimeStart = validTimeStart;
    }

     
    public Date getValidTimeEnd() {
        return validTimeEnd;
    }

     
    public void setValidTimeEnd(Date validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

     
    public String getPicSmall() {
        return picSmall;
    }

     
    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall == null ? null : picSmall.trim();
    }

     
    public String getPicBig() {
        return picBig;
    }

     
    public void setPicBig(String picBig) {
        this.picBig = picBig == null ? null : picBig.trim();
    }

     
    public Boolean getHasLeaf() {
        return hasLeaf;
    }

     
    public void setHasLeaf(Boolean hasLeaf) {
        this.hasLeaf = hasLeaf;
    }

     
    public String getSiDescribe() {
        return siDescribe;
    }

     
    public void setSiDescribe(String siDescribe) {
        this.siDescribe = siDescribe == null ? null : siDescribe.trim();
    }
}