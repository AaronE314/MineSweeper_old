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

    int nWinIteration = 0;
    int nColorIteration = 0;
    Point position;
    boolean isBomb = false;
    int nValue;
    Color arColours[] = {Color.BLUE, new Color(21, 148, 8), Color.RED, new Color(00, 00, 100), new Color(165, 102, 21), new Color(0, 185, 185), Color.BLACK, Color.GRAY};
    public PanGrid parentGrid;
    boolean revealed = false;
    boolean Flaged = false;
    
    //Setting all the directions around each tile
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
    
    //action listener for each tile
    @Override
    public void actionPerformed(ActionEvent e) {
        //Checking if flag mode is enabled
        if (!parentGrid.Flagon) {
            //if not it will play right sound and reveal the tile
            if(isBomb){
                parentGrid.playSound("Long_Bomb.wav");
            }else if (!revealed){
                parentGrid.playSound("Click.wav");
            }
            reveal();
        } else {
            //if it is it will set the required info for it to flag or unflag it
            if (parentGrid.parent.panOptions.nBombs > 0) {
                if (!revealed && !Flaged) {
                    setBackground(Color.YELLOW);
                    Flaged = true;
                    parentGrid.parent.panOptions.UpdateBombLabel(-1);
                } else if (Flaged && !revealed) {
                    setBackground(PanOptions.getcolour());
                    Flaged = false;
                    parentGrid.parent.panOptions.UpdateBombLabel(1);
                }
            } else if (Flaged && !revealed) {
                setBackground(parentGrid.BGC);
                Flaged = false;
                parentGrid.parent.panOptions.UpdateBombLabel(1);
            }
        }
    }
    
    //Reveal the tile
    public void reveal() {
        revealed = true;
        //Checking if it is a bomb
        if (isBomb) {
            //if it is check if its the first click
            if (!parentGrid.FirstClick) {
                //if its not first click end game
                setBackground(Color.RED);
                parentGrid.hitBomb = true;
                parentGrid.revealAll();
            } else {
                //if it is first click set it to not be a bomb
                parentGrid.resetBomb(this);
                reveal();
            }
        } else {
            //if its not a bomb then set the text of the button to number of bombs around
            setBackground(Color.WHITE);
            if (nValue != 0) {
                setText(String.valueOf(nValue));
                setFont(new Font("Arial", Font.BOLD, 16));//Serif
                setForeground(arColours[(nValue - 1)]);
            }
            if (parentGrid.checkWin()) {
                parentGrid.win();
            }
        }
        
        //if there are 0 bombs around it start a chain reaction to reaval other tiles
        if (nValue == 0 && !isBomb) {
            revealAround();
        }
        parentGrid.FirstClick = false;
        if (Flaged) {
            parentGrid.parent.panOptions.UpdateBombLabel(1);
        }
    }
    
    //Reveal tiles around each tile if not bomb
    void revealAround() {
        for (int i = 0; i < getAdjacentTiles().size(); i++) {
            if (!getAdjacentTiles().get(i).isBomb && !getAdjacentTiles().get(i).revealed && !Flaged) {
                getAdjacentTiles().get(i).reveal();
            }
        }
    }
    //Gets an array of  the adjacent tiles
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
    
    //The win animation
    public void WinAnim(int nDelay, int nIteration) {
        nColorIteration = nIteration;
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                
                nWinIteration += 1;
                if (nWinIteration == nColorIteration) {
                    if (isBomb){
                        setBackground(Color.MAGENTA);
                    }else{
                        setBackground(Color.BLUE);
                    }
                }else if (nWinIteration > nColorIteration){
                    if (isBomb){
                        setBackground(Color.MAGENTA);
                    }else{
                        setBackground(Color.WHITE);
                    }
                }
            }
        };
        new Timer(nDelay, taskPerformer).start();

    }
}
