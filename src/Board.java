public class Board {
    private int size;
    private int[][] grid;

    public Board(int size) {
        this.size = size;
        grid = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public boolean isSafe(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size && grid[r][c] != 1;
    }

    public void setBomb(int r, int c) {
        grid[r][c] = 1;
    }

    public void setCoin(int r, int c) {
        grid[r][c] = 2;
    }

    public void markPath(int r, int c) {
        if (grid[r][c] == 0)
            grid[r][c] = 9;
    }

    public void printBoard(int kr, int kc) {
        System.out.print("  ");
        for (int i = 1; i <= size; i++) System.out.print(i + " ");
        System.out.println();

        for (int r = 0; r < size; r++) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < size; c++) {
                if (r == kr && c == kc) System.out.print("K ");
                else if (grid[r][c] == 1) System.out.print("X ");
                else if (grid[r][c] == 2) System.out.print("C ");
                else if (grid[r][c] == 9) System.out.print("• ");
                else System.out.print("- ");
            }
            System.out.println();
        }
    }
}
