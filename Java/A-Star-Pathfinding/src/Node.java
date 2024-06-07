public class Node {

    int x=0, y=0, f=0, g=0, h=0;
    Node parent;
    boolean isWall;

    public Node (int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }


}