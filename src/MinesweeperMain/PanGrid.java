package MinesweeperMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public final class PanGrid extends JPanel {

    BtnTile[][] grid;
    int Length, Height;
    int BombNum = 10;
    Random rand;
    boolean Flagon = false;
    PanMain parent;
    boolean FirstClick = true;
    boolean hitBomb = false;

    public PanGrid(int width, int length) {
        CreateGrid(width, length);
        playSound("New_Game.wav");
    }

    public void AddBomb() {
        boolean added = false;
        while (!added) {
            int randX = rand.nextInt(Length);
            int randY = rand.nextInt(Height);
            if (!grid[randX][randY].isBomb && !(randX == 0 && randY == 0)) {
                grid[randX][randY].isBomb = true;
                added = true;
            }
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
        hitBomb = false;
        FirstClick = true;
        rand = new Random();
        Length = length;
        Height = height;
        this.setLayout(new GridLayout(Height, Length));
        grid = new BtnTile[Length][Height];
        for (int y = 0; y < Height; y++) {
            for (int x = 0; x < Length; x++) {
                grid[x][y] = new BtnTile();
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
                    if (grid[i][j].getAdjacentTiles().get(k).isBomb) {
                        bombsAround += 1;
                    }
                }
                grid[i][j].nValue = bombsAround;
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

    void resetBomb(BtnTile aThis) {
        grid[0][0].isBomb = true;
        aThis.isBomb = false;
        setnum();
    }

    boolean checkWin() {
        if (hitBomb) {
            return false;
        }
        for (int i = 0; i < Length; i++) {
            for (int j = 0; j < Height; j++) {
                if (!grid[i][j].revealed && !grid[i][j].isBomb) {
                    return false;
                }
            }
        }
        return true;
    }

    void win() {
//        boolean even = false;
//        for (int i = 0; i < Length; i++) {
//            for (int j = 0; j < Height; j++) {
//                grid[i][j].isColoured = even;
//                even = !even;
//            }
//        }
        for (int i = 0; i < 0; i++) {
            for (int j = 0; j < Height; j++) {
                grid[i][j].WinAnum();
            }
        }
    }

    void playSound(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File fSound = new File(url);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(fSound);
                    clip.open(inputStream);
                    clip.start();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
