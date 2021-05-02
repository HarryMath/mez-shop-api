package com.mez.api.tools;

import java.time.LocalDate;

public abstract class DateFormatter {

  private static final String[] MONTHS_SHORT = {"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};

  public static String nowDateString() {
    LocalDate date = LocalDate.now();
    int day = date.getDayOfMonth();
    String month = MONTHS_SHORT[date.getMonthValue() - 1];
    int year = date.getYear();
    return day + " " + month + " " + year;
  }

}
