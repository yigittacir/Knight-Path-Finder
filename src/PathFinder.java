import java.util.List;

public interface PathFinder {
    List<Node> findPath(Board board, Node start, Node goal);
}
