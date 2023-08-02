package Scans;

import Enums.ScansEnum;
import Interfaces.Scans;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UpScan implements Scans {

    private String pathToConduct = "C:\\Users\\suple\\Desktop\\machinations\\Scalping\\LongTrader.py";
    private String tokenName;
    private Map<Long, Float> queue;
    private int minutes;
    private float percent;
    private ScansEnum type = ScansEnum.UP;

    public ScansEnum getType() {
        return type;
    }
    public UpScan(String tokenName, float percent, int minutes){
        this.tokenName = tokenName;
        this.queue = new LinkedHashMap<>();
        this.percent = percent;
        this.minutes = minutes;
    }
    @Override
    public String getPath() {
        return this.pathToConduct;
    }
    public void setQueue(Map<Long, Float> queue) {
        this.queue = queue;
    }

    public Map<Long, Float> getQueue() {
        return queue;
    }

    public boolean scanIteration(Float nowPrice){

        StringBuilder sb = new StringBuilder();
        long firstTime = 0;
        long lastTime;
        float tempValue;

        for (Map.Entry<Long, Float> entry : queue.entrySet()) {
            tempValue = entry.getValue();
            lastTime = entry.getKey();
            if (firstTime == 0) {
                firstTime = entry.getKey();
            }
            if (tempValue * (1 + (percent/100)) <= nowPrice) {
                sb.delete(0, sb.length());
                sb.append("Стоимость ").append(tokenName).append(" увеличилась на: ")
                        .append(((nowPrice/tempValue)-1)*100).append("% за ")
                        .append(((System.currentTimeMillis() - lastTime) / 1000 / 60)).append(" минут\n");
                sb.append("Цена на старте: ").append(tempValue).append(" Цена сейчас: ").append(nowPrice).append("\n");
                System.out.println(sb);
                return true;
            }
        }
        this.setQueue(queue.entrySet().stream()
                .filter(longFloatEntry -> System.currentTimeMillis()-longFloatEntry.getKey()<((long) minutes * 60 * 1000))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        sb.delete(0, sb.length());
        return false;
    }
}
