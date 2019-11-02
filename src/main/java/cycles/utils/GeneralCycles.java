package cycles.utils;

import cycles.services.CyclesService;

import java.io.PrintWriter;

public class GeneralCycles extends CycleInformationBuilder {

    public GeneralCycles(CyclesService cyclesService) {
        super(cyclesService);
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

}
