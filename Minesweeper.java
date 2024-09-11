public class Minesweeper {

    // Data members
    private char[][] board;   // The game board where cells will be displayed
    private boolean[][] mines; // Array to track the locations of mines
    private boolean[][] revealed; // Array to track which cells have been revealed
    private int rows; // Number of rows in the board
    private int cols; // Number of columns in the board
    private int numMines; // Number of mines in the game
    private boolean gameOver; // Boolean to check if the game is over

    // Constructor to initialize the board with the specified dimensions and number of mines
    public Minesweeper(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        this.board = new char[rows][cols];
        this.mines = new boolean[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.gameOver = false;

        initializeBoard();
        placeMines();
        calculateNumbers();
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean status) {
        this.gameOver = status;
    }

    // Method to initialize the game board with empty values
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '-'; // Unrevealed cell
                revealed[i][j] = false; // No cell has been revealed initially
            }
        }
    }

    // Method to randomly place mines on the board
    private void placeMines() {
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);

            if (!mines[row][col]) { // If no mine is placed here yet
                mines[row][col] = true;
                minesPlaced++;
            }
        }
    }

    // Method to calculate numbers on the board for non-mine cells
    private void calculateNumbers() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!mines[i][j]) {
                    int adjacentMines = countAdjacentMines(i, j);
                    board[i][j] = (adjacentMines > 0) ? (char) ('0' + adjacentMines) : '0';
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(cols - 1, col + 1); j++) {
                if (mines[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Method to display the current state of the board
    public void displayBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (revealed[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    // Method to handle a player's move (reveal a cell or place a flag)
    public void playerMove(int row, int col, String action) {
        if (action.equals("reveal")) {
            revealCell(row, col);
        } else if (action.equals("flag")) {
            flagCell(row, col);
        } else if (action.equals("unflag")) {
            unflagCell(row, col);
        }
    }

    // Method to check if the player has won the game
    public boolean checkWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!mines[i][j] && !revealed[i][j]) {
                    return false; // If any non-mine cell is not revealed, the player hasn't won
                }
            }
        }
        return true; // All non-mine cells revealed
    }

    // Method to check if the player has lost the game
    public boolean checkLoss(int row, int col) {
        return mines[row][col]; // Game over if the player reveals a mine
    }

    // Method to reveal a cell (and adjacent cells if necessary)
    private void revealCell(int row, int col) {
        if (revealed[row][col] || mines[row][col]) {
            return; // Don't reveal if already revealed or if it's a mine
        }

        revealed[row][col] = true; // Reveal the cell
        if (board[row][col] == '0') {
            // If the cell has 0 adjacent mines, reveal its neighbors
            for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
                for (int j = Math.max(0, col - 1); j <= Math.min(cols - 1, col + 1); j++) {
                    if (!revealed[i][j]) {
                        revealCell(i, j); // Recursively reveal neighboring cells
                    }
                }
            }
        }
    }

    // Method to flag a cell as containing a mine
    private void flagCell(int row, int col) {
        if (!revealed[row][col]) {
            board[row][col] = 'F'; // Flag as a mine
        }
    }

    // Method to unflag a cell
    private void unflagCell(int row, int col) {
        if (board[row][col] == 'F') {
            board[row][col] = '-'; // Unflag the cell
        }
    }
}
