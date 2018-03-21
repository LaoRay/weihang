package cn.com.clubank.weihang.restful.systemSettings;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.message.service.IMessageService;

/**
 * 消息
 * @author YangWei
 *
 */
@Controller
public class MessageController {

	@Autowired
	private IMessageService iMessageService;
	
	/**
	 * 客户查询所有消息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/customerFindMsgList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult customerFindMsgList(HttpServletRequest request, @RequestBody JSONObject param) {
		if (StringUtil.isBlank(request.getHeader("customerId"))) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "会员唯一id为空！");
		}
		return iMessageService.findByTargetId(request.getHeader("customerId"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 员工查询所有消息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userFindMsgList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult userFindMsgList(HttpServletRequest request, @RequestBody JSONObject param) {
		if (StringUtil.isBlank(request.getHeader("userId"))) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "用户唯一id为空！");
		}
		return iMessageService.findByTargetId(request.getHeader("userId"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 客户查询未读消息量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/customerGetUnreadTotal", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult customerGetUnreadTotal(HttpServletRequest request) {
		if (StringUtil.isBlank(request.getHeader("customerId"))) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "会员唯一id为空！");
		}
		return iMessageService.getUnreadTotalByTargetId(request.getHeader("customerId"));
	}
	
	/**
	 * 员工查询未读消息量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userGetUnreadTotal", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult userGetUnreadTotal(HttpServletRequest request) {
		if (StringUtil.isBlank(request.getHeader("userId"))) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "用户唯一id为空！");
		}
		return iMessageService.getUnreadTotalByTargetId(request.getHeader("userId"));
	}
	
	/**
	 * 客户端已读
	 * 
	 * @return
	 */
	@RequestMapping(value = "/customerMsgHaveRead", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult customerMsgHaveRead(HttpServletRequest request, @RequestBody JSONObject param) {
		return iMessageService.haveRead(param.getString("msgId"), request.getHeader("customerId"));
	}
	
	/**
	 * 员工端已读
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userMsgHaveRead", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult userMsgHaveRead(HttpServletRequest request, @RequestBody JSONObject param) {
		return iMessageService.haveRead(param.getString("msgId"), request.getHeader("userId"));
	}
	
}
