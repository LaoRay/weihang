package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 集团组管理
 * 
 * @author YangWei
 *
 */
public class CarGroup {
	
	/**
	 * 集团组主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String groupId;

	/**
	 * 登录账号
	 */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 集团组名称
     */
    private String groupName;

    /**
     * 删除标记
     */
    private Integer deleteMark = 0;

    /**
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * 备注
     */
    private String description;

    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    private Date modifyDate;

    private String modifyUserId;

    private String modifyUserName;

    /**
     * 组标识图片
     */
    private String groupPic;

    /**
     * 组账号车辆
     */
    private String groupCar; //集团组的虚拟车辆id

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
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

    public String getGroupPic() {
        return groupPic;
    }

    public void setGroupPic(String groupPic) {
        this.groupPic = groupPic == null ? null : groupPic.trim();
    }

    public String getGroupCar() {
        return groupCar;
    }

    public void setGroupCar(String groupCar) {
        this.groupCar = groupCar == null ? null : groupCar.trim();
    }
}