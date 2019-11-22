package ru.niceaska.todolist;

import ru.niceaska.todolist.model.Task;

public interface IOnUpdateUIListener {
    void onUpdateTask(Task task);
    void onDeleteTask(Task task);
}
