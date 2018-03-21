package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;

/**
 * 产品类别表
 * 
 * @author Liangwl
 *
 */
public class ProductCategory {

	/**
	 * 汽车服务
	 */
	public static final String CAR_SERVICE = "CarService";
	/**
	 * 汽车清洗
	 */
	public static final String CAR_CLEAN = "CarClean";
	/**
	 * 汽车保养
	 */
	public static final String CAR_MAINTAIN = "CarMaintain";
	/**
	 * 汽车美容
	 */
	public static final String CAR_BEAUTY = "CarBeauty";
	/**
	 * 汽车维修
	 */
	public static final String CAR_REPAIR = "CarRepair";

	/**
	 * @mbggenerated 类别ID
	 */
	@WeihColumn(WeihColumnCode.UUID)
	private String categoryId;

	/**
	 * @mbggenerated 父ID
	 */
	private String parentId;

	/**
	 * @mbggenerated 类别key
	 */
	private String categoryKey;

	/**
	 * 父分类名称（自定义属性）
	 */
	private String parentName;

	/**
	 * @mbggenerated 名称
	 */
	private String cname;

	/**
	 * @mbggenerated 排序
	 */
	private Integer sort;

	/**
	 * @mbggenerated 是否叶子节点 0不是 1是
	 */
	private Integer hasLeaf;

	/**
	 * @mbggenerated 类别状态
	 */
	private Integer categoryStatus;

	/**
	 * @mbggenerated 备注
	 */
	private String description;

	/**
	 * @mbggenerated 创建日期
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.CREATE_TIME)
	private Date createDate;

	/**
	 * @mbggenerated 创建用户主键
	 */
	private String createUserId;

	/**
	 * @mbggenerated 创建用户名
	 */
	private String createUserName;

	/**
	 * @mbggenerated 修改日期
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@WeihColumn(WeihColumnCode.UPDATE_TIME)
	private Date modifyDate;

	/**
	 * @mbggenerated 修改用户主键
	 */
	private String modifyUserId;

	/**
	 * @mbggenerated 修改用户名
	 */
	private String modifyUserName;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId == null ? null : categoryId.trim();
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId == null ? null : parentId.trim();
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getHasLeaf() {
		return hasLeaf;
	}

	public void setHasLeaf(Integer hasLeaf) {
		this.hasLeaf = hasLeaf;
	}

	public Integer getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Integer categoryStatus) {
		this.categoryStatus = categoryStatus;
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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}
}