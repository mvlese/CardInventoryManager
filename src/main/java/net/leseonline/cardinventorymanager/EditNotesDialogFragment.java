package net.leseonline.cardinventorymanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mlese on 3/6/2017.
 */
public class EditNotesDialogFragment extends DialogFragment {
    private View mRootView;
    private EditText mEditText;
    private Button mCancelButton;
    private Button mOkButton;

    private IEditNotesDialogListener mListener = null;

    /**
     * This interface provides feedback to the caller.
     */
    public interface IEditNotesDialogListener {
        public void onEditNotesDialogPositiveAction(EditNotesDialogFragment dialog);

        public void onEditNotesDialogNegativeAction(EditNotesDialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            if (activity instanceof IEditNotesDialogListener) {
                mListener = (IEditNotesDialogListener) activity;
            }
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement IAddTeamDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.add_notes_layout, container, false);
//        getDialog().setTitle("Simple Dialog");

        EditText et = (EditText) mRootView.findViewById(R.id.notes_edit);
        et.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                  }

                  @Override
                  public void afterTextChanged(Editable s) {
                      String notes = s.toString().trim();
                      Button button = (Button) mRootView.findViewById(R.id.ok_button);
                      button.setEnabled(s.length() > 0);
                  }
              }
        );


        Button button = (Button) mRootView.findViewById(R.id.cancel_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyListener(false);
            }

        });

        button = (Button) mRootView.findViewById(R.id.ok_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyListener(true);
            }

        });

        return mRootView;
    }

    public String getNotes() {
        EditText et = (EditText) mRootView.findViewById(R.id.notes_edit);
        String notes = et.getText().toString().trim();
        return notes;
    }

    private void notifyListener(boolean isOk) {
        if (mListener != null) {
            if (isOk) {
                mListener.onEditNotesDialogPositiveAction(this);
            } else {
                mListener.onEditNotesDialogNegativeAction(this);
            }
        }
        dismiss();
    }

}
