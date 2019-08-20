package cycles.services;

import cycles.algorithm.*;
import cycles.utils.SAXHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;

public class CyclesService {
    private String path;
    private Map<String, Set<String>> dependencies;
    private Graph<String> graph;
    private List<byte[]> cycles;
    private long tarjanTime;
    private CycleCompressor cycleCompressor;
    private HashMap<Long, String> vertexes;
    private int numberOfCycles;


    public CyclesService(String path){
        this.path = path;
        this.dependencies = new HashMap<String, Set<String>>();
        this.graph = new Graph<>(true);
        this.cycles = new ArrayList<>();
        this.tarjanTime = 0;
        this.vertexes = new HashMap<>();
        this.numberOfCycles = 0;
    }

    public void runSAX(){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            SAXParser saxParser = factory.newSAXParser();

            File inputFile = new File(this.path);
            SAXHandler handler = new SAXHandler();
            saxParser.parse(inputFile, handler);

            this.dependencies = transform(handler.getClasses(), handler.getClass_pack());
            this.graph = buildGraph(dependencies, true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void runTarjan(int limit, boolean isExactLimit){

        long startTime = System.currentTimeMillis();

        Tarjan tj = new Tarjan();

        this.cycles = tj.findCycles(graph, limit, isExactLimit);
        this.cycleCompressor = tj.getCycleCompressor();
        this.numberOfCycles = tj.getNumberOfCycles();

        this.tarjanTime = System.currentTimeMillis() - startTime;
    }

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

    private Graph<String> buildGraph(Map<String, Set<String>> dependencies, boolean isDirected) {
        Graph<String> graph = new Graph<>(isDirected);
        long id = 0;

        Map<String, Vertex<String>> vertexes = new HashMap<>();

        for (String packageName : dependencies.keySet()) {
            Vertex<String> vertex = new Vertex<>(id);
            vertex.setData(packageName);
            this.vertexes.put(id, packageName);
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

    public List<byte[]> getCycles(){
        return this.cycles;
    }

    public long[] decompressCycle(byte[] cycle){
        return this.cycleCompressor.decompress(cycle);
    }

    public HashMap<Long, String> getVertexes(){
        return this.vertexes;
    }

    public int getNumberOfCycles(){
        return this.numberOfCycles;
    }
}
