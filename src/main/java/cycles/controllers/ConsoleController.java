package cycles.controllers;

import cycles.services.ServiceForControllers;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleController {
    private String path, typeLimit, asShown;
    private boolean isExactLimit, isWithID;
    private ServiceForControllers serviceForControllers;

    public ConsoleController(){
        this.path = null;
    }

    public void runConsole(){
        this.requestPath();
        this.showCycles();
    }

    private void requestPath(){
        printRequestPath();
        String option = requestIn_string();

        while (!checkOptionPath(option)){
            System.out.println("Opción INVALIDA. Intente nuevamente.");
            option = requestIn_string();
        }

        if (option.equals("*") && this.path == null){
            System.out.println("Ingrese la ruta del archivo ODEM:");
            String auxPath = requestIn_string();

            while (!checkPath(auxPath)){
                System.out.println("\nArchivo ingresado no es ODEM. Intente nuevamente");
                auxPath = requestIn_string();
            }

            this.path = auxPath;
        }

        if (path != null)
            this.serviceForControllers = new ServiceForControllers(path);
    }

    private void showCycles(){
        printCycleOptions();
        String option = requestIn_string();

        while (!checkOptionShowCycle(option)){
            System.out.println("Opción INVALIDA. Intente nuevamente.");
            option = requestIn_string();
        }

        System.out.println("Ingrese el límite "+typeLimit+" (>2)");
        int limit = requestIn_in();

        while (!checkLimit(limit)){
            System.out.println("Límite INVALIDO. Intente nuevamente.");
            limit = requestIn_in();
        }

        String cycles = this.serviceForControllers.getCycles(limit, isExactLimit, isWithID);
        System.out.println(cycles);
    }

    // Cycles
    private void printCycleOptions(){
        System.out.println(
                "\nVer ciclos con ...\n" +
                "   (1)    ... límite exacto    [mostrando IDENTIFICADOR]\n" +
                "   (2)    ... limite exacto    [mostrando NOMBRE]\n" +
                "   (3)    ... limite máximo    [mostrando IDENTIFICADOR]\n" +
                "   (4)    ... limite máximo    [mostrando NOMBRE]\n"
        );
    }
    private boolean checkOptionShowCycle(String option){
        boolean isSelected = false;
        switch (option){
            case "1":
                this.typeLimit = "exacto";
                this.asShown = "identificador";
                this.isExactLimit = true;
                this.isWithID = true;
                isSelected = true;
                break;

            case "2":
                this.typeLimit = "exacto";
                this.asShown = "nombre";
                this.isWithID = false;
                this.isExactLimit = true;
                isSelected = true;
                break;

            case "3":
                this.typeLimit = "máximo";
                this.asShown = "identificador";
                this.isWithID = true;
                this.isExactLimit = false;
                isSelected = true;
                break;

            case "4":
                this.typeLimit = "máximo";
                this.asShown = "nombre";
                this.isWithID = false;
                this.isExactLimit = false;
                isSelected = true;
                break;
        }

        return isSelected;
    }
    private boolean checkLimit(int limit) {
        boolean correct = false;

        if (limit >= 3 && limit <= Integer.MAX_VALUE)
            correct = true;

        return correct;
    }

    // Path
    private void printRequestPath(){
        System.out.println(
                "\n VER CICLOS. Indique de qué archivo ODEM:\n"+
                "   (1)    apache-camel-1.6.0.odem\n" +
                "   (2)    apache-camel-1.6.1.odem\n" +
                "   (3)    apache-camel-1.6.2.odem\n" +
                "   (4)    apache-camel-2.0.0.odem\n" +
                "   (5)    apache-cxf-2.0.6.odem\n" +
                "   (6)    apache-cxf-2.1.1.odem\n" +
                "   (7)    db-derby-10.8.1.2.odem\n" +
                "   (8)    db-derby-10.9.1.0.odem\n" +
                "   (9)    hibernate-core-4.0.0.Final.odem\n" +
                "   (10)   hibernate-core-4.1.0.Final.odem\n" +
                "   (11)   hibernate-core-4.2.0.Final.odem\n" +
                "   (*)    Ingresar OTRO archivo ODEM\n"
        );
    }
    private boolean checkOptionPath(String option){
        boolean isSelected = false;

        switch (option){
            case "1":
                this.path = "src/main/resources/filesODEM/apache-camel-1.6.0.odem";
                isSelected = true;
                break;

            case "2":
                this.path = "src/main/resources/filesODEM/apache-camel-1.6.1.odem";
                isSelected = true;
                break;

            case "3":
                this.path = "src/main/resources/filesODEM/apache-camel-1.6.2.odem";
                isSelected = true;
                break;

            case "4":
                this.path = "src/main/resources/filesODEM/apache-camel-2.0.0.odem";
                isSelected = true;
                break;

            case "5":
                this.path = "src/main/resources/filesODEM/apache-cxf-2.0.6.odem";
                isSelected = true;
                break;

            case "6":
                this.path = "src/main/resources/filesODEM/apache-cxf-2.1.1.odem";
                isSelected = true;
                break;

            case "7":
                this.path = "src/main/resources/filesODEM/db-derby-10.8.1.2.odem";
                isSelected = true;
                break;

            case "8":
                this.path = "src/main/resources/filesODEM/db-derby-10.9.1.0.odem";
                isSelected = true;
                break;

            case "9":
                this.path = "src/main/resources/filesODEM/hibernate-core-4.0.0.Final.odem";
                isSelected = true;
                break;

            case "10":
                this.path = "src/main/resources/filesODEM/hibernate-core-4.1.0.Final.odem";
                isSelected = true;
                break;

            case "11":
                this.path = "src/main/resources/filesODEM/hibernate-core-4.2.0.Final.odem";
                isSelected = true;
                break;

            case "*":
                isSelected = true;
                this.path = null;
                break;

            default:
                this.path = null;
                break;
        }

        return isSelected;
    }
    private boolean checkPath(String path){
        String regularExpression = "(.*?)\\.(ODEM|odem)$";
        return Pattern.matches(regularExpression, path);
    }

    private String requestIn_string(){
        Scanner reader = new Scanner(System.in);
        return  reader.next();
    }
    private int requestIn_in(){
        Scanner reader = new Scanner(System.in);
        int in = 0;
        try{
            in =  reader.nextInt();
        } catch (InputMismatchException ime){
            System.out.println("SÓLO SE PUEDEN INGRESAR ENTEROS");
        }

        return in;
    }
}