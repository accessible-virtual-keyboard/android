package no.ntnu.stud.avikeyb.inputdevices;

import android.os.Handler;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;

/**
 * Generic web socket input interface for the android app
 *
 * We only want a single instance of the server running a singleton is used to achieve this
 */
public class WebSocketInput {

    private static WebSocketInput instance;
    private static boolean serverStarted = false;


    public static WebSocketInput getInstance() {
        if (instance == null) {
            instance = new WebSocketInput();
        }
        return instance;
    }


    private SocketServer socketServer;
    private InputInterface inputInterface;
    private Handler inputHandler;


    /**
     * Sets the input insterface to forward the incoming input to
     *
     * @param handler the handler for the thread that should receive the inputs
     * @param input the input interface to forward the incoming inputs to
     */
    public synchronized void setInputInterface(Handler handler, InputInterface input) {
        this.inputHandler = handler;
        this.inputInterface = input;
    }

    /**
     * Starts the local socket server
     */
    public void start() {
        if(serverStarted){
            return;
        }
        startNewServer();
    }

    /**
     * Stops the local socket server
     */
    public void stop() {
        if(!serverStarted){
            return;
        }
        try {
            closeAllConnections();
            socketServer.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        serverStarted = false;
        socketServer = null;
    }


    // The server can only be started once so we have to create a new instance on every restart
    private void startNewServer(){
        serverStarted = true;
        socketServer = new SocketServer(8080);
        socketServer.start();
    }

    private void closeAllConnections(){
        for(WebSocket con : socketServer.connections()){
            con.closeConnection(CloseFrame.NORMAL, "Server is stopping");
        }
    }

    private class SocketServer extends WebSocketServer {

        public SocketServer(int port) {
            super(new InetSocketAddress(port));
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            Log.d("WEBSOCKET", "Connected " + handshake.getResourceDescriptor());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            Log.d("WEBSOCKET", "Closed " + reason);

        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            Log.d("WEBSOCKET", message);

            synchronized (WebSocketInput.this) {

                if (inputInterface == null) {
                    return;
                }

                final InputType input = convertToInputType(message);

                inputHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (WebSocketInput.this) {
                            inputInterface.setInputState(input, true);
                            inputInterface.setInputState(input, false);
                        }
                    }
                });
            }
        }

        private InputType convertToInputType(String message) {
            switch (message.trim()) {
                case "1":
                    return InputType.INPUT1;
                case "2":
                    return InputType.INPUT2;
                case "3":
                    return InputType.INPUT3;
                case "4":
                    return InputType.INPUT4;
                default:
                    return null;
            }
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            Log.d("WEBSOCKET", "Error " + ex.getMessage());
        }
    }


}


