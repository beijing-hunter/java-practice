package basics.demo.idefault;

/**
 * java8 特性 接口支持默认方法
 * 
 * @author cmt
 *
 */
public interface IDefaultInterface {

	void setName(String name);

	default String getName() {
		return "hello world";
	}
}

class DefaultTest implements IDefaultInterface {

	private String name;

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

}

class DefaultTestB implements IDefaultInterface {

	private String name;

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
