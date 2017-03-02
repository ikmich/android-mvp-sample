package org.ikmich.sqlitefoo.ui;

import org.ikmich.sqlitefoo.App;
import org.ikmich.sqlitefoo.data.Comment;
import org.ikmich.sqlitefoo.data.CommentsDataSource2;

import java.io.IOException;
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
//        String[] comments = new String[]{"Cool", "Very nice", "Hate it"};
//        int nextInt = new Random().nextInt(3);
//        Log.d(">>>", "Random int: " + nextInt);
//
        // save the new comment to the database
        interactionListener.onCommentAdded(datasource.createComment(comment));
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
                    Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/json").build();
                    Response response = client.newCall(request).execute();
                    interactionListener.onRemoteCommentsFetched(response.body().string());
                } catch (IOException ex) {
                }
            }
        }).start();


    }
}
