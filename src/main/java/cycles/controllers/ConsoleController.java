package cycles.controllers;

import cycles.services.ServicesForControllers;
import cycles.utils.Printer;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleController {
    private static final int MAX_NUMBER_OF_CYCLES_TO_SHOW = 20;
    private static final int INVALID_OPTION = -1;
    private String path;
    private ServicesForControllers services;

    public ConsoleController() {
        this.path = null;
    }

    public void runConsole() {
        boolean continueOtherFile = true;

        while (continueOtherFile){
            continueOtherFile = false;
            this.path = this.requestFilePath();
            this.services = new ServicesForControllers(this.path);
            boolean continueSameFile = true;

            while (continueSameFile){
                this.services.resetCycleInformationBuilders();
                Printer.printActionsOptions();
                int action = this.requestOption(2);

                boolean isExactLimit = this.requestTypeLimit();
                int limit = this.requestLimit();

                boolean isWithID = this.requestView();
                String package1, package2;
                if (action == 2){
                    if (isWithID){
                        Printer.printReferences(this.services.getReferences());
                    }
                    else    Printer.printPackageNames(this.services.getPackagesName());
                    package1 = this.requestPackage(1, isWithID);
                    package2 = this.requestPackage(2, isWithID);
                }
                else{
                    package1 = ServicesForControllers.NO_PACKAGE;
                    package2 = ServicesForControllers.NO_PACKAGE;
                }

                String info = this.services.getCyclesToShow(limit, isExactLimit, isWithID, MAX_NUMBER_OF_CYCLES_TO_SHOW, package1, package2);
                Printer.printCycles(info);

                Printer.printInformation(this.services.getInformationToShow(package1, package2));

                this.questionSave(limit, isExactLimit, isWithID, package1, package2);

                int cont = this.requestContinue();
                if (cont != 1)
                    continueSameFile = false;

                if (cont == 2)
                    continueOtherFile = true;
            }
        }
    }

    //AUXILIARY METHODS
    private int requestLimit() {
        Printer.printRequestLimit();
        Scanner reader = new Scanner(System.in);
        String in = reader.next();
        int entry = this.getValidInt(in, 3, Integer.MAX_VALUE);
        while (entry == INVALID_OPTION){
            Printer.printInvalidOption();
            in = reader.next();
            entry = this.getValidInt(in, 3, Integer.MAX_VALUE);
        }

        return entry;
    }
    private int requestContinue() {
        Printer.printRequestContinue();
        return this.requestOption(3);
    }
    private int requestOption(int max){
        Scanner reader = new Scanner(System.in);
        String in = reader.next();
        int option = this.getValidInt(in, 1, max);
        while (option == INVALID_OPTION){
            Printer.printInvalidOption();
            in = reader.next();
            option = this.getValidInt(in, 1, max);
        }

        return option;
    }
    private int getValidPackage(String in) {
        try {
            int option = Integer.parseInt(in);
            if (option >= 0 && option <= this.services.getNumberOfPackages())
                return option;
            return INVALID_OPTION;

        } catch (NumberFormatException e){
            return INVALID_OPTION;
        }
    }
    private int getValidInt(String entry, int min, int max){
        try {
            int option = Integer.parseInt(entry);
            if (option >= min && option <= max)     return option;
            return INVALID_OPTION;

        } catch (NumberFormatException e){
            return INVALID_OPTION;
        }
    }

    private void questionSave(int limit, boolean isExactLimit, boolean isWithID, String package1, String package2) {
        Printer.printQuestionSave();
        int option = this.requestOption( 2);
        if (option == 1){
            String path = this.requestFileToSave();
            this.services.saveAsTextFile(path, limit, isExactLimit, isWithID, MAX_NUMBER_OF_CYCLES_TO_SHOW, package1, package2);
        }
    }

    private boolean requestView() {
        Printer.printRequestIsWithID();
        return (this.requestOption(2) == 1);
    }
    private boolean requestTypeLimit() {
        Printer.printRequestTypeLimit();
        return (this.requestOption(2) == 1);
    }
    private boolean isValidFilePath(String path){
        String regularExpression = "(.*?)\\.(ODEM|odem)$";
        boolean isODEM = Pattern.matches(regularExpression, path);
        File f = new File(path);
        boolean exist = f.exists();
        return isODEM && exist;
    }
    private boolean isValidFileToSavePath(String path) {
        File f = new File(path);
        boolean exist = f.exists();
        boolean directory = f.isDirectory();

        return exist && directory;
    }

    private String requestFilePath(){
        Printer.printRequestFilePath();
        Scanner reader = new Scanner(System.in);
        String path = reader.nextLine();

        while (!isValidFilePath(path)){
            Printer.printInvalidInputPath();
            path = reader.nextLine();
        }

        return path;
    }
    private String requestFileToSave() {
        Printer.printRequestForSave();
        Scanner reader = new Scanner(System.in);
        String path = reader.nextLine();

        while (!isValidFileToSavePath(path)){
            Printer.printInvalidInputPathForSave();
            path = reader.next();
        }

        return path;
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
    private String requestPackage(int number, boolean isWithID) {
        Printer.printRequestPackage(number, isWithID);
        Scanner reader = new Scanner(System.in);
        String in = reader.nextLine();

        if (isWithID){
            int entry = this.getValidPackage(in);
            while (entry == INVALID_OPTION){
                Printer.printInvalidOption();
                in = reader.nextLine();
                entry = this.getValidPackage(in);
            }

            return String.valueOf(entry);
        }

        while (!this.services.existPackage(in)){
            Printer.printInvalidOption();
            in = reader.nextLine();
        }

        return in;
    }
}