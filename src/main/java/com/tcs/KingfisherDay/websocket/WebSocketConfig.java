package com.tcs.KingfisherDay.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/quizWS").setHandshakeHandler(new CustomHandshakeHandler()).withSockJS();
		registry.addEndpoint("/eventWS").setHandshakeHandler(new CustomHandshakeHandler()).withSockJS();
		registry.addEndpoint("/eventMobileWS").setHandshakeHandler(new CustomHandshakeHandler()).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic");
	}
}
