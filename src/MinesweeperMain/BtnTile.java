package MinesweeperMain;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;

public class BtnTile extends JButton implements ActionListener {

    Point position;
    boolean bomb = false;
    int value;
    public PanGrid parentGrid;
    boolean revealed = false;
    boolean Shift = false;
    boolean Flaged = false;

    enum Directions {

        Up(new Point(0, 1)), Down(new Point(0, -1)), Right(new Point(1, 0)), Left(new Point(-1, 0)),
        UpRight(new Point(1, 1)), DownRight(new Point(1, -1)), UpLeft(new Point(-1, 1)), DownLeft(new Point(-1, -1));
        private Point value;

        Directions(Point value) {
            this.value = value;
        }

        public Point getValue() {
            return value;
        }
    }

    public void init(int x, int y) {
        position = new Point(x, y);
        addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!parentGrid.Flagon) {
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
        if (bomb) {
            if (!parentGrid.FirstClick) {
                setBackground(Color.RED);
                parentGrid.revealAll();
            } else {
                parentGrid.resetBomb(this);
                reveal();
            }
        } else {
            setBackground(Color.WHITE);
            if (value != 0) {
                setText(String.valueOf(value));
            }
        }

        if (value == 0 && !bomb) {
            revealAround();
        }
        parentGrid.FirstClick = false;
    }

    void revealAround() {
        for (int i = 0; i < getAdjacentTiles().size(); i++) {
            if (!getAdjacentTiles().get(i).bomb && !getAdjacentTiles().get(i).revealed && !Flaged) {
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
}
