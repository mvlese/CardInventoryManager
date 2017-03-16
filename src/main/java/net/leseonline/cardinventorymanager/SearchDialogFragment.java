package net.leseonline.cardinventorymanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by mlese on 3/16/2017.
 */
public class SearchDialogFragment extends DialogFragment {
    private View mRootView;

    private ISearchDialogListener mListener = null;

    /**
     * This interface provides feedback to the caller.
     */
    public interface ISearchDialogListener {
        public void onSearchDialogPositiveAction(SearchDialogFragment dialog);

        public void onSearchDialogNegativeAction(SearchDialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            if (activity instanceof ISearchDialogListener) {
                mListener = (ISearchDialogListener) activity;
            }
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement IAddTeamDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.search_dialog_layout, container, false);

        Spinner mConditions = (Spinner)mRootView.findViewById(R.id.condition_spinner);
        ArrayAdapter<String> conditionsSpinnerArrayAdapter =
                new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_spinner_item, Card.Condition.getTextValues());
        conditionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditions.setAdapter(conditionsSpinnerArrayAdapter);

        Spinner mPositions = (Spinner)mRootView.findViewById(R.id.position_spinner);
        ArrayAdapter<String> positionsSpinnerArrayAdapter =
                new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_spinner_item, BaseballCard.Position.getTextValues());
        positionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositions.setAdapter(positionsSpinnerArrayAdapter);


        Button button = (Button) mRootView.findViewById(R.id.cancel_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyListener(false);
            }

        });

        button = (Button) mRootView.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyListener(true);
            }

        });

        button = (Button) mRootView.findViewById(R.id.clear_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText et = (EditText)mRootView.findViewById(R.id.first_name_edit);
                et.setText("");
                et = (EditText)mRootView.findViewById(R.id.last_name_edit);
                et.setText("");
                et = (EditText)mRootView.findViewById(R.id.team_edit);
                et.setText("");
                et = (EditText)mRootView.findViewById(R.id.company_edit);
                et.setText("");
                et = (EditText)mRootView.findViewById(R.id.year_edit);
                et.setText("");
                Spinner spinner = (Spinner)mRootView.findViewById(R.id.position_spinner);
                spinner.setSelection(0);
                spinner = (Spinner)mRootView.findViewById(R.id.condition_spinner);
                spinner.setSelection(0);
            }

        });

        return mRootView;
    }

    public SearchModel getSearchModel() {
        return new SearchModel();
    }

    private void notifyListener(boolean isOk) {
        if (mListener != null) {
            if (isOk) {
                mListener.onSearchDialogPositiveAction(this);
            } else {
                mListener.onSearchDialogNegativeAction(this);
            }
        }
        dismiss();
    }


}
