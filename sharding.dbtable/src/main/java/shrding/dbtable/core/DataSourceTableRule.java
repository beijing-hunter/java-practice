package shrding.dbtable.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 
 * @author:高崖雷
 * @date:Nov 11, 2020 3:08:05 PM
 * @version:V1.0 版权：版权归作者所有，禁止外泄以及其他商业项目
 */
public class DataSourceTableRule {

	private String last_insert_id01 = "last_insert_id";

	private String last_insert_id02 = "LAST_INSERT_ID";

	/**
	 * 默认数据源key
	 */
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

		if (this.tableAndDbRuleMap == null || this.tableAndDbRuleMap.isEmpty()) {
			this.init();
		}

		Statement statement = null;

		try {

			boolean isLast = sql.indexOf(last_insert_id01) >= 0 || sql.indexOf(last_insert_id02) >= 0;

			if (isLast) {
				return new AnalysisResult(null, null, false);
			}

			statement = CCJSqlParserUtil.parse(sql);

			if (statement instanceof Select) {
				Select selectStatement = (Select) statement;
				TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
				List<String> tables = tablesNamesFinder.getTableList(selectStatement);
				List<String> dbSources = this.extractDbSource(tables);
				// this.logger.debug("sharding.dbtable:tables={},dbName={}",
				// JSON.toJSONString(tables),
				// JSON.toJSONString(dbSources));
				return new AnalysisResult(dbSources, tables);
			} else {
				return new AnalysisResult(null, null, false);
			}

		} catch (Exception e) {
			this.logger.error("sharding.dbtable-SQLParser异常:", e);
		}

		return new AnalysisResult(null, null, false);
	}
	
	public static void main(String[] args) throws JSQLParserException {
		String sql="select p.userId as authorId from cm_post p where p.id in ()";
		CCJSqlParserUtil.parse(sql);
	}

	private List<String> extractDbSource(List<String> tables) {

		if (tables.size() == 1) {
			List<String> dbs = this.tableAndDbRuleMap.get(tables.get(0));
			return dbs == null ? this.defaultDataSourceKeyList : dbs;
		}

		int retainCount = 0;
		List<String> retainResults = null;

		for (String sqlTable : tables) {

			List<String> dataSources = this.tableAndDbRuleMap.get(sqlTable);

			if (dataSources == null) {
				dataSources = new ArrayList<String>(1);
				dataSources.addAll(this.defaultDataSourceKeyList);
			}

			List<String> dst = new ArrayList<String>(dataSources);

			if (retainCount == 0) {
				retainResults = dst;
				retainCount++;
			} else {
				retainResults.retainAll(dst);
			}
		}

		return retainResults;
	}

	private synchronized void init() {

		if (this.tableAndDbRuleMap == null || this.tableAndDbRuleMap.isEmpty()) {

			this.tableAndDbRuleMap = new HashMap<String, List<String>>();
			Set<String> dataSourceKeys = this.dbAndTableRule.keySet();

			for (String dataSourceKey : dataSourceKeys) {

				List<String> tables = this.dbAndTableRule.get(dataSourceKey);

				for (String tableKey : tables) {

					List<String> dbKeys = this.tableAndDbRuleMap.get(tableKey);

					if (dbKeys == null) {
						dbKeys = new ArrayList<String>(3);
						this.tableAndDbRuleMap.put(tableKey, dbKeys);
					}

					dbKeys.add(dataSourceKey);
				}
			}
		}

		if (this.defaultDataSourceKey != null) {

			String[] keys = this.defaultDataSourceKey.split(",");
			this.defaultDataSourceKeyList = Arrays.asList(keys);
		}
	}

	class AnalysisResult {

		private List<String> dbSources;

		private List<String> tables;

		private boolean isSelectSql = true;

		public AnalysisResult(List<String> dbSources, List<String> tables) {
			super();
			this.dbSources = dbSources;
			this.tables = tables;
		}

		public AnalysisResult(List<String> dbSources, List<String> tables, boolean isSelectSql) {
			super();
			this.dbSources = dbSources;
			this.tables = tables;
			this.isSelectSql = isSelectSql;
		}

		public boolean isSelectSql() {
			return isSelectSql;
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
