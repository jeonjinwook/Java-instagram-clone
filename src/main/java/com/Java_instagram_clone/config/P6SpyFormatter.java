package com.Java_instagram_clone.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6SpyFormatter implements MessageFormattingStrategy {

  @PostConstruct
  public void setLogMessageFormat() {
    P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
  }

  @Override
  public String formatMessage(int connectionId, String now, long elapsed, String category,
      String prepared, String sql, String url) {
    String formattedSql = formatSql(category, sql);
    Date currentDate = new Date();
    SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

    return format1.format(currentDate) + " | " + "OperationTime : " + elapsed + "ms" + formattedSql
        + createStack();
  }

  private String formatSql(String category, String sql) {
    if (sql == null || sql.trim().isEmpty()) {
      return sql;
    }

    if (!Category.STATEMENT.getName().equals(category)) {
      return sql;
    }

    String trimmedSql = sql.trim();
    String lowerCaseSql = trimmedSql.toLowerCase(Locale.ROOT);

    FormatStyle style = (lowerCaseSql.startsWith("create") || lowerCaseSql.startsWith("alter")
        || lowerCaseSql.startsWith("comment")) ? FormatStyle.DDL : FormatStyle.BASIC;

    return style.getFormatter().format(sql);
  }

  private String createStack() {
    Stack<String> callStack = new Stack<>();
    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    for (StackTraceElement stackTraceElement : stackTrace) {
      String trace = stackTraceElement.toString();
      if (trace.startsWith("com.Java_instagram_clone")) {
        callStack.push(trace);
      }
    }
    StringBuffer sb = new StringBuffer();
    int order = 1;
    while (!callStack.isEmpty()) {
      sb.append("\n\t\t").append(order++).append(".").append(callStack.pop());
    }
    return new StringBuffer().append("\n\tCall Stack :").append(sb).append("\n")
        .append("\n--------------------------------------").toString();
  }
}