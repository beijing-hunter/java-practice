package shrding.dbtable.core;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Invocation;

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

		return null;
	}

	public void close(Invocation invocation, TransactionContext context) throws Exception {

	}
}
