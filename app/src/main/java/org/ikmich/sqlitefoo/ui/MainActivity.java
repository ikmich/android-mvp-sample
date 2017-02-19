package org.ikmich.sqlitefoo.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ikmich.sqlitefoo.R;
import org.ikmich.sqlitefoo.data.Comment;
import org.ikmich.sqlitefoo.data.CommentsDataSource;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private CommentsDataSource datasource;

    @BindView(android.R.id.list)
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife init
        ButterKnife.bind(this);

        datasource = new CommentsDataSource(this);
        datasource.open();

        populateList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final Comment comment = (Comment) adapterView.getAdapter().getItem(i);

                EditCommentFragment f = new EditCommentFragment();
                FragmentManager fm = getSupportFragmentManager();
                f.show(fm, comment, new EditCommentFragment.InteractionListener() {
                    @Override
                    public void onUpdate(String newComment) {
                        datasource.updateComment(comment.getId(), newComment);
                        populateList();
                    }
                });
            }
        });
    }

    void populateList() {
        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    void setListAdapter(ArrayAdapter<Comment> adapter) {
        list.setAdapter(adapter);
    }

    ListAdapter getListAdapter() {
        return list.getAdapter();
    }

    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();

        Comment comment;

        switch (view.getId()) {
            case R.id.add:
                String[] comments = new String[]{"Cool", "Very nice", "Hate it"};
                int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                comment = datasource.createComment(comments[nextInt]);
                adapter.add(comment);
                break;

            case R.id.delete:
                // Check that there are items first.
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    datasource.deleteComment(comment);
                    adapter.remove(comment);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    void toast(Object o) {
        Toast.makeText(this, o.toString(), Toast.LENGTH_SHORT).show();
    }
}
