package cn.com.clubank.weihang.common.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import cn.com.clubank.weihang.common.util.StringUtil;

/**
 * 切面定义：实现保存时的主键创建、更新时的更新时间修改等动作
 * 
 * @author YangWei
 *
 */
@Component
public class WeihAspect implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object[] args = mi.getArguments();
		if (args.length == 1) {
			//获取到对象
			Object obj = args[0];
			if (obj instanceof List) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) obj;
				for (Object object : list) {
					handleFields(mi, object);
				}
			} else {
				handleFields(mi, obj);
			}
			
		}
		
		return mi.proceed();
	}

	private void handleFields(MethodInvocation mi, Object obj) throws Throwable {
		//对象的所有字段
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			//如果有自定义注解WeihColumn，则按照注解类型处理
			if (field.isAnnotationPresent(WeihColumn.class)) {
				if (mi.getMethod().getName().contains("insert")) {
					insert(obj, field);
				} else if (mi.getMethod().getName().contains("update")) {
					update(obj, field);
				}
			}
		}
	}
	
	/**
	 * 插入时的处理
	 * @param obj
	 * @param field
	 * @throws Throwable
	 */
	private void insert(Object obj, Field field) throws Throwable {
		//主键
		if (field.getAnnotation(WeihColumn.class).value().equals(WeihColumnCode.UUID)) {
			Method method = null;
			try {
				//首字母大写
				method = obj.getClass().getMethod("set" + StringUtil.upperCaseFirstChar(field.getName()), String.class);
			} catch (NoSuchMethodException e) {
				//存在首字母小写的情况
				method = obj.getClass().getMethod("set" + StringUtil.lowerCaseFirstChar(field.getName()), String.class);
			}
			if (method != null) {
				method.invoke(obj, UUID.randomUUID().toString());
			}
		}
		
		//创建时间，更新时间也在插入的时候添加数据
		if (field.getAnnotation(WeihColumn.class).value().equals(WeihColumnCode.CREATE_TIME) || field.getAnnotation(WeihColumn.class).value().equals(WeihColumnCode.UPDATE_TIME)) {
			Method method = null;
			try {
				//首字母大写
				method = obj.getClass().getMethod("set" + StringUtil.upperCaseFirstChar(field.getName()), Date.class);
			} catch (NoSuchMethodException e) {
				//存在首字母小写的情况
				method = obj.getClass().getMethod("set" + StringUtil.lowerCaseFirstChar(field.getName()), Date.class);
			}
			if (method != null) {
				method.invoke(obj, new Date());
			}
		}
	}
	
	/**
	 * 更新时的处理
	 * @param obj
	 * @param field
	 * @throws Throwable
	 */
	private void update(Object obj, Field field) throws Throwable {
		//更新时间
		if (field.getAnnotation(WeihColumn.class).value().equals(WeihColumnCode.UPDATE_TIME)) {
			Method method = null;
			try {
				//首字母大写
				method = obj.getClass().getMethod("set" + StringUtil.upperCaseFirstChar(field.getName()), Date.class);
			} catch (NoSuchMethodException e) {
				//存在首字母小写的情况
				method = obj.getClass().getMethod("set" + StringUtil.lowerCaseFirstChar(field.getName()), Date.class);
			}
			if (method != null) {
				method.invoke(obj, new Date());
			}
		}
				
	}
}
