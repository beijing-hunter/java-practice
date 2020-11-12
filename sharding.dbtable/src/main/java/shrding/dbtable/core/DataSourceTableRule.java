package shrding.dbtable.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class DataSourceTableRule {

	private String defaultDataSourceKey;

	private List<String> defaultDataSourceKeyList;

	private Map<String, List<String>> dbAndTableRule;

	private Map<String, List<String>> tableAndDbRuleMap;

	private Logger logger = LoggerFactory.getLogger(DataSourceTableRule.class);

	public void setDefaultDataSourceKey(String defaultDataSourceKey) {
		this.defaultDataSourceKey = defaultDataSourceKey;
	}

	public String getDefaultDataSourceKey() {
		return defaultDataSourceKey;
	}

	public List<String> getDefaultDataSourceKeyList() {
		return defaultDataSourceKeyList;
	}

	public void setDbAndTableRule(Map<String, List<String>> dbAndTableRule) {
		this.dbAndTableRule = dbAndTableRule;
	}

	/**
	 * 解析sql语句
	 * 
	 * @param sql
	 * @return
	 */
	public AnalysisResult analysisRule(String sql) {

		if (sql == null) {
			return null;
		}

		if (this.dbAndTableRule == null || this.dbAndTableRule.isEmpty()) {
			return null;
		}

		return new AnalysisResult(null, null);
	}

	private List<String> extractDbSource(List<String> tables) {

		return null;
	}

	private synchronized void init() {

	}

	class AnalysisResult {

		private List<String> dbSources;

		private List<String> tables;

		public AnalysisResult(List<String> dbSources, List<String> tables) {
			super();
			this.dbSources = dbSources;
			this.tables = tables;
		}

		public List<String> getDbSources() {
			return dbSources;
		}

		public List<String> getTables() {
			return tables;
		}

		public boolean isSuccess() {

			return this.dbSources != null && this.dbSources.size() > 0;
		}

		public String getJoinTableKey() {

			if (tables == null || tables.isEmpty()) {
				return null;
			}

			return JSON.toJSONString(tables);
		}

	}
}
