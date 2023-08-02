package Commands;

import LocalHostServerStuff.*;

import java.net.DatagramSocket;
import java.util.ArrayList;

public class NonUserCommands {
    public static boolean closePosition(DatagramSocket ds, int serverPort, long id, ArrayList<Long> idArr, float price)
            throws WrongServerWork {

        SendJson.sendJson(MakeJson.makeCommandJson("close", Long.toString(id), Float.toString(price)), ds, serverPort);
        ServerAnswer answer = ReceiveJson.receive(ds);
        long orderId = Long.parseLong(answer.getAnswer());
        if (orderId != 0) {
            idArr.add(orderId);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFilled(DatagramSocket ds, int serverPort, long id) throws WrongServerWork{
        SendJson.sendJson(MakeJson.makeCommandJson("is_filled", Long.toString(id)), ds, serverPort);
        ServerAnswer answer = ReceiveJson.receive(ds);
        return answer.getAnswer().equals("true");
    }

    public static boolean cancel(DatagramSocket ds, int serverPort, long id) throws  WrongServerWork{
        SendJson.sendJson(MakeJson.makeCommandJson("cancel", Long.toString(id)), ds, serverPort);
        ServerAnswer answer = ReceiveJson.receive(ds);
        return answer.getAnswer().equals("true");
    }
}
