# weiqunkong
微群控， 电脑统一控制多台Android设备


## 实现进度
1. 把用USB连接到电脑上的设备，都展示在应用程序上
2. 添加打开微信功能，打开QQ， 打开微博，打开摄像头功能
3. 添加单独在PC端控制Android设备的功能，操作之后可以群控所有手机
4. 添加设备列表
5. 选择某一个设备，群控所有连接到电脑的设备
6. 【TODO】录制脚本功能
7. 【TODO】BUG冲突解决(broken pipe , monkey --port 12345 这两个主要BUG)

## 注意
>如果设备使用USB连接到电脑后，adb devices 没有显示出设备， 则需要在手机上打开 USB调试功能。


### 比较耗时的操作
```java
//创建这个对象,比较耗时,大概需要2s,因此默认只刚开始创建改对象,然后保存起来,不一直创建
IChimpDevice chimpDevice = new AdbChimpDevice(dev);
```

```java
//这个截屏的操作也比较耗时,大概需要 1~3s, 不固定
RawImage rawImage = device.getScreenshot();
```


## 使用说明
一、下载代码
```shell
git clone https://github.com/zhongxia245/weiqunkong.git
```

二、使用IDEA或者 Eclipse 打开【本人使用 IDEA】

三、src下的 App.java  和  MainWindow.java 是包含main函数

## 说明
MainWindow 可以在 手机屏幕截图上，直接进行操作
App 上可以投射多个设备的屏幕截图，但是目前操作功能，只实现了 HOME，BACK，MENU 三个按键


手机屏幕投射原理:
根据adb获取手机屏幕的截图，然后替换PC应用上显示的图片，由于获取屏幕截图需要1~3s，因此存在延迟。 需要寻找更好的解决方案。
可以去调研下  toal control 是如何解决的。

## 想要最终实现的效果图
>最终想要实现的效果图大概是这样的。

图片来源于 通路云
![](http://ww2.sinaimg.cn/large/801b780agw1f93s7ar9iaj20tp0fegp1.jpg)

## 目前效果图
![](http://ww3.sinaimg.cn/large/801b780agw1f93rxty21sg20mn0gt49u.gif)



