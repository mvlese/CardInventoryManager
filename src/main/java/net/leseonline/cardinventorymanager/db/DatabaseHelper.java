package net.leseonline.cardinventorymanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.leseonline.cardinventorymanager.BaseballCard;
import net.leseonline.cardinventorymanager.Card;
import net.leseonline.cardinventorymanager.EffectState;
import net.leseonline.cardinventorymanager.R;
import net.leseonline.cardinventorymanager.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mlese on 3/5/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cards.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CARDS);
        db.execSQL(CREATE_TABLE_PLAYERS);
        db.execSQL(CREATE_TABLE_TEAMS);
        db.execSQL(CREATE_TABLE_PLAYER_TEAMS);
        db.execSQL(CREATE_TABLE_PLAYER_POSITIONS);
        db.execSQL(CREATE_TABLE_CARD_SEQUENCE);
        db.execSQL(CREATE_TABLE_EFFECT_STATES);
        db.execSQL(CREATE_TABLE_SORT_ORDERS);

        // Fill effect states;
        EffectState es = new EffectState();
        es.setResId(R.id.switch_haptics);
        es.setIsOn(false);
        addToEffectStates(es, db);

        es = new EffectState();
        es.setResId(R.id.switch_sound);
        es.setIsOn(false);
        addToEffectStates(es, db);

        es = new EffectState();
        es.setResId(R.id.switch_messaging);
        es.setIsOn(false);
        addToEffectStates(es, db);

        // Fill sort orders.
        SortOrder so = new SortOrder();
        so.setResId(R.id.sort_player_text_view);
        so.setSwitchResId(R.id.switch_player_name);
        so.setSortOrder(0);
        so.setIsDesc(false);
        so.setIsEnabled(true);
        addToSortOrders(so, db);

        so = new SortOrder();
        so.setResId(R.id.sort_team_text_view);
        so.setSwitchResId(R.id.switch_team_name);
        so.setSortOrder(1);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        so.setResId(R.id.sort_value_text_view);
        so.setSwitchResId(R.id.switch_value);
        so.setSortOrder(2);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        so.setResId(R.id.sort_year_text_view);
        so.setSwitchResId(R.id.switch_year);
        so.setSortOrder(3);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        so.setResId(R.id.sort_player_text_view);
        so.setSwitchResId(R.id.switch_player_name);
        so.setSortOrder(4);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        so.setResId(R.id.sort_condition_text_view);
        so.setSwitchResId(R.id.switch_condition);
        so.setSortOrder(5);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        so.setResId(R.id.sort_company_text_view);
        so.setSwitchResId(R.id.switch_company);
        so.setSortOrder(6);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This code simply recreates the database.  It does not preserve the prior data.
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.CardsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.PlayersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.TeamsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.PlayerTeamsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.PlayerPositionsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.CardSequence.TABLE_NAME);

        onCreate(db);
    }

    public long addCard(BaseballCard card) {
        long rowId = -1;
        SQLiteDatabase db = null;

        long playerId = getPlayerId(card.getFirstName(), card.getLastName());
        long teamId = getTeamId(card.getTeamName());

        try {
            ContentValues values = new ContentValues();
            values.put(CardsContract.CardsEntry.COLUMN_NAME_CATEGORY, card.getCategoryName());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_COMPANY, card.getCategoryName());
//            values.put(CardsContract.CardsEntry.COLUMN_NAME_PLAYER_ID, playerId);
            values.put(CardsContract.CardsEntry.COLUMN_NAME_VALUE, card.getValue());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, card.getYear());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, card.getNotes());
//            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, frontImagePath);
//            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, backImagePath);
            values.put(CardsContract.CardsEntry.COLUMN_NAME_CONDITION_CODE, card.getCondition().getCode());

            db = this.getWritableDatabase();
            db.beginTransaction();

            // Get player id, if any.
            // Get team id, if any.
            // Create player, get id.
            // Add position to player.
            // Create team, get id.
            // Add team to player.
            // Add card, get id

            rowId = db.insert(CardsContract.CardsEntry.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        }catch (Exception ex) {
            ex.printStackTrace();
            rowId = -1;
        }
        finally {
            try {
                db.endTransaction();
                db.close();
            } catch(Exception ex) {

            }
        }

        return rowId;
    }

    public long addPlayer(String firstName, String lastName) {
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(CardsContract.PlayersEntry.COLUMN_NAME_FIRST_NAME, firstName);
            values.put(CardsContract.PlayersEntry.COLUMN_NAME_LAST_NAME, lastName);

            SQLiteDatabase db = this.getWritableDatabase();

            rowId = db.insert(CardsContract.PlayersEntry.TABLE_NAME, null, values);

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            rowId = -1;
        }

        return rowId;
    }

    public long addTeam(String teamName) {
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(CardsContract.TeamsEntry.COLUMN_NAME_TEAM_NAME, teamName);

            SQLiteDatabase db = this.getWritableDatabase();

            rowId = db.insert(CardsContract.TeamsEntry.TABLE_NAME, null, values);

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            rowId = -1;
        }

        return rowId;
    }

    public long addTeamToPlayer(int playerId, int teamId) {
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(CardsContract.PlayerTeamsEntry.COLUMN_NAME_PLAYER_ID, playerId);
            values.put(CardsContract.PlayerTeamsEntry.COLUMN_NAME_TEAM_ID, teamId);

            SQLiteDatabase db = this.getWritableDatabase();

            rowId = db.insert(CardsContract.PlayerTeamsEntry.TABLE_NAME, null, values);

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            rowId = -1;
        }

        return rowId;
    }

    public long addPositionToPlayer(int playerId, BaseballCard.Position position) {
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(CardsContract.PlayerPositionsEntry.COLUMN_NAME_PLAYER_ID, playerId);
            values.put(CardsContract.PlayerPositionsEntry.COLUMN_NAME_POSITION_CODE, position.getCode());

            SQLiteDatabase db = this.getWritableDatabase();

            rowId = db.insert(CardsContract.PlayerPositionsEntry.TABLE_NAME, null, values);

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            rowId = -1;
        }

        return rowId;
    }

    public void deleteCardRecord(long id) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(CardsContract.CardsEntry.TABLE_NAME, "_id = ?", new String[] {String.valueOf(id)});

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public long getPlayerId(String firstName, String lastName) {
        return getPlayerId(firstName, null, lastName, null);
    }

    public long getPlayerId(String firstName, String middleName, String lastName, String suffix) {
        List<String> args = new ArrayList<String>();
        args.add(firstName.trim().toLowerCase());
        args.add(lastName.trim().toLowerCase());
        String whereClause = "lower(firstName) = ? and lower(lastName) = ?";
        if (middleName != null) {
            whereClause += " and lower(middleName) = ?";
            args.add(middleName.trim().toLowerCase());
        }
        if (suffix != null) {
            whereClause += " and lower(suffix) = ?";
            args.add(suffix.trim().toLowerCase());
        }
        String[] columns = new String[] {"_id"};
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor c = db.query(CardsContract.PlayersEntry.TABLE_NAME, columns, whereClause, args.toArray(new String[0]), null, null, null);
            if (c.moveToNext()) {
                result = c.getLong(0);
            }
            c.close();
        } finally {
            try {
                db.close();
            }catch (Exception ex) {

            }
        }

        return result;
    }

    public long getTeamId(String teamName) {
        String whereClause = "lower(name) = ?";
        String[] columns = new String[] {"_id"};
        String[] args = new String[] {teamName.toLowerCase()};
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor c = db.query(CardsContract.TeamsEntry.TABLE_NAME, columns, whereClause, args, null, null, null);
            if (c.moveToNext()) {
                result = c.getLong(0);
            }
            c.close();
        } finally {
            try {
                db.close();
            }catch (Exception ex) {

            }
        }

        return result;
    }

    public int getNextUniqueid() {
        int result = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            String[] columns = new String[] {CardsContract.CardSequence.COLUMN_NAME_SEQUENCE_NUM};

            Cursor c = db.query(CardsContract.CardSequence.TABLE_NAME, columns, null, null, null, null, null);
            if (c.moveToNext()) {
                int next = c.getInt(0) + 1;
                values.put(CardsContract.CardSequence.COLUMN_NAME_SEQUENCE_NUM, next);

                int rowsAffected = db.update(CardsContract.CardSequence.TABLE_NAME, values, null, null);
                if (rowsAffected == 1) {
                    result = next;
                }
            } else {
                // No value has been set yet, so set it to one.
                values.put(CardsContract.CardSequence.COLUMN_NAME_SEQUENCE_NUM, 1);
                long rowId = db.insert(CardsContract.CardSequence.TABLE_NAME, null, values);
                if (rowId > 0) {
                    result = 1;
                }
            }
            c.close();
            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public SortOrder[] getSortOrders() {
        SortOrder[] sortOrders = new SortOrder[0];
        String sql = "SELECT count(*) from " + CardsContract.SortOrders.TABLE_NAME;
        SQLiteDatabase db = null;
        try {
            int count = 0;
            db = this.getReadableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToNext()) {
                count = c.getInt(0);
                sortOrders = new SortOrder[count];
            }
            c.close();
            if (count > 0) {
                sql = "SELECT " +
                        CardsContract.SortOrders.COLUMN_NAME_RES_ID + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_SWITCH_RES_ID + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_SORT_ORDER + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_IS_DESC +
                        " from " + CardsContract.SortOrders.TABLE_NAME;
                c = db.rawQuery(sql, null);
                int n = 0;
                while (c.moveToNext()) {
                    SortOrder sortOrder = new SortOrder();
                    sortOrder.setResId(c.getInt(0));
                    sortOrder.setSwitchResId(c.getInt(1));
                    sortOrder.setSortOrder(c.getInt(2));
                    sortOrder.setIsEnabled(c.getInt(3) != 0);
                    sortOrder.setIsDesc(c.getInt(4) != 0);
                    sortOrders[n++] = sortOrder;
                }
                c.close();
            }
        }catch (Exception ex) {
            String s = ex.getMessage();
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }

        return sortOrders;
    }

    public EffectState[] getEffectStates() {
        EffectState[] effectStates = new EffectState[0];
        String sql = "SELECT count(*) from " + CardsContract.EffectsStates.TABLE_NAME;
        SQLiteDatabase db = null;
        try {
            int count = 0;
            db = this.getReadableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToNext()) {
                count = c.getInt(0);
                effectStates = new EffectState[count];
            }
            c.close();
            if (count > 0) {
                sql = "SELECT " +
                        CardsContract.EffectsStates.COLUMN_NAME_RES_ID + ", " +
                        CardsContract.EffectsStates.COLUMN_NAME_IS_ON +
                        " from " + CardsContract.EffectsStates.TABLE_NAME;
                c = db.rawQuery(sql, null);
                int n = 0;
                while (c.moveToNext()) {
                    EffectState effectState = new EffectState();
                    effectState.setResId(c.getInt(0));
                    effectState.setIsOn(c.getInt(1) != 0);
                    effectStates[n++] = effectState;
                }
                c.close();
            }
        }catch (Exception ex) {
            String s = ex.getMessage();
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }

        return effectStates;
    }

    public void addToEffectStates(EffectState es, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CardsContract.EffectsStates.COLUMN_NAME_RES_ID, es.getResId());
        values.put(CardsContract.EffectsStates.COLUMN_NAME_IS_ON, es.isOn() ? 1 : 0);

        try {
            db.insert(CardsContract.EffectsStates.TABLE_NAME, null, values);
        } catch (Exception ex) {
            String s = ex.getMessage();
        }
    }

    public void addToSortOrders(SortOrder so, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CardsContract.SortOrders.COLUMN_NAME_RES_ID, so.getResId());
        values.put(CardsContract.SortOrders.COLUMN_NAME_SWITCH_RES_ID, so.getSwitchResId());
        values.put(CardsContract.SortOrders.COLUMN_NAME_SORT_ORDER, so.getSortOrder());
        values.put(CardsContract.SortOrders.COLUMN_NAME_IS_DESC, so.isDesc() ? 1 : 0);
        values.put(CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED, so.isEnabled() ? 1 : 0);

        try {
            db.insert(CardsContract.SortOrders.TABLE_NAME, null, values);
        } catch (Exception ex) {
            String s = ex.getMessage();
        }
    }

    private static final String CREATE_TABLE_CARDS = "CREATE TABLE " +
            CardsContract.CardsEntry.TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            CardsContract.CardsEntry.COLUMN_NAME_CATEGORY + " TEXT, " +
            CardsContract.CardsEntry.COLUMN_NAME_COMPANY + " TEXT, " +
            CardsContract.CardsEntry.COLUMN_NAME_PLAYER_ID + " INT, " +
            CardsContract.CardsEntry.COLUMN_NAME_VALUE + " FLOAT, " +
            CardsContract.CardsEntry.COLUMN_NAME_YEAR + " INT, " +
            CardsContract.CardsEntry.COLUMN_NAME_NOTES + " TEXT, " +
            CardsContract.CardsEntry.COLUMN_NAME_CONDITION_CODE + " INT " +
            CardsContract.CardsEntry.COLUMN_NAME_FRONT_IMAGE_PATH + " TEXT, " +
            CardsContract.CardsEntry.COLUMN_NAME_BACK_IMAGE_PATH + " TEXT, " +
            CardsContract.CardsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";

    private static final String CREATE_TABLE_PLAYERS = "CREATE TABLE " +
            CardsContract.PlayersEntry.TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            CardsContract.PlayersEntry.COLUMN_NAME_LAST_NAME + " TEXT, " +
            CardsContract.PlayersEntry.COLUMN_NAME_FIRST_NAME + " TEXT, " +
            CardsContract.PlayersEntry.COLUMN_NAME_MIDDLE_NAME + " TEXT, " +
            CardsContract.PlayersEntry.COLUMN_NAME_SUFFIX + " TEXT, " +
            CardsContract.PlayersEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";

    private static final String CREATE_TABLE_TEAMS = "CREATE TABLE " +
            CardsContract.TeamsEntry.TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            CardsContract.TeamsEntry.COLUMN_NAME_TEAM_NAME + " TEXT, " +
            CardsContract.TeamsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";

    private static final String CREATE_TABLE_PLAYER_TEAMS = "CREATE TABLE " +
            CardsContract.PlayerTeamsEntry.TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            CardsContract.PlayerTeamsEntry.COLUMN_NAME_PLAYER_ID + " INT, " +
            CardsContract.PlayerTeamsEntry.COLUMN_NAME_TEAM_ID + " INT, " +
            CardsContract.PlayerTeamsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";

    private static final String CREATE_TABLE_PLAYER_POSITIONS = "CREATE TABLE " +
            CardsContract.PlayerPositionsEntry.TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY," +
            CardsContract.PlayerPositionsEntry.COLUMN_NAME_PLAYER_ID + " INT, " +
            CardsContract.PlayerPositionsEntry.COLUMN_NAME_POSITION_CODE + " INT, " +
            CardsContract.PlayerPositionsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";

    private static final String CREATE_TABLE_CARD_SEQUENCE = "CREATE TABLE " +
            CardsContract.CardSequence.TABLE_NAME + "(" +
            CardsContract.CardSequence.COLUMN_NAME_SEQUENCE_NUM + " INT )";

    private static final String CREATE_TABLE_EFFECT_STATES = "CREATE TABLE " +
            CardsContract.EffectsStates.TABLE_NAME + "(" +
            CardsContract.EffectsStates.COLUMN_NAME_RES_ID + " INT, " +
            CardsContract.EffectsStates.COLUMN_NAME_IS_ON + " BOOLEAN)";

    private static final String CREATE_TABLE_SORT_ORDERS = "CREATE TABLE " +
            CardsContract.SortOrders.TABLE_NAME + "(" +
            CardsContract.SortOrders.COLUMN_NAME_RES_ID + " INT, " +
            CardsContract.SortOrders.COLUMN_NAME_SWITCH_RES_ID + " INT, " +
            CardsContract.SortOrders.COLUMN_NAME_SORT_ORDER + " INT, " +
            CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED + " BOOLEAN, " +
            CardsContract.SortOrders.COLUMN_NAME_IS_DESC + " BOOLEAN)";

}
