package com.lenovo.Form;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.lenovo.ScreenCapture.OperateAndroid;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by zhongxia on 10/24/16.
 */
public class ImageThread extends Thread {
  private JLabel _label = null;
  private IDevice _device = null;
  private int _width = 270;
  private int _height = 480;

  /**
   * 构造函数
   *
   * @param label  标签组件
   * @param device 设备对象
   */
  public ImageThread(JLabel label, IDevice device) {
    _label = label;
    _device = device;
  }

  /**
   * 构造函数
   *
   * @param label  标签组件
   * @param device 设备对象
   * @param width  宽
   * @param height 高
   */
  public ImageThread(JLabel label, IDevice device, int width, int height) {
    _label = label;
    _device = device;
    _width = width;
    _height = height;
  }

  @Override
  public void run() {
    super.run();
    ImageIcon image = null;
    try {
      while (true) {
        image = new ImageIcon("no.gif");
        image.setImage(getImageIcon(_device).getImage().getScaledInstance(_width, _height, Image.SCALE_SMOOTH));
        _label.setIcon(image);
      }
    } catch (Exception e) {
      System.out.println("获取屏幕截图异常!设备名称:" + _device.getName());
    }
  }

  /**
   * 获取屏幕截图
   *
   * @param device 设备
   * @return
   */
  public static ImageIcon getImageIcon(IDevice device) {
    try {
      long start = System.currentTimeMillis();

      RawImage rawImage = device.getScreenshot();

      long end = System.currentTimeMillis();
      System.out.println("获取设备屏幕的时间" + (end - start) + "毫秒");

//      BufferedImage image = new BufferedImage(rawImage.width, rawImage.height, BufferedImage.TYPE_INT_RGB);
      BufferedImage image = new BufferedImage(rawImage.width, rawImage.height, BufferedImage.SCALE_SMOOTH);

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
