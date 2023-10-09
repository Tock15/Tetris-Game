import java.lang.Thread;

public class PuzzleThread extends Thread
{
    private final PuzzleArea pa;
    private boolean solved;

    public PuzzleThread(PuzzleArea pa)
    {
        this.pa = pa;
        solved = false;
    }

    public void run()
    {
        while (!solved) {
            pa.blockPlaced = false;
            // waits until block is placed
            while (!pa.blockPlaced) System.out.print("");
            // attempts to clear lines
            pa.clearLines();
            solved = pa.isPC();
        }
        System.out.println("Level " + pa.level + " Clear!\nPress 'esc' to return to level selection");
    }
}