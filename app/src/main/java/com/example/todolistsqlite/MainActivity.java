package com.example.todolistsqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.todolistsqlite.Adapter.ToDoAdapter;
import com.example.todolistsqlite.DatabaseHelper.DatabaseHelper;
import com.example.todolistsqlite.Model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDiaCloseListner {
private RecyclerView mRecyclerview;
private FloatingActionButton fab;
private DatabaseHelper myDB;

private List<TodoModel> mlist;
private ToDoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerview=findViewById(R.id.recycler);
        fab=findViewById(R.id.fab);
        myDB=new DatabaseHelper(this);
        mlist=new ArrayList<>();
        adapter=new ToDoAdapter(MainActivity.this,myDB);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        mlist=myDB.getAllTAsks();
        Collections.reverse(mlist);
        adapter.setTask(mlist);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);
    }

    @Override
    public void dialogClose(DialogInterface dialogInterface) {
        mlist=myDB.getAllTAsks();
        Collections.reverse(mlist);
        adapter.setTask(mlist);
        adapter.notifyDataSetChanged();
    }
}