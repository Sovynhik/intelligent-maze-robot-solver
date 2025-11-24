package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.state.State;

public record MazeState(int x, int y) implements State {
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}