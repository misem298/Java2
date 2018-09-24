package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

public class Server {
    private HashMap<Vector, String> clien = new HashMap<>();
    private Vector<ClientHandler> clients;
    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8189);
            clients= new Vector<>();
            System.out.println("Server started ... Waiting for clients");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected ");
                new ClientHandler(this, socket);
                //clients.add(new ClientHandler(this, socket));
            }
                } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String from, String msg) {
        if (msg.startsWith("/")){
            for (ClientHandler o : clients) {
                if (msg.startsWith("/" + o.getNick())) {
                    o.sendMsg(from + ": " + msg);
                }
            }
        } else {
            for (ClientHandler o: clients) {
            o.sendMsg(from + ": " + msg);
            }
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }
    public void unSubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
