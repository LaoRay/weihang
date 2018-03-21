package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.product.dao.ShopIntegralMapper;
import cn.com.clubank.weihang.manage.product.pojo.ShopIntegral;
import cn.com.clubank.weihang.manage.product.service.IShopIntegralService;

@Service
public class ShopIntegralServiceImpl implements IShopIntegralService {

	@Resource
	private ShopIntegralMapper shopIntegralMapper;
	
	@Resource
	private CustomerInfoMapper customerInfoMapper;
	
	@Override
	public String selectShopIntegralList() {
		JSONObject json=new JSONObject();
		
		List<ShopIntegral> list=shopIntegralMapper.selectAll();
		if(list==null){
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "失败");
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "成功");
			json.put("data", list);
		}
		return json.toString();
	}

	@Override
	public String updateIntegralAccount(String customerId,String siId) {
		JSONObject json=new JSONObject();
		ShopIntegral si=shopIntegralMapper.selectByPrimaryKey(siId);
		if (si == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "积分商品不存在");
			return json.toString();
		}
		Integer changeGrade=si.getChangeGrade();//积分商品兑换等级
		Integer changeIntegral=si.getChangeIntegral();//积分商品兑换积分
		
		Map<String,Object> map=customerInfoMapper.selectCustomerIntegralGradeInfo(customerId);
		Integer integralAccount=map.get("integralAccount")==null?0:(Integer) map.get("integralAccount");//会员积分
		Integer level = map.get("level")==null?1:(Integer) map.get("level");//会员等级

		//判断兑换等级
		if(level<changeGrade){
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "会员等级不够");
			return json.toString();
		}
		//判断兑换积分
		if(integralAccount<changeIntegral){
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "积分不够");
			return json.toString();
		}
		//兑换并更新积分 TODO 生成兑换记录
		CustomerInfo customer=new CustomerInfo();
		customer.setCId(customerId);
		customer.setIntegralAccount(integralAccount-changeIntegral);
		customerInfoMapper.updateSelectiveByPrimaryKey(customer);
		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "兑换成功");
		return json.toString();
	}

}
