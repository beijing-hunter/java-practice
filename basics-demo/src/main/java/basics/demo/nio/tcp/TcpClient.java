package basics.demo.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class TcpClient {
	private SocketChannel socket;

	private String END = "#";

	public static void main(String[] args) {

		TcpClient client = new TcpClient();
		client.connet();
		System.out.println(args[0]);

		if (args[0].equals("set")) {
			client.sendMsg("set&student&value1");
		} else if (args[0].equals("get")) {
			client.sendMsg("get&student");
		}

		while (true) {
			client.receiveMsg();
		}
	}

	/**
	 * 与服务器简历链接
	 * 
	 * @return
	 */
	private void connet() {

		try {

			this.socket = SocketChannel.open();
			this.socket.configureBlocking(false);
			this.socket.connect(new InetSocketAddress("127.0.0.1", 8907));

			while (!this.socket.finishConnect()) {
				System.out.println("同127.0.0.1" + "的连接正在建立，请稍等！");
				Thread.sleep(10);
			}

			System.out.println("同127.0.0.1已建立链接");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMsg(String msg) {

		msg = msg + this.END;

		try {

			ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());

			while (bb.hasRemaining()) {
				this.socket.write(bb);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receiveMsg() {

		try {

			ByteBuffer bb = ByteBuffer.allocate(1024);
			List<Byte> list = new ArrayList<Byte>();

			while (this.socket.read(bb) > 0) {

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
				System.out.println("client receive:" + content);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
