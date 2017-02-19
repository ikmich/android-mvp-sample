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

        void removeComment(Comment comment);
    }

    interface Presenter extends Model.InteractionListener {
        void onResume();

        void onPause();

        void handleUpdateAction(long id, String comment);

        void handleAddAction();

        void handleDeleteAction(Comment comment);
    }

    interface Model {
        void openDatasource();

        void closeDatasource();

        void addComment();

        void updateComment(long id, String comment);

        void deleteComment(Comment comment);

        List<Comment> getAllComments();

        interface InteractionListener {
            void onCommentDeleted(Comment comment);

            void onCommentAdded(Comment comment);
        }
    }
}
