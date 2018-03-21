package cn.com.clubank.weihang.common.dataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class DataSourceAspect {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void before(JoinPoint point) throws NoSuchMethodException {
		Object target = point.getTarget();
		String method = point.getSignature().getName();
		Class classz = target.getClass();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		Method m = classz.getMethod(method, parameterTypes);
		if (m != null && m.isAnnotationPresent(DataSource.class)) {
			DataSource dataSource = m.getAnnotation(DataSource.class);
			DynamicDataSourceHolder.setDataSource(dataSource.value());
		}

	}
}
