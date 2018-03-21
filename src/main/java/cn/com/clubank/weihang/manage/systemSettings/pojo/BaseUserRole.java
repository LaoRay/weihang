package cn.com.clubank.weihang.manage.systemSettings.pojo;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 人员角色表
 * @author Liangwl
 *
 */
public class BaseUserRole {
	
	/**
	 * 人员角色主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String urId;

    /**
     * 人员主键
     */
    private String userId;

    /**
     * 角色主键
     */
    private String roleId;

    public String getUrId() {
        return urId;
    }

    public void setUrId(String urId) {
        this.urId = urId == null ? null : urId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}