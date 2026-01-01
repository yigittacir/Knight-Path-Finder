import java.util.ArrayList;
import java.util.List;

public class DFSSolver implements PathFinder {

    private static final int[][] MOVES = {
        {2,1},{2,-1},{-2,1},{-2,-1},
        {1,2},{1,-2},{-1,2},{-1,-2}
    };

    @Override
    public List<Node> findPath(Board board, Node start, Node goal) {
        boolean[][] visited = new boolean[board.getSize()][board.getSize()];
        Node result = dfs(start.row, start.col, goal, board, visited, null);
        if (result == null) return null;

        List<Node> path = new ArrayList<>();
        while (result != null) {
            path.add(0, result);
            result = result.parent;
        }
        return path;
    }

    private Node dfs(int r, int c, Node goal, Board board,
                     boolean[][] visited, Node parent) {

        if (r == goal.row && c == goal.col)
            return new Node(r, c, parent);

        visited[r][c] = true;

        for (int[] m : MOVES) {
            int nr = r + m[0];
            int nc = c + m[1];

            if (board.isSafe(nr, nc) && !visited[nr][nc]) {
                Node res = dfs(nr, nc, goal, board, visited, new Node(r, c, parent));
                if (res != null) return res;
            }
        }
        return null;
    }
}
