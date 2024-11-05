package com.app.stomptest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketController {

    /**
     * Test
     * @MessageMapping : 클라이언트가 /app/message로 메시지를 전송하면 호출됨
     * @SendTo : 메시지가 /topic/messages를 구독 중인 모든 클라이언트에게 전송
     */
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        log.info("클라이언트로부터 메시지를 수신했습니다: " + message);
        return "Received: " + message;
    }
}
