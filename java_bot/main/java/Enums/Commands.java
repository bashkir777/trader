package Enums;

public enum Commands {
    INFO("info"), EXIT("exit"), SHOW_ALL_PRICES("show_all_prices"),
    SHOW_ALL_SPREADS("show_all_spreads"), SHOW_TIME_WORKING("show_time_working"),
    START("start"), DELETE("delete"), SHOW_AVERAGE("show_average"), CLEAR("clear"),
    SHOW_NOW_PRICE("show_now_price"), HELP("help"), SHOW_TIME("show_time"),
    SHOW_DEVIATION("show_deviation"), ANALISE_INFO("analise_info"), FEAR_AND_GREED("fng"),
    START_FILE("start_file"), AVG_VOLATILITY("avg_volatility"), BRIEF("brief"), AVG_VOLUME("avg_volume"),
    VOL_DEVIATION("vol_deviation"), CHANGE("change"), PIVOT_POINT("pivot"), MY_BALANCE("balance"), BUY("buy"),
    SELL("sell"), VPN("vpn"), SCALP("scalp");
    private String name;
    Commands(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
