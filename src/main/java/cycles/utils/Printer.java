package cycles.utils;

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
                        "   (12)    * INGRESAR RUTA de un archivo ODEM\n"

        );
    }
    public static void printRequestNewFilePath(){
        System.out.println(
                "\n>> Ingrese la ruta (que no contenga espacios en blanco) del archivo ODEM:");
    }
}
