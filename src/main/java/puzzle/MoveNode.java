package puzzle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by guzy on 16/7/11.
 */
public class MoveNode <P,M> {

    public final P pos;

    final M move;

    final MoveNode<P,M> prev;

    MoveNode(P pos,M move,MoveNode<P,M> prev){
        this.pos=pos;
        this.prev=prev;
        this.move=move;
    }

    List<M> asMoveList(){
        List<M> solution=new LinkedList<M>();
        for(MoveNode<P,M> n=this;n.move!=null;n=n.prev){
            solution.add(n.move);
        }
        return solution;
    }
}
