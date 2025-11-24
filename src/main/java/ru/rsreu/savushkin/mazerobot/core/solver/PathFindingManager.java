package ru.rsreu.savushkin.mazerobot.core.solver;

import ru.rsreu.savushkin.mazerobot.core.solver.impl.BreadthFirstSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.impl.DepthFirstSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.impl.AStarSolver;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

public class PathFindingManager {
    private ProblemSolver currentStrategy;
    private final Map<String, ProblemSolver> solvers = new LinkedHashMap<>();

    public PathFindingManager() {
        register(new BreadthFirstSolver());
        register(new DepthFirstSolver());
        register(new AStarSolver());

        this.currentStrategy = solvers.values().iterator().next();
    }

    private void register(ProblemSolver solver) {
        solvers.put(solver.getName(), solver);
    }

    /**
     * Запускает поиск пути от динамического начального состояния.
     */
    public <S extends State> List<S> findPath(Environment<S, ?> env, S startState) {
        if (currentStrategy == null) throw new IllegalStateException("Algorithm not selected");
        return currentStrategy.solve(env, startState);
    }

    public void setAlgorithm(String name) {
        if (solvers.containsKey(name)) {
            currentStrategy = solvers.get(name);
        }
    }

    public String getCurrentAlgorithmName() {
        return currentStrategy.getName();
    }

    public Set<String> getAvailable() {
        return solvers.keySet();
    }
}