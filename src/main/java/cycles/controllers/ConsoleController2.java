package cycles.controllers;

import cycles.services.ServicesForGuiController;
import cycles.utils.Printer;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleController2 {
    private static final int MAX_NUMBER_OF_CYCLES_TO_SHOW = 20;
    private static final int INVALID_OPTION = -1;
    private static final String INVALID_ENTRY = ">> ¡ERROR! → Entrada Invalida";
    private String path, pathForSave;
    private boolean isExactLimit, isWithID;
    private ServicesForGuiController services;

    public ConsoleController2() {
        this.path = null;
    }

    public void runConsole() {
        this.path = this.requestFilePath();
        this.services = new ServicesForGuiController(this.path);
        int action = this.requestAction();

        switch (action) {
            case 1:
                break;

            case 2:
                break;
        }
    }

    private String requestFilePath(){
        Printer.printRequestFilePath();
        String path = this.getPathFromInput(this.requestOption(1, 12));
        if (path == null)   return this.requestNewFilePath();
        return path;
    }
    private String requestNewFilePath() {
        Printer.printRequestNewFilePath();
        Scanner reader = new Scanner(System.in);
        String path = reader.next();

        while (!isValidFilePath(path)){
            System.out.println("\n>> Opción incorrecta. Intente nuevamente:");
            path = reader.next();
        }

        return path;
    }
    private int requestAction(){
        Printer.printActionsOptions();
        return this.requestOption(1, 2);
    }

    private int requestOption(int min, int max){
        Scanner reader = new Scanner(System.in);
        String in = reader.next();
        int option = this.getValidOption(in, min, max);
        System.out.println(option);
        while (option == INVALID_OPTION){
            System.out.println("\n>> Opción incorrecta. Intente nuevamente");
            in = reader.next();
            option = this.getValidOption(in, min, max);
        }

        return option;
    }
    private String getPathFromInput(int in) {
        switch (in){
            case 1:     return "src/main/resources/filesODEM/apache-camel-1.6.0.odem";
            case 2:     return "src/main/resources/filesODEM/apache-camel-1.6.1.odem";
            case 3:     return "src/main/resources/filesODEM/apache-camel-1.6.2.odem";
            case 4:     return "src/main/resources/filesODEM/apache-camel-2.0.0.odem";
            case 5:     return "src/main/resources/filesODEM/apache-cxf-2.0.6.odem";
            case 6:     return "src/main/resources/filesODEM/apache-cxf-2.1.1.odem";
            case 7:     return "src/main/resources/filesODEM/db-derby-10.8.1.2.odem";
            case 8:     return "src/main/resources/filesODEM/db-derby-10.9.1.0.odem";
            case 9:     return "src/main/resources/filesODEM/hibernate-core-4.0.0.Final.odem";
            case 10:    return "src/main/resources/filesODEM/hibernate-core-4.1.0.Final.odem";
            case 11:    return "src/main/resources/filesODEM/hibernate-core-4.2.0.Final.odem";
            default:    return null;
        }
    }
    private int getValidOption(String entry, int min, int max){
        try {
            int option = Integer.parseInt(entry);
            if (option >= min && option <= max)     return option;
            return INVALID_OPTION;

        } catch (NumberFormatException e){
            return INVALID_OPTION;
        }
    }
    private boolean isValidFilePath(String path){
        String regularExpression = "(.*?)\\.(ODEM|odem)$";
        boolean isODEM = Pattern.matches(regularExpression, path);
        File f = new File(path);
        boolean exist = f.exists();
        return isODEM && exist;
    }
}