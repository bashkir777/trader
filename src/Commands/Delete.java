package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.util.Collection;
import java.util.Iterator;

public class Delete implements Commands {
    private Collection<BinanceScanner> scannersList;
    private String tokenName;
    public Delete(Collection<BinanceScanner> scannersList, String tokenName){
        this.scannersList = scannersList;
        this.tokenName = tokenName.toUpperCase();
    }
    @Override
    public void run() {
        boolean success = false;
        Iterator<BinanceScanner> iterator = scannersList.iterator();
        while (iterator.hasNext()){
            BinanceScanner nowScanner = iterator.next();
            if(nowScanner.getTokenName().equals(tokenName)){
                iterator.remove();
                nowScanner.interrupt();
                success = true;
            }
        }
        if (!success){
            System.out.println("Монеты с таким названием нет в списке сканирования");
        }else {
            System.out.println("Анализ монеты " + tokenName.toUpperCase() + " был завершен");
        }
        System.out.println("___________________________________________________________________________");
    }
}
