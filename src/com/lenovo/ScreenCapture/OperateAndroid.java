package com.lenovo.ScreenCapture;

import com.android.chimpchat.ChimpManager;
import com.android.chimpchat.adb.AdbChimpDevice;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.android.ddmlib.IDevice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * 操作Android手机的方法封装
 * 1、提供 HOME,BACK,MENU,POWER 四个按键的功能
 * 2、提供 点击事件,滑动事件
 */
public class OperateAndroid {
  private AdbChimpDevice device; //adb移动设备
  private ChimpManager manager;  //移动设备管理对象
  private static OperateAndroid oa; //操作移动设备的类

  public static String HOME = "KEYCODE_HOME";
  public static String BACK = "KEYCODE_BACK";
  public static String MENU = "KEYCODE_MENU";
  public static String POWER = "KEYCODE_POWER";

  /**
   * 初始化操作类
   *
   * @param dev 通过adb返回的device对象
   */
  public OperateAndroid(IDevice dev) {
    long start = System.currentTimeMillis();
    if (device == null) {
      IChimpDevice chimpDevice = new AdbChimpDevice(dev);
      device = (AdbChimpDevice) chimpDevice;
      manager = device.getManager();
    }
    long end = System.currentTimeMillis();
  }

  /**
   * 获取OA对象
   * 如果已经存在,则支持返回,不存在则生成
   *
   * @param dev
   * @return
   */
  public static OperateAndroid getOperateAndroid(IDevice dev) {
    if (oa == null) {
      oa = new OperateAndroid(dev);
    }
    return oa;
  }

  /**
   * Android touch 事件
   *
   * @param x 坐标
   * @param y 坐标
   */
  public void touch(int x, int y) {
    device.touch(x, y, com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
  }

  /**
   * Android touch 事件
   *
   * @param x    坐标
   * @param y    坐标
   * @param type touch 类型
   */
  public void touch(int x, int y, TouchPressType type) {
    device.touch(x, y, type);
  }

  /**
   * press 触摸事件
   *
   * @param str
   */
  public void press(String str) {
    device.press(str, com.android.chimpchat.core.TouchPressType.DOWN_AND_UP);
  }

  /**
   * press 触摸按下事件
   *
   * @param str
   */
  public void press_DOWN(String str) {
    device.press(str, com.android.chimpchat.core.TouchPressType.DOWN);
  }

  /**
   * press 触摸放开事件
   *
   * @param str
   */
  public void press_UP(String str) {
    device.press(str, com.android.chimpchat.core.TouchPressType.UP);
  }

  /**
   * 移动 事件
   *
   * @param startX 开始位置X
   * @param startY 开始位置Y
   * @param endX   结束位置X
   * @param endY   结束位置Y
   * @param time   时间
   * @param step   步骤
   */
  public void drag(int startX, int startY, int endX, int endY, int time, int step) {
    device.drag(startX, startY, endX, endY, time, step);
  }

  /**
   * 输入字符
   *
   * @param c 字符
   */
  public void type(char c) {
    device.type(Character.toString(c));
  }

  /**
   * touch按下
   *
   * @param x 坐标
   * @param y 坐标
   * @throws Exception
   */
  public void touchDown(int x, int y) throws Exception {
    manager.touchDown(x, y);
  }

  /**
   * touch 放开
   *
   * @param x 坐标
   * @param y 坐标
   * @throws Exception
   */
  public void touchUp(int x, int y) throws Exception {
    manager.touchUp(x, y);
  }

  /**
   * touch 移动
   *
   * @param x 坐标
   * @param y 坐标
   * @throws Exception
   */
  public void touchMove(int x, int y) throws Exception {
    manager.touchMove(x, y);
  }

  /**
   * 获取屏幕宽度
   *
   * @return
   */
  public int getScreenWidth() {
    return Integer.parseInt(device.getProperty("display.width"));
  }

  /**
   * 获取屏幕高度
   *
   * @return
   */
  public int getScreenHeight() {
    return Integer.parseInt(device.getProperty("display.height"));
  }

  /**
   * 安装软件
   *
   * @param path
   */
  public void installPackage(String path) {
    device.installPackage(path);
  }

  /**
   * 打开应用
   *
   * @param activityPath apk包名/主界面  eg: cn.com.fetion/.android.ui.activities.StartActivity
   */
  public void startActivity(String activityPath) {
    String action = "android.intent.action.MAIN";
    Collection<String> categories = new ArrayList();
    categories.add("android.intent.category.LAUNCHER");
    try {
      device.startActivity(null, action, null, null, categories, new HashMap<String, Object>(), activityPath, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 打开微信
   */
  public void openWeiXin() {
    startActivity("com.tencent.mm/com.tencent.mm.ui.LauncherUI");
  }

  /**
   * 打开QQ
   */
  public void openQQ() {
    startActivity("com.tencent.mobileqq/com.tencent.mobileqq.activity.SplashActivity");
  }

  /**
   * 打开摄像头
   */
  public void openCamera() {
    startActivity("com.android.camera/com.android.camera.Camera");
  }

  /**
   * 打开微博
   */
  public void openWeiBo() {
    startActivity("com.sina.weibo/com.sina.weibo.SplashActivity");
  }
}
