package basics.demo.idefault;

public class DMain {

	public static void main(String[] args) {
		DefaultTest dt = new DefaultTest();
		dt.setName("test");
		System.out.println(dt.getName());

		DefaultTestB dtb = new DefaultTestB();
		dtb.setName("testb");
		System.out.println(dtb.getName());
	}
}
