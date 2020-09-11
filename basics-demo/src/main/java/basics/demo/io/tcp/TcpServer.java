package basics.demo.io.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

		ServerSocket server = null;

		try {

			InetAddress bindAddr = InetAddress.getByName(SERVICE_IP);
			server = new ServerSocket(SERVICE_PORT, this.backLog, bindAddr);

			while (true) {

				Socket clientConnect = server.accept();// 接受一个请求链接，阻塞当前线程
				this.receiveMsg(clientConnect);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			if (server != null) {

				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void receiveMsg(Socket socket) {

		try {

			InputStream in = socket.getInputStream();// 获取输入流
			byte[] inByte = new byte[1024];
			StringBuilder inSb = new StringBuilder();
			int i = 0;

			// read()方法一直读取信息（阻塞），直到返回值为-1结束，所有处理信息传输时一般自定义结束符
			for (i = in.read(inByte, 0, inByte.length); i > 0; i = in.read(inByte, 0, inByte.length)) {

				String content = new String(inByte);
				inSb.append(content);
				int indexEnd = content.lastIndexOf(this.END_CHAR);

				if (indexEnd >= 0) {

					// 执行命令
					RedisCommand command = RedisServer.analysis(inSb.toString().substring(0, indexEnd));
					String result = RedisServer.exec(command);
					System.out.println("exec result:" + result);

					this.sendMsg(result, socket);

					inSb = new StringBuilder();
					// break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMsg(String msg, Socket socket) {

		OutputStream out;

		try {
			out = socket.getOutputStream();
			out.write((msg + this.END_CHAR).getBytes());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
