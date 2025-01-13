package com.example.planmyweek.Comman;

import android.provider.BaseColumns;

public class ActivityContract {

    private ActivityContract() {
    }

    public static final class ActivityEntry implements BaseColumns {
        public static final String TABLE_NAME = "activities";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_DUE_TIME = "due_time";
        public static final String COLUMN_COMPLETED = "completed";
    }
}
