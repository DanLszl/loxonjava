package sample;

import sample.game.GameChangeListener;
import sample.game.GameStateListener;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

public class ClientEndpoint extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;

    private final List<GameChangeListener> listeners = new ArrayList<>();

    public void addListener(GameChangeListener gameChangeListener) {
        this.listeners.add(gameChangeListener);
    }

    public ClientEndpoint() {
        listeners.add(GameStateListener.INSTANCE);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        session.addMessageHandler(this);
        this.session = session;
    }

    @Override
    public void onMessage(String message) {
//        System.out.println(message);

        for (GameChangeListener gameChangeListener : listeners) {
            gameChangeListener.onMessage(message);
        }

    }

    private void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
