package com.Java_instagram_clone.component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

  private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String userId = getUserIdFromSession(session);
    userSessions.put(userId, session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    String userId = getUserIdFromSession(session);
    userSessions.remove(userId);
  }

  public void sendMessageToUser(String userId, String message) throws IOException {
    WebSocketSession session = userSessions.get(userId);
    if (session != null && session.isOpen()) {
      session.sendMessage(new TextMessage(message));
    }
  }

  private String getUserIdFromSession(WebSocketSession session) {
    String query = session.getUri().getQuery();
    return query.split("=")[1];
  }
}
