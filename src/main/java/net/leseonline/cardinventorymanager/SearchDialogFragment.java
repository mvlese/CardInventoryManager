package net.leseonline.cardinventorymanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

/**
 * Created by mlese on 3/16/2017.
 */
public class SearchDialogFragment extends DialogFragment {
    private View mRootView;
    private DatabaseHelper mDatabaseHelper;
    ArrayAdapter<Card.Condition> mConditionsSpinnerArrayAdapter;
    ArrayAdapter<BaseballCard.Position> mPositionsSpinnerArrayAdapter;
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
        mRootView = inflater.inflate(R.layout.search_dialog_layout_with_labels, container, false);
        mDatabaseHelper = new DatabaseHelper(mRootView.getContext());
        SearchModel model = mDatabaseHelper.getSearchModel();

        Spinner mConditions = (Spinner)mRootView.findViewById(R.id.condition_spinner);
        mConditionsSpinnerArrayAdapter =
                new ArrayAdapter<Card.Condition>(mRootView.getContext(), android.R.layout.simple_spinner_item, Card.Condition.values());
        mConditionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditions.setAdapter(mConditionsSpinnerArrayAdapter);

        Spinner mPositions = (Spinner)mRootView.findViewById(R.id.position_spinner);
        mPositionsSpinnerArrayAdapter =
                new ArrayAdapter<BaseballCard.Position>(mRootView.getContext(), android.R.layout.simple_spinner_item, BaseballCard.Position.values());
        mPositionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositions.setAdapter(mPositionsSpinnerArrayAdapter);

        int condIdx = mConditionsSpinnerArrayAdapter.getPosition(model.getCondition());
        int posIdx = mPositionsSpinnerArrayAdapter.getPosition(model.getPosition());

        EditText et = (EditText)mRootView.findViewById(R.id.first_name_edit);
        et.addTextChangedListener(filterTextWatcher);
        et.setText(model.getFirstName());

        et = (EditText)mRootView.findViewById(R.id.last_name_edit);
        et.addTextChangedListener(filterTextWatcher);
        et.setText(model.getLastName());

        et = (EditText)mRootView.findViewById(R.id.team_edit);
        et.addTextChangedListener(filterTextWatcher);
        et.setText(model.getTeamName());

        et = (EditText)mRootView.findViewById(R.id.company_edit);
        et.addTextChangedListener(filterTextWatcher);
        et.setText(model.getCompany());

        String cardNum = "";
        et = (EditText)mRootView.findViewById(R.id.card_num_edit);
        et.addTextChangedListener(filterTextWatcher);
        if (model.getCardNum() > 0) {
            cardNum = String.valueOf(model.getCardNum());
        }
        et.setText(cardNum);

        String year = "";
        et = (EditText)mRootView.findViewById(R.id.year_edit);
        et.addTextChangedListener(filterTextWatcher);
        if (model.getYear() != Integer.MIN_VALUE) {
            year = String.valueOf(model.getYear());
        }
        et.setText(year);

        Spinner spinner = (Spinner)mRootView.findViewById(R.id.position_spinner);
        spinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);
        spinner.setSelection(posIdx);

        spinner = (Spinner)mRootView.findViewById(R.id.condition_spinner);
        spinner.setOnItemSelectedListener(spinnerOnItemSelectedListener);
        spinner.setSelection(condIdx);

        Button button = (Button) mRootView.findViewById(R.id.cancel_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utilities.playClick(mRootView.getContext());
                notifyListener(false);
            }

        });

        button = (Button) mRootView.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utilities.playClick(mRootView.getContext());
                notifyListener(true);
            }

        });

        button = (Button) mRootView.findViewById(R.id.clear_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utilities.playClick(mRootView.getContext());
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
                et = (EditText)mRootView.findViewById(R.id.card_num_edit);
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
        SearchModel model = new SearchModel();

        EditText et = (EditText)mRootView.findViewById(R.id.first_name_edit);
        model.setFirstName(et.getText().toString().trim());

        et = (EditText)mRootView.findViewById(R.id.last_name_edit);
        model.setLastName(et.getText().toString().trim());

        et = (EditText)mRootView.findViewById(R.id.team_edit);
        model.setTeamName(et.getText().toString().trim());

        et = (EditText)mRootView.findViewById(R.id.company_edit);
        model.setCompany(et.getText().toString().trim());

        et = (EditText)mRootView.findViewById(R.id.year_edit);
        String temp = et.getText().toString().trim();
        int year = (temp.length() == 0) ? Integer.MIN_VALUE : Integer.parseInt(temp);
        model.setYear(year);

        et = (EditText)mRootView.findViewById(R.id.card_num_edit);
        temp = et.getText().toString().trim();
        int cardNum = (temp.length() == 0) ? Integer.MIN_VALUE : Integer.parseInt(temp);
        model.setCardNum(cardNum);

        Spinner spinner = (Spinner)mRootView.findViewById(R.id.position_spinner);
        BaseballCard.Position position =
                mPositionsSpinnerArrayAdapter.getItem(spinner.getSelectedItemPosition());
        model.setPosition(position);

        spinner = (Spinner)mRootView.findViewById(R.id.condition_spinner);
        Card.Condition condition =
                mConditionsSpinnerArrayAdapter.getItem(spinner.getSelectedItemPosition());
        model.setCondition(condition);

        return model;
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

    private void checkIsSearchable() {
        boolean isSearching = false;

        EditText et = (EditText)mRootView.findViewById(R.id.first_name_edit);
        isSearching |= et.getText().length() > 0;

        et = (EditText)mRootView.findViewById(R.id.last_name_edit);
        isSearching |= et.getText().length() > 0;

        et = (EditText)mRootView.findViewById(R.id.team_edit);
        isSearching |= et.getText().length() > 0;

        et = (EditText)mRootView.findViewById(R.id.company_edit);
        isSearching |= et.getText().length() > 0;

        et = (EditText)mRootView.findViewById(R.id.card_num_edit);
        isSearching |= et.getText().length() > 0;

        et = (EditText)mRootView.findViewById(R.id.year_edit);
        isSearching |= et.getText().length() > 0;

        Spinner spinner = (Spinner)mRootView.findViewById(R.id.position_spinner);
        isSearching |= spinner.getSelectedItemPosition() > 0;

        spinner = (Spinner)mRootView.findViewById(R.id.condition_spinner);
        isSearching |= spinner.getSelectedItemPosition() > 0;

        Button btn = (Button)mRootView.findViewById(R.id.search_button);
        btn.setText(isSearching ? "Search" : "Close");
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkIsSearchable();
        }
    };

    private Spinner.OnItemSelectedListener spinnerOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            checkIsSearchable();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            checkIsSearchable();
        }
    };
}
