package ru.rsreu.savushkin.mazerobot.core.solver;

import ru.rsreu.savushkin.mazerobot.core.solver.impl.BreadthFirstSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.impl.DepthFirstSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.impl.AStarSolver;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;
import ru.rsreu.savushkin.mazerobot.core.solver.impl.GradientSolver;

import java.util.*;

/**
 * Менеджер поиска пути (PathFindingManager).
 * <p>Предоставляет интерфейс для выбора и запуска различных алгоритмов поиска пути
 * (DFS, BFS, A*) в пространстве состояний.</p>
 */
public class PathFindingManager {
    private ProblemSolver currentStrategy;
    private final Map<String, ProblemSolver> solvers = new LinkedHashMap<>();

    /**
     * Инициализирует менеджер, регистрируя все доступные алгоритмы поиска.
     * <p>По умолчанию устанавливает первый зарегистрированный алгоритм как текущий.</p>
     */
    public PathFindingManager() {
        register(new BreadthFirstSolver());
        register(new DepthFirstSolver());
        register(new AStarSolver());
        register(new GradientSolver());

        this.currentStrategy = solvers.values().iterator().next();
    }

    /**
     * Регистрирует новый решатель в системе, используя его имя как ключ.
     * @param solver Реализация алгоритма {@link ProblemSolver}.
     */
    private void register(ProblemSolver solver) {
        solvers.put(solver.getName(), solver);
    }

    /**
     * Запускает поиск пути от заданного начального состояния с использованием текущего выбранного алгоритма.
     *
     * @param env Среда (Environment), в которой происходит поиск.
     * @param startState Начальное состояние.
     * @param <S> Тип состояния.
     * @return Список состояний, составляющих найденный путь.
     * @throws IllegalStateException если не выбран ни один алгоритм.
     */
    public <S extends State> List<S> findPath(Environment<S, ?> env, S startState) {
        if (currentStrategy == null) throw new IllegalStateException("Algorithm not selected");
        return currentStrategy.solve(env, startState);
    }

    /**
     * Устанавливает текущий алгоритм поиска по его имени.
     * @param name Название алгоритма.
     */
    public void setAlgorithm(String name) {
        if (solvers.containsKey(name)) {
            currentStrategy = solvers.get(name);
        }
    }

    /**
     * Возвращает имя текущего выбранного алгоритма.
     * @return Имя текущего алгоритма.
     */
    public String getCurrentAlgorithmName() {
        return currentStrategy.getName();
    }

    /**
     * Возвращает набор имен всех доступных алгоритмов поиска.
     * @return Набор имен алгоритмов.
     */
    public Set<String> getAvailable() {
        return solvers.keySet();
    }
}