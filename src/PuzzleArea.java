import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class PuzzleArea extends GameArea
{
    public int level;

    public PuzzleArea(int level)
    {
        this.level = level;
        Color[][] map = new Color[getRows()][getColumns()];
        ArrayList<TetrisBlock> availBlocks = new ArrayList<>();
        if (level == 1)
        {
            for (int r = 18; r < getRows(); r++) {
                for (int c = 0; c < getColumns(); c++) {
                    map[r][c] = Color.GRAY;
                }
                map[r][2] = null;
            }
            for (int i = 0; i < 7; i++) {
                availBlocks.add(new TetrisBlock(i));
            }
        } else if (level == 2) {
            for (int r = 19; r < getRows(); r++) {
                for (int c = 3; c < getColumns(); c++) {
                    map[r][c] = Color.GRAY;
                }
            }
            map[21][2] = Color.GRAY;
            for (int i = 0; i < 7; i++) {
                availBlocks.add(new TetrisBlock(i));
            }
        } else if (level == 3) {
            for (int r = 19; r < getRows(); r++) {
                for (int c = 0; c < getColumns(); c++) {
                    map[r][c] = Color.GRAY;
                }
            }
            for (int c = 3; c <= 6; c++) { map[19][c] = null; }
            for (int c = 4; c <= 5; c++) { map[20][c] = null; }
            for (int c = 4; c <= 5; c++) { map[21][c] = null; }
            for (int i = 2; i < 6; i++) {
                availBlocks.add(new TetrisBlock(i));
            }
        } else if (level == 4) {
            for (int r = 16; r < getRows(); r++) {
                for (int c = 0; c < 4; c++) {
                    map[r][c] = Color.GRAY;
                }
                for (int c = 7; c < getColumns(); c++) {
                    map[r][c] = Color.GRAY;
                }
            }
            map[16][3] = null;
            map[16][6] = Color.GRAY;
            map[17][6] = Color.GRAY;
            map[19][4] = Color.GRAY;
            map[19][5] = Color.GRAY;
            map[20][4] = Color.GRAY;
            map[21][4] = Color.GRAY;
            map[21][5] = Color.GRAY;
            availBlocks.add(new TetrisBlock(0));
            availBlocks.add(new TetrisBlock(2));
            availBlocks.add(new TetrisBlock(2));
            availBlocks.add(new TetrisBlock(5));
            availBlocks.add(new TetrisBlock(5));
        } else if (level == 5) {
            for (int r = 18; r < getRows(); r++) {
                for (int c = 3; c < getColumns(); c++) {
                    map[r][c] = Color.GRAY;
                }
            }
            map[18][3] = null;
            map[20][2] = Color.GRAY;
            availBlocks.add(new TetrisBlock(0));
            availBlocks.add(new TetrisBlock(4));
            availBlocks.add(new TetrisBlock(4));
            availBlocks.add(new TetrisBlock(5));
            availBlocks.add(new TetrisBlock(5));
        }
        setBackground(map);
        setBag(availBlocks);
    }

    public void spawnBlock(int blockType)
    {
        if (!isPC() && getBag().size() > 0) {
            for (int i = 0; i < getBag().size(); i++) {
                if (getBag().get(i).getType() == blockType) {
                    if (getBlock() != null) { getBag().add(new TetrisBlock(getBlock().getType())); }
                    setBlock(getBag().remove(i));
                    getBlock().move(0, -4);
                    moveGhostBlock();
                    repaint();
                    break;
                }
            }
        }
    }

    public void makeBlockBackground()
    {
        super.makeBlockBackground();
        setBlock(null);
        setGhostBlock(null);
    }

    protected void moveDown()
    {
        if (!isBottom(getBlock())) {
            getBlock().down();
            repaint();
        }
    }

    public void moveRight()
    {
        if (getBlock() != null) { super.moveRight(); }
    }

    public void moveLeft()
    {
        if (getBlock() != null) { super.moveLeft(); }
    }

    public void softDrop()
    {
        if (getBlock() != null) { moveDown(); }
    }

    public void hardDrop()
    {
        if (getBlock() != null) {
            Tetris.playHardDrop();
            while (!isBottom(getBlock())) {
                getBlock().down();
            }
            repaint();
            makeBlockBackground();
            blockPlaced = true;
        }
    }

    public void rotateCCW()
    {
        if (getBlock() != null) { super.rotateCCW(); }
    }

    public void rotateCW()
    {
        if (getBlock() != null) { super.rotateCW(); }
    }

    public void rotate180()
    {
        if (getBlock() != null) { super.rotate180(); }
    }

    protected void drawFrame(Graphics g)
    {
        g.setColor(Color.GRAY);
        for(int y = 7; y < getRows(); y++) { // board frame
            for(int x = 0; x < getColumns(); x++) {
                g.drawRect(x * getCellSize() + 100, y * getCellSize(), getCellSize(), getCellSize());
            }
        }
        g.drawRect(50, 15, 350, 80); // bag frame
        for (int i = 0; i < 14; i++) {
            TetrisBlock printBlock = new TetrisBlock(i % 7);
            for(int r = 0; r < printBlock.getHeight(); r++) {
                for(int c = 0; c < printBlock.getWidth(); c++) {
                    if(printBlock.getShape()[r][c] == 1) {
                        g.setColor(Color.gray);
                        g.fillRect(60 + c * 10 + printBlock.getQueueOffset() + printBlock.getType() * 50,65 + r * 10 + i / 7 * -40, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawRect(60 + c * 10 + printBlock.getQueueOffset() + printBlock.getType() * 50,65 + r * 10 + i / 7 * -40, 10, 10);
                    }
                }
            }
        }
    }

    protected void drawCurrentBlock(Graphics g) // unchanged
    {
        if (getBlock() != null) {
            for (int r = 0; r < getBlock().getHeight(); r++) {
                for (int c = 0; c < getBlock().getWidth(); c++) {
                    if (getBlock().getShape()[r][c] == 1) {
                        g.setColor(getBlock().getColour());
                        g.fillRect((c + getBlock().getX()) * getCellSize() + 100,(r + getBlock().getY()) * getCellSize(), getCellSize(), getCellSize());
                        g.setColor(Color.BLACK);
                        g.drawRect((c + getBlock().getX()) * getCellSize() + 100,(r + getBlock().getY()) * getCellSize(), getCellSize(), getCellSize());
                    }
                }
            }
        }
    }

    protected void drawGhostBlock (Graphics g) // unchanged
    {
        if (getGhostBlock() != null) {
            for (int r = 0; r < getGhostBlock().getHeight(); r++) {
                for (int c = 0; c < getGhostBlock().getWidth(); c++) {
                    if (getGhostBlock().getShape()[r][c] == 1) {
                        g.setColor(getGhostBlock().getColour());
                        g.drawRect((c + getGhostBlock().getX()) * getCellSize() + 101,(r + getGhostBlock().getY()) * getCellSize() + 1, getCellSize() - 2, getCellSize() - 2);
                    }
                }
            }
        }
    }

    protected void drawQueueBlocks(Graphics g)
    {
        for (int i = 0; i < getBag().size(); i++) {
            int duplicateOffset = 0;
            TetrisBlock queueBlock = getBag().get(i);
            for (int j = 0; j < i; j++) {
                if (queueBlock.getType() == getBag().get(j).getType()) duplicateOffset -= 40;
            }
            for(int r = 0; r < queueBlock.getHeight(); r++) {
                for(int c = 0; c < queueBlock.getWidth(); c++) {
                    if(queueBlock.getShape()[r][c] == 1) {
                        g.setColor(queueBlock.getColour());
                        g.fillRect(60 + c * 10 + queueBlock.getQueueOffset() + queueBlock.getType() * 50,65 + r * 10 + duplicateOffset, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawRect(60 + c * 10 + queueBlock.getQueueOffset() + queueBlock.getType() * 50,65 + r * 10 + duplicateOffset, 10, 10);
                    }
                }
            }
        }
    }

    protected void drawHoldBlock(Graphics g)
    {
        System.out.print("");
    }

    protected void drawBackground(Graphics g) // unchanged
    {
        Color colour;
        for (int r = 0; r < getRows(); r++) {
            for (int c = 0; c < getColumns(); c++) {
                colour = getBg()[r][c];
                if(colour != null) {
                    int x = c * getCellSize() + 100;
                    int y = r * getCellSize();
                    g.setColor(colour);
                    g.fillRect(x, y, getCellSize(), getCellSize());
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, getCellSize(), getCellSize());
                }
            }
        }
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}
