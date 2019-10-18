package cycles.utils;

import cycles.services.CyclesService;

import java.io.PrintWriter;
import java.util.List;

public abstract class CycleInformationBuilder {
    protected boolean isCompleteInformation;
    protected StringBuilder informationToShow;
    protected CyclesService cyclesService;

    protected CycleInformationBuilder(CyclesService cyclesService){
        this.informationToShow = new StringBuilder();
        this.cyclesService = cyclesService;
    }

    public abstract String getInformationToShow(boolean isWithID, int maxNumberOfCyclesToShow);

    public void saveInfoInTextFile(boolean isWithID, boolean isExactLimit, int limit, int maxNumberOfCyclesToShow, PrintWriter wr){
        wr.write(this.getHeaderToFile());
        wr.write(this.getInfoToFile());
        wr.write(this.getSubtitleToFile(isExactLimit, limit));
        wr.write(this.informationToShow.toString());

        if (!isCompleteInformation){
            wr.write("\n");
            this.toCompleteFile(maxNumberOfCyclesToShow, isWithID, wr);
        }
    }

    protected abstract void toCompleteFile(int maxNumberOfCyclesToShow, boolean isWithID, PrintWriter wr);

    protected void processCycles(int since, int until, boolean isWithID, PrintWriter wr){
        List<byte[]> compressCycles = this.cyclesService.getCycles();

        for (int i = since; i < until; i++) {
            byte[] compressCycle = compressCycles.get(i);
            long[] decompressCycle = this.cyclesService.decompressCycle(compressCycle);
            this.buildInformation(decompressCycle, i, isWithID, wr);
        }
    }
    protected void buildInformation(long[] cycle, int index, boolean isWithID, PrintWriter wr){
        if (wr == null)
            this.informationToShow.append(this.getCycleInformation(cycle, index, isWithID));
        else
            wr.write(this.getCycleInformation(cycle, index, isWithID));
    }
    protected String getHeaderLimitInfo(){
        return ("¡ATENCIÓN! La cantidad de ciclos es mayor al permitido para mostrar.\n" +
                "Guarde la salida en un archivo para visualizarla completa.\n\n");
    }
    private String getHeaderToFile(){
        return (
                "TP Final TALLER DE PROGRAMACIÓN JAVA 2019\n" +
                        "\n" +
                        "Jeremías Brisuela  jere05.mdq@gmail.com\n" +
                        "Noelia Fluxá       noefluxa@gmail.com\n" +
                        "\n" +
                        "*****************************************\n");
    }
    private String getInfoToFile(){
        StringBuilder info = new StringBuilder();

        info.append("\n\n");
        info.append("Ruta de archivo:          ");
        info.append(this.cyclesService.getPath());
        info.append("\n");
        info.append("Cantidad de ciclos:       ");
        info.append(String.valueOf(this.getNumberOfCycles()));
        info.append("\n");
        info.append("Tiempo de Tarjan (ms):    ");
        info.append(String.valueOf(this.cyclesService.getTarjanTime()));
        info.append("\n");
        info.append("Cantidad de paquetes:     ");
        info.append(String.valueOf(this.cyclesService.getNumberOfPackages()));
        info.append("\n");
        info.append("Cantidad de dependencias: ");
        info.append(String.valueOf(this.cyclesService.getNumberOfDependencies()));
        info.append("\n\n");

        return info.toString();
    }

    protected String getInfoAboutCycle(long[] cycle, boolean isWithID){
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
    protected String getInfoAboutPackage(long packageID, boolean isWithID){
        if (isWithID)
            return String.valueOf(packageID);

        return this.cyclesService.getPackages().get(packageID);
    }
    protected String fullInformationToShow(boolean isWithID){
        this.processCycles(0, this.cyclesService.getNumberOfCycles(), isWithID, null);
        return this.informationToShow.toString();
    }
    protected String limitInformationToShow(int maxNumberOfCyclesToShow, boolean isWithID){
        StringBuilder info = new StringBuilder();
        info.append(this.getHeaderLimitInfo());

        this.processCycles(0, maxNumberOfCyclesToShow, isWithID, null);
        info.append(this.informationToShow);

        return info.toString();
    }

    public abstract String getCycleInformation(long[] cycle, int index, boolean isWithID);
    public abstract int getNumberOfCycles();
    protected abstract String getSubtitleToFile(boolean isExactLimit, int limit);
}