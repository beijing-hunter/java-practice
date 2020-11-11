package shrding.dbtable.core;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;

/**
 * 
 * @author:高崖雷
 * @date:Nov 11, 2020 3:08:40 PM
 * @version:V1.0 版权：版权归作者所有，禁止外泄以及其他商业项目
 */
public class ExecutorPluginUtils {

	/**
	 * 获取sql语句
	 * 
	 * @param invocation
	 * @return
	 */
	public static String getSqlByInvocation(Invocation invocation) {
		final Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameterObject = args[1];
		BoundSql boundSql = ms.getBoundSql(parameterObject);
		return boundSql.getSql();
	}

	// 定义一个内部辅助类，作用是包装sq
	static class BoundSqlSqlSource implements SqlSource {
		private BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
}
