package cn.com.clubank.weihang.manage.repair.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 评价表
 * @author Liangwl
 *
 */
public class WorkEvaluate {
    /**
     * @mbggenerated
     * 评价表主键
     */
	@WeihColumn(WeihColumnCode.UUID)
    private String woeId;

    /**
     * @mbggenerated
     * 评价单号
     */
    private String evaluateNo;
    
   /**
    * @mbggenerated
    * 接车单ID
    */
   private String wpoId;

    /**
     * @mbggenerated
     * 评价人
     */
    private String evaluateBy;

    /**
     * @mbggenerated
     * 评价类型  1、洗车单2、维修单
     */
    private Integer evaluateType;

    /**
     * @mbggenerated
     * 评价时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date evaluateDate;

    /**
     * @mbggenerated
     * 有效标志
     */
    private Integer enabledMark = 1;

    /**
     * @mbggenerated
     * 审核人
     */
    private String auditorBy;

    /**
     * @mbggenerated
     * 审核时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date auditorDate;

    /**
     * @mbggenerated
     * 评价顾问
     */
    private Integer evaluateConsultant = 1;
    
    /**
     * 服务顾问ID
     */
    private String consultantId;

    public String getConsultantId() {
		return consultantId;
	}
    
    public void setConsultantId(String consultantId) {
		this.consultantId = consultantId;
	}

	/**
     * @mbggenerated
     * 评价技师
     */
    private Integer evaluateSupervisor = 1;
    
    /**
     * 技师ID
     */
    private String supervisorId;

    public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	/**
     * @mbggenerated
     * 评价洗车工
     */
    private Integer evaluateWash = 1;
    
    /**
     * 洗车师ID
     */
    private String washerId;

    public String getWasherId() {
		return washerId;
	}

	public void setWasherId(String washerId) {
		this.washerId = washerId;
	}

	/**
     * @mbggenerated
     * 状态1、待审核2、审核通过 3不通过
     */
    private Integer status = 1;

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

    /**
     * @mbggenerated
     * 评价内容
     */
    private String evaluateInfo;

     
    public String getWoeId() {
        return woeId;
    }

     
    public void setWoeId(String woeId) {
        this.woeId = woeId == null ? null : woeId.trim();
    }

    
    public String getEvaluateNo() {
        return evaluateNo;
    }

     
    public void setEvaluateNo(String evaluateNo) {
        this.evaluateNo = evaluateNo == null ? null : evaluateNo.trim();
    }

     
    public String getEvaluateBy() {
        return evaluateBy;
    }

     
    public void setEvaluateBy(String evaluateBy) {
        this.evaluateBy = evaluateBy == null ? null : evaluateBy.trim();
    }

     
    public Integer getEvaluateType() {
        return evaluateType;
    }

     
    public void setEvaluateType(Integer evaluateType) {
        this.evaluateType = evaluateType;
    }

     
    public Date getEvaluateDate() {
        return evaluateDate;
    }

     
    public void setEvaluateDate(Date evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

     
    public Integer getEnabledMark() {
        return enabledMark;
    }

     
    public void setEnabledMark(Integer enabledMark) {
        this.enabledMark = enabledMark;
    }

     
    public String getAuditorBy() {
        return auditorBy;
    }

     
    public void setAuditorBy(String auditorBy) {
        this.auditorBy = auditorBy == null ? null : auditorBy.trim();
    }

     
    public Date getAuditorDate() {
        return auditorDate;
    }

     
    public void setAuditorDate(Date auditorDate) {
        this.auditorDate = auditorDate;
    }

    
    public Integer getEvaluateConsultant() {
        return evaluateConsultant;
    }

     
    public void setEvaluateConsultant(Integer evaluateConsultant) {
        this.evaluateConsultant = evaluateConsultant;
    }

     
    public Integer getEvaluateSupervisor() {
        return evaluateSupervisor;
    }

     
    public void setEvaluateSupervisor(Integer evaluateSupervisor) {
        this.evaluateSupervisor = evaluateSupervisor;
    }

     
    public Integer getEvaluateWash() {
        return evaluateWash;
    }

     
    public void setEvaluateWash(Integer evaluateWash) {
        this.evaluateWash = evaluateWash;
    }

     
    public Integer getStatus() {
        return status;
    }

      
    public void setStatus(Integer status) {
        this.status = status;
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

     
    public String getEvaluateInfo() {
        return evaluateInfo;
    }

     
    public void setEvaluateInfo(String evaluateInfo) {
        this.evaluateInfo = evaluateInfo == null ? null : evaluateInfo.trim();
    }

	public String getWpoId() {
		return wpoId;
	}

	public void setWpoId(String wpoId) {
		this.wpoId = wpoId;
	}
	
	/**
	 * 自定义参数:服务时间
	 */
	private Date serviceDate;

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	

}