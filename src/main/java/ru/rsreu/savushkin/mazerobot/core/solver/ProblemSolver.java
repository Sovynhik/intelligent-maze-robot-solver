package ru.rsreu.savushkin.mazerobot.core.solver;

import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import java.util.List;

/**
 * Универсальный интерфейс для реализации алгоритмов решения задач (Problem Solver).
 * <p>Определяет основной контракт для всех стратегий поиска пути.</p>
 */
public interface ProblemSolver {
    /**
     * Находит решение задачи (путь) в заданной среде, начиная с указанного стартового состояния.
     *
     * @param env Среда, в которой происходит поиск.
     * @param startState Начальное состояние.
     * @param <S> Тип состояния, в котором оперирует решатель.
     * @return Список состояний, формирующих найденный путь.
     */
    <S extends State> List<S> solve(Environment<S, ?> env, S startState);

    /**
     * Возвращает имя решателя (алгоритма).
     * @return Имя решателя.
     */
    String getName();
}