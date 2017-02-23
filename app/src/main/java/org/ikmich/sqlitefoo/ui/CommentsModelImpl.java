package org.ikmich.sqlitefoo.ui;

import android.util.Log;

import org.ikmich.sqlitefoo.App;
import org.ikmich.sqlitefoo.data.Comment;
import org.ikmich.sqlitefoo.data.CommentsDataSource2;

import java.util.List;
import java.util.Random;

/**
 *
 */
public class CommentsModelImpl implements CommentsContract.Model {

    private InteractionListener interactionListener;
    private CommentsDataSource2 datasource;

    // TODO How to get activity context in MVP

    public CommentsModelImpl(CommentsContract.Model.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;

        datasource = new CommentsDataSource2(App.getContext());
        datasource.open();
    }

    @Override
    public void addComment() {
        String[] comments = new String[]{"Cool", "Very nice", "Hate it"};
        int nextInt = new Random().nextInt(3);
        Log.d(">>>", "Random int: " + nextInt);

        // save the new comment to the database
        Comment comment = datasource.createComment(comments[nextInt]);
        interactionListener.onCommentAdded(comment);
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
}
