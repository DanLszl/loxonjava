package sample;

import com.google.gson.Gson;
import sample.game.GameChangeListener;
import sample.game.GameStateListener;
import sample.pack.magic.MagicData;
import sample.pack.magic.MagicValue;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

//        // Text
//        new Thread(() -> {
//            try {
//                Thread.sleep(3400);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            for (int i = 0; i < 500; i++) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                MagicData magicData = new MagicData();
//
//                List<MagicValue> values = new ArrayList<>();
//
//                Random random =  new Random(System.nanoTime());
//
//                for(int j = 101; j <= 110; ++j) {
//                    MagicValue magicValue = new MagicValue();
//                    magicValue.setPlanetIndex(j);
//                    magicValue.setMagicness(random.nextDouble());
//                    values.add(magicValue);
//                }
//                magicData.setMagicValues(values);
//
//                onMessage(new Gson().toJson(magicData));
//            }
//
//        }).start();
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);

        for (GameChangeListener gameChangeListener : listeners) {
            gameChangeListener.onMessage(message);
        }

    }

    private void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
