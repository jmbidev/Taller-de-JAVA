package cycles.utils;

import cycles.services.CyclesService;

import java.io.PrintWriter;

public class ParticularCycles extends CycleInformationBuilder {
    private String package1;
    private String package2;

    public ParticularCycles(CyclesService cyclesService) {
        super(cyclesService);
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

    public void setPackage1(String package1) {
        this.package1 = package1;
    }
    public void setPackage2(String package2) {
        this.package2 = package2;
    }
}
