package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Обобщенный класс, представляющий агента-робота.
 * <p>Агент содержит текущее состояние {@code S} и взаимодействует со средой {@code Environment}.</p>
 * <p>Реализует функциональность издателя (Publisher) в паттерне Наблюдатель, уведомляя слушателей об изменениях состояния.</p>
 *
 * @param <S> Тип состояния, в котором оперирует агент.
 */
public class RobotAgent<S extends State> {
    private S currentState;
    private final Environment<S, ?> environment;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Создает новый экземпляр агента.
     *
     * @param environment Среда, в которой действует агент.
     */
    public RobotAgent(Environment<S, ?> environment) {
        this.environment = environment;
        this.currentState = environment.getInitialState();
    }

    /**
     * Применяет действие к текущему состоянию через среду.
     * Состояние агента обновляется, если действие было валидным и привело к новому состоянию.
     *
     * @param action Действие, которое нужно применить (должно быть приведено к типу {@code Action}).
     * @return {@code true}, если состояние агента было изменено; {@code false} в противном случае.
     */
    public boolean applyAction(Object action) {
        S next = environment.applyAction(currentState, (Action) action);

        // Обновляем состояние, только если оно изменилось (т.е. ход был валиден).
        if (!next.equals(currentState)) {
            currentState = next;
            notifyListeners();
            return true;
        }
        return false;
    }

    /**
     * Возвращает текущее состояние агента.
     * @return Текущее состояние.
     */
    public S getCurrentState() { return currentState; }

    /**
     * Возвращает среду, в которой действует агент.
     * @return Среда.
     */
    public Environment<S, ?> getEnvironment() { return environment; }

    /**
     * Проверяет, достиг ли агент целевого состояния.
     * @return {@code true}, если агент находится в целевом состоянии; {@code false} в противном случае.
     */
    public boolean isAtGoal() { return environment.isGoal(currentState); }

    /**
     * Добавляет слушателя, который будет уведомлен об изменениях состояния агента.
     * @param l Слушатель.
     */
    public void addListener(Listener l) { listeners.add(l); }

    /**
     * Уведомляет всех зарегистрированных слушателей о событии.
     */
    private void notifyListeners() {
        for (var l : listeners) l.handle(new Event());
    }
}