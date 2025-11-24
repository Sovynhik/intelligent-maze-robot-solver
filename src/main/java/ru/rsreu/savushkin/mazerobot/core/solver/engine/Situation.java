package ru.rsreu.savushkin.mazerobot.core.solver.engine;

import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.State;

/**
 * Ситуация (Узел дерева поиска).
 * Хранит состояние, ссылку на родителя (историю) и глубину.
 */
public class Situation<S extends State> {
    private final S state;
    private final Situation<S> parent;
    private final Action action;
    private final int depth;

    public Situation(S state) {
        this(state, null, null, 0);
    }

    public Situation(S state, Situation<S> parent, Action action, int depth) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.depth = depth;
    }

    public S getState() { return state; }
    public Situation<S> getParent() { return parent; }
    public int getDepth() { return depth; }

    /**
     * Проверяет наличие цикла в ТЕКУЩЕЙ ветке (от текущего узла до корня).
     * Используется для DFS, чтобы не хранить глобальный список посещенных.
     */
    public boolean hasLoop(S targetState) {
        Situation<S> current = this;
        while (current != null) {
            if (current.state.equals(targetState)) {
                return true;
            }
            current = current.parent;
        }
        return false;
    }
}