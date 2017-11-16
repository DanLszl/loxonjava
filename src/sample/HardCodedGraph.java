package sample;

import java.util.*;

public class HardCodedGraph {

    private static Map<Integer, List<Integer>> neighbours;

    static {

        neighbours = new HashMap<>();

        neighbours.put(101, Arrays.asList(103, 105, 107));
        neighbours.put(102, Arrays.asList(108, 106, 104));
        neighbours.put(103, Arrays.asList(101, 105, 110));
        neighbours.put(104, Arrays.asList(106, 102, 109));
        neighbours.put(105, Arrays.asList(103, 101, 106, 107, 110, 108));

        neighbours.put(106, Arrays.asList(108, 107, 105, 109, 104, 102));
        neighbours.put(107, Arrays.asList(108, 105, 101, 106, 109));
        neighbours.put(108, Arrays.asList(110, 102, 106, 105, 107));
        neighbours.put(109, Arrays.asList(104, 106, 107));
        neighbours.put(110, Arrays.asList(103, 105, 108));
    }


    private static void checkSelf() {

        Set<Map.Entry<Integer, List<Integer>>> entries = neighbours.entrySet();

        for (Map.Entry<Integer, List<Integer>> entry : entries) {
            int key = entry.getKey();

            for (int neighbour : entry.getValue()) {
                if (neighbour == key)
                    throw new RuntimeException("Self matching: " + neighbour);
            }
        }
    }

    private static void checkNeighbour() {
        Set<Map.Entry<Integer, List<Integer>>> entries = neighbours.entrySet();

        for (Map.Entry<Integer, List<Integer>> entry : entries) {
            int key = entry.getKey();

            for (int neighbour : entry.getValue()) {
                List<Integer> backRef = neighbours.get(neighbour);

                try {
                    if (!backRef.contains(key))
                        throw new RuntimeException("No backreference: " + key + " to " + neighbour);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Key error: " + backRef);
                }
            }
        }
    }

    public static void main(String[] args) {
        checkSelf();
        checkNeighbour();
    }


    static Map<Integer, List<Integer>> getNeighbours() {
        return neighbours;
    }

}
