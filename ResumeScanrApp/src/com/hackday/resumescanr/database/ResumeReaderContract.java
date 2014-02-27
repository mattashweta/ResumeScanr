package com.hackday.resumescanr.database;

import android.provider.BaseColumns;

public final class ResumeReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ResumeReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ResumeEntry implements BaseColumns {
        public static final String TABLE_NAME = "resumeEntry";
        public static final String COLUMN_NAME_ENTRY_ID = "resumeid";
        public static final String COLUMN_NAME_STUDNAME = "studName";
        public static final String COLUMN_NAME_STUDGPA = "studGPA";
        public static final String COLUMN_NAME_STUDEMAIL= "studEmail";
        public static final String COLUMN_NAME_UNIV = "studUniv";
        public static final String COLUMN_NAME_BRANCH = "studBranch";
    }
}
