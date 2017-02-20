package org.ikmich.sqlitefoo.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 */

public abstract class AbsCommentsDataSource implements CommentsTable, ICommentsDataSource {
    Context context;
    SQLiteDatabase database;
    DbHelper dbHelper;
    String[] allColumns = {COLUMN_ID,
            COLUMN_COMMENT};

    AbsCommentsDataSource(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
