package ru.niceaska.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogInsertFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(getActivity());

        createProjectAlert.setTitle(getResources().getString(R.string.add_new_task));

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_layout, null);
        final EditText editText = v.findViewById(R.id.task_edit);
        createProjectAlert.setView(v)

                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        insertNewtask(editText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                    }
                });
        return createProjectAlert.create();
    }


    private void insertNewtask(String newTask) {
        if (getActivity() instanceof IAddToDb) {
            ((IAddToDb) getActivity()).addNewTask(newTask);
        }
    }
}
