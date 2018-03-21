package cn.com.clubank.weihang.client.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.systemSettings.service.IBaseLogService;

/**
 * 后台：日志管理
 * @author Liangwl
 *
 */
@Controller
public class ClientLogController {

	@Resource
	private IBaseLogService iBaseLogService;
	
	/**
	 * 后台：通过操作用户（模糊），操作时间查询日志列表并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientFindLogListAndPaged", method = RequestMethod.POST)
	@ResponseBody
	public String findLogListAndPaged(@RequestBody JSONObject param){
		
		return iBaseLogService.selectLogListAndPaged(param.getString("operateName"),param.getString("operateTimeOne") ,param.getString("operateTimeTwo") ,param.getInteger("pageIndex") ,param.getInteger("pageSize"));
	}
}
