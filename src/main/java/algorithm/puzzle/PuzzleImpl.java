package algorithm.puzzle;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Created by guzy on 18/2/5.
 */
public class PuzzleImpl implements Puzzle<Node,Move> {

    Node node;

    Node dest;

    Node[][]arr;

    public PuzzleImpl(int x,int y) {
        arr=new Node[x][y];
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                arr[i][j]=new Node(i,j,new Random().nextInt(16),x,y);
            }
        }
        node=arr[new Random().nextInt(x)][new Random().nextInt(y)];

        dest=node;
        for(int i=0;i<8;i++){
            if(CollectionUtils.isEmpty(node.getMoves())){
                break;
            }
            dest=move(node,new ArrayList<Move>(node.getMoves()).get(new Random().nextInt(node.getMoves().size())));
        }
        //dest=arr[new Random().nextInt(x)][new Random().nextInt(y)];
    }

    @Override
    public Node initialPosition() {
        return node;
    }

    @Override
    public boolean isGoal(Node position) {
        return dest.equals(position);
    }

    @Override
    public Set<Move> legalMoves(Node position) {
        return position.getMoves();
    }

    @Override
    public Node move(Node position, Move move) {
        return arr[position.getX()+move.getX()][position.getY()+move.getY()];
    }
}
