package algorithm.puzzle;

import java.util.Set;

/**
 * Created by guzy on 16/7/11.
 */
public interface Puzzle<P,M> {
    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position,M move);
}
