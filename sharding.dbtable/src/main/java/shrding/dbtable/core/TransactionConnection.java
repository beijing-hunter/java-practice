package shrding.dbtable.core;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Invocation;

public class TransactionConnection {

	private static String fieldConName = "connection";

	private static String fieldDbName = "dataSource";

	private static String fieldUConName = "unwrappedConnection";

	public static TransactionContext setNewConnection(Invocation invocation, DataSource dataSource) throws Exception {

		return new TransactionContext();
	}

	public static void close(Invocation invocation, TransactionContext Context) throws Exception {

	}
}
