package Commands;

import Interfaces.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AvgVolume implements Commands {
    // выводит средний объем, за указанный период в днях, в каждом дне берется объем за кол-во часов,
    // прошедших на момент запроса
    public static String httpKey = "https://fapi.binance.com/fapi/v1/klines?symbol={1}&interval=1h&limit={0}";
    //возвращает массив из 2 элементов [среднее за предыдущие дни, среднее за сегодняшний день]
    public static double[] getAvgVolume(String tokenName, int days) throws IOException {
        int hours = Integer.parseInt(ZonedDateTime.now().toString().split("T")[1].split(":")[0]);
        int period = (24* (days - 1)) + hours + 1;
        String httpFormated = MessageFormat.format(httpKey, period, tokenName);
        HttpURLConnection connection;
        URL url = new URL(httpFormated);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader read = new BufferedReader(reader);
        String line = read.readLine();
        String[] arr = line.substring(1, line.length()-1).split(",");
        ArrayList<Float> avgValues = new ArrayList<>();
        Iterator<String> iter = Arrays.stream(arr).iterator();
        for (int i = 0; i < days; i++){
            float summ=0;
            for (int j = 0; j < hours; j++){
                for (int g = 0; g < 5; g++){
                    iter.next();
                }
                String volumeStr = iter.next();
                float volume = Float.parseFloat(volumeStr.substring(1, volumeStr.length()-1));
                summ += volume;
                //System.out.println(volume);
                for (int g = 0; g < 6; g++){
                    iter.next();
                }
            }
            avgValues.add(summ/hours);

            for(int g = 0; g < (24 - hours)*12; g ++){
                try{
                    iter.next();
                }catch (NoSuchElementException e){
                    break;
                }
            }
        }
        double lastVal = avgValues.get(avgValues.size()-1);
        avgValues.remove(avgValues.size()-1);
        double avgLastFewDays;
        if (avgValues.size()!=0){
            avgLastFewDays = avgValues.stream().mapToDouble(value -> value).average().getAsDouble();
        }else{
            avgLastFewDays = 0;
        }

        return new double[]{avgLastFewDays, lastVal};

    }

    private String tokenName;
    private int days;
    public AvgVolume(String tokenName, int days){
        this.days = days;
        this.tokenName = tokenName.toUpperCase();
    }

    @Override
    public void run() {
        try{
            double[] arr = getAvgVolume(tokenName, days);
            System.out.println("Средний объем за последние " + days + " дней к данному моменту времени составляет "+ ((arr[0] + arr[1])/2) + " " +tokenName.split("U")[0]);
        }catch (IOException e){
            System.out.println("Не удалось получить данные с Binance");
        }
        System.out.println("___________________________________________________________________________");
    }
}
