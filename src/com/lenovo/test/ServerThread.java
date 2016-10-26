package com.lenovo.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThread extends Thread {
  private Socket so;

  public ServerThread(Socket so) {
    this.so = so;
  }

  public void run() {
    try {
      InputStream is = so.getInputStream();
      OutputStream os = so.getOutputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      PrintStream ps = new PrintStream(os);
      while (true) {
        String temp = br.readLine();// �ͻ�Ҫ��println
        ps.println("from server:" + temp);// ������

        // if (temp.equals("bye")) {
        // break;
        // }
      }
      // ps.close();
      // br.close();
      // so.close();
    } catch (Exception rr) {
      rr.printStackTrace();
    }
  }
}
