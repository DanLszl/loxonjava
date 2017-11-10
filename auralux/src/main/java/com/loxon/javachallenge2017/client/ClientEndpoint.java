package com.loxon.javachallenge2017.client;

import com.google.gson.Gson;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.strategy.Strategy;
import com.loxon.javachallenge2017.strategy.StrategyFactory;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class ClientEndpoint extends Endpoint implements MessageHandler.Whole<String> {
	private Session session;

	private boolean firstMessage = true;
	private GameDescription gameDescription;
	private Strategy strategy;

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(this);
		this.session = session;
	}

	@Override
	public void onMessage(String message) {
		System.out.println(message);
		Gson gson = new Gson();

		if (firstMessage) {
			gameDescription = gson.fromJson(message, GameDescription.class);
			strategy = StrategyFactory.getStrategy(gameDescription);
			firstMessage = !firstMessage;
		} else {
			GameState gameState = gson.fromJson(message, GameState.class);
			Response response = strategy.getResponse(gameState);
			String answer = gson.toJson(response);
			System.err.println(answer);
			sendMessage(answer);
		}
	}

	private void sendMessage(String message) {
		session.getAsyncRemote().sendText(message);
	}
}
