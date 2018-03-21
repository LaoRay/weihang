package cn.com.clubank.weihang.client.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;

/**
 * 后台：会员列表
 * @author Liangwl
 *
 */
@Controller
public class ClientMemberListController {

	@Resource
	private ICustomerInfoService iCustomerInfoService;
	
	@Resource
	private ICarInfoService iCarInfoService;
	
	/**
	 * 后台：获得会员列表并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindCustomerListAndPaged",method=RequestMethod.POST)
	@ResponseBody
	public String findCustomerListAndPaged(@RequestBody JSONObject param){
		
		return iCustomerInfoService.selectCustomerListAndPaged(param.getString("realname"),param.getString("nickname") ,param.getInteger("status") ,param.getInteger("pageIndex") ,param.getInteger("pageSize") );
	}
	
	/**
	 * 后台：获得会员详情
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindCustomerDetail",method=RequestMethod.POST)
	@ResponseBody
	public String findCustomerDetail(@RequestBody JSONObject param){
		
		return iCustomerInfoService.selectCustomerDetail(param.getString("cId"));
	}
	
	/**
	 * 后台：新增或编辑会员信息
	 * @param customer
	 * @return
	 */
	@RequestMapping(value="/clientAddOrEditCustomer",method=RequestMethod.POST)
	@ResponseBody
	public String addOrEditCustomer(@RequestBody CustomerInfo customer){
		
		return iCustomerInfoService.insertOrUpdateCustomer(customer);
	}
	
	/**
	 * 后台：获得会员车辆列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindCustomerCarList",method=RequestMethod.POST)
	@ResponseBody
	public String findCustomerCarList(@RequestBody JSONObject param){
		
		return iCarInfoService.selectCustomerCarList(param.getString("customerId"));
	}
	
	/**
	 * 后台：获得车辆品牌ID、车辆品牌，集团ID、集团名
	 * @return
	 */
	@RequestMapping(value="/clientFindCarbrandAndGroup",method=RequestMethod.POST)
	@ResponseBody
	public String findCarbrandAndGroup(){
		
		return iCarInfoService.selectCarbrandAndGroup();
	}
	
	/**
	 * 后台：新增或编辑车辆
	 * @param car
	 * @return
	 */
	@RequestMapping(value="/clientAddOrEditCar",method=RequestMethod.POST)
	@ResponseBody
	public String addOrEditCar(@RequestBody CarInfo car){
		
		return iCarInfoService.insertOrUpdateCar(car);
	}
	
	/**
	 * 后台：删除车辆
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientDeleteCar",method=RequestMethod.POST)
	@ResponseBody
	public String deleteCar(@RequestBody JSONObject param){
		
		return iCarInfoService.deleteCar(param.getString("carId"));
	}
	
	/**
	 * 后台：获得车辆详情
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindCarDetail",method=RequestMethod.POST)
	@ResponseBody
	public String findCarDetail(@RequestBody JSONObject param){
		
		return iCarInfoService.selectCarDetail(param.getString("carId"));
	}
}
