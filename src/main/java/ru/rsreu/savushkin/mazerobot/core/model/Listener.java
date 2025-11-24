package ru.rsreu.savushkin.mazerobot.core.model;
import ru.rsreu.savushkin.mazerobot.core.entity.Event;

public interface Listener {
    void handle(Event event);
}