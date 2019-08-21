package cycles.services;

import java.io.*;
import java.util.List;

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

    // SERVICES
    public String getCyclesInformation(boolean isForSave, boolean isWithID, boolean isTotalCycle, boolean isExactLimit, int limit, long package1, long package2){
        StringBuilder information = new StringBuilder();

        information.append("\nINFO\n");
        information.append("    Ruta de archivo: " + this.cyclesService.getPath()+"\n");
        information.append("    Tiempo de Tarjan: " + this.getTarjanTime()+ " ms\n");
        information.append("    Cant. de ciclos: "  + this.getNumberOfCycles() + "\n");
        information.append("    Cant. de paquetes: "    + this.getNumberOfPackages() + "\n");
        information.append("    Cant. de relaciones entre paquetes: "   + this.getNumberOfEdges() + "\n");

        if (isWithID){
            information.append("\nREFERENCIAS\n");
            information.append(this.getReferences());
        }

        information.append("\n");
        information.append(this.getCycleCharacteristics(isTotalCycle, isExactLimit, limit, package1, package2));

        if (isTotalCycle)
                this.getCycles(limit, isExactLimit, isWithID);
        else
            this.getExistCycle(package1, package2, limit, isExactLimit, isWithID);

        if (isForSave)
            information.append(this.infoCycles.toString());

        else
            information.append(this.infoToShow.toString());

        if (this.getNumberOfCycles() == 0){
            System.out.println("NO HAY CICLOS");
        }

        return information.toString();
    } //Retorna la información para mostrar los ciclos
    public boolean saveCompleteInfo(String pathForSave, boolean isForSave, boolean isWithID, boolean isTotalCycle, boolean isExactLimit, int limit, long package1, long package2) {
        File f = new File(pathForSave+"/DEPENDENCIAS_CICLICAS.txt"); //TODO: Correct the file name

        try {

            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);

            wr.write(this.getHeader());
            wr.write(this.getCyclesInformation(isForSave, isWithID, isTotalCycle, isExactLimit, limit, package1, package2));

            wr.close();
            bw.close();

        } catch (IOException e){
            System.out.println("ERROR AL ESCRBIR EN EL ARCHIVO "+f);
        }

        return f.exists();
    } // Almacena la información de los ciclos en un archivo y retorna si lo puedo hacer correctamente
    public int getNumberOfCycles(){
        return this.numberOfCycles;
    }          // Retorna el número de ciclos
    public int getNumberOfPackages() {
        return this.cyclesService.getVertexes().size();
    }       // Retorna el número de paquetes
    public String getReferences(){
        return this.references;
    }           // Retorna las referencias
    public long getTarjanTime(){
        return this.cyclesService.getTarjanTime();
    }             // Retorna el tiempo de Tarjan
    public int getNumberOfEdges(){
        return this.cyclesService.getGraph().getEdges().size();
    }           // Retorna el número de relaciones entre paquetes
    public boolean isCompleteInfo() {            // Retorna si la información se puede mostrar completa o no
        return this.isCompleteInfo;
    }

    // AUXILIARY METHODS
    private void getExistCycle(long idPackage1, long idPackage2, int limit, boolean isExactLimit, boolean isWithID) {
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
    private String getCycleCharacteristics(boolean isTotalCycles, boolean isExactLimit, int limit, long package1, long package2) {
        String info = "" ;

        if (isTotalCycles && isExactLimit)
            info = "CICLOS con "+limit+" paquetes\n";

        else if (isTotalCycles && !isExactLimit)
            info = "CICLOS con, a lo sumo, "+limit+" paquetes\n";

        else if (!isTotalCycles && isExactLimit)
            info = "CICLOS entre el paquete " + package1 + " y " + package2 + " con " + limit + " paquetes\n";

        else if (!isTotalCycles && !isExactLimit)
            info = "CICLOS entre el paquete " + package1 + " y " + package2 + " con, a lo sumo, " + limit + " paquetes\n\n";

        return info;
    } // Retorna las caracteristicas de los ciclos a mostrar
    private String getCycles(int limit, boolean isExactLimit, boolean isWithID) {

        this.cyclesService.runTarjan(limit, isExactLimit);
        List<byte[]> cycles = this.cyclesService.getCycles();

        for (byte[] cycle : cycles) {
            long[] decompressCycle = this.cyclesService.decompressCycle(cycle);
            this.numberOfCycles++;
            this.saveCycle(decompressCycle, isWithID);
        }

        return this.infoToShow.toString();
    }
    private String getHeader(){
        return (
                "TP Final TALLER DE PROGRAMACIÓN JAVA 2019\n" +
                        "\n" +
                        "Jeremías Brisuela  jere05.mdq@gmail.com\n" +
                        "Noelia Fluxá       noefluxa@gmail.com\n" +
                        "\n" +
                        "*****************************************\n\n");
    }   // Retorna el header que se guardará en el archivo
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

        return this.cyclesService.getVertexes().get(id);
    }
    private void buildReferences() {
        StringBuilder references = new StringBuilder();

        for (Long id : this.cyclesService.getVertexes().keySet()) {
            references.append(id);
            references.append(" -->  ");
            references.append(this.cyclesService.getVertexes().get(id));
            references.append("\n");
        }

        this.references = references.toString();
    } // Contruye las referencias (ID --> Nombre de Paquete)

}
