package cycles;

import cycles.controllers.ConsoleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application {
    private static ConsoleController consoleController = new ConsoleController();

    public static void main(String[] args) {
        printHeader();
        String option = runExecutionOptions();

        switch (option){
            case "1":
                Main.consoleController.runConsole();
                break;

            case "2":
                launch(args);
                break;
        }
    }

    private static void printHeader(){
        System.out.println(
            "TP Final TALLER DE PROGRAMACIÓN JAVA 2019\n" +
            "\n" +
            "Jeremías Brisuela  jere05.mdq@gmail.com\n" +
            "Noelia Fluxá       noefluxa@gmail.com\n" +
            "\n" +
            "*****************************************\n");
    }

    private static String runExecutionOptions() {
        printOptions();
        String option = requestOption();

        while (!checkOptions(option)){
            System.out.println("Opción INVALIDA. Intente nuevamente.");
            option = requestOption();
        }

        return option;
    }

    private static void printOptions(){
        System.out.println(
                "Indique cómo desea ejecutar la aplicación:\n" +
                "   (1) Mediante CONSOLA\n" +
                "   (2) Mediante INTERFAZ GRÁFICA\n");
    }
    private static String requestOption(){
        boolean isSelected = false;
        Scanner reader = new Scanner(System.in);
        return  reader.next();
    }
    private static boolean checkOptions(String option){
        boolean isSelected = false;

        switch (option){
            case "1":
                isSelected = true;
                break;

            case "2":
                isSelected = true;
                break;

            default:
                break;
        }

        return isSelected;
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        stage.setTitle("TP FINAL - Taller de Programación Java");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

    }
}
