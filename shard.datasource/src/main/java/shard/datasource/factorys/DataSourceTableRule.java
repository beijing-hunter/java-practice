package shard.datasource.factorys;

import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 数据源表规则
 * 
 * @author cmt
 *
 */
public class DataSourceTableRule {

	private Map<String, List<String>> tableRule;

	public Map<String, List<String>> getTableRule() {
		return tableRule;
	}

	public void setTableRule(Map<String, List<String>> tableRule) {
		this.tableRule = tableRule;
	}

	public String dataSourceKey(String sql) {

		String dataSourceKey = this.analysisRule(sql);
		return dataSourceKey;
	}

	/**
	 * 解析sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private String analysisRule(String sql) {

		if (StringUtils.isEmpty(sql)) {
			return null;
		}

		if (this.tableRule == null || this.tableRule.isEmpty()) {
			return null;
		}

		for (String key : this.tableRule.keySet()) {

			List<String> tableNames = this.tableRule.get(key);

			for (String tableName : tableNames) {

				if (sql.indexOf(tableName) >= 0) {
					return key;
				}
			}
		}

		return null;
	}
}
