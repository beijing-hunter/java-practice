package shrding.dbtable.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.plugin.Invocation;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.jdbc.datasource.DataSourceUtils;

import shrding.dbtable.core.DataSourceTableRule.AnalysisResult;

/**
 * 
 * @author:高崖雷
 * @date:Nov 11, 2020 3:32:35 PM
 * @version:V1.0 版权：版权归作者所有，禁止外泄以及其他商业项目
 */
public class ShardDbTableHandler {

	private boolean env = false;

	private DataSourceTableRule tableRule;

	private Map<String, DataSource> dataSourceMap;

	public void setEnv(boolean env) {
		this.env = env;
	}

	public void setTableRule(DataSourceTableRule tableRule) {
		this.tableRule = tableRule;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public TransactionContext handler(Invocation invocation) throws Exception {

		if (!this.env) {
			return null;
		}

		String sql = ExecutorPluginUtils.getSqlByInvocation(invocation);

		AnalysisResult result = this.tableRule.analysisRule(sql);
		String dataSourceKey = DbRouteFactory.getRouteDbName(this.tableRule.getDefaultDataSourceKeyList(), result);
		return TransactionConnection.setNewConnection(invocation, this.dataSourceMap.get(dataSourceKey));
	}

	public void close(Invocation invocation, TransactionContext context) throws Exception {

		if (context == null) {
			return;
		}

		TransactionConnection.close(invocation, context);
	}
}
