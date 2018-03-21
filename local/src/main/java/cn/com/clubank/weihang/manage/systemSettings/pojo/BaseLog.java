package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统日志表
 * @author Liangwl
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseLog {
	
	/**
	 * 日志主键
	 */
    private String logId;

    /**
     * 操作时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date operateTime;

    /**
     * 操作用户ID
     */
    private String operateUserid;

    /**
     * 操作用户
     */
    private String operateName;

    /**
     * 系统模块主键
     */
    private String moduleId;

    /**
     * 按钮主键
     */
    private String bId;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 执行结果状态
     */
    private Integer executeResult;

    /**
     * 创建日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

    /**
     * 执行结果信息
     */
    private String executeResultjson;
}