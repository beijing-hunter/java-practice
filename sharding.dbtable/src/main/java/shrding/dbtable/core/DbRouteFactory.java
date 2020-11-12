package shrding.dbtable.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import shrding.dbtable.core.DataSourceTableRule.AnalysisResult;

public class DbRouteFactory {

	private static Map<String, Long> routeDbRecordMap = new ConcurrentHashMap<String, Long>();

	private static Map<String, DbTableRouteRecord> routeTableRecordMap = new ConcurrentHashMap<String, DbTableRouteRecord>();

	public static Map<String, Long> getRouteDbRecordMap() {
		return routeDbRecordMap;
	}

	public static Map<String, DbTableRouteRecord> getRouteTableRecordMap() {
		return routeTableRecordMap;
	}

	public static synchronized String getRouteDbName(List<String> defaultDbs, AnalysisResult result) {

		return null;
	}


	private static void tableExeRrecord(AnalysisResult result) {

	}
}
