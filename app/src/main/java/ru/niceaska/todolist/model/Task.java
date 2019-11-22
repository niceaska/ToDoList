package ru.niceaska.todolist.model;

public class Task {
    private int id;
    private String taskTitle;
    private boolean isDone;

    public Task(int id, String taskTitle, boolean isDone) {
        this.id = id;
        this.taskTitle = taskTitle;
        this.isDone = isDone;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getId() {
        return id;
    }
}
