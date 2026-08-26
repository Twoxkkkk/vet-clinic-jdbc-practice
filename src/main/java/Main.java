import presentation.cli.MainConsoleController;
import presentation.cli.shared.ServicesFactory;

public class Main {
    public static void main(String[] args) {

        ServicesFactory services = new ServicesFacade();

        MainConsoleController mainConsoleController = new MainConsoleController(services);
        mainConsoleController.start();
    }
}
