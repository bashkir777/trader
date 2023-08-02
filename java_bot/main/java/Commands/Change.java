package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;

public class Change implements Commands {
    private String tokenName;
    private int interval;
    private String type;
    private static String URL = "https://fapi.binance.com/fapi/v1/klines?symbol={0}&interval=1{1}&limit={2}";

    public Change(String type, String tokenName, int interval) throws NumberFormatException {
        this.tokenName = tokenName.toUpperCase();
        this.interval = interval;
        if (!(type.equalsIgnoreCase("-d") || !type.equalsIgnoreCase("-m")|| !type.equalsIgnoreCase("-h"))){
              throw new NumberFormatException();
        }
        this.type = type;

    }

    @Override
    public void run() {
        try{
            HttpURLConnection connection;
            java.net.URL url;
            url = new URL(MessageFormat.format(URL, tokenName, type.charAt(1),interval));
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader read = new BufferedReader(reader);
            String line = read.readLine();
            String[] subLine = line.substring(1, line.length() - 1).split(",");
            Iterator<String> iter = Arrays.stream(subLine).iterator();
            iter.next();
            String priceAtStartInterval = iter.next();
            priceAtStartInterval = priceAtStartInterval.substring(1, priceAtStartInterval.length()-1);
            float nowPrice = BinanceScanner.httpRequest(tokenName);
            float previousPrice = Float.parseFloat(priceAtStartInterval);
            float deviation;
            if (nowPrice > previousPrice){
                deviation = ((nowPrice/previousPrice)-1)*100;
                System.out.println("За указанный промежуток времени цена увеличилась на " + String.format("%.2f", deviation)+"%");
            } else if (nowPrice < previousPrice) {
                deviation = -(100-(nowPrice*100/previousPrice));
                System.out.println("За указанный промежуток времени цена уменьшилась на " + String.format("%.2f", deviation)+"%");
            }else{
                System.out.println("За указанный промежуток времени цена не изменилась");
            }
            System.out.println("___________________________________________________________________________");

        }catch (IOException e){
            System.out.println("Не удалось получить информацию с Binance...");

        }
    }
}
