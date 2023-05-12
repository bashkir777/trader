package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.util.Collection;
import java.util.Iterator;

public class Clear implements Commands {
    private Collection<BinanceScanner> scannersList;
    public Clear(Collection<BinanceScanner> scannersList){
        this.scannersList = scannersList;
    }
    @Override
    public void run() {
        Iterator<BinanceScanner> iterator = scannersList.iterator();
        while (iterator.hasNext()){
            BinanceScanner scanner = iterator.next();
            scanner.interrupt();
            iterator.remove();
        }
        System.out.println("Список монет для анализа был успешно очищен");
        System.out.println("___________________________________________________________________________");
    }
}
