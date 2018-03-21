package cn.com.clubank.weihang.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.annotation.WeihAuth;
import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.ResultCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 拦截器
 * 
 * @author
 *
 */
@Slf4j
public class WeihInterceptor implements HandlerInterceptor {

	@Autowired
	private JedisClient jedisClient;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 判断该次是否需要拦截（即有无@WeihAuth）
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			WeihAuth weihAuth = ((HandlerMethod) handler).getMethodAnnotation(WeihAuth.class);
			// 没有声明需要权限,或者声明不验证权限
			if (weihAuth == null) {
				return true;
			} else {
				// 请求的token
				String token = request.getHeader("token");
				String jsonString = jedisClient.get("SESSION:" + token);
				if (StringUtils.isBlank(jsonString)) {
					JSONObject json = new JSONObject();
					json.put("apiStatus", ResultCode.AUTH_TOKEN_INVALID.getValue());
					json.put("msg", "token验证失败，请重新登录！");
					response.setContentType("application/json;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.write(json.toString());
					out.flush();
					out.close();
					return false;
				}
				jedisClient.expire("SESSION:" + token, 864000);
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		if (e != null) {
			// 控制台打印
			e.printStackTrace();
			// 记录log
			log.error("服务器内部错误", e);

			JSONObject json = new JSONObject();
			json.put("apiStatus", ResultCode.SERVER_ERROR.getValue());
			json.put("msg", "服务器内部错误");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(json.toString());
			out.flush();
			out.close();
		}
	}
}
