package shard.datasource.factorys;

public class DataSourceContextHolder {

	private static final ThreadLocal<String> context = new ThreadLocal<String>();

	public static void setDataSourceKey(String key) {
		context.set(key);
	}

	public static String getDataSourceKey() {
		return context.get();
	}

	public static void clear() {
		context.remove();
	}
}
