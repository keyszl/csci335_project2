package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class kings implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {

        //since kings can move backwards they have "double" value in this evaluator
        return (c.numPiecesOf(c.getCurrentPlayer()) + c.numKingsOf(c.getCurrentPlayer()))
                - (c.numPiecesOf(c.getCurrentPlayer().opponent()) + c.numKingsOf(c.getCurrentPlayer().opponent()));
    }
}
