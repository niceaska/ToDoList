package ru.niceaska.todolist;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.CallbackCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.todolist.model.Task;

public class MainActivity extends AppCompatActivity implements IAddToDb {

    private static final String DIALOG_TASK = "dialogTask";
    private CompositeDisposable compositeDisposable;
    private ToDoListAdapter toDoListAdapter;
    private ToDoRepository toDoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        compositeDisposable = new CompositeDisposable();
        toDoRepository = new ToDoRepository();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogInsertFragment().show(getSupportFragmentManager(), DIALOG_TASK);
            }
        });

        final List<Task> titles = toDoRepository.getData(getApplicationContext());

        final RecyclerView recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        compositeDisposable.add(Single.fromCallable(new Callable<List<Task>>() {

            @Override
            public List<Task> call() throws Exception {
                return toDoRepository.getData(getApplicationContext());
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> strings) throws Exception {
                        toDoListAdapter = new ToDoListAdapter(titles, new IOnUpdateUIListener() {
                            @Override
                            public void onUpdateTask(final Task task) {
                                Completable.fromAction(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        toDoRepository.updateTask(getApplicationContext(), task);
                                    }
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe();
                            }
                            @Override
                            public void onDeleteTask(final Task task) {
                                Completable.fromAction(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        toDoRepository.deleteTask(getApplicationContext(), task);
                                    }
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new CompletableObserver() {
                                            @Override
                                            public void onSubscribe(Disposable d) {
                                                compositeDisposable.add(d);
                                            }

                                            @Override
                                            public void onComplete() {
                                                toDoListAdapter.deleteTask(task);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        });

                            }
                        });
                        recyclerView.setAdapter(toDoListAdapter);
                    }
                }));

    }


    @Override
    public void addNewTask(final String task) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                toDoRepository.addNewTask(getApplicationContext(), task);
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                toDoListAdapter.updateList(task);
            }
        }).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
