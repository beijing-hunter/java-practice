package basics.demo.aio.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import basics.demo.io.redis.utils.RedisCommand;
import basics.demo.io.redis.utils.RedisServer;

public class TcpServer {

	public static final String SERVICE_IP = "127.0.0.1";

	public static final int SERVICE_PORT = 8907;

	public static final String END_CHAR = "#";

	private AsynchronousServerSocketChannel serverSocketChannel;

	private AsynchronousChannelGroup channelGroup;

	public static void main(String[] args) {

		TcpServer server = new TcpServer();
		server.startListen();

		while (true) {

			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void startListen() {

		try {

			ExecutorService executor = Executors.newFixedThreadPool(20);
			this.channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
			this.serverSocketChannel = AsynchronousServerSocketChannel.open(this.channelGroup).bind(new InetSocketAddress(InetAddress.getByName(SERVICE_IP), SERVICE_PORT));

			this.serverSocketChannel.accept(this.serverSocketChannel, new AioAcceptHandler());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

	/**
	 * 当IO完成时触发该方法，该方法的第一个参数代表IO操作返回的对象，第二个参数代表发起IO操作时传入的附加参数
	 */
	public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {

		attachment.accept(attachment, this);

		try {
			System.out.println("有客户端连接:" + result.getRemoteAddress().toString());
			this.startRead(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 当IO失败时触发该方法，第一个参数代表IO操作失败引发的异常或错误。
	 */
	public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
		// TODO Auto-generated method stub

	}

	public void startRead(AsynchronousSocketChannel socket) {
		ByteBuffer clientBuffer = ByteBuffer.allocate(1024);
		socket.read(clientBuffer, clientBuffer, new AioReadHandler(socket));
	}
}

class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel socket;

	private CharsetDecoder decoder = Charset.forName("GBK").newDecoder();

	public AioReadHandler(AsynchronousSocketChannel socket) {
		this.socket = socket;
	}

	public void completed(Integer result, ByteBuffer bb) {

		if (result > 0) {

			bb.flip();

			try {
				System.out.println("收到" + socket.getRemoteAddress().toString() + "的消息:" + decoder.decode(bb));
				bb.compact();

				String content = decoder.decode(bb).toString();

				if (!content.isEmpty()) {

					int indexEnd = content.lastIndexOf(TcpServer.END_CHAR);

					if (indexEnd >= 0) {

						// 执行命令
						RedisCommand command = RedisServer.analysis(content.substring(0, indexEnd));
						String resultStr = RedisServer.exec(command);
						System.out.println("exec result:" + resultStr);

						this.socket.write(ByteBuffer.wrap(resultStr.getBytes())).get();// 返回执行结果
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			this.socket.read(bb, bb, this);
		} else {

			try {
				System.out.println("客户端断线:" + socket.getRemoteAddress().toString());
				bb = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub

	}

}
