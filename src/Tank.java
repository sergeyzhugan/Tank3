public class Tank {
    private int speed = 1;
    private int x;
    private int y;

//    1 - up, 2 - down, 3 - left, 4 - right
    private int direction;

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    private ActionField af;
    private BattleField bf;

    public void turn(int direction) {
        this.direction = direction;
        af.processTurn(this);
    }

    public boolean move(int direction) {
        turn(direction);

        if( quadrantPosition() && isNotFreeField(direction)){
            return false;
        }

        if ( !af.checkCoordinates() ) {
            return false;
        }

        af.processMove(this);

        return true;
    }

    void clean() {
        moveToQuadrant(bf.getFirstLine(), bf.getFirstLine());

        for (int i = bf.getFirstLine(); i <= bf.getLastLine(); i++) {
            moveToQuadrant(bf.getFirstLine(), i);
            turn(2);

            if (!zalp()) {
                return;
            }

        }
    }

    boolean zalp() {
        while (true) {
            int B = bf.getB();

            fire();

            if (B == 0)
                return false;

            if (B == bf.getB()) {
                return true;
            }
        }
    }

    public void fire() {
        Bullet bullet = new Bullet((x + 25), (y + 25), direction);
        af.processFire(bullet);
    }

    private void moveToQuadrant() {

    }

    public Tank(ActionField af, BattleField bf) {
        this(af, bf, 128, 512, 1);
    }

    public Tank(ActionField af, BattleField bf, int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.af = af;
        this.bf = bf;
    }

    boolean quadrantPosition(){
        return (this.getX() == 0 && this.getY() == 0) 									? true :
                ((this.getX() % bf.getQuadrantSize()) == 0 && (this.getY() == 0)) 				? true :
                        ((this.getY() % bf.getQuadrantSize()) == 0 && (this.getX() == 0)) 				? true :
                                ((this.getX() % bf.getQuadrantSize()) == 0 && (this.getY() % bf.getQuadrantSize() == 0)) ? true :
                                        false;
    }

    String checkMove(String quadrant, int direction) {
        int delimiter = quadrant.indexOf("_");

        int y = Integer.valueOf(quadrant.substring(0, delimiter));
        int x = Integer.valueOf(quadrant.substring(delimiter + 1));

        int newX = (direction == 3)  ? x-1 :
                (direction == 4) ? x+1 :
                        x;

        int newY = (direction == 1)    ? y-1 :
                (direction == 2) ? y+1 :
                        y;

        if( bf.isOutQuadrant(newY, newX)) {
            return "out";
        }

        if (bf.scanQuadrant(newX-1, newY-1).equals("B")) {
            return "B";
        }

        return "free";
    }

    boolean isNotFreeField(int direction) {
        return !checkMove(af.getQuadrant(this.getX(), this.getY()), direction).equals("free") ? true :
                false;
    }

    public void updateX(int x) {
        this.x += x;
    }
    public void updateY(int y) {
        this.y += y;
    }

    private void moveToXLine(int x) {
        while (this.getX() != x) {
            moveToX(x, 3, this.getX() > x);
            moveToX(x, 4, this.getX() < x);
        }

    }

    private void moveToYLine(int y) {
        while (this.getY() != y) {
            moveToY(y, 1, this.getY() > y);
            moveToY(y, 2, this.getY() < y);
        }
    }

    private void moveToX(int x, int dir, boolean i) {
        if (!i) {
            return;
        }

        while (this.getX() != x) {
            if (!move(dir)) {
                fire();
            }
        }
    }

    private void moveToY(int y, int dir, boolean i) {
        if (!i) {
            return;
        }

        while (this.getY() != y) {
            if (!move(dir)) {
                fire();
            }
        }
    }

    public void moveToQuadrant(int v, int h) {
        String quadrantCoordinates = af.getQuadrantXY(v, h);
        int delimiter = quadrantCoordinates.indexOf("_");

        int x = Integer.valueOf(quadrantCoordinates.substring(0, delimiter));
        int y = Integer.valueOf(quadrantCoordinates.substring(delimiter + 1));

        moveToXLine(x);
        moveToYLine(y);
    }

    public void moveRandom() {
        while (true) {
            int direction = (int) (System.currentTimeMillis() % 4) + 1;

            String go = checkMove(af.getQuadrant(this.getX(), this.getY()), direction);

            if (go.equals("out")) {
                continue;
            }

            if (go.equals("B")) {
                fire();
            }

            for(int i = 0; i < bf.getQuadrantSize(); i++){
                move(direction);
            }

        }
    }

}
