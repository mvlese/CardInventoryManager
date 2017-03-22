package net.leseonline.cardinventorymanager;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.concurrent.locks.Condition;

public class CaptureDataActivity extends AppCompatActivity implements EditNotesDialogFragment.IEditNotesDialogListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsEdit = false;
        mCard = null;
        mNotes = "";
        mDatabaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_capture_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUniqueId = getIntent().getIntExtra(getResources().getString(R.string.extra_unique_id), -1);
        if (mUniqueId < 0) {
            mIsEdit = true;
            mUniqueId *= -1;
        }

        mConditions = (Spinner)findViewById(R.id.condition_spinner);
        ArrayAdapter<Card.Condition> conditionsSpinnerArrayAdapter =
            new ArrayAdapter<Card.Condition>(this, android.R.layout.simple_spinner_item, Card.Condition.values());
        conditionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditions.setAdapter(conditionsSpinnerArrayAdapter);

        mPositions = (Spinner)findViewById(R.id.position_spinner);
        ArrayAdapter<BaseballCard.Position> positionsSpinnerArrayAdapter =
                new ArrayAdapter<BaseballCard.Position>(this, android.R.layout.simple_spinner_item, BaseballCard.Position.values());
        positionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositions.setAdapter(positionsSpinnerArrayAdapter);

        mCancelButton = (Button)findViewById(R.id.capture_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.playClick(v.getContext());
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mNotesButton = (Button)findViewById(R.id.capture_notes_button);
        mNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.playClick(v.getContext());
                // Create the notes dialog
                FragmentManager fm = getFragmentManager();
                EditNotesDialogFragment dialogFragment = new EditNotesDialogFragment();
                dialogFragment.show(fm, "CaptureNotes");
            }
        });

        mSaveButton = (Button)findViewById(R.id.capture_save_button);
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
                setResult(RESULT_OK);
                finish();
            }
        });

        if (mIsEdit) {
            // TODO mvl - handle notes
            mNotesButton.setEnabled(false);

            TextView tv = (TextView)findViewById(R.id.capture_data_heading);
            tv.setText(getResources().getText(R.string.capture_image_header_edit));

            BaseballCard card = mDatabaseHelper.find(mUniqueId);
            int condIdx = conditionsSpinnerArrayAdapter.getPosition(card.getCondition());
            int posIdx = positionsSpinnerArrayAdapter.getPosition(card.getPosition());

            EditText et = (EditText)findViewById(R.id.first_name_edit);
            et.setText(card.getFirstName());
            et = (EditText)findViewById(R.id.last_name_edit);
            et.setText(card.getLastName());
            et = (EditText)findViewById(R.id.team_edit);
            et.setText(card.getTeamName());
            et = (EditText)findViewById(R.id.company_edit);
            et.setText(card.getCompanyName());

            String year = "";
            et = (EditText)findViewById(R.id.year_edit);
            if (card.getYear() > 0) {
                year = String.valueOf(card.getYear());
            }
            et.setText(year);

            String cardNum = "";
            et = (EditText)findViewById(R.id.card_num_edit);
            if (card.getCardNum() > 0) {
                cardNum = String.valueOf(card.getCardNum());
            }
            et.setText(cardNum);

            et = (EditText)findViewById(R.id.value_edit);
            DecimalFormat df = new DecimalFormat("#######0.00");
            String value = df.format(card.getValue());
            et.setText(value);

            Spinner spinner = (Spinner)findViewById(R.id.position_spinner);
            spinner.setSelection(posIdx);

            spinner = (Spinner)findViewById(R.id.condition_spinner);
            spinner.setSelection(condIdx);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onEditNotesDialogPositiveAction(EditNotesDialogFragment dialog) {
        mNotes = dialog.getNotes();
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
        result.setCompanyName(((TextView) findViewById(R.id.company_edit)).getText().toString().trim());
        result.setFirstName(((TextView) findViewById(R.id.first_name_edit)).getText().toString().trim());
        result.setLastName(((TextView) findViewById(R.id.last_name_edit)).getText().toString().trim());
        result.setTeamName(((TextView) findViewById(R.id.team_edit)).getText().toString().trim());

        String valueString = ((TextView)findViewById(R.id.value_edit)).getText().toString().trim();
        float value = 0.0f;
        try {
            value = Float.parseFloat(valueString);
        } catch(Exception ex) {
        }
        result.setValue(value);

        String yearString = ((TextView)findViewById(R.id.year_edit)).getText().toString().trim();
        int year = 0;
        try {
            year = Integer.parseInt(yearString);
        } catch (Exception ex) {

        }
        result.setYear(year);

        String cardNumString = ((TextView)findViewById(R.id.card_num_edit)).getText().toString().trim();
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
