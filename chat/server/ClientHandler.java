package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandler {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private String nick;
    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String msg = in.readUTF();
                            if(msg.startsWith("/auth")) {
                                AuthService as = new AuthService(ClientHandler.this, msg);
                            if(as.getAuthorized()) {
                                sendMsg("/authok");
                                server.subscribe(ClientHandler.this);
                                nick=as.getNick();
                                break;
                            }else {
                                sendMsg("wrong pass or login");
                                server.unSubscribe(ClientHandler.this);
                            }
                            }
                        }
                        while (true) {
                            String msg = in.readUTF();
                            System.out.println("from client: " + msg + " " + ClientHandler.this.socket.toString());
                            server.broadcast(nick, msg);
                            if (msg.equals("/end")) break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getNick(){
        return nick;
    }
}
