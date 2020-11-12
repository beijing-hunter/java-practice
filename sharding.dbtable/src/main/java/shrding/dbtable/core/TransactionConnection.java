package shrding.dbtable.core;

import java.lang.reflect.Field;
import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.plugin.Invocation;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 
 * @author:高崖雷
 * @date:Nov 11, 2020 4:01:57 PM
 * @version:V1.0 版权：版权归作者所有，禁止外泄以及其他商业项目
 */
public class TransactionConnection {

	private static String fieldConName = "connection";

	private static String fieldDbName = "dataSource";

	private static String fieldUConName = "unwrappedConnection";

	public static void setNewConnection(Invocation invocation, DataSource dataSource) throws Exception {

		CachingExecutor executor = (CachingExecutor) invocation.getTarget();
		SpringManagedTransaction transaction = (SpringManagedTransaction) executor.getTransaction();
		transaction.close();

		Field fieldConnection = transaction.getClass().getDeclaredField(fieldConName);
		Field fieldDataSource = transaction.getClass().getDeclaredField(fieldDbName);
		Field fieldUnwrappedConnection = transaction.getClass().getDeclaredField(fieldUConName);
		fieldConnection.setAccessible(true);
		fieldDataSource.setAccessible(true);
		fieldUnwrappedConnection.setAccessible(true);

		Connection connection = DataSourceUtils.getConnection(dataSource);
		fieldConnection.set(transaction, connection);
		fieldDataSource.set(transaction, dataSource);
		fieldUnwrappedConnection.set(transaction, connection);
	}
}
