package chat.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.lang.System.*;


public class Controller implements Initializable {
    @FXML
    TextField textField;
    @FXML
    TextArea textArea;
    @FXML
    HBox authPanel;
    @FXML
    HBox msgPanel;
    @FXML
    TextField loginField;
    @FXML
    PasswordField passField;

    private boolean authoriazed;
    public void setAuthorized(boolean authorized) {
        this.authoriazed = authorized;
        if(authoriazed) {
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);
            textArea.clear();
            authPanel.setVisible(false);
            authPanel.setManaged(false);
        }
        else   {
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);
            authPanel.setVisible(true);
            authPanel.setManaged(true);
        }
    }

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner sc = new Scanner(System.in);

    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 8189;


    public void sendMsg() {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendAuthMsg() {
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String s = in.readUTF();
                            if(s.equals("/authok")) {
                                setAuthorized(true);
                                break;
                            }
                            //System.out.println(s);
                            textArea.appendText(s + "\n");
                        }
                        while (true) {
                            String s = in.readUTF();
                            //System.out.println(s);
                            textArea.appendText(s + "\n");
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
            });
            t.setDaemon(true);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

