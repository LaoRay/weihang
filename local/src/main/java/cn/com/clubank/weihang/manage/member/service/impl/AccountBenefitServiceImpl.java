package cn.com.clubank.weihang.manage.member.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.AccountBenefitMapper;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.service.IAccountBenefitService;

/**
 * 账户权益（账户等级）
 * 
 * @author YangWei
 *
 */
@Service
public class AccountBenefitServiceImpl implements IAccountBenefitService {

	@Resource
	private AccountBenefitMapper accountBenefitMapper;

	@Override
	public AccountBenefit selectByPrimaryKey(String abId) {
		return accountBenefitMapper.selectByPrimaryKey(abId);
	}

	@Override
	public List<AccountBenefit> selectAll() {
		return accountBenefitMapper.selectAll();
	}

	@Override
	public AccountBenefit getByAccount(BigDecimal account) {
		return accountBenefitMapper.getLessAccountFrist(account);
	}

	/**
	 * 后台：获取会员权益列表
	 */
	@Override
	public CommonResult clientFindAccountBenefitList(Integer pageIndex, Integer pageSize) {
		PageObject<AccountBenefit> page = new PageObject<>(pageIndex, pageSize);
		int total = accountBenefitMapper.selectAccountBenefitCount();
		page.setRows(total);
		List<AccountBenefit> list = accountBenefitMapper.selectAccountBenefitList();
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 后台：获取会员权益详情
	 */
	@Override
	public CommonResult clientFindAccountBenefitByAbId(String abId) {
		AccountBenefit accountBenefit = accountBenefitMapper.selectByPrimaryKey(abId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", accountBenefit);
	}

	/**
	 * 后台：新增或修改会员权益
	 */
	@Override
	public CommonResult clientModifyAccountBenefit(AccountBenefit accountBenefit) {
		if (accountBenefit != null) {
			String msg = "";
			if (StringUtils.isBlank(accountBenefit.getAbId())) {
				accountBenefitMapper.insert(accountBenefit);
				msg = "新增成功";
			} else {
				accountBenefitMapper.updateSelectiveByPrimaryKey(accountBenefit);
				msg = "修改成功";
			}
			return CommonResult.result(ResultCode.SUCC.getValue(), msg);
		}
		return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
	}

	/**
	 * 后台：修改有效无效状态
	 */
	@Override
	public CommonResult clientUpdateAccountBenefitStatus(String abId, Integer status) {
		AccountBenefit accountBenefit = accountBenefitMapper.selectByPrimaryKey(abId);
		accountBenefit.setEnabledMark(status);
		accountBenefitMapper.updateByPrimaryKey(accountBenefit);
		return CommonResult.result(ResultCode.SUCC.getValue(), "设置成功");
	}

	/**
	 * 后台：删除会员权益
	 */
	@Override
	public CommonResult clientDeleteAccountBenefit(String abId) {
		accountBenefitMapper.deleteByPrimaryKey(abId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}
}
