package Commands;

import Interfaces.Commands;
import Interfaces.Scans;
import Scanners.BinanceScanner;

import java.util.Collection;

public class Info implements Commands {
    private String tokenName;
    private Collection<BinanceScanner> scannersList;
    private float nowPrice;
    private Scans scan;
    public Info(String tokenName, Collection<BinanceScanner> scannersList){
        this.tokenName = tokenName.toUpperCase();
        this.scannersList = scannersList;

    }
    @Override
    public void run() {
        boolean token_found = false;
        for (BinanceScanner binanceScanner : scannersList) {
            this.scan = binanceScanner.getScan();
            this.nowPrice = binanceScanner.getNowPrice();
            if (binanceScanner.getTokenName().equals(tokenName.toUpperCase())) {
                token_found = true;
                float maximum=-1;
                float minimum=10000;
                float firstValue = 0;
                for (float price:scan.getQueue().values()){
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

                System.out.println("Максимальная цена присутсвующая в очереди - " + maximum+"$");
                System.out.println("Минимальная цена присутсвующая в очереди - " + minimum+"$");
                System.out.println("Spread - " + ((maximum/minimum)-1)*100 + "%");
                System.out.println("Длина очереди - " + scan.getQueue().size());
                System.out.println("Цена сейчас - " + nowPrice + "$");
                System.out.println("Цена в начале анализа - "+ firstValue+"$");
                if (nowPrice > firstValue){
                    System.out.println("На доступном для анализа промежутке " +
                            "времени цена увеличилась на " + ((nowPrice / firstValue)-1) * 100 + "%");
                }else if (nowPrice < firstValue){
                    System.out.println("На доступном для анализа промежутке " +
                            "времени цена уменьшилась на " + ((firstValue / nowPrice)-1) * 100 + "%");
                }else{
                    System.out.println("Цена не изменилась");
                }
                System.out.println("___________________________________________________________________________");
            }
        }
        if (!token_found){
            System.out.println("Такой монеты нет в списке для анализа");
            System.out.println("___________________________________________________________________________");
        }
    }
}
