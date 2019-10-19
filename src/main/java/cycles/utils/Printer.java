package cycles.utils;

import java.util.EventListener;
import java.util.List;

public class Printer {
    public static void printActionsOptions() {
        System.out.println(
                "\n>> ¿Qué desea hacer?\n" +
                        "   (1) Visualizar TODAS las dependencias cíclicas de un sistema\n" +
                        "   (2) Visualizar las depedencias cíclicas ENTRE DOS PAQUETES\n"
        );
    }
    public static void printRequestFilePath(){
        System.out.println(
                "\n>> Indique de qué sistema quiere ver ciclos\n" +
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
                        "   (12)   * INGRESAR RUTA de un archivo ODEM\n"

        );
    }
    public static void printRequestNewFilePath(){
        System.out.println(
                "\n>> Ingrese la ruta del archivo ODEM:");
    }
    public static void printInvalidOption(){
        System.out.println("\n>> Opción incorrecta. Intente nuevamente");
    }
    public static void printInvalidInputPath(){
        System.out.println("\n>> Entrada incorrecta: el archivo no es '.odem' o es inexistente. Intente nuevamente");
    }

    public static void printRequestTypeLimit() {
        System.out.println(
                "\n>> ¿Con qué límite?\n" +
                        "   (1) Limite EXACTO\n" +
                        "   (2) Limite MÁXIMO\n"
        );
    }

    public static void printRequestIsWithID() {
        System.out.println(
                "\n>> ¿Cómo visualizar los ciclos?\n" +
                        "   (1) Por IDENTIFICADORES\n" +
                        "   (2) Por NOMBRE DE PAQUETE\n"
        );
    }

    public static void printReferences(String references) {
        System.out.println("\n>> Referencias:\n" + references);
    }


    public static void printPackageNames(List<String> packageName) {
        System.out.println("\n>> Nombre de paquetes:");
        for (String name : packageName){
            System.out.println("     "+name);
        }
    }

    public static void printRequestPackage(int number, boolean isWithID) {
        StringBuilder msg = new StringBuilder();
        msg.append("\n>> Ingrese el ");
        if (isWithID)   msg.append("identificador");
        else            msg.append("nombre");
        msg.append(" del paquete ");
        msg.append(String.valueOf(number));
        msg.append("\n");
        System.out.println(msg.toString());
    }

    public static void printCycles(String infoToShow) {
        System.out.println("\n********** CICLOS **********");
        System.out.println(infoToShow);
    }

    public static void printRequestLimit() {
        System.out.println("\n>> Ingresar el límite (>2)\n");
    }

    public static void printRequestForSave() {
        System.out.println("\n>> Ingresar ruta de CARPETA dónde guardar la información\n");
    }

    public static void printInvalidInputPathForSave() {
        System.out.println("\n>> Entrada incorrecta: no es una carpeta o es inexistente. Intente nuevamente");
    }

    public static void printInformation(String info){
        System.out.println("\n********** INFORMACIÓN **********");
        System.out.println(info);
    }

    public static void printQuestionSave() {
        System.out.println(
                "\n>> ¿Guardar resultados en archivo?\n" +
                        "   (1) Sí\n" +
                        "   (2) No\n"
        );
    }

    public static void printRequestContinue() {
        System.out.println(
                "\n>> ¿Cómo continuar?\n" +
                        "   (1) Seguir con el mismo archivo\n" +
                        "   (2) Cambiar de archivo\n" +
                        "   (3) Terminar\n"
        );
    }
}
