package ru.rsreu.savushkin.mazerobot.core.state;

import java.util.List;

/**
 * Окружение (Предметная область).
 * Определяет правила игры: начальное состояние, цель, возможные ходы.
 * @param <S> Тип состояния.
 * @param <A> Тип действия.
 */
public interface Environment<S extends State, A extends Action> {
    S getInitialState();
    S getGoalState();
    boolean isValid(S state);
    boolean isGoal(S state);
    List<A> getPossibleActions(S state);
    S applyAction(S state, Action action);
}