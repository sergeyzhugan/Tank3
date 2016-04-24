public class BattleField {
    private int BF_WIDTH = 576;
    private int BF_HEIGHT = 576;

    private int firstLine = 1;
    private int lastLine = 9;

    private int leftLimit = 0;
    private int topLimit = 0;
    private int rightLimit = 64 * 8;
    private int downLimit = 64 * 8;

    public int getFirstLine() {
        return firstLine;
    }

    public int getLastLine() {
        return lastLine;
    }

    public int getLeftLimit() {
        return leftLimit;
    }

    public int getTopLimit() {
        return topLimit;
    }

    public int getRightLimit() {
        return rightLimit;
    }

    public int getDownLimit() {
        return downLimit;
    }

    public int getBF_WIDTH() {
        return BF_WIDTH;
    }

    public int getBF_HEIGHT() {
        return BF_HEIGHT;
    }

    public int getQuadrantSize() {
        return quadrantSize;
    }

    public int getStepSize() {
        return stepSize;
    }

    private int quadrantSize = 64;
    private int stepSize = 1;


    String[][] battleField = { { " ", "B", "B", " ", " ", "B", " ", " ", " " },
            { "B", "B", "B", "B", "B", "B", "B", "B", " " }, { "B", "B", "B", " ", " ", " ", " ", " ", " " },
            { " ", "B", "B", " ", "B", "B", "B", "B", " " }, { " ", "B", " ", " ", " ", " ", " ", " ", " " },
            { "B", "B", "B", " ", "B", "B", "B", "B", " " }, { " ", "B", "B", " ", " ", " ", " ", " ", " " },
            { " ", "B", "B", "B", "B", "B", "B", "B", " " }, { "B", " ", " ", " ", " ", " ", "B", "B", "B" } };

    public BattleField() {
    }

    public int getDimensionX() {
        return battleField[0].length;
    }

    public int getDimensionY() {
        return battleField.length;
    }

    public String scanQuadrant(int x, int y) {
        return battleField[y][x];
    }

    public void updateQuadrant(int x, int y, String newFieldType) {
        battleField[y][x] = newFieldType;
    }

    public int getB() {
        int B = 0;
        for (String[] str : battleField) {
            for (String el : str) {
                if (el.equals("B")) {
                    B++;
                }
            }
        }

        return B;
    }

    public boolean isOutQuadrant(int x, int y) {
        return (x > this.getLastLine() ||
                y > this.getLastLine() ||
                x < this.getFirstLine() ||
                y < this.getFirstLine()) ? true : false;
    }

}
