package Commands;

import Interfaces.Commands;
import Scanners.BinanceScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.OptionalDouble;

public class ShowDeviation implements Commands {
    private final String tokenName;
    private final int interval;
    private static String URL = "https://fapi.binance.com/fapi/v1/klines?symbol={0}&interval=1d&limit={1}";
    public ShowDeviation(String tokenName, int interval){
        this.tokenName = tokenName.toUpperCase();
        this.interval = interval;
    }
    public static double average(String tokenName, int interval) throws IOException{
        HttpURLConnection connection;
        java.net.URL url;
        url = new URL(MessageFormat.format(URL, tokenName, interval+1));
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader read = new BufferedReader(reader);
        String line = read.readLine();
        StringBuilder sb = new StringBuilder(line);
        line = sb.substring(1, sb.length() - 1);
        String[] arr = line.split(",");
        Iterator<String> iterator = Arrays.stream(arr).iterator();
        ArrayList<Float> prices = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        int counter = 1;
        while (iterator.hasNext()) {
            String now = iterator.next();
            arrayList.add(now);
            counter++;
            if (counter == 13) {
                counter = 1;

                float open = Float.parseFloat(arrayList.get(1).substring(1, (arrayList.get(1).length() - 1)));
                float close = Float.parseFloat(arrayList.get(4).substring(1, (arrayList.get(4).length() - 1)));
                prices.add(open);
                prices.add(close);
                arrayList.clear();
            }
        }
        //prices.remove(prices.size()-1);
        OptionalDouble average = prices.stream().mapToDouble(value -> value).average();
        return average.getAsDouble();
    }
    public static double showDeviation(String tokenName, int interval) throws IOException{
        double average = average(tokenName, interval);
        double nowPrice = BinanceScanner.httpRequest(tokenName);
        if (nowPrice > average){
            return ((nowPrice/average)-1)*100;

        } else if (nowPrice < average) {
            return -((average/nowPrice)-1)*100;

        }else{
            return 0;
        }
    }
    @Override
    public void run() {
        try{
            double res = showDeviation(tokenName, interval);
            if (res>0){
                System.out.println("Цена монеты " + tokenName + " больше средней цены за последние "
                        + interval +" дней на "+String.format("%.2f", res)+"%");
            }else if(res<0){
                System.out.println("Цена монеты " + tokenName + " меньше средней цены за последние "
                        + interval +" дней на " + String.format("%.2f", res) + "%");
            }else {
                System.out.println("Цена монеты " + tokenName + " равняется средней цене за последние "
                        + interval + " дней");
            }
            System.out.println("___________________________________________________________________________");

        }catch (IOException e){
            System.out.println("Монеты с таким названием не существует");
            System.out.println("___________________________________________________________________________");
        }
    }
}
