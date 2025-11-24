package ru.rsreu.savushkin.mazerobot.core.solver.engine;

import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Обобщенный класс для хранения состояния (узла) в дереве/графе поиска.
 * Улучшен для A* поиска: хранит стоимость пути g(n) и общую стоимость f(n).
 */
public class Situation<S extends State> implements Comparable<Situation<S>> { // <--- Реализует Comparable
    private final S state;
    private final Situation<S> parent;
    private final Action action;
    private final int depth; // Глубина в дереве (количество действий)
    private final double gCost; // Стоимость пути от старта до текущего узла g(n)
    private double fCost; // Общая оценочная стоимость f(n) = g(n) + h(n)

    public Situation(S state) {
        this(state, null, null, 0, 0);
    }

    // Обновленный конструктор
    public Situation(S state, Situation<S> parent, Action action, int depth, double gCost) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.depth = depth;
        this.gCost = gCost; // <--- Сохраняем g(n)
        // fCost будет рассчитана в решателе AStarSolver
    }

    public S getState() { return state; }
    public Situation<S> getParent() { return parent; }
    public Action getAction() { return action; }
    public int getDepth() { return depth; }
    public double getGCost() { return gCost; }

    // Геттер и сеттер для F-стоимости
    public double getFCost() { return fCost; }
    public void setFCost(double fCost) { this.fCost = fCost; }

    public boolean hasLoop(S target) {
        Situation<S> current = this;
        while (current != null) {
            if (current.getState().equals(target)) return true;
            current = current.getParent();
        }
        return false;
    }

    @Override
    public int compareTo(Situation<S> other) {
        // Сравнение по F-стоимости: для PriorityQueue, A* ищет минимальную стоимость
        return Double.compare(this.fCost, other.fCost);
    }
}