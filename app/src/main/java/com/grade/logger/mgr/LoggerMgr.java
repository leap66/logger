package com.grade.logger.mgr;

import android.app.Activity;
import android.content.Context;

import com.grade.logger.log.CrashHandler;
import com.grade.logger.log.LogLevel;
import com.grade.logger.log.LogStashDescription;
import com.grade.logger.log.Logger;

import java.util.Map;

/**
 * LoggerMgr :
 * <p>
 * </> Created by ylwei on 2018/12/29.
 */
public class LoggerMgr {

  // 初始化日志组件
  public static void init(Context context, String errUrl, String errToken) {
    NetContext.init(context);
    Logger.init();
    // 初始化异常捕获组件
    CrashHandler.getInstance().init();
    // 初始化错误信息分发器
    LogStashDescription errorDescription = new LogStashDescription(LogLevel.ERROR);
    errorDescription.setFileName("error");
    errorDescription.setToken(errToken);
    errorDescription.setServerUrl(errUrl);
    errorDescription.sendNow();
    Logger.addDestination(errorDescription);
  }

  // 初始化日志组件
  public static void init(Context context, String errUrl, String errToken, String buryUrl, String buryToken) {
    init(context, errUrl, errToken);
    // 初始化埋点分发器
    LogStashDescription buryDescription = new LogStashDescription(LogLevel.BURY);
    buryDescription.setFileName("bury");
    buryDescription.setToken(buryToken);
    buryDescription.setServerUrl(buryUrl);
    buryDescription.sendNow();
    Logger.addDestination(buryDescription);
  }

  public static void checkPermission(Activity activity) {
    Logger.checkPermission(activity);
  }

  public static void error(String message) {
    Logger.error(message);
  }

  public static void error(Thread thread, Throwable throwable) {
    Logger.error(thread, throwable);
  }

  public static void bury(Map<String, Object> map) {
    Logger.bury(map);
  }
}
