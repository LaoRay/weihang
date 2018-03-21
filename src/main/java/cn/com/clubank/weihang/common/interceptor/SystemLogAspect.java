package cn.com.clubank.weihang.common.interceptor;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.annotation.WeihAuthLog;
import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseLogMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseLog;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用spring AOP记录系统日志
 * 
 * @author YangWei
 *
 */
@Slf4j
@Aspect
@Component
public class SystemLogAspect {

	@Resource
	private BaseLogMapper baseLogMapper;

	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private JedisClient jedisClient;

	/**
	 * Controller层切点
	 */
	@Pointcut("execution (* cn.com.clubank.weihang.restful.**..*.*(..)) || execution (* cn.com.clubank.weihang.client.**..*.*(..)) || execution (* cn.com.clubank.weihang.website.**..*.*(..))")
	public void controllerAspect() {
	}

	/**
	 * 后置通知 用于拦截Controller层记录用户的操作
	 * 
	 * @param joinPoint
	 *            切点
	 */
	@After("controllerAspect()")
	public void after(JoinPoint joinPoint) {
		saveLog(joinPoint, null);
	}

	/**
	 * 异常通知 用于拦截记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		saveLog(joinPoint, e.getMessage());
	}

	/**
	 * 保存日志
	 * @param token
	 * @param joinPoint
	 * @param exception
	 */
	private void saveLog(JoinPoint joinPoint, String exception) {
		// 获取token
		String token = request.getHeader("token");
		if (StringUtil.isBlank(token) && request.getAttribute("token") != null) {
			token = request.getAttribute("token").toString();
		}
		if (StringUtil.isNotBlank(token)) {
			// ip地址
			String ipAddress = request.getRemoteAddr();
			//操作模块及功能
			String operationType = "";
			String operationName = "";
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			WeihAuthLog weihLog = method.getAnnotation(WeihAuthLog.class);
			if (weihLog != null) {
				operationType = weihLog.logFeatures();
				operationName = weihLog.logModule();
				
				try {
					JSONObject session = JSON.parseObject(jedisClient.get("SESSION:" + token));
					String content = "";
					if (StringUtil.isNotBlank(exception)) {
						content = exception;
					} else if (StringUtil.isNotBlank(operationType)) {
						content = "【" + operationType + "】" + operationName;
					}
					BaseLog baseLog = new BaseLog(session.getString("operateMobile"), session.getString("operateName"), operationName, operationType, ipAddress, content, session.getInteger("flatType"));
					baseLogMapper.insert(baseLog);
					log.info("记录操作日志：{}", JSON.toJSONStringWithDateFormat(log, Constants.DATE_COMMON));
				} catch (Exception e) {
					log.error("记录日志异常：{}", e.toString());
				}
			}
		}
	}
	
}
