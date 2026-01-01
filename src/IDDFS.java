import java.util.*;

public class IDDFS implements PathFinder {

    private Board board;
    private int goalRow, goalCol;
    private List<Node> path;

    private static final int[] dr = {2,2,-2,-2,1,1,-1,-1};
    private static final int[] dc = {1,-1,1,-1,2,-2,2,-2};

    @Override
    public List<Node> findPath(Board board, Node start, Node goal) {
        this.board = board;
        this.goalRow = goal.row;
        this.goalCol = goal.col;
        path = new ArrayList<>();

        for (int depth = 0; depth <= board.getSize() * board.getSize(); depth++) {
            Set<String> visited = new HashSet<>();
            Node res = dfs(start.row, start.col, depth, visited, null);
            if (res != null) {
                build(res);
                return path;
            }
        }
        return null;
    }

    private Node dfs(int r, int c, int depth, Set<String> visited, Node parent) {
        Node cur = new Node(r, c, parent);
        if (r == goalRow && c == goalCol) return cur;
        if (depth == 0) return null;

        visited.add(r + "," + c);

        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (board.isSafe(nr, nc) && !visited.contains(nr + "," + nc)) {
                Node res = dfs(nr, nc, depth - 1, visited, cur);
                if (res != null) return res;
            }
        }
        return null;
    }

    private void build(Node n) {
        while (n != null) {
            path.add(0, n);
            n = n.parent;
        }
    }
}
