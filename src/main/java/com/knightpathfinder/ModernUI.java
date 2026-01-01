package com.knightpathfinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ModernUI extends Application {

    private static final int TILE_SIZE = 65;
    private static final int BOARD_SIZE = 8;

    // --- MODERN COLOR PALETTE (Dracula/Neon Theme) ---
    private static final String CSS_BG = "#1E1E1E"; // Çok koyu gri arka plan
    private static final String CSS_PANEL = "#252526"; // Panel rengi
    private static final String CSS_ACCENT = "#007ACC"; // VS Code Mavisi
    private static final Color COL_TILE_LIGHT = Color.web("#3E3E42");
    private static final Color COL_TILE_DARK = Color.web("#2D2D30");
    private static final Color COL_START = Color.web("#4EC9B0"); // Turkuaz
    private static final Color COL_GOAL = Color.web("#FFD700"); // Altın
    private static final Color COL_BOMB = Color.web("#F44747"); // Soft Kırmızı
    private static final Color COL_PATH = Color.web("#C586C0"); // Mor (Neon)

    private Board board;
    private Node startNode;
    private Node goalNode;
    private Pane[][] cellViews = new Pane[BOARD_SIZE][BOARD_SIZE];

    private enum EditMode {
        SET_START, SET_GOAL, SET_BOMB
    }
    private EditMode currentMode = EditMode.SET_START;

    private TableView<AlgoResult> resultTable;
    private ObservableList<AlgoResult> tableData;

    @Override
    public void start(Stage stage) {
        board = new Board(BOARD_SIZE);
        startNode = new Node(0, 0, null);
        goalNode = new Node(7, 7, null);
        board.setCoin(7, 7);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + CSS_BG + ";");

        // --- SOL: KONTROL PANELİ ---
        VBox leftPanel = createControlPanel();
        root.setLeft(leftPanel);

        // --- MERKEZ: GRID ---
        StackPane centerContainer = new StackPane(createBoardGrid());
        centerContainer.setPadding(new Insets(20));
        root.setCenter(centerContainer);

        // --- ALT: SONUÇ TABLOSU ---
        VBox bottomPanel = createResultPanel();
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 1150, 850);

        // --- CSS ENJEKSİYONU ---
        scene.getStylesheets().add("data:text/css," + getInlineCSS());

        stage.setTitle("Knight Pathfinder");
        stage.setScene(scene);
        stage.show();

        updateBoardView();
    }

    // --- CSS STYLES ---
    private String getInlineCSS() {
        return ".root { -fx-font-family: 'Segoe UI', sans-serif; }"
                + ".label { -fx-text-fill: #CCCCCC; }"
                + ".button { "
                + "   -fx-background-color: #3C3C3C; -fx-text-fill: white; "
                + "   -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"
                + "   -fx-padding: 10 20; -fx-font-size: 14px;"
                + "} "
                + ".button:hover { -fx-background-color: #505050; }"
                + ".button:pressed { -fx-background-color: #007ACC; }"
                + ".run-btn { -fx-background-color: #007ACC; } "
                + ".run-btn:hover { -fx-background-color: #0098FF; }"
                + ".clear-btn { -fx-background-color: #D32F2F; } "
                + ".clear-btn:hover { -fx-background-color: #F44336; }"
                + ".radio-button { -fx-text-fill: #AAAAAA; -fx-padding: 5; }"
                + ".radio-button:selected { -fx-text-fill: white; }"
                + ".table-view { -fx-base: #252526; -fx-control-inner-background: #252526; -fx-background-color: #252526; }"
                + ".table-view .column-header-background { -fx-background-color: #333333; }"
                + ".table-view .column-header { -fx-background-color: transparent; }"
                + ".table-view .column-header .label { -fx-text-fill: white; -fx-font-weight: bold; }"
                + ".table-cell { -fx-text-fill: #DDDDDD; -fx-alignment: CENTER; -fx-border-color: transparent; }"
                + ".table-row-cell:odd { -fx-background-color: #2D2D30; }"
                + ".table-row-cell:even { -fx-background-color: #252526; }"
                + ".table-row-cell:selected { -fx-background-color: #004E8C; }";
    }

    private GridPane createBoardGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(3);
        grid.setVgap(3);

        // Hafif gölge efekti
        grid.setEffect(new DropShadow(20, Color.BLACK));

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Pane cell = new StackPane();
                cell.setPrefSize(TILE_SIZE, TILE_SIZE);

                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                rect.setArcWidth(10); // Yuvarlak köşeli kareler
                rect.setArcHeight(10);

                Text icon = new Text("");
                icon.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, 28));

                cell.getChildren().addAll(rect, icon);

                int finalR = r;
                int finalC = c;
                cell.setOnMouseClicked(e -> handleCellClick(finalR, finalC));

                cellViews[r][c] = cell;
                grid.add(cell, c, r);
            }
        }
        return grid;
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(25));
        panel.setPrefWidth(280);
        panel.setStyle("-fx-background-color: " + CSS_PANEL + "; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 0);");

        Label title = new Label("KNIGHT\nPATHFINDER");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white; -fx-line-spacing: -5;");

        // --- MODE SELECTION ---
        VBox modeBox = new VBox(10);
        modeBox.setStyle("-fx-background-color: #2D2D30; -fx-padding: 15; -fx-background-radius: 8;");

        Label lblTools = new Label("TOOLS");
        lblTools.setFont(Font.font(12));
        lblTools.setStyle("-fx-text-fill: #666;");

        ToggleGroup group = new ToggleGroup();
        RadioButton rbStart = new RadioButton("♞ Move Knight");
        rbStart.setToggleGroup(group);
        rbStart.setSelected(true);
        rbStart.setOnAction(e -> currentMode = EditMode.SET_START);

        RadioButton rbGoal = new RadioButton("💰 Move Coin");
        rbGoal.setToggleGroup(group);
        rbGoal.setOnAction(e -> currentMode = EditMode.SET_GOAL);

        RadioButton rbBomb = new RadioButton("💣 Place Bomb");
        rbBomb.setToggleGroup(group);
        rbBomb.setOnAction(e -> currentMode = EditMode.SET_BOMB);

        modeBox.getChildren().addAll(lblTools, rbStart, rbGoal, rbBomb);

        // --- ACTION BUTTONS ---
        Button runBtn = new Button("RUN ALGORITHMS");
        runBtn.getStyleClass().add("run-btn");
        runBtn.setMaxWidth(Double.MAX_VALUE);
        runBtn.setPrefHeight(45);
        runBtn.setOnAction(e -> runAllAlgorithms());

        Button clearBtn = new Button("CLEAR BOMBS");
        clearBtn.getStyleClass().add("clear-btn");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setOnAction(e -> clearBombs());

        panel.getChildren().addAll(title, new Separator(), modeBox, new Region(), runBtn, clearBtn);
        return panel;
    }

    private VBox createResultPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(0, 20, 20, 20));
        panel.setStyle("-fx-background-color: " + CSS_BG + ";");
        panel.setPrefHeight(250);

        Label lbl = new Label("Performance Results (Click row to visualize path)");
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        resultTable = new TableView<>();
        tableData = FXCollections.observableArrayList();
        resultTable.setItems(tableData);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AlgoResult, String> colName = new TableColumn<>("ALGORITHM");
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().name));

        TableColumn<AlgoResult, String> colFound = new TableColumn<>("STATUS");
        colFound.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().found ? "FOUND" : "FAILED"));
        // Status renklendirme
        colFound.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("FOUND")) {
                        setStyle("-fx-text-fill: #4EC9B0; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #F44747; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<AlgoResult, Number> colSteps = new TableColumn<>("STEPS");
        colSteps.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().steps));

        TableColumn<AlgoResult, String> colTime = new TableColumn<>("TIME");
        colTime.setCellValueFactory(d -> new SimpleStringProperty(
                String.format("%d ms / %d ns", d.getValue().timeMs, d.getValue().timeNs)
        ));

        resultTable.getColumns().addAll(colName, colFound, colSteps, colTime);

        resultTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.found) {
                drawPathOnBoard(newVal.path);
            }
        });

        panel.getChildren().addAll(lbl, resultTable);
        return panel;
    }

    // --- LOGIC ---
    private void handleCellClick(int r, int c) {
        switch (currentMode) {
            case SET_START:
                if (board.isSafe(r, c) || board.isBomb(r, c)) {
                    startNode = new Node(r, c, null);
                }
                break;
            case SET_GOAL:
                if (board.isSafe(r, c) || board.isBomb(r, c)) {
                    goalNode = new Node(r, c, null);
                }
                break;
            case SET_BOMB:
                if ((r == startNode.row && c == startNode.col) || (r == goalNode.row && c == goalNode.col)) {
                    return;
                }

                // BOMBA EKLEME/ÇIKARMA
                if (board.isBomb(r, c)) {
                    board.removeBomb(r, c); 
                }else {
                    board.setBomb(r, c);
                }
                break;
        }
        updateBoardView();
        // Bir değişiklik yapıldığında mevcut path çizimini temizle
        clearPathEffects();
    }

    private void runAllAlgorithms() {
        tableData.clear();
        clearPathEffects();
        board.setCoin(goalNode.row, goalNode.col);

        List<AlgoResult> results = new ArrayList<>();
        results.add(runAlgo("Breadth First Search (BFS)", new BFSSolver()));
        results.add(runAlgo("Depth First Search (DFS)", new DFSSolver()));
        results.add(runAlgo("A* (A-Star)", new AStarSolver()));
        results.add(runAlgo("Greedy Best First", new GreedyBestFirstSearch()));
        results.add(runAlgo("IDDFS", new IDDFS()));

        results.sort(Comparator.comparingLong(r -> r.timeNs));
        tableData.addAll(results);
    }

    private AlgoResult runAlgo(String name, PathFinder solver) {
        long start = System.nanoTime();
        List<Node> path = solver.findPath(board, startNode, goalNode);
        long end = System.nanoTime();

        long ns = end - start;
        return new AlgoResult(name, path != null, path != null ? path.size() - 1 : 0, ns / 1_000_000, ns, path);
    }

    private void updateBoardView() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Pane cell = cellViews[r][c];
                Text icon = (Text) cell.getChildren().get(1);
                Rectangle bg = (Rectangle) cell.getChildren().get(0);

                // Default
                bg.setFill((r + c) % 2 == 0 ? COL_TILE_LIGHT : COL_TILE_DARK);
                bg.setEffect(null); // Efektleri temizle
                icon.setText("");

                if (board.isBomb(r, c)) {
                    bg.setFill(COL_BOMB);
                    icon.setText("💣");
                }

                if (r == startNode.row && c == startNode.col) {
                    bg.setFill(COL_START);
                    icon.setText("♞");
                    icon.setFill(Color.WHITE);
                } else if (r == goalNode.row && c == goalNode.col) {
                    bg.setFill(COL_GOAL);
                    icon.setText("💰");
                }
            }
        }
    }

    private void drawPathOnBoard(List<Node> path) {
        updateBoardView(); // Reset board first
        if (path == null) {
            return;
        }

        for (int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            boolean isStart = (n.row == startNode.row && n.col == startNode.col);
            boolean isGoal = (n.row == goalNode.row && n.col == goalNode.col);

            if (!isStart && !isGoal) {
                Pane cell = cellViews[n.row][n.col];
                Rectangle bg = (Rectangle) cell.getChildren().get(0);

                bg.setFill(COL_PATH);
                // Glow Efekti ekleyelim
                DropShadow glow = new DropShadow();
                glow.setColor(COL_PATH);
                glow.setRadius(15);
                bg.setEffect(glow);

                // Adım numarasını küçük olarak yaz
                Text txt = (Text) cell.getChildren().get(1);
                txt.setText(String.valueOf(i));
                txt.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
                txt.setFill(Color.WHITE);
            }
        }
    }

    private void clearPathEffects() {
        updateBoardView();
    }

    private void clearBombs() {
        board = new Board(BOARD_SIZE);
        board.setCoin(goalNode.row, goalNode.col);
        updateBoardView();
        tableData.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class AlgoResult {

        String name;
        boolean found;
        int steps;
        long timeMs;
        long timeNs;
        List<Node> path;

        public AlgoResult(String n, boolean f, int s, long ms, long ns, List<Node> p) {
            name = n;
            found = f;
            steps = s;
            timeMs = ms;
            timeNs = ns;
            path = p;
        }
    }
}
