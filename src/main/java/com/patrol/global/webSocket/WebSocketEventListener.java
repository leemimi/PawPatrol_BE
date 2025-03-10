package com.patrol.global.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
//dkfsd

@Component
public class WebSocketEventListener implements ApplicationListener<AbstractSubProtocolEvent> {
    private static final ConcurrentHashMap<String, Set<String>> roomSubscriptions = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUserId(accessor);

        if (userId == null) {
            logger.warn("User ID not found in WebSocket session");
            return;
        }

        if (event instanceof SessionSubscribeEvent) {
            handleSubscription(accessor, userId);
        } else if (event instanceof SessionUnsubscribeEvent) {
            handleUnsubscription(accessor, userId);
        } else if (event instanceof SessionDisconnectEvent) {
            handleDisconnect(userId);
        }
    }

    private String getUserId(StompHeaderAccessor accessor) {
        // 1. 세션 속성에서 사용자 ID 추출 (핸드셰이크 시점에 저장한 값)
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null && sessionAttributes.containsKey("userId")) {
            return sessionAttributes.get("userId").toString();
        }

        // 2. Principal에서 시도
        Principal principal = accessor.getUser();
        if (principal != null) {
            return principal.getName();
        }

        return null;
    }

    private void handleSubscription(StompHeaderAccessor accessor, String userId) {
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/queue/chat/")) {
            String roomId = destination.substring("/queue/chat/".length());

            roomSubscriptions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                    .add(userId);

            logger.info("User {} subscribed to room {}", userId, roomId);
        }
    }

    private void handleUnsubscription(StompHeaderAccessor accessor, String userId) {
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/queue/chat/")) {
            String roomId = destination.substring("/queue/chat/".length());
            Set<String> subscribers = roomSubscriptions.get(roomId);

            if (subscribers != null) {
                subscribers.remove(userId);
                logger.info("User {} unsubscribed from room {}", userId, roomId);
            }
        }
    }

    private void handleDisconnect(String userId) {
        // 모든 구독에서 사용자 제거
        for (Map.Entry<String, Set<String>> entry : roomSubscriptions.entrySet()) {
            if (entry.getValue().remove(userId)) {
                logger.info("User {} removed from room {} due to disconnect", userId, entry.getKey());
            }
        }
    }

    /**
     * 특정 사용자가 특정 채팅방에 구독 중인지 확인
     */
    public static boolean isSubscribedToRoom(String userId, String roomId) {
        Set<String> subscribers = roomSubscriptions.get(roomId);
        return subscribers != null && subscribers.contains(userId);
    }

    /**
     * 사용자가 구독 중인 모든 채팅방 ID 목록 반환
     */
    public static Set<String> getUserSubscribedRooms(String userId) {
        Set<String> rooms = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : roomSubscriptions.entrySet()) {
            if (entry.getValue().contains(userId)) {
                rooms.add(entry.getKey());
            }
        }

        return rooms;
    }
}