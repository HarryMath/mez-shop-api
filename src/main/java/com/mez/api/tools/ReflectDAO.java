package com.mez.api.tools;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class ReflectDAO {

  public <T> void save(T object, String tableName) throws SQLException {
    Field[] fields = object.getClass().getDeclaredFields();
    System.out.println(object.getClass().getName());
    for (Field field: fields) {
      try {
        String name = field.getName();
        field.setAccessible(true);
        System.out.println(name + ": " + field.get(object) );
        field.setAccessible(false);
      } catch (IllegalAccessException e) {
        System.out.println(e.getMessage());
      }
    }
    System.out.println();
  }
}
