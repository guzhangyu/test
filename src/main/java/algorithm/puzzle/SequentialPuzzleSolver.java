package algorithm.puzzle;

import java.util.*;

/**
 * 迷宫算法
 * Created by guzy on 16/7/11.
 */
public class SequentialPuzzleSolver<P,M> {

    private final Puzzle<P,M> puzzle;

    private final Set<P> seen=new HashSet<P>();

    public SequentialPuzzleSolver(Puzzle<P,M> puzzle){
        this.puzzle=puzzle;
    }

    public List<M> solve(){
        P pos=puzzle.initialPosition();
        return searchDeeply(new MoveNode<P, M>(pos, null, null));
    }

    /**
     * 深度优先遍历
     * @param node
     * @return
     */
    public List<M> searchDeeply(MoveNode<P, M> node){
        if(!seen.contains(node.pos)){
            seen.add(node.pos);
            if(puzzle.isGoal(node.pos)){
                return node.asMoveList();
            }else{
                for(M m:puzzle.legalMoves(node.pos)){
                    P dest=puzzle.move(node.pos,m);
                    List<M> moves= searchDeeply(new MoveNode<P, M>(dest, m, node));
                    if(moves!=null){
                        return moves;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 宽度优先遍历
     * @param node
     * @return
     */
    public List<M> searchWidely(MoveNode<P,M> node){
        List<MoveNode<P,M>> nodes= Arrays.asList(node);
        while(nodes.size()>0){
            List<MoveNode<P,M>> curList=new ArrayList<MoveNode<P, M>>();
            for(MoveNode<P,M> n:nodes){
                seen.add(n.pos);
                if(puzzle.isGoal(n.pos)){
                    return n.asMoveList();
                }
                for(M m:puzzle.legalMoves(n.pos)){
                    MoveNode<P,M> moveNode=new MoveNode<P, M>(puzzle.move(n.pos,m),m,n);
                    if(!seen.contains(moveNode)){
                        curList.add(moveNode);
                    }
                }
            }
            nodes=curList;
        }
        return null;
    }

    public static void main(String[] args) {
        PuzzleImpl puzzle=null;
        List<Move> moves=null;
        do{
            puzzle=new PuzzleImpl(5,5);
            //SequentialPuzzleSolver resolver=new SequentialPuzzleSolver(algorithm.puzzle);
            ConcurrentPuzzleSolver resolver=new ConcurrentPuzzleSolver(puzzle);
            moves=resolver.solve();

        }while (moves.size()<2);
        Collections.reverse(moves);
        System.out.println(moves);
        for(int i=0;i<puzzle.arr.length;i++){
            StringBuffer sb1=new StringBuffer();
            StringBuffer sb2=new StringBuffer();
            StringBuffer sb3=new StringBuffer();
            for(int j=0;j<puzzle.arr[i].length;j++){
                Node node=puzzle.arr[i][j];
               if(node.isUp()){
                   sb1.append("   ↑   ");
               }else{
                   sb1.append("       ");
               }

                if(node.isLeft()){
                    sb2.append(" ←");
                }else{
                    sb2.append("  ");
                }
                sb2.append(node.getX()).append(",").append(node.getY());
                if(node.isRight()){
                    sb2.append("→ ");
                }else{
                    sb2.append("  ");
                }

                if(node.isDown()){
                    sb3.append("   ↓   ");
                }else{
                    sb3.append("       ");
                }
            }
            System.out.println(sb1);
            System.out.println(sb2);
            System.out.println(sb3);
        }
        System.out.println(puzzle.node);
        System.out.println(puzzle.dest);
    }
}
