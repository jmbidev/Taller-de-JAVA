package cycles.services;

import cycles.algorithm.*;
import cycles.utils.SAXHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;

/**
 * It is the tool provider.
 * Provides information about the cycles between packages.
 * @author Jeremías Brisuela & Noelia Fluxá
 * @version 1.1
 */
public class CyclesService {
    private String path;
    private Map<String, Set<String>> dependencies;
    private Graph<String> graph;
    private List<byte[]> cycles;
    private long tarjanTime;
    private CycleCompressor cycleCompressor;
    private HashMap<Long, String> packages;
    private int numberOfCycles;

    public CyclesService(String path){
        this.path = path;
        this.dependencies = new HashMap<String, Set<String>>();
        this.graph = new Graph<>(true);
        this.cycles = new ArrayList<>();
        this.tarjanTime = 0;
        this.packages = new HashMap<>();
        this.numberOfCycles = 0;
    }


    /**
     * Run SAX (Simple API for XML) to process class dependencies.
     */
    public void runSAX(){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            SAXParser saxParser = factory.newSAXParser();

            File inputFile = new File(this.path);
            SAXHandler handler = new SAXHandler();
            saxParser.parse(inputFile, handler);

            this.dependencies = transform(handler.getClasses(), handler.getClass_pack());
            this.graph = buildGraph(dependencies);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Transform classes dependencies in packages dependencies.
     * @param classes       Class dependencies.
     * @param class_pack    Packages containers.
     * @return              Packages dependencies.
     */
    private static Map<String, Set<String>> transform(Map<String, Set<String>> classes, Map<String, String> class_pack) {

        Map<String, Set<String>> packDependencies = new HashMap<>();
        Set<String> dependenciesKeys = classes.keySet();

        for (String classFrom : dependenciesKeys) {

            Set<String> toSet = classes.get(classFrom);
            String packFrom = class_pack.get(classFrom);

            if (!packDependencies.containsKey(packFrom)) {
                packDependencies.put(packFrom, new HashSet<String>());
            }

            for (String classTo : toSet) {
                String packTo = class_pack.get(classTo);
                if (packTo != null && !packFrom.equals(packTo))
                    packDependencies.get(packFrom).add(packTo);
            }
        }

        return packDependencies;
    }

    /**
     * Builds dependencies graph between packages since class dependencies.
     * @param   dependencies    Class dependencies set.
     * @return  {@code Graph} that represents dependencies graph between packages.
     */
    private Graph<String> buildGraph(Map<String, Set<String>> dependencies) {
        Graph<String> graph = new Graph<>(true);
        long id = 0;

        Map<String, Vertex<String>> vertexes = new HashMap<>();

        for (String packageName : dependencies.keySet()) {
            Vertex<String> vertex = new Vertex<>(id);
            vertex.setData(packageName);
            this.packages.put(id, packageName);
            vertexes.put(packageName, vertex);
            id++;
        }

        for (String packageName : dependencies.keySet()) {
            Vertex<String> vertex = vertexes.get(packageName);

            for (String packageDependency : dependencies.get(packageName)) {

                Vertex<String> dependency = vertexes.get(packageDependency);
                vertex.addAdjacentVertex(new Edge<>(vertex, dependency), dependency);
                graph.addEdge(vertex.getId(), dependency.getId(), vertex.getData(), dependency.getData());
            }
        }

        return graph;
    }

    /**
     * Run Tarjan Algorithm to get cycles from dependencies graph between packages.
     * @param limit         Specifies nodes amount in the cycles.
     * @param isExactLimit  Specifies if {@code limit} is exact (if isn't exact limit is interpreted as a maximum limit).
     */
    public void runTarjan(int limit, boolean isExactLimit, String package1, String package2){

        long startTime = System.currentTimeMillis();

        Tarjan tj = new Tarjan();

        this.cycles = tj.findCycles(graph, limit, isExactLimit, package1, package2);
        this.cycleCompressor = tj.getCycleCompressor();
        this.numberOfCycles = tj.getNumberOfCycles();

        this.tarjanTime = System.currentTimeMillis() - startTime;
    }

    /**
     * To get compressed cycles.
     * @return  Byte's arrays {@code List} with compressed information about exist cycles.
     */
    public List<byte[]> getCycles(){
        return this.cycles;
    }

    /**
     * To get one decompressed cycle.
     * @param   cycle Cycle to decompress.
     * @return  {@code long}'s array with decompressed {@code cycle}.
     */
    public long[] decompressCycle(byte[] cycle){
        return this.cycleCompressor.decompress(cycle);
    }

    /**
     * To get the packages of the packages dependencies graph.
     * @return  {@code HashMap} with packages info. Each entry has package's ID as key and package name as value.
     */
    public HashMap<Long, String> getPackages(){
        return this.packages;
    }

    /**
     * To get the run time of Tarjan algorithm.
     * @return  {@code long} that represents time in seconds.
     */
    public long getTarjanTime(){
        return tarjanTime;
    }

    /**
     * To get the dependencies graph between packages.
     * @return  {@code Graph} that represents dependencies graph between packages.
     */
    public Graph<String> getGraph(){
        return this.graph;
    }

    /**
     * To get path of the '.odem' file analyzed.
     * @return  {@code String} that represents the file path.
     */
    public String getPath(){
        return this.path;
    }

    /**
     * To get the number of cycles.
     * @return  Number of cycles.
     */
    public int getNumberOfCycles(){
        return this.numberOfCycles;
    }

    /**
     * To get the number of dependencies.
     * @return  Number of dependencies.
     */
    public int getNumberOfDependencies(){
        return this.graph.getEdges().size();
    }

    /**
     * To get the number of packages.
     * @return  Number of packages.
     */
    public int getNumberOfPackages(){
        return this.graph.getVertexes().size();
    }
}
