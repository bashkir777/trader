package Commands;

import Enums.ScansEnum;
import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.util.Collection;

public class Start implements Commands {
    private Collection<BinanceScanner> scannersList;
    private float percent;
    private int minutes;
    private ScansEnum scansEnum;
    private String tokenName;
    public Start(Collection<BinanceScanner> scannersList, ScansEnum scansEnum, float percent, int minutes, String tokenName){
        this.scannersList = scannersList;
        this.percent = percent;
        this.minutes = minutes;
        this.scansEnum = scansEnum;
        this.tokenName = tokenName;
    }
    @Override
    public void run() {
        try{
            BinanceScanner.httpRequest(tokenName);
            try {
                BinanceScanner binanceScanner = new BinanceScanner(tokenName, scansEnum, minutes, percent);
                scannersList.add(binanceScanner);
                binanceScanner.start();
                System.out.println("Сканирование " + tokenName.toUpperCase() + " успешно запущено");
            }catch (Exception e){
                System.out.println("Не удалось запустить сканирование " + tokenName.toUpperCase());
            }
            System.out.println("___________________________________________________________________________");
        }catch (Exception e){
            System.out.println("Монеты с таким названием не существует");
        }

    }
}
