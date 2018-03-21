package cn.com.clubank.weihang.common.dataSource;

import java.util.concurrent.atomic.AtomicInteger;

public class DynamicDataSourceHolder {

	public static final ThreadLocal<String> holder = new ThreadLocal<>();

	// 写库对应的数据源key
	private static final String MASTER = "master";

	private static AtomicInteger counter = new AtomicInteger(-1);

	// 读库对应的数据源key
	private static final String SLAVE_1 = "slave_1";
	private static final String SLAVE_2 = "slave_2";

	public static void setDataSource(DataSourceType dataSourceType) {
		if (dataSourceType == DataSourceType.MASTER) {
			System.out.println("=========" + MASTER + "=============");
			holder.set(MASTER);
		} else if (dataSourceType == DataSourceType.SLAVE) {
			holder.set(roundRobinSlaveKey());
		}
	}

	public static String getDataSource() {
		return holder.get();
	}

	private static String roundRobinSlaveKey() {
		Integer index = counter.incrementAndGet() % 2;
		if (counter.get() > 9999) {
			counter.set(-1);
		}
		if (index == 0) {
			System.out.println("=========" + SLAVE_1 + "=============");
			return SLAVE_1;
		} else {
			System.out.println("=========" + SLAVE_2 + "=============");
			return SLAVE_2;
		}
	}

}
