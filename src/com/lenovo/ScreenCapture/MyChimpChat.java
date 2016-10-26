package com.lenovo.ScreenCapture;

import java.net.Socket;

public class MyChimpChat {

	public Socket socket;

	public MyChimpChat() {

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Runtime.getRuntime()
							.exec("adb forward tcp:12345 tcp:12345");
					Runtime.getRuntime().exec("adb shell monkey --port 12345");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		th.start();

		try {
			Thread.sleep(1000);
			socket = new Socket("127.0.0.1", 12345);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void touchDown(int x, int y) {
		try {
			socket.getOutputStream().write(
					new String("touch down " + x + " " + y + "\n").getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void touchUp(int x, int y) {
		try {
			socket.getOutputStream().write(
					new String("touch up " + x + " " + y + "\n").getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Press() {
		try {
			socket.getOutputStream().write(
					new String("press KEYCODE_HOME\n").getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void touchMove(int x, int y) {
		try {
			socket.getOutputStream().write(
					new String("touch move " + x + " " + y + "\n").getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
