package Commands;

import Interfaces.Commands;

import java.time.ZonedDateTime;

public class ShowTime implements Commands {
    @Override
    public void run() {
        System.out.println(ZonedDateTime.now().toString().split("T")[1].split("\\.")[0]);
        System.out.println("___________________________________________________________________________");
    }
}
