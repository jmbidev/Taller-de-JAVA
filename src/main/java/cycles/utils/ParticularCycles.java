package cycles.utils;

import cycles.services.CyclesService;

import java.io.PrintWriter;

public class ParticularCycles extends CycleInformationBuilder {
    private int cyclesAmount;
    private String package1;
    private String package2;
    private int lastCycleView;

    public ParticularCycles(CyclesService cyclesService) {
        super(cyclesService);
        this.cyclesAmount = 0;
        this.lastCycleView = 0;
    }

    @Override
    public String getInformationToShow(boolean isWithID, int maxNumberOfCyclesToShow) {
        this.cyclesAmount = 0;
        int cyclesAvailable = this.cyclesService.getNumberOfCycles();

        if (cyclesAvailable > maxNumberOfCyclesToShow){
            int until = 0;
            int viewAvailable = maxNumberOfCyclesToShow;

            while (viewAvailable>0 && cyclesAvailable>0) {
                int since = until;

                if (viewAvailable > cyclesAvailable)    until = since+cyclesAvailable;
                else                                    until += viewAvailable;

                this.processCycles(since, until, isWithID, null);
                viewAvailable = maxNumberOfCyclesToShow - this.cyclesAmount;
                cyclesAvailable -= until-since;
            }

            this.isCompleteInformation = (cyclesAvailable==0);

            StringBuilder infoToShow = new StringBuilder();
            if (!this.isCompleteInformation){
                infoToShow.append(this.getHeaderLimitInfo());
                this.lastCycleView = until;
            }

            infoToShow.append(this.informationToShow);
            if (!this.isCompleteInformation)                infoToShow.append("   ...");
            return infoToShow.toString();
        }

        this.isCompleteInformation = true;
        return this.fullInformationToShow(isWithID);
    }

    @Override
    public String getCycleInformation(long[] cycle, int index, boolean isWithID) {
        StringBuilder info = new StringBuilder();

        String infoCycleCommon = this.getInfoAboutCycle(cycle, isWithID, this.package1, this.package2);

        if (infoCycleCommon != null) {
            this.cyclesAmount++;

            info.append(infoCycleCommon);

                if (index != this.cyclesService.getNumberOfCycles() - 1)
                    info.append("\n");
        }

        return info.toString();
    }

    @Override
    public int getNumberOfCycles() {
        return this.cyclesAmount;
    }

    @Override
    protected String getSubtitleToFile(boolean isExactLimit, int limit) {
        StringBuilder info = new StringBuilder();

        info.append("   Ciclos entre '");
        info.append(this.package1);
        info.append("' y '");
        info.append(this.package2);
        info.append("' con");
        if (!isExactLimit)  info.append(", al menos, ");
        else                info.append(" ");
        info.append(String.valueOf(limit));
        info.append(" paquetes:\n\n");

        return info.toString();
    }

    @Override
    protected void toCompleteFile(int maxNumberOfCyclesToShow, boolean isWithID, PrintWriter wr) {
        this.processCycles(this.lastCycleView, this.cyclesService.getNumberOfCycles(), isWithID, wr);
    }

    private String getInfoAboutCycle(long[] cycle, boolean isWithID, String package1, String package2){
        boolean isPackage1 = false;
        boolean isPackage2 = false;

        StringBuilder info = new StringBuilder();
        int cycleSize = cycle.length;

        info.append("   ");
        for (int i = 0; i < cycleSize; i++) {
            String currentPackage = this.getInfoAboutPackage(cycle[i], isWithID);

            if (currentPackage.equals(package1))   isPackage1 = true;
            if (currentPackage.equals(package2))   isPackage2 = true;

            info.append(currentPackage);

            if (i != cycleSize-1)
                info.append(" → ");
        }

        if (isPackage1 && isPackage2)   return info.toString();

        return null;
    }
    public void setPackage1(String package1) {
        this.package1 = package1;
    }
    public void setPackage2(String package2) {
        this.package2 = package2;
    }
}
