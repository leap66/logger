package com.grade.logger.log;

import com.google.gson.JsonElement;

import java.util.Date;

/**
 * LogModel : 日志记录Model
 * <p>
 * </> Created by ylwei on 2018/2/26.
 */
public class LogModel {
  private LogLevel level;
  private String message;
  private String thread;
  private String fileName;
  private String function;
  private int line;

  private String appId;
  private int appBuild;

  private String IMEI;
  private String IMSI;
  private String PHONE_WIFI_MAC;
  private String ROUTER_MAC;
  private JsonElement cell_id;
  private String device_id;
  private String srceen_resolution_desc;
  private String horizontal_flag;
  private String os_version;
  private String mobile_operators_desc;
  private String network_desc;
  private String sdk_version;
  private String device_desc;
  private String app_version;
  private String language;
  private String IP;
  private String isRoot;
  private Date gmtTime;
  private String mac;
  private String size;
  private Date event_time;

  public LogLevel getLevel() {
    return level;
  }

  public void setLevel(LogLevel level) {
    this.level = level;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getThread() {
    return thread;
  }

  public void setThread(String thread) {
    this.thread = thread;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public String getOs_version() {
    return os_version;
  }

  public void setOs_version(String os_version) {
    this.os_version = os_version;
  }

  public String getApp_version() {
    return app_version;
  }

  public void setApp_version(String app_version) {
    this.app_version = app_version;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public int getAppBuild() {
    return appBuild;
  }

  public void setAppBuild(int appBuild) {
    this.appBuild = appBuild;
  }

  public String getIMEI() {
    return IMEI;
  }

  public void setIMEI(String IMEI) {
    this.IMEI = IMEI;
  }

  public String getIMSI() {
    return IMSI;
  }

  public void setIMSI(String IMSI) {
    this.IMSI = IMSI;
  }

  public String getPHONE_WIFI_MAC() {
    return PHONE_WIFI_MAC;
  }

  public void setPHONE_WIFI_MAC(String PHONE_WIFI_MAC) {
    this.PHONE_WIFI_MAC = PHONE_WIFI_MAC;
  }

  public String getROUTER_MAC() {
    return ROUTER_MAC;
  }

  public void setROUTER_MAC(String ROUTER_MAC) {
    this.ROUTER_MAC = ROUTER_MAC;
  }

  public JsonElement getCell_id() {
    return cell_id;
  }

  public void setCell_id(JsonElement cell_id) {
    this.cell_id = cell_id;
  }

  public String getDevice_id() {
    return device_id;
  }

  public void setDevice_id(String device_id) {
    this.device_id = device_id;
  }

  public String getSrceen_resolution_desc() {
    return srceen_resolution_desc;
  }

  public void setSrceen_resolution_desc(String srceen_resolution_desc) {
    this.srceen_resolution_desc = srceen_resolution_desc;
  }

  public String getHorizontal_flag() {
    return horizontal_flag;
  }

  public void setHorizontal_flag(String horizontal_flag) {
    this.horizontal_flag = horizontal_flag;
  }

  public String getMobile_operators_desc() {
    return mobile_operators_desc;
  }

  public void setMobile_operators_desc(String mobile_operators_desc) {
    this.mobile_operators_desc = mobile_operators_desc;
  }

  public String getNetwork_desc() {
    return network_desc;
  }

  public void setNetwork_desc(String network_desc) {
    this.network_desc = network_desc;
  }

  public String getSdk_version() {
    return sdk_version;
  }

  public void setSdk_version(String sdk_version) {
    this.sdk_version = sdk_version;
  }

  public String getDevice_desc() {
    return device_desc;
  }

  public void setDevice_desc(String device_desc) {
    this.device_desc = device_desc;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getIP() {
    return IP;
  }

  public void setIP(String IP) {
    this.IP = IP;
  }

  public String getIsRoot() {
    return isRoot;
  }

  public void setIsRoot(String isRoot) {
    this.isRoot = isRoot;
  }

  public Date getGmtTime() {
    return gmtTime;
  }

  public void setGmtTime(Date gmtTime) {
    this.gmtTime = gmtTime;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Date getEvent_time() {
    return event_time;
  }

  public void setEvent_time(Date event_time) {
    this.event_time = event_time;
  }
}
