package com.lenovo.ScreenCapture;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;

/**
 * 主页面,手机屏幕实时投射到PC端,PC端通过adb控制手机【反向控制】
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

  public static void main(String[] args) {
    new MainWindow();
  }

  /**
   * PC端应用页面
   */
  public MainWindow() {
    super();
    initWindow();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(200, 200);

    setVisible(true);
    setResizable(true);
  }

  JLabel label;   //用来放置屏幕截图的组件
  public static String targetNum = null;
  public static IDevice device = null;
  public Thread th = null;
  public OperateAndroid oa = null;
  public int width, height;
  public double zoom = 1;


  /**
   * 初始化窗口
   * 根据ADB获取链接到设备上的设备列表,展示在设备列表的菜单下
   */
  public void initWindow() {
    // final MyChimpChat mych = new MyChimpChat();

    label = new JLabel();
    add(label);
    label.setHorizontalAlignment(SwingConstants.CENTER);

    String adbLocation = System.getProperty("com.android.screenshot.bindir");

    if (adbLocation != null && adbLocation.length() != 0) {
      adbLocation += File.separator + "adb";
    } else {
      adbLocation = "adb";
    }

    AndroidDebugBridge.init(false);
    AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(adbLocation, true);
    int count = 0;

    System.out.println("是否存在设备:" + bridge.hasInitialDeviceList());

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
        return;
      }
    }

    //android机器列表
    final IDevice[] devices = bridge.getDevices();

    JMenu fileMenu = new JMenu("设备列表");
    JMenu functionMenu = new JMenu("功能键");
    JMenu functionZoom = new JMenu("缩放");

    JMenuItem ItemHome = new JMenuItem("HOME");
    JMenuItem ItemBack = new JMenuItem("BACK");
    JMenuItem ItemMenu = new JMenuItem("MENU");
    //JMenuItem ItemPower = new JMenuItem("POWER");

    functionMenu.add(ItemHome);
    functionMenu.add(ItemBack);
    functionMenu.add(ItemMenu);
    //functionMenu.add(ItemPower);

    JMenuItem Item100 = new JMenuItem("100%");
    JMenuItem Item80 = new JMenuItem("80%");
    JMenuItem Item50 = new JMenuItem("50%");

    functionZoom.add(Item100);
    functionZoom.add(Item80);
    functionZoom.add(Item50);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(functionMenu);
    menuBar.add(functionZoom);

    JMenuItem newMenuItem = null;

    //获取连接的设备列表,然后动态生成到设备列表中
    for (IDevice d : devices) {
      newMenuItem = new JMenuItem(d.getName());
      newMenuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent arg0) {
          System.out.println(arg0.getActionCommand());

          targetNum = arg0.getActionCommand();
          targetNum = targetNum.substring(targetNum.lastIndexOf("-") + 1);

          for (IDevice d : devices) {
            if (d.getSerialNumber().equals(targetNum)) {

              device = d;
              oa = new OperateAndroid(device);

              //System.out.printf("width:%d,height:%d", oa.getScreenWidth(), oa.getScreenHeight());
              // oa = OperateAndroid.getOperateAndroid(device);
              initResolution();

              setSize(width, (height + 60));

              if (!th.isAlive()) {
                th.start();
              }
            }
          }
        }
      });
      fileMenu.add(newMenuItem);
    }

    if (devices.length < 1) {
      fileMenu.setText("没有设备");
      fileMenu.setEnabled(false);
      functionMenu.setEnabled(false);
    }

    ItemHome.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        oa.press(OperateAndroid.HOME);
      }
    });
    ItemBack.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        oa.press(OperateAndroid.BACK);
      }
    });
    ItemMenu.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        oa.press(OperateAndroid.MENU);
      }
    });

    // ItemPower.addActionListener(new ActionListener() {
    //
    // @Override
    // public void actionPerformed(ActionEvent arg0) {
    // oa.press(OperateAndroid.POWER);
    // }
    // });

    Item100.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        zoom = 1;
        initResolution();
        width = (int) (width * zoom);
        height = (int) (height * zoom);
        setSize(width, height + 60);
      }
    });

    Item80.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        zoom = 0.8;
        initResolution();
        width = (int) (width * zoom);
        height = (int) (height * zoom);
        setSize(width, height + 60);
      }
    });
    Item50.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        zoom = 0.5;
        initResolution();
        width = (int) (width * zoom);
        height = (int) (height * zoom);
        setSize(width, height + 60);
      }
    });

    setJMenuBar(menuBar);

    th = new Thread(new Runnable() {

      @Override
      public void run() {
        ImageIcon image = null;
        while (true) {

          image = new ImageIcon("no.gif");

          image.setImage(getImageIcon(targetNum).getImage()
              .getScaledInstance(width, height, Image.SCALE_DEFAULT));

          label.setIcon(image);
        }
      }
    });

    label.addMouseListener(new MouseListener() {

      @Override
      public void mouseReleased(MouseEvent e) {
        try {
          oa.touchUp((int) (e.getX() / zoom), (int) (e.getY() / zoom));
          System.out.printf("mouseReleased,x=%d,y=%d", e.getX(), e.getY());
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        try {
          oa.touchDown((int) (e.getX() / zoom), (int) (e.getY() / zoom));
          System.out.printf("mousePressed,x=%d,y=%d", e.getX(), e.getY());
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
      }
    });

    label.addMouseMotionListener(new MouseMotionListener() {

      @Override
      public void mouseMoved(MouseEvent e) {
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        try {
          oa.touchMove((int) (e.getX() / zoom),
              (int) (e.getY() / zoom));
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    label.addMouseWheelListener(new MouseWheelListener() {

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

  /**
   * PC端展示设备屏幕截图的容器大小
   */
  public void initResolution() {
    width = 480;  // oa.getScreenWidth();
    height = 800;  // oa.getScreenHeight();
    //width = oa.getScreenWidth() / scale;
    //height = oa.getScreenHeight() / scale;
  }

  /**
   * 获取屏幕截图
   *
   * @param targetNum 当前设备所在下标
   * @return
   */
  public ImageIcon getImageIcon(String targetNum) {
    try {
      long start = System.currentTimeMillis();
      RawImage rawImage = device.getScreenshot();
      long end = System.currentTimeMillis();
      System.out.println("获取设备屏幕的时间" + (end - start) + "毫秒");

      BufferedImage image = new BufferedImage(rawImage.width, rawImage.height, BufferedImage.TYPE_INT_RGB);

      int index = 0;
      int IndexInc = rawImage.bpp >> 3;
      for (int y = 0; y < rawImage.height; y++) {
        for (int x = 0; x < rawImage.width; x++) {
          int value = rawImage.getARGB(index);
          index += IndexInc;
          image.setRGB(x, y, value);
        }
      }
      return new ImageIcon(image);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
