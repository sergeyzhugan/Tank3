import javax.swing.*;
import java.awt.*;

public class ActionField extends JPanel {
    private boolean COLORDED_MODE = false;

    private BattleField bf;
    private Bullet bullet;
    private Tank tank;

    public void runTheGame() {
        tank.clean();
    }

    public boolean checkCoordinates() {
        if (tank.getY() >= bf.getTopLimit() &&
            tank.getY() <= bf.getDownLimit() &&
            tank.getX() >= bf.getLeftLimit() &&
            tank.getX() <= bf.getRightLimit()) {
            return true;
        }
        return false;
    }

    public void processTurn(Tank tank) {
        reset();
    }

    public void processMove(Tank tank) {
        int value = bf.getStepSize();

        int direction = tank.getDirection();

        if (direction == 1 || direction == 3) {
            value *= -1;
        }

        if (direction == 1 || direction == 2) {
            tank.updateY(value);
        }

        if (direction == 3 || direction == 4) {
            tank.updateX(value);
        }

        reset();
    }

    private void reset() {
        repaint();

        try {
            Thread.sleep(bullet.getSpeed());
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    private boolean checkBulletCoordinates(Bullet bullet) {
        int corrective = 10;

        return (bullet.getY() > corrective ||
                bullet.getY() < bf.getBF_HEIGHT() - corrective ||
                bullet.getX() < bf.getBF_WIDTH() - corrective ||
                bullet.getX() > corrective) ?
                true : false;
    }

    public void processFire(Bullet bullet) {
        this.bullet = bullet;
        int direction = bullet.getDirection();

        int value = bullet.getSpeed();

        if (direction == 1 || direction == 3) {
            value *= -1;
        }

        if (direction == 1 || direction == 2) {
            do {
                bullet.updateY(value);
                reset();
            } while (checkBulletCoordinates(bullet) && !processInterception(bullet));
        }

        if (direction == 3 || direction == 4) {
            do {
                bullet.updateX(value);
                reset();
            } while (checkBulletCoordinates(bullet) && !processInterception(bullet));
        }

        bullet.destroy();
    }

    private boolean processInterception(Bullet bullet) {
        String quadrant = getQuadrant(bullet.getX(), bullet.getY());

        int delimiter = quadrant.indexOf("_");

        int y = Integer.valueOf(quadrant.substring(0, delimiter)) - 1;
        int x = Integer.valueOf(quadrant.substring(delimiter + 1)) - 1;


        if (bf.isOutQuadrant(y + 1, x + 1)) {
            return true;
        }


        if (!bf.scanQuadrant(x, y).equals(" ") ) {
            bf.updateQuadrant(x,y, " ");
            return true;
        }

        return false;
    }

   public String getQuadrantXY(int v, int h) {
        int x = (h - 1) * bf.getQuadrantSize();
        int y = (v - 1) * bf.getQuadrantSize();

        return x + "_" + y;
    }

   public String getQuadrant(int x, int y) {
        int quadrantX = (x / bf.getQuadrantSize()) + 1;
        int quadrantY = (y / bf.getQuadrantSize()) + 1;
        return quadrantY + "_" + quadrantX;
    }


    public ActionField() throws Exception {
        bf = new BattleField();
        tank = new Tank(this, bf);
        bullet = new Bullet(-100, -100, -1);

        JFrame frame = new JFrame("BATTLE FIELD, DAY 2");
        frame.setLocation(750, 150);
        frame.setMinimumSize(new Dimension(bf.getBF_WIDTH(), bf.getBF_HEIGHT() + 22));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int i = 0;
        Color cc;
        for (int v = 0; v < 9; v++) {
            for (int h = 0; h < 9; h++) {
                if (COLORDED_MODE) {
                    if (i % 2 == 0) {
                        cc = new Color(252, 241, 177);
                    } else {
                        cc = new Color(233, 243, 255);
                    }
                } else {
                    cc = new Color(180, 180, 180);
                }
                i++;
                g.setColor(cc);
                g.fillRect(h * 64, v * 64, 64, 64);
            }
        }

        for (int j = 0; j < bf.getDimensionY(); j++) {
            for (int k = 0; k < bf.getDimensionX(); k++) {
                if (bf.scanQuadrant(k,j).equals("B")) {
                    String coordinates = getQuadrantXY(k + 1, j + 1);
                    int separator = coordinates.indexOf("_");
                    int y = Integer.parseInt(coordinates.substring(0, separator));
                    int x = Integer.parseInt(coordinates.substring(separator + 1));
                    g.setColor(new Color(0, 0, 255));
                    g.fillRect(x, y, 64, 64);
                }
            }
        }

        g.setColor(new Color(255, 0, 0));
        g.fillRect(tank.getX(), tank.getY(), 64, 64);

        g.setColor(new Color(0, 255, 0));
        if (tank.getDirection() == 1) {
            g.fillRect(tank.getX() + 20, tank.getY(), 24, 34);
        } else if (tank.getDirection() == 2) {
            g.fillRect(tank.getX() + 20, tank.getY() + 30, 24, 34);
        } else if (tank.getDirection() == 3) {
            g.fillRect(tank.getX(), tank.getY() + 20, 34, 24);
        } else {
            g.fillRect(tank.getX() + 30, tank.getY() + 20, 34, 24);
        }

        g.setColor(new Color(255, 255, 0));
        g.fillRect(bullet.getX(), bullet.getY(), 14, 14);
    }

}
