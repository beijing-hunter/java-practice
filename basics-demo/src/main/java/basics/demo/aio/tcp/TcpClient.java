package basics.demo.aio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpClient {

	public static final String SERVICE_IP = "127.0.0.1";

	public static final int SERVICE_PORT = 8907;

	public static final String END_CHAR = "#";

	private AsynchronousChannelGroup channelGroup;

	public TcpClient() {

		try {

			ExecutorService executor = Executors.newFixedThreadPool(20);
			this.channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TcpClient client = new TcpClient();

		System.out.println(args[0]);

		if (args[0].equals("set")) {
			client.connet("set&student&value1#");
		} else if (args[0].equals("get")) {
			client.connet("get&student#");
		}

		while (true) {
			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void connet(String content) {

		try {

			AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(this.channelGroup);
			socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
			socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			socketChannel.connect(new InetSocketAddress(SERVICE_IP, SERVICE_PORT), socketChannel, new AioConnectHandler(content));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class AioConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

	private String content;

	public AioConnectHandler(String content) {
		this.content = content;
	}

	public void completed(Void result, AsynchronousSocketChannel socket) {

		try {
			socket.write(ByteBuffer.wrap(this.content.getBytes())).get();
			this.startRead(socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void failed(Throwable exc, AsynchronousSocketChannel scoket) {

	}

	private void startRead(AsynchronousSocketChannel socket) {

		ByteBuffer bb = ByteBuffer.allocate(1024);
		socket.read(bb, bb, new AioReadHandler(socket));
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

				String content = decoder.decode(bb).toString();
				System.out.println("收到" + socket.getRemoteAddress().toString() + "的消息:" + content);
				bb.compact();

			} catch (Exception e) {
				e.printStackTrace();
			}

			this.socket.read(bb, bb, this);
		} else if (result == -1) {

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
