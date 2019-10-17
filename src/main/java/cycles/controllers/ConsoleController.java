package cycles.controllers;

import cycles.services.ServiceForControllers;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleController {
    private String path, pathForSave;
    private boolean isExactLimit, isWithID;
    private ServiceForControllers serviceForControllers;

    public ConsoleController() {
        this.path = null;
    }

    public void runConsole() {
        String action = this.requestAction();

        this.requestPath();
        if (path == null)
            path = this.requestNewPath();

        this.serviceForControllers = new ServiceForControllers(path);

        switch (action) {
            case "1":
                this.showCycles();
                break;

            case "2":
                this.printReferences(this.serviceForControllers.getReferences());
                long idPackage1 = this.requestPackageID("primer");
                long idPackage2 = this.requestPackageID("segundo");
                this.existCycle(idPackage1, idPackage2);
                break;
        }
    }

    // ACTIONS
    private void showCycles(){
        this.requestCyclesOption();
        int limit = this.requestLimit();
        System.out.println(this.serviceForControllers.getCyclesInformation(false, this.isWithID, true, this.isExactLimit, limit, -1, -1));

        if (!this.serviceForControllers.isCompleteInfo()){
            this.printIncompleteInfo();
            this.pathForSave = this.requestPathForSave();
            this.printSavedOk(this.serviceForControllers.saveCompleteInfo(this.pathForSave, true, this.isWithID, true, this.isExactLimit, limit, -1, -1));
        }

        else {
            String save = this.requestSaveInformation();
            if (save.equals("1")){
                this.printRequestPathForSave();
                this.pathForSave = this.requestPathForSave();
                this.printSavedOk(this.serviceForControllers.saveCompleteInfo(this.pathForSave, true, this.isWithID, true, this.isExactLimit, limit, -1, -1));
            }
        }
    }
    private void existCycle(long idPackage1, long idPackage2){
        this.requestCyclesOption();
        int limit = this.requestLimit();

        System.out.println(this.serviceForControllers.getCyclesInformation(false, isWithID, false, isExactLimit, limit, idPackage1, idPackage2));

        if (!this.serviceForControllers.isCompleteInfo()){
            this.pathForSave = this.requestPathForSave();
            this.printSavedOk(this.serviceForControllers.saveCompleteInfo(pathForSave, true, isWithID, false, isExactLimit, limit, idPackage1, idPackage2));
        }

        else {
            String save = this.requestSaveInformation();
            if (save.equals("1")){
                this.printRequestPathForSave();
                this.pathForSave = this.requestPathForSave();
                this.printSavedOk(this.serviceForControllers.saveCompleteInfo(pathForSave, true, isWithID, false, isExactLimit, limit, idPackage1, idPackage2));
            }
        }
    }

    // REQUEST FOR THE USER
    private int requestLimit(){
        this.printRequestLimit();
        int limit = this.requestIn_in();

        while (!checkLimit(limit)){
            printInvalidLimit();
            limit = this.requestIn_in();
        }

        return limit;
    }
    private void requestPath() {
        this.printRequestPath();
        String option = requestIn_string();

        while (!checkOptionPath(option)) {
            this.printInvalidOption();
            option = requestIn_string();
        }
    }
    private String requestNewPath(){
        this.printRequestNewPath();
        String path = requestIn_string();

        while (!checkNewPath(path)){
            this.printInvalidPath(path);
            path = requestIn_string();
        }

        return path;
    }
    private String requestAction() {
        this.printActionsOptions();
        String option = requestIn_string();

        while (!(option.equals("1") || option.equals("2"))) {
            this.printInvalidOption();
            option = requestIn_string();
        }

        return option;
    }
    private void requestCyclesOption(){
        this.printCyclesOption();
        String option = requestIn_string();

        while (!checkOptionCycle(option)) {
            this.printInvalidOption();
            option = requestIn_string();
        }
    }
    private String requestPathForSave(){
        this.printRequestPathForSave();
        String option = requestIn_string();

        while (!(this.checkPathForSave(option))) {
            this.printInvalidPathForSave();
            option = requestIn_string();
        }

        return option;
    }
    private String requestSaveInformation(){
        this.printOptionForSave();
        String option = this.requestIn_string();

        while (!(option.equals("1") || option.equals("2"))){
            this.printInvalidOption();
            option = this.requestIn_string();
        }

        return option;
    }
    private long requestPackageID(String order){
        this.printRequestPackageID(order);
        int id = this.requestIn_in();

        while (id < 0 || id > this.serviceForControllers.getNumberOfPackages()){
            printInvalidLimit();
            id = this.requestIn_in();
        }

        return (long) id;
    }

    // REQUEST IN
    private int requestIn_in(){
        Scanner reader = new Scanner(System.in);
        int in = 0;
        try{
            in =  reader.nextInt();
        } catch (InputMismatchException ime){
            System.out.println("Entrada Invalida");
        }

        return in;
    }
    private String requestIn_string() {
        Scanner reader = new Scanner(System.in);
        return reader.next();
    }

    // CHECKS
    private boolean checkLimit(int limit) {
        boolean correct = false;

        if (limit >= 3 && limit < Integer.MAX_VALUE)
            correct = true;

        return correct;
    }
    private boolean checkNewPath(String path){
        String regularExpression = "(.*?)\\.(ODEM|odem)$";
        boolean isODEM = Pattern.matches(regularExpression, path);
        File f = new File(path);
        boolean exist = f.exists();
        return isODEM && exist;
    }
    private boolean checkPathForSave(String path){
        File directory = new File(path);
        return directory.exists() && directory.isDirectory();
    }
    private boolean checkOptionPath(String option){
        boolean isSelected = false;

        switch (option){
            case "0":
                isSelected = true;
                this.path = null;
                break;

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

            default:
                this.path = null;
                break;
        }

        return isSelected;
    }
    private boolean checkOptionCycle(String option){
        boolean isSelected = false;
        switch (option){
            case "1":
                this.isExactLimit = true;
                this.isWithID = true;
                isSelected = true;
                break;

            case "2":
                this.isWithID = false;
                this.isExactLimit = true;
                isSelected = true;
                break;

            case "3":
                this.isWithID = true;
                this.isExactLimit = false;
                isSelected = true;
                break;

            case "4":
                this.isWithID = false;
                this.isExactLimit = false;
                isSelected = true;
                break;
        }

        return isSelected;
    }

    // PRINTS
    private void printCyclesInfo(){
        System.out.println("INFO");
        System.out.println("    Tiempo de Tarjan: " + this.serviceForControllers.getTarjanTime()+ " ms");
        System.out.println("    Cant. de ciclos: "  + this.serviceForControllers.getNumberOfCycles());
        System.out.println("    Cant. de paquetes: "    + this.serviceForControllers.getNumberOfPackages());
        System.out.println("    Cant. de relaciones entre paquetes: "   + this.serviceForControllers.getNumberOfEdges());
    }
    private void printRequestPath(){
        System.out.println(
                        "\nIndique de qué sistema quiere ver ciclos\n" +
                        "   (0)    INGRESAR RUTA de un archivo ODEM\n"+
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
                        "   (11)   hibernate-core-4.2.0.Final.odem\n"
        );
    }
    private void printCyclesOption(){
        System.out.println(
                "\nVer ciclos con ...\n" +
                        "   (1)    ... límite exacto    [mostrando IDENTIFICADOR]\n" +
                        "   (2)    ... limite exacto    [mostrando NOMBRE]\n" +
                        "   (3)    ... limite máximo    [mostrando IDENTIFICADOR]\n" +
                        "   (4)    ... limite máximo    [mostrando NOMBRE]\n"
        );
    }
    private void printOptionForSave(){
        System.out.println("\n¿Quiere guardar los resultados?");
        System.out.println("    (1) SÍ");
        System.out.println("    (2) NO");
    }
    private void printRequestLimit() {
        System.out.println("Ingrese el límite (tiene que ser > 2):");
    }
    private void printRequestNewPath(){
        System.out.println(
                "\nIngrese la ruta (que no contenga espacios en blanco) del archivo ODEM");
    }
    private void printIncompleteInfo(){
        System.out.println("\nLa cantidad de ciclos exedió el límite que se puede mostrar.");
    }
    private void printActionsOptions() {
        System.out.println(
                "\n¿Qué desea hacer?\n" +
                        "   (1) Visualizar dependencias cíclicas de un sistema\n" +
                        "   (2) Comprobar si un par de paquetes de un sistema pertenecen a un mismo ciclo de dependencias\n"
        );
    }
    private void printInvalidPathForSave(){
        System.out.println("La ruta ingresada NO corresponde a un directorio. Intente nuevamente.");
    }
    private void printRequestPathForSave(){
        System.out.println("Ingrese la ruta de un DIRECTORIO dónde guardar la información.");
    }
    private void printInvalidPath(String path){
        System.out.println("La ruta ingresada \""+ path +"\" es INVALIDA. Intente nuevamente.");
    }
    private void printSavedOk(boolean isSavedOk){
        if (isSavedOk)
            System.out.println("\nInformación guardada correctamente");
        else
            System.out.println("No se pudo guardar la información.");
    }
    private void printReferences(String references){
        System.out.println(
                "A continuación se mostrará cada paquete son su identificador.\n" +
                "Luego se solicitará que ingrese el identificador de los dos paquetes a chequear si pertenecen a un mismo ciclo de dependencias."
        );
        System.out.println("REFERENCIAS");
        System.out.println(references);
    }
    private void printRequestPackageID(String order){
        System.out.println("Ingrese el ID del " + order + " paquete: ");
    }
    private void printInvalidOption(){
        System.out.println("Opción INVALIDA. Intente nuevamente.");
    }
    private void printInvalidLimit(){
        System.out.println("Límite INVALIDO. Intente nuevamente.");
    }
}