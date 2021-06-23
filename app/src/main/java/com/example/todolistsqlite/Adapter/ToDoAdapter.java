package com.example.todolistsqlite.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistsqlite.AddNewTask;
import com.example.todolistsqlite.DatabaseHelper.DatabaseHelper;
import com.example.todolistsqlite.MainActivity;
import com.example.todolistsqlite.Model.TodoModel;
import com.example.todolistsqlite.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyviewHolder> {

    private List<TodoModel> modelList;
    private MainActivity activity;
    private DatabaseHelper myDB;

    public ToDoAdapter( MainActivity activity, DatabaseHelper myDB) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
      final TodoModel item = modelList.get(position);
      holder.checkBox.setText(item.getTask());
      holder.checkBox.setChecked(toBoolean(item.getStatus()));

      holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked){
                  myDB.updateStatus(item.getId(),1);
              }else {
                  myDB.updateStatus(item.getId(),0);
              }
          }
      });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }
    public void setTask(List<TodoModel> mlist){
        this.modelList=mlist;
        notifyDataSetChanged();
    }
    public void deleteTask(int position){
        TodoModel item = modelList.get(position);
        myDB.deleteTask(item.getId());
        modelList.remove(position);
        notifyItemRemoved(position);
    }

    public  void  editItem(int position){
        TodoModel item = modelList.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
         CheckBox checkBox;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}
