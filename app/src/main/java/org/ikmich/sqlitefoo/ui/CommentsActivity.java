package org.ikmich.sqlitefoo.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.ikmich.sqlitefoo.R;
import org.ikmich.sqlitefoo.data.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends BaseActivity implements CommentsContract.View {

    @BindView(android.R.id.list)
    ListView list;

    @BindView(R.id.progress_fetch_remote)
    ProgressBar fetchProgress;

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

                AddEditCommentFragment f = new AddEditCommentFragment();
                FragmentManager fm = getSupportFragmentManager();
                f.openEditDialog(fm, comment, new AddEditCommentFragment.UpdateActionListener() {
                    @Override
                    public void onUpdate(String newComment) {
                        presenter.onClickUpdate(comment.getId(), newComment);
                    }
                });
            }
        });
    }

    @Override
    public void populateList(List<Comment> comments) {
        // use the SimpleCursorAdapter to openEditDialog the
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
    public void addComment(final Comment comment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("unchecked")
                ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
                adapter.add(comment);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void addComments(final List<Comment> comments) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("unchecked")
                ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
                for (Comment comment : comments) {
                    adapter.add(comment);
                }
                adapter.notifyDataSetChanged();
            }
        });

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
            case R.id.btn_add:
                AddEditCommentFragment f = new AddEditCommentFragment();
                FragmentManager fm = getSupportFragmentManager();
                f.openAddDialog(fm, new AddEditCommentFragment.AddActionListener() {
                    @Override
                    public void onAdd(String newComment) {
                        presenter.onClickAdd(newComment);
                    }
                });

                break;

            case R.id.btn_delete_first:
                // Check that there are items first.
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    presenter.onClickDeleteFirst(comment);
                }

                break;

            case R.id.btn_delete_all:
                if (getListAdapter().getCount() > 0) {
                    presenter.onClickDeleteAll();
                }
                break;

            case R.id.btn_fetch_remote:
                presenter.onClickFetchRemote();
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

    @Override
    public void toast(String message) {
        super.toast(message);
    }

    @Override
    public void alert(String message) {
        super.alert(message);
    }

    @Override
    public void showFetchProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fetchProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideFetchProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fetchProgress.setVisibility(View.GONE);
            }
        });
    }
}
