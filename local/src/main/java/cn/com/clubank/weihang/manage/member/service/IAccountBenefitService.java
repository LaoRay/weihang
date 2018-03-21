package cn.com.clubank.weihang.manage.member.service;

import java.math.BigDecimal;
import java.util.List;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;

/**
 * 账户权益（账户等级）表（业务逻辑层接口）
 * 
 * @author YangWei
 *
 */
public interface IAccountBenefitService {

	AccountBenefit selectByPrimaryKey(String abId);

	List<AccountBenefit> selectAll();

	/**
	 * 根据金额获取当前级别信息
	 * 
	 * @param account
	 * @return
	 */
	AccountBenefit getByAccount(BigDecimal account);

	/**
	 * 后台：获取会员权益列表
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * 
	 * @return
	 */
	CommonResult clientFindAccountBenefitList(Integer pageIndex, Integer pageSize);

	/**
	 * 后台：获取会员权益详情
	 * 
	 * @param abId
	 * @return
	 */
	CommonResult clientFindAccountBenefitByAbId(String abId);

	/**
	 * 后台：新增或修改会员权益
	 * 
	 * @param accountBenefit
	 * @return
	 */
	CommonResult clientModifyAccountBenefit(AccountBenefit accountBenefit);

	/**
	 * 后台：修改有效无效状态
	 * 
	 * @param abId
	 * @param status
	 * @return
	 */
	CommonResult clientUpdateAccountBenefitStatus(String abId, Integer status);

	/**
	 * 后台：删除会员权益
	 * 
	 * @param abId
	 * @return
	 */
	CommonResult clientDeleteAccountBenefit(String abId);
}
