package Commands;

import Interfaces.Commands;

public class Help implements Commands {
    @Override
    public void run() {
        System.out.println("Список команд, доступных в данной версии трейдера:");
        System.out.println("start [tokenName] [type] [percent] [minutes] - запускает сканирование монеты");
        System.out.println("delete [tokenName] - останавливает сканирование монеты");
        System.out.println("clear - останавливает сканирование всех монет");
        System.out.println("info [tokenName] - выводит информацию о сканируемой монете");
        System.out.println("show_all_prices - выводит цены всех сканируемых монет");
        System.out.println("show_all_spreads - выводит спреды всех сканируемых монет");
        System.out.println("fng - показывает fear&greed index");
        System.out.println("avg_volatility [tokenName] [days] - показывает среднюю волатильность за период в днях");
        System.out.println("show_average [tokenName] [days] - выводит среднюю цену монеты");
        System.out.println("show_now_price [tokenName] - выводит цену любой существующей монеты");
        System.out.println("show_time_working - выводит время работы трейдера");
        System.out.println("exit - завершает работу трейдера");
        System.out.println("show_time - показывает местное время");
        System.out.println("show_deviation [tokenName] [days] - выводит отклонение цены от средей в %");
        System.out.println("show_average [tokenName] [days] - выводит среднюю цену за период в днях");
        System.out.println("___________________________________________________________________________");
    }
}
