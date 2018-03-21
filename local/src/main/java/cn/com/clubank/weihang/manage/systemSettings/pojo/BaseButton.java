package cn.com.clubank.weihang.manage.systemSettings.pojo;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 按钮表
 * @author Liangwl
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseButton {
	
	/**
	 * 按钮主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String bId;

    /**
     * 模块主键
     */
    private String moduleId;

    /**
     * 父级主键
     */
    private String parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 按钮名称
     */
    private String buttonName;

    /**
     * Action地址
     */
    private String actionAddress;

    /**
     * 排序码
     */
    private Integer sort;
}