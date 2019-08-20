package cycles.services;

import cycles.algorithm.CycleCompressor;

import java.util.HashMap;
import java.util.List;

public class ServiceForControllers {

    private CyclesService cyclesService;
    private HashMap<Long, String> vertexes;

    public ServiceForControllers(String path){
        this.cyclesService = new CyclesService(path);
        cyclesService.runSAX();
        this.vertexes = this.cyclesService.getVertexes();
    }

    public String getCycles(int limit, boolean isExactLimit, boolean isWithID){
        StringBuilder info = new StringBuilder();

        this.cyclesService.runTarjan(limit, isExactLimit);
        List<byte[]> cycles = this.cyclesService.getCycles();

        for (byte[] cycle : cycles){
            long[] decompressCycle = this.cyclesService.decompressCycle(cycle);

            int size = decompressCycle.length;

            for (int j = 0; j < size; j++) {
                info.append(this.getAppend(size, j, decompressCycle[j], isWithID));
            }

            info.append("\n");
        }

        return info.toString();
    }

    public String getReferences(){
        StringBuilder references = new StringBuilder();

        for (Long id : this.vertexes.keySet()){
            references.append(id+" --> "+this.vertexes.get(id)+"\n");
        }

        return references.toString();
    }

    private String getAppend(int limit, int index, long id, boolean isWithID){
        StringBuilder info = new StringBuilder();
        info.append(this.getShowInfo(id, isWithID));

        if (index != limit-1)
            info.append(" → ");

        return info.toString();
    }

    private String getShowInfo(long id, boolean isWithID){
        if (isWithID)
            return String.valueOf(id);

        return this.vertexes.get(id);
    }

}
