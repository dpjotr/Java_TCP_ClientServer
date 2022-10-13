package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
public class TCPServer{
    public  static void main(String[]args) {
        Map<String, LinkedList<Registration>> db= new HashMap<>();
        try (ServerSocket server=new ServerSocket(Services.port)){
                System.out.print("Server started\n");
                while(true) {
                    Socket client = server.accept();
                    new Thread(() -> {
                        try (OutputStream output = client.getOutputStream();
                             InputStream input = client.getInputStream()) {
                            String currentClient = null;
                            for (int i = 0; i < 2; i++) {
                                byte[] buf = new byte[30];
                                int n = input.read(buf);
                                String msg = new String(buf, 0, n);
                                System.out.println(msg);

                                if ((msg.startsWith("name:"))) {
                                    currentClient = msg.substring("name:".length());
                                    Date date = new Date(System.currentTimeMillis());
                                    Registration r = new Registration(date, currentClient);

                                    if (!db.containsKey(currentClient))
                                        synchronized (db) {
                                            db.put(currentClient, new LinkedList<>());
                                        }

                                    var list = db.get(currentClient);
                                    list.add(r);
                                    synchronized (db) {
                                        db.put(currentClient, list);
                                    }
                                    output.write(Services.convertObjectToBytes("Registration done"));
                                }
                                if (msg.equals("Show regs")) {
                                    output.write(Services.convertObjectToBytes(db.get(currentClient)));
                                }
                            }
                        }
                        catch (Exception ignored) {
                        }
                    }).start();
                }
        }
        catch (Exception e){
        }
        System.out.println("Server is closed");
    }
}
