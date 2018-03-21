package cn.com.clubank.weihang.client.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseDepartmentService;

/**
 * 行政部门表
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientBaseDepartmentController {

	@Autowired
	private BaseDepartmentService baseDepartmentService;

	/**
	 * 查询所有部门列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindBaseDepartmentList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindBaseDepartmentList() {
		return baseDepartmentService.clientFindBaseDepartmentList();
	}
}
