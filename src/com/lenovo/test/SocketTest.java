package com.lenovo.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTest {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		ServerSocket ss = new ServerSocket(6666);
		while (true) {
			Socket accept = ss.accept();
			InputStream inputStream = accept.getInputStream();
			byte[] b = new byte[2];
			inputStream.read(b);

		}
	}

}
