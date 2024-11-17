
import java.io.*;
import java.net.*;

public class ClientTerminal {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare Components


    // Constructor
    public ClientTerminal(){
        try{
            //System.out.println("Sending request to server...");
            System.out.println("Sending request to Server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection established...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }






    // Start Reading [ Method ]
    public void startReading(){
        // thread - data read karke deta rahega
        Runnable r1 = ()->{
            try {
                System.out.println("Reader Started...");
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        //System.out.println("Server terminated the chat..");
                        System.out.println("Server terminated the chat..");

                        socket.close();
                        break;
                    }
                    System.out.println("Server : " + msg);


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
        // System.out.println("This is client...");
        System.out.println("This is Client...");
        new ClientTerminal();
    }
}
