package shrding.dbtable.core;

import java.util.List;
import java.util.Map;
import java.util.Random;
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

	private static Random dbRandom = new Random();

	public static Map<String, Long> getRouteDbRecordMap() {
		return routeDbRecordMap;
	}

	public static Map<String, DbTableRouteRecord> getRouteTableRecordMap() {
		return routeTableRecordMap;
	}

	public static String getRouteDbName(List<String> defaultDbs, AnalysisResult result) {

		List<String> dbs = null;

		if (!result.isSuccess()) {
			dbs = defaultDbs;
		} else {
			dbs = result.getDbSources();
		}

		int index = dbRandom.nextInt(dbs.size());
		String dbKey = dbs.get(index);

		Long exeCount = routeDbRecordMap.get(dbKey);

		if (exeCount == null) {
			exeCount = 0L;
			routeDbRecordMap.put(dbKey, exeCount);
		}

		routeDbRecordMap.put(dbKey, exeCount + 1);
		return dbKey;
	}

	private static void tableExeRrecord(AnalysisResult result, List<String> dbs) {

		if (result.getTables() == null || result.getTables().isEmpty()) {
			return;
		}

		String joinTableKey = result.getJoinTableKey();
		DbTableRouteRecord record = routeTableRecordMap.get(joinTableKey);

		if (record == null) {

			record = new DbTableRouteRecord(joinTableKey, 0, dbs);
			routeTableRecordMap.put(joinTableKey, record);
		}

		record.setExeNum(record.getExeNum() + 1);
	}
}
