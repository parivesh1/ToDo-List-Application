package com.example.android.afinal;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private Cursor cursor;

    public TaskAdapter(Context context1, Cursor cursor1) {
        context = context1;
        cursor = cursor1;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view);
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        String name = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
        long id = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry._ID));
        holder.title.setText(name);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
