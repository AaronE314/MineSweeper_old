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
    boolean won = false;
    Color BGC;

    public PanGrid(int width, int length) {
        BGC = Color.LIGHT_GRAY;
        CreateGrid(width, length);
        playSound("New_Game.wav");
    }

    //Adding a bomb in a random location
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

    //Removing entire grid
    public void KillGrid() {
        for (int y = 0; y < Length; y++) {
            for (int x = 0; x < Height; x++) {
                remove(grid[x][y]);
                revalidate();
                repaint();
            }
        }
    }

    //Create a new grid
    void CreateGrid(int height, int length) {
        won = false;
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
                grid[x][y].setBackground(BGC);
            }
        }
        for (int i = 0; i < BombNum; i++) {
            AddBomb();
        }
        try {
            parent.panOptions.SetBombLabel(BombNum);
            parent.panOptions.SetTimeLabel();
            parent.panOptions.SetScoreLabel();
        } catch (Exception Errorz) {
        }

        setnum();

    }

    //Set the amount of bombs around each tile
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

    //Reaveal the entire map
    void revealAll() {
        for (int i = 0; i < Length; i++) {
            for (int j = 0; j < Height; j++) {
                if (!grid[i][j].revealed) {
                    grid[i][j].reveal();
                }
            }
        }
    }

    //Fix bomb if first click is a bomb
    void resetBomb(BtnTile aThis) {
        grid[0][0].isBomb = true;
        aThis.isBomb = false;
        setnum();
    }

    //Check for if they won
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

    //Play a win anum
    void win() {
        won = true;
        playSound("Win.wav");
        parent.panOptions.AddScore(parent.panOptions.nTimePlayed);
        parent.panOptions.TimePassed.stop();
        int nDelay = Math.round(1600 / Height);
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Length; j++) {
                grid[j][i].WinAnim(nDelay, (Height - i));
            }
        }
    }

    //Easy way to play sounds
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