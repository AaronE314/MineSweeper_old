package MinesweeperMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.Timer;

public class BtnTile extends JButton implements ActionListener {

    boolean isColoured = true;
    Point position;
    boolean isBomb = false;
    int nValue;
    Color arColours[] = {Color.BLUE, new Color(21, 148, 8), Color.RED, new Color(00, 00, 100), new Color(165, 102, 21), new Color(0, 185, 185), Color.BLACK, Color.GRAY};
    public PanGrid parentGrid;
    boolean revealed = false;
    boolean Flaged = false;

    enum Directions {

        Up(new Point(0, 1)), Down(new Point(0, -1)), Right(new Point(1, 0)), Left(new Point(-1, 0)),
        UpRight(new Point(1, 1)), DownRight(new Point(1, -1)), UpLeft(new Point(-1, 1)), DownLeft(new Point(-1, -1));
        private Point pValue;

        Directions(Point nValue) {
            this.pValue = nValue;
        }

        public Point getValue() {
            return pValue;
        }
    }

    public void init(int x, int y) {
        position = new Point(x, y);
        addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!parentGrid.Flagon) {
            if(isBomb){
                parentGrid.playSound("Long_Bomb.wav");
            }else{
                parentGrid.playSound("Click.wav");
            }
            reveal();
        } else {
            if (parentGrid.parent.panOptions.nBombs > 0) {
                if (!revealed && !Flaged) {
                    setBackground(Color.YELLOW);
                    Flaged = true;
                    parentGrid.parent.panOptions.UpdateBombLabel(-1);
                } else if (Flaged && !revealed) {
                    setBackground(Color.LIGHT_GRAY);
                    Flaged = false;
                    parentGrid.parent.panOptions.UpdateBombLabel(1);
                }
            } else if (Flaged && !revealed) {
                setBackground(Color.LIGHT_GRAY);
                Flaged = false;
                parentGrid.parent.panOptions.UpdateBombLabel(1);
            }
        }
    }

    public void reveal() {
        revealed = true;
        if (isBomb) {
            if (!parentGrid.FirstClick) {
                setBackground(Color.RED);
                parentGrid.hitBomb = true;
                parentGrid.revealAll();
            } else {
                parentGrid.resetBomb(this);
                reveal();
            }
        } else {
            setBackground(Color.WHITE);
            if (nValue != 0) {
                setText(String.valueOf(nValue));
                setFont(new Font("Arial", Font.BOLD, 16));//Serif
                setForeground(arColours[(nValue - 1)]);
            }
            if (parentGrid.checkWin()) {
                parentGrid.playSound("Win.wav");
                parentGrid.win();
            }
        }

        if (nValue == 0 && !isBomb) {
            revealAround();
        }
        parentGrid.FirstClick = false;
        if (Flaged) {
            parentGrid.parent.panOptions.UpdateBombLabel(1);
        }
    }

    void revealAround() {
        for (int i = 0; i < getAdjacentTiles().size(); i++) {
            if (!getAdjacentTiles().get(i).isBomb && !getAdjacentTiles().get(i).revealed && !Flaged) {
                getAdjacentTiles().get(i).reveal();
            }
        }
    }

    public ArrayList<BtnTile> getAdjacentTiles() {
        ArrayList<BtnTile> tileList = new ArrayList();

        for (int i = 0; i < 8; i++) {
            Directions d = Directions.values()[i];
            try {
                tileList.add(parentGrid.grid[position.x + d.getValue().x][position.y + d.getValue().y]);
            } catch (Exception e) {
            }
        }

        return tileList;
    }

    public void WinAnum() {
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isColoured) {
                    setBackground(Color.BLUE);
                } else {
                    setBackground(Color.WHITE);
                }
                isColoured = !isColoured;
            }
        };
        new Timer(300, taskPerformer).start();

    }
}
