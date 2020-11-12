package shrding.dbtable.core;

import java.util.List;


public class DbTableRouteRecord {


	private String tableNames;


	private long exeNum;


	private List<String> refDbs;

	public DbTableRouteRecord(String tableNames, long exeNum, List<String> refDbs) {
		super();
		this.tableNames = tableNames;
		this.exeNum = exeNum;
		this.refDbs = refDbs;
	}

	public String getTableNames() {
		return tableNames;
	}

	public void setTableNames(String tableNames) {
		this.tableNames = tableNames;
	}

	public long getExeNum() {
		return exeNum;
	}

	public void setExeNum(long exeNum) {
		this.exeNum = exeNum;
	}

	public List<String> getRefDbs() {
		return refDbs;
	}

	public void setRefDbs(List<String> refDbs) {
		this.refDbs = refDbs;
	}
}
