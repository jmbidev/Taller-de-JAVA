package cycles.utils;

import java.util.List;

public class Printer {
    public static void printActionsOptions() {
        System.out.println(
                "\n>> ¿Qué quiere visualizar?\n" +
                        "   (1) TODAS las dependencias cíclicas de un sistema\n" +
                        "   (2) Las dependencias cíclicas ENTRE DOS PAQUETES de un sistema"
        );
    }
    public static void printRequestFilePath(){
        System.out.println(
                "\n>> Ingrese la ruta de un archivo ODEM"
        );
    }
    public static void printInvalidOption(){
        System.out.println(">> ¡ERROR! Opción incorrecta. INTENTE NUEVAMENTE");
    }
    public static void printInvalidInputPath(){
        System.out.println(">> ¡ERROR! El archivo no es '.odem' o es inexistente.INTENTE NUEVAMENTE");
    }
    public static void printRequestTypeLimit() {
        System.out.println(
                "\n>> ¿Qué tipo de límite es el ingresado?\n" +
                        "   (1) Límite EXACTO\n" +
                        "   (2) Límite MÁXIMO"
        );
    }
    public static void printRequestIsWithID() {
        System.out.println(
                "\n>> ¿Cómo visualizar las dependencias cíclicas del sistema?\n" +
                        "   (1) Por IDENTIFICADORES\n" +
                        "   (2) Por NOMBRE DE PAQUETES"
        );
    }
    public static void printReferences(String subtitle, String references) {
        System.out.println("\n\n>> "+subtitle+":\n" + references);
    }

    public static void printReferences(String references) {
        System.out.println(references);
    }


    public static void printPackageNames(List<String> packageName) {
        System.out.println("\n>> Nombre de paquetes:");
        for (String name : packageName){
            System.out.println("   "+name);
        }
    }
    public static void printRequestPackage(int number, boolean isWithID) {
        StringBuilder msg = new StringBuilder();
        msg.append("\n>> Ingrese el ");
        if (isWithID)   msg.append("identificador");
        else            msg.append("nombre");
        msg.append(" del paquete ");
        msg.append(String.valueOf(number));
        System.out.println(msg.toString());
    }
    public static void printCycles(String infoToShow) {
        System.out.print(infoToShow);
    }
    public static void printRequestLimit() {
        System.out.println("\n>> Ingrese el límite de paquetes en cada ciclo (debe ser >2)");
    }
    public static void printRequestForSave() {
        System.out.println("\n>> Ingresar ruta de CARPETA dónde guardar la información");
    }
    public static void printInvalidInputPathForSave() {
        System.out.println(">>¡ERORR! No es una carpeta o es inexistente. INTENTE NUEVAMENTE");
    }
    public static void printInformation(String info){
        System.out.println(info);
    }
    public static void printQuestionSave() {
        System.out.println(
                "\n>> ¿Guardar resultados en archivo?\n" +
                        "   (1) Sí\n" +
                        "   (2) No"
        );
    }
    public static void printRequestContinue() {
        System.out.println(
                "\n>> ¿Cómo continuar?\n" +
                        "   (1) Seguir con el mismo archivo\n" +
                        "   (2) Cambiar de archivo\n" +
                        "   (3) Terminar"
        );
    }
    public static void printHeader() {
        StringBuilder header = new StringBuilder();
        header.append("------------------------------------------\n");
        header.append("TP Final TALLER DE PROGRAMACIÓN JAVA 2019\n\n");
        header.append("Jeremías Brisuela  jere05.mdq@gmail.com\n");
        header.append("Noelia Fluxá       noefluxa@gmail.com\n");
        header.append("------------------------------------------\n");
        System.out.print(header);
    }

    public static void printTitle(String title) {
        System.out.println("\n********** "+title.toUpperCase()+" **********");
    }

    public static void waitingAnswer() {
        System.out.print(">> ");
    }

    public static void printInvalidLimit() {
        System.out.println(">> ¡ERROR! El límite ingresado es incorrecto.INTENTE NUEVAMENTE");
    }
}
