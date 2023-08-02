package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.util.Collection;

public class ShowAllSpreads implements Commands {
    private Collection<BinanceScanner> scannersList;
    public ShowAllSpreads(Collection<BinanceScanner> scannersList){
        this.scannersList = scannersList;
    }
    @Override
    public void run() {
        for(BinanceScanner binanceScanner: scannersList){
            float maximum=-1;
            float minimum=10000;
            float firstValue = 0;
            for (float price:binanceScanner.getScan().getQueue().values()){
                if (firstValue == 0){
                    firstValue = price;
                }
                if (price>maximum){
                    maximum = price;
                }
                if (price<minimum){
                    minimum = price;
                }
            }
            System.out.println("Spread " + binanceScanner.getTokenName() + " - " + ((maximum/minimum)-1)*100 + "%");
        }
        if (scannersList.size() == 0){
            System.out.println("Список монет для анлиза пуст");
        }
        System.out.println("___________________________________________________________________________");
    }
}
