package com.example.android.afinal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private TaskAdapter adapter;
    private EditText editAdd;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskDBHelper dbHelper = new TaskDBHelper(this);
        db = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);
        Toast.makeText(this, "Welcome!!", Toast.LENGTH_SHORT).show();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
        editAdd = findViewById(R.id.edit_text_add);
        Button add = findViewById(R.id.add_btn);
        add.setOnClickListener(v -> addItem());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                Toast.makeText(this, "All Tasks Deleted!!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAll() {
        db.delete(TaskContract.TaskEntry.TABLE_NAME, null, null);
        adapter.swapCursor(getAllItems());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void addItem() {
        if (editAdd.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Enter the Title first", Toast.LENGTH_SHORT).show();
            return;
        }
        String title = editAdd.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_NAME, title);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, cv);
        Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
        adapter.swapCursor(getAllItems());
        editAdd.getText().clear();
    }

    private void removeItem(long id) {
        db.delete(TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry._ID + "=" + id, null);

        Toast.makeText(this, "Task Deleted!", Toast.LENGTH_SHORT).show();
        adapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_TIMESTAMP + " ASC"
        );
    }
}