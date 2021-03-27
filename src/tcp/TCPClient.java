package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class TCPClient implements  AutoCloseable, Runnable{


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private LinkedList<Registration> registrations;


    public TCPClient(String n) throws Exception {
        name=n;

    }



    private  boolean connection(String host, int port) throws IOException, InterruptedException {
        socket = new Socket(host, port);

        Thread.sleep((int)(Math.random()*2000));
        output = socket.getOutputStream();
        output.write(("name:"+name).getBytes());
        output.flush();
        input = socket.getInputStream();
        byte[] buf= new byte[Services.size];
        input.read(buf);
        Object incomeMessage=Services.convertObjectFromBytes(buf);
        //System.out.println((String)incomeMessage);

        output.write(("Show regs").getBytes());
        output.flush();
        input.read(buf);
        incomeMessage=Services.convertObjectFromBytes(buf);

        try{
            registrations=(LinkedList<Registration>)incomeMessage;

            Date date = new Date(System.currentTimeMillis());


            if(!registrations.isEmpty())
            {
                String info=("Registations for "+name+" "+date+ ": "+"\n");
                for (var x:registrations )
                {
                    info+=(x.getUser()+"\\"+x.getDate()+"\n");
                }
                System.out.println(info);
            }
        }
        catch (Exception e){
            System.out.println( e.getMessage());
        }
        socket.close();
        return true;
    }

    @Override
    public void close() throws Exception {
        if(socket!=null && !socket.isClosed()) socket.close(); }

    @Override
    public void run() {
        try {
            connection(InetAddress.getLocalHost().getHostAddress(), Services.port);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  static void main(String[]args) throws Exception {

        for(int m=0;m<8;m++)
                for(int i=0;i<20;i++)
                    try (TCPClient client = new TCPClient("Client"+i)){
                                            new Thread(client).start();
                    }
                System.out.println("Wait");
    }
}
