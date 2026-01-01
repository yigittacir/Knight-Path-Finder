import java.util.*;

public class BFSSolver implements PathFinder {

    private static final int[][] MOVES = {
        {2,1},{2,-1},{-2,1},{-2,-1},
        {1,2},{1,-2},{-1,2},{-1,-2}
    };

    public List<Node> findPath(Board board, Node start, Node goal) {
        Queue<Node> q = new LinkedList<>();
        boolean[][] visited = new boolean[board.getSize()][board.getSize()];
        q.add(start);
        visited[start.row][start.col] = true;

        while (!q.isEmpty()) {
            Node cur = q.poll();
            if (cur.row == goal.row && cur.col == goal.col)
                return build(cur);

            for (int[] m : MOVES) {
                int r = cur.row + m[0];
                int c = cur.col + m[1];
                if (board.isSafe(r, c) && !visited[r][c]) {
                    visited[r][c] = true;
                    q.add(new Node(r, c, cur));
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
