import java.io.BufferedReader;
import java.net.*;
import java.io.*;

public class ServerTerminal {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // Constructor...
    public ServerTerminal(){
        // server = new ServerSocket(7777);  ---> will throw exception
        try{
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch( Exception e ) {
            // TODO :handle exception
            e.printStackTrace();
        }
    }
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
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : " + msg);
                    System.out.println("Client : " + msg);
                }
            }
            catch(Exception e){
                // e.printStackTrace();
                System.out.println("Connection Lost...!");
            }
        } ;

        new Thread(r1).start();
    }
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
        new ServerTerminal();
    }
}
