import java.awt.Color;

public class TetrisBlock {
    private int[][] shape;
    private final int type;
    private int x;
    private int y;
    private int rotation;
    private Color colour;
    private int queueOffset;

    public TetrisBlock(int t) {
        type = t;
        x = 3;
        y = 0;
        queueOffset = 0;
        rotation = 0;
        if(type == 0) // I
        {
            queueOffset = -5;
            shape = new int[][] {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}};
            colour = Color.cyan;
        }
        if (type == 1) // J
        {
            shape = new int[][] {{1, 0, 0}, {1, 1, 1}, {0, 0, 0}};
            colour = Color.blue;
        }
        if (type == 2) // L
        {
            shape = new int[][] {{0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
            colour = new Color(255, 165, 0);
        }
        if (type == 3) // Z
        {
            shape = new int[][] {{1, 1, 0}, {0, 1, 1}, {0, 0, 0}};
            colour = Color.red;
        }
        if (type == 4) // S
        {
            shape = new int[][] {{0, 1, 1}, {1, 1, 0}, {0, 0, 0}};
            colour = Color.green;
        }
        if (type == 5) // T
        {
            shape = new int[][] {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}};
            colour = Color.magenta;
        }
        if (type == 6) // O
        {
            x = 4;
            queueOffset = 5;
            shape = new int[][] {{1, 1}, {1, 1}};
            colour = Color.yellow;
        }
    }

    public TetrisBlock(int t, int x, int y, int[][] s)
    {
        this(t);
        this.x = x;
        this.y = y;
        shape = s;
    }

    public int[][] getShape()
    {
        return shape;
    }

    public int getType()
    {
        return type;
    }

    public int getHeight()
    {
        return shape.length;
    }

    public int getWidth()
    {
        return shape[0].length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getQueueOffset()  { return queueOffset; }

    public int getRotation() {
        return rotation;
    }

    public Color getColour()
    {
        return colour;
    }

    public void right()
    {
        x++;
    }

    public void left()
    {
        x--;
    }

    public void down()
    {
        y++;
    }

    public void move(int changeX, int changeY)
    {
        x += changeX;
        y -= changeY;
    }

    public void cw()
    {
        rotation = (rotation + 1) % 4;
        if (type == 0)
        {
            shape = new int[][] {{shape[3][0], shape[2][0], shape[1][0], shape[0][0]}, {shape[3][1], shape[2][1], shape[1][1], shape[0][1]}, {shape[3][2], shape[2][2], shape[1][2], shape[0][2]}, {shape[3][3], shape[2][3], shape[1][3], shape[0][3]}};
        }
        else if (type != 6)
        {
            shape = new int[][] {{shape[2][0], shape[1][0], shape[0][0]}, {shape[2][1], shape[1][1], shape[0][1]}, {shape[2][2], shape[1][2], shape[0][2]}};
        }
    }

    public void ccw() {
        rotation = (rotation + 3) % 4;
        if (type == 0)
        {
            shape = new int[][] {{shape[0][3], shape[1][3], shape[2][3], shape[3][3]}, {shape[0][2], shape[1][2], shape[2][2], shape[3][2]}, {shape[0][1], shape[1][1], shape[2][1], shape[3][1]}, {shape[0][0], shape[1][0], shape[2][0], shape[3][0]}};
        }
        else if (type != 6)
        {
            shape = new int[][] {{shape[0][2], shape[1][2], shape[2][2]}, {shape[0][1], shape[1][1], shape[2][1]}, {shape[0][0], shape[1][0], shape[2][0]}};
        }
    }

    public void flip()
    {
        cw();
        cw();
    }
}