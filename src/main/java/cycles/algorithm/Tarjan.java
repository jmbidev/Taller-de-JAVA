package cycles.algorithm;

import java.util.*;

public class Tarjan {

    private static int LIMIT_MIN = 2;
    private Set<Vertex<String>> visited;
    private Deque<Vertex<String>> pointStack;
    private Deque<Vertex<String>> markedStack;
    private Set<Vertex<String>> markedSet;
    private int nbits;
    private CycleCompressor cycleCompressor;
    private int numberOfCycles;
    private String package1;
    private String package2;

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

    public List<byte[]> findCycles(Graph<String> graph, int limit, boolean isExactLimit) {
        reset();
        this.cycleCompressor = new CycleCompressor(graph.getVertexes().size());

        List<byte[]> result = new ArrayList<>();

        for(Vertex<String> vertex : graph.getVertexes()) {
            findCycles(vertex, vertex, result, limit, isExactLimit);
            visited.add(vertex);

            while(!markedStack.isEmpty()) {
                markedSet.remove(markedStack.pollFirst());
            }
        }

        return result;
    }

    private boolean findCycles(Vertex start, Vertex<String> current, List<byte[]> result, int limit, boolean isExactLimit) {
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

                if ((isExactLimit && pointStack.size() > LIMIT_MIN && pointStack.size() == limit) ||
                        (!isExactLimit && pointStack.size() > LIMIT_MIN && pointStack.size() <= limit)){

                    pointStack.offerFirst(adjacent);
                    List<Vertex<String>> cycle = new ArrayList<>();
                    Iterator<Vertex<String>> itr = pointStack.descendingIterator();

                    while (itr.hasNext()) {
                        cycle.add(itr.next());
                    }

                    this.numberOfCycles++;
                    result.add(this.cycleCompressor.compress(cycle));

                    pointStack.pollFirst();
                }


            } else if (!markedSet.contains(adjacent)) {
                hasCycle = findCycles(start, adjacent, result, limit, isExactLimit) || hasCycle;
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
}