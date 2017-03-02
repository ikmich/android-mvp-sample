package org.ikmich.sqlitefoo.ui;

import org.ikmich.sqlitefoo.data.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CommentsPresenterImpl implements CommentsContract.Presenter {

    private CommentsContract.View commentsView;
    private CommentsContract.Model commentsModel;

    CommentsPresenterImpl(CommentsContract.View commentsView) {
        this.commentsView = commentsView;

        commentsModel = new CommentsInteractor(this);

        commentsView.populateList(commentsModel.getAllComments());
    }

    @Override
    public void onClickUpdate(long id, String newComment) {
        commentsModel.updateComment(id, newComment);
    }

    @Override
    public void onClickAdd(String comment) {
        commentsModel.addComment(comment);
    }

    @Override
    public void onClickDeleteFirst(Comment comment) {
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
    public void onBulkCommentsAdded(List<Comment> comments) {
        commentsView.toast("Bulk comments added!");
        commentsView.addComments(comments);
    }

    @Override
    public void onCommentUpdated() {
        commentsView.populateList(commentsModel.getAllComments());
    }

    @Override
    public void onClickFetchRemote() {
        commentsView.showFetchProgress();
        commentsModel.fetchRemoteComments();
    }

    @Override
    public void onRemoteCommentsFetched(List<String> commentList) {
        commentsView.hideFetchProgress();
        commentsModel.addBulkComments(commentList);
    }

    @Override
    public void onClickDeleteAll() {
        commentsView.toast("All comments deleted.");
        commentsModel.deleteAllComments();
    }

    @Override
    public void onAllCommentsDeleted() {
        commentsView.populateList(new ArrayList<Comment>());
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
