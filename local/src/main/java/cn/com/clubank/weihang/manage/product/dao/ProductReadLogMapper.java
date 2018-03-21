package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductReadLog;

/**
 * 浏览记录表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface ProductReadLogMapper {

    int deleteByPrimaryKey(String id);

    int insert(ProductReadLog record);

    int insertSelective(ProductReadLog record);
    
    List<ProductReadLog> selectAll();

    ProductReadLog selectByPrimaryKey(String id);
    
    ProductReadLog selectBySkuId(String skuId);
    
    /**
     * 通过客户ID查询浏览记录并分页
     * @param customerId 客户ID
     * @param startIndex 起始索引
     * @param pageSize 每页行数
     * @return
     */
    List<Map<String,Object>> selectByCustomerId(@Param("customerId") String customerId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);

    int updateByPrimaryKeySelective(ProductReadLog record);

    int updateByPrimaryKey(ProductReadLog record);
}