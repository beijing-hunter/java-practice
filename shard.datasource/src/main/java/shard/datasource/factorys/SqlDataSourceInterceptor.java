package shard.datasource.factorys;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.apache.ibatis.executor.CachingExecutor;

/**
 * 拦截sql，切换数据源
 * 
 * @author
 *
 */
@SuppressWarnings("rawtypes")
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class SqlDataSourceInterceptor implements Interceptor {

	@Autowired
	private DataSourceTableRule tableRule;

	private Map<String, DataSource> dataSourceMap;

	public Map<String, DataSource> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public Object intercept(Invocation invocation) throws Throwable {

		String sql = ExecutorPluginUtils.getSqlByInvocation(invocation);

		String dataSourceKey = this.tableRule.dataSourceKey(sql);
		System.out.println(dataSourceKey);

		// 通过改变当前事务中的connection属性值，实现动态数据库切换
		CachingExecutor ce = (CachingExecutor) invocation.getTarget();
		SpringManagedTransaction t = (SpringManagedTransaction) ce.getTransaction();

		Field fieldConnection = t.getClass().getDeclaredField("connection");
		Field fieldAutoCommit = t.getClass().getDeclaredField("autoCommit");
		Field fieldDataSource = t.getClass().getDeclaredField("dataSource");
		fieldConnection.setAccessible(true);
		fieldAutoCommit.setAccessible(true);
		fieldDataSource.setAccessible(true);

		DataSource dataSource = this.dataSourceMap.get(dataSourceKey);
		Connection connection = DataSourceUtils.getConnection(dataSource);
		fieldConnection.set(t, connection);
		fieldAutoCommit.set(t, connection.getAutoCommit());
		fieldDataSource.set(t, dataSource);

		Object result = invocation.proceed();
		return result;
	}

	public Object plugin(Object target) {

		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {

	}

}
