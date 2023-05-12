package Enums;

public enum Commands {
    INFO("info"), EXIT("exit"), SHOW_ALL_PRICES("show_all_prices"),
    SHOW_ALL_SPREADS("show_all_spreads"), SHOW_TIME_WORKING("show_time_working"),
    START("start"), DELETE("delete"), SHOW_AVERAGE("show_average"), CLEAR("clear"),
    SHOW_NOW_PRICE("show_now_price");
    private String name;
    Commands(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
