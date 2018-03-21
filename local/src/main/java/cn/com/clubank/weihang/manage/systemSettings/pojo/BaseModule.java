package cn.com.clubank.weihang.manage.systemSettings.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统模块表
 * @author Liangwl
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModule {
	
	/**
	 * 模块主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String moduleId;

    /**
     * 父级主键
     */
    private String parentId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 图标
     */
    private String icon;

    /**
     * 导航地址
     */
    private String url;

    /**
     * 导航目标
     */
    private String target;

    /**
     * 菜单选项
     */
    private Integer isMenu;

    /**
     * 允许展开
     */
    private Integer allowExpand;

    /**
     * 是否公开
     */
    private Integer isPublic;

    /**
     * 允许编辑
     */
    private Integer allowEdit;

    /**
     * 允许删除
     */
    private Integer allowDelete;

    /**
     * 排序码
     */
    private Integer sort;

    /**
     * 删除标记
     */
    private Integer deleteMark=0;

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