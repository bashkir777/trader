package Commands;
import Interfaces.Commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FearNGread implements Commands{
    public static String urlAddress = "https://alternative.me/crypto/fear-and-greed-index/";
    public static int getFnG(){
        HttpURLConnection connection;
        try{
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            BufferedReader read = new BufferedReader(reader);
            for (int i = 0; i < 210 ; i++){
                read.readLine();
            }
            while (true){
                String line = read.readLine().strip();
                if(line.startsWith("<div class=\"fng-circle\"")){
                    return Integer.parseInt(line.split(">")[1].split("<")[0]);
                }
            }
        }catch (Exception e){
            return 0;
        }
    }
    @Override
    public void run() {
        HttpURLConnection connection;
        try{
            String fng = "Текущий индекс F&G: ";
            System.out.println(fng + getFnG());
            System.out.println("___________________________________________________________________________");
        }catch (Exception e){
            System.out.println("Ошибка парса f&g");
            System.out.println("___________________________________________________________________________");
        }

    }
}
