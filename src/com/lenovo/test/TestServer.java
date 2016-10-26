package com.lenovo.test;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class TestServer extends Thread {
	private ServerSocket ss = null;

	public TestServer() {
		try {
			ss = new ServerSocket(7456);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Socket so = ss.accept();
				ServerThread st = new ServerThread(so);
				st.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new TestServer().start();
	}

}