package com.example.alina.todolist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alina.todolist.R;

/**
 * Created by Alina on 22.11.2017.
 */

public class AddSubTaskDialogFragment extends DialogFragment implements TextView.OnEditorActionListener{

    public interface CreateSubTaskDialogListener {
        void onFinishSubTask(String inputText);
    }

    private EditText subTaskDescription;
    private SwitchCompat switchSubTaskStatus;

    public AddSubTaskDialogFragment() {

    }

    public static AddSubTaskDialogFragment newInstance(String description) {
        AddSubTaskDialogFragment fragment = new AddSubTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("description", description);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_subtask, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subTaskDescription = (EditText) view.findViewById(R.id.descriptionSubTaskText);
        switchSubTaskStatus = (SwitchCompat) view.findViewById(R.id.subTaskSwitcher);
        String title = getArguments().getString("description", "Enter description");
        getDialog().setTitle(title);
        subTaskDescription.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        subTaskDescription.setOnEditorActionListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            CreateSubTaskDialogListener listener = (CreateSubTaskDialogListener) getActivity();
            listener.onFinishSubTask(subTaskDescription.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }
}
