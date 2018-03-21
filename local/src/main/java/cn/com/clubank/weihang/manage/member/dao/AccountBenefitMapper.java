package cn.com.clubank.weihang.manage.member.dao;

import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户权益表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface AccountBenefitMapper {

	int deleteByPrimaryKey(String abId);

	int insert(AccountBenefit record);

	AccountBenefit selectByPrimaryKey(String abId);

	List<AccountBenefit> selectAll();

	int updateByPrimaryKey(AccountBenefit record);

	int updateSelectiveByPrimaryKey(AccountBenefit accountBenefit);

	/**
	 * 根据小于金额的第一条数据
	 * 
	 * @param account
	 * @return
	 */
	AccountBenefit getLessAccountFrist(BigDecimal account);

	/**
	 * 后台：获取会员权益总数
	 * 
	 * @return
	 */
	int selectAccountBenefitCount();

	/**
	 * 后台：获取会员权益列表
	 * 
	 * @return
	 */
	List<AccountBenefit> selectAccountBenefitList();
	/**
	 * 通过权益升级界限获得权益ID
	 * @param upgradeTotal 权益升级界限
	 * @return
	 */
	AccountBenefit selectAbId(BigDecimal upgradeTotal);
}