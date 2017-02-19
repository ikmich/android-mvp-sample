package org.ikmich.sqlitefoo.ui;

import org.ikmich.sqlitefoo.data.Comment;

import java.util.List;

/**
 *
 */
public class CommentsPresenterImpl implements CommentsContract.Presenter {

    private CommentsContract.View commentsView;
    private CommentsInteractor interactor;

    public CommentsPresenterImpl(CommentsContract.View commentsView) {
        this.commentsView = commentsView;
        interactor = new CommentsInteractor(this);

        List<Comment> comments = interactor.getAllComments();
        if (comments != null && !comments.isEmpty()) {
            commentsView.populateList(interactor.getAllComments());
        }
    }

    @Override
    public void handleUpdateAction(long id, String newComment) {
        interactor.updateComment(id, newComment);
        commentsView.populateList(interactor.getAllComments());
    }

    @Override
    public void handleAddAction() {
        interactor.addComment();
    }

    @Override
    public void handleDeleteAction(Comment comment) {
        interactor.deleteComment(comment);
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
    public void onResume() {
        interactor.openDatasource();
    }

    @Override
    public void onPause() {
        interactor.closeDatasource();
    }
}
