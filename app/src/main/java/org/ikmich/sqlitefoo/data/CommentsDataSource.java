package org.ikmich.sqlitefoo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) for the Comments.
 */
public class CommentsDataSource implements CommentsTable {

    private SQLiteDatabase database;
    private AppSqliteHelper dbHelper;
    private String[] allColumns = {COLUMN_ID,
            COLUMN_COMMENT};

    public CommentsDataSource(Context context) {
        dbHelper = new AppSqliteHelper(context);
        // TODO - Should 'open()' be called here??
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Saves a Comment in the db.
     *
     * @param comment The Comment to be saved.
     * @return The saved Comment.
     */
    public Comment createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, comment);

        long insertId = database.insert(TABLE_COMMENTS, null,
                values);

        Cursor cursor = database.query(
                TABLE_COMMENTS,
                allColumns,
                COLUMN_ID + " = " + insertId,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        Comment newComment = Comment.fromCursor(cursor);
        cursor.close();

        return newComment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(TABLE_COMMENTS, COLUMN_ID
                + " = " + id, null);
    }

    /**
     * Update a Comment.
     *
     * @param id         The id of the comment to update.
     * @param newComment The new comment.
     */
    public void updateComment(long id, String newComment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, newComment);
        database.updateWithOnConflict(
                TABLE_COMMENTS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();

        Cursor cursor = database.query(TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = Comment.fromCursor(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    /*private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }*/
}
