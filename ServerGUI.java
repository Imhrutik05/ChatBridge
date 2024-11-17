import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.net.*;
import java.io.*;

public class ServerGUI extends JFrame{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);
    // Constructor...
    public ServerGUI(){
        // server = new ServerSocket(7777);  ---> will throw exception
        try{
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            startWriting();

        } catch( Exception e ) {
            // TODO :handle exception
            e.printStackTrace();
        }
    }

    private void createGUI() {
        // GUI Code

        this.setTitle("Server Messager[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Coding for Component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon(""));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Frame Layout
        this.setLayout(new BorderLayout());

        // Adding the component to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
//        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }



    // Start Reading [Method]
    public void startReading(){
        // thread - data read karke deta rahega
        Runnable r1 = ()->{
            try {
                System.out.println("Reader Started...");
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        // System.out.println("Client terminated the chat..");
                        System.out.println("Client terminated the chat..");
                        JOptionPane.showMessageDialog(this, "Client Terminated the chat" );
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : " + msg);
                    // System.out.println("Client : " + msg);
                    messageArea.append("Client : " + msg + "\n");
                }
            }
            catch(Exception e){
                // e.printStackTrace();
                System.out.println("Connection Lost...!");
            }
        } ;

        new Thread(r1).start();
    }

    // Start Writing Send [Method]
    public void startWriting(){
        // thread - data ko user se lega and then usko send karega client tk
        Runnable r2 = ()->{
            try {
                System.out.println("Writer started...");
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
                System.out.println("Connection Lost...!");
            }
            catch(Exception e){
                e.printStackTrace();

            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        // System.out.println("This is Server going to start server");
        System.out.println("Server is going to start a chat with Client");
        new ServerGUI();
    }
}
