package com.grade.logger.log;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.grade.logger.mgr.NetContext;
import com.grade.logger.util.Bean2MapUtil;
import com.grade.logger.util.DeviceInfoUtil;
import com.grade.logger.util.LogUtil;

import org.json.JSONException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Logger : 日志记录
 * <p>
 * </> Created by ylwei on 2018/2/27.
 */
public class Logger {
  private static HashMap<BaseDestination, BlockingQueue<Map>> destinations = new HashMap<>();
  private static Map<String, Object> parameter;

  public static void init() {
    parameter = initField();
  }

  public static void checkPermission(Activity context) {
    if (ContextCompat.checkSelfPermission(context,
        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      try {
        ActivityCompat.requestPermissions(context, new String[] {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
      } catch (ClassCastException e) {
        e.printStackTrace();
      }
    }
  }

  public static void addDestination(BaseDestination destination) {
    BlockingQueue<Map> queue = new LinkedBlockingDeque<>();
    destinations.put(destination, queue);
    Logger.Worker worker = new Logger.Worker(destination, queue);
    worker.start();
  }

  public static void error(String message) {
    Logger.sendError(Thread.currentThread(), message, null);
  }

  public static void error(Thread thread, Throwable throwable) {
    Logger.sendError(thread, null, throwable);
  }

  public static void bury(Map<String, Object> map) {
    Logger.sendBury(map);
  }

  private static void sendError(Thread thread, String message, Throwable throwable) {
    Map<String, Object> map = new HashMap<>();
    map.put(LogField.LEVEL, LogLevel.ERROR);

    StringBuilder sb = new StringBuilder();
    if (message != null) {
      sb.append(message);
    }
    if (throwable != null) {
      sb.append(": ");
      Writer result = new StringWriter();
      PrintWriter printWriter = new PrintWriter(result);
      throwable.printStackTrace(printWriter);
      sb.append(result.toString());
    }
    map.put(LogField.MESSAGE, sb.toString());
    if (thread != null) {
      map.put(LogField.THREAD, thread.getName());
      int i = 0;
      for (StackTraceElement element : thread.getStackTrace()) {
        if (!element.isNativeMethod()) {
          i++;
          if (i == 4) {
            map.put(LogField.FILE_NAME, element.getClassName());
            map.put(LogField.LINE, element.getLineNumber());
            map.put(LogField.FUNCTION, element.getMethodName());
            break;
          }
        }
      }
    }
    send(LogLevel.ERROR, map);
  }

  private static void sendBury(Map<String, Object> map) {
    Map<String, Object> model = new HashMap<>();
    model.put(LogField.LEVEL, LogLevel.BURY);
    model.put(LogField.THREAD, Thread.currentThread().getName());
    int i = 0;
    for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
      if (!element.isNativeMethod()) {
        i++;
        if (i == 4) {
          model.put(LogField.FILE_NAME, element.getClassName());
          model.put(LogField.LINE, element.getLineNumber());
          model.put(LogField.FUNCTION, element.getMethodName());
          break;
        }
      }
    }
    map.putAll(model);
    send(LogLevel.BURY, map);
  }

  private static void send(LogLevel logLevel, Map<String, Object> data) {
    for (BaseDestination destination : destinations.keySet()) {
      if (destination.getLevel().equals(logLevel)) {
        data.put(LogField.LEVEL, logLevel);
        LogUtil.e("Logger.sending:", data.toString());
        destination.send(setField(data));
      }
    }
  }

  public static void cancel() {
    for (BaseDestination destination : destinations.keySet()) {
      destination.cancel();
    }
  }

  private static class Worker extends Thread {
    BlockingQueue<Map> queue;
    private BaseDestination destination;

    Worker(BaseDestination destination, BlockingQueue<Map> queue) {
      this.destination = destination;
      this.queue = queue;
    }

    public void run() {
      for (;;) {
        try {
          Map data = queue.take();
          destination.send(setField(data));
        } catch (InterruptedException e) {
          LogUtil.e("进程异常", e.getMessage());
          return;
        }
      }
    }
  }

  private static Map<String, Object> initField() {
    Context context = NetContext.getInstance();
    if (context == null)
      return new HashMap<>();
    LogModel logModel = new LogModel();
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo;
    try {
      packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      packageInfo = new PackageInfo();
    }
    logModel.setAppBuild(packageInfo.versionCode);
    logModel.setAppId(context.getPackageName());

    logModel.setIMEI(DeviceInfoUtil.getImei());
    logModel.setIMSI(DeviceInfoUtil.getImsi());
    logModel.setPHONE_WIFI_MAC(DeviceInfoUtil.getWifiMac());
    logModel.setROUTER_MAC(DeviceInfoUtil.getRouterMac());
    try {
      logModel.setCell_id(DeviceInfoUtil.getCellInfo());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    logModel.setDevice_id(DeviceInfoUtil.getUDID());
    logModel.setSrceen_resolution_desc(DeviceInfoUtil.getScreenResolution());
    logModel.setHorizontal_flag(DeviceInfoUtil.getScreenType());
    logModel.setOs_version(DeviceInfoUtil.getOsVersion());
    logModel.setMobile_operators_desc(DeviceInfoUtil.getOperators());
    logModel.setNetwork_desc(DeviceInfoUtil.getNetworkType());
    logModel.setSdk_version(DeviceInfoUtil.getSdkVersion());
    logModel.setDevice_desc(DeviceInfoUtil.getDeviceInfo());
    logModel.setApp_version(packageInfo.versionName);
    logModel.setLanguage(DeviceInfoUtil.getSystemLanguage());
    logModel.setIP(DeviceInfoUtil.getDeviceIP());
    logModel.setIsRoot(DeviceInfoUtil.isRoot() + "");
    logModel.setGmtTime(DeviceInfoUtil.getGMT());
    logModel.setMac(DeviceInfoUtil.getMacAddr());
    logModel.setSize(DeviceInfoUtil.getPhoneSize());
    logModel.setEvent_time(new Date());
    return Bean2MapUtil.b2M(logModel);
  }

  private static Map<String, Object> setField(Map<String, Object> data) {
    Map<String, Object> map = new HashMap<>();
    map.putAll(parameter);
    for (String key : data.keySet()) {
      if (data.get(key) != null)
        map.put(key, data.get(key));
    }
    map.put(LogField.EVENT_TIME, new Date());
    map.put(LogField.GMT, DeviceInfoUtil.getGMT());
    return map;
  }
}
