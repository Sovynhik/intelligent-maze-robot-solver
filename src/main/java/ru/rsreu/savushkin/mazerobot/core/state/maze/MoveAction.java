package ru.rsreu.savushkin.mazerobot.core.state.maze;

import ru.rsreu.savushkin.mazerobot.core.state.Action;

public record MoveAction(int dx, int dy, boolean isDouble) implements Action {
    @Override
    public String getName() {
        String dir = (dx == 0 && dy == -1) ? "Вверх" :
                (dx == 0 && dy == 1)  ? "Вниз" :
                        (dx == -1 && dy == 0) ? "Влево" :
                                (dx == 1 && dy == 0)  ? "Вправо" : "???";
        return isDouble ? "Прыжок " + dir : dir;
    }
}