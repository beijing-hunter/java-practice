package basics.demo.lambdas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lambda表达式可以看成是匿名内部类，使用Lambda表达式时，接口必须是函数式接口
 * 
 * @author cmt
 *
 */
public class LMain {

	public static void main(String[] args) {
		ILambdaTest t = (a, b) -> {
			return a - b;
		};

		System.out.println(t.sub(1, 1));

		ILambdaTestB tb = () -> System.out.println("hello world");
		tb.show();
		String name = tb.getName();
		System.out.println(name);

		List<Integer> datas = new ArrayList<Integer>();
		datas.add(3);
		datas.add(1);
		datas.add(5);
		datas.add(4);
		datas.add(0);
		datas.add(6);

		Collections.sort(datas, (a, b) -> {
			return a > b ? 1 : -1;
		});

		System.out.println(datas);

		datas.forEach(System.out::println);// 方法引用，引用的接口必须是函数式接口
	}
}
