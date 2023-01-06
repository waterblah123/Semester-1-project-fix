import java.awt.*;
import javax.swing.*;
import java.util.Scanner;

public class GameBoard extends JPanel {
    private JFrame myFrame;
    private int columns = 7;
    private int colLength = 6;
    public Coin[][] board = new Coin[7][6];
    Scanner sc = new Scanner(System.in);

    int coinDiameter = 70;
    int cageThickness = 35;
    int textAreaHeight = 250;
    int boardHeight = 7 * cageThickness + 6 * coinDiameter;
    int windowWidth = 8 * cageThickness + 7 * coinDiameter;
    int windowHeight = 7 * cageThickness + 6 * coinDiameter + textAreaHeight;

    int globalColor; // red starts game

    // constructor
    public GameBoard() {
        myFrame = new JFrame("Connect Four!");
        myFrame.add(this);
        myFrame.setSize(windowWidth, windowHeight + cageThickness);
        myFrame.setVisible(true);
    }

    public void runGame() {
        int numTurns = 0;
        boolean continueGame = true;

        while (continueGame) {
            numTurns++;
            globalColor = numTurns % 2;

            askUser(globalColor);
            repaint();

            if (hasWon(globalColor)) {
                System.out.print("Good Game! ");
                if (globalColor == 0)
                    System.out.print("Yellow ");
                else
                    System.out.print("Red ");
                    
                System.out.println("has won in " + numTurns + " turns.");

                continueGame = false;
            }

            else if (boardIsFull()) {
                System.out.println("Tie Game! The game has concluded in " + numTurns + " turns.");
                continueGame = false;
            }
        }
    }

    // drops coin based on user input x
    public void askUser(int color) {
        boolean coindropped = false;
        while (!coindropped) {
            System.out.print("Choose a column 1-7 to drop your coin: ");
            int xVal = Integer.parseInt(sc.nextLine()) - 1; // parseint turns String --> int
            if (xVal < 0 || xVal > 6) { //if out of bounds
                System.out.println("Input a valid column value 1-7");
            } else if (!dropCoin(xVal, color)) {
                System.out.println("Choose a column that is not filled");
            } else {
                coindropped = true;
            }
        }
    }

    // returns false if invalid, true if valid
    public boolean dropCoin(int x, int color) {
        for (int i = 0; i < 6; i++) {
            if (board[x][i] == null) {
                board[x][i] = new Coin(color, x, i);
                return true;
            }
        }
        return false;
    }

    public void paintComponent(Graphics g) {
        super.repaint(); // don't worry about this -- it 'clears the screen'

        g.setColor(Color.black);
        g.fillRect(0, 0, windowWidth, windowHeight); // draw a black background

        drawCage(g);
        drawCoins(g);
        drawText(g, globalColor);
    }

    public void drawCage(Graphics g) {
        g.setColor(Color.blue);

        // draw horizontal cage rectangles
        for (int i = 0; i < 7; i++) {
            g.fillRect(0, i * (cageThickness + coinDiameter) + textAreaHeight, windowWidth, cageThickness);
        }
        // draw vertical cage rectangles
        for (int i = 0; i < 8; i++) {
            g.fillRect(i * (cageThickness + coinDiameter), textAreaHeight, cageThickness, boardHeight);
        }
    }

    public void drawCoins(Graphics g) {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {

                if (board[x][y] != null) {
                    // set color
                    if (board[x][y].getColor() == 0)
                        g.setColor(Color.yellow);
                    else
                        g.setColor(Color.red);

                    // draw coin (dot)
                    int topX = cageThickness + x * (coinDiameter + cageThickness);
                    int topY = (6 - y) * (coinDiameter + cageThickness) + textAreaHeight - coinDiameter; // math moment
                    g.fillOval(topX, topY, coinDiameter, coinDiameter);

                }
            }
        }
    }

    public void drawText(Graphics g, int color) {
        g.setFont(new Font("Consolas", Font.BOLD, 50));
        if (color == 0) {
            g.setColor(Color.yellow);
            g.drawString("YELLOW'S TURN", 190, 140);
        } else if (color == 1) {
            g.setColor(Color.red);
            g.drawString("RED'S TURN", 240, 140);
        }
    }

    // returns true if the board is full
    public boolean boardIsFull() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                if (board[x][y] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    // returns true if there is a win
    public boolean hasWon(int color) {
        return xWin(color) || yWin(color) || diag1Win(color) || diag2Win(color);
    }

    // returns true if there is a horizontal win
    public boolean xWin(int color) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 6; y++) {
                // need to check for null
                if (board[x][y] != null && board[x + 1][y] != null && board[x + 2][y] != null
                        && board[x + 3][y] != null) {
                    if (board[x][y].getColor() == color && board[x + 1][y].getColor() == color
                            && board[x + 2][y].getColor() == color && board[x + 3][y].getColor() == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // returns true if there is a vertical win
    public boolean yWin(int color) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 7; x++) {
                if (board[x][y] != null && board[x][y + 1] != null && board[x][y + 2] != null
                        && board[x][y + 3] != null) {
                    if (board[x][y].getColor() == color && board[x][y + 1].getColor() == color
                            && board[x][y + 2].getColor() == color && board[x][y + 3].getColor() == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // return true if there is a bottom-left to top-right (/) diagonal win
    public boolean diag1Win(int color) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                if (board[x][y] != null && board[x + 1][y + 1] != null && board[x + 2][y + 2] != null
                        && board[x + 3][y + 3] != null) {
                    if (board[x][y].getColor() == color && board[x + 1][y + 1].getColor() == color
                            && board[x + 2][y + 2].getColor() == color && board[x + 3][y + 3].getColor() == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // return true if there is a bottom-right to top-left (\) diagonal win
    public boolean diag2Win(int color) {
        for (int x = 3; x < 7; x++) {
            for (int y = 0; y < 3; y++) {
                if (board[x][y] != null && board[x - 1][y + 1] != null && board[x - 2][y + 2] != null
                        && board[x - 3][y + 3] != null) {
                    if (board[x][y].getColor() == color && board[x - 1][y + 1].getColor() == color
                            && board[x - 2][y + 2].getColor() == color && board[x - 3][y + 3].getColor() == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
