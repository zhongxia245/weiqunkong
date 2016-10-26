package com.lenovo.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) {
		Socket socket = new Socket();
		InetSocketAddress inetSocketAddress = new InetSocketAddress(
				"127.0.0.1", 12345);
		try {
			Runtime.getRuntime().exec("adb forward tcp:12345 tcp:12345");
			socket.connect(inetSocketAddress, 3000);

			OutputStream outputStream = socket.getOutputStream();
			outputStream.write("$1".getBytes());

			InputStream inputStream = socket.getInputStream();
			byte[] b = new byte[10];
			int read = inputStream.read(b);
			if (read > 0) {
				System.out.println(new String(b));
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
