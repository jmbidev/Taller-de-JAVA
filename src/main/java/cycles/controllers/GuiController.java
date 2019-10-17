package cycles.controllers;

import cycles.services.ServicesForGuiController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int MAX_NUMBER_OF_CYCLES_TO_SHOW = 150;
    private ServicesForGuiController services;
    private Window stage;

    // ********** Main **********
    @FXML    private Button resetTool;
    @FXML    private Button searchFile;
    @FXML    private TextField path;
    @FXML    private Pane pane1;
    @FXML    private Pane pane2;

    private void initializeMain(){
        this.path.setText("");
        this.path.setPromptText("Path");

        this.pane2.setDisable(true);
        this.pane1.setDisable(false);
        this.resetTool.setDisable(true);
        this.searchFile.setDisable(false);

        this.dependenciesAmount.setText("");
        this.packagesAmount.setText("");
    }
    private void resetTool(){
        this.initializeMain();
        this.initializeConfiguration();
        this.initializeResults();
    }

    @Override   public void initialize(URL location, ResourceBundle resources) {
        this.resetTool();
    }
    @FXML       public void clickSearchPath(MouseEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Archivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Odem File", "*.odem"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null){
            String path = selectedFile.getPath();
            this.services = new ServicesForGuiController(path);

            this.path.setText(path);
            this.path.setDisable(true);
            this.resetTool.setDisable(false);
            this.pane2.setDisable(false);
            this.references.setText(this.services.getReferences());
            this.packagesAmount.setText(String.valueOf(this.services.getNumberOfPackages()));
            this.dependenciesAmount.setText(String.valueOf(this.services.getNumberOfDependencies()));
        }
    }
    @FXML       public void clickResetTool(MouseEvent event){
        this.resetTool();
    }


    // ********** Configuration **********

    @FXML   private Pane config;

    @FXML    private Button showCycles;
    @FXML    private Button saveCycles;
    @FXML    private Button resetCycles;

    @FXML    private Spinner<Integer> limit;
    @FXML    private RadioButton exactLimit;
    @FXML    private RadioButton maxLimit;

    @FXML    private RadioButton showToID;
    @FXML    private RadioButton showToName;

    @FXML    private RadioButton allCycles;
    @FXML    private RadioButton particularCycles;
    @FXML    private ComboBox<String> package1;
    @FXML    private ComboBox<String> package2;

    @FXML    private Label process;

    private void initializeConfiguration(){
        this.config.setDisable(false);
        this.showCycles.setDisable(false);
        this.saveCycles.setDisable(true);
        this.resetCycles.setDisable(true);

        this.exactLimit.setSelected(true);
        this.maxLimit.setSelected(false);

        this.showToID.setSelected(true);
        this.showToName.setSelected(false);

        this.allCycles.setSelected(true);
        this.particularCycles.setSelected(false);

        this.package1.setValue(null);
        this.package2.setValue(null);
        this.package1.setDisable(true);
        this.package2.setDisable(true);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, Integer.MAX_VALUE, 3);
        limit.setValueFactory(valueFactory);
    }
    private void buildOptionsPackages(){
        this.package1.getItems().clear();
        this.package2.getItems().clear();

        for (String option : this.services.getPackagesOptions(this.showToID.isSelected())){
            this.package1.getItems().add(option);
            this.package2.getItems().add(option);
        }
    }

    @FXML   public void clickExactLimit(MouseEvent event){
        this.exactLimit.setSelected(true);
        this.maxLimit.setSelected(false);
    }
    @FXML   public void clickMaxLimit(MouseEvent event){
        this.maxLimit.setSelected(true);
        this.exactLimit.setSelected(false);
    }
    @FXML   public void clickShowToID(MouseEvent event){
        this.showToID.setSelected(true);
        this.showToName.setSelected(false);
        this.buildOptionsPackages();
        this.references.setText(this.services.getReferences());
    }
    @FXML   public void clickShowToName(MouseEvent event){
        this.showToName.setSelected(true);
        this.showToID.setSelected(false);
        this.buildOptionsPackages();
        this.references.setText("");
    }
    @FXML   public void clickAllCycles(MouseEvent event){
        this.allCycles.setSelected(true);
        this.particularCycles.setSelected(false);

        this.package1.setDisable(true);
        this.package2.setDisable(true);
    }
    @FXML   public void clickParticularCycles(MouseEvent event) {
        this.particularCycles.setSelected(true);
        this.allCycles.setSelected(false);

        this.package1.setDisable(false);
        this.package2.setDisable(false);

        this.buildOptionsPackages();
    }
    @FXML   public void clickResetShowCycles(MouseEvent event){
        this.initializeConfiguration();
        this.initializeResults();
        this.config.setDisable(false);
        this.showCycles.setDisable(false);
        this.saveCycles.setDisable(true);
        this.resetCycles.setDisable(true);
        this.references.setText(this.services.getReferences());
    }
    @FXML   public void clickShowCycles(MouseEvent event){
        this.config.setDisable(true);
        this.saveCycles.setDisable(false);
        this.showCycles.setDisable(true);
        this.resetCycles.setDisable(false);

        int limit =             this.limit.getValue();
        boolean isExactLimit =  this.exactLimit.isSelected();
        boolean isWithID =      this.showToID.isSelected();

        this.process.setVisible(true);

        if (allCycles.isSelected()){
            this.answer.setText(this.services.getInfoToShow(limit, isExactLimit, isWithID, MAX_NUMBER_OF_CYCLES_TO_SHOW));
            this.process.setVisible(false);
            this.cyclesAmount.setText(String.valueOf(this.services.getNumberOfCycles(false)));
        }

        else {
            String package1 = this.package1.getValue();
            String package2 = this.package2.getValue();
            this.answer.setText(this.services.getInfoToShow(limit, isExactLimit, isWithID, MAX_NUMBER_OF_CYCLES_TO_SHOW, package1, package2));
            this.process.setVisible(false);
            this.cyclesAmount.setText(String.valueOf(this.services.getNumberOfCycles(true)));
        }

        this.tarjanTime.setText(String.valueOf(this.services.getTarjanTime()));
    }
    @FXML   public void clickSaveOut(MouseEvent event){

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar Directorio");
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null){
            int limit =             this.limit.getValue();
            boolean isExactLimit =  this.exactLimit.isSelected();
            boolean isWithID =      this.showToID.isSelected();
            String path     = selectedDirectory.getPath();

            if (this.particularCycles.isSelected()){
                String package1 = this.package1.getValue();
                String package2 = this.package2.getValue();

                this.services.saveOut(path, limit, isExactLimit, isWithID, package1, package2);
            }

            else    this.services.saveOut(path, limit, isExactLimit, isWithID);
        }


    }

    // ********** Results **********
    @FXML    private TextArea answer;
    @FXML    private TextArea references;
    @FXML    private TextField cyclesAmount;
    @FXML    private TextField dependenciesAmount;
    @FXML    private TextField packagesAmount;
    @FXML    private TextField tarjanTime;


    private void initializeResults(){
        this.answer.setText("");
        this.references.setText("");
        this.cyclesAmount.setText("");
        this.tarjanTime.setText("");
    }

}
