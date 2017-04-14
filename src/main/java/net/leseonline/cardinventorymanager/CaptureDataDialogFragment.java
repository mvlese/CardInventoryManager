package net.leseonline.cardinventorymanager;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.text.DecimalFormat;

/**
 * Created by mlese on 4/6/2017.
 */
public class CaptureDataDialogFragment extends DialogFragment implements EditNotesDialogFragment.IEditNotesDialogListener {
    private View mRootView;
    private Spinner mPositions;
    private Spinner mConditions;
    private Button mCancelButton;
    private Button mNotesButton;
    private Button mSaveButton;
    private String mNotes;
    private int mUniqueId;
    private Boolean mIsEdit;
    private static BaseballCard mCard = null;
    private DatabaseHelper mDatabaseHelper;
    private FragmentManager mFragmentManager;
    private EditNotesDialogFragment mEditNotesDialogFragment;
    private BaseballCard mEditableCard;
    public CaptureDataDialogFragment() {
        super();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mUniqueId = args.getInt("uniqueId", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.capture_data_dialog_layout, container, false);
        mDatabaseHelper = new DatabaseHelper(mRootView.getContext());

        mIsEdit = false;
        mCard = null;
        mNotes = "";

        if (mUniqueId < 0) {
            mIsEdit = true;
            mUniqueId *= -1;
        }

        mFragmentManager = getFragmentManager();
        mEditNotesDialogFragment = new EditNotesDialogFragment();
        mEditableCard = null;

        mConditions = (Spinner)mRootView.findViewById(R.id.condition_spinner);
        ArrayAdapter<Card.Condition> conditionsSpinnerArrayAdapter =
                new ArrayAdapter<Card.Condition>(mRootView.getContext(), android.R.layout.simple_spinner_item, Card.Condition.values());
        conditionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditions.setAdapter(conditionsSpinnerArrayAdapter);

        mPositions = (Spinner)mRootView.findViewById(R.id.position_spinner);
        ArrayAdapter<BaseballCard.Position> positionsSpinnerArrayAdapter =
                new ArrayAdapter<BaseballCard.Position>(mRootView.getContext(), android.R.layout.simple_spinner_item, BaseballCard.Position.values());
        positionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositions.setAdapter(positionsSpinnerArrayAdapter);

        mCancelButton = (Button)mRootView.findViewById(R.id.capture_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.playClick(v.getContext());
                dismiss();
            }
        });

        mNotesButton = (Button)mRootView.findViewById(R.id.capture_notes_button);
        mNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.playClick(v.getContext());
                // Create the notes dialog
                FragmentManager fm = getFragmentManager();
                EditNotesDialogFragment dialogFragment = new EditNotesDialogFragment();
                if (mEditableCard != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("notes", mEditableCard.getNotes());
                    dialogFragment.setArguments(bundle);
                }
                dialogFragment.setListener(CaptureDataDialogFragment.this);
                dialogFragment.show(fm, "CaptureNotes");
            }
        });

        mSaveButton = (Button)mRootView.findViewById(R.id.capture_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.playClick(v.getContext());
                mCard = saveCardData();
                if (mIsEdit) {
                    mDatabaseHelper.updateCard(mCard);
                } else {
                    // New card
                    mDatabaseHelper.addCard(mCard);
                }
                dismiss();
            }
        });

        if (mIsEdit) {
            // TODO mvl - handle notes
            mNotesButton.setEnabled(true);

            TextView tv = (TextView)mRootView.findViewById(R.id.capture_data_heading);
            tv.setText(getResources().getText(R.string.capture_image_header_edit));

            mEditableCard = mDatabaseHelper.find(mUniqueId);
            int condIdx = conditionsSpinnerArrayAdapter.getPosition(mEditableCard.getCondition());
            int posIdx = positionsSpinnerArrayAdapter.getPosition(mEditableCard.getPosition());

            EditText et = (EditText)mRootView.findViewById(R.id.first_name_edit);
            et.setText(mEditableCard.getFirstName());
            et = (EditText)mRootView.findViewById(R.id.last_name_edit);
            et.setText(mEditableCard.getLastName());
            et = (EditText)mRootView.findViewById(R.id.team_edit);
            et.setText(mEditableCard.getTeamName());
            et = (EditText)mRootView.findViewById(R.id.company_edit);
            et.setText(mEditableCard.getCompanyName());

            String year = "";
            et = (EditText)mRootView.findViewById(R.id.year_edit);
            if (mEditableCard.getYear() > 0) {
                year = String.valueOf(mEditableCard.getYear());
            }
            et.setText(year);

            String cardNum = "";
            et = (EditText)mRootView.findViewById(R.id.card_num_edit);
            if (mEditableCard.getCardNum() > 0) {
                cardNum = String.valueOf(mEditableCard.getCardNum());
            }
            et.setText(cardNum);

            et = (EditText)mRootView.findViewById(R.id.value_edit);
            DecimalFormat df = new DecimalFormat("#######0.00");
            String value = df.format(mEditableCard.getValue());
            et.setText(value);

            Spinner spinner = (Spinner)mRootView.findViewById(R.id.position_spinner);
            spinner.setSelection(posIdx);

            spinner = (Spinner)mRootView.findViewById(R.id.condition_spinner);
            spinner.setSelection(condIdx);
        }

        return mRootView;
    }

    @Override
    public void onEditNotesDialogPositiveAction(EditNotesDialogFragment dialog) {
        mNotes = dialog.getNotes();
        if (mEditableCard != null) {
            mEditableCard.setNotes(mNotes);
        }
    }

    @Override
    public void onEditNotesDialogNegativeAction(EditNotesDialogFragment dialog) {
        // do nothing
    }

    public static BaseballCard getCard() {
        return mCard;
    }

    private BaseballCard saveCardData() {
        BaseballCard result = new BaseballCard(mUniqueId);
        result.setCompanyName(((TextView)mRootView.findViewById(R.id.company_edit)).getText().toString().trim());
        result.setFirstName(((TextView)mRootView.findViewById(R.id.first_name_edit)).getText().toString().trim());
        result.setLastName(((TextView)mRootView.findViewById(R.id.last_name_edit)).getText().toString().trim());
        result.setTeamName(((TextView)mRootView.findViewById(R.id.team_edit)).getText().toString().trim());

        String valueString = ((TextView)mRootView.findViewById(R.id.value_edit)).getText().toString().trim();
        float value = 0.0f;
        try {
            value = Float.parseFloat(valueString);
        } catch(Exception ex) {
        }
        result.setValue(value);

        String yearString = ((TextView)mRootView.findViewById(R.id.year_edit)).getText().toString().trim();
        int year = 0;
        try {
            year = Integer.parseInt(yearString);
        } catch (Exception ex) {

        }
        result.setYear(year);

        String cardNumString = ((TextView)mRootView.findViewById(R.id.card_num_edit)).getText().toString().trim();
        int cardNum = Integer.MIN_VALUE;
        try {
            cardNum = Integer.parseInt(cardNumString);
        } catch (Exception ex) {

        }
        result.setCardNum(cardNum);

        result.setPosition(BaseballCard.Position.values()[mPositions.getSelectedItemPosition()]);
        result.setCondition(BaseballCard.Condition.values()[mConditions.getSelectedItemPosition()]);
        result.setNotes(mNotes);

        return result;
    }
}
