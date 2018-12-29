package com.grade.logger.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grade.logger.mgr.NetContext;
import com.grade.logger.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * LogStashDescription : 日志缓存
 * <p>
 * </> Created by ylwei on 2018/2/28.
 */
public class LogStashDescription implements BaseDestination {
  private String serverUrl;
  private String token;

  private int interval = 1000 * 60 * 30;
  private LogLevel logLevel;
  private File entriesFile;
  private File sendingFile;
  private Gson gson;
  private boolean isSending = false;
  private OkHttpClient client;
  private boolean cancel = false;
  private Timer timer;

  public LogStashDescription(LogLevel logLevel) {
    this.logLevel = logLevel;
    entriesFile = new File(NetContext.getInstance().getFilesDir().getAbsolutePath(), "logger.json");
    sendingFile = new File(NetContext.getInstance().getFilesDir().getAbsolutePath(),
        "logger.sending.json");
    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    initUpload();
  }

  @Override
  public LogLevel getLevel() {
    return logLevel;
  }

  @Override
  public void setInterval(int interval) {
    this.interval = interval;
  }

  @Override
  public void setFileName(String fileName) {
    entriesFile = new File(NetContext.getInstance().getFilesDir().getAbsolutePath(),
        fileName + ".json");
    sendingFile = new File(NetContext.getInstance().getFilesDir().getAbsolutePath(),
        fileName + ".sending.json");
  }

  @Override
  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  private void initUpload() {
    timer = new Timer(true);
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (!cancel)
          sendNow();
      }
    }, 1000, interval);
  }

  @Override
  public void cancel() {
    cancel = true;
    timer.cancel();
  }

  @Override
  public void send(Map data) {
    try {
      FileWriter writer = new FileWriter(entriesFile, true);
      try {
        writer.write(gson.toJson(data) + "\n");
        writer.flush();
      } finally {
        writer.close();
      }
      sendNow();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized void sendNow() {
    if (!sendingFile.exists()) {
      try {
        boolean created = sendingFile.createNewFile();
        FileInputStream fis = new FileInputStream(entriesFile);
        FileOutputStream fos = new FileOutputStream(sendingFile);
        try {
          int n;
          byte[] buffer = new byte[1024 * 4];
          while (-1 != (n = fis.read(buffer))) {
            fos.write(buffer, 0, n);
          }
        } finally {
          fis.close();
          fos.close();
        }
        boolean deleted = entriesFile.delete();
        LogUtil.i("delete entriesFile", "" + deleted);
        if (deleted) {
          boolean createNew = entriesFile.createNewFile();
        }
      } catch (IOException e) {
        return;
      }
    }

    if (!isSending) {
      isSending = true;
      List<Map> logModels = readFromFile(sendingFile);
      if (logModels.size() <= 0) {
        boolean deleted = sendingFile.delete();
        LogUtil.i("delete sendingFile", "empty " + deleted);
        isSending = false;
      } else {
        try {
          Collections.reverse(logModels);
          OkHttpClient client = getClient();
          RequestBody body = RequestBody.create(MediaType.parse("application/json"),
              gson.toJson(logModels).getBytes(Util.UTF_8));
          Request request = new Request.Builder().url(serverUrl).post(body).build();
          client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              isSending = false;
              if (e != null)
                LogUtil.e("上传日志到服务器的Request请求失败", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              if (response.isSuccessful()) {
                boolean deleted = sendingFile.delete();
                LogUtil.i("delete sendingFile", "" + deleted);
                isSending = false;
              } else {
                isSending = false;
                LogUtil.e("上传日志到服务器的Request请求失败", response.message());
              }
            }
          });

        } catch (Exception e) {
          e.printStackTrace();
          isSending = false;
          LogUtil.e("上传日志错误", e.getMessage());
        }
      }
    }
  }

  private OkHttpClient getClient() {
    if (client == null) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      client = new OkHttpClient.Builder().retryOnConnectionFailure(false)
          .authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
              String credential = "Basic " + token;
              if (credential.equals(response.request().header("Authorization"))) {
                return null;
              }
              return response.request().newBuilder().addHeader("Authorization", credential)
                  .header("Content-Type", "application/json").build();
            }
          }).addInterceptor(logging).build();
    }
    return client;
  }

  private List<Map> readFromFile(File file) {
    List<Map> result = new LinkedList<>();
    try {
      FileReader reader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(reader);

      try {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          result.add(gson.fromJson(line, Map.class));
        }
      } finally {
        bufferedReader.close();
        reader.close();
      }
    } catch (Exception e) {
      System.out.println("---read------" + e.getMessage());
      e.printStackTrace();
    }
    return result;
  }

}
