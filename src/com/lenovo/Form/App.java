package com.lenovo.Form;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.lenovo.ScreenCapture.MainWindow;
import com.lenovo.ScreenCapture.OperateAndroid;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhongxia on 10/24/16.
 */
public class App {
  private JPanel pApp;
  private JButton btnWX;
  private JButton btnConnect;
  private JButton btnHome;
  private JButton btnBack;
  private JButton btnMenu;
  private JPanel panelDevices;
  private JPanel pLeft;
  private JScrollPane spMain;
  private JButton btnControl;

  private static IDevice[] devices;  //所有的设备数组
  private static ArrayList<JLabel> deviceList = new ArrayList(100);//所有展示设备屏幕截图的组件
  private static ArrayList<OperateAndroid> oaList = new ArrayList(100);//所有展示设备屏幕截图的组件

  /**
   * 构造函数
   */
  public App() {
    bindEvent();
    devices = getDevices();
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("App");
    frame.setContentPane(new App().pApp);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setSize(800, 600);
    frame.setVisible(true);
  }

  /**
   * 绑定事件
   */
  public void bindEvent() {
    //打开微信
    btnWX.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        oaList.get(0).openWeiXin();
      }
    });

    //群控微信,弹出操作面板
    btnControl.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        JFrame frame = new JFrame("操作页面");
        ControlPage cp = new ControlPage(devices[0]);
        frame.setContentPane(cp.panel1);
        frame.setSize(cp.panel1.getWidth(), cp.panel1.getHeight() + 20);
        frame.setVisible(true);
      }
    });

    //连接设备
    btnConnect.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        btnConnect.setEnabled(false);
        if (devices != null) {
          if (devices.length > 0) {
            initDevices(devices);
          } else {
            JOptionPane.showMessageDialog(pApp, "没有检测到设备连接该电脑");
          }
        }
      }
    });

    //返回首页
    btnHome.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        for (OperateAndroid oa : oaList) {
          if (oa != null) {
            try {
              oa.press(OperateAndroid.HOME);
            } catch (Exception ev) {
              System.out.println("按home键报错");
              ev.printStackTrace();
            }
          }
        }
      }
    });

    //返回键
    btnBack.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        for (OperateAndroid oa : oaList) {
          if (oa != null) {
            try {
              oa.press(OperateAndroid.BACK);
            } catch (Exception ev) {
              System.out.println("按back键报错");
              ev.printStackTrace();
            }
          }
        }
      }
    });

    //菜单键
    btnMenu.addMouseListener(new ZxMouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        for (OperateAndroid oa : oaList) {
          if (oa != null) {
            try {
              oa.press(OperateAndroid.MENU);
            } catch (Exception ev) {
              System.out.println("按menu键报错");
              ev.printStackTrace();
            }
          }
        }
      }
    });
  }

  /**
   * 获取设备列表
   *
   * @return
   */
  public IDevice[] getDevices() {
    String adbLocation = System.getProperty("com.android.screenshot.bindir");

    if (adbLocation != null && adbLocation.length() != 0) {
      adbLocation += File.separator + "adb";
    } else {
      adbLocation = "adb";
    }

    AndroidDebugBridge.terminate();
    AndroidDebugBridge.init(false);
    AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(adbLocation, true);

    int count = 0;

    /**
     * 循环100次,每次间隔0.1s, 去获取是否有设备连接到电脑上
     */
    while (bridge.hasInitialDeviceList() == false) {
      try {
        Thread.sleep(100);
        count++;
      } catch (InterruptedException e) {

      }

      if (count > 100) {
        System.err.println("获取设备超时,请检查是否把设备用USB连接到PC上");
        JOptionPane.showMessageDialog(pApp, "获取设备超时,请检查是否把设备用USB连接到PC上");
        break;
      }
    }
    //android机器列表
    IDevice[] devices = bridge.getDevices();
    return devices;
  }

  /**
   * 把设备列表的设备展示到面板上
   *
   * @param devices
   */
  public void initDevices(IDevice[] devices) {
    //获取连接的设备列表,然后动态生成到设备列表中
    for (IDevice d : devices) {
      OperateAndroid oa = new OperateAndroid(d);

      JLabel label = new JLabel();
      label.setSize(270, 480);
      label.setToolTipText(d.getName());

      panelDevices.add(label);

      deviceList.add(label);
      oaList.add(oa);

      initDeviceImage(label, d);
    }
  }

  /**
   * 初始化设备屏幕截图到应用上
   *
   * @param label  放置屏幕截图的组件
   * @param device 设备对象
   */
  public void initDeviceImage(JLabel label, IDevice device) {
    ImageThread it = new ImageThread(label, device);
    if (!it.isAlive()) {
      it.start();
    }
  }
}
