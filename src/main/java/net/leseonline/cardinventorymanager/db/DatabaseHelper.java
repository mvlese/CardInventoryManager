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
import net.leseonline.cardinventorymanager.SearchModel;
import net.leseonline.cardinventorymanager.SortOrder;

import java.lang.reflect.Array;
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
//        db.execSQL(CREATE_TABLE_PLAYERS);
//        db.execSQL(CREATE_TABLE_TEAMS);
//        db.execSQL(CREATE_TABLE_PLAYER_TEAMS);
//        db.execSQL(CREATE_TABLE_PLAYER_POSITIONS);
//        db.execSQL(CREATE_TABLE_CARD_SEQUENCE);
        db.execSQL(CREATE_TABLE_EFFECT_STATES);
        db.execSQL(CREATE_TABLE_SORT_ORDERS);
        db.execSQL(CREATE_TABLE_SEARCH_MODEL);

        // Fill effect states;
        EffectState es = new EffectState();
        es.setFriendlyName("haptics");
        es.setResId(R.id.switch_haptics);
        es.setIsOn(false);
        addToEffectStates(es, db);

        es = new EffectState();
        es.setFriendlyName("sound");
        es.setResId(R.id.switch_sound);
        es.setIsOn(false);
        addToEffectStates(es, db);

        es = new EffectState();
        es.setFriendlyName("messaging");
        es.setResId(R.id.switch_messaging);
        es.setIsOn(false);
        addToEffectStates(es, db);

        // Fill sort orders.
        SortOrder so = new SortOrder();
        es.setFriendlyName("player");
        so.setEnabledResId(R.id.switch_player);
        so.setDescendingResId(R.id.switch_player_order);
        so.setSortOrder(0);
        so.setIsDesc(false);
        so.setIsEnabled(true);
        addToSortOrders(so, db);

        so = new SortOrder();
        es.setFriendlyName("team");
        so.setEnabledResId(R.id.switch_team);
        so.setDescendingResId(R.id.switch_team_order);
        so.setSortOrder(1);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        es.setFriendlyName("year");
        so.setEnabledResId(R.id.switch_year);
        so.setDescendingResId(R.id.switch_year_order);
        so.setSortOrder(2);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        es.setFriendlyName("value");
        so.setEnabledResId(R.id.switch_value);
        so.setDescendingResId(R.id.switch_value_order);
        so.setSortOrder(3);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        es.setFriendlyName("condition");
        so.setEnabledResId(R.id.switch_condition);
        so.setDescendingResId(R.id.switch_condition_order);
        so.setSortOrder(4);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);

        so = new SortOrder();
        es.setFriendlyName("company");
        so.setEnabledResId(R.id.switch_company);
        so.setDescendingResId(R.id.switch_company_order);
        so.setSortOrder(5);
        so.setIsDesc(false);
        so.setIsEnabled(false);
        addToSortOrders(so, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This code simply recreates the database.  It does not preserve the prior data.
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.CardsEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.PlayersEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.TeamsEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.PlayerTeamsEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.PlayerPositionsEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.CardSequence.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.EffectsStates.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.SortOrders.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CardsContract.SearchModel.TABLE_NAME);

        onCreate(db);
    }

    public long addCard(BaseballCard card) {
        long rowId = -1;
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            // Get player id, if any.
            // Get team id, if any.
            // Create player, get id.
            // Add position to player.
            // Create team, get id.
            // Add team to player.
            // Add card, get id

//            long playerId = getPlayerId(card.getFirstName(), card.getLastName());
//            if (playerId == -1) {
//                playerId = addPlayer(card.getFirstName(), card.getLastName(), db);
//            }
//            long teamId = getTeamId(card.getTeamName());
//            if (teamId == -1) {
//                teamId = addTeam(card.getTeamName(), db);
//            }
//
//            if (getPlayerTeamId(playerId, teamId) == -1) {
//                addTeamToPlayer(playerId, teamId, db);
//            }
//
//            if (getPlayerPositionId(playerId, card.getPosition().getCode()) == -1) {
//                addPositionToPlayer(playerId, card.getPosition(), db);
//            }

            ContentValues values = new ContentValues();
            values.put(CardsContract.CardsEntry.COLUMN_NAME_CATEGORY, card.getCategoryName());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_COMPANY, card.getCompanyName());
            //values.put(CardsContract.CardsEntry.COLUMN_NAME_PLAYER_ID, playerId);
            values.put(CardsContract.CardsEntry.COLUMN_NAME_PLAYER_FIRST_NAME, card.getFirstName());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_PLAYER_LAST_NAME, card.getLastName());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_TEAM_NAME, card.getTeamName());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_POSITION_CODE, card.getPosition().getCode());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_VALUE, card.getValue());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, card.getYear());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_CARD_NUM, card.getCardNum());
            values.put(CardsContract.CardsEntry.COLUMN_NAME_NOTES, card.getNotes());
//            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, frontImagePath);
//            values.put(CardsContract.CardsEntry.COLUMN_NAME_YEAR, backImagePath);
            values.put(CardsContract.CardsEntry.COLUMN_NAME_CONDITION_CODE, card.getCondition().getCode());

            String whereClause = "_id=?";
            String[] whereArgs = new String[]{String.valueOf(card.getUniqueId())};

            db.update(CardsContract.CardsEntry.TABLE_NAME, values, whereClause, whereArgs);

            db.setTransactionSuccessful();
            rowId = card.getUniqueId();
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

//    private long addPlayer(String firstName, String lastName, SQLiteDatabase db) {
//        long rowId = -1;
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CardsContract.PlayersEntry.COLUMN_NAME_FIRST_NAME, firstName);
//            values.put(CardsContract.PlayersEntry.COLUMN_NAME_LAST_NAME, lastName);
//
//            rowId = db.insert(CardsContract.PlayersEntry.TABLE_NAME, null, values);
//
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            rowId = -1;
//        }
//
//        return rowId;
//    }
//
//    public long addPlayer(String firstName, String lastName) {
//        long rowId = -1;
//
//        SQLiteDatabase db = null;
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CardsContract.PlayersEntry.COLUMN_NAME_FIRST_NAME, firstName);
//            values.put(CardsContract.PlayersEntry.COLUMN_NAME_LAST_NAME, lastName);
//
//            db = this.getWritableDatabase();
//            rowId = addPlayer(firstName, lastName, db);
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            rowId = -1;
//        } finally {
//            try {
//                db.close();
//            } catch(Exception ex) {}
//        }
//
//        return rowId;
//    }
//
//    private long addTeam(String teamName, SQLiteDatabase db) {
//        long rowId = -1;
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CardsContract.TeamsEntry.COLUMN_NAME_TEAM_NAME, teamName);
//
//            rowId = db.insert(CardsContract.TeamsEntry.TABLE_NAME, null, values);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            rowId = -1;
//        }
//
//        return rowId;
//    }
//
//    public long addTeam(String teamName) {
//        long rowId = -1;
//
//        SQLiteDatabase db = null;
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CardsContract.TeamsEntry.COLUMN_NAME_TEAM_NAME, teamName);
//
//            db = this.getWritableDatabase();
//            rowId = addTeam(teamName, db);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            rowId = -1;
//        } finally {
//            try {
//                db.close();
//            } catch(Exception ex) {}
//        }
//
//        return rowId;
//    }
//
//    private long addTeamToPlayer(long playerId, long teamId, SQLiteDatabase db) {
//        long rowId = -1;
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CardsContract.PlayerTeamsEntry.COLUMN_NAME_PLAYER_ID, playerId);
//            values.put(CardsContract.PlayerTeamsEntry.COLUMN_NAME_TEAM_ID, teamId);
//
//            rowId = db.insert(CardsContract.PlayerTeamsEntry.TABLE_NAME, null, values);
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            rowId = -1;
//        }
//
//        return rowId;
//    }
//
//    public long addPositionToPlayer(long playerId, BaseballCard.Position position, SQLiteDatabase db) {
//        long rowId = -1;
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CardsContract.PlayerPositionsEntry.COLUMN_NAME_PLAYER_ID, playerId);
//            values.put(CardsContract.PlayerPositionsEntry.COLUMN_NAME_POSITION_CODE, position.getCode());
//
//            rowId = db.insert(CardsContract.PlayerPositionsEntry.TABLE_NAME, null, values);
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            rowId = -1;
//        }
//
//        return rowId;
//    }

    public void deleteCardRecord(long id) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(CardsContract.CardsEntry.TABLE_NAME, "_id = ?", new String[]{String.valueOf(id)});

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method returns the player ID for the given names.
     * @param firstName the first name of interest.
     * @param lastName the last name of interest.
     * @return the player ID or -1 if not found.
     */
//    public long getPlayerId(String firstName, String lastName) {
//        return getPlayerId(firstName, null, lastName, null);
//    }

    /**
     * This method returns the player ID for the given names.
     * @param firstName the first name of interest.
     * @param middleName the middle name of interest.
     * @param lastName the last name of interest.
     * @param suffix the suffix of interest.
     * @return the player ID or -1 if not found.
     */
//    public long getPlayerId(String firstName, String middleName, String lastName, String suffix) {
//        long result = -1;
//        List<String> args = new ArrayList<String>();
//        args.add(firstName.trim().toLowerCase());
//        args.add(lastName.trim().toLowerCase());
//        String whereClause = "lower(firstName) = ? and lower(lastName) = ?";
//        if (middleName != null) {
//            whereClause += " and lower(middleName) = ?";
//            args.add(middleName.trim().toLowerCase());
//        }
//        if (suffix != null) {
//            whereClause += " and lower(suffix) = ?";
//            args.add(suffix.trim().toLowerCase());
//        }
//        String[] columns = new String[] {"_id"};
//        SQLiteDatabase db = null;
//        try {
//            db = this.getReadableDatabase();
//            Cursor c = db.query(CardsContract.PlayersEntry.TABLE_NAME, columns, whereClause, args.toArray(new String[0]), null, null, null);
//            if (c.moveToNext()) {
//                result = c.getLong(0);
//            }
//            c.close();
//        } finally {
//            try {
//                db.close();
//            }catch (Exception ex) {
//
//            }
//        }
//
//        return result;
//    }

    /**
     * This method returns the ID of the given team name, if found.
     * @param teamName the team name of interest.
     * @return the team ID or -1 if not found.
     */
//    public long getTeamId(String teamName) {
//        long result = -1;
//        String whereClause = "lower(name) = ?";
//        String[] columns = new String[] {"_id"};
//        String[] args = new String[] {teamName.toLowerCase()};
//        SQLiteDatabase db = null;
//        try {
//            db = this.getReadableDatabase();
//            Cursor c = db.query(CardsContract.TeamsEntry.TABLE_NAME, columns, whereClause, args, null, null, null);
//            if (c.moveToNext()) {
//                result = c.getLong(0);
//            }
//            c.close();
//        } finally {
//            try {
//                db.close();
//            }catch (Exception ex) {
//
//            }
//        }
//
//        return result;
//    }

//    public long getPlayerTeamId(long playerId, long teamId) {
//        long result = -1;
//        String whereClause =
//                CardsContract.PlayerTeamsEntry.COLUMN_NAME_PLAYER_ID + "=? and " +
//                        CardsContract.PlayerTeamsEntry.COLUMN_NAME_TEAM_ID + "=?";
//        String[] whereArgs = new String[] {String.valueOf(playerId), String.valueOf(teamId)};
//        String[] columns = new String[] {"_id"};
//        SQLiteDatabase db = null;
//        try {
//            db = this.getReadableDatabase();
//            Cursor c = db.query(CardsContract.PlayerTeamsEntry.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
//            if (c.moveToNext()) {
//                result = c.getLong(0);
//            }
//            c.close();
//        } finally {
//            try {
//                db.close();
//            }catch (Exception ex) {
//
//            }
//        }
//
//        return result;
//    }
//
//    public long getPlayerPositionId(long playerId, int positionCode) {
//        long result = -1;
//        String whereClause =
//                CardsContract.PlayerPositionsEntry.COLUMN_NAME_PLAYER_ID + "=? and " +
//                        CardsContract.PlayerPositionsEntry.COLUMN_NAME_POSITION_CODE + "=?";
//        String[] whereArgs = new String[] {String.valueOf(playerId), String.valueOf(positionCode)};
//        String[] columns = new String[] {"_id"};
//        SQLiteDatabase db = null;
//        try {
//            db = this.getReadableDatabase();
//            Cursor c = db.query(CardsContract.PlayerPositionsEntry.TABLE_NAME, columns, whereClause, whereArgs, null, null, null);
//            if (c.moveToNext()) {
//                result = c.getLong(0);
//            }
//            c.close();
//        } finally {
//            try {
//                db.close();
//            }catch (Exception ex) {
//
//            }
//        }
//
//        return result;
//    }

    /**
     * This method returns the next unique ID.  -- MAYBE ANOTHER METHOD WOULD BE BETTER --
     * @return
     */
    public int getNextUniqueid() {
        int rowId = -1;

        // Make a dummy entry into the Cards table to get the row ID.
        try {
            ContentValues values = new ContentValues();

            SQLiteDatabase db = this.getWritableDatabase();

            rowId = (int)db.insert(CardsContract.CardsEntry.TABLE_NAME, CardsContract.CardsEntry.COLUMN_NAME_CATEGORY, values);

            db.close();
        }catch (Exception ex) {
            ex.printStackTrace();
            rowId = -1;
        }

        return rowId;
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
                        CardsContract.SortOrders.COLUMN_NAME_FRIENDLY_NAME + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_ENABLED_RES_ID + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_DESC_RES_ID + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_SORT_ORDER + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED + ", " +
                        CardsContract.SortOrders.COLUMN_NAME_IS_DESC +
                        " from " + CardsContract.SortOrders.TABLE_NAME;
                c = db.rawQuery(sql, null);
                int n = 0;
                while (c.moveToNext()) {
                    SortOrder sortOrder = new SortOrder();
                    sortOrder.setFriendlyName(c.getString(0));
                    sortOrder.setEnabledResId(c.getInt(1));
                    sortOrder.setDescendingResId(c.getInt(2));
                    sortOrder.setSortOrder(c.getInt(3));
                    sortOrder.setIsEnabled(c.getInt(4) != 0);
                    sortOrder.setIsDesc(c.getInt(5) != 0);
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
                        CardsContract.EffectsStates.COLUMN_NAME_FRIENDLY_NAME + ", " +
                        CardsContract.EffectsStates.COLUMN_NAME_RES_ID + ", " +
                        CardsContract.EffectsStates.COLUMN_NAME_IS_ON +
                        " from " + CardsContract.EffectsStates.TABLE_NAME;
                c = db.rawQuery(sql, null);
                int n = 0;
                while (c.moveToNext()) {
                    EffectState effectState = new EffectState();
                    effectState.setFriendlyName(c.getString(0));
                    effectState.setResId(c.getInt(1));
                    effectState.setIsOn(c.getInt(2) != 0);
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

    public boolean isSoundEnabled() {
        boolean result = false;
        EffectState[] effectStates = getEffectStates();
        for (EffectState es: effectStates) {
            if (es.getFriendlyName().compareToIgnoreCase("sound") == 0) {
                result = es.isOn();
            }
        }
        return result;
    }

    public void addToEffectStates(EffectState es, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CardsContract.EffectsStates.COLUMN_NAME_FRIENDLY_NAME, es.getFriendlyName());
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
        values.put(CardsContract.SortOrders.COLUMN_NAME_FRIENDLY_NAME, so.getFriendlyName());
        values.put(CardsContract.SortOrders.COLUMN_NAME_ENABLED_RES_ID, so.getEnabledResId());
        values.put(CardsContract.SortOrders.COLUMN_NAME_DESC_RES_ID, so.getDescendingResId());
        values.put(CardsContract.SortOrders.COLUMN_NAME_SORT_ORDER, so.getSortOrder());
        values.put(CardsContract.SortOrders.COLUMN_NAME_IS_DESC, so.isDesc() ? 1 : 0);
        values.put(CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED, so.isEnabled() ? 1 : 0);

        try {
            db.insert(CardsContract.SortOrders.TABLE_NAME, null, values);
        } catch (Exception ex) {
            String s = ex.getMessage();
        }
    }

    private void setSwichState(String tableName, ContentValues values, String whereClause, int resId) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.update(tableName, values, whereClause, new String[]{String.valueOf(resId)});
        }catch (Exception ex) {
            String s = ex.getMessage();
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }
    }

    public void setSortOrderEnabledState(int resId, boolean isChecked) {
        ContentValues values = new ContentValues();
        values.put(CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED, isChecked ? 1 : 0);
        String whereClause = CardsContract.SortOrders.COLUMN_NAME_ENABLED_RES_ID + "=?";
        setSwichState(CardsContract.SortOrders.TABLE_NAME, values, whereClause, resId);
    }

    public void setSortOrderIsDescending(int resId, boolean isChecked) {
        ContentValues values = new ContentValues();
        values.put(CardsContract.SortOrders.COLUMN_NAME_IS_DESC, isChecked ? 1 : 0);
        String whereClause = CardsContract.SortOrders.COLUMN_NAME_DESC_RES_ID + "=?";
        setSwichState(CardsContract.SortOrders.TABLE_NAME, values, whereClause, resId);
    }

    public void setEffectStateIsOn(int resId, boolean isOn) {
        ContentValues values = new ContentValues();
        values.put(CardsContract.EffectsStates.COLUMN_NAME_IS_ON, isOn ? 1 : 0);
        String whereClause = CardsContract.EffectsStates.COLUMN_NAME_RES_ID + "=?";
        setSwichState(CardsContract.EffectsStates.TABLE_NAME, values, whereClause, resId);
    }

    public float getCollectionValue() {
        float value = 0.0F;
        String sql = "select sum(" + CardsContract.CardsEntry.COLUMN_NAME_VALUE + ") from " +
                CardsContract.CardsEntry.TABLE_NAME;
        SQLiteDatabase db = null;
        try {
            int count = 0;
            db = this.getReadableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToNext()) {
                value = c.getFloat(0);
            }
            c.close();
        } catch(Exception ex) {
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }

        return value;
    }

    public void saveSearchModel(SearchModel model) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            int rowsAffected = db.delete(CardsContract.SearchModel.TABLE_NAME, null, null);

            // insert new values
            ContentValues values = new ContentValues();
            values.put(CardsContract.SearchModel.COLUMN_NAME_FIRST_NAME, model.getFirstName());
            values.put(CardsContract.SearchModel.COLUMN_NAME_LAST_NAME, model.getLastName());
            values.put(CardsContract.SearchModel.COLUMN_NAME_TEAM_NAME, model.getTeamName());
            values.put(CardsContract.SearchModel.COLUMN_NAME_COMPANY, model.getCompany());
            values.put(CardsContract.SearchModel.COLUMN_NAME_YEAR, model.getYear());
            values.put(CardsContract.SearchModel.COLUMN_NAME_CARD_NUM, model.getCardNum());

            int conditionCode = model.getCondition().getCode();
            values.put(CardsContract.SearchModel.COLUMN_NAME_CONDITION, conditionCode);

            int positionCode = model.getPosition().getCode();
            values.put(CardsContract.SearchModel.COLUMN_NAME_POSITION, positionCode);

            db.insert(CardsContract.SearchModel.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        }catch (Exception ex) {
            String s = ex.getMessage();
        } finally {
            try {
                db.endTransaction();
                db.close();
            }catch (Exception ex) {
            }
        }

    }

    public SearchModel getSearchModel() {
        SearchModel model = new SearchModel();
        String[] columns = new String[] {
            CardsContract.SearchModel.COLUMN_NAME_FIRST_NAME,
            CardsContract.SearchModel.COLUMN_NAME_LAST_NAME,
            CardsContract.SearchModel.COLUMN_NAME_TEAM_NAME,
            CardsContract.SearchModel.COLUMN_NAME_COMPANY,
            CardsContract.SearchModel.COLUMN_NAME_YEAR,
            CardsContract.SearchModel.COLUMN_NAME_CARD_NUM,
            CardsContract.SearchModel.COLUMN_NAME_CONDITION,
            CardsContract.SearchModel.COLUMN_NAME_POSITION
        };
        SQLiteDatabase db = null;
        try {
            int count = 0;
            db = this.getReadableDatabase();
            Cursor c = db.query(CardsContract.SearchModel.TABLE_NAME, columns, null, null, null, null, null);
            if (c.moveToNext()) {
                int n = 0;
                model.setFirstName(c.getString(n++));
                model.setLastName(c.getString(n++));
                model.setTeamName(c.getString(n++));
                model.setCompany(c.getString(n++));
                model.setYear(c.getInt(n++));
                model.setCardNum(c.getInt(n++));
                int conditionCode = c.getInt(n++);
                model.setCondition(Card.Condition.fromCode(conditionCode));

                int positionCode = c.getInt(n++);
                model.setPosition(BaseballCard.Position.fromCode(positionCode));
            }
            c.close();
        } catch(Exception ex) {
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }

        return model;
    }

    private String getWildCardArg(String data, String column, ArrayList<String> args) {
        String result = "";
        if (data.length() > 0) {
            String operator = "=";
            String temp = data;
            if (temp.contains("*")) {
                operator = "like";
                temp = temp.replace("*", "%");
            }
            result = String.format("AND %s %s ? ", column, operator);
            args.add(temp);
        }

        return result;
    }

    /**
     * This method returns an array of Card IDs that matches the search criteria.
     * @return an array of card IDs.
     */
    public ArrayList<Long> search() {
        SearchModel searchModel = getSearchModel();
        ArrayList<Long> result = new ArrayList<Long>();
        String[] columns = new String[] {"_id"};
        ArrayList<String> args = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            String selection = "1 = 1 ";

            selection += getWildCardArg(getSearchModel().getFirstName(), CardsContract.CardsEntry.COLUMN_NAME_PLAYER_FIRST_NAME, args);
            selection += getWildCardArg(getSearchModel().getLastName(), CardsContract.CardsEntry.COLUMN_NAME_PLAYER_LAST_NAME, args);
            selection += getWildCardArg(getSearchModel().getTeamName(), CardsContract.CardsEntry.COLUMN_NAME_TEAM_NAME, args);
            selection += getWildCardArg(getSearchModel().getCompany(), CardsContract.CardsEntry.COLUMN_NAME_COMPANY, args);

            if (searchModel.getCondition() != Card.Condition.None) {
                selection += String.format("AND %s = ? ", CardsContract.CardsEntry.COLUMN_NAME_CONDITION_CODE);
                args.add(String.valueOf(searchModel.getCondition().getCode()));
            }
            if (searchModel.getPosition() != BaseballCard.Position.None) {
                selection += String.format("AND %s = ? ", CardsContract.CardsEntry.COLUMN_NAME_POSITION_CODE);
                args.add(String.valueOf(searchModel.getPosition().getCode()));
            }
            if (searchModel.getYear() > 0) {
                selection += String.format("AND %s = ? ", CardsContract.CardsEntry.COLUMN_NAME_YEAR);
                args.add(String.valueOf(searchModel.getYear()));
            }
            if (searchModel.getCardNum() > 0) {
                selection += String.format("AND %s = ? ", CardsContract.CardsEntry.COLUMN_NAME_CARD_NUM);
                args.add(String.valueOf(searchModel.getCardNum()));
            }
            int count = 0;
            db = this.getReadableDatabase();
            Cursor c = db.query(CardsContract.CardsEntry.TABLE_NAME, columns, selection, args.toArray(new String[0]), null, null, null);
            while(c.moveToNext()) {
                result.add(c.getLong(0));
            }
            c.close();
        } catch (Exception ex) {
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }

        return result;
    }

    /**
     * This method returns the BaseballCard object for the given ID.
     * @param id the card id to search for.
     * @return the BaseballCard object or null if not found;
     */
    public BaseballCard find(long id) {
        BaseballCard card = null;
        String[] args = new String[] { String.valueOf(id) };
        String[] columns = new String[]{
            CardsContract.CardsEntry.COLUMN_NAME_CATEGORY,
            CardsContract.CardsEntry.COLUMN_NAME_COMPANY,
            CardsContract.CardsEntry.COLUMN_NAME_PLAYER_FIRST_NAME,
            CardsContract.CardsEntry.COLUMN_NAME_PLAYER_LAST_NAME,
            CardsContract.CardsEntry.COLUMN_NAME_TEAM_NAME,
            CardsContract.CardsEntry.COLUMN_NAME_NOTES,
            CardsContract.CardsEntry.COLUMN_NAME_VALUE,
            CardsContract.CardsEntry.COLUMN_NAME_YEAR,
            CardsContract.CardsEntry.COLUMN_NAME_CARD_NUM,
            CardsContract.CardsEntry.COLUMN_NAME_POSITION_CODE,
            CardsContract.CardsEntry.COLUMN_NAME_CONDITION_CODE
        };
        SQLiteDatabase db = null;
        try {
            String selection = "_id = ?";

            db = this.getReadableDatabase();
            Cursor c = db.query(CardsContract.CardsEntry.TABLE_NAME, columns, selection, args, null, null, null);
            if(c.moveToNext()) {
                int n = 1;
                card = new BaseballCard((int)id);
                card.setCompanyName(c.getString(n++));
                card.setFirstName(c.getString(n++));
                card.setLastName(c.getString(n++));
                card.setTeamName(c.getString(n++));
                card.setNotes(c.getString(n++));

                String temp = c.getString(n++);
                if (temp != null && temp.length() > 0) {
                    card.setValue(Float.valueOf(temp));
                }

                temp = c.getString(n++);
                if (temp != null && temp.length() > 0) {
                    card.setYear(Integer.valueOf(temp));
                }

                temp = c.getString(n++);
                if (temp != null && temp.length() > 0) {
                    card.setCardNum(Integer.valueOf(temp));
                }

                temp = c.getString(n++);
                if (temp != null && temp.length() > 0) {
                    int code = Integer.valueOf(temp);
                    card.setPosition(BaseballCard.Position.fromCode(code));
                }

                temp = c.getString(n++);
                if (temp != null && temp.length() > 0) {
                    int code = Integer.valueOf(temp);
                    card.setCondition(BaseballCard.Condition.fromCode(code));
                }
            }
            c.close();
        } catch (Exception ex) {
        } finally {
            try {
                db.close();
            }catch (Exception ex) {
            }
        }

        return card;
    }

    public void updateCard(BaseballCard card) {
        // addCard actually does an update
        addCard(card);
    }

    private static final String CREATE_TABLE_CARDS = "CREATE TABLE " +
            CardsContract.CardsEntry.TABLE_NAME + "(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            CardsContract.CardsEntry.COLUMN_NAME_CATEGORY + " TEXT COLLATE NOCASE, " +
            CardsContract.CardsEntry.COLUMN_NAME_COMPANY + " TEXT COLLATE NOCASE, " +
//            CardsContract.CardsEntry.COLUMN_NAME_PLAYER_ID + " INT, " +
            CardsContract.CardsEntry.COLUMN_NAME_PLAYER_FIRST_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.CardsEntry.COLUMN_NAME_PLAYER_LAST_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.CardsEntry.COLUMN_NAME_TEAM_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.CardsEntry.COLUMN_NAME_POSITION_CODE + " TEXT COLLATE NOCASE, " +
            CardsContract.CardsEntry.COLUMN_NAME_VALUE + " FLOAT, " +
            CardsContract.CardsEntry.COLUMN_NAME_YEAR + " INT, " +
            CardsContract.CardsEntry.COLUMN_NAME_CARD_NUM + " INT, " +
            CardsContract.CardsEntry.COLUMN_NAME_NOTES + " TEXT COLLATE NOCASE, " +
            CardsContract.CardsEntry.COLUMN_NAME_CONDITION_CODE + " INT " +
//            CardsContract.CardsEntry.COLUMN_NAME_FRONT_IMAGE_PATH + " TEXT, " +
//            CardsContract.CardsEntry.COLUMN_NAME_BACK_IMAGE_PATH + " TEXT, " +
            CardsContract.CardsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";

//    private static final String CREATE_TABLE_PLAYERS = "CREATE TABLE " +
//            CardsContract.PlayersEntry.TABLE_NAME + "(" +
//            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//            CardsContract.PlayersEntry.COLUMN_NAME_LAST_NAME + " TEXT, " +
//            CardsContract.PlayersEntry.COLUMN_NAME_FIRST_NAME + " TEXT, " +
//            CardsContract.PlayersEntry.COLUMN_NAME_MIDDLE_NAME + " TEXT, " +
//            CardsContract.PlayersEntry.COLUMN_NAME_SUFFIX + " TEXT, " +
//            CardsContract.PlayersEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";
//
//    private static final String CREATE_TABLE_TEAMS = "CREATE TABLE " +
//            CardsContract.TeamsEntry.TABLE_NAME + "(" +
//            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//            CardsContract.TeamsEntry.COLUMN_NAME_TEAM_NAME + " TEXT, " +
//            CardsContract.TeamsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";
//
//    private static final String CREATE_TABLE_PLAYER_TEAMS = "CREATE TABLE " +
//            CardsContract.PlayerTeamsEntry.TABLE_NAME + "(" +
//            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//            CardsContract.PlayerTeamsEntry.COLUMN_NAME_PLAYER_ID + " INT, " +
//            CardsContract.PlayerTeamsEntry.COLUMN_NAME_TEAM_ID + " INT, " +
//            CardsContract.PlayerTeamsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";
//
//    private static final String CREATE_TABLE_PLAYER_POSITIONS = "CREATE TABLE " +
//            CardsContract.PlayerPositionsEntry.TABLE_NAME + "(" +
//            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//            CardsContract.PlayerPositionsEntry.COLUMN_NAME_PLAYER_ID + " INT, " +
//            CardsContract.PlayerPositionsEntry.COLUMN_NAME_POSITION_CODE + " INT, " +
//            CardsContract.PlayerPositionsEntry.COLUMN_NAME_CREATED + " DATETIME DEFAULT(datetime('now','localtime')))";
//
//    private static final String CREATE_TABLE_CARD_SEQUENCE = "CREATE TABLE " +
//            CardsContract.CardSequence.TABLE_NAME + "(" +
//            CardsContract.CardSequence.COLUMN_NAME_SEQUENCE_NUM + " INT )";

    private static final String CREATE_TABLE_EFFECT_STATES = "CREATE TABLE " +
            CardsContract.EffectsStates.TABLE_NAME + "(" +
            CardsContract.EffectsStates.COLUMN_NAME_FRIENDLY_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.EffectsStates.COLUMN_NAME_RES_ID + " INT, " +
            CardsContract.EffectsStates.COLUMN_NAME_IS_ON + " BOOLEAN)";

    private static final String CREATE_TABLE_SORT_ORDERS = "CREATE TABLE " +
            CardsContract.SortOrders.TABLE_NAME + "(" +
            CardsContract.SortOrders.COLUMN_NAME_FRIENDLY_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.SortOrders.COLUMN_NAME_ENABLED_RES_ID + " INT, " +
            CardsContract.SortOrders.COLUMN_NAME_DESC_RES_ID + " INT, " +
            CardsContract.SortOrders.COLUMN_NAME_SORT_ORDER + " INT, " +
            CardsContract.SortOrders.COLUMN_NAME_SORT_ENABLED + " BOOLEAN, " +
            CardsContract.SortOrders.COLUMN_NAME_IS_DESC + " BOOLEAN)";

    private static final String CREATE_TABLE_SEARCH_MODEL = "CREATE TABLE " +
            CardsContract.SearchModel.TABLE_NAME + "(" +
            CardsContract.SearchModel.COLUMN_NAME_FIRST_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.SearchModel.COLUMN_NAME_LAST_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.SearchModel.COLUMN_NAME_TEAM_NAME + " TEXT COLLATE NOCASE, " +
            CardsContract.SearchModel.COLUMN_NAME_COMPANY + " TEXT COLLATE NOCASE, " +
            CardsContract.SearchModel.COLUMN_NAME_YEAR + " INT, " +
            CardsContract.SearchModel.COLUMN_NAME_CARD_NUM + " INT, " +
            CardsContract.SearchModel.COLUMN_NAME_CONDITION + " INT, " +
            CardsContract.SearchModel.COLUMN_NAME_POSITION + " INT)";

}
