package LocalHostServerStuff;

import com.google.gson.Gson;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveJson {
    public static ServerAnswer receive(DatagramSocket ds) throws WrongServerWork {

        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        try{
            ds.setSoTimeout(3000);
            ds.receive(dp);
        }catch (Exception e){
            throw new WrongServerWork("Не удалось получить ответ сервера");
        }
        String answer = new String(dp.getData());
        StringBuilder sb = new StringBuilder();
        for(char c: answer.toCharArray()){
            if(c != 0){
                sb.append(c);
            }
        }
        Gson gson = new Gson();
        ServerAnswer serverAnswer = gson.fromJson(sb.toString(), ServerAnswer.class);
        return serverAnswer;

    }
}
