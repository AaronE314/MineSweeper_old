package MinesweeperMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JPanel;

public class PanGrid extends JPanel {

    Tile[][] grid;
    int Length, Height;
    int BombNum = 10;
    Random rand;
    boolean Flagon = false;
    PanMain parent;
    boolean FirstClick = true;
    int nBombsss = 0;

    public PanGrid(int width, int length) {
        CreateGrid(width, length);
    }

    public void AddBomb() {
        boolean added = false;
        nBombsss+=1;
        while (!added) {
            int randX = rand.nextInt(Length);
            int randY = rand.nextInt(Height);
            if (!grid[randX][randY].bomb && !(randX == 0 && randY == 0)) {
                grid[randX][randY].bomb = true;
                added = true;
            }
            System.out.println(randX + ", " + randY + " #: "+ nBombsss);
        }
    }

    public void KillGrid() {
        for (int y = 0; y < Length; y++) {
            for (int x = 0; x < Height; x++) {
                remove(grid[x][y]);
                revalidate();
                repaint();
            }
        }
    }

    void CreateGrid(int height, int length) {
        FirstClick = true;
        rand = new Random();
        Length = length;
        Height = height;
        this.setLayout(new GridLayout(Height, Length));
        grid = new Tile[Length][Height];
        for (int y = 0; y < Height; y++) {
            for (int x = 0; x < Length; x++) {
                grid[x][y] = new Tile();
                grid[x][y].init(x, y);
                grid[x][y].parentGrid = this;
                this.add(grid[x][y]);
                grid[x][y].setBackground(Color.LIGHT_GRAY);
            }
        }
        for (int i = 0; i < BombNum; i++) {
            AddBomb();
        }
        try {
            parent.panOptions.SetBombLabel(BombNum);
        } catch (Exception Errorz) {
        }
        setnum();

    }

    void setnum() {
        for (int i = 0; i < Length; i++) {
            for (int j = 0; j < Height; j++) {
                int bombsAround = 0;
                for (int k = 0; k < grid[i][j].getAdjacentTiles().size(); k++) {
                    if (grid[i][j].getAdjacentTiles().get(k).bomb) {
                        bombsAround += 1;
                    }
                }
                grid[i][j].value = bombsAround;
            }
        }
    }

    void revealAll() {
        for (int i = 0; i < Length; i++) {
            for (int j = 0; j < Height; j++) {
                if (!grid[i][j].revealed) {
                    grid[i][j].reveal();
                }
            }
        }
    }

    void resetBomb(Tile aThis) {
        grid[0][0].bomb = true;
        aThis.bomb = false;
        setnum();
        //System.err.println("Error: Bomb on first click");
    }
}
