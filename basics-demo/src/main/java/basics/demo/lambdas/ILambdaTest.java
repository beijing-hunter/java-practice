package basics.demo.lambdas;

/**
 * Lambda表达式可以看成是匿名内部类，使用Lambda表达式时，接口必须是函数式接口
 * 
 * @author cmt
 *
 */
public interface ILambdaTest {// 函数式接口：即只有一个抽象方法（可以有多个默认方法）

	int sub(int a, int b);
}

interface ILambdaTestB {// 函数式接口：即只有一个抽象方法（可以有多个默认方法）
	void show();

	default String getName() {
		return "ILambdaTestB";
	}
}

interface ILambdaTestC {// 此接口不是函数式接口

	void setName(String name);

	String getName();
}
