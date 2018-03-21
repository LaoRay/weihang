package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

public class BaseLog {
	
	/**
	 * 日志主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String logId;

	/**
	 * 操作时间
	 */
	@WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date operateTime;

    private String operateUserid; //手机号

    /**
     * 操作用户
     */
    private String operateName;

    private String moduleId;

    private String bId;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 浏览器
     */
    private String browser;

    private Integer executeResult;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 平台类型：1-IOS，2-安卓，3-管理端，4-网站
     */
    private Integer flatType = -1;

    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

    private String executeResultjson;

    public BaseLog(String operateUserid, String operateName, String moduleId, String bId, String ipAddress,
			String content, Integer flatType) {
		super();
		this.operateUserid = operateUserid;
		this.operateName = operateName;
		this.moduleId = moduleId;
		this.bId = bId;
		this.ipAddress = ipAddress;
		this.content = content;
		this.flatType = flatType;
	}

	public BaseLog() {
		super();
	}

	public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateUserid() {
        return operateUserid;
    }

    public void setOperateUserid(String operateUserid) {
        this.operateUserid = operateUserid == null ? null : operateUserid.trim();
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName == null ? null : operateName.trim();
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId == null ? null : moduleId.trim();
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId == null ? null : bId.trim();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser == null ? null : browser.trim();
    }

    public Integer getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(Integer executeResult) {
        this.executeResult = executeResult;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getFlatType() {
        return flatType;
    }

    public void setFlatType(Integer flatType) {
        this.flatType = flatType;
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

    public String getExecuteResultjson() {
        return executeResultjson;
    }

    public void setExecuteResultjson(String executeResultjson) {
        this.executeResultjson = executeResultjson == null ? null : executeResultjson.trim();
    }
    
    /**
     * 平台类型：1-IOS，2-安卓，3-管理端，4-网站
     * @param flatType
     */
    public String getFlatTypeStr() {
        if (this.flatType == 1) {
        	return "IOS";
        } else if (this.flatType == 2) {
        	return "安卓";
        } else if (this.flatType == 3) {
        	return "管理端";
        } else if (this.flatType == 4) {
        	return "网站";
        }
        return "";
    }
    
}