package sample;

import sample.ClientEndpoint;
import sample.game.GameStateListener;
import sample.game.GameVis;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import javax.xml.bind.DatatypeConverter;

public class Main {

    public static void initGame() throws InterruptedException, IOException, DeploymentException {
        WebSocketContainer webSocket = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
            @Override
            public void beforeRequest(Map<String, List<String>> headers) {
                headers.put("Authorization", Arrays.asList("Basic " + DatatypeConverter.printBase64Binary("preall:UU2aevJOWAHv".getBytes())));
            }
        };
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();

        // The clientendpoint adds the listener, sync is not important
        new Thread(GameVis::startVisualization).start();

        webSocket.connectToServer(ClientEndpoint.class, config, URI.create("ws://javachallenge.loxon.hu:8080/JavaChallenge2017/websocket"));
//        System.in.read();
    }

    public static void main(String[] args) throws IOException, DeploymentException, InterruptedException {
        //initGame();
        // The clientendpoint adds the listener, sync is not important
        new Thread(GameVis::startVisualization).start();
        Scanner sc = new Scanner(System.in);
        GameStateListener gameStateListener = GameStateListener.INSTANCE;
        String lastMessage = "";

        while (sc.hasNext()) {
            String message = sc.nextLine();
            gameStateListener.onMessage(message);
            lastMessage = message;
        }
        System.out.printf(lastMessage);
    }
}
