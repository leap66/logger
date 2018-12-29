package com.grade.logger.log;

/**
 * LogLevel : 日志类型级别枚举
 * <p>
 * </> Created by ylwei on 2018/2/26.
 */
public enum LogLevel {
  ERROR(10), BURY(8);

  public int point;

  private LogLevel(int point) {
    this.point = point;
  }
}
