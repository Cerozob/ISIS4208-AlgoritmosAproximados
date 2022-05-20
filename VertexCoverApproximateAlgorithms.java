import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VertexCoverApproximateAlgorithms {

    private class Edge {

        public int left;
        public int right;

        public int other(int i) {
            return (i == left) ? right : left;
        }

        public Edge(int pLeft, int pRight) {
            left = pLeft;
            right = pRight;
        }
    }

    private abstract class ApproximateAlgorithm {

        public int count;
        public int[] solution;

        public abstract void parseInput(Edge[] edges);

        public abstract void solveVertexCover();

        public int getCount() {
            if (solution.length == 0) {
                solveVertexCover();
            }
            count = solution.length;
            return count;
        }

        // vertices separados por coma TAB conteo
        public String parseSolution() {
            StringBuilder sb = new StringBuilder();
            if (solution.length == 0) {
                solveVertexCover();
            }
            for (int i : solution) {
                sb.append(i);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\t");
            sb.append(getCount());
            return sb.toString();
        }

        public String solveIndividual(Edge[] edges) {
            parseInput(edges);
            solveVertexCover();
            return parseSolution();
        }

    }

    private class Algorithm1 extends ApproximateAlgorithm {

        public Edge[] input;

        @Override
        public void parseInput(VertexCoverApproximateAlgorithms.Edge[] edges) {
            input = edges;
            // Collections.shuffle(input);
        }

        @Override
        public void solveVertexCover() {
            HashMap<Integer, Integer> solutionMap = new HashMap<>();
            for (int i = 0; i < input.length; i++) {
                Edge currentEdge = input[i];
                if (!(solutionMap.containsKey(currentEdge.left) || solutionMap.containsKey(currentEdge.right))) {
                    solutionMap.put(currentEdge.left, 1);
                    solutionMap.put(currentEdge.right, 1);
                }
            }
            solution = solutionMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        }
    }

    private class Algorithm2 extends ApproximateAlgorithm {

        public HashMap<Integer, List<Integer>> input;

        @Override
        public void parseInput(VertexCoverApproximateAlgorithms.Edge[] edges) {
            Integer leftid = null;
            Integer rightid = null;
            input = new HashMap<>();
            for (Edge e : edges) {
                leftid = e.left;
                rightid = e.right;
                if (!input.containsKey(leftid)) {
                    input.put(leftid, new LinkedList<Integer>());

                }
                if (!input.containsKey(rightid)) {
                    input.put(rightid, new LinkedList<Integer>());
                }
                input.get(leftid).add(rightid);
                input.get(rightid).add(leftid);
            }
        }

        public int rank(int v) {
            return input.get(v).size();
        }

        public int findHighestRank() {
            int max = -1;
            int id = -1;
            for (Map.Entry<Integer, List<Integer>> entry : input.entrySet()) {
                int size = entry.getValue().size();
                int currentid = entry.getKey();
                if (size >= max) {
                    max = size;
                    id = currentid;
                }
            }
            return id;
        }

        public void discardEdges(int v) {
            input.remove(v);
            for (Map.Entry<Integer, List<Integer>> lista : input.entrySet()) {
                if (lista.getValue().contains(v)) {
                    lista.getValue().remove(lista.getValue().indexOf(v));
                }
            }
        }

        @Override
        public void solveVertexCover() {
            ArrayList<Integer> sol = new ArrayList<>();
            while (!input.isEmpty()) {
                int added = findHighestRank();
                sol.add(added);
                // borre sus ejes
                discardEdges(added);
            }
            solution = sol.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    private class Algorithm3 extends ApproximateAlgorithm {

        public HashMap<Integer, List<Integer>> input;

        @Override
        public void parseInput(VertexCoverApproximateAlgorithms.Edge[] edges) {
            Integer leftid = null;
            Integer rightid = null;
            input = new HashMap<>();
            for (Edge e : edges) {
                leftid = e.left;
                rightid = e.right;
                if (!input.containsKey(leftid)) {
                    input.put(leftid, new LinkedList<Integer>());

                }
                if (!input.containsKey(rightid)) {
                    input.put(rightid, new LinkedList<Integer>());
                }
                input.get(leftid).add(rightid);
                input.get(rightid).add(leftid);
            }
        }

        public int rank(int v) {

            return input.get(v).size();
        }

        public int findHighestRank(int v, int w) {
            return (rank(v) >= rank(w)) ? v : w;
        }

        public void discardEdges(int v) {
            input.remove(v);
            ArrayList<Integer> markforDeletion = new ArrayList<>();
            for (Integer index : input.keySet()) {
                while (input.get(index).contains(v)) {
                    input.get(index).remove(input.get(index).indexOf(v));
                }
                if (input.get(index).isEmpty()) {
                    markforDeletion.add(index);
                }
            }
            markforDeletion.forEach(i -> input.remove(i));
        }

        public int[] findRandomEdge(Random r) {
            int[] vertices = input.keySet().stream().mapToInt(Integer::intValue).toArray();
            int edgeLeft = vertices[r.nextInt(vertices.length)];
            int edgeRight = input.get(edgeLeft).get(r.nextInt(input.get(edgeLeft).size()));
            int[] edge = { edgeLeft, edgeRight };
            return edge;
        }

        @Override
        public void solveVertexCover() {
            Random r = new Random();
            ArrayList<Integer> sol = new ArrayList<>();
            while (!input.isEmpty()) {
                // encuentre uno random
                int[] randomEdge = findRandomEdge(r);
                // agregue el de mayor rango
                int added = findHighestRank(randomEdge[0], randomEdge[1]);
                sol.add(added);
                // borre sus ejes
                discardEdges(added);
            }
            solution = sol.stream().mapToInt(Integer::intValue).toArray();
            return;
        }

    }

    private class Algorithm4 extends ApproximateAlgorithm {

        public LinkedList<Edge> input;

        @Override
        public void parseInput(VertexCoverApproximateAlgorithms.Edge[] edges) {
            input = new LinkedList<Edge>(Arrays.asList(edges));
            Collections.shuffle(input);
        }

        @Override
        public void solveVertexCover() {
            HashMap<Integer, Integer> solutionMap = new HashMap<>();
            Random random = new Random();
            while (!input.isEmpty()) {
                Edge currentEdge = input.pop();
                if (!(solutionMap.containsKey(currentEdge.left) || solutionMap.containsKey(currentEdge.right))) {
                    if (random.nextBoolean()) {
                        solutionMap.put(currentEdge.left, 1);
                    } else {
                        solutionMap.put(currentEdge.right, 1);
                    }
                }
            }
            solution = solutionMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        }
    }

    public static void main(String[] args) {
        VertexCoverApproximateAlgorithms instance = new VertexCoverApproximateAlgorithms();
        try {
            // 0 = test mode
            // 1 = algoritmo 1
            // 2 = algoritmo 2
            // 3 = algoritmo 3
            // 4 = algoritmo 4
            int algorithmNumber = (args.length > 0) ? Integer.parseInt(args[0]) : 0;
            if (algorithmNumber != 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                Edge[] edges = parseGraphInput(instance, br);
                ApproximateAlgorithm algorithm;
                switch (algorithmNumber) {
                    case 1:
                        algorithm = instance.new Algorithm1();
                        System.out.println(algorithm.solveIndividual(edges));
                        break;
                    case 2:
                        algorithm = instance.new Algorithm2();
                        System.out.println(algorithm.solveIndividual(edges));
                        break;
                    case 3:
                        algorithm = instance.new Algorithm3();
                        System.out.println(algorithm.solveIndividual(edges));
                        break;
                    case 4:
                        algorithm = instance.new Algorithm4();
                        System.out.println(algorithm.solveIndividual(edges));
                        break;
                    default:
                        System.out.println("wut");
                        break;
                }
            } else {
                int testCases = (args.length > 1) ? Integer.parseInt(args[1]) : 10;
                instance.runTestCases(testCases);
            }
        } catch (IOException e) {
            System.out.println("Error leyendo la entrada");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("error leyendo la línea");
            e.printStackTrace();
            throw e;
        }

    }

    private static Edge[] parseGraphInput(VertexCoverApproximateAlgorithms instance, BufferedReader br)
            throws IOException {
        String line;
        ArrayList<Edge> edgesArrayList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] edge = line.split("\t");
            int left = Integer.parseInt(edge[0]);
            int right = Integer.parseInt(edge[1]);
            edgesArrayList.add(instance.new Edge(left, right));
        }
        Edge[] edges = edgesArrayList.toArray(new Edge[edgesArrayList.size()]);
        return edges;
    }

    public Edge[] generateGraph(int size) {
        String[] command = { "python", "graphGenerator.py", Integer.toString(size) };

        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            Process process = pb.start();
            InputStream in = process.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            return parseGraphInput(this, br);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void runTestCases(int n) {

        int[] sizes = { 10000 };
        System.out.println("algoritmo\ttiempo ejecucion\tcaseid\tconteo");
        for (int size : sizes) {
            for (int i = 1; i < n + 1; i++) {
                Edge[] testInput = generateGraph(size);
                Runner r1 = new Runner(new Algorithm1(), testInput, i, size);
                Runner r2 = new Runner(new Algorithm2(), testInput, i, size);
                Runner r3 = new Runner(new Algorithm3(), testInput, i, size);
                Runner r4 = new Runner(new Algorithm4(), testInput, i, size);
                r1.start();
                r4.start();
                r2.start();
                r3.start();
            }
        }

    }

    private class Runner extends Thread {
        ApproximateAlgorithm algorithm;
        Edge[] input;
        int caseid;
        int caseSize;

        public Runner(ApproximateAlgorithm instance, Edge[] pInput, int pcaseid, int pcaseSize) {
            algorithm = instance;
            input = pInput;
            caseid = pcaseid;
            caseSize = pcaseSize;
        }

        public void run() {
            StringBuilder sb = new StringBuilder();
            algorithm.parseInput(input);
            long timeStartAlg1 = System.nanoTime();
            algorithm.solveVertexCover();
            long timeEndAlg1 = System.nanoTime();
            long timeDiffAlg1 = timeEndAlg1 - timeStartAlg1;
            sb.append(algorithm.getClass().getSimpleName());
            sb.append("\t");
            sb.append(timeDiffAlg1);
            sb.append("\t");
            sb.append(caseid + ";" + caseSize);
            sb.append("\t");
            sb.append(algorithm.getCount());
            sb.append("\n");
            writetoOutput(sb.toString());
            return;
        }

        public synchronized void writetoOutput(String s) {
            System.out.print(s);
        }
    }
}