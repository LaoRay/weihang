package cn.com.clubank.weihang.manage.product.pojo;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 品牌类别关系表
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrandRelation {

	@WeihColumn(WeihColumnCode.UUID)
	private String id;

	private String categoryId;

	private String brandId;
}