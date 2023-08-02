package Commands;

import Interfaces.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;

public class PivotPoint implements Commands {
    public static String httpKey = "https://fapi.binance.com/fapi/v1/klines?symbol={0}&interval=1d&limit=2";
    private String tokenName;
    public static float pivotRequest(String tokenName) throws IOException {
        HttpURLConnection connection;
        URL url = new URL(MessageFormat.format(httpKey, tokenName));
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader read = new BufferedReader(reader);
        String[] line = read.readLine().split(",");
        Iterator<String> iter = Arrays.stream(line).iterator();
        iter.next();
        iter.next();
        String highStr = iter.next();
        String lowStr = iter.next();
        String closeStr = iter.next();
        float high = Float.parseFloat(highStr.substring(1, highStr.length()-1));
        float low = Float.parseFloat(lowStr.substring(1, highStr.length()-1));
        float close = Float.parseFloat(closeStr.substring(1, highStr.length()-1));
        return (high + low + close) / 3;
    }
    public PivotPoint(String tokenName){
        this.tokenName = tokenName.toUpperCase();
    }
    @Override
    public void run() {
        try{
            float pivotPoint = pivotRequest(tokenName);
            System.out.println("Пивотная точка " + tokenName + " - "+ String.format("%.5f", pivotPoint)+ "$");
        }catch (IOException e){
            System.out.println("Не удалось получить данные с Binance");
        }
        System.out.println("___________________________________________________________________________");
    }
}
