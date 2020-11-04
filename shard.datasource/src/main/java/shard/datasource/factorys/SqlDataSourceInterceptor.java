package shard.datasource.factorys;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

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

	public Object intercept(Invocation invocation) throws Throwable {

		String sql = ExecutorPluginUtils.getSqlByInvocation(invocation);

		String dataSourceKey = this.tableRule.dataSourceKey(sql);
		System.out.println(dataSourceKey);
		DataSourceContextHolder.setDataSourceKey(dataSourceKey);

		Object result = invocation.proceed();
		return result;
	}

	public Object plugin(Object target) {

		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {

	}

}
