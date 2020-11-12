package shrding.dbtable.core;


import javax.sql.DataSource;

import org.apache.ibatis.plugin.Invocation;


public class TransactionConnection {

	private static String fieldConName = "connection";

	private static String fieldDbName = "dataSource";

	private static String fieldUConName = "unwrappedConnection";


	public static void setNewConnection(Invocation invocation, DataSource dataSource) throws Exception {


	}
}
