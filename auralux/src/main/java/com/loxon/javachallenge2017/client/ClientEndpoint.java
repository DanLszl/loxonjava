package com.loxon.javachallenge2017.client;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class ClientEndpoint extends Endpoint implements MessageHandler.Whole<String> {
	private Session session;
	private boolean isFirst = true;
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(this);
		this.session = session;
	}

	@Override
	public void onMessage(String message) {
		System.out.println(message);
		if(isFirst) {
			isFirst = !isFirst;
			sendMessage("{\"moveFrom\":102,\"moveTo\":106,\"armySize\":12}");
			sendMessage("{\"moveFrom\":102,\"moveTo\":104,\"armySize\":11}");
			sendMessage("{\"moveFrom\":102,\"moveTo\":108,\"armySize\":14}");
			sendMessage("{\"moveFrom\":102,\"moveTo\":110,\"armySize\":13}");
		}
	}

	private void sendMessage(String message) {
		session.getAsyncRemote().sendText(message);
	}
}
