package com.grade.logger.usecase;

import com.grade.logger.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Test :
 * <p>
 * </> Created by ylwei on 2018/12/29.
 */
public class Test {

  private static Map<String, Object> getMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", Thread.currentThread().getStackTrace()[3].getMethodName());
    return map;
  }

  private static void logger(Map<String, Object> map) {
    Logger.bury(map);
  }

  // 进入注册第一页
  public static void QFAPP_SYS_REG_ENTRY_SW(String channel) {
    Map<String, Object> map = getMap();
    map.put("channel", channel);
    logger(map);
  }
}
