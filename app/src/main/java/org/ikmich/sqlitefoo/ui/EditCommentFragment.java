package org.ikmich.sqlitefoo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ikmich.sqlitefoo.R;
import org.ikmich.sqlitefoo.data.Comment;

import butterknife.ButterKnife;

/**
 *
 */
public class EditCommentFragment extends AppCompatDialogFragment {

    Comment comment;
    InteractionListener interactionListener;

    public void show(FragmentManager fm, Comment comment, @NonNull InteractionListener listener) {
        this.comment = comment;
        this.interactionListener = listener;
        show(fm, EditCommentFragment.class.getName());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Comment");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_comment, null);

        // ButterKnife binding initialization should be done before setView(view)
        ButterKnife.bind(view);

        builder.setView(view);

        final EditText commentInput = ButterKnife.findById(view, R.id.input_comment);
        commentInput.setText(comment.getComment());

        Button updateButton = ButterKnife.findById(view, R.id.btn_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("Clicked");
                dismiss();
                interactionListener.onUpdate(commentInput.getText().toString());
            }
        });

        return builder.create();
    }

    void toast(Object o) {
        Toast.makeText(getActivity(), o.toString(), Toast.LENGTH_SHORT).show();
    }

    interface InteractionListener {
        void onUpdate(String newComment);
    }
}
