package Commands;

import Interfaces.Commands;
import LocalHostServerStuff.*;
import Scanners.BinanceScanner;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Buy implements Commands {
    private String ticker;
    private float quantity;
    private float price;
    private DatagramSocket ds;
    private int serverPort;
    private ArrayList<Long> idArr;
    public Buy(String ticker, DatagramSocket ds, int serverPort, ArrayList<Long> idArr) throws WrongServerWork, IOException {
        this.ticker = ticker.toUpperCase();
        this.price = BinanceScanner.httpRequest(ticker);
        this.quantity = (((MyBalance.getMyBalance(ds, serverPort))*5)/4)/price;
        this.ds = ds;
        this.serverPort = serverPort;
        this.idArr = idArr;
    }
    public Buy(String ticker, float price, DatagramSocket ds, int serverPort, ArrayList<Long> idArr) throws WrongServerWork {
        this.ticker = ticker.toUpperCase();
        this.price = price;
        this.quantity = (((MyBalance.getMyBalance(ds, serverPort))*5)/4)/price;
        this.ds = ds;
        this.serverPort = serverPort;
        this.idArr = idArr;
    }
    public Buy(String ticker, float price, float quantity, DatagramSocket ds, int serverPort, ArrayList<Long> idArr) {
        this.ticker = ticker.toUpperCase();
        this.quantity = quantity;
        this.price = price;
        this.ds = ds;
        this.serverPort = serverPort;
        this.idArr = idArr;
    }
    @Override
    public void run() {
        SendJson.sendJson(MakeJson.makeApplicationJson("long", price, quantity, ticker), ds, serverPort);
        try{
            ds.setSoTimeout(3000);
            ServerAnswer serverAnswer = ReceiveJson.receive(ds);
            String answer = serverAnswer.getAnswer();
            if (answer.equals("exception")){
                System.out.println("Не удалось создать заявку на покупку, проверьте баланс кошелька, " +
                        "цену и количество токенов");
            }else {
                String str = answer.split("'origQty'")[1].split(",")[0];
                String str2 = answer.split("'price'")[1].split(",")[0];
                System.out.println("Заявка на покупку " + str.substring(3, str.length()-1) + " токенов " + ticker
                        +" по цене "+str2.substring(3,str2.length()-1)+  "$ успешно создана");
                idArr.add(Long.parseLong(answer.split(":")[1].split(", ")[0].substring(1)));
            }
        }catch (WrongServerWork | SocketException wrongServerWork){
            System.out.println("Не удалось получить ответ от сервера...");
        }finally {
            System.out.println("___________________________________________________________________________");
        }
    }
}
