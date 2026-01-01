import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Board board = new Board(8);

        System.out.println("=== KNIGHT PATHFINDING COMPARISON ===");

        System.out.print("Knight Start Column (1-8): ");
        int scCol = sc.nextInt() - 1;
        System.out.print("Knight Start Row (1-8): ");
        int scRow = sc.nextInt() - 1;

        System.out.print("Coin Column (1-8): ");
        int gc = sc.nextInt() - 1;
        System.out.print("Coin Row (1-8): ");
        int gr = sc.nextInt() - 1;

        System.out.print("How many bombs? ");
        int bombCount = sc.nextInt();

        for (int i = 0; i < bombCount; i++) {
            System.out.print("Bomb " + (i + 1) + " Column: ");
            int c = sc.nextInt() - 1;
            System.out.print("Bomb " + (i + 1) + " Row: ");
            int r = sc.nextInt() - 1;
            board.setBomb(r, c);
        }

        Node start = new Node(scRow, scCol, null);
        Node goal = new Node(gr, gc, null);

        board.printBoard(scRow, scCol);

        List<Result> results = new ArrayList<>();

        results.add(run("BFS", new BFSSolver(), board, start, goal));
        results.add(run("DFS", new DFSSolver(), board, start, goal));
        results.add(run("A*", new AStarSolver(), board, start, goal));
        results.add(run("Greedy", new GreedyBestFirstSearch(), board, start, goal));
        results.add(run("IDDFS", new IDDFS(), board, start, goal));

        System.out.println("\n=========== FINAL COMPARISON ===========");
        System.out.printf("%-15s %-8s %-8s %-10s %-12s\n",
                "Algorithm", "Found", "Steps", "ms", "ns");

        results.sort(Comparator.comparingLong(r -> r.timeNs));

        for (Result r : results) {
            System.out.printf("%-15s %-8s %-8d %-10d %-12d\n",
                    r.name, r.found ? "YES" : "NO",
                    r.steps, r.timeMs, r.timeNs);
        }
    }

    static class Result {
        String name;
        boolean found;
        int steps;
        long timeMs, timeNs;

        Result(String n, boolean f, int s, long ms, long ns) {
            name = n;
            found = f;
            steps = s;
            timeMs = ms;
            timeNs = ns;
        }
    }

    private static Result run(String name, PathFinder solver,
                              Board board, Node start, Node goal) {

        long t1 = System.nanoTime();
        List<Node> path = solver.findPath(board, start, goal);
        long t2 = System.nanoTime();

        long ns = t2 - t1;
        long ms = ns / 1_000_000;

        System.out.println("\n---- " + name + " ----");

        if (path == null) {
            System.out.println("No path found");
            return new Result(name, false, 0, ms, ns);
        }

        System.out.println("Steps: " + (path.size() - 1));
        System.out.println("Time: " + ms + " ms");
        System.out.println("Time: " + ns + " ns");

        System.out.print("Path: ");
        for (Node n : path)
            System.out.print("(" + (n.col + 1) + "," + (n.row + 1) + ") ");
        System.out.println();

        return new Result(name, true, path.size() - 1, ms, ns);
    }
}
