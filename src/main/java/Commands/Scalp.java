package Commands;

import Enums.Side;
import Interfaces.Commands;
import LocalHostServerStuff.WrongServerWork;
import Scanners.BinanceScanner;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Scalp implements Commands {
    public final int leverage = 5;
    public static boolean tradeActive = false;
    private String tokenName;
    private float percent;
    private Side side;
    private float percentOfDep;
    private DatagramSocket ds;
    private int serverPort;
    private ArrayList<Long> idArr;

    public Scalp(String tokenName, Side side, DatagramSocket ds, int serverPort, ArrayList<Long> idArr) {
        this.percent = 1.0015f;
        this.tokenName = tokenName.toUpperCase();
        this.side = side;
        this.percentOfDep = 0.25f;
        this.ds = ds;
        this.serverPort = serverPort;
        this.idArr = idArr;
    }

    public Scalp(String tokenName, float percent, Side side, DatagramSocket ds, int serverPort, ArrayList<Long> idArr) {
        if (1 < percent && percent < 2) {
            this.tokenName = tokenName.toUpperCase();
            this.percent = percent;
            this.side = side;
            this.percentOfDep = 0.25f;
            this.ds = ds;
            this.serverPort = serverPort;
            this.idArr = idArr;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public Scalp(String tokenName, float percent, Side side, float percentOfDep, DatagramSocket ds, int serverPort,
                 ArrayList<Long> idArr) {
        if (1 < percent && percent < 2 && percentOfDep < 1 && percentOfDep > 0) {
            this.tokenName = tokenName.toUpperCase();
            this.percent = percent;
            this.side = side;
            this.percentOfDep = percentOfDep;
            this.ds = ds;
            this.serverPort = serverPort;
            this.idArr = idArr;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void run() {
        Thread scalpThread = new Thread() {
            @Override
            public void run() {
                tradeActive = true;


                try {
                    System.out.println(1);
                    float balance = MyBalance.getMyBalance(ds, serverPort);
                    System.out.println(balance);
                    System.out.println(balance * percentOfDep);
                    float price = BinanceScanner.httpRequest(tokenName);
                    if (side.equals(Side.BUY)) {
                        new Buy(tokenName, price, (balance * percentOfDep * leverage) / price, ds, serverPort, idArr).run();
                    } else {
                        new Sell(tokenName, price, (balance * percentOfDep * leverage) / price, ds, serverPort, idArr).run();
                    }
                    if (idArr.isEmpty()) {
                        System.out.println("Не удалось создать заявку на покупку");
                    }
                    for (int i = 0; i < 10; i++) {
                        if (NonUserCommands.isFilled(ds, serverPort, idArr.get(0))) {
                            boolean positionClose = NonUserCommands.closePosition(ds, serverPort, idArr.get(0), idArr,
                                    price * percent);
                            if (positionClose) {
                                idArr.remove(0);
                                break;
                            }
                        }
                        Thread.sleep(4000);

                    }
                    while (tradeActive) {
                        tradeActive = !NonUserCommands.isFilled(ds, serverPort, idArr.get(0));
                        Thread.sleep(3000);
                    }
                    idArr.remove(0);
                } catch (WrongServerWork e) {
                    System.out.println("Не удалось получить ответ от сервера...");
                } catch (IOException e) {
                    System.out.println("API Binance не доступен ");
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                    System.exit(0);
                }

            }
        };
        if (!tradeActive) {
            scalpThread.start();
            System.out.println("Процесс успешно запущен");
        } else {
            System.out.println("В данный момент нельзя открыть позицию, так как предыдущая позиция еще активна");
        }
        System.out.println("___________________________________________________________________________");


    }
}
