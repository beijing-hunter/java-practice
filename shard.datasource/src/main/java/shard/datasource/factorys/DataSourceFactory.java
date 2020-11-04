package shard.datasource.factorys;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

public class DataSourceFactory extends AbstractRoutingDataSource {

	private static String defaultDataSourceKey = "dataSource_db01";

	/**
	 * 当前操作的数据源key
	 */
	@Override
	protected Object determineCurrentLookupKey() {

		String dataSourceKey = DataSourceContextHolder.getDataSourceKey();
		DataSourceContextHolder.clear();

		if (StringUtils.isEmpty(dataSourceKey)) {
			return defaultDataSourceKey;
		}

		return dataSourceKey;
	}

}
