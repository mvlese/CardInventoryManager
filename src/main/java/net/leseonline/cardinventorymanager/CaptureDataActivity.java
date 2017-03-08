package net.leseonline.cardinventorymanager;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CaptureDataActivity extends AppCompatActivity implements EditNotesDialogFragment.IEditNotesDialogListener {
    private Spinner mPositions;
    private Spinner mConditions;
    private Button mCancelButton;
    private Button mNotesButton;
    private Button mSaveButton;
    private String mNotes;
    private int mUniqueId;
    private static BaseballCard mCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCard = null;
        mNotes = "";
        setContentView(R.layout.activity_capture_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUniqueId = getIntent().getIntExtra(getResources().getString(R.string.extra_unique_id), -1);

        mConditions = (Spinner)findViewById(R.id.condition_spinner);
        ArrayAdapter<String> conditionsSpinnerArrayAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Card.Condition.getTextValues());
        conditionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditions.setAdapter(conditionsSpinnerArrayAdapter);

        mPositions = (Spinner)findViewById(R.id.position_spinner);
        ArrayAdapter<String> positionsSpinnerArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BaseballCard.Position.getTextValues());
        positionsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositions.setAdapter(positionsSpinnerArrayAdapter);

        mCancelButton = (Button)findViewById(R.id.capture_cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mNotesButton = (Button)findViewById(R.id.capture_notes_button);
        mNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mCard = saveCardData();
                setResult(RESULT_OK);
                finish();
            }
        });
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
            year = Integer.parseInt(valueString);
        } catch (Exception ex) {

        }
        result.setYear(year);

        result.setPosition(BaseballCard.Position.values()[mPositions.getSelectedItemPosition()]);
        result.setCondition(BaseballCard.Condition.values()[mConditions.getSelectedItemPosition()]);
        result.setNotes(mNotes);

        return result;
    }
}
