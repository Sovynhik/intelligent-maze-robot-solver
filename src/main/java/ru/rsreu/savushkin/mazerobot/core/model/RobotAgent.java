package ru.rsreu.savushkin.mazerobot.core.model;

import ru.rsreu.savushkin.mazerobot.core.entity.Event;
import ru.rsreu.savushkin.mazerobot.core.state.Action;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.ArrayList;
import java.util.List;

public class RobotAgent<S extends State> {
    private S currentState;
    private final Environment<S, ?> environment;
    private final List<Listener> listeners = new ArrayList<>();

    public RobotAgent(Environment<S, ?> environment) {
        this.environment = environment;
        this.currentState = environment.getInitialState();
    }

    public boolean applyAction(Object action) {
        S next = environment.applyAction(currentState, (Action) action);

        // !!! КЛЮЧЕВОЕ ИСПРАВЛЕНИЕ: Обновляем состояние только если оно изменилось.
        // MazeEnvironment возвращает текущее состояние, если ход невалиден (в стену или прыжок неудачен).
        if (!next.equals(currentState)) {
            currentState = next;
            notifyListeners();
            return true;
        }
        return false;
    }

    public S getCurrentState() { return currentState; }
    public Environment<S, ?> getEnvironment() { return environment; }
    public boolean isAtGoal() { return environment.isGoal(currentState); }

    public void addListener(Listener l) { listeners.add(l); }
    private void notifyListeners() {
        for (var l : listeners) l.handle(new Event());
    }
}