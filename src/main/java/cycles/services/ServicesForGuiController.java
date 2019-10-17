package cycles.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesForGuiController {
    private CyclesService cyclesService;
    private String references, cyclesInformation;
    private List<String> packagesID, packagesName;
    private int particularCyclesAmount;

    public ServicesForGuiController(String path) {
        this.cyclesService = new CyclesService(path);
        this.cyclesService.runSAX();
        this.packagesID = new ArrayList<>();
        this.packagesName = new ArrayList<>();
        this.buildReferences();
    }

    // ********** SERVICES **********

    /**
     *  To get cycles's information to show.
     * @param limit                     Specifies nodes amount in the cycles.
     * @param isExactLimit              Specifies if {@code limit} is exact (if isn't exact limit is interpreted as a maximum limit).
     * @param isWithID                  Specifies if will show package with ID (if is false is interpreted to be shown to package name).
     * @param maxNumberOfCyclesToShow   Specifies maximum number of cycles that the controller can to show.
     * @return                          Cycles's information to show.
     */
    public String getInfoToShow(int limit, boolean isExactLimit, boolean isWithID, int maxNumberOfCyclesToShow){
        this.buildInfoAboutParticularCycles(limit, isExactLimit, isWithID);

        if (this.cyclesService.getNumberOfCycles() > maxNumberOfCyclesToShow)
            return this.buildLimitedInfoToShow(maxNumberOfCyclesToShow);

        return this.cyclesInformation;
    }

    /**
     *  To get cycles's information to show.
     * @param limit                     Specifies nodes amount in the cycles.
     * @param isExactLimit              Specifies if {@code limit} is exact (if isn't exact limit is interpreted as a maximum limit).
     * @param isWithID                  Specifies if will show package with ID (if is false is interpreted to be shown to package name).
     * @param maxNumberOfCyclesToShow   Specifies maximum number of cycles that the controller can to show.
     * @return                          Cycles's information to show.
     */
    public String getInfoToShow(int limit, boolean isExactLimit, boolean isWithID, int maxNumberOfCyclesToShow, String package1, String package2){
        this.buildInfoAboutParticularCycles(limit, isExactLimit, isWithID, package1, package2);

        if (this.cyclesService.getNumberOfCycles() > maxNumberOfCyclesToShow)
            return this.buildLimitedInfoToShow(maxNumberOfCyclesToShow);

        return this.cyclesInformation;
    }

    /**
     * To get references to show package's ID associated with its package name.
     * @return  References.
     */
    public String getReferences(){
        return this.references;
    }

    /**
     * To get the number of cycles.
     * @return  Number of cycles.
     */
    public int getNumberOfCycles(boolean isParticularCase){
        if (isParticularCase)   return this.particularCyclesAmount;

        return this.cyclesService.getNumberOfCycles();
    }

    /**
     * To get the run time of Tarjan algorithm.
     * @return  {@code long} that represents time in seconds.
     */
    public long getTarjanTime(){
        return this.cyclesService.getTarjanTime();
    }

    /**
     * To get the number of dependencies.
     * @return  Number of dependencies.
     */
    public int getNumberOfDependencies(){
        return this.cyclesService.getNumberOfDependencies();
    }

    /**
     * To get the number of packages.
     * @return  Number of packages.
     */
    public int getNumberOfPackages() {
        return this.cyclesService.getNumberOfPackages();
    }

    /**
     * To get options for select packages.
     * @param isWithID  Specifies if will show package with ID (if is false is interpreted to be shown to package name).
     * @return          Options for select packages.
     */
    public List<String> getPackagesOptions(boolean isWithID){
        if (isWithID)   return this.packagesID;
        return this.packagesName;
    }

    public void saveOut(String pathForSave, int limit, boolean isExactLimit, boolean isWithID){
        File f = new File(pathForSave+"/Cycles_TPFinal_Brisuela&Fluxá.txt");

        try {

            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            this.saveGeneralOut(wr, false);

            wr.write("Todos los ciclos con");
            if (!isExactLimit)  wr.write(", al menos,");
            else wr.write(" ");
            wr.write(String.valueOf(limit));
            wr.write(" paquetes:\n");
            wr.write(this.cyclesInformation);

            if (isWithID){
                wr.write("\n\n");
                wr.write("Referencias\n:");
                wr.write(this.getReferences());
            }

            wr.close();
            bw.close();

        } catch (IOException e){
            System.out.println("Error to write the file "+f);
        }
    }

    public void saveOut(String pathForSave, int limit, boolean isExactLimit, boolean isWithID, String package1, String package2){
        File f = new File(pathForSave+"/Cycles_TPFinal_Brisuela&Fluxá.txt");

        try {

            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            this.saveGeneralOut(wr, true);

            wr.write("Ciclos entre '");
            wr.write(package1);
            wr.write("' y '");
            wr.write(package2);
            wr.write("' con");
            if (!isExactLimit)  wr.write(", al menos,");
            else wr.write(" ");
            wr.write(String.valueOf(limit));
            wr.write(" paquetes:\n");
            wr.write(this.cyclesInformation);

            if (isWithID){
                wr.write("\n\n");
                wr.write("Referencias:\n");
                wr.write(this.getReferences());
            }

            wr.close();
            bw.close();

        } catch (IOException e){
            System.out.println("Error to write the file "+f);
        }
    }

    // ********** AUXILIARY METHODS **********

    /**
     * Build the information about all cycles.
     * @param limit         Specifies nodes amount in the cycles.
     * @param isExactLimit  Specifies if {@code limit} is exact (if isn't exact limit is interpreted as a maximum limit).
     * @param isWithID      Specifies if will show package with ID (if is false is interpreted to be shown to package name).
     */
    private void buildInfoAboutParticularCycles(int limit, boolean isExactLimit, boolean isWithID){
        this.cyclesService.runTarjan(limit, isExactLimit);
        List<byte[]> compressCycles = this.cyclesService.getCycles();

        StringBuilder infoToShow = new StringBuilder();
        int numberOfCycles = this.cyclesService.getNumberOfCycles();

        for (byte[] cycle : compressCycles) {
            numberOfCycles--;
            long[] decompressCycle = this.cyclesService.decompressCycle(cycle);
            infoToShow.append(this.getInfoAboutCycle(decompressCycle, isWithID));

            if (numberOfCycles != 0){
                infoToShow.append("\n");
            }
        }

        this.cyclesInformation = infoToShow.toString();
    }

    private void buildInfoAboutParticularCycles(int limit, boolean isExactLimit, boolean isWithID, String package1, String package2){
        this.cyclesService.runTarjan(limit, isExactLimit);
        List<byte[]> compressCycles = this.cyclesService.getCycles();
        this.particularCyclesAmount = 0;

        StringBuilder infoToShow = new StringBuilder();
        int numberOfCycles = this.cyclesService.getNumberOfCycles();

        for (byte[] cycle : compressCycles) {
            numberOfCycles--;
            long[] decompressCycle = this.cyclesService.decompressCycle(cycle);
            String infoCycleCommon = this.getInfoAboutCycle(decompressCycle, isWithID, package1, package2);

            if (infoCycleCommon != null){
                this.particularCyclesAmount++;
                infoToShow.append(infoCycleCommon);

                if (numberOfCycles != 0){
                    infoToShow.append("\n");
                }
            }
        }

        this.cyclesInformation = infoToShow.toString();
    }

    /**
     * Builds and returns cycle's information to show but limited by maximum number of cycles that the controller can to show.
     * @param maxNumberOfCyclesToShow   Specifies maximum number of cycles that the controller can to show.
     * @return                          Cycles's information to show limited by maximum number of cycles that the controller can to show.
     */
    private String buildLimitedInfoToShow(int maxNumberOfCyclesToShow){
        StringBuilder infoToShow = new StringBuilder();
        infoToShow.append(
                "¡ATENCIÓN! La cantidad de ciclos es mayor al permitido para mostrar.\n" +
                "Guarde la salida en un archivo para visualizarla completa.\n\n");

        String[] cyclesToShow = this.cyclesInformation.split("\n", maxNumberOfCyclesToShow+1);
        for (int i = 0; i < maxNumberOfCyclesToShow; i++) {
            infoToShow.append(cyclesToShow[i]);
            infoToShow.append("\n");
        }
        infoToShow.append("   ...");

        return infoToShow.toString();
    }

    /**
     * Build the information about cycles.
     * @param cycle     Decompress cycle.
     * @param isWithID  Specifies if will show package with ID (if is false is interpreted to be shown to package name).
     * @return  Information about cycles.
     */
    private String getInfoAboutCycle(long[] cycle, boolean isWithID){
        StringBuilder info = new StringBuilder();
        int cycleSize = cycle.length;

        info.append("   ");
        for (int i = 0; i < cycleSize; i++) {
            info.append(this.getInfoAboutPackage(cycle[i], isWithID));

            if (i != cycleSize-1)
                info.append(" → ");
        }

        return info.toString();
    }

    private String getInfoAboutCycle(long[] cycle, boolean isWithID, String package1, String package2){
        boolean isPackage1 = false;
        boolean isPackage2 = false;

        StringBuilder info = new StringBuilder();
        int cycleSize = cycle.length;

        info.append("   ");
        for (int i = 0; i < cycleSize; i++) {
            String current = this.getInfoAboutPackage(cycle[i], isWithID);
            if (current.equals(package1))   isPackage1 = true;
            if (current.equals(package2))   isPackage2 = true;

            info.append(current);

            if (i != cycleSize-1)
                info.append(" → ");
        }

        if (isPackage1 && isPackage2)   return info.toString();

        return null;
    }

    /**
     * To get information about one package.
     * @param packageID     Package ID.
     * @param isWithID      Specifies if will show package with ID (if is false is interpreted to be shown to package name).
     * @return              Information about package.
     */
    private String getInfoAboutPackage(long packageID, boolean isWithID){
        if (isWithID)
            return String.valueOf(packageID);

        return this.cyclesService.getPackages().get(packageID);
    }

    /**
     * Build references to show package's ID associated with its package name.
     */
    private void buildReferences() {
        StringBuilder references = new StringBuilder();
        int i = this.cyclesService.getNumberOfPackages();

        for (Long id : this.cyclesService.getPackages().keySet()) {
            references.append("   ");
            i--;
            String name = this.cyclesService.getPackages().get(id);
            this.packagesID.add(String.valueOf(id));
            this.packagesName.add(name);
            references.append(id);
            references.append(" → ");
            references.append(name);

            if (i != 0)
                references.append("\n");
        }

        this.references = references.toString();
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
                        "*****************************************\n");
    }

    private void saveGeneralOut(PrintWriter wr, boolean isParticularCase){
        wr.write(this.getHeader());

        wr.write("\n\n");

        wr.write("Ruta de archivo:          ");
        wr.write(this.cyclesService.getPath());
        wr.write("\n");

        wr.write("Cantidad de ciclos:       ");
        wr.write(String.valueOf(this.getNumberOfCycles(isParticularCase)));
        wr.write("\n");

        wr.write("Tiempo de Tarjan (ms):    ");
        wr.write(String.valueOf(this.getTarjanTime()));
        wr.write("\n");

        wr.write("Cantidad de paquetes:     ");
        wr.write(String.valueOf(this.getNumberOfPackages()));
        wr.write("\n");

        wr.write("Cantidad de dependencias: ");
        wr.write(String.valueOf(this.getNumberOfDependencies()));

        wr.write("\n\n");
    }
}
