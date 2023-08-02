package Commands;

import Interfaces.Commands;
import LocalHostServerStuff.*;

import java.net.DatagramSocket;

public class MyBalance implements Commands {
    private DatagramSocket ds;
    private int serverPort;
    public MyBalance(DatagramSocket ds, int serverPort){
        this.ds = ds;
        this.serverPort = serverPort;
    }
    public static float getMyBalance(DatagramSocket ds, int serverPort) throws WrongServerWork {

        SendJson.sendJson(MakeJson.makeCommandJson("balance"), ds, serverPort);
        ServerAnswer serverAnswer = ReceiveJson.receive(ds);
        return Float.parseFloat(serverAnswer.getAnswer());
    }
    @Override
    public void run() {
        try {
            System.out.println("Баланс вашего фьючерсного кошелька binance - "+ String.format("%.2f",
                    getMyBalance(ds, serverPort))+"$");
        }catch (WrongServerWork e){
            System.out.println("Не удалось получить ответ от сервера...");
        }
        System.out.println("___________________________________________________________________________");
    }
}
