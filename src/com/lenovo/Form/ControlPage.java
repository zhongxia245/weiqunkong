package com.lenovo.Form;

import com.android.ddmlib.IDevice;
import com.lenovo.ScreenCapture.OperateAndroid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by zhongxia on 10/25/16.
 */
public class ControlPage extends JFrame {
  public JPanel panel1;
  public JLabel lblImage;
  private JTextPane tpCommand;
  private JButton btnExec;
  private IDevice _device = null;
  private OperateAndroid oa = null;
  private double zoom = 2.5;
  private int width = (int) (1080 / zoom);
  private int height = (int) (1920 / zoom);
  private ImageThread it = null;
  private Boolean recordFlag = false; //是否录制脚本
  private String command = "";  //命令


  public ControlPage(IDevice device, OperateAndroid _oa) throws HeadlessException {
    _device = device;
    oa = _oa;
    lblImage.setSize(width, height);
    initEvent();

    it = new ImageThread(lblImage, device, width, height);

    if (!it.isAlive()) {
      it.start();
    }
  }

  /**
   * 停止线程
   */
  public void stopThread() {
    if (it != null && it.isAlive()) {
      it.stop();
      System.out.println("中断操作设备的线程!");
    }
  }

  /**
   * 初始化操作事件
   */
  public void initEvent() {
    lblImage.addMouseListener(new ZxMouseListener() {
      @Override
      public void mouseReleased(MouseEvent e) {
        try {
          int x = (int) (e.getX() * zoom);
          int y = (int) (e.getY() * zoom);
          oa.touchUp((int) (e.getX() * zoom), (int) (e.getY() * zoom));
          command += "touch up " + x + " " + y + "\n";
          setCommandText();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        try {
          int x = (int) (e.getX() * zoom);
          int y = (int) (e.getY() * zoom);
          oa.touchDown(x, y);
          command += "touch down " + x + " " + y + "\n";
          setCommandText();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });

    lblImage.addMouseMotionListener(new MouseMotionListener() {

      @Override
      public void mouseMoved(MouseEvent e) {
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        try {
          oa.touchMove((int) (e.getX() * zoom), (int) (e.getY() * zoom));
          System.out.println("move");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    lblImage.addMouseWheelListener(new MouseWheelListener() {

      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == 1) {
          oa.press("KEYCODE_DPAD_DOWN");
          command += "press KEYCODE_DPAD_DOWN \n";
        } else if (e.getWheelRotation() == -1) {
          oa.press("KEYCODE_DPAD_UP");
          command += "press KEYCODE_DPAD_UP \n";
        }
      }
    });

    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
          case KeyEvent.VK_BACK_SPACE:
            oa.press("KEYCODE_DEL");
            break;
          case KeyEvent.VK_SPACE:
            oa.press("KEYCODE_SPACE");
            break;
          case KeyEvent.VK_DELETE:
            oa.press("KEYCODE_FORWARD_DEL");
            break;
          case KeyEvent.VK_UP:
            oa.press("KEYCODE_DPAD_UP");
            break;
          case KeyEvent.VK_DOWN:
            oa.press("KEYCODE_DPAD_DOWN");
            break;
          case KeyEvent.VK_LEFT:
            oa.press("KEYCODE_DPAD_LEFT");
            break;
          case KeyEvent.VK_RIGHT:
            oa.press("KEYCODE_DPAD_RIGHT");
            break;
          case KeyEvent.VK_ENTER:
            oa.press("KEYCODE_ENTER");
            break;
          case KeyEvent.VK_CONTROL:
            break;
          case KeyEvent.VK_ALT:
            break;
          case KeyEvent.VK_SHIFT:
            break;
          default:
            oa.type(e.getKeyChar());
        }

      }
    });

    /**
     * 执行录制的操作
     * */
    btnExec.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        String[] commands = command.split("\n");
        for (int i = 0; i < commands.length; i++) {
          try {
            oa.shell(commands[i]);
            /**
             * touch down  和 touch up  两个步骤和在一起,才是一个完整的操作
             * 每一个操作之间间隔2s
             */
            if (commands[i].contains("touch up")) {
              Thread.sleep(2000);
            }

          } catch (InterruptedException ev) {

          }
        }
      }
    });
  }

  /**
   * 设置文本域显示命令
   */
  public void setCommandText() {
    tpCommand.setText(command);
  }
}
