public class Node implements Comparable<Node> {
    int row, col;
    Node parent;
    int gCost;
    int hCost;

    public Node(int row, int col, Node parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getFCost(), other.getFCost());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) return false;
        Node o = (Node) obj;
        return row == o.row && col == o.col;
    }

    @Override
    public int hashCode() {
        return row * 31 + col;
    }
}
