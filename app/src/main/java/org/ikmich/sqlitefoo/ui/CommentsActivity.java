package org.ikmich.sqlitefoo.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.ikmich.sqlitefoo.R;
import org.ikmich.sqlitefoo.data.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends BaseActivity implements CommentsContract.View {

    @BindView(android.R.id.list)
    ListView list;

    private CommentsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife init
        ButterKnife.bind(this);

        presenter = new CommentsPresenterImpl(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final Comment comment = (Comment) adapterView.getAdapter().getItem(i);

                EditCommentFragment f = new EditCommentFragment();
                FragmentManager fm = getSupportFragmentManager();
                f.show(fm, comment, new EditCommentFragment.InteractionListener() {
                    @Override
                    public void onUpdate(String newComment) {
                        presenter.handleUpdateAction(comment.getId(), newComment);
                    }
                });
            }
        });
    }

    @Override
    public void populateList(List<Comment> comments) {
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, comments);
        setListAdapter(adapter);
    }

    void setListAdapter(ArrayAdapter<Comment> adapter) {
        list.setAdapter(adapter);
    }

    ListAdapter getListAdapter() {
        return list.getAdapter();
    }

    @Override
    public void addComment(Comment comment) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        adapter.add(comment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeComment(Comment comment) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        adapter.remove(comment);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        Comment comment;

        switch (view.getId()) {
            case R.id.add:
                presenter.handleAddAction();
                break;

            case R.id.delete:
                // Check that there are items first.
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    presenter.handleDeleteAction(comment);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }
}
