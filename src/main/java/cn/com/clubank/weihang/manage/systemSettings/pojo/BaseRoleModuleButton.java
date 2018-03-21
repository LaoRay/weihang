package cn.com.clubank.weihang.manage.systemSettings.pojo;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色模块按钮表
 * @author Liangwl
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRoleModuleButton {
	
	/**
	 * 角色模块按钮主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String brmbId;

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 模块主键
     */
    private String moduleId;

    /**
     * 按钮主键
     */
    private String bId;
}