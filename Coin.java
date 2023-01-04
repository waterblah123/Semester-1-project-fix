public class Coin {
    // 0 = yellow, 1 = red
    private int color;

    // goes from 1 - 7
    private int xpos;

    // goes from 1-6
    private int ypos;

    public Coin(int c, int x, int y){
        color = c;
        xpos = x;
        ypos = y;
    }

    public int getColor(){
        return color;
    }

    public int getXpos(){
        return xpos;
    }

    public int getYpos(){
        return ypos;
    }
}
