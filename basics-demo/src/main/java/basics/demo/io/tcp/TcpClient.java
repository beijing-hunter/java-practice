package basics.demo.io.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {
	private Socket socket;

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

		client.receiveMsg();

		while (true) {

		}
	}

	/**
	 * 与服务器简历链接
	 * 
	 * @return
	 */
	private void connet() {

		try {
			this.socket = new Socket("127.0.0.1", 8907);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMsg(String msg) {

		msg = msg + this.END;

		try {
			this.socket.getOutputStream().write(msg.getBytes());
			this.socket.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receiveMsg() {

		byte[] inByte = new byte[1024];
		StringBuilder inSb = new StringBuilder();

		try {

			while (this.socket.getInputStream().read(inByte, 0, inByte.length) > 0) {

				String content = new String(inByte);
				inSb.append(content);

				if (content.lastIndexOf(this.END) >= 0) {
					System.out.println("client receive:" + inSb.toString());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
