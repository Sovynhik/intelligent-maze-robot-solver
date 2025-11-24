package ru.rsreu.savushkin.mazerobot.core.solver;

import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import java.util.List;

/**
 * Универсальный интерфейс решателя задач.
 */
public interface ProblemSolver {
    <S extends State> List<S> solve(Environment<S, ?> env);
    String getName();
}