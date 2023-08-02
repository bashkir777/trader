package Interfaces;

import Enums.ScansEnum;

import java.util.HashMap;
import java.util.Map;

public interface Scans {
    boolean scanIteration(Float nowPrice);
    void setQueue(Map<Long, Float> queue);
    Map<Long, Float> getQueue();
    String getPath();
    ScansEnum getType();
}
