package org.ikmich.sqlitefoo.data;

import java.util.List;

/**
 *
 */

public interface ICommentsDataSource {
    Comment createComment(String comment);
    void deleteComment(Comment comment);
    void updateComment(long id, String newComment);
    boolean hasComments();
    List<Comment> getAllComments();
    void open();
    void close();
}
