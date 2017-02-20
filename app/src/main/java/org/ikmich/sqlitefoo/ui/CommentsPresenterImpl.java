package org.ikmich.sqlitefoo.ui;

import org.ikmich.sqlitefoo.data.Comment;

/**
 *
 */
public class CommentsPresenterImpl implements CommentsContract.Presenter {

    private CommentsContract.View commentsView;
    private CommentsContract.Model commentsModel;

    public CommentsPresenterImpl(CommentsContract.View commentsView) {
        this.commentsView = commentsView;

        commentsModel = new CommentsModelImpl(this);

        commentsView.populateList(commentsModel.getAllComments());
    }

    @Override
    public void handleUpdateAction(long id, String newComment) {
        commentsModel.updateComment(id, newComment);
    }

    @Override
    public void handleAddAction() {
        commentsModel.addComment();
    }

    @Override
    public void handleDeleteAction(Comment comment) {
        commentsModel.deleteComment(comment);
    }

    @Override
    public void onCommentDeleted(Comment comment) {
        commentsView.removeComment(comment);
    }

    @Override
    public void onCommentAdded(Comment comment) {
        commentsView.addComment(comment);
    }

    @Override
    public void onCommentUpdated() {
        commentsView.populateList(commentsModel.getAllComments());
    }

    @Override
    public void onResume() {
        commentsModel.openDatasource();
    }

    @Override
    public void onPause() {
        commentsModel.closeDatasource();
    }
}
