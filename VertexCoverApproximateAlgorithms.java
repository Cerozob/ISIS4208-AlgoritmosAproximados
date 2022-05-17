import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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

        @Override
        public void parseInput(VertexCoverApproximateAlgorithms.Edge[] edges) {
            // TODO Auto-generated method stub

        }

        @Override
        public void solveVertexCover() {
            // TODO Auto-generated method stub

        }
    }

    private class Algorithm3 extends ApproximateAlgorithm {

        public boolean[][] input;

        @Override
        public void parseInput(VertexCoverApproximateAlgorithms.Edge[] edges) {
            Integer leftid = null;
            Integer rightid = null;
            int max = 0;
            for (Edge e : edges) {
                leftid = e.left;
                rightid = e.right;
                max = Math.max(leftid, Math.max(rightid, max));
            }
            input = new boolean[max + 1][max + 1];
            for (Edge e : edges) {
                leftid = e.left;
                rightid = e.right;
                input[leftid][rightid] = true;
                input[rightid][leftid] = true;
            }
        }

        @Override
        public void solveVertexCover() {

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
                    instance.runTestCases(Integer.parseInt(args[1]));

                    break;
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
        ApproximateAlgorithm algorithm1 = new Algorithm1();
        ApproximateAlgorithm algorithm2 = new Algorithm2();
        ApproximateAlgorithm algorithm3 = new Algorithm3();
        ApproximateAlgorithm algorithm4 = new Algorithm4();
        int[] sizes = { 100, 1000, 10000 };
        StringBuilder sb = new StringBuilder();
        sb.append("algoritmo\ttiempo ejecucion\tcaseid\tconteo\n");
        for (int i = 0; i < n; i++) {
            for (int size : sizes) {
                Edge[] testInput = generateGraph(size);
                algorithm1.parseInput(testInput);
                algorithm2.parseInput(testInput);
                algorithm3.parseInput(testInput);
                algorithm4.parseInput(testInput);
                Long timeStartAlg1 = System.nanoTime();
                algorithm1.solveVertexCover();
                Long timeEndAlg1 = System.nanoTime();
                Long timeDiffAlg1 = timeEndAlg1 - timeStartAlg1;
                Long timeStartAlg2 = System.nanoTime();
                algorithm2.solveVertexCover();
                Long timeEndAlg2 = System.nanoTime();
                Long timeDiffAlg2 = timeEndAlg2 - timeStartAlg2;
                Long timeStartAlg3 = System.nanoTime();
                algorithm3.solveVertexCover();
                Long timeEndAlg3 = System.nanoTime();
                Long timeDiffAlg3 = timeEndAlg3 - timeStartAlg3;
                Long timeStartAlg4 = System.nanoTime();
                algorithm4.solveVertexCover();
                Long timeEndAlg4 = System.nanoTime();
                Long timeDiffAlg4 = timeEndAlg4 - timeStartAlg4;
                // 1 --------------------
                sb.append(1);
                sb.append("\t");
                sb.append(timeDiffAlg1);
                sb.append("\t");
                sb.append(i);
                sb.append("\tconteo");
                sb.append(algorithm1.getCount());
                sb.append("\n");
                // 2 --------------------
                sb.append(2);
                sb.append("\t");
                sb.append(timeDiffAlg2);
                sb.append("\t");
                sb.append(i);
                sb.append("\tconteo");
                sb.append(algorithm2.getCount());
                sb.append("\n");
                // 3 --------------------
                sb.append(3);
                sb.append("\t");
                sb.append(timeDiffAlg3);
                sb.append("\t");
                sb.append(i);
                sb.append("\tconteo");
                sb.append(algorithm3.getCount());
                sb.append("\n");
                // 4 --------------------
                sb.append(4);
                sb.append("\t");
                sb.append(timeDiffAlg4);
                sb.append("\t");
                sb.append(i);
                sb.append("\tconteo");
                sb.append(algorithm4.getCount());
                sb.append("\n");
                System.out.println(sb.toString());
                sb.setLength(0);
            }
        }

    }
}