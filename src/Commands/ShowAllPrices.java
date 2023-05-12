package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.util.Collection;

public class ShowAllPrices implements Commands {
    private Collection<BinanceScanner> scannersList;
    public ShowAllPrices(Collection<BinanceScanner> scannersList){
        this.scannersList = scannersList;
    }
    @Override
    public void run() {
        for(BinanceScanner binanceScanner: scannersList){
            System.out.println("Стоимость "+binanceScanner.getTokenName() + " - " + binanceScanner.getNowPrice() + "$");
        }
        if (scannersList.size() == 0){
            System.out.println("Список монет для анaлиза пуст");
        }
        System.out.println("___________________________________________________________________________");
    }
}
