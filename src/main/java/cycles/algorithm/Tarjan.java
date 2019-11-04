package cycles.algorithm;

import cycles.services.ServicesForControllers;

import java.util.*;

public class Tarjan {

    private static int LIMIT_MIN = 2;
    private Set<Vertex<String>> visited;
    private Deque<Vertex<String>> pointStack;
    private Deque<Vertex<String>> markedStack;
    private Set<Vertex<String>> markedSet;
    private CycleCompressor cycleCompressor;
    private int numberOfCycles;

    public Tarjan() {
        reset();
        numberOfCycles = 0;
    }

    private void reset() {
        visited = new HashSet<>();
        pointStack = new LinkedList<>();
        markedStack = new LinkedList<>();
        markedSet = new HashSet<>();
    }

    public List<byte[]> findCycles(Graph<String> graph, int limit, boolean isExactLimit, String package1, String package2) {
        reset();
        this.cycleCompressor = new CycleCompressor(graph.getVertexes().size());

        List<byte[]> result = new ArrayList<>();

        for(Vertex<String> vertex : graph.getVertexes()) {
            findCycles(vertex, vertex, result, limit, isExactLimit, package1, package2);
            visited.add(vertex);

            while(!markedStack.isEmpty()) {
                markedSet.remove(markedStack.pollFirst());
            }
        }

        return result;
    }

    private boolean findCycles(Vertex start, Vertex<String> current, List<byte[]> result, int limit, boolean isExactLimit, String package1, String package2) {

        if (pointStack.size() == limit)
            return true;

        boolean hasCycle = false;
        pointStack.offerFirst(current);
        markedSet.add(current);
        markedStack.offerFirst(current);


        for (Vertex<String> adjacent : current.getAdjacent()) {

            if (visited.contains(adjacent)) {
                continue;

            } else if ( (adjacent.equals(start) )) {

                hasCycle = true;

                if (this.isValidCycle(isExactLimit, limit)){

                    pointStack.offerFirst(adjacent);
                    List<Vertex<String>> cycle = new ArrayList<>();
                    Iterator<Vertex<String>> itr = pointStack.descendingIterator();

                    while (itr.hasNext()) {
                        cycle.add(itr.next());
                    }

                    if (this.isContainsPackages(cycle, package1, package2)){
                        this.numberOfCycles++;
                        result.add(this.cycleCompressor.compress(cycle));
                    }

                    pointStack.pollFirst();
                }


            } else if (!markedSet.contains(adjacent)) {
                hasCycle = findCycles(start, adjacent, result, limit, isExactLimit, package1, package2) || hasCycle;
            }
        }

        if (hasCycle) {
            while(!markedStack.peekFirst().equals(current)) {
                markedSet.remove(markedStack.pollFirst());
            }
            markedSet.remove(markedStack.pollFirst());
        }

        pointStack.pollFirst();
        return hasCycle;
    }

    public CycleCompressor getCycleCompressor() {
        return this.cycleCompressor;
    }

    public int getNumberOfCycles(){
        return this.numberOfCycles;
    }

    private boolean isValidCycle(boolean isExactLimit, int limit){
        return  ((isExactLimit && pointStack.size() > LIMIT_MIN && pointStack.size() == limit) ||
                (!isExactLimit && pointStack.size() > LIMIT_MIN && pointStack.size() <= limit));
    }

    private boolean isContainsPackages(List<Vertex<String>> cycle, String package1, String package2){
        if (package1.equals(ServicesForControllers.NO_PACKAGE )&& package2.equals(ServicesForControllers.NO_PACKAGE ))
            return true;

        boolean isPackage1 = false;
        boolean isPackage2 = false;


        for (Vertex<String> adjacent : cycle) {
            String currentPackage = adjacent.getData();

            if (currentPackage.equals(package1))   isPackage1 = true;
            if (currentPackage.equals(package2))   isPackage2 = true;


            if (isPackage1 && isPackage2)
                return true;
        }

        return  false;
    }
}