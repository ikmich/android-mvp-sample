package org.ikmich.sqlitefoo.ui;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.ikmich.sqlitefoo.App;
import org.ikmich.sqlitefoo.data.Comment;
import org.ikmich.sqlitefoo.data.CommentsDataSource2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class CommentsInteractor implements CommentsContract.Model {

    private InteractionListener interactionListener;
    private CommentsDataSource2 datasource;

    // TODO How to get activity context in MVP

    public CommentsInteractor(CommentsContract.Model.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;

        datasource = new CommentsDataSource2(App.getContext());
        datasource.open();
    }

    @Override
    public void addComment(String comment) {
        // save the new comment to the database
        Comment cmt = datasource.createComment(comment);

        /*
         Having 'interactionListener.onCommentAdded(comment)' here may not be exactly necessary, since the
         'datasource.createComment(comment)' returns immediately, and the result can simply be returned to the
         calling class. The interaction listener methods are best suited for
         data requests that are asynchronous and return at a later time.
         */
        interactionListener.onCommentAdded(cmt);
    }

    @Override
    public void addBulkComments(List<String> comments) {
        List<Comment> cmts = datasource.createComments(comments);
        interactionListener.onBulkCommentsAdded(cmts);
    }

    @Override
    public void updateComment(long id, String comment) {
        datasource.updateComment(id, comment);
        interactionListener.onCommentUpdated();
    }

    @Override
    public List<Comment> getAllComments() {
        return datasource.getAllComments();
    }

    @Override
    public void deleteComment(Comment comment) {
        // Check that there are items first.
        if (datasource.hasComments()) {
            datasource.deleteComment(comment);
            interactionListener.onCommentDeleted(comment);
        }
    }

    @Override
    public void deleteAllComments() {
        datasource.deleteAll();
        interactionListener.onAllCommentsDeleted();
    }

    @Override
    public void openDatasource() {
        datasource.open();
    }

    @Override
    public void closeDatasource() {
        datasource.close();
    }

    @Override
    public void fetchRemoteComments() {
        final String url = "https://jsonplaceholder.typicode.com/comments?postId=2";

        final OkHttpClient client = new OkHttpClient();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(url)
                            .addHeader("Content-Type", "application/json").build();
                    Response response = client.newCall(request).execute();
                    String responseJson = response.body().string();

                    Gson gson = new Gson();
                    JsonArray responseComments = gson.fromJson(responseJson, JsonArray.class);
                    List<String> comments = new ArrayList<>();

                    for (int i = 0; i < responseComments.size(); i++) {
                        JsonElement jsonElement = responseComments.get(i);
                        String body = jsonElement.getAsJsonObject().get("body").getAsString();
                        comments.add(body);
                    }

                    interactionListener.onRemoteCommentsFetched(comments);
                } catch (IOException ex) {
                    Log.e("CommentsInteractor", ">>> Request error", ex);
                }
            }
        }).start();
    }
}
