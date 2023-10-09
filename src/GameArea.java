import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameArea extends JPanel
{
    private final int rows = 22;
    private final int columns = 10;
    private final int cellSize = 25;
    private Color[][] background;
    private TetrisBlock block;
    private TetrisBlock ghostBlock;
    private ArrayList<TetrisBlock> bag;
    private TetrisBlock holdPiece;
    public boolean canHold;
    private Timer hardDropTimer;
    private boolean canHardDrop;
    public int dropScore;
    public boolean topOut;
    public Timer gravityTimer;
    public Timer lockTimer;
    public int lockDelay;
    public boolean blockPlaced;
    private boolean lastMoveIsRotation;
    private boolean isTripleKick;

    // game initialization
    public GameArea()
    {
        Color backgroundColor = new Color(35, 35, 35);
        this.setBackground(backgroundColor);

        background = new Color[rows][columns];
        bag = new ArrayList<>();
        holdPiece = null;
        canHold = true;
        canHardDrop = true;
        topOut = false;
        blockPlaced = false;
    }

    // game properties
    class GravityTask extends TimerTask
    {
        public void run() {
            moveDown("gravity");
        }
    }

    public void moveDown(String source)
    {
        if (!isBottom(block)) {
            block.down();
            repaint();
            if (source.equals("drop")) dropScore++;
            lastMoveIsRotation = false;
        } else {
            if (lockTimer == null) { // prevents scheduling multiple tasks if moveDown() is called at the bottom
                lockTimer = new Timer();
                lockTimer.schedule(new PlaceBlockTask(), lockDelay);
            }
        }
    }

    class PlaceBlockTask extends TimerTask
    {
        public void run() {
            if (isBottom(block)) { // block might leave a bottom after touching it
                makeBlockBackground();
                blockPlaced = true;
                lockTimer.cancel();
            }
            else { lockTimer = null; } // null needed to restart the timer in moveDown()
        }
    }

    protected void resetLockDelay()
    {
        if (lockTimer != null) {
            lockTimer.cancel();
            lockTimer = new Timer();
            lockDelay -= 10;
            lockTimer.schedule(new PlaceBlockTask(), lockDelay);
        }
    }

    public void spawnBlock()
    {
        while (bag.size() <= 7) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                list.add(i);
            }
            for (int i = 0; i < 7; i++) {
                bag.add(new TetrisBlock(list.remove((int) (Math.random() * list.size()))));
            }
        }
        block = bag.remove(0);
        moveGhostBlock();
        // block = new TetrisBlock((int) (Math.random() * 7));
    }

    public void makeBlockBackground()
    {
        for(int r = 0; r < block.getHeight(); r++) {
            for(int c = 0; c < block.getWidth(); c++) {
                if(block.getShape()[r][c] == 1 && block.getY() + r < 22) {
                    background[block.getY() + r][block.getX() + c] = block.getColour();
                }
            }
        }
    }

    public int clearLines()
    {
        int linesCleared = 0;
        for (int r = rows - 1; r >= 0; r--) {
            boolean completeLine = true;
            for (int c = 0; c < columns; c++) {
                if (background[r][c] == null) {
                    completeLine = false;
                    break;
                }
            }
            if (completeLine) {
                Tetris.playClear();
                linesCleared++;
                for (int i = r - 1; i >= 0; i--) {
                    System.arraycopy(background[i], 0, background[i + 1], 0, columns);
                }
                r++; // Check the same row again after shifting
            }
        }
        for (int c = 0; c < 9; c++) {
            if (background[1][c] != null) {
                topOut = true;
                Tetris.playLose();
                break;
            }
        }
        repaint();
        return linesCleared;
    }

    // game controls
    public void moveRight()
    {
        Tetris.playMove();
        if (!isRightWall()) {
            block.right();
            moveGhostBlock();
            repaint();
            resetLockDelay();
            lastMoveIsRotation = false;
        }
    }

    public void moveLeft()
    {
        Tetris.playMove();
        if (!isLeftWall()) {
            block.left();
            moveGhostBlock();
            repaint();
            resetLockDelay();
            lastMoveIsRotation = false;
        }
    }

    public void softDrop()
    {
        moveDown("drop");
    }

    public void hardDrop()
    {
        if (canHardDrop) {
            Tetris.playHardDrop();
            if (lockTimer != null) {
                lockTimer.cancel();

            }
            while (!isBottom(block)) {
                block.down();
                dropScore += 2;
            }
            repaint();
            makeBlockBackground();
            blockPlaced = true;
            hardDropTimer = new Timer();
            canHardDrop = false; // prevents accidental double drops
            hardDropTimer.schedule(new HardDropTask(), 100);
        }
    }

    class HardDropTask extends TimerTask
    {
        public void run() {
            canHardDrop = true;
            hardDropTimer.cancel();
        }
    }

    public void rotateCCW()
    {
        Tetris.playRotate();
        block.ccw();
        if (wallKick(0, 0)) return;
        if (block.getType() == 0) { // wall kicks for I
            if (block.getRotation() == 3) // 0 --> 3
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(2, 0)) return;
                if (wallKick(-1, 2)) return;
                if (wallKick(2, -1)) return;
            }
            if (block.getRotation() == 0) // 1 --> 0
            {
                if (wallKick(2, 0)) return;
                if (wallKick(-1, 0)) return;
                if (wallKick(2, -1)) return;
                if (wallKick(-1, -2)) return;
            }
            if (block.getRotation() == 1) // 2 --> 1
            {
                if (wallKick(1, 0)) return;
                if (wallKick(-2, 0)) return;
                if (wallKick(1, -2)) return;
                if (wallKick(-2, 1)) return;
            }
            if (block.getRotation() == 2) // 3 --> 2
            {
                if (wallKick(-2, 0)) return;
                if (wallKick(1, 0)) return;
                if (wallKick(-2, -1)) return;
                if (wallKick(1, 2)) return;
            }
        } else { // wall kicks for other blocks
            if (block.getRotation() == 3) // 0 --> 3
            {
                if (wallKick(1, 0)) return;
                if (wallKick(1, 1)) return;
                if (wallKick(0, -2)) return;
                if (wallKick(1 , -2)) return;
            }
            if (block.getRotation() == 0) // 1 --> 0
            {
                if (wallKick(1, 0)) return;
                if (wallKick(1, -1)) return;
                if (wallKick(0, 2)) return;
                if (wallKick(1, 2)) return;
            }
            if (block.getRotation() == 1) // 2 --> 1
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(-1, 1)) return;
                if (wallKick(0, -2)) return;
                if (wallKick(-1, -2)) return;
            }
            if (block.getRotation() == 2) // 3 --> 2
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(-1, -1)) return;
                if (wallKick(0, 2)) return;
                if (wallKick(-1, 2)) return;
            }
        }
        block.cw();
    }

    public void rotateCW()
    {
        Tetris.playRotate();
        block.cw();
        if (wallKick(0, 0)) return;
        if (block.getType() == 0) { // wall kicks for I
            if (block.getRotation() == 1) // 0 --> 1
            {
                if (wallKick(-2, 0)) return;
                if (wallKick(1, 0)) return;
                if (wallKick(-2, -1)) return;
                if (wallKick(1, 2)) return;
            }
            if (block.getRotation() == 2) // 1 --> 2
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(2, 0)) return;
                if (wallKick(-1, 2)) return;
                if (wallKick(2, -1)) return;
            }
            if (block.getRotation() == 3) // 2 --> 3
            {
                if (wallKick(2, 0)) return;
                if (wallKick(-1, 0)) return;
                if (wallKick(2, 1)) return;
                if (wallKick(-1, -2)) return;
            }
            if (block.getRotation() == 0) // 3 --> 0
            {
                if (wallKick(1, 0)) return;
                if (wallKick(-2, 0)) return;
                if (wallKick(1, -2)) return;
                if (wallKick(-2, 1)) return;
            }
        } else { // wall kicks for other blocks
            if (block.getRotation() == 1) // 0 --> 1
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(-1, 1)) return;
                if (wallKick(0, -2)) return;
                if (wallKick(-1 , -2)) return;
            }
            if (block.getRotation() == 2) // 1 --> 2
            {
                if (wallKick(1, 0)) return;
                if (wallKick(1, -1)) return;
                if (wallKick(0, 2)) return;
                if (wallKick(1, 2)) return;
            }
            if (block.getRotation() == 3) // 2 --> 3
            {
                if (wallKick(1, 0)) return;
                if (wallKick(1, 1)) return;
                if (wallKick(0, -2)) return;
                if (wallKick(1, -2)) return;
            }
            if (block.getRotation() == 0) // 3 --> 0
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(-1, -1)) return;
                if (wallKick(0, 2)) return;
                if (wallKick(-1, 2)) return;
            }
        }
        block.ccw();
    }

    public void rotate180()
    {
        Tetris.playRotate();
        block.flip();
        if (wallKick(0, 0)) return;
        if (block.getType() != 0) { // wall kicks for I
            if (block.getRotation() == 2) // 0 --> 2
            {
                if (wallKick(0, 1)) return;
                if (wallKick(1, 1)) return;
                if (wallKick(-1, 1)) return;
                if (wallKick(1, 0)) return;
                if (wallKick(-1, 0)) return;
            }
            if (block.getRotation() == 3) // 1 --> 3
            {
                if (wallKick(1, 0)) return;
                if (wallKick(1, 2)) return;
                if (wallKick(1, 1)) return;
                if (wallKick(0, 2)) return;
                if (wallKick(0, 1)) return;
            }
            if (block.getRotation() == 0) // 2 --> 0
            {
                if (wallKick(0, -1)) return;
                if (wallKick(-1, -1)) return;
                if (wallKick(1, -1)) return;
                if (wallKick(-1, 0)) return;
                if (wallKick(1, 0)) return;
            }
            if (block.getRotation() == 1) // 3 --> 1
            {
                if (wallKick(-1, 0)) return;
                if (wallKick(-1, 2)) return;
                if (wallKick(-1, 1)) return;
                if (wallKick(0, 2)) return;
                if (wallKick(0, 1)) return;
            }
        }
        block.flip();
    }

    public void hold()
    {
        if (canHold) {
            TetrisBlock temp;
            if (holdPiece == null) temp = bag.remove(0);
            else temp = holdPiece;
            holdPiece = new TetrisBlock(block.getType());
            block = temp;
            moveGhostBlock();
            canHold = false;
            if (lockTimer != null)
            {
                lockTimer.cancel();
                lockDelay = 500;
            }
        }
    }

    // helper methods
    protected boolean isRightWall()
    {
        for (int r = 0; r < block.getHeight(); r++) {
            for (int c = 0; c < block.getWidth(); c++) {
                if (block.getShape()[r][c] == 1 && (block.getX() + c == 9 || background[block.getY() + r][block.getX() + c + 1] != null)) return true;
            }
        }
        return false;
    }
    protected boolean isLeftWall()
    {
        for (int r = 0; r < block.getHeight(); r++) {
            for (int c = 0; c < block.getWidth(); c++) {
                if (block.getShape()[r][c] == 1 && (block.getX() + c == 0 || background[block.getY() + r][block.getX() + c - 1] != null)) return true;
            }
        }
        return false;
    }

    protected boolean isBottom(TetrisBlock piece)
    {
        for (int r = 0; r < piece.getHeight(); r++) {
            for (int c = 0; c < piece.getWidth(); c++) {
                if (piece.getShape()[r][c] == 1 && (piece.getY() + r == 21 || background[piece.getY() + r + 1][piece.getX() + c] != null)) return true;
            }
        }
        return false;
    }

    protected boolean wallKick(int offsetX, int offsetY)
    {
        boolean valid = true;
        for (int r = 0; r < block.getHeight(); r++) { // checks if the rotation is valid
            for (int c = 0; c < block.getWidth(); c++) {
                if(block.getShape()[r][c] == 1 && (block.getY() + r - offsetY < 0 || block.getY() + r - offsetY > 21 || block.getX() + c + offsetX > 9 || block.getX() + c + offsetX < 0 || background[block.getY() + r - offsetY][block.getX() + c + offsetX] != null)) valid = false;
            }
        }
        if (valid) {
            block.move(offsetX, offsetY);
            moveGhostBlock();
            repaint();
            resetLockDelay();
            lastMoveIsRotation = true;
            isTripleKick = (block.getType() == 5 && offsetX != 0 && offsetY == -2);
            return true;
        }
        return false;
    }

    public int isTSpin()
    {
        if (block.getType() == 5 && blockPlaced && lastMoveIsRotation)
        {
            int frontCornerCount = 0;
            int backCornerCount = 0;
            if (block.getX() == -1 || background[block.getY()][block.getX()] != null) // top left
            {
                if (block.getRotation() == 0 || block.getRotation() == 3) frontCornerCount++;
                else backCornerCount++;
            }
            if (block.getX() == 8 || background[block.getY()][block.getX() + 2] != null) // top right
            {
                if (block.getRotation() == 0 || block.getRotation() == 1) frontCornerCount++;
                else backCornerCount++;
            }
            if (block.getY() == 20 || block.getX() == -1 || background[block.getY() + 2][block.getX()] != null) // bottom left
            {
                if (block.getRotation() == 2 || block.getRotation() == 3) frontCornerCount++;
                else backCornerCount++;
            }
            if (block.getY() == 20 || block.getX() == 8 || background[block.getY() + 2][block.getX() + 2] != null) // bottom right
            {
                if (block.getRotation() == 2 || block.getRotation() == 1) frontCornerCount++;
                else backCornerCount++;
            }
            if (frontCornerCount + backCornerCount >= 3)
            {
                if (isTripleKick || frontCornerCount == 2) return 2;
                else return 1;
            }
        }
        return 0;
    }

    public boolean isPC()
    {
        for (int c = 0; c < 9; c++) {
            if (background[21][c] != null) {
                return false;
            }
        }
        return true;
    }

    protected void moveGhostBlock()
    {
        ghostBlock = new TetrisBlock(block.getType(), block.getX(), block.getY(), block.getShape());
        while (!isBottom(ghostBlock))
        {
            ghostBlock.down();
        }
    }

    // visuals
    protected void drawFrame(Graphics g)
    {
        g.setColor(Color.GRAY);
        for(int y = 2; y < rows; y++) { // board frame
            for(int x = 0; x < columns; x++) {
                g.drawRect(x * cellSize + 100, y * cellSize, cellSize, cellSize);
            }
        }
        g.drawRect(375, 50, 50, 200); // queue frame
        g.drawRect(25, 50, 50, 40); // hold frame
    }

    protected void drawCurrentBlock(Graphics g)
    {
        if (block != null) {
            for (int r = 0; r < block.getHeight(); r++) {
                for (int c = 0; c < block.getWidth(); c++) {
                    if (block.getShape()[r][c] == 1) {
                        g.setColor(block.getColour());
                        g.fillRect((c + block.getX()) * cellSize + 100,(r + block.getY()) * cellSize, cellSize, cellSize);
                        g.setColor(Color.BLACK);
                        g.drawRect((c + block.getX()) * cellSize + 100,(r + block.getY()) * cellSize, cellSize, cellSize);
                    }
                }
            }
        }
    }

    protected void drawGhostBlock (Graphics g)
    {
        if (ghostBlock != null) {
            for (int r = 0; r < ghostBlock.getHeight(); r++) {
                for (int c = 0; c < ghostBlock.getWidth(); c++) {
                    if (ghostBlock.getShape()[r][c] == 1) {
                        g.setColor(ghostBlock.getColour());
                        g.drawRect((c + ghostBlock.getX()) * cellSize + 101,(r + ghostBlock.getY()) * cellSize + 1, cellSize - 2, cellSize - 2);
                    }
                }
            }
        }
    }

    protected void drawQueueBlocks(Graphics g)
    {
        if (block != null) {
            for (int i = 0; i < 5; i++) {
                TetrisBlock queueBlock = bag.get(i);
                for(int r = 0; r < queueBlock.getHeight(); r++) {
                    for(int c = 0; c < queueBlock.getWidth(); c++) {
                        if(queueBlock.getShape()[r][c] == 1) {
                            g.setColor(queueBlock.getColour());
                            g.fillRect(385 + c * 10 + queueBlock.getQueueOffset(),60 + 40 * i + r * 10, 10, 10);
                            g.setColor(Color.BLACK);
                            g.drawRect(385 + c * 10 + queueBlock.getQueueOffset(),60 + 40 * i + r * 10, 10, 10);
                        }
                    }
                }
            }
        }
    }

    protected void drawHoldBlock(Graphics g)
    {
        if (holdPiece != null) {
            for (int r = 0; r < holdPiece.getHeight(); r++) {
                for (int c = 0; c < holdPiece.getWidth(); c++) {
                    if (holdPiece.getShape()[r][c] == 1) {
                        if (canHold) g.setColor(holdPiece.getColour());
                        else g.setColor(Color.GRAY);
                        g.fillRect(35 + c * 10 + holdPiece.getQueueOffset(),60 + r * 10, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawRect(35 + c * 10 + holdPiece.getQueueOffset(),60 + r * 10, 10, 10);
                    }
                }
            }
        }
    }

    protected void drawBackground(Graphics g)
    {
        Color colour;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                colour = background[r][c];
                if(colour != null) {
                    int x = c * cellSize + 100;
                    int y = r * cellSize;
                    g.setColor(colour);
                    g.fillRect(x, y, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawFrame(g);
        drawCurrentBlock(g);
        drawGhostBlock(g);
        drawQueueBlocks(g);
        drawHoldBlock(g);
        drawBackground(g);
    }

    // accessors
    public int getRows()
    {
        return rows;
    }

    public int getColumns()
    {
        return columns;
    }

    public int getCellSize()
    {
        return cellSize;
    }

    public Color[][] getBg()
    {
        return background;
    }

    public TetrisBlock getBlock() {
        return block;
    }

    public TetrisBlock getGhostBlock() {
        return ghostBlock;
    }

    public ArrayList<TetrisBlock> getBag() {
        return bag;
    }

    // mutators
    protected void setBackground(Color[][] background)
    {
        this.background = background;
    }

    protected void setBlock(TetrisBlock block)
    {
        this.block = block;
    }

    protected void setGhostBlock(TetrisBlock ghostBlock) { this.ghostBlock = ghostBlock; }

    protected void setBag(ArrayList<TetrisBlock> bag)
    {
        this.bag = bag;
    }
}