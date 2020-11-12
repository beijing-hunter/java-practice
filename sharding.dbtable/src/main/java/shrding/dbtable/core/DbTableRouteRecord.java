package shrding.dbtable.core;

import java.util.List;

/**
 * 
 * @author:高崖雷
 * @date:Nov 12, 2020 11:55:38 AM
 * @version:V1.0 版权：版权归作者所有，禁止外泄以及其他商业项目
 */
public class DbTableRouteRecord {

	/**
	 * join表key
	 */
	private String tableNames;

	/**
	 * join表key,执行次数
	 */
	private long exeNum;

	/**
	 * join表key，常命中的数据库
	 */
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
