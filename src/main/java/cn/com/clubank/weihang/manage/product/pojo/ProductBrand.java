package cn.com.clubank.weihang.manage.product.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品品牌
 * @author YangWei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrand {
	
	/**
	 * 品牌Id
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String brandId;

	/**
	 * 品牌首字母
	 */
    private String brandKey;

    /**
     * logo地址
     */
    private String brandLogoUrl;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌状态 1:有效 2:无效
     */
    private Integer brandStatus = 1;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    /**
     * 创建用户主键
     */
    private String createUserId;

    /**
     * 创建用户名
     */
    private String createUserName;

    /**
     * 修改日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    private Date modifyDate;

    /**
     * 修改用户主键
     */
    private String modifyUserId;

    /**
     * 修改用户名
     */
    private String modifyUserName;

}