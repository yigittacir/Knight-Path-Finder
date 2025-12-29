import java.util.*;

public class AStarSolver implements PathFinder {

    private static final int[] dr = {-2,-1,1,2,2,1,-1,-2};
    private static final int[] dc = {-1,-2,-2,-1,1,2,2,1};

    public List<Node> findPath(Board board, Node start, Node goal) {
        PriorityQueue<Node> open = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        start.gCost = 0;
        start.hCost = heuristic(start, goal);
        open.add(start);

        while (!open.isEmpty()) {
            Node cur = open.poll();

            if (cur.row == goal.row && cur.col == goal.col)
                return buildPath(cur);

            visited.add(cur.row + "," + cur.col);

            for (int i = 0; i < 8; i++) {
                int nr = cur.row + dr[i];
                int nc = cur.col + dc[i];

                if (!board.isSafe(nr, nc)) continue;

                if (visited.contains(nr + "," + nc)) continue;

                Node next = new Node(nr, nc, cur);
                next.gCost = cur.gCost + 1;
                next.hCost = heuristic(next, goal);
                open.add(next);
            }
        }
        return null;
    }

    private int heuristic(Node a, Node b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    private List<Node> buildPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node);
            node = node.parent;
        }
        return path;
    }
}
