package ru.niceaska.todolist;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.todolist.model.Task;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder> {

    private List<Task> todo;
    private IOnUpdateUIListener listener;


    public ToDoListAdapter(List<Task> todo, IOnUpdateUIListener listener) {
        this.todo = todo;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new ToDoViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.bindTask(todo.get(position));

    }

    @Override
    public int getItemCount() {
        return todo.size();
    }

    public void updateList(String task) {
        List<Task> newTasks = new ArrayList<>(todo);
        todo.add(new Task(newTasks.size(), task, false));
        notifyDataSetChanged();
    }

    public void deleteTask(Task task) {
        List<Task> taskList = new ArrayList<>(todo);
        taskList.remove(task);
        this.todo = taskList;
        notifyDataSetChanged();

    }

    static class ToDoViewHolder extends RecyclerView.ViewHolder {

        private TextView toDoTask;
        private CheckBox checkBox;
        private ImageButton close;
        private IOnUpdateUIListener listener;

        public ToDoViewHolder(@NonNull View itemView, IOnUpdateUIListener listener) {
            super(itemView);
            toDoTask = itemView.findViewById(R.id.task);
            checkBox = itemView.findViewById(R.id.checked_task);
            close = itemView.findViewById(R.id.close_button);
            this.listener = listener;
        }

        void bindTask(final Task task) {
            toDoTask.setText(task.getTaskTitle());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   task.setDone(isChecked);
                   listener.onUpdateTask(task);
                   markTaskDone(isChecked);
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteTask(task);
                }
            });
            markTaskDone(task.isDone());
            checkBox.setChecked(task.isDone());
        }

        private void markTaskDone(boolean isChecked) {
            toDoTask.setPaintFlags(isChecked ?
                    toDoTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG :
                    toDoTask.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
