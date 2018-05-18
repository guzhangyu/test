package algorithm.puzzle;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guzy on 16/7/11.
 */
public class Node {

    private int x,y,dirs;

    private boolean up,down,left,right;

    Set<Move> moves=new HashSet<Move>() ;


    public Node() {
    }

    public Node(int x, int y,int dirs,int width,int height) {
        this.x = x;
        this.y = y;

        up=x>0 && dirs%2==1;
        if(up){
            moves.add(Move.UP);
        }

        dirs/=2;
        down=x<height-1 && dirs%2==1;
        if(down){
            moves.add(Move.DOWN);
        }

        dirs/=2;
        left=y>0 && dirs%2==1;
        if(left){
            moves.add(Move.LEFT);
        }

        dirs/=2;
        right=y<width-1 && dirs%2==1;
        if(right){
            moves.add(Move.RIGHT);
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", dirs=" + dirs +
                ", up=" + up +
                ", down=" + down +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public int getDirs() {
        return dirs;
    }

    public void setDirs(int dirs) {
        this.dirs = dirs;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Integer getI() {
        return i;
    }

    public static void setI(Integer i) {
        Node.i = i;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !(obj instanceof Node)){
            return false;
        }
        Node node=(Node)obj;
        return node.x==x&&node.y==y;
    }

    @Override
    public int hashCode() {
        return x*600+y;
    }

    static Integer i=0;

    public List<Node> children;

    public Integer compute(){
        return i++;
    }

    public void parallelRecursively(final Executor executor,List<Node> nodes,final List<Integer> results){
        if(nodes==null){
            return;
        }
        for(final Node node:nodes){
           executor.execute(new Runnable() {
               @Override
               public void run() {
                   results.add( node.compute());
               }
           });
            parallelRecursively(executor,node.children,results);
        }
    }

    public static void main(String[] args) {
        Node node=new Node();
        List<Node> nodes=new ArrayList<Node>();
        for(int i=0;i<10;i++){
            nodes.add(new Node());
        }
        node.children=nodes;
        ExecutorService executor= Executors.newFixedThreadPool(10);
        List<Integer> results=new ArrayList<Integer>();
        node.parallelRecursively(executor, Arrays.asList(node),results);
        for(Integer i:results){
            System.out.println("i:"+i);
        }
        executor.shutdown();
    }
}
