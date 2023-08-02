package Commands;

import Interfaces.Commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class AvgVolatility implements Commands {
    private static String URL = "https://fapi.binance.com/fapi/v1/klines?symbol={0}&interval=1d&limit={1}";
    private String tokenName;
    private int dayInterval;

    public float[] getArrVolatility(String tokenName, int dayInterval) throws Exception{
        HttpURLConnection connection;
        java.net.URL url;
        url = new URL(MessageFormat.format(URL, tokenName, dayInterval));
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader read = new BufferedReader(reader);
        String line = read.readLine();
        String[] subLine = line.substring(1, line.length() - 1).split(",");
        Iterator<String> iter = Arrays.stream(subLine).iterator();
        float[] arr = new float[dayInterval];
        for (int i = 0; i < dayInterval; i++) {
            float firstValue = 0;
            for (int j = 0; j < 12; j++) {
                if (j == 2) {
                    String now = iter.next();
                    firstValue = Float.parseFloat(now.substring(1, now.length() - 1));
                } else if (j == 3) {
                    String now = iter.next();
                    arr[i] = ((firstValue / Float.parseFloat(now.substring(1, now.length() - 1))) - 1f) * 100f;
                } else {
                    iter.next();
                }
            }
        }
        return arr;

    }

    public AvgVolatility(String tokenName, int dayInterval) {
        this.tokenName = tokenName.toUpperCase();
        this.dayInterval = dayInterval;
    }

    @Override
    public void run() {
        try {
            float[] arrFor2Month = getArrVolatility(tokenName, 60);
            float[] arr = getArrVolatility(tokenName, dayInterval);
            ArrayList<Float> finalArr = new ArrayList<>();
            for (int i = 0; i < dayInterval; i++){
                int counter = 0;
                for (int j = 0; j<60;j++){
                    if (arr[i] <= arrFor2Month[j]){
                        counter ++;
                    }
                }
                if (counter >= 12){
                    finalArr.add(arr[i]);
                }
            }
            double res = finalArr.stream().mapToDouble(num->(double)num).average().getAsDouble();
            DecimalFormat df = new DecimalFormat("#.##");
            String[] changeToDot = df.format(res).split(",");
            System.out.println("Средняя волатильность " + tokenName+ " за " + dayInterval+" дней: "+changeToDot[0]+"."+changeToDot[1]+"%");
        }catch (Exception e){
            System.out.println("Не удалось получить информацию из Binance");
        }
        System.out.println("___________________________________________________________________________");

    }
}
