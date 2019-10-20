package cycles;

import cycles.utils.Printer;

import java.util.Scanner;

public class Prueba {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        Printer.printHeader();
        Printer.printTitle("configuration");
        Printer.printRequestFilePath();
        Printer.waitingAnswer();
        reader.nextLine();

        Printer.printRequestLimit();
        Printer.waitingAnswer();
        reader.nextLine();

        Printer.printRequestTypeLimit();
        Printer.waitingAnswer();
        reader.nextLine();

        Printer.printRequestIsWithID();
        Printer.waitingAnswer();
        reader.nextLine();

        Printer.printActionsOptions();
        Printer.waitingAnswer();
        reader.nextLine();

    }
}
