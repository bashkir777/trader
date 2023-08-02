package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.util.Collection;

public class AnaliseInfo implements Commands {
    private Collection<BinanceScanner> scannersList;
    private String tokenName;
    public AnaliseInfo(Collection<BinanceScanner> scannersList, String tokenName){
        this.scannersList = scannersList;
        this.tokenName = tokenName.toUpperCase();
    }
    @Override
    public void run() {
        boolean flag = false;
        for(BinanceScanner scanner: scannersList){
            if (scanner.getTokenName().equals(tokenName)){
                System.out.println("Тип анализа: "+scanner.getScan().getType());
                System.out.println("Интервал: "+scanner.getMinutes());
                System.out.println("Процент: " + scanner.getPercent());
                flag = true;
            }
        }
        if (!flag){
            System.out.println("В списке для анализа нет монеты с таким называнием");
        }
        System.out.println("___________________________________________________________________________");
    }
}
