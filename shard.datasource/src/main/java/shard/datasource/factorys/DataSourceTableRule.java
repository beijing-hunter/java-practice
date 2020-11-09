package shard.datasource.factorys;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 数据源表规则
 * 
 * @author cmt
 *
 */
public class DataSourceTableRule {

	private String defaultDataSourceKey = "dataSource_db01";

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

		Statement statement = null;

		try {
			statement = CCJSqlParserUtil.parse(sql);

			// System.out.println(statement.toString());
			Select selectStatement = (Select) statement;
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tables = tablesNamesFinder.getTableList(selectStatement);

			Map<String, String> tableAndDataSourceMap = new HashMap<String, String>();// key:taleName value:dataSource

			for (String sqlTable : tables) {

				boolean isSuccess = false;

				for (String key : this.tableRule.keySet()) {

					List<String> tableNames = this.tableRule.get(key);

					for (String tableName : tableNames) {

						if (sqlTable.equals(tableName)) {
							tableAndDataSourceMap.put(sqlTable, key);
							isSuccess = true;
							break;
						}
					}

					if (isSuccess) {
						break;
					}
				}

				if (isSuccess == false) {
					tableAndDataSourceMap.put(sqlTable, defaultDataSourceKey);
				}

			}

			if (tableAndDataSourceMap.size() == 0) {
				return null;
			}

			if (tableAndDataSourceMap.size() == 1) {
				String dbKey = tableAndDataSourceMap.values().iterator().next();
				System.out.println(JSON.toJSONString(tables) + "," + dbKey);
			}

			Iterator<String> dataSources = tableAndDataSourceMap.values().iterator();
			String currentDataSourceKey = null;
			int dbSize = 0;

			while (dataSources.hasNext()) {
				String dataSourceKey = dataSources.next();

				if (StringUtils.isEmpty(currentDataSourceKey)) {
					dbSize++;
					currentDataSourceKey = dataSourceKey;
				} else {

					if (!currentDataSourceKey.equals(dataSourceKey)) {
						dbSize++;
					}
				}
			}

			if (dbSize == 1) {

				System.out.println(JSON.toJSONString(tables) + "," + currentDataSourceKey);
				return currentDataSourceKey;
			}

		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
