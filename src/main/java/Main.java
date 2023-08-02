import Commands.*;
import Enums.ScansEnum;
import Enums.Side;
import LocalHostServerStuff.*;
import Scanners.BinanceScanner;
import Enums.Commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.*;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Long> idArr = new ArrayList<>();
        int botPort = 15500;
        int serverPort = 3111;
        /*try{
            botPort = Integer.parseInt(args[0]);
            serverPort = Integer.parseInt(args[0]);
            DatagramSocket ds = new DatagramSocket(botPort);
            ds.close();
        }catch (Exception e){
            System.out.println("При запуске в аргументы программы надо передать два номера порта:"+"\n"+
                    " порт для бота и порт для сервера");
            System.exit(0);
        }*/

         /*ClassLoader loader = Main.class.getClassLoader();
        InputStream stream = loader.getResourceAsStream("text.txt");
        Scanner scanner = new Scanner(stream);
        System.out.println(scanner.nextLine());*/

        DatagramSocket ds;

        try{
            ds = new DatagramSocket(botPort);
        }catch (IOException e){
            System.out.println("Не удалось открыть подключение по заданному порту");
            return;
        }

        long start = System.currentTimeMillis();
        ArrayList<BinanceScanner> scannersList = new ArrayList<>();
        Scanner scanner1 = new Scanner(System.in);

        while (true) {
            System.out.print("Введите команду: ");
            String line = scanner1.nextLine();
            boolean commandFound = false;
            String[] cmdArr = line.split(" ");
            try {
                for (Commands cmd : Commands.values()) {
                    if (cmd.getName().equals(line.split(" ")[0])) {
                        switch (cmd) {
                            case INFO -> new Info(line.split(" ")[1], scannersList).run();
                            case EXIT -> new Exit().run();
                            case SHOW_ALL_PRICES -> new ShowAllPrices(scannersList).run();
                            case SHOW_ALL_SPREADS -> new ShowAllSpreads(scannersList).run();
                            case SHOW_TIME_WORKING -> new ShowTimeWorking(start).run();
                            case START -> {
                                try {
                                    new Start(scannersList, ScansEnum.valueOf(cmdArr[2].toUpperCase()), Float.parseFloat(cmdArr[3]),
                                            Integer.parseInt(cmdArr[4]), cmdArr[1]).run();
                                } catch (Exception e2) {
                                    System.out.println("Порядок ввода аргументов: название, тип сканирования, проценты, минуты");
                                    System.out.println("___________________________________________________________________________");
                                }
                            }
                            case DELETE -> new Delete(scannersList, cmdArr[1]).run();
                            case SHOW_AVERAGE ->
                                    new ShowAverage(line.split(" ")[1], Integer.parseInt(line.split(" ")[2])).run();
                            case CLEAR -> new Clear(scannersList).run();
                            case SHOW_NOW_PRICE -> new ShowNowPrice(cmdArr[1]).run();
                            case HELP -> new Help().run();
                            case SHOW_TIME -> new ShowTime().run();
                            case SHOW_DEVIATION -> new ShowDeviation(cmdArr[1], Integer.parseInt(cmdArr[2])).run();
                            case ANALISE_INFO -> new AnaliseInfo(scannersList, cmdArr[1]).run();
                            case FEAR_AND_GREED -> new FearNGread().run();
                            case START_FILE -> new StartFile(cmdArr[1], scannersList).run();
                            case AVG_VOLATILITY -> new AvgVolatility(cmdArr[1], Integer.parseInt(cmdArr[2])).run();
                            case BRIEF -> new Brief().run();
                            case AVG_VOLUME -> new AvgVolume(cmdArr[1], Integer.parseInt(cmdArr[2])).run();
                            case VOL_DEVIATION -> new VolDeviation(cmdArr[1], Integer.parseInt(cmdArr[2])).run();
                            case CHANGE -> new Change(cmdArr[1], cmdArr[2], Integer.parseInt(cmdArr[3])).run();
                            case PIVOT_POINT -> new PivotPoint(cmdArr[1]).run();
                            case MY_BALANCE -> new MyBalance(ds, serverPort).run();
                            case BUY -> {
                                try{
                                    if (cmdArr.length == 2){
                                        new Buy(cmdArr[1], ds, serverPort, idArr).run();
                                    } else if (cmdArr.length == 3) {
                                        new Buy(cmdArr[1], Float.parseFloat(cmdArr[2]), ds, serverPort, idArr).run();
                                    } else if (cmdArr.length == 4) {
                                        new Buy(cmdArr[1], Float.parseFloat(cmdArr[2]),Float.parseFloat(cmdArr[3]), ds
                                                , serverPort, idArr).run();
                                    }else{
                                        System.out.println(1);
                                        throw new NumberFormatException();
                                    }
                                }catch (WrongServerWork wrongServerWork){
                                    System.out.println("Не удалось получить ответ от сервера...");
                                }catch (IOException exception){
                                    System.out.println("Не удалось получить ответ от binance");
                                }
                            }
                            case SELL -> {
                                try{
                                    if (cmdArr.length == 2){
                                        new Sell(cmdArr[1], ds, serverPort, idArr).run();
                                    } else if (cmdArr.length == 3) {
                                        new Sell(cmdArr[1], Float.parseFloat(cmdArr[2]), ds, serverPort, idArr).run();
                                    } else if (cmdArr.length == 4) {
                                        new Sell(cmdArr[1], Float.parseFloat(cmdArr[2]),Float.parseFloat(cmdArr[3]), ds
                                                , serverPort, idArr).run();
                                    }else{
                                        System.out.println(1);
                                        throw new NumberFormatException();
                                    }
                                }catch (WrongServerWork wrongServerWork){
                                    System.out.println("Не удалось получить ответ от сервера...");
                                }catch (IOException exception){
                                    System.out.println("Не удалось получить ответ от binance");
                                }
                            }
                            case VPN -> {
                                if (cmdArr[1].equals("--on") || cmdArr[1].equals("--off")){
                                    new Vpn(cmdArr[1], ds, serverPort).run();
                                }else {
                                    throw new IndexOutOfBoundsException();
                                }
                            }
                            case SCALP -> {
                                if(cmdArr[1].equals("--buy") || cmdArr[1].equals("--sell")){
                                    if ( cmdArr.length == 3){
                                        new Scalp(cmdArr[2], Side.valueOf(cmdArr[1].toUpperCase().substring(2)), ds, serverPort
                                                , idArr).run();
                                    } else if (cmdArr.length == 4) {
                                        new Scalp(cmdArr[2], Float.parseFloat(cmdArr[3]),
                                                Side.valueOf(cmdArr[1].toUpperCase().substring(2)), ds, serverPort, idArr).run();
                                    } else if (cmdArr.length == 5) {
                                        new Scalp(cmdArr[2], Float.parseFloat(cmdArr[3]),
                                                Side.valueOf(cmdArr[1].toUpperCase().substring(2)),Float.parseFloat(cmdArr[4]),
                                                ds, serverPort, idArr).run();
                                    } else{
                                        throw new IndexOutOfBoundsException();
                                    }
                                }else {
                                    throw new IndexOutOfBoundsException();
                                }
                            }
                        }
                        commandFound = true;
                    }
                }
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                //e.printStackTrace();
                System.out.println("В команду были переданы не все требуемые аргументы");
                System.out.println("Чтобы посмотреть информацию о командах введите help");
                System.out.println("___________________________________________________________________________");
                continue;
            }
            if (!commandFound) {
                System.out.println("Такой команды не существует");
                System.out.println("___________________________________________________________________________");
            }
        }
    }
}