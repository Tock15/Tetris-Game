import java.lang.Thread;
import java.util.Timer;

public class GameThread extends Thread
{
    private final GameArea ga;
    private int speed;
    private int level;
    private final int levelReq = 10; // change this for level requirement
    private int totalLines;
    private int score;
    private double scoreMultiplier;
    private int combo;
    private int b2b;

    public GameThread(GameArea ga)
    {
        this.ga = ga;
        speed = 1000;
        level = 1;
        totalLines = 0;
        score = 0;
        scoreMultiplier = 1;
        combo = -1;
        b2b = -1;
    }

    public void run()
    {
        while (!ga.topOut) {
            ga.dropScore = 0;
            ga.spawnBlock();
            ga.blockPlaced = false;
            ga.canHold = true;
            int tSpinType;
            int linesCleared;
            try {
                ga.gravityTimer = new Timer();
                ga.gravityTimer.schedule(ga.new GravityTask(), 0, speed);
                ga.lockTimer = null;
                ga.lockDelay = 500;
                // waits until block is placed
                while (!ga.blockPlaced) System.out.print("");
                // cancels gravity and attempts to clear lines
                ga.gravityTimer.cancel();
                // level & scoring
                score += ga.dropScore;
                tSpinType = ga.isTSpin();
                linesCleared = ga.clearLines();
                totalLines += linesCleared;
            } catch (IllegalArgumentException e) {
                Tetris.endSpecial(score);
                break;
            }
            if (tSpinType == 0) // normal scoring
            {
                if (ga.isPC()) {
                    score += 3500 * level;
                    System.out.println("ALL CLEAR");
                } else if (linesCleared == 1) {
                    score += 100 * level;
                    System.out.println("Single");
                    scoreMultiplier = 1;
                    b2b = -1;
                } else if (linesCleared == 2) {
                    score += 300 * level;
                    System.out.println("Double");
                    scoreMultiplier = 1;
                    b2b = -1;
                } else if (linesCleared == 3) {
                    score += 500 * level;
                    System.out.println("Triple");
                    scoreMultiplier = 1;
                    b2b = -1;
                } else if (linesCleared == 4) {
                    score += 800 * level * scoreMultiplier;
                    scoreMultiplier = 1.5;
                    b2b++;
                    if (b2b > 0) System.out.print("B2B x" + b2b + " ");
                    System.out.println("Tetris");
                }
            }
            else if (tSpinType == 1) // Mini T-Spins
            {
                if (linesCleared == 0) {
                    score += 100 * level;
                    System.out.println("T-Spin Mini");
                } else if (linesCleared == 1) {
                    score += 200 * level * scoreMultiplier;
                    scoreMultiplier = 1.5;
                    b2b++;
                    if (b2b > 0) System.out.print("B2B x" + b2b + " ");
                    System.out.println("T-Spin Mini Single");
                } else if (linesCleared == 2) {
                    score += 400 * level * scoreMultiplier;
                    scoreMultiplier = 1.5;
                    b2b++;
                    if (b2b > 0) System.out.print("B2B x" + b2b + " ");
                    System.out.println("T-Spin Mini Double");
                }
            } else if (tSpinType == 2) // Real T-Spins
            {
                if (linesCleared == 0) {
                    score += 400 * level;
                    System.out.println("T-Spin");
                } else if (linesCleared == 1) {
                    score += 800 * level * scoreMultiplier;
                    scoreMultiplier = 1.5;
                    b2b++;
                    if (b2b > 0) System.out.print("B2B x" + b2b + " ");
                    System.out.println("T-Spin Single");
                } else if (linesCleared == 2) {
                    score += 1200 * level * scoreMultiplier;
                    scoreMultiplier = 1.5;
                    b2b++;
                    if (b2b > 0) System.out.print("B2B x" + b2b + " ");
                    System.out.println("T-Spin Double");
                } else if (linesCleared == 3) {
                    score += 1600 * level * scoreMultiplier;
                    scoreMultiplier = 1.5;
                    b2b++;
                    if (b2b > 0) System.out.print("B2B x" + b2b + " ");
                    System.out.println("T-Spin Triple");
                }
            }
            if (linesCleared == 0) combo = -1;
            else combo++;
            if (combo > 0) {
                score += 50 * combo * level;
                System.out.println("Combo x" + combo);
            }
            System.out.println("Score: " + score);
            int lvl = totalLines / levelReq + 1;
            if(lvl > level) {
                level = lvl;
                System.out.println("LEVEL: " + level);
                speed = (int) (1000 * Math.pow(0.8 - ((level - 1) * 0.007), level - 1));
            }
            if(ga.topOut){
                ga.canHold = false;
                System.out.println("Game Over!");
                System.out.println("Lines Cleared: "+ totalLines);
                System.out.println("Score: " + score);
                Tetris.end(score);
                break;
            }
        }
    }
}