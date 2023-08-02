package Commands;

import Interfaces.Commands;

import java.io.IOException;

public class VolDeviation implements Commands {
    private String tokenName;
    private int days;
    public VolDeviation (String tokenName, int days){
        this.tokenName = tokenName.toUpperCase();
        this.days = days;
    }
    public static double showDeviation(String tokenName, int days) throws IOException {
        double[] avgMethodCall = AvgVolume.getAvgVolume(tokenName, days+1);
        return avgMethodCall[0]/avgMethodCall[1];
    }
    @Override
    public void run() {

        try{
            double deviation = showDeviation(tokenName, days);
            if (deviation>1){
                System.out.println(String.format("Сегодня объем меньше, чем средний объем за предыдущие "+days+" дней на %.2f", (deviation -1)*100)+"%");
            }else{
                System.out.println(String.format("Сегодня объем больше, чем средний объем за предыдущие "+days+" дней на %.2f", ((1-deviation)*100))+"%");
            }
        }catch (IOException e){
            System.out.println("Не удалось получить данные с Binance, проверьте подключение к интернету");
        }
        System.out.println("___________________________________________________________________________");
    }
}
