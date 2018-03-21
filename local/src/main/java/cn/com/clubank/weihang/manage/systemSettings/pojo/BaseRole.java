package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色信息表
 * @author Liangwl
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRole {
	
	/**
	 * 角色主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 排序码
     */
    private Integer sort;

    /**
     * 删除标记
     */
    private Integer deleteMark;

    /**
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * 备注
     */
    private String description;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    private Date modifyDate;

    private String modifyUserId;

    private String modifyUserName;

}