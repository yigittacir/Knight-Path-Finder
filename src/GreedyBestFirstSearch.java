import java.util.*;

public class GreedyBestFirstSearch implements PathFinder {

    private static final int[][] MOVES = {
        {2,1},{2,-1},{-2,1},{-2,-1},
        {1,2},{1,-2},{-1,2},{-1,-2}
    };

    public List<Node> findPath(Board board, Node start, Node goal) {
        PriorityQueue<Node> pq = new PriorityQueue<>(
            Comparator.comparingInt(n -> Math.abs(n.row - goal.row) + Math.abs(n.col - goal.col))
        );

        Set<String> visited = new HashSet<>();
        pq.add(start);

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.row == goal.row && cur.col == goal.col)
                return build(cur);

            visited.add(cur.row + "," + cur.col);

            for (int[] m : MOVES) {
                int r = cur.row + m[0];
                int c = cur.col + m[1];
                if (board.isSafe(r, c) && !visited.contains(r + "," + c)) {
                    pq.add(new Node(r, c, cur));
                }
            }
        }
        return null;
    }

    private List<Node> build(Node n) {
        List<Node> path = new ArrayList<>();
        while (n != null) {
            path.add(0, n);
            n = n.parent;
        }
        return path;
    }
}
