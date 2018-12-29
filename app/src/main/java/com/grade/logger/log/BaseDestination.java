package com.grade.logger.log;

import java.util.Map;

/**
 * BaseDestination : 日志缓存接口类
 * <p>
 * </> Created by ylwei on 2018/2/28.
 */
public interface BaseDestination {

  void send(Map map);

  void sendNow();

  LogLevel getLevel();

  void setInterval(int interval);

  void setFileName(String fileName);

  void setToken(String token);

  void setServerUrl(String url);

  void cancel();
}
