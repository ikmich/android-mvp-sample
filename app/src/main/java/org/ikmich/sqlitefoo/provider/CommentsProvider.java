package org.ikmich.sqlitefoo.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.ikmich.sqlitefoo.data.CommentsTable;
import org.ikmich.sqlitefoo.data.DbHelper;

import java.util.HashMap;

public class CommentsProvider extends ContentProvider implements CommentsTable {

    DbHelper dbHelper;
    SQLiteDatabase db;

    public static final String PROVIDER_NAME = "org.ikmich.sqlitefoo.provider.CommentsProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/comments";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> COMMENTS_PROJECTION_MAP;

    public static final int CODE_COMMENTS = 1;
    public static final int CODE_COMMENT_ID = 2;

    static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PROVIDER_NAME, "comments", CODE_COMMENTS);
        uriMatcher.addURI(PROVIDER_NAME, "comments/#", CODE_COMMENT_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_COMMENTS);

        switch (uriMatcher.match(uri)) {
            case CODE_COMMENTS:
                qb.setProjectionMap(COMMENTS_PROJECTION_MAP);
                break;

            case CODE_COMMENT_ID:
                qb.appendWhere(COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder.equals("")) {
            /**
             * By default sort on comments
             */
            sortOrder = COLUMN_COMMENT;
        }

        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {

            case CODE_COMMENTS:
                return "vnd.android.cursor.dir/org.ikmich.sqlitefoo.comments";

            case CODE_COMMENT_ID:
                return "vnd.android.cursor.item/org.ikmich.sqlitefoo.comments";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        /**
         * Add a new comment record
         */
        long rowID = db.insert(TABLE_COMMENTS, "", contentValues);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case CODE_COMMENTS:
                count = db.delete(TABLE_COMMENTS, selection, selectionArgs);
                break;

            case CODE_COMMENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(
                        TABLE_COMMENTS,
                        COLUMN_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case CODE_COMMENTS:
                count = db.update(TABLE_COMMENTS, contentValues, selection, selectionArgs);
                break;

            case CODE_COMMENT_ID:
                count = db.update(TABLE_COMMENTS, contentValues,
                        COLUMN_ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
