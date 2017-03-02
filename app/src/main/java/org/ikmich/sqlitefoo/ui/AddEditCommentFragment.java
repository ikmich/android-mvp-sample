package org.ikmich.sqlitefoo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ikmich.sqlitefoo.R;
import org.ikmich.sqlitefoo.data.Comment;

import butterknife.ButterKnife;

import static org.ikmich.sqlitefoo.ui.ActionType.EDIT;

/**
 *
 */
public class AddEditCommentFragment extends AppCompatDialogFragment {

    Comment comment;
    UpdateActionListener updateActionListener;
    AddActionListener addActionListener;

    // Default action is 'edit'
    private ActionType actionType = EDIT;

    public void openEditDialog(FragmentManager fm, Comment comment, @NonNull UpdateActionListener listener) {
        this.actionType = ActionType.EDIT;
        this.comment = comment;
        this.updateActionListener = listener;
        show(fm, AddEditCommentFragment.class.getName());
    }

    public void openAddDialog(FragmentManager fm, @NonNull AddActionListener listener) {
        this.actionType = ActionType.ADD;
        this.addActionListener = listener;
        show(fm, AddEditCommentFragment.class.getName());
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_edit_comment, null);

        // ButterKnife binding initialization should be done before setView(view)
        ButterKnife.bind(view);

        builder.setView(view);

        final Button actionButton = ButterKnife.findById(view, R.id.btn_action);
        final EditText commentInput = ButterKnife.findById(view, R.id.input_comment);
        commentInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    actionButton.performClick();
                    handled = true;
                }
                return handled;
            }
        });

        switch (this.actionType) {
            case ADD:
                builder.setTitle("Add Comment");
                commentInput.setText("");
                actionButton.setText(R.string.add);
                break;

            case EDIT:
                builder.setTitle("Edit Comment");
                commentInput.setText(comment.getComment());
                actionButton.setText(R.string.update);
                break;
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (actionType) {
                    case ADD:
                        String s = commentInput.getText().toString();
                        if (!TextUtils.isEmpty(s)) {
                            addActionListener.onAdd(commentInput.getText().toString());
                        }

                        break;
                    case EDIT:
                        updateActionListener.onUpdate(commentInput.getText().toString());
                        break;
                }

                dismiss();
            }
        });

        return builder.create();
    }

    void toast(Object o) {
        Toast.makeText(getActivity(), o.toString(), Toast.LENGTH_SHORT).show();
    }

    interface UpdateActionListener {
        void onUpdate(String newComment);
    }

    interface AddActionListener {
        void onAdd(String newComment);
    }
}
