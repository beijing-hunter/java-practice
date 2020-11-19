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

	private static Map<String, Long> routeDbRecordMap = new ConcurrentHashMap<String, Long>();

	private static Map<String, DbTableRouteRecord> routeTableRecordMap = new ConcurrentHashMap<String, DbTableRouteRecord>();

	public static Map<String, Long> getRouteDbRecordMap() {
		return routeDbRecordMap;
	}

	public static Map<String, DbTableRouteRecord> getRouteTableRecordMap() {
		return routeTableRecordMap;
	}

	public static String getRouteDbName(List<String> defaultDbs, AnalysisResult result) {

		List<String> dbs = null;
		tableExeRrecord(result);

		if (!result.isSuccess()) {
			dbs = defaultDbs;
		} else {
			dbs = result.getDbSources();
		}

		String minDbkey = null;
		long minExeCount = 0l;
		int i = 0;

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
