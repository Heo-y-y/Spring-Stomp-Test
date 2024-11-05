package com.app.stomptest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 클라이언트가 접속할 엔드포인트 정의
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }

    /**
     * 메시지 브로커 설정
     * enableSimpleBroker : 구독할 때 사용
     * setApplicationDestinationPrefixes : 서버가 클라이언트에게 메시지를 전송할 때 사용
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * WebSocket 연결, 구독, 연결 해제 시 로그 기록을 위한 인터셉터 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public org.springframework.messaging.Message<?> preSend(org.springframework.messaging.Message<?> message, org.springframework.messaging.MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null) {
                    switch (accessor.getCommand()) {
                        case CONNECT:
                            log.info("새로운 WebSocket 연결이 시작");
                            break;
                        case SUBSCRIBE:
                            log.info("클라이언트가 주제에 구독을 요청 : " + accessor.getDestination());
                            break;
                        case DISCONNECT:
                            log.info("WebSocket 연결이 종료");
                            break;
                        default:
                            break;
                    }
                }
                return message;
            }
        });
    }
}

