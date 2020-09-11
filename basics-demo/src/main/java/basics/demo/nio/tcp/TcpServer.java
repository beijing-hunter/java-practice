package basics.demo.nio.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basics.demo.io.redis.utils.RedisCommand;
import basics.demo.io.redis.utils.RedisServer;

public class TcpServer {

	public static final String SERVICE_IP = "127.0.0.1";

	public static final int SERVICE_PORT = 8907;

	public final String END_CHAR = "#";

	private final int backLog = 100;

	public static void main(String[] args) {

		TcpServer tcpServer = new TcpServer();
		tcpServer.startListen();
	}

	private void startListen() {

		try {

			// 打开一个ServerSocketChannel
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(InetAddress.getByName(SERVICE_IP), SERVICE_PORT));
			// 设置ServerSocketChannel为非阻塞模式
			ssc.configureBlocking(false);

			// 打开一个选择器
			Selector selector = Selector.open();

			// 将ServerSocketChannel注册到选择器上去并监听accept事件
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			while (selector.select() > 0) {// 这里会发生阻塞，等待就绪的通道,但在每次select()方法调用之间，只有一个通道就绪了。

				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

				while (iterator.hasNext()) {

					SelectionKey selectionKey = (SelectionKey) iterator.next();

					if (selectionKey.isAcceptable()) {// 通道上是否有可接受的连接

						ServerSocketChannel sscTmp = (ServerSocketChannel) selectionKey.channel();// 获取在selector上注册的ServerSocketChannel即：ssc
						SocketChannel newSC = sscTmp.accept();// accept()方法会一直阻塞到有新连接到达。
						newSC.configureBlocking(false);
						newSC.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);// 将新监听到的链接，注册到selector

					} else if (selectionKey.isReadable()) {// 通道上是否有数据可读
						this.receiveMsg(selectionKey);
					}

					if (selectionKey.isWritable()) {// 测试写入数据，若写入失败在会自动取消注册该键
					}

					// 必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
					iterator.remove();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receiveMsg(SelectionKey selectionKey) {

		try {

			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			ByteBuffer bb = ByteBuffer.allocate(1024);
			List<Byte> list = new ArrayList<Byte>();

			while (socketChannel.read(bb) > 0) {

				bb.flip();

				while (bb.hasRemaining()) {
					list.add(bb.get());
				}
			}

			bb.clear();

			byte[] bytes = new byte[list.size()];

			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = list.get(i);
			}

			String content = (new String(bytes)).trim();

			if (!content.isEmpty()) {

				int indexEnd = content.lastIndexOf(this.END_CHAR);

				if (indexEnd >= 0) {

					// 执行命令
					RedisCommand command = RedisServer.analysis(content.substring(0, indexEnd));
					String result = RedisServer.exec(command);
					System.out.println("exec result:" + result);

					this.sendMsg(result, socketChannel);// 返回执行结果
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMsg(String msg, SocketChannel socketChannel) {

		try {

			//SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			ByteBuffer bb = ByteBuffer.wrap((msg + this.END_CHAR).getBytes());

			while (bb.hasRemaining()) {
				socketChannel.write(bb);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}