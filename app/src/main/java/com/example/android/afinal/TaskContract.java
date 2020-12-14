package com.example.android.afinal;

import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract() {
    }

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "TaskList";
        public static final String COLUMN_NAME = "Task";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}