package com.grade.logger.mgr;

import android.content.Context;

/**
 * NetContext : 全局Context管理器 Application
 * <p>
 * </> Created by ylwei on 2018/2/24.
 */
public class NetContext {
  private static Object instance;

  public static Context getInstance() {
    if (null == instance)
      throw new NullPointerException("you should init NetContext first");
    return (Context) instance;
  }

  static void init(Context context) {
    if (null == context)
      throw new NullPointerException("context is NULL");
    instance = context.getApplicationContext();
  }
}
