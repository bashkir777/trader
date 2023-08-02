package LocalHostServerStuff;

import org.json.simple.JSONObject;

import java.net.*;
import java.nio.ByteBuffer;

public class SendJson {
    public static void sendJson(JSONObject json, DatagramSocket ds, int portServer){

        InetAddress localAddr;
        try{
            localAddr = InetAddress.getByName("localhost");
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        byte[] buf = ByteBuffer.wrap(json.toJSONString().getBytes()).array();
        DatagramPacket dp;
        try{
            dp = new DatagramPacket(buf, buf.length, localAddr, portServer);
        }catch (Exception e){
            System.out.println("Не удалось отправить json на сервер");
            return;
        }
        try {
            ds.send(dp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
