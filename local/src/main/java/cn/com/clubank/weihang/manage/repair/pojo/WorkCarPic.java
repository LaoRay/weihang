package cn.com.clubank.weihang.manage.repair.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 车辆状态图片库
 * @author Liangwl
 *
 */
public class WorkCarPic {
    /**
     * @mbggenerated
     * 车辆状态图片库主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String cspId;

    /**
     * @mbggenerated
     * 工单ID
     */
    private String workOrderId;

    /**
     * @mbggenerated
     * 图片路径
     */
    private String picUrl;

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
     * 创建用户ID
     */
    private String createUserId;

    /**
     * @mbggenerated
     * 创建用户名
     */
    private String createUserName;

    
    public String getCspId() {
        return cspId;
    }

     
    public void setCspId(String cspId) {
        this.cspId = cspId == null ? null : cspId.trim();
    }

     
    public String getWorkOrderId() {
        return workOrderId;
    }

     
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId == null ? null : workOrderId.trim();
    }

     
    public String getPicUrl() {
        return picUrl;
    }

     
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
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