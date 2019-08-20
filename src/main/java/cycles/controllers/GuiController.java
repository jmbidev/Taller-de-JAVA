package cycles.controllers;

import cycles.algorithm.Vertex;

import cycles.services.ServiceForControllers;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.fxml.FXML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.*;


public class GuiController implements Initializable {

    private static final int NO_LIMIT = -1;
    private static final int LIM_MIN = 3;

    @FXML private Button button_searchFile;
    @FXML private Button button_saveCycles;
    @FXML private Button button_seeCycles;
    @FXML private Button button_check;
    @FXML private Button button_clearCycleTwoPackages;
    @FXML private Button button_saveCyclesWithPackages;
    @FXML private Button button_reset;
    @FXML private Button button_resetCycles;

    @FXML private ComboBox<String> comboBox_package1;
    @FXML private ComboBox<String> comboBox_package2;

    @FXML private Label label_selectInvalid;

    @FXML private RadioButton radioButton_maxLimit;
    @FXML private RadioButton radioButton_exactLimit;
    @FXML private RadioButton radioButton_seeID;
    @FXML private RadioButton radioButton_seeName;

    @FXML private Spinner<Integer> spinner_maxLimit;
    @FXML private Spinner<Integer> spinner_exactLimit;

    @FXML private TextArea textArea_answerCycles;
    @FXML private TextArea textArea_answerCyclesTwoPackages;
    @FXML private TextArea textArea_reference;

    @FXML private TextField textField_path;
    @FXML private TextField count_nodes;
    @FXML private TextField count_edged;
    @FXML private TextField count_cycles;
    @FXML private TextField time_tarjan;
    @FXML private TextField count_cyclesWithPackages;

    @FXML private Tooltip tooltip_check;

    private SpinnerValueFactory<Integer> exactValueFactory;
    private SpinnerValueFactory<Integer> maxValueFactory;

   // private ServiceForControllers sfc;
    private Window stage;
    private boolean manyCycles = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.exactValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, Integer.MAX_VALUE, 3);
        this.maxValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, Integer.MAX_VALUE, 3);
        this.spinner_maxLimit.setValueFactory(maxValueFactory);
        this.spinner_maxLimit.setEditable(false);
        this.spinner_exactLimit.setValueFactory(exactValueFactory);
        this.spinner_exactLimit.setEditable(false);

        this.textArea_answerCycles.setEditable(false);
        this.textArea_answerCyclesTwoPackages.setEditable(false);
        this.textArea_reference.setEditable(false);

        this.tooltip_check.setText("Se tienen en cuenta los ciclos con el límite establecido anteriormente");
        this.panelInitial();
    }

    private void panelInitial() {
       // this.sfc = new ServiceForControllers();
        this.clearTextArea();
        this.button_searchFile.setDisable(false);
        this.buttons(true);
        this.radioButtons(true);
        this.label_selectInvalid.setVisible(false);
        this.radioButton_seeID.setSelected(true);
        this.radioButton_seeName.setSelected(false);
        this.radioButton_exactLimit.setSelected(true);
        this.radioButton_maxLimit.setSelected(false);
        this.comboBox_package1.setValue(null);
        this.comboBox_package2.setValue(null);
        this.comboBox_package1.setDisable(true);
        this.comboBox_package2.setDisable(true);
        this.textField_path.setText("");
    }

    private void clearTextArea() {
        this.textArea_answerCyclesTwoPackages.setText("");
        this.textArea_answerCycles.setText("");
        this.textArea_reference.setText("");
        this.count_edged.setText("");
        this.count_nodes.setText("");
        this.count_cycles.setText("");
        this.time_tarjan.setText("");
        this.count_cyclesWithPackages.setText("");
    }

    private void buttons(boolean isDisable){
        this.button_seeCycles.setDisable(isDisable);
        this.button_check.setDisable(isDisable);
        this.button_saveCycles.setDisable(isDisable);
        this.button_saveCyclesWithPackages.setDisable(isDisable);
        this.button_clearCycleTwoPackages.setDisable(isDisable);
        this.button_resetCycles.setDisable(isDisable);
        this.button_reset.setDisable(isDisable);
    }

    private void radioButtons(boolean isDisable){
        this.radioButton_maxLimit.setDisable(isDisable);
        this.radioButton_exactLimit.setDisable(isDisable);
        this.radioButton_seeID.setDisable(isDisable);
        this.radioButton_seeName.setDisable(isDisable);
    }

    @FXML
    public void searchFile(MouseEvent event){
        /*FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Archivo");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Odem File", "*.odem"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null){
            String path = selectedFile.getPath();
            //this.app.setPath(path);
            //this.app.runSAX();
            this.textField_path.setText(path);
            this.button_searchFile.setDisable(true);
            this.button_seeCycles.setDisable(false);
            this.button_reset.setDisable(false);
            this.spinner_exactLimit.setDisable(false);
            this.radioButtons(false);
        }

         */
    }

    @FXML
    public void seeCycles(MouseEvent event){
        /*int limit = NO_LIMIT;
        boolean isExactLimit = false;

        if (this.radioButton_maxLimit.isSelected()){
            isExactLimit = false;
            limit = this.spinner_maxLimit.getValue();
        }

        if (this.radioButton_exactLimit.isSelected()){
            isExactLimit = true;
            limit = this.spinner_exactLimit.getValue();
        }

        //this.app.runTarjan(limit, isExactLimit);
        //List<List<Vertex<String>>> cycles = this.app.getCycles();
        StringBuilder answer = new StringBuilder();

        this.count_nodes.setText(String.valueOf(this.app.getGraph().getVertexes().size()));
        this.count_edged.setText(String.valueOf(this.app.getGraph().getEdges().size()));
        this.count_cycles.setText(String.valueOf(cycles.size()));
        this.time_tarjan.setText(String.valueOf(this.app.getTarjanTime()));

        //if (cycles.size() == 0){
            answer.append("No hay ciclos de dependencia entre los paquetes");

        } else if (cycles.size() < 300000){
            answer.append("Se detectaron los siguientes ciclos de dependencias entre paquetes: \n\n");
            answer.append(this.showCycles(cycles));
        } else {
            answer.append("Muchos ciclos para mostrar. Guardar en archivo");
            this.manyCycles = true;
            this.button_saveCycles.setDisable(false);
        }


        this.textArea_answerCycles.setText(answer.toString());

        if (this.radioButton_seeID.isSelected()){
            this.showReferences();
        } else {
            this.textArea_reference.setText("");
        }

        this.buildOptionsPackages();

        this.radioButtons(true);
        this.button_seeCycles.setDisable(true);
        this.spinner_exactLimit.setDisable(true);
        this.spinner_maxLimit.setDisable(true);

        this.button_resetCycles.setDisable(false);
        this.button_saveCycles.setDisable(false);
        this.button_check.setDisable(false);
        this.comboBox_package1.setDisable(false);
        this.comboBox_package2.setDisable(false);
*/
    }

    private String showCycles(List<List<Vertex<String>>> cycles){
        /*StringBuilder show = new StringBuilder();
        int i;

        if (this.radioButton_seeID.isSelected()) {

            for (List<Vertex<String>> cycle : cycles){
                i = cycle.size();

                for (Vertex<String> vertex : cycle){
                    i--;
                    if (i == 0) show.append(String.valueOf(vertex.getId()));
                    else        show.append(String.valueOf(vertex.getId())+" → ");
                }
                show.append("\n");
            }
        } else {
            for (List<Vertex<String>> cycle : cycles){
                i = cycle.size();

                for (Vertex<String> vertex : cycle){
                    i--;
                    if (i == 0) show.append(vertex.getData());
                    else        show.append(vertex.getData()+" → ");
                }
                show.append("\n");
            }
        }
        return show.toString();
*/
        return new String();
    }

    @FXML
    private void showReferences() {
        /*
        StringBuilder answer = new StringBuilder();
        Collection<Vertex<String>> vertexes = this.app.getGraph().getVertexes();
        for (Vertex<String> vertex : vertexes){
            answer.append(vertex.getId() + " --> " + vertex.getData() + "\n");
        }
        this.textArea_reference.setText(answer.toString());

         */
    }

    @FXML
    public void saveCycles(MouseEvent event){
        /*
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Directorio");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null){

            if (manyCycles){
                this.saveManyCycles(selectedDirectory.getPath());
            } else {
                this.app.saveAnswerCycles(selectedDirectory.getPath(), this.textArea_answerCycles.getText());
                this.button_saveCycles.setDisable(true);
            }

        }
        this.button_saveCycles.setDisable(true);

         */
    }

    private void saveManyCycles(String path){
        /*
        StringBuilder pathDestination = new StringBuilder(path);
        pathDestination.append("/answerCycles.txt");
        try {
            File file = new File(pathDestination.toString());
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            List<List<Vertex<String>>> cycles = this.app.getCycles();

            if (this.radioButton_seeID.isSelected()) {
                for (List<Vertex<String>> cycle : cycles){
                    int i = cycle.size();

                    for (Vertex<String> vertex : cycle){
                        i--;
                        if (i == 0) bw.write(String.valueOf(vertex.getId()));
                        else        bw.write(String.valueOf(vertex.getId())+" → ");
                    }
                    bw.write("\n");
                }
            } else {
                for (List<Vertex<String>> cycle : cycles){
                    int i = cycle.size();

                    for (Vertex<String> vertex : cycle){
                        i--;
                        if (i == 0) bw.write(vertex.getData());
                        else        bw.write(vertex.getData()+" → ");
                    }
                    bw.write("\n");
                }
            }

            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void buildOptionsPackages() {
/*
        this.comboBox_package1.getItems().clear();
        this.comboBox_package2.getItems().clear();

        if (this.radioButton_seeID.isSelected()){
            Set<Long> ids = this.app.getGraph().getIDs();
            for (Long id : ids){
                this.comboBox_package1.getItems().add(String.valueOf(id));
                this.comboBox_package2.getItems().add(String.valueOf(id));
            }
        } else {
            for (String option : this.app.getPackages()) {
                this.comboBox_package1.getItems().add(option);
                this.comboBox_package2.getItems().add(option);
            }
        }*/
    }


    @FXML
    public void check(MouseEvent event) {
        /*this.label_selectInvalid.setVisible(false);

        String package1 = this.comboBox_package1.getValue();
        String package2 = this.comboBox_package2.getValue();

        if (package1 == null && package2 == null){
            this.label_selectInvalid.setText("¡DEBE SELECCIONAR LOS PAQUETES!");
            this.label_selectInvalid.setVisible(true);
        } else if (package1 == null) {
            this.label_selectInvalid.setText("¡SELECCIONE EL PAQUETE #1!");
            this.label_selectInvalid.setVisible(true);
        } else if (package2 == null){
            this.label_selectInvalid.setText("¡SELECCIONE EL PAQUETE #2!");
            this.label_selectInvalid.setVisible(true);
        }

        else {
            List<List<Vertex<String>>> cycles = this.existCycle();
            StringBuilder text = new StringBuilder(this.textArea_answerCyclesTwoPackages.getText());

            text.append( "Paquete #1: " + package1 + "\n" + "Paquete #2: " + package2 + "\n" + "\n");

            if (cycles.size() == 0) {
                text.append("No existen ciclos de dependencia entre los paquetes seleccionados.");

            } else text.append(this.showCycles(cycles));

            text.append("\n\n");
            text.append("-----------------------------------------------------");
            text.append("\n\n");

            this.textArea_answerCyclesTwoPackages.setText(text.toString());
            this.count_cyclesWithPackages.setText(String.valueOf(cycles.size()));

            this.button_saveCyclesWithPackages.setDisable(false);
            this.button_clearCycleTwoPackages.setDisable(false);
        }

         */
    }

    private List<List<Vertex<String>>> existCycle(){
        /*List<List<Vertex<String>>> cycles = this.app.getCycles();
        List<List<Vertex<String>>> cyclesWithPackages = new ArrayList<>();

        if (radioButton_seeID.isSelected()){
            for (List<Vertex<String>> cycle : cycles){
                boolean isPackage1 = false;
                boolean isPackage2 = false;

                for (Vertex<String> vertex : cycle){
                    if (vertex.getId() == Integer.parseInt(this.comboBox_package1.getValue())) isPackage1 = true;
                    if (vertex.getId() == Integer.parseInt(this.comboBox_package2.getValue())) isPackage2 = true;

                    if (isPackage1 && isPackage2){
                        cyclesWithPackages.add(cycle);
                        break;
                    }
                }
            }
        }

        else {
            for (List<Vertex<String>> cycle : cycles){
                boolean isPackage1 = false;
                boolean isPackage2 = false;

                for (Vertex<String> vertex : cycle){

                    if (vertex.getData().equals(this.comboBox_package1.getValue())) isPackage1 = true;
                    if (vertex.getData().equals(this.comboBox_package2.getValue())) isPackage2 = true;

                    if (isPackage1 && isPackage2){
                        cyclesWithPackages.add(cycle);
                    }
                }


            }
        }
*/
        return new ArrayList<>();
    }

    @FXML
    public void clearCycleTwoPackages(){
        this.textArea_answerCyclesTwoPackages.setText("");
        this.button_saveCyclesWithPackages.setDisable(true);
        this.button_clearCycleTwoPackages.setDisable(true);
        this.count_cyclesWithPackages.setText("");
    }

    @FXML
    public void saveCyclesWithPackages(MouseEvent event){/*
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Directorio");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null){
            this.app.saveAnswerTwoPackages(selectedDirectory.getPath(), this.textArea_answerCyclesTwoPackages.getText());
            this.button_saveCyclesWithPackages.setDisable(true);
        }*/
    }

    @FXML
    public void restart(MouseEvent event){
        this.panelInitial();
    }

    @FXML
    public void resetCycles(){
        this.clearTextArea();
        this.buttons(true);
        this.button_seeCycles.setDisable(false);
        this.radioButtons(false);
        this.button_resetCycles.setDisable(false);
        this.label_selectInvalid.setVisible(false);
        this.comboBox_package1.setDisable(true);
        this.comboBox_package2.setDisable(true);
        this.button_reset.setDisable(false);

        this.radioButton_seeID.setSelected(true);
        this.radioButton_seeName.setSelected(false);
        this.radioButton_exactLimit.setSelected(true);
        this.radioButton_maxLimit.setSelected(false);
        this.spinner_exactLimit.setDisable(false);
        this.spinner_maxLimit.setDisable(true);
    }

    // Radio Button

    @FXML
    public void cycleExactLimit(){
        this.radioButton_maxLimit.setSelected(false);
        this.radioButton_exactLimit.setSelected(true);

        this.spinner_maxLimit.setDisable(true);
        this.spinner_exactLimit.setDisable(false);
    }

    @FXML
    public void cycleMaxLimit(){
        this.radioButton_maxLimit.setSelected(true);
        this.radioButton_exactLimit.setSelected(false);

        this.spinner_maxLimit.setDisable(false);
        this.spinner_exactLimit.setDisable(true);
    }

    @FXML
    public void seeForID(){
        this.radioButton_seeID.setSelected(true);
        this.radioButton_seeName.setSelected(false);
    }

    @FXML
    public void seeForName(){
        this.radioButton_seeID.setSelected(false);
        this.radioButton_seeName.setSelected(true);
    }
}
