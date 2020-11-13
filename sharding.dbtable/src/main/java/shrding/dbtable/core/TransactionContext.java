package shrding.dbtable.core;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Invocation;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class TransactionContext {

	private SpringManagedTransaction transaction;

	private Object oldCon;

	private Object oldUCon;

	private Object oldDataSource;

	private DataSource newDataSource;

	private Connection newConnection;

	public TransactionContext(SpringManagedTransaction transaction, Object oldCon, Object oldUCon, Object oldDataSource, DataSource newDataSource, Connection newConnection) {
		super();
		this.transaction = transaction;
		this.oldCon = oldCon;
		this.oldUCon = oldUCon;
		this.oldDataSource = oldDataSource;
		this.newDataSource = newDataSource;
		this.newConnection = newConnection;
	}

	public SpringManagedTransaction getTransaction() {
		return transaction;
	}

	public Object getOldCon() {
		return oldCon;
	}

	public Object getOldUCon() {
		return oldUCon;
	}

	public Object getOldDataSource() {
		return oldDataSource;
	}

	public DataSource getNewDataSource() {
		return newDataSource;
	}

	public Connection getNewConnection() {
		return newConnection;
	}

}
