package com.loxon.javachallenge2017.client;

import com.google.gson.Gson;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.strategy.HardCodedGraph;
import com.loxon.javachallenge2017.strategy.Strategy;
import com.loxon.javachallenge2017.strategy.StrategyFactory;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.List;

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
		Gson gson = new Gson();

		if (firstMessage) {
			gameDescription = gson.fromJson(message, GameDescription.class);
			for (Planet planet : gameDescription.getPlanets()) {
				planet.setNeighbours(HardCodedGraph.getNeighbours().get(planet.getPlanetID()));
			}
			String graphIncludedMessage = gson.toJson(gameDescription);
			strategy = StrategyFactory.getStrategy(gameDescription);
			firstMessage = !firstMessage;
			System.out.println(graphIncludedMessage);
		} else {
			GameState gameState = gson.fromJson(message, GameState.class);
			List<Response> responses = strategy.getResponse(gameState);
			for (Response response : responses) {
				String answer = gson.toJson(response);
				System.err.println(answer);
				sendMessage(answer);
			}
		}
	}

	private void sendMessage(String message) {
		session.getAsyncRemote().sendText(message);
	}
}
