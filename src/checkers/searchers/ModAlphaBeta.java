package checkers.searchers;
import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

public class ModAlphaBeta extends CheckersSearcher {
    private int numNodes = 0;
    public ModAlphaBeta(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelp(board, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private Optional<Duple<Integer, Move>> selectMoveHelp(Checkerboard board, int depth, int alpha, int beta) {
        // Algorithm goes here.
        // In particular, recursive calls look like:
        // Optional<Duple<Integer, Move>> recursiveResult = selectMoveHelp(someOtherBoard, depth + 1);

        Optional<Duple<Integer, Move>> best = Optional.empty();

        if (board.gameOver()){
            if(board.playerWins(board.getCurrentPlayer())){
                return Optional.of(new Duple<>(Integer.MAX_VALUE, board.getLastMove()));
            }
            if(board.playerWins(board.getCurrentPlayer().opponent())){
                return Optional.of(new Duple<>(-Integer.MAX_VALUE, board.getLastMove()));
            }
            return Optional.of(new Duple<>(0, board.getLastMove()));
        }

        if (getDepthLimit() < depth && board.allCaptureMoves(board.getCurrentPlayer()).isEmpty()){
            return Optional.of(new Duple<>(getEvaluator().applyAsInt(board), board.getLastMove()));
        }



        //ordering heuristic

        PriorityQueue<Checkerboard> boards = new PriorityQueue<Checkerboard>(Comparator.comparingInt(b -> -1 * getEvaluator().applyAsInt(b)));

        boards.addAll(board.getNextBoards());

        for (Checkerboard alternative: boards) {
            numNodes += 1;
            int negation = board.getCurrentPlayer() != alternative.getCurrentPlayer() ? -1 : 1;
            int scoreFor = negation * selectMoveHelp(alternative, depth+1, -beta, -alpha).get().getFirst();
            if (best.isEmpty() || best.get().getFirst() < scoreFor) {
                best = Optional.of(new Duple<>(scoreFor, alternative.getLastMove()));
                alpha = Math.max(alpha, scoreFor);
            }
            if(alpha >= beta){
                return best;
            }
        }
        return best;
    }
}