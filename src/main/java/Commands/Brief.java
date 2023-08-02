package Commands;

import Interfaces.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class Brief implements Commands {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static String[] tokens = {
            "ONTUSDT", "WAVESUSDT",
            "ADAUSDT", "FTMUSDT",
            "BATUSDT", "IOSTUSDT",
            "XRPUSDT",
            "ASHUSDT",
            "LPTUSDT",
            "FXSUSDT",
            "CHRUSDT",
            "USKUSDT",
            "ELOUSDT",
            "PHBUSDT",
            "ZECUSDT",
            "IMXUSDT",
            "TRXUSDT",
            "IGHUSDT",
            "BTCUSDT",
            "LTCUSDT",
            "ARBUSDT",
            "SOLUSDT",
            "NEARUSDT",
            "BCHUSDT",
            "EOSUSDT",
            "DOTUSDT",
            "MKRUSDT",
            "LINAUSDT",
            "MTLUSDT"
    };

    public static ArrayList<String> findTokensToTrade(ArrayList<String> list) {
        ArrayList<String> toReturn = new ArrayList<>();
        for (String token: list){
            try {
                double now = ShowDeviation.showDeviation(token, 10);
                if (now < -6 || now > 6){
                    toReturn.add(token);
                }
            }catch (IOException e){
                assert true;
            }
        }

        return toReturn;

    }
    public static class Worker extends Thread{
        private ArrayList<String> list = new ArrayList<>();
        private ArrayList<String> toReturn = new ArrayList<>();
        public Worker(ArrayList<String> list){
            this.list.addAll(list);
        }
        @Override
        public void run() {
            ArrayList<String> insideList = findTokensToTrade(list);
            toReturn.addAll(insideList);
        }
    }
    @Override
    public void run() {
        System.out.print(ANSI_YELLOW+"Fear & greed: ");
        System.out.println(FearNGread.getFnG()+ANSI_RESET);
        ArrayList<String> forWorker1 = new ArrayList<>();
        ArrayList<String> forWorker2 = new ArrayList<>();
        ArrayList<String> forWorker3 = new ArrayList<>();
        ArrayList<String> forWorker4 = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++){
            if (i % 4 == 0){
                forWorker1.add(tokens[i]);
            } else if (i % 4 == 1) {
                forWorker2.add(tokens[i]);
            } else if (i % 4 == 2) {
                forWorker3.add(tokens[i]);
            }else {
                forWorker4.add(tokens[i]);
            }
        }
        Worker worker1 = new Worker(forWorker1);
        worker1.start();
        Worker worker2 = new Worker(forWorker2);
        worker2.start();
        Worker worker3 = new Worker(forWorker3);
        worker3.start();
        Worker worker4 = new Worker(forWorker4);
        worker4.start();

        try{
            worker1.join();
            worker2.join();
            worker3.join();
            worker4.join();
        }catch (InterruptedException e){
            assert true;
        }
        ArrayList<String> list = new ArrayList<>();
        list.addAll(worker1.toReturn);
        list.addAll(worker2.toReturn);
        list.addAll(worker3.toReturn);
        list.addAll(worker4.toReturn);
        HashMap<String, Double> tokenVolUp = new HashMap<>();
        Thread worker5 = new Thread(){
            @Override
            public void run() {
                for(String token: list){
                    try{
                        double deviation = VolDeviation.showDeviation(token, 2);
                        if (deviation < 0.85){
                            tokenVolUp.put(token, deviation);
                        }
                    }catch (IOException e){
                        System.out.println("Проблемы с подключением к интернету...");
                    }
                }
            }
        };
        HashMap<String, Double> tokenVolDown = new HashMap<>();
        Thread worker6 = new Thread(){
            @Override
            public void run() {
                for(String token: list){
                    try{
                        double deviation = VolDeviation.showDeviation(token, 2);
                        if (deviation > 1.35){
                            tokenVolDown.put(token, deviation);
                        }
                    }catch (IOException e){
                        System.out.println("Проблемы с подключением к интернету...");
                    }
                }
            }
        };


        worker5.start();
        worker6.start();

        try{
            worker5.join();
            worker6.join();
        }catch (InterruptedException e){
            assert true;
        }

        if (list.size()==0){
            System.out.println(ANSI_BLUE+"Монет с большим отклонением от средней цены не было найдено"+ANSI_RESET);
            return;
        }
        System.out.println(ANSI_BLUE+"Список монет с большим отклонением от средней цены за 10 дней:"+ANSI_RESET);

        for(String token: list){
            try {
                double deviation = ShowDeviation.showDeviation(token, 10);
                String strDeviation = Double.toString(deviation);
                strDeviation = strDeviation.split("\\.")[0]+"."+strDeviation.split("\\.")[1].substring(0,2);
                String sign = "";
                if (deviation > 0){
                    sign ="+";
                }
                System.out.println(token + " "+sign + strDeviation + "% от средней цены за последние 10 дней");
            }catch (IOException e){
                System.out.println("Проблемы с подключением к интернету...");
            }
        }

        if(tokenVolUp.size()==0){
            System.out.println(ANSI_GREEN+"Монеты с повышенным объемом отсутствуют"+ANSI_RESET);
        }else{
            System.out.println(ANSI_GREEN+"Список монет с повышенным объемом относительно среднего за последние 2 дня:"+ANSI_RESET);
            for (Map.Entry<String, Double> entry:tokenVolUp.entrySet()){
                String token = entry.getKey();
                double deviation = entry.getValue();
                System.out.println("Объём "+ token+ " больше среднего за последние 2 дня на " +String.format("%.2f", (1-deviation)*100) + "%");
            }
        }



        if(tokenVolDown.size()==0){
            System.out.println(ANSI_RED+"Монеты с пониженным объемом отсутствуют"+ANSI_RESET);
            System.out.println("___________________________________________________________________________");
            return;
        }

        System.out.println(ANSI_RED+"Список монет с пониженным объемом относительно среднего за последние 2 дня:"+ANSI_RESET);
        for (Map.Entry<String, Double> entry:tokenVolDown.entrySet()){
            String token = entry.getKey();
            double deviation = entry.getValue();
            System.out.println("Объём "+ token+ " меньше среднего за последние 2 дня на " +String.format("%.2f", (deviation-1)*100) + "%");
        }
        System.out.println("___________________________________________________________________________");
    }
}
