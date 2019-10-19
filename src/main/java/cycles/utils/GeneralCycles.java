package cycles.utils;

import cycles.services.CyclesService;

import java.io.PrintWriter;

public class GeneralCycles extends CycleInformationBuilder {
    private static final int NO_VALUE = -1;

    public GeneralCycles(CyclesService cyclesService) {
        super(cyclesService);
    }

    @Override
    public String getCycleInformation(long[] cycle, int index, boolean isWithID){
        StringBuilder info = new StringBuilder();

        info.append(this.getInfoAboutCycle(cycle, isWithID));

        if (index != this.cyclesService.getNumberOfCycles()-1)
            info.append("\n");

        return info.toString();
    }

    @Override
    public int getNumberOfCycles() {
        return this.cyclesService.getNumberOfCycles();
    }

    @Override
    protected String getSubtitleToFile(boolean isExactLimit, int limit) {
        StringBuilder info = new StringBuilder();

        info.append("   Todos los ciclos con");
        if (!isExactLimit)      info.append(", al menos, ");
        else                    info.append(" ");
        info.append(String.valueOf(limit));
        info.append(" paquetes:\n\n");

        return info.toString();
    }

    @Override
    public String getInformationToShow(boolean isWithID, int maxNumberOfCyclesToShow) {
        if (this.getNumberOfCycles() > maxNumberOfCyclesToShow){
            this.isCompleteInformation = false;
            return this.limitInformationToShow(maxNumberOfCyclesToShow, isWithID);
        }

        this.isCompleteInformation = true;
        return this.fullInformationToShow(isWithID);
    }

    @Override
    protected void toCompleteFile(int maxNumberOfCyclesToShow, boolean isWithID, PrintWriter wr) {
        processCycles(maxNumberOfCyclesToShow, this.getNumberOfCycles(), isWithID, wr);
    }

    private String limitInformationToShow(int maxNumberOfCyclesToShow, boolean isWithID){
        StringBuilder info = new StringBuilder();
        info.append(this.getHeaderLimitInfo());

        this.processCycles(0, maxNumberOfCyclesToShow, isWithID, null);
        info.append(this.informationToShow);
        info.append("   ...");

        return info.toString();
    }
}
