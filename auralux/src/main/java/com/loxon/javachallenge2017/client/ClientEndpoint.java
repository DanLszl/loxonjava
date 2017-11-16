package com.loxon.javachallenge2017.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.Standing;
import com.loxon.javachallenge2017.strategy.HardCodedGraph;
import com.loxon.javachallenge2017.strategy.Strategy;
import com.loxon.javachallenge2017.strategy.StrategyFactory;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        try {
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
                Response response = strategy.getResponse(gameState);
                if (response != null) {
                    String answer = gson.toJson(response);
                    System.err.println(answer);
                    sendMessage(answer);
                }

                System.out.println(message);
                if (gameState.getGameStatus().equals("ENDED")) {
                    for (Standing standing : gameState.getStandings()) {
                        System.err.println(standing.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
