package cn.com.clubank.weihang.restful.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;

@Controller
public class CarController {

	@Resource
	private ICarInfoService iCarInfoService;

	/**
	 * 根据会员编号，获取会员车辆信息
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/memberGetCarInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getCarInfo(@RequestBody JSONObject param) {

		return iCarInfoService.selectByCustomerId(param.getString("customerId"));
	}

	/**
	 * 删除会员车辆信息
	 * 
	 * @param carId
	 * @return
	 */
	@RequestMapping(value = "/memberDeleteCar", method = RequestMethod.POST)
	@ResponseBody
	public String deleteCarInfo(@RequestBody JSONObject param) {

		return iCarInfoService.deleteCar(param.getString("carId"));
	}

	/**
	 * 设置默认车辆
	 * 
	 * @param customerId
	 * @param carNo
	 * @return
	 */
	@RequestMapping(value = "/memberSetDefaultCar", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult isDefaultCarNo(@RequestBody CarInfo record) {
		return iCarInfoService.setDefaultCar(record);
	}

	/**
	 * 新增车辆
	 * 
	 * @param car
	 * @return
	 */
	@RequestMapping(value = "/memberAddNewCar", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addNewCar(@RequestBody CarInfo record) {
		return iCarInfoService.addNewCar(record);
	}

	/**
	 * 查询车辆账户流水
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/memberFindCarAccountRecodList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findCarAccountRecodList(@RequestBody JSONObject json) {
		return iCarInfoService.findCarAccountRecodList(json.getString("customerId"), json.getInteger("pageIndex"),
				json.getInteger("pageSize"));
	}
}
