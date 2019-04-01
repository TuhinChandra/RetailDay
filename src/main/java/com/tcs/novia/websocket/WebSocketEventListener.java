package com.tcs.novia.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		LOGGER.info("handleWebSocketConnectListener::connected +1");
		messagingTemplate.convertAndSend("/topic/public", System.currentTimeMillis());
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		LOGGER.info("handleWebSocketDisconnectListener::disconnected -1");
		messagingTemplate.convertAndSend("/topic/public", System.currentTimeMillis());
	}
}
