package ru.rsreu.savushkin.mazerobot.core.solver.engine;

import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.State;

/**
 * Обобщенный класс для хранения узла (ситуации) в дереве или графе поиска.
 * <p>Используется для восстановления пути и хранения оценочных функций для алгоритмов,
 * таких как A* (A-Star), где необходимы gCost и fCost.</p>
 *
 * @param <S> Тип состояния, которое хранится в узле.
 */
public class Situation<S extends State> implements Comparable<Situation<S>> {
    private final S state;
    private final Situation<S> parent;
    private final Action action;
    private final int depth;
    private final double gCost;
    private double fCost;

    /**
     * Конструктор для создания корневого узла.
     *
     * @param state Начальное состояние.
     */
    public Situation(S state) {
        this(state, null, null, 0, 0);
    }

    /**
     * Полный конструктор для создания новой ситуации в процессе поиска.
     *
     * @param state Текущее состояние.
     * @param parent Родительская ситуация.
     * @param action Действие, которое привело из родителя в текущее состояние.
     * @param depth Глубина в дереве (количество действий).
     * @param gCost Стоимость пути от начального состояния до текущего узла.
     */
    public Situation(S state, Situation<S> parent, Action action, int depth, double gCost) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.depth = depth;
        this.gCost = gCost;
    }

    /**
     * Возвращает текущее состояние узла.
     * @return Состояние.
     */
    public S getState() { return state; }

    /**
     * Возвращает родительский узел (для восстановления пути).
     * @return Родительский узел.
     */
    public Situation<S> getParent() { return parent; }

    /**
     * Возвращает действие, которое привело в этот узел.
     * @return Действие.
     */
    public Action getAction() { return action; }

    /**
     * Возвращает глубину узла (количество действий от начала).
     * @return Глубина.
     */
    public int getDepth() { return depth; }

    /**
     * Возвращает стоимость пути g(n) от начального состояния.
     * @return Стоимость g(n).
     */
    public double getGCost() { return gCost; }

    /**
     * Возвращает полную оценочную стоимость f(n) = g(n) + h(n).
     * @return Стоимость f(n).
     */
    public double getFCost() { return fCost; }

    /**
     * Устанавливает полную оценочную стоимость f(n).
     * @param fCost Новая стоимость f(n).
     */
    public void setFCost(double fCost) { this.fCost = fCost; }

    /**
     * Проверяет, не содержит ли путь от корня до текущего узла заданное целевое состояние.
     * <p>Используется для предотвращения циклов в DFS и Minimax.</p>
     * @param target Состояние, которое нужно проверить.
     * @return {@code true}, если целевое состояние уже было посещено в этой ветке.
     */
    public boolean hasLoop(S target) {
        Situation<S> current = this;
        while (current != null) {
            if (current.getState().equals(target)) return true;
            current = current.getParent();
        }
        return false;
    }

    /**
     * Определяет порядок узлов в приоритетной очереди (для A*).
     * <p>Узел с меньшей fCost имеет более высокий приоритет.</p>
     * @param other Другой узел для сравнения.
     * @return Результат сравнения fCost.
     */
    @Override
    public int compareTo(Situation<S> other) {
        return Double.compare(this.fCost, other.fCost);
    }
}