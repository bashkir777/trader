package Commands;

import Interfaces.Commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.OptionalDouble;

public class ShowAverage implements Commands {
    private String tokenName;
    private int interval;
    private static String URL = "https://fapi.binance.com/fapi/v1/klines?symbol={0}&interval=1d&limit={1}";

    public ShowAverage(String tokenName, int interval){
        this.tokenName = tokenName.toUpperCase();
        this.interval = interval;

    }

    @Override
    public void run()  {
        HttpURLConnection connection;
        java.net.URL url;
        try{
            url = new URL(MessageFormat.format(URL, tokenName, interval));
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader read = new BufferedReader(reader);
            String line = read.readLine();
            StringBuilder sb = new StringBuilder(line);
            line = sb.substring(1, sb.length()-1);
            String[] arr = line.split(",");
            Iterator<String> iterator = Arrays.stream(arr).iterator();
            ArrayList<Float> prices = new ArrayList<>();
            ArrayList<String> arrayList = new ArrayList<>();
            int counter = 1;
            while (iterator.hasNext()){
                String now = iterator.next();
                arrayList.add(now);
                counter++;
                if (counter == 13){
                    counter = 1;

                    float open = Float.parseFloat(arrayList.get(1).substring(1, (arrayList.get(1).length()-1)));
                    float close = Float.parseFloat(arrayList.get(4).substring(1, (arrayList.get(4).length()-1)));
                    prices.add(open);
                    prices.add(close);
                    arrayList.clear();
                }
            }
            OptionalDouble average = prices.stream().mapToDouble(value -> value).average();
            System.out.println("Среднее значение монеты " + tokenName + " за последние " + interval + " дней - "+ average.getAsDouble());
            System.out.println("___________________________________________________________________________");
        }catch (Exception e){
            System.out.println("Монеты с таким названием не существует");
            System.out.println("___________________________________________________________________________");
        }

    }
}
