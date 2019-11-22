package ru.niceaska.todolist;

public class TaskDbSchema {
    public static final String NAME = "tasks";

    public static final class Cols {
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String IS_DONE = "done";
    }
}
