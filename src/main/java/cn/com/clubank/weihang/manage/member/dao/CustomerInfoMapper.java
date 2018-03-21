package cn.com.clubank.weihang.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;

/**
 * 客户信息表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface CustomerInfoMapper {

	int deleteByPrimaryKey(String cId);

	int insert(CustomerInfo record);

	CustomerInfo selectByPrimaryKey(String cId);

	CustomerInfo selectByMobile(String mobile);

	CustomerInfo selectByCarNo(String carNo);

	List<CustomerInfo> selectAll();

	int updateByPrimaryKey(CustomerInfo customerInfo);

	int updateSelectiveByPrimaryKey(CustomerInfo customerInfo);

	CustomerInfo selectByRecommendCode(String recommendCode);

	Map<String, String> selectCustomerLevel(String customerId);

	/**
	 * 通过会员ID获得会员积分与会员等级
	 * 
	 * @param cId
	 *            会员ID
	 * @return
	 */
	Map<String, Object> selectCustomerIntegralGradeInfo(String cId);

	/**
	 * 根据id获取姓名
	 * 
	 * @param userId
	 * @return
	 */
	String getNameById(String cId);

	/**
	 * 按会员车牌号、姓名、昵称模糊查询，会员状态查询到的条数
	 * 
	 * @param carNo
	 * 			     车牌号
	 * @param realname
	 *            会员姓名（真实姓名）
	 * @param nickname
	 *            会员昵称
	 * @param status
	 *            会员状态
	 * @return
	 */
	int selectCount(@Param("carNo") String carNo, @Param("realname") String realname, @Param("nickname") String nickname,
			@Param("status") Integer status);

	/**
	 * 按会员姓名、昵称、车牌号模糊查询，会员状态查询并分页
	 * 
	 * @param realname
	 *            会员姓名（真实姓名）
	 * @param nickname
	 *            会员昵称
	 * @param carNo
	 *            车牌号
	 * @param status
	 *            会员状态
	 * @param startIndex
	 *            起始索引
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	List<CustomerInfo> selectCustomerListByNameOrStatusPaged(@Param("realname") String realname,
			@Param("nickname") String nickname, @Param("carNo") String carNo, @Param("status") Integer status,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
	
	/**
	 * 要导出的会员表中数据
	 * @param realname 会员昵称
	 * @param nickname 会员姓名
	 * @param carNo 车牌号
	 * @param status 状态
	 * @return
	 */
	List<Map<String,Object>> exportMemberInfo(@Param("realname") String realname,
			@Param("nickname") String nickname, @Param("carNo") String carNo, @Param("status") Integer status);
}