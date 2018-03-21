package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 车辆品牌表
 * @author Liangwl
 *
 */
public class CarBrand {
    /**
     * @mbggenerated
     * 品牌ID
     */
	
	@WeihColumn(WeihColumnCode.UUID)
    private String carBrandId;

    /**
     * @mbggenerated
     * 品牌首字母
     */
    private String carBrandKey;

    /**
     * @mbggenerated
     * logo地址
     */
    private String carBrandLogoUrl;

    /**
     * @mbggenerated
     * 品牌名称
     */
    private String carBrandName;

    /**
     * @mbggenerated
     * 品牌状态：1有效，2无效
     */
    private Integer carBrandStatus;

    /**
     * @mbggenerated
     * 删除标记
     */
    private Integer deleteMark = 0;

    /**
     * @mbggenerated
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * @mbggenerated
     * 备注
     */
    private String description;

    /**
     * @mbggenerated
     * 创建日期
     */
    
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
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

    /**
     * @mbggenerated
     * 修改日期
     */
    
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    /**
     * @mbggenerated
     * 修改用户主键
     */
    private String modifyUserId;

    /**
     * @mbggenerated
     * 修改用户名
     */
    private String modifyUserName;

     
    public String getCarBrandId() {
        return carBrandId;
    }

     
    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId == null ? null : carBrandId.trim();
    }

     
    public String getCarBrandKey() {
        return carBrandKey;
    }

     
    public void setCarBrandKey(String carBrandKey) {
        this.carBrandKey = carBrandKey == null ? null : carBrandKey.trim();
    }

     
    public String getCarBrandLogoUrl() {
        return carBrandLogoUrl;
    }

     
    public void setCarBrandLogoUrl(String carBrandLogoUrl) {
        this.carBrandLogoUrl = carBrandLogoUrl == null ? null : carBrandLogoUrl.trim();
    }

     
    public String getCarBrandName() {
        return carBrandName;
    }

     
    public void setCarBrandName(String carBrandName) {
        this.carBrandName = carBrandName == null ? null : carBrandName.trim();
    }

     
    public Integer getCarBrandStatus() {
        return carBrandStatus;
    }

     
    public void setCarBrandStatus(Integer carBrandStatus) {
        this.carBrandStatus = carBrandStatus;
    }

     
    public Integer getDeleteMark() {
        return deleteMark;
    }

     
    public void setDeleteMark(Integer deleteMark) {
        this.deleteMark = deleteMark;
    }

     
    public Integer getEnabledMark() {
        return enabledMark;
    }

     
    public void setEnabledMark(Integer enabledMark) {
        this.enabledMark = enabledMark;
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
}