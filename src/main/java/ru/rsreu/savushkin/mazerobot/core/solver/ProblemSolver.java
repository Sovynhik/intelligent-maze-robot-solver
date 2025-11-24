package ru.rsreu.savushkin.mazerobot.core.solver;

import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import java.util.List;

/**
 * Универсальный интерфейс интеллектуального решателя.
 */
public interface ProblemSolver {
    /**
     * Находит решение задачи, начиная с указанного стартового состояния.
     */
    <S extends State> List<S> solve(Environment<S, ?> env, S startState);
    String getName();
}