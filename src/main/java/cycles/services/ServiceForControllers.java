package cycles.services;

import java.io.*;
import java.util.List;

/**
 * It is the information provider to controllers.
 * @author Jeremías Brisuela & Noelia Fluxá
 * @version 1.1
 */

public class ServiceForControllers {

    private static final int MAX_CYCLE_SHOW = 30;

    private boolean isCompleteInfo;
    private CyclesService cyclesService;
    private StringBuilder infoCycles, infoToShow;
    private int numberOfCycles;
    private String references;

    public ServiceForControllers(String path) {
        this.cyclesService = new CyclesService(path);
        cyclesService.runSAX();
        this.buildReferences();

        this.infoCycles = new StringBuilder();
        this.infoToShow = new StringBuilder();
        this.isCompleteInfo = true;
        this.numberOfCycles = 0;
    }

    // ********** SERVICES **********

    /**
     * To get information about the cycles. The view of the information will be according to its use.
     *
     * @param isForSave     Specifies or not if the information will be saved.
     * @param isWithID      Specifies or not if the packages will be viewed by ID.
     * @param isNotVerifier Specifies or not if cycles weren't searched between two particular packages.
     * @param isExactLimit  Specifies if the search was with exact limit (if wasn't with exact limit is interpreted as that it was maximum limit).
     * @param limit         Specifies nodes amount in the cycles searched.
     * @param package1      Specifies the first package from searched between two particular packages.
     * @param package2      Specifies the second package from searched between two particular packages.
     * @return The information about the packages dependencies cycles.
     */
    public String getCyclesInformation(boolean isForSave, boolean isWithID, boolean isNotVerifier, boolean isExactLimit, int limit, long package1, long package2) {
        StringBuilder information = new StringBuilder();

        information.append(this.getGeneralInfoAboutCycles());

        if (isWithID) {
            information.append("\nREFERENCES\n");
            information.append(this.getReferences());
        }

        information.append("\n");
        information.append(this.getInfoAboutCyclesCharacteristics(isNotVerifier, isExactLimit, limit, package1, package2));

        if (isNotVerifier) this.buildInfoAboutAllCycles(limit, isExactLimit, isWithID);
        else this.buildInfoAboutCyclesBetweenTwoPackages(package1, package2, limit, isExactLimit, isWithID);

        if (this.getNumberOfCycles() == 0) information.append("THERE AREN'T CYCLES");
        else if (isForSave) information.append(this.infoCycles.toString());
        else information.append(this.infoToShow.toString());

        return information.toString();
    }

    /**
     * To get number of cycles.
     *
     * @return The number of cycles.
     */
    public int getNumberOfCycles() {
        return this.numberOfCycles;
    }

    /**
     * To get number of packages.
     *
     * @return The number of packages.
     */
    public int getNumberOfPackages() {
        return this.cyclesService.getPackages().size();
    }

    /**
     * To get references: package ID --> package name.
     *
     * @return The references.
     */
    public String getReferences() {
        return this.references;
    }

    /**
     * To get the run time of Tarjan algorithm.
     *
     * @return {@code long} that represents time in seconds.
     */
    public long getTarjanTime() {
        return this.cyclesService.getTarjanTime();
    }

    /**
     * To get the number of edges.
     *
     * @return The number of edges.
     */
    public int getNumberOfEdges() {
        return this.cyclesService.getGraph().getEdges().size();
    }


    // ********** AUXILIARY METHODS **********

    /**
     * To get information about the characteristics from one cycle.
     * @param isNotVerifier Specifies or not if cycles weren't searched between two particular packages.
     * @param isExactLimit  Specifies if the search was with exact limit (if wasn't with exact limit is interpreted as that it was maximum limit).
     * @param limit         Specifies nodes amount in the cycles searched.
     * @param package1      Specifies the first package from searched between two particular packages.
     * @param package2      Specifies the second package from searched between two particular packages.
     * @return              The information about the characteristics from one packages dependencies cycle.
     */
    private String getInfoAboutCyclesCharacteristics(boolean isNotVerifier, boolean isExactLimit, int limit, long package1, long package2){
        StringBuilder info = new StringBuilder();

        if (isNotVerifier){
            info.append("Cycles with ");

            if (!isExactLimit){
                info.append("a most ");
            }

        }
        else {
            info.append("Cycles between package '");
            info.append(package1);
            info.append("' and the package '");
            info.append(package2);
            info.append("' with ");

            if (!isExactLimit){
                info.append("a most ");
            }
        }

        info.append(limit);
        info.append(" packages\n");

        return info.toString();
    }

    /** To get general information about the packages dependencies cycles.
     * @return The general information about the cycles.
     */
    private String getGeneralInfoAboutCycles(){
        StringBuilder info = new StringBuilder();
        info.append("\nINFO\n");

        info.append("    Path file: ");
        info.append(this.cyclesService.getPath());
        info.append("\n");

        info.append("    Tarjan time: ");
        info.append(this.getTarjanTime());
        info.append("ms \n");

        info.append("    Cycles amount: ");
        info.append(this.getNumberOfCycles());
        info.append("\n");

        info.append("    Packages amount: ");
        info.append(this.getNumberOfPackages());
        info.append("\n");

        info.append("    Edges amount: ");
        info.append(this.getNumberOfEdges());
        info.append("\n");

        return info.toString();
    }

    /**
     * To get the header for the saved file.
     * @return  The header for the saved file.
     */
    private String getHeader(){
        return (
                "TP Final TALLER DE PROGRAMACIÓN JAVA 2019\n" +
                        "\n" +
                        "Jeremías Brisuela  jere05.mdq@gmail.com\n" +
                        "Noelia Fluxá       noefluxa@gmail.com\n" +
                        "\n" +
                        "*****************************************\n\n");
    }

    /**
     * Save information about cycles in the file.
     * @param pathForSave   Path for save the file.
     * @param isForSave     Specifies or not if the information will be saved.
     * @param isWithID      Specifies or not if the packages will be viewed by ID.
     * @param isNotVerifier Specifies or not if cycles weren't searched between two particular packages.
     * @param isExactLimit  Specifies if the search was with exact limit (if wasn't with exact limit is interpreted as that it was maximum limit).
     * @param limit         Specifies nodes amount in the cycles searched.
     * @param package1      Specifies the first package from searched between two particular packages.
     * @param package2      Specifies the second package from searched between two particular packages.
     * @return If the information could be saved well.
     */
    public boolean saveCompleteInfo(String pathForSave, boolean isForSave, boolean isWithID, boolean isNotVerifier, boolean isExactLimit, int limit, long package1, long package2) {
        File f = new File(pathForSave+"/Cycles_TPFinal_Brisuela&Fluxá.txt"); //TODO: Correct the file name

        try {

            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            wr.write(this.getHeader());
            wr.write(this.getCyclesInformation(isForSave, isWithID, isNotVerifier, isExactLimit, limit, package1, package2));

            wr.close();
            bw.close();

        } catch (IOException e){
            System.out.println("Error to write the file"+f);
        }

        return f.exists();
    }

    public boolean isCompleteInfo() {            // Retorna si la información se puede mostrar completa o no
        return this.isCompleteInfo;
    }

    private void buildInfoAboutCyclesBetweenTwoPackages(long idPackage1, long idPackage2, int limit, boolean isExactLimit, boolean isWithID) {
        boolean package1;
        boolean package2;
        boolean isAdded;

        this.cyclesService.runTarjan(limit, isExactLimit);
        List<byte[]> cycles = this.cyclesService.getCycles();

        for (byte[] cycle : cycles) {
            package1 = false;
            package2 = false;
            isAdded = false;

            long[] decompressCycle = this.cyclesService.decompressCycle(cycle);
            int size = decompressCycle.length;

            for (int j = 0; j < size; j++) {
                long value = decompressCycle[j];

                if (value == idPackage1)
                    package1 = true;

                else if (value == idPackage2)
                    package2 = true;

                if (package1 && package2 && !isAdded){
                    this.numberOfCycles++;
                    isAdded = true;
                    this.saveCycle(decompressCycle, isWithID);
                }
            }
        }
    }

    private String buildInfoAboutAllCycles(int limit, boolean isExactLimit, boolean isWithID) {

        this.cyclesService.runTarjan(limit, isExactLimit);
        List<byte[]> cycles = this.cyclesService.getCycles();

        for (byte[] cycle : cycles) {
            long[] decompressCycle = this.cyclesService.decompressCycle(cycle);
            this.numberOfCycles++;
            this.saveCycle(decompressCycle, isWithID);
        }

        return this.infoToShow.toString();
    }
    private String getAppendForSeeCycle(int limit, int index, long id, boolean isWithID) {
        StringBuilder info = new StringBuilder();
        info.append(this.getShowInfo(id, isWithID));

        if ((index != limit - 1))
            info.append(" → ");

        return info.toString();
    }  // Retorna la concatenación a hacer según la pos del paquete dentro del ciclo
    private void saveCycle(long[] cycle, boolean isWithID) {
        int size = cycle.length;

        if (this.numberOfCycles < MAX_CYCLE_SHOW) {
            for (int j = 0; j < size; j++) {
                this.infoToShow.append(this.getAppendForSeeCycle(size, j, cycle[j], isWithID));
                this.infoCycles.append(this.getAppendForSeeCycle(size, j, cycle[j], isWithID));
            }

            infoToShow.append("\n");
        } else {
            if (this.numberOfCycles == MAX_CYCLE_SHOW) {
                infoToShow.append("...");
                this.isCompleteInfo = false;
            }

            for (int j = 0; j < size; j++) {
                this.infoCycles.append(this.getAppendForSeeCycle(size, j, cycle[j], isWithID));
            }
        }

        this.infoCycles.append("\n");
    } // Guarda como debe ser mostrado un ciclo
    private String getShowInfo(long id, boolean isWithID){
        if (isWithID)
            return String.valueOf(id);

        return this.cyclesService.getPackages().get(id);
    }


    private void buildReferences() {
        StringBuilder references = new StringBuilder();

        for (Long id : this.cyclesService.getPackages().keySet()) {
            references.append(id);
            references.append(" -->  ");
            references.append(this.cyclesService.getPackages().get(id));
            references.append("\n");
        }

        this.references = references.toString();
    }

}
