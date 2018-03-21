package cn.com.clubank.weihang.common.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * QuartzJobBean对象的实例化过程是在Quartz中进行的，xxxService是在Spring容器当中的，
 * 如果我们不指定jobFactory，那么Spring就使用AdaptableJobFactory，
 * 那么我们写一个类继承它，然后复写这个方法进行对Job的注入
 * 
 * @author LeiQY
 *
 */
public class BaseQuartzJobFactory extends AdaptableJobFactory {

	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
