import Commands.*;
import Enums.ScansEnum;
import Scanners.BinanceScanner;
import Enums.Commands;

import java.util.*;



public class Main {
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        ArrayList<BinanceScanner> scannersList = new ArrayList<>();
        Scanner scanner1 = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            String line = scanner1.nextLine();
            boolean commandFound = false;
            String[] cmdArr = line.split(" ");
            try{
                for (Commands cmd: Commands.values()){
                    if (cmd.getName().equals(line.split(" ")[0])){
                        switch (cmd){
                            case INFO -> new Info(line.split(" ")[1], scannersList).run();
                            case EXIT -> new Exit().run();
                            case SHOW_ALL_PRICES -> new ShowAllPrices(scannersList).run();
                            case SHOW_ALL_SPREADS -> new ShowAllSpreads(scannersList).run();
                            case SHOW_TIME_WORKING -> new ShowTimeWorking(start).run();
                            case START -> {
                                try{
                                    new Start(scannersList, ScansEnum.valueOf(cmdArr[2].toUpperCase()), Float.parseFloat(cmdArr[3]),
                                            Integer.parseInt(cmdArr[4]), cmdArr[1]).run();
                                }catch (Exception e2){
                                    System.out.println("Порядок ввода аргументов: название, тип сканирования, проценты, минуты");
                                    System.out.println("___________________________________________________________________________");
                                }
                            }
                            case DELETE -> new Delete(scannersList, cmdArr[1]).run();
                            case SHOW_AVERAGE -> new ShowAverage(line.split(" ")[1],Integer.parseInt(line.split(" ")[2])).run();
                            case CLEAR -> new Clear(scannersList).run();
                            case SHOW_NOW_PRICE -> new ShowNowPrice(cmdArr[1]).run();
                        }
                        commandFound = true;
                    }
                }
            }catch (IndexOutOfBoundsException e){
                System.out.println("В команду были переданы не все требуемые аргументы");
                System.out.println("Чтобы посмотреть информацию о командах введите help");
                System.out.println("___________________________________________________________________________");
                continue;
            }
            if (!commandFound){
                System.out.println("Такой команды не существует");
                System.out.println("___________________________________________________________________________");
            }
        }
    }
}