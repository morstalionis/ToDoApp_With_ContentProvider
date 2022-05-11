package com.example.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Fragment_Completed extends Fragment implements Class_Interface {



    Class_Completed_Adapter adapter_completed;
    Class_Adapter adapter_pending;
    Class_Database class_database;
    RecyclerView recyclerView;
    ArrayList<String> task_id, task_title, task_details, task_date, task_done;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        recyclerView = view.findViewById(R.id.recycler_completed);

        onResume();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        class_database = new Class_Database(getContext());
        task_id = new ArrayList<>();
        task_title = new ArrayList<>();
        task_details = new ArrayList<>();
        task_date = new ArrayList<>();
        task_done = new ArrayList<>();

        adapter_completed = new Class_Completed_Adapter(getContext(), task_id, task_title, task_date, task_done, task_details, this);

        storedata();

        recyclerView.setAdapter(adapter_completed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //SWIPER
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    void storedata() {
        Cursor cursor = class_database.c_readAllData();
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                task_id.add(cursor.getString(0));
                task_title.add(cursor.getString(1));
                task_date.add(cursor.getString(2));
                task_done.add(cursor.getString(3));
                task_details.add(cursor.getString(4));
            }
        }

    }

    @Override
    public void onItemClick(int position) {

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Task?");
                    builder.setMessage("Are you sure you want to delete this Task?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            class_database.c_deleteItem(task_id.get(position));
                            adapter_completed.notifyItemRemoved(position);
                            adapter_completed.notifyDataSetChanged();
                            onResume();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter_completed.notifyItemChanged(position);
                        }
                    });
                    builder.create().show();
                    break;

                case ItemTouchHelper.LEFT:

                    AlertDialog.Builder newbuilder = new AlertDialog.Builder(getContext());
                    newbuilder.setTitle("Undo Complete Task");
                    newbuilder.setMessage("Set Task as Incomplete?");
                    newbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uncomplete(position);
                            class_database.c_deleteItem(task_id.get(position));
                            adapter_completed.notifyItemRemoved(position);

                            //Shitty Workaround I used for Debugging
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //Shitty Workaround I used for Debugging

                            onResume();
                        }
                    });
                    newbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter_completed.notifyItemChanged(position);
                        }
                    });
                    newbuilder.create().show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.green))
                    .addSwipeLeftActionIcon(R.drawable.ic_undo)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

    };

    void uncomplete(int position){
        String txt_taskTitle = (task_title.get(position));
        String txt_taskDetail = (task_details.get(position));
        String txt_taskCreationDate = (task_date.get(position));

        Date src_date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String modified = df.format(src_date);

        Class_Database add_db = new Class_Database(getContext());
        add_db.p_add_task(txt_taskTitle.toString().trim(), txt_taskCreationDate.toString().trim(), modified.toString().trim(), txt_taskDetail.toString().trim());
    }
}