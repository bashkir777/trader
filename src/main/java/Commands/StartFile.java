package Commands;

import Enums.ScansEnum;
import Scanners.BinanceScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class StartFile implements Interfaces.Commands{
    private String filePath;
    ArrayList<BinanceScanner> scannersList;
    public StartFile(String filePath, ArrayList<BinanceScanner> scannersList){
        this.filePath = filePath;
        this.scannersList = scannersList;
    }
    @Override
    public void run() {
        try{
            Scanner scanner = new Scanner(new File(filePath));
            short counter = 0;
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                try{
                    new Start(scannersList, ScansEnum.valueOf(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]),
                            Integer.parseInt(line.split(" ")[3]), line.split(" ")[0]).run();
                }catch (Exception e){
                    System.out.println("Строка: " + line+ " не соответсвует формату: название, тип сканирования" +
                            ", проценты, минуты");
                    counter+=1;
                }
                if (counter == 0){
                    System.out.println("Содержимое файла успешно обработано");
                }else {
                    System.out.println("Формату не соответствуют " + counter + " строки, остальные успешно обработаны");
                }
                System.out.println("___________________________________________________________________________");
            }

        }catch (FileNotFoundException e){
            System.out.println("Не удалось найти файл по данному пути");
            System.out.println("___________________________________________________________________________");
        }

    }
}
