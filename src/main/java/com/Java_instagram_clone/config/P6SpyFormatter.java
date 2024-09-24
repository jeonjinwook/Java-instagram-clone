package com.Java_instagram_clone.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import java.util.Locale;
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
    return String.format("[%s] | %d ms | %s", category, elapsed, formattedSql);
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
}