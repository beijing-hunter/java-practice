package shrding.dbtable.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import shrding.dbtable.core.DataSourceTableRule.AnalysisResult;

/**
 * 
 * @author:高崖雷
 * @date:Nov 11, 2020 3:26:29 PM
 * @version:V1.0 版权：版权归作者所有，禁止外泄以及其他商业项目
 */
public class DbRouteFactory {

	/**
	 * dbname命中次数
	 */
	private static Map<String, Long> routeDbRecordMap = new ConcurrentHashMap<String, Long>();

	/**
	 * join表key,执行记录
	 */
	private static Map<String, DbTableRouteRecord> routeTableRecordMap = new ConcurrentHashMap<String, DbTableRouteRecord>();

	public static Map<String, Long> getRouteDbRecordMap() {
		return routeDbRecordMap;
	}

	public static Map<String, DbTableRouteRecord> getRouteTableRecordMap() {
		return routeTableRecordMap;
	}

	/**
	 * 根据解析结果路由到db，db命中次数最少的负责此次sql执行
	 * 
	 * @param defaultDb
	 * @param result
	 * @return
	 */
	public static synchronized String getRouteDbName(List<String> defaultDbs, AnalysisResult result) {

		List<String> dbs = null;
		tableExeRrecord(result);

		if (!result.isSuccess()) {
			dbs = defaultDbs;
		} else {
			dbs = result.getDbSources();
		}

		String minDbkey = null;// 查询db命中次数最少的
		long minExeCount = 0l;
		int i = 0;// 辅助计算

		for (String dbKey : dbs) {

			Long exeCount = routeDbRecordMap.get(dbKey);

			if (exeCount == null) {
				exeCount = 0L;
				routeDbRecordMap.put(dbKey, exeCount);
			}

			if (i == 0) {
				minExeCount = exeCount;
				minDbkey = dbKey;
				i++;
			}

			if (exeCount <= minExeCount) {
				minExeCount = exeCount;
				minDbkey = dbKey;
			}
		}

		routeDbRecordMap.put(minDbkey, minExeCount + 1);
		return minDbkey;
	}

	/**
	 * 记录join表执行次数
	 * 
	 * @param result
	 */
	private static void tableExeRrecord(AnalysisResult result) {

		if (!result.isSuccess()) {
			return;
		}

		String joinTableKey = result.getJoinTableKey();
		DbTableRouteRecord record = routeTableRecordMap.get(joinTableKey);

		if (record == null) {

			record = new DbTableRouteRecord(joinTableKey, 0, result.getDbSources());
			routeTableRecordMap.put(joinTableKey, record);
		}

		record.setExeNum(record.getExeNum() + 1);
	}
}
