package Scanners;

import Commands.ShowTime;
import Enums.ScansEnum;
import Interfaces.Scans;
import Scans.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;



public class BinanceScanner extends Thread {
    private static final String apiKey = "https://fapi.binance.com/fapi/v1/ticker/price?symbol=";
    private final String tokenName;
    private final ScansEnum type;
    private final int minutes;
    private final float percent;
    private Scans scan;
    private float nowPrice;

    public int getMinutes() {
        return minutes;
    }

    public float getPercent() {
        return percent;
    }

    public String getTokenName() {
        return tokenName;
    }

    public BinanceScanner(String tokenName, ScansEnum type, int minutes, float percent) {
        this.tokenName = tokenName.toUpperCase();
        this.type = type;
        this.minutes = minutes;
        this.percent = percent;
    }
    public static float httpRequest(String tokenName) throws IOException {
        HttpURLConnection connection;
        URL url = new URL(apiKey + tokenName);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader read = new BufferedReader(reader);
        String line = read.readLine().split(",")[1].split(":")[1];
        return Float.parseFloat(line.substring(1, line.length() - 1));
    }
    public Scans getScan() {
        return scan;
    }

    public void setScan(Scans scan) {
        this.scan = scan;
    }

    public float getNowPrice() {
        return nowPrice;
    }

    @Override
    public void run(){
        StringBuilder sb = new StringBuilder();
        Scans scan = null;
        switch (type) {
            case UP -> {
                scan = new UpScan(tokenName, percent, minutes);
                this.setScan(scan);
            }
            case DOWN -> {
                scan = new DownScan(tokenName, percent, minutes);
                this.setScan(scan);
            }
        }
        try {
            while (true) {
                try{
                    this.nowPrice = httpRequest(tokenName);
                }catch (IOException e) {
                    System.out.println("Поток монеты " + tokenName + " не получает цену на протяжении 5 секунд");
                    System.out.println("___________________________________________________________________________");
                }

                boolean startTrade = scan.scanIteration(nowPrice);
                scan.getQueue().put(System.currentTimeMillis(), nowPrice);
                sb.delete(0, sb.length());
                if (startTrade) {
                    System.out.print("Управление передано программе-трейдеру в ");
                    new ShowTime().run();
                    String path = scan.getPath();
                    String price = Float.toString(nowPrice);
                    try {
                        String[] args = {"python_1", path, tokenName, price};
                        Runtime.getRuntime().exec(args);
                    } catch (IOException ex) {
                        System.out.println("Не удалось запустить отработку сигнала");
                    }
                    System.exit(0);
                }
                Thread.sleep(200);
            }

        } catch (InterruptedException e) {
            assert true;
        }

    }
}
