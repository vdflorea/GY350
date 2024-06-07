// Vlad Florea
// 22409144

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class Main extends JFrame implements Runnable, MouseListener, MouseMotionListener {

    private static final Dimension WindowSize = new Dimension(800, 800);
    private BufferStrategy strategy;
    private Graphics offscreenBuffer;

    private static final int NUMCELLS = 40; // Number of cells of ONE side of a square grid NUMCELLS * NUMCELLS in size
    private boolean[][][] gameState = new boolean[NUMCELLS][NUMCELLS][2]; // Two buffers (front/back) 0/1

    private static final int CELL_WIDTH = WindowSize.width / NUMCELLS;
    private static final int CELL_HEIGHT = WindowSize.height / NUMCELLS;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;

    // Write/read buffer values will flip continuously during program execution
    private static int writeBuffer = 1;
    private static int readBuffer = 0;

    private boolean isPlaying = false;
    private Point lastClickedPosition = new Point(0, 0);

    // Implement dragging functionality
    private Point lastDraggedPosition = new Point(0, 0);
    private int selectedCellXIndex;
    private int selectedCellYIndex;
    private boolean[][] SelectedCell = new boolean[NUMCELLS][NUMCELLS];

    private String workingDirectory; // Implement file handling for loading/saving game state

    public Main() {
        addMouseListener(this);
        addMouseMotionListener(this);

        // Create and set up the window
        this.setTitle("Conway's Game of Life (Finished)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window, centred on the screen
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        // Implement double buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        offscreenBuffer = strategy.getDrawGraphics();

        // Instantiate each Cell within the FRONT BUFFER [0] (set to false initially)
        for (int i = 0; i < NUMCELLS; i++) {
            for (int j = 0; j < NUMCELLS; j++) {
                gameState[i][j][readBuffer] = false; // On first iteration, we write to buffer 0, (then read from buffer 0 and write to buffer 1)
                gameState[i][j][writeBuffer] = false;
            }
        }

        // Store program working directory in a variable
        workingDirectory = System.getProperty("user.dir");

        // Create and start a thread
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        // While NOT playing, the player can do the following actions:
        // --> Invert any cell on the grid manually (clicking it)
        // --> Click randomise button infinitely many times
        // --> Click start button once

        // While playing, the player can do the following actions:
        // --> Click stop button once

        while (true) {

            // If start/stop button clicked
            // NB: Modify condition based on position of button and gaps in paint()
            if (lastClickedPosition.getX() <= BUTTON_WIDTH + 10 && lastClickedPosition.getX() > 10) {
                if (lastClickedPosition.getY() <= BUTTON_HEIGHT + 35 && lastClickedPosition.getY() > 35) {
                    isPlaying = !isPlaying; // Invert game state (start <-> stop)
                    lastClickedPosition.setLocation(0, 0); // Reset lastClickedPosition
                }
            }

            // If randomise button clicked
            // NB: Modify condition based on position of button and gaps in paint()
            if (lastClickedPosition.getX() <= 2*BUTTON_WIDTH + 20 && lastClickedPosition.getX() > BUTTON_WIDTH + 10) {
                if (lastClickedPosition.getY() <=  BUTTON_HEIGHT + 35 && lastClickedPosition.getY() > 35) {
                    // Only randomise cells when the game is NOT in action
                    if (!isPlaying) {
                        randomiseCells();
                    }
                    lastClickedPosition.setLocation(0, 0); // Reset lastClickedPosition
                }
            }

            // If load button clicked
            // NB: Modify condition based on position of button and gaps in paint()
            if (lastClickedPosition.getX() <= 3*BUTTON_WIDTH + 30 && lastClickedPosition.getX() > 2*BUTTON_WIDTH + 20) {
                if (lastClickedPosition.getY() <=  BUTTON_HEIGHT + 35 && lastClickedPosition.getY() > 35) {
                    // Only load cells when the game is NOT in action
                    if (!isPlaying) {
                        loadCells();
                    }
                    lastClickedPosition.setLocation(0, 0); // Reset lastClickedPosition
                }
            }

            // If save button clicked
            // NB: Modify condition based on position of button and gaps in paint()
            if (lastClickedPosition.getX() <= 4*BUTTON_WIDTH + 40 && lastClickedPosition.getX() > 3*BUTTON_WIDTH + 30) {
                if (lastClickedPosition.getY() <=  BUTTON_HEIGHT + 35 && lastClickedPosition.getY() > 35) {
                    // Only save cells when the game is NOT in action
                    if (!isPlaying) {
                        saveCells();
                    }
                    lastClickedPosition.setLocation(0, 0); // Reset lastClickedPosition
                }
            }

            if (!isPlaying) {

                // Invert the specific cell in which lastClickedPosition/lastDraggedPosition lies within
                for (int i = 0; i < WindowSize.height; i += CELL_HEIGHT) {
                    int upperBoundHeight = i + CELL_HEIGHT;
                    for (int j = 0; j < WindowSize.width; j += CELL_WIDTH) {
                        int upperBoundWidth = j + CELL_WIDTH;

                        // If lastClickedPosition lies within the current Cell we are checking:
                        if (lastClickedPosition.getX() != 0 && lastClickedPosition.getY() != 0) {
                            if (lastClickedPosition.getX() <= upperBoundWidth && lastClickedPosition.getX() > j) {
                                if (lastClickedPosition.getY() <= upperBoundHeight && lastClickedPosition.getY() > i) {

                                    gameState[j / CELL_HEIGHT][i / CELL_WIDTH][readBuffer] = !gameState[j / CELL_HEIGHT][i / CELL_WIDTH][readBuffer];
                                    lastClickedPosition.setLocation(0, 0); // Reset lastClickedPosition
                                }
                            }
                        }

                        // If lastDraggedPosition lies within the current Cell we are checking:
                        if (lastDraggedPosition.getX() != 0 && lastDraggedPosition.getY() != 0) {
                            if (lastDraggedPosition.getX() <= upperBoundWidth && lastDraggedPosition.getX() > j) {
                                if (lastDraggedPosition.getY() <= upperBoundHeight && lastDraggedPosition.getY() > i) {

                                    // If the current Cell we are checking is NOT the currently selected cell
                                    // --> Either the mouse went out of bounds of currently selected cell OR this is the first cell being selected
                                    if (SelectedCell[j / CELL_HEIGHT][i / CELL_WIDTH] == false) {
                                        selectedCellYIndex = i / CELL_WIDTH;
                                        selectedCellXIndex = j / CELL_HEIGHT;

                                        SelectedCell = new boolean[NUMCELLS][NUMCELLS]; // Reset ALL selected cells to false (only one selected cell at a time
                                        SelectedCell[selectedCellXIndex][selectedCellYIndex] = true; // Set "selected" to true in parallel array for current selected cell

                                        // Paint selected cell ONCE
                                        gameState[selectedCellXIndex][selectedCellYIndex][readBuffer] = !gameState[j / CELL_HEIGHT][i / CELL_WIDTH][readBuffer];

                                        lastDraggedPosition.setLocation(0, 0); // Reset lastDraggedPosition
                                    }
                                }
                            }
                        }
                    }

                    this.repaint();
                }
            } else if (isPlaying) {
                // Cellular automata
                // NB: After applying rules, swap the buffer from 0 <-> 1 (back/front)

                for (int x = 0; x < NUMCELLS; x++) {
                    for (int y = 0; y < NUMCELLS; y++) {
                        boolean isAlive = gameState[x][y][readBuffer];
                        int numAliveNeighbours = 0;

                        gameState[x][y][writeBuffer] = gameState[x][y][readBuffer]; // Copy over contents of the readBuffer to the writeBuffer

                        // Loop through each neighbour, counting how many neighbours are alive
                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                // Ignore the current cell
                                if (xx ==  0 && yy ==  0) continue;

                                // Calculate the neighbour's coordinates, wrapping around if necessary
                                int neighbourX = (x + xx + NUMCELLS) % NUMCELLS;
                                int neighbourY = (y + yy + NUMCELLS) % NUMCELLS;

                                // If the neighbour is alive, increment the count
                                if (gameState[neighbourX][neighbourY][readBuffer]) {
                                    numAliveNeighbours++;
                                }
                            }
                        }

                        // If current cell is alive
                        if (isAlive) {
                            // NOTE: Alive cells with two or three alive neighbours live on to the next generation (iteration)
                            if (numAliveNeighbours < 2 || numAliveNeighbours > 3) {
                                // Cell dies if it has fewer than two neighbours (underpopulation) OR greater than three neighbours (overpopulation)
                                gameState[x][y][writeBuffer] = false;
                            }
                        } else { // If current cell is dead
                            if (numAliveNeighbours == 3) {
                                // Cell comes back to life if it has exactly three neighbours (reproduction)
                                gameState[x][y][writeBuffer] = true;
                            }
                        }
                    }
                }
                swapBuffers();
                this.repaint();
            }

            try {
                Thread.sleep(100); // "Delay" of 100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void randomiseCells() {
        for (int i = 0; i < NUMCELLS; i++) {
            for (int j = 0; j < NUMCELLS; j++) {
                gameState[i][j][readBuffer] = Math.random() < 0.2; // Set true/false based on 20/80 chance statement in favour of black cells
            }
        }
    }

    public void loadCells() {

        String filename = workingDirectory + "\\src\\lifegame.txt";
        String in;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            in = reader.readLine();
            reader.close();

            if (in != null && in.length() == NUMCELLS*NUMCELLS) { // Something wrong with file if false

                // Read each "boolean" Character of file, toggling state of each cell in grid accordingly
                for (int i = 0; i < NUMCELLS; i++) {
                    for (int j = 0; j < NUMCELLS; j++) {
                        Character c = in.charAt((NUMCELLS * i) + j);

                        if (c == '1') {
                            gameState[i][j][readBuffer] = true;
                        } else if (c == '0') {
                            gameState[i][j][readBuffer] = false;
                        } else {
                            System.out.println("Invalid load file at " + filename);
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Invalid load file at " + filename);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveCells() {

        String filename = workingDirectory + "\\src\\lifegame.txt";
        String out = "";

        // Iterate through each cell, concatenating 1 or 0 to file output String depending on state of cell
        for (int i = 0; i < NUMCELLS; i++) {
            for (int j = 0; j < NUMCELLS; j++) {
                if (gameState[i][j][readBuffer] == true) {
                    out += 1;
                } else {
                    out += 0;
                }
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(out); // Write output string to file
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void swapBuffers() {
        // Flip buffers (front <-> back)
        int temp = readBuffer;
        readBuffer = writeBuffer;
        writeBuffer = temp;
    }

    public void paint(Graphics g) {
        // Implement double buffering
        g = offscreenBuffer;

        // Paint each Cell (Black for FALSE, White for TRUE)
        for (int i = 0; i < WindowSize.height; i += CELL_HEIGHT) {
            for (int j = 0; j < WindowSize.width; j += CELL_WIDTH) {
                // Set the colour of each Cell depending on whether clicked on or not (true/false)
                if (gameState[i / CELL_HEIGHT][j / CELL_WIDTH][readBuffer] == true) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }

                // Draw each Cell
                g.fillRect(i, j, CELL_WIDTH, CELL_HEIGHT);
            }
        }

        // Draw rectangles behind fonts
        if (isPlaying) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(10, 35, BUTTON_WIDTH, BUTTON_HEIGHT); // Draw either green/red (start/stop) rectangle depending on game state
        g.setColor(Color.GREEN);
        g.fillRect(10 + BUTTON_WIDTH + 10, 35, BUTTON_WIDTH, BUTTON_HEIGHT); // Draw randomise button background
        g.fillRect(10 + BUTTON_WIDTH + 10 + BUTTON_WIDTH + 10, 35, BUTTON_WIDTH, BUTTON_HEIGHT); // Draw load button background
        g.fillRect(10 + BUTTON_WIDTH + 10 + BUTTON_WIDTH + 10 + BUTTON_WIDTH + 10, 35, BUTTON_WIDTH, BUTTON_HEIGHT); // Draw save button background

        // Draw fonts
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.ITALIC,  15));
        if (!isPlaying) {
            g.drawString("Start", 45, 50); // Draw "start" text over GREEN background
        } else if (isPlaying) {
            g.drawString("Stop", 45, 50); // Draw "stop" text over RED background
        }
        g.drawString("Randomise", 10 + BUTTON_WIDTH + 10 + 15, 50); // Draw randomise button text
        g.drawString("Load", 10 + BUTTON_WIDTH + 10 + BUTTON_WIDTH + 10 + 30, 50); // Draw randomise button text
        g.drawString("Save", 10 + BUTTON_WIDTH + 10 + BUTTON_WIDTH + 10 + BUTTON_WIDTH + 10 + 30, 50); // Draw randomise button text

        // Implement double buffering
        strategy.show();
    }

    /******************** Mouse Events ********************/
    public void mousePressed(MouseEvent e) {
        // If user clicks left mouse button within the JFrame
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Store exact x & y coordinates of position clicked into a variable
            lastClickedPosition = e.getPoint();
        }
    }
    public void mouseReleased(MouseEvent e) {
        // Not required
    }
    public void mouseEntered(MouseEvent e) {
        // Not required
    }
    public void mouseExited(MouseEvent e) {
        // Not required
    }
    public void mouseClicked(MouseEvent e) {
        // Not required
    }

    /******************** Mouse Motion Events ********************/
    public void mouseDragged(MouseEvent e) {
        // If user drags mouse with left mouse button pressed down within the JFrame
        if (e.getButton() == 0) {
            // Store exact x & y coordinates of position clicked into a variable
            lastDraggedPosition = e.getPoint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        // Not required
    }

    public static void main(String[] args) {
        Main m = new Main();
    }
}

