package org.ikmich.sqlitefoo.ui;

import org.ikmich.sqlitefoo.data.Comment;

import java.util.List;

/**
 * Defines MVP contract for Comments.
 */
public class CommentsContract {

    public interface View {
        void populateList(List<Comment> comments);

        void addComment(Comment comment);

        void addComments(List<Comment> comments);

        void removeComment(Comment comment);

        void toast(String message);

        void alert(String message);

        void showFetchProgress();

        void hideFetchProgress();
    }

    interface Presenter extends Model.InteractionListener {
        void onResume();

        void onPause();

        void onClickUpdate(long id, String comment);

        void onClickAdd(String comment);

        void onClickDeleteFirst(Comment comment);

        void onClickFetchRemote();
    }

    interface Model {
        void openDatasource();

        void closeDatasource();

        void addComment(String comment);

        void addBulkComments(List<String> comments);

        void updateComment(long id, String comment);

        void deleteComment(Comment comment);

        List<Comment> getAllComments();

        void fetchRemoteComments();

        interface InteractionListener {
            void onCommentDeleted(Comment comment);

            void onCommentAdded(Comment comment);

            void onBulkCommentsAdded(List<Comment> comments);

            void onCommentUpdated();

            void onRemoteCommentsFetched(List<String> commentList);
        }
    }
}
