package com.grade.logger.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean2MapUtil : 实体类转化为Map
 * <p>
 * </> Created by ylwei on 2018/2/26.
 */
public class Bean2MapUtil {

  public static Map<String, Object> b2M(Object object) {
    Map<String, Object> map = new HashMap<>();

    Class cls = object.getClass();
    Field[] fields = cls.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        map.put(field.getName(), field.get(object));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return map;
  }

}
