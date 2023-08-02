package Commands;

import Interfaces.Commands;

public class ShowTimeWorking implements Commands {
    private long start;
    public ShowTimeWorking(long start){
        this.start = start;
    }
    @Override
    public void run() {
        String str = String.format("Бот был запущен %.1f минут назад", ((System.currentTimeMillis()-start)/1000f/60f));
        System.out.println(str);
        System.out.println("___________________________________________________________________________");
    }
}
