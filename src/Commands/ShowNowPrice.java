package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.io.IOException;

public class ShowNowPrice implements Commands {
    private String tokenName;
    public ShowNowPrice(String tokenName){
        this.tokenName = tokenName.toUpperCase();
    }

    @Override
    public void run() {
        try{
            float price = BinanceScanner.httpRequest(tokenName);
            System.out.println("Цена " + tokenName + " - " + price+"$");
        }catch (IOException e){
            System.out.println("Монеты с таким названием не существует");
        }
        System.out.println("___________________________________________________________________________");
    }
}
