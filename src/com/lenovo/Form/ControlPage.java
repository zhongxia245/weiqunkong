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
  private IDevice _device = null;
  private OperateAndroid oa = null;
  private double zoom = 2.5;
  private int width = (int) (1080 / zoom);
  private int height = (int) (1920 / zoom);

  public ControlPage(IDevice device) throws HeadlessException {
    _device = device;
    oa = new OperateAndroid(device);
    panel1.setSize(width, height);
    initEvent();

    ImageThread it = new ImageThread(lblImage, device, width, height);

    if (!it.isAlive()) {
      it.start();
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
          oa.touchUp((int) (e.getX() * zoom), (int) (e.getY() * zoom));
          System.out.printf("mouseReleased,x=%d,y=%d", e.getX(), e.getY());
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        try {
          oa.touchDown((int) (e.getX() * zoom), (int) (e.getY() * zoom));
          System.out.printf("mousePressed,x=%d,y=%d", e.getX(), e.getY());
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
        } else if (e.getWheelRotation() == -1) {
          oa.press("KEYCODE_DPAD_UP");
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
  }
}
