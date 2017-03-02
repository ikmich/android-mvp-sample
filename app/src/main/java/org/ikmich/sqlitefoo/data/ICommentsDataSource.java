package org.ikmich.sqlitefoo.data;

import java.util.List;

/**
 *
 */

public interface ICommentsDataSource {
    Comment createComment(String comment);
    List<Comment> createComments(List<String> comments);
    void deleteComment(Comment comment);
    void deleteAll();
    void updateComment(long id, String newComment);
    boolean hasComments();
    List<Comment> getAllComments();
    void open();
    void close();
}
