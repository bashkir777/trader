package LocalHostServerStuff;
import org.json.simple.JSONObject;
public class MakeJson {
    public static JSONObject makeApplicationJson(String type, float price, float quantity,String ticker){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commandType", "application");
        jsonObject.put("price", Float.toString(price));
        jsonObject.put("quantity", Float.toString(quantity));
        jsonObject.put("ticker", ticker);
        jsonObject.put("typeOperation", type);
        return jsonObject;
    }
    public static JSONObject makeCommandJson(String commandJson){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commandType", "no_args");
        jsonObject.put("command", commandJson);
        return jsonObject;
    }

    public static JSONObject makeCommandJson(String commandJson, String arg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commandType", "one_arg");
        jsonObject.put("command", commandJson);
        jsonObject.put("argument", arg);
        return jsonObject;
    }

    public static JSONObject makeCommandJson(String commandJson, String arg1, String arg2){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commandType", "two_arg");
        jsonObject.put("command", commandJson);
        jsonObject.put("argument1", arg1);
        jsonObject.put("argument2", arg2);
        return jsonObject;
    }
}
