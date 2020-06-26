package com.example.expensestracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Database extends SQLiteOpenHelper {

    // Database details
    private static final String DATABASE_NAME = "db_expenses";
    private static final int DATABASE_VERSION = 1;

    // Budget table
    private static final String tb_BUDGET = "tb_budget";

    private static final String id_BUDGET = "id_budget";
    private static final String tt_BUDGET = "tt_budget"; // Total budget
    private static final String rt_BUDGET = "rt_budget"; // Renew timer
    private static final String rp_BUDGET = "rp_budget"; // Renew pattern

    // Entries table
    private static final String tb_ENTRIES = "tb_entries";

    private static final String id_ENTRIES = "id_entries";
    private static final String nm_ENTRIES = "nm_entries";
    private static final String rs_ENTRIES = "rs_entries"; // Amount of money
    private static final String tp_ENTRIES = "tp_entries"; // Type ((0)expense/(1)income)
    private static final String tm_ENTRIES = "tm_entries"; // Time



    public Database(Context mContext) {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql = "CREATE TABLE "+tb_BUDGET+" (\n" +
                "    " + id_BUDGET + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + tt_BUDGET + " REAL NOT NULL, \n" +
                "    " + rt_BUDGET + " INTEGER NOT NULL, \n" +
                "    " + rp_BUDGET + " INTEGER NOT NULL \n" +
                ");";
        db.execSQL(sql);

        sql = "CREATE TABLE "+tb_ENTRIES+" (\n" +
                "    " + id_ENTRIES + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + nm_ENTRIES + " VARCHAR(100) NOT NULL,\n" +
                "    " + rs_ENTRIES + " VARCHAR(100) NOT NULL,\n" +
                "    " + tp_ENTRIES + " VARCHAR(100) NOT NULL,\n" +
                "    " + tm_ENTRIES + " VARCHAR(100) NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+tb_BUDGET+";");
        db.execSQL("DROP TABLE IF EXISTS "+tb_ENTRIES+";");
        onCreate(db);
    }

    /**
     * This method insert a new entry to the database.
     *
     * @param name
     * @param amount
     * @param type
     * @return boolean Returns {@code true} if successfully inserted
     */

    public boolean addEntry(String name, String amount, String type) {
        ContentValues vals = new ContentValues();
        vals.put(nm_ENTRIES, name);
        vals.put(rs_ENTRIES, amount);
        vals.put(tp_ENTRIES, type);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        vals.put(tm_ENTRIES, sdf.format(c.getTime()));

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tb_ENTRIES, null, vals) == 1;
    }

    /**
     * Get every entry and converts to {@code String[][]}
     * in the following pattern: {@code { [name], [amount], [type], [time], [id] }}.
     *
     * @return String[][] Transaction entries
     */

    public String[][] getEntries() {
        SQLiteDatabase db = getReadableDatabase();

        String[][] entry = new String[0][];
        String[] details;

        Cursor cursor = db.rawQuery("SELECT * FROM "+tb_ENTRIES, null);
        if (cursor.moveToLast()) {
            do {
                details = new String[] {cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(0)};
                entry = Arrays.copyOf(entry, entry.length+1);
                entry[entry.length-1] = details;
            } while (cursor.moveToPrevious());
        }
        return entry;
    }

    /**
     * Deletes entry in database with the passed {@code id}.
     *
     * @param id
     * @return boolean Returns {@code true} if successfully deleted
     */

    public boolean deleteEntry(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+tb_ENTRIES, null);

        int idd = -1;
        String[][] entries = getEntries();
        for (String[] ob : entries) {
            idd = Arrays.asList(ob).indexOf(String.valueOf(id));
            Log.d("IDD", String.valueOf(idd));
            if (idd != -1) {
                idd = Arrays.asList(entries).indexOf(ob);
                break;
            }
        }
        if (idd == -1) return false;

        Log.d("ID ON LIST", String.valueOf(idd));

        cursor.moveToPosition(idd);

        float remove = Float.parseFloat(entries[idd][1].replace(",",""));
        Log.d("REMOVE FROM BUDGET", String.valueOf(remove));
        if (Integer.parseInt(entries[idd][2]) == 1) { remove = -remove; }
        increaseBudget(remove);

        return db.delete(tb_ENTRIES, id_ENTRIES + "=?", new String[] {String.valueOf(id)}) == 1;
    }

    /**
     * Sum {@code val} to current total budget and
     * updates it to summed value.
     *
     * @param val
     * @return Nothing.
     */

    public void increaseBudget(float val) {
        SQLiteDatabase db = getWritableDatabase();

        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+tb_BUDGET, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getFloat(0) == 0);
        } cur.close();

        ContentValues vals = new ContentValues();

        if (!empty) {
            Cursor cursor = db.rawQuery("SELECT "+tt_BUDGET+" FROM "+tb_BUDGET, null);
            cursor.moveToFirst();
            float total = cursor.getFloat(0);
            cursor.close();

            float newtotal = total+val;
            Log.d("NEW TOTAL", String.valueOf(newtotal));
            vals.put(tt_BUDGET, newtotal);

            db.update(tb_BUDGET, vals, id_BUDGET + "=?", new String[] {"1"});
        } else {
            vals.put(tt_BUDGET, val);
            vals.put(rt_BUDGET, 0);
            vals.put(rp_BUDGET, 0);

            db.insert(tb_BUDGET, null, vals);
        }
    }

    /**
     * Changes total budget to new {@code val}.
     *
     * @param val
     * @return Nothing.
     */

    public void editBudget(float val) {
        SQLiteDatabase db = getWritableDatabase();

        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+tb_BUDGET, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getFloat(0) == 0);
        } cur.close();

        ContentValues vals = new ContentValues();
        if (!empty) {
            vals.put(tt_BUDGET, val);
            db.update(tb_BUDGET, vals, id_BUDGET + "=?", new String[] {"1"});
        } else {
            db.insert(tb_BUDGET, null, vals);
        }

    }

    /**
     * Gets total budget float value from database.
     *
     * @return float Returns total budget
     */

    public float getTotalBudget() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+tt_BUDGET+" FROM "+tb_BUDGET, null);
        cursor.moveToFirst();
        float total = cursor.getFloat(0);
        Log.d("RETURNED TOTAL BUDGET", String.valueOf(total));
        return total;
    }
}
