package Commands;

import Interfaces.Commands;
import LocalHostServerStuff.*;

import java.net.DatagramSocket;

public class Vpn implements Commands {
    private final String type;
    private final DatagramSocket ds;
    private final int serverPort;
    public Vpn(String flag, DatagramSocket ds, int serverPort){
        this.type = flag.substring(2);
        this.ds = ds;
        this.serverPort = serverPort;
    }
    @Override
    public void run() {
        SendJson.sendJson(MakeJson.makeCommandJson("vpn_"+type), ds, serverPort);
        try{
            ServerAnswer answer = ReceiveJson.receive(ds);

            if(answer.getAnswer().equals("успешно")){
                String typeAnswer = type.equals("on") ? "подключен":"отключен";
                System.out.println("vpn успешно "+ typeAnswer);
            }else{
                String typeAnswer = type.equals("on") ? "подключить":"отключить";
                System.out.println("не удалось "+ typeAnswer + " vpn");
            }

        }catch (WrongServerWork e){
            System.out.println("Не удалось получить ответ от сервера");
        }
        System.out.println("___________________________________________________________________________");

    }
}
