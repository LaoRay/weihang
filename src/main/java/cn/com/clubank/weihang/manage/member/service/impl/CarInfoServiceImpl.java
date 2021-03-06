package cn.com.clubank.weihang.manage.member.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.BaseParameterCode;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.bespeak.dao.WorkReservationOrderMapper;
import cn.com.clubank.weihang.manage.bespeak.pojo.WorkReservationOrder;
import cn.com.clubank.weihang.manage.member.dao.AccountBenefitMapper;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarBrandMapper;
import cn.com.clubank.weihang.manage.member.dao.CarGroupMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarBrand;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.service.IAccountBenefitService;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;
import cn.com.clubank.weihang.manage.repair.dao.WorkPickupOrderMapper;
import cn.com.clubank.weihang.manage.repair.pojo.WorkPickupOrder;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseParameterService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarInfoServiceImpl implements ICarInfoService {

	@Resource
	private CarInfoMapper carInfoMapper;
	@Autowired
	private CustomerInfoMapper customerInfoMapper;
	@Autowired
	private CarAccountRecordMapper carAccountRecordMapper;
	@Autowired
	private WorkPickupOrderMapper workPickupOrderMapper;
	@Autowired
	private WorkReservationOrderMapper workReservationOrderMapper;

	@Resource
	private CarBrandMapper carBrandMapper;
	@Resource
	private CarGroupMapper carGroupMapper;
	@Resource
	private AccountBenefitMapper accountBenefitMapper;

	@Resource
	private IAccountBenefitService iAccountBenefitService;

	@Resource
	private BaseParameterService baseParameterService;

	@Override
	public boolean deleteByPrimaryKey(String carId) {

		return carInfoMapper.deleteByPrimaryKey(carId) > 0;
	}

	@Override
	public boolean insert(CarInfo record) {

		return carInfoMapper.insert(record) > 0;
	}

	@Override
	public String selectByCarNo(String carNo) {
		JSONObject json = new JSONObject();

		CarInfo car = carInfoMapper.selectByCarNo(carNo);
		if (car == null) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "无车辆");
		} else {
			String cId = car.getCustomerId();
			CustomerInfo customer = customerInfoMapper.selectByPrimaryKey(cId);
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("RecordDate", car.getCreateDate());// 车牌登记日期
			json.put("data", customer);
		}
		return json.toString();
	}

	@Override
	public CarInfo selectByPrimaryKey(String carId) {

		return carInfoMapper.selectByPrimaryKey(carId);
	}

	@Override
	public String selectByCustomerId(String customerId) {
		JSONObject json = new JSONObject();

		List<CarInfo> list = carInfoMapper.selectByCustomerId(customerId);
		if (list.size() == 0) {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "没有车辆");
		} else {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "有车辆");
			json.put("data", list);

		}
		return json.toString();
	}

	@Override
	public List<CarInfo> selectAll() {

		return carInfoMapper.selectAll();
	}

	@Override
	public boolean updateByPrimaryKey(CarInfo record) {

		return carInfoMapper.updateByPrimaryKey(record) > 0;
	}

	@Override
	public String deleteCar(String carId) {
		JSONObject json = new JSONObject();
		CarInfo carInfo = carInfoMapper.selectByPrimaryKey(carId);
		if (carInfo != null) {
			// 判断是否存在未完成的订单
			List<WorkPickupOrder> wpos = workPickupOrderMapper.findUndoneByCarNo(carInfo.getCarNo());
			if (wpos.size() > 0) {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "该车辆存在正在服务中的订单，不能删除");
				return json.toString();
			}
			List<WorkReservationOrder> wros = workReservationOrderMapper.selectUndoneByCarNo(carInfo.getCarNo());
			if (wros.size() > 0) {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "该车辆存在未完成或未取消的预约单，不能删除");
				return json.toString();
			}
		}
		if (carInfo != null && carInfo.getIsDefault()) {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "默认车辆不能删除");
			return json.toString();
		}
		if (carInfo.getAccount().compareTo(new BigDecimal(0)) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "账户不为零不能删除");
			return json.toString();
		}
		if (carInfoMapper.deleteCar(carId) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public BigDecimal getAccount(String customerId) {

		return carInfoMapper.getAccount(customerId);
	}

	/**
	 * 设置用户默认车辆
	 */
	@Override
	public CommonResult setDefaultCar(CarInfo record) {
		try {
			carInfoMapper.updateIsDefault(record);
			return CommonResult.result(ResultCode.SUCC.getValue(), "设置成功");
		} catch (Exception e) {
			log.error("服务器异常", e);
			return CommonResult.result(ResultCode.SERVER_ERROR.getValue(), "服务器内部错误");
		}
	}

	/**
	 * 新增车辆
	 */
	@Override
	public CommonResult addNewCar(CarInfo car) {
		if (car == null || StringUtil.isBlank(car.getCustomerId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误：客户id不能为空");
		}
		List<CarInfo> carList = carInfoMapper.selectAllByCustomerId(car.getCustomerId());
		// 如果车牌号为空，添加虚拟车辆信息
		if (StringUtils.isBlank(car.getCarNo())) {
			if (carList.size() > 0) {
				return CommonResult.result(ResultCode.FAIL.getValue(), "请填写车牌号");
			}
			car.setCarNo(StringUtil.getVirtualCarNo());
			car.setCustomerId(car.getCustomerId());
			car.setIsDefault(true);
			car.setAccount(new BigDecimal(Constants.INT_0));
			car.setIsVirtual(true);
			car.setCarId(null);
			carInfoMapper.insert(car);
			return CommonResult.result(ResultCode.SUCC.getValue(), "添加车辆成功");
		} else { // 如果不为空，添加真实车辆
			CarInfo info = carInfoMapper.selectByCarNo(car.getCarNo());
			if (info != null) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "车牌号已存在");
			}

			if (carList.size() == 1 && carList.get(0).getIsVirtual()) {
				// 如果只存在一条车辆数据并且是虚拟数据，则覆盖虚拟车辆
				car.setCarId(carList.get(0).getCarId());
				car.setAccount(carList.get(0).getAccount());
				car.setAccountRechargeTotal(carList.get(0).getAccountRechargeTotal());
				car.setAbId(carList.get(0).getAbId());
				car.setIsDefault(true);
				car.setIsVirtual(false);
				carInfoMapper.updateSelectiveByPrimaryKey(car);
			} else {
				car.setCustomerId(car.getCustomerId());
				car.setAccount(new BigDecimal(Constants.INT_0));
				if (carList.size() == 0) {
					// 用户账户下没有任何车辆
					car.setIsDefault(true);
				}
				// 限制用户已有的车辆数
				int total = carInfoMapper.selectTotalByCustomerId(car.getCustomerId());
				// 参数表中获取限制车辆数
				String value = baseParameterService.getValueByKeyCode(BaseParameterCode.CUSTOMER_CAR_NUM.getValue());
				try {
					if (StringUtil.isNotBlank(value) && total >= Integer.valueOf(value)) {
						return CommonResult.result(ResultCode.FAIL.getValue(), "用户车辆数不能超过" + value + "辆");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return CommonResult.result(ResultCode.FAIL.getValue(), "系统参数【每位会员限制车辆数】配置错误");
				}
				carInfoMapper.insert(car);
			}
			return CommonResult.result(ResultCode.SUCC.getValue(), "添加车辆成功");
		}
	}

	/**
	 * 查询车辆账户流水
	 */
	@Override
	public CommonResult findCarAccountRecodList(String customerId, Integer pageIndex, Integer pageSize) {
		CarInfo carInfo = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		if (carInfo == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "账户信息异常");
		}
		List<CarAccountRecord> recordList = carAccountRecordMapper.selectRecordListByCarId(carInfo.getCarId(),
				PageObject.getStart(pageIndex, pageSize), pageSize);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询车辆账户流水成功", recordList);
	}

	@Override
	public String selectCustomerCarList(String customerId) {
		JSONObject json = new JSONObject();

		List<Map<String, Object>> list = carInfoMapper.selectCustomerCarList(customerId);
		if (list != null && list.size() != Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}
		return JSON.toJSONStringWithDateFormat(json, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectCarbrandAndGroup() {
		JSONObject json = new JSONObject();

		Map<String, Object> map = new HashMap<>();

		List<CarBrand> carBrandList = carBrandMapper.selectAll();// 获得所有车辆品牌

		List<CarGroup> groupList = carGroupMapper.selectAll();// 获得所有集团组

		map.put("carBrandList", carBrandList);
		map.put("groupList", groupList);

		json.put("apiStatus", ResultCode.SUCC);
		json.put("msg", "查询成功");
		json.put("data", map);
		return json.toString();
	}

	@Override
	public CommonResult insertOrUpdateCar(CarInfo car) {
		CarInfo info = carInfoMapper.selectByCarNo(car.getCarNo());
		if (info != null && !info.getCarId().equals(car.getCarId())) {
			return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "车牌号已存在");
		}
		if (StringUtils.isBlank(car.getCarId())) {// 主键为空执行新增
			return addNewCar(car);
		} else {
			carInfoMapper.updateSelectiveByPrimaryKey(car);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public String selectCarDetail(String carId) {
		JSONObject json = new JSONObject();

		Map<String, Object> map = carInfoMapper.selectCarDetailByCarId(carId);
		if (!map.isEmpty()) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", map);
		} else {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", map);
		}
		return json.toString();
	}

	@Override
	public CommonResult clientMemberSetAssets(String customerId, String benefitId, BigDecimal account,
			Integer integralAccount) {
		if (StringUtil.isBlank(customerId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		CustomerInfo cus = customerInfoMapper.selectByPrimaryKey(customerId);
		if (cus == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "没有找到客户信息");
		}
		CarInfo car = carInfoMapper.selectDefaultCarByCustomerId(customerId);
		if (car == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "没有找到客户的车辆信息");
		}
		// 设置客户的积分
		if (integralAccount != null) {
			cus.setIntegralAccount(integralAccount);
			customerInfoMapper.updateByPrimaryKey(cus);
		}

		// 设置车辆的等级和金额
		if (StringUtil.isNotBlank(benefitId) || account != null) {
			if (StringUtil.isNotBlank(benefitId)) {
				car.setAbId(benefitId);
			}
			if (account != null) {
				car.setAccount(account);
			}
			carInfoMapper.updateByPrimaryKey(car);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "设置成功");
	}

	@Override
	public CommonResult findByCarNo(String carNo, Integer pageIndex, Integer pageSize) {
		List<CarInfo> recordList = carInfoMapper.findByCarNo(carNo, PageObject.getStart(pageIndex, pageSize), pageSize);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", recordList);
	}

	@Override
	public CommonResult memberScanGetCarInfoByNo(String customerId, String carNo) {
		Map<String, Object> car = carInfoMapper.selectCarDetailByCarNo(carNo);
		if (car == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "车辆信息不存在【" + carNo + "】");
		}
		if (!car.get("customerId").toString().equals(customerId)) {
			return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "此车辆不属于您的车辆");
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", car);
	}

	/**
	 * 查询车辆详情
	 */
	@Override
	public CommonResult memberFindCarInfo(String carId) {
		if (StringUtils.isBlank(carId)) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		CarInfo carInfo = carInfoMapper.selectByPrimaryKey(carId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", carInfo);
	}

	/**
	 * 编辑车辆信息
	 */
	@Override
	public CommonResult memberModifyCarInfo(CarInfo carInfo) {
		if (carInfo == null || StringUtils.isBlank(carInfo.getCarId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		carInfoMapper.updateSelectiveByPrimaryKey(carInfo);
		return CommonResult.result(ResultCode.SUCC.getValue(), "更新成功");
	}
}
