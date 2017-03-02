package org.ikmich.sqlitefoo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.ikmich.sqlitefoo.provider.CommentsProvider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CommentsDataSource2 extends AbsCommentsDataSource {

    private final String TAG = ">>> " + getClass().getName();

    public CommentsDataSource2(Context context) {
        super(context);
    }

    @Override
    public Comment createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, comment);

        Uri insertedUri = context.getContentResolver().insert(CommentsProvider.CONTENT_URI, values);
        if (insertedUri != null) {
            Log.d(TAG, "Uri after insert Comment: " + insertedUri.toString());

            Cursor cursor = context.getContentResolver().query(
                    insertedUri,
                    null,
                    null,
                    null,
                    null);

            if (cursor != null) {
                cursor.moveToFirst();

                Comment newComment = Comment.fromCursor(cursor);
                cursor.close();

                return newComment;
            }
        }

        return null;
    }

    @Override
    public List<Comment> createComments(List<String> comments) {
        List<Comment> cmts = new ArrayList<>();
        for (String comment : comments) {
            cmts.add(createComment(comment));
        }
        return cmts;
    }

    @Override
    public void deleteComment(Comment comment) {
        int numRows = context.getContentResolver().delete(
                CommentsProvider.CONTENT_URI,
                COLUMN_ID + " = " + comment.getId(),
                null);
        Log.d(TAG, numRows + " deleted.");
    }

    @Override
    public void updateComment(long id, String newComment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, newComment);
        int numRows = context.getContentResolver().update(
                CommentsProvider.CONTENT_URI,
                values,
                COLUMN_ID + " = " + id,
                null);
        Log.d(TAG, numRows + " updated.");
    }

    @Override
    public boolean hasComments() {
        List<Comment> allComments = getAllComments();
        return !allComments.isEmpty();
    }

    @Override
    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                CommentsProvider.CONTENT_URI,
                null,
                null,
                null,
                COLUMN_COMMENT);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Comment comment = Comment.fromCursor(cursor);
                comments.add(comment);
                cursor.moveToNext();
            }
        }

        return comments;
    }
}
