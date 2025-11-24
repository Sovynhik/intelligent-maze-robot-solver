package ru.rsreu.savushkin.mazerobot.core.solver;

import ru.rsreu.savushkin.mazerobot.core.solver.impl.BreadthFirstSolver;
import ru.rsreu.savushkin.mazerobot.core.solver.impl.DepthFirstSolver;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

public class PathFindingManager {
    private ProblemSolver currentStrategy;
    private final Map<String, ProblemSolver> solvers = new LinkedHashMap<>();

    public PathFindingManager() {
        register(new BreadthFirstSolver());
        register(new DepthFirstSolver());

        // По умолчанию BFS
        this.currentStrategy = solvers.values().iterator().next();
    }

    private void register(ProblemSolver solver) {
        solvers.put(solver.getName(), solver);
    }

    public <S extends State> List<S> findPath(Environment<S, ?> env) {
        if (currentStrategy == null) throw new IllegalStateException("Algorithm not selected");
        return currentStrategy.solve(env);
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