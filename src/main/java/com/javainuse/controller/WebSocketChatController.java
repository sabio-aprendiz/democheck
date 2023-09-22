package com.javainuse.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.stereotype.Controller;


@Controller
public class WebSocketChatController {

	@MessageMapping("/chat.sendMessage")
	public String sendMessage(@Payload String webSocketChatMessage) {
		return webSocketChatMessage;
	}


}
