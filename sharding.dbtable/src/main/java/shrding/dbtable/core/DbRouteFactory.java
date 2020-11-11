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
	private static Map<String, Long> routeRecordMap = new ConcurrentHashMap<String, Long>();

	public static Map<String, Long> getRouteRecordMap() {
		return routeRecordMap;
	}

	/**
	 * 根据解析结果路由到db，db命中次数最少的负责此次sql执行
	 * 
	 * @param defaultDb
	 * @param result
	 * @return
	 */
	public static synchronized String getRouteDbName(String defaultDb, AnalysisResult result) {

		if (result == null || !result.isSuccess()) {
			return defaultDb;
		}

		List<String> dbs = result.getDbSources();
		String minDbkey = null;// 查询db命中次数最少的
		long minExeCount = 0l;

		for (String dbKey : dbs) {

			Long exeCount = routeRecordMap.get(dbKey);

			if (exeCount == null) {
				exeCount = 0L;
				routeRecordMap.put(dbKey, exeCount);
			}

			if (minExeCount == 0) {
				minExeCount = exeCount;
				minDbkey = dbKey;
			}

			if (exeCount <= minExeCount) {
				minExeCount = exeCount;
				minDbkey = dbKey;
			}
		}

		routeRecordMap.put(minDbkey, minExeCount + 1);
		return minDbkey;
	}
}
