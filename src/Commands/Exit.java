package Commands;

import Interfaces.Commands;

public class Exit implements Commands {

    @Override
    public void run() {
        System.exit(1);
    }
}
