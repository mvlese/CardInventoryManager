package net.leseonline.cardinventorymanager.db;

import android.provider.BaseColumns;

/**
 * Created by mlese on 3/5/2017.
 */
public class CardsContract {
    public CardsContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class CardsEntry implements BaseColumns {
        // id
        // category
        // company
        // lastName
        // firstName
        // teamName
        // position
        // value
        // year
        // notes
        // condition
        // frontImagePath
        // backImagePath
        // createTimestamp
        public static final String TABLE_NAME = "cards";
        public static final String COLUMN_NAME_UNIQUE_ID = "unique_id";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_COMPANY = "company";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_FRONT_IMAGE_PATH = "front_path";
        public static final String COLUMN_NAME_BACK_IMAGE_PATH = "back_path";
        public static final String COLUMN_NAME_CONDITION_CODE = "condition";
        public static final String COLUMN_NAME_CREATED = "created";
    }

    public static abstract class PlayersEntry implements BaseColumns {
        public static final String TABLE_NAME = "players";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_MIDDLE_NAME = "middle_name";
        public static final String COLUMN_NAME_SUFFIX = "suffix";
        public static final String COLUMN_NAME_CREATED = "created";
    }

    public static abstract class TeamsEntry implements BaseColumns {
        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_NAME_TEAM_NAME = "name";
        public static final String COLUMN_NAME_CREATED = "created";
    }

    // A player may have belonged to a number of teams.
    public static abstract class PlayerTeamsEntry implements BaseColumns {
        public static final String TABLE_NAME = "player_teams";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
        public static final String COLUMN_NAME_TEAM_ID = "team_id";
        public static final String COLUMN_NAME_CREATED = "created";
    }

    // A player may have belonged to a number of teams.
    public static abstract class PlayerPositionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "player_positions";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
        public static final String COLUMN_NAME_POSITION_CODE = "position_code";
        public static final String COLUMN_NAME_CREATED = "created";
    }

    // This hold the most recent card sequence number.
    public static abstract class CardSequence implements BaseColumns {
        public static final String TABLE_NAME = "card_sequence";
        public static final String COLUMN_NAME_SEQUENCE_NUM = "sequence_num";
    }

}