import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Frame implements Runnable, ActionListener {

    TextField textField;
    TextArea textArea;
    Button button;

    ServerSocket serverSocket;
    Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Thread chat;

    Server() {
        textField = new TextField();
        textArea = new TextArea();

        button = new Button("Send");
        button.addActionListener(this);

        chat = new Thread(this);
        chat.setDaemon(true);
        chat.start();

        add(textField);
        add(textArea);
        add(button);

        setSize(500, 500);
        setTitle("Server");
        setLayout(new FlowLayout());
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(1200);
            socket = serverSocket.accept();

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String msg = dataInputStream.readUTF();
                textArea.append("Client: " + msg + "\n");
            }
        } catch (IOException E) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
               String msg = textField.getText();
               textArea.append("Server: " + msg + "\n");
               textField.setText("");

               dataOutputStream.writeUTF(msg);
               dataOutputStream.flush();

        } catch (IOException ex) {

        }
    }

    public static void main(String[] args) {
        new Server();
    }
}