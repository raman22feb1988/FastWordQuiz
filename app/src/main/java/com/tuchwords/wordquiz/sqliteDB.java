package com.tuchwords.wordquiz;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Environment;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class sqliteDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CSW2024.db";
    private static int DATABASE_VERSION;
    public Context lastActivity;
    ArrayList<String> last;
    boolean recreate;

    ArrayList<String> queryTables;
    ArrayList<String> comparators;
    ArrayList<String> comparator;

    public sqliteDB(Context context, int version, ArrayList<String> ultimate, boolean create) {
        super(context, DATABASE_NAME, null, version);
        // TODO Auto-generated constructor stub
        lastActivity = context;
        DATABASE_VERSION = version;
        last = ultimate;
        recreate = create;

        queryTables = new ArrayList<>();
        queryTables.add("blanks");
        queryTables.add("words");

        comparators = new ArrayList<>();
        comparators.add("=");
        comparators.add(">");
        comparators.add("<");
        comparators.add(">=");
        comparators.add("<=");
        comparators.add("!=");

        comparator = new ArrayList<>();
        comparator.add("IN");
        comparator.add("NOT IN");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists words(_word_ text collate nocase primary key, _length_ integer, _alphagram_ text collate nocase, _definition_ text collate nocase, _probability_ real, _time_ real, _solved_ integer, _back_ text collate nocase, _front_ text collate nocase, _tag_ text collate nocase, _page_ integer, _answers_ integer, _csw24_ integer, _csw21_ integer, _csw19_ integer, _csw15_ integer, _csw12_ integer, _csw07_ integer, _nwl23_ integer, _nwl20_ integer, _nwl18_ integer, _twl06_ integer, _nswl23_ integer, _nswl20_ integer, _nswl18_ integer, _wims_ integer, _cel21_ integer, _serial_ integer, _position_ integer, _timestamp_ text collate nocase, _incorrect_ integer, _wrong_ text collate nocase, _reverse_ text collate nocase, _zetagram_ text collate nocase, _no_a_ integer, _no_b_ integer, _no_c_ integer, _no_d_ integer, _no_e_ integer, _no_f_ integer, _no_g_ integer, _no_h_ integer, _no_i_ integer, _no_j_ integer, _no_k_ integer, _no_l_ integer, _no_m_ integer, _no_n_ integer, _no_o_ integer, _no_p_ integer, _no_q_ integer, _no_r_ integer, _no_s_ integer, _no_t_ integer, _no_u_ integer, _no_v_ integer, _no_w_ integer, _no_x_ integer, _no_y_ integer, _no_z_ integer, _vowels_ integer, _consonants_ integer, _points_ integer, _power_ integer)"
        );
        db.execSQL(
                "create table if not exists filters(_length_ integer, _query_ text collate nocase, _blank_ integer, _complete_ integer, _incomplete_ integer, _counter_ integer, _something_ integer, _nothing_ integer, _partial_ integer, _total_ integer, _solved_ integer, _unsolved_ integer, _page_ integer, _sort_ text collate nocase, _name_ text collate nocase, _serial_ integer primary key)"
        );
        db.execSQL(
                "create table if not exists colours(_tag_ text collate nocase, _colour_ text collate nocase)"
        );
        db.execSQL(
                "create table if not exists zoom(_activity_ text collate nocase, _rows_ integer, _columns_ integer, _size_ integer, _spinner_ integer, _loader_)"
        );
        db.execSQL(
                "create table if not exists prefixes(_prefix_ text collate nocase, _before_ text collate nocase)"
        );
        db.execSQL(
                "create table if not exists suffixes(_suffix_ text collate nocase, _after_ text collate nocase)"
        );
        db.execSQL(
                "create table if not exists blanks(_word_ text collate nocase, _length_ integer, _alphagram_ text collate nocase, _anagram_ text collate nocase, _identity_ text collate nocase primary key, _definition_ text collate nocase, _probability_ real, _chance_ real, _time_ real, _solved_ integer, _back_ text collate nocase, _front_ text collate nocase, _tag_ text collate nocase, _page_ integer, _answers_ integer, _csw24_ integer, _csw21_ integer, _csw19_ integer, _csw15_ integer, _csw12_ integer, _csw07_ integer, _nwl23_ integer, _nwl20_ integer, _nwl18_ integer, _twl06_ integer, _nswl23_ integer, _nswl20_ integer, _nswl18_ integer, _wims_ integer, _cel21_ integer, _serial_ integer, _position_ integer, _timestamp_ text collate nocase, _incorrect_ integer, _wrong_ text collate nocase, _reverse_ text collate nocase, _zetagram_ text collate nocase, _omegagram_ text collate nocase, _no_a_ integer, _no_b_ integer, _no_c_ integer, _no_d_ integer, _no_e_ integer, _no_f_ integer, _no_g_ integer, _no_h_ integer, _no_i_ integer, _no_j_ integer, _no_k_ integer, _no_l_ integer, _no_m_ integer, _no_n_ integer, _no_o_ integer, _no_p_ integer, _no_q_ integer, _no_r_ integer, _no_s_ integer, _no_t_ integer, _no_u_ integer, _no_v_ integer, _no_w_ integer, _no_x_ integer, _no_y_ integer, _no_z_ integer, _total_a_ integer, _total_b_ integer, _total_c_ integer, _total_d_ integer, _total_e_ integer, _total_f_ integer, _total_g_ integer, _total_h_ integer, _total_i_ integer, _total_j_ integer, _total_k_ integer, _total_l_ integer, _total_m_ integer, _total_n_ integer, _total_o_ integer, _total_p_ integer, _total_q_ integer, _total_r_ integer, _total_s_ integer, _total_t_ integer, _total_u_ integer, _total_v_ integer, _total_w_ integer, _total_x_ integer, _total_y_ integer, _total_z_ integer, _vowels_ integer, _consonants_ integer, _points_ integer, _power_ integer, _total_vowels_ integer, _total_consonants_ integer, _total_points_ integer, _total_power_ integer)"
        );
        db.execSQL(
                "create table if not exists letters(_letter_ text collate nocase, _frequency_ integer, _points_ integer, _is_vowel_ integer, _is_consonant_ integer, _is_power_ integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        try {
            if (last != null) {
                for (String myQueries : last) {
                    db.execSQL(myQueries);
                }

                if (recreate) {
                    onCreate(db);
                }
            }
        }
        catch (SQLiteException e) {
            alertBox("Error", e.toString(), lastActivity);
        }
    }

    public void dropTable(Context activity, boolean myParent)
    {
        DATABASE_VERSION++;

        ArrayList<String> dropStatements = new ArrayList<>();
        dropStatements.add("DROP TABLE if exists words");
        dropStatements.add("DROP TABLE if exists filters");
        dropStatements.add("DROP TABLE if exists colours");
        dropStatements.add("DROP TABLE if exists zoom");
        dropStatements.add("DROP TABLE if exists prefixes");
        dropStatements.add("DROP TABLE if exists suffixes");
        dropStatements.add("DROP TABLE if exists blanks");
        dropStatements.add("DROP TABLE if exists letters");

        if (myParent)
        {
            MainActivity myActivity = (MainActivity) activity;
            myActivity.reload(dropStatements, DATABASE_VERSION, true);
        }
        else
        {
            Report reportActivity = (Report) activity;
            reportActivity.reload(dropStatements, DATABASE_VERSION, true);
        }
    }

    public void myQuery(String sqlQuery, Context activity, boolean myParent) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] tokens = sqlQuery.split("\\s+");
            ArrayList<String> theQueries = new ArrayList<>();
            theQueries.add(sqlQuery);
            if (tokens[0].equalsIgnoreCase("ALTER") || tokens[0].equalsIgnoreCase("DROP") || tokens[0].equalsIgnoreCase("CREATE") || tokens[0].equalsIgnoreCase("TRUNCATE"))
            {
                DATABASE_VERSION++;

                if (myParent)
                {
                    MainActivity myActivity = (MainActivity) activity;
                    myActivity.reload(theQueries, DATABASE_VERSION, false);
                }
                else
                {
                    Report reportActivity = (Report) activity;
                    reportActivity.reload(theQueries, DATABASE_VERSION, false);
                }
            }
            else
            {
                db.execSQL(sqlQuery);
            }

            refresh(activity, myParent);
        }
        catch (SQLiteException e) {
            alertBox("Error", e.toString(), activity);
        }
    }

    public boolean containsWord(String myWord, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM " + (blank ? "blanks" : "words") + " WHERE _word_ = \"" + myWord + "\")", null);

        int exist = 0;

        if (cursor.moveToFirst()) {
            do {
                exist = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return (exist != 0);
    }

    public String getLabel(String guess, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_ FROM " + (blank ? "blanks WHERE _identity_ = \"" : "words WHERE _word_ = \"") + guess + "\"", null);

        String label = null;

        if (cursor.moveToFirst()) {
            do {
                label = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return label;
    }

    public String getLabelColours(Context myParentContext)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_, _colour_ FROM colours ORDER BY _tag_", null);

        StringBuilder labelColours = new StringBuilder("<b>");
        int line = 1;

        int nightModeFlags =
                myParentContext.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        String white = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#000000" : "#FFFFFF");

        if (cursor.moveToFirst()) {
            do {
                String label = cursor.getString(0);
                String colour = cursor.getString(1);

                if (colour.equals(white))
                {
                    if (line == 1) {
                        labelColours.append(line).append(". ").append(label.isEmpty() ? "(Default)" : label).append(": ").append(colour);
                    } else {
                        labelColours.append("<br>").append(line).append(". ").append(label.isEmpty() ? "(Default)" : label).append(": ").append(colour);
                    }
                }
                else {
                    if (line == 1) {
                        labelColours.append("<font color=\"").append(colour).append("\">").append(line).append(". ").append(label.isEmpty() ? "(Default)" : label).append(": ").append(colour).append("</font>");
                    } else {
                        labelColours.append("<br><font color=\"").append(colour).append("\">").append(line).append(". ").append(label.isEmpty() ? "(Default)" : label).append(": ").append(colour).append("</font>");
                    }
                }
                line++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        labelColours.append("</b>");
        return new String(labelColours);
    }

    public List<Pair<String, String>> getAllLabels()
    {
        HashSet<String> labelsList = new HashSet<>();
        List<Pair<String, String>> columnItemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_, _colour_ FROM colours", null);

        if (cursor.moveToLast()) {
            do {
                String label = cursor.getString(0);
                String colour = cursor.getString(1);

                if (!labelsList.contains(label)) {
                    labelsList.add(label);
                    columnItemList.add(new Pair<>(label, colour));
                }
            } while (cursor.moveToPrevious());
        }

        cursor.close();
        Collections.sort(columnItemList, (o1, o2) -> (o1.first).compareTo(o2.first));
        return columnItemList;
    }

    public List<Pair<String, String>> getAllColours()
    {
        HashSet<String> tagList = new HashSet<>();
        List<Pair<String, String>> rowItemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_, _colour_ FROM colours", null);

        if (cursor.moveToLast()) {
            do {
                String label = cursor.getString(0);
                String colour = cursor.getString(1);

                if (!tagList.contains(colour)) {
                    tagList.add(colour);
                    rowItemList.add(new Pair<>(label, colour));
                }
            } while (cursor.moveToPrevious());
        }

        cursor.close();
        Collections.sort(rowItemList, (o1, o2) -> (o1.second).compareTo(o2.second));
        return rowItemList;
    }

    public ArrayList<String> getTableNames()
    {
        ArrayList<String> tableList = new ArrayList<>();
        int idx = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(0);

                if (idx > 0) {
                    tableList.add(data);
                }
                idx++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tableList;
    }

    public String getSchema()
    {
        StringBuilder schema = new StringBuilder();
        ArrayList<String> tablesList = getTableNames();

        for (String tableName : tablesList)
        {
            String[] columnList = getAllColumns(tableName);
            ArrayList<String> columnArray = new ArrayList<>();
            for (String columnName : columnList)
            {
                columnArray.add(columnName.substring(1, columnName.length() - 1));
            }
            schema.append(schema.length() == 0 ? "<b><u>Table '" + tableName + "'</u></b>:<br>" + columnArray : "<br><b><u>Table '" + tableName + "'</u></b>:<br>" + columnArray);
        }
        return new String(schema);
    }

    public void exportDB(Context situation, boolean parent)
    {
        LayoutInflater myInflater = LayoutInflater.from(situation);
        final View myCustomView = myInflater.inflate(R.layout.progressbar, null);

        ProgressBar p4 = myCustomView.findViewById(R.id.progressbar1);
        TextView t41 = myCustomView.findViewById(R.id.textview78);
        TextView t42 = myCustomView.findViewById(R.id.textview79);

        AlertDialog myDialog = new AlertDialog.Builder(situation)
                .setTitle("Exporting CSV")
                .setView(myCustomView)
                .create();
        myDialog.show();

        SQLiteDatabase db = this.getReadableDatabase();

        Thread thread4 = new Thread(() -> {
            File[] inputDir = ContextCompat.getExternalFilesDirs(situation, Environment.DIRECTORY_DOCUMENTS);
            File exportDir = inputDir[0];

            ArrayList<String> tables = getTableNames();
            StringBuilder outputDir = new StringBuilder();
            try {
                for (String table : tables) {
                    File file = new File(exportDir, table + ".csv");
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    Cursor curCSV = db.rawQuery("SELECT * FROM " + table, null);
                    String[] columnsList = curCSV.getColumnNames();
                    for (int number = 0; number < columnsList.length; number++) {
                        columnsList[number] = columnsList[number].substring(1, columnsList[number].length() - 1);
                    }
                    csvWrite.writeNext(columnsList);
                    double myLine = 0.0;
                    double myStep = curCSV.getCount() / 100.0;
                    while (curCSV.moveToNext()) {
                        String[] arrStr = new String[columnsList.length];
                        for (int index = 0; index < columnsList.length; index++) {
                            arrStr[index] = curCSV.getString(index);
                        }
                        csvWrite.writeNext(arrStr);
                        myLine++;
                        if (myLine % myStep < 1 || myLine == 1.0) {
                            updateProgressBar(situation, parent, p4, t41, t42, myDialog, myLine / myStep, ((int) myLine) + "/" + curCSV.getCount(), false);
                        }
                    }

                    csvWrite.close();
                    curCSV.close();
                    outputDir.append("\nSaved ").append(table).append(" table to ").append(file.getAbsolutePath()).append(".");
                    uiThreadBox("Export CSV", "Export CSV complete." + new String(outputDir), situation, parent);
                }
            } catch (Exception sqlEx) {
                myDialog.dismiss();
                uiThreadBox("Error", sqlEx.toString(), situation, parent);
            } finally {
                myDialog.dismiss();
            }
        });

        thread4.start();
    }

    public void importDB(Context situation, boolean parent)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        File[] inputDir = ContextCompat.getExternalFilesDirs(situation, Environment.DIRECTORY_DOCUMENTS);
        File exportDir = inputDir[0];
        File storageDir = Environment.getExternalStorageDirectory();
        String dataDir = exportDir.getAbsolutePath() + "/words.csv";
        String path = dataDir.substring((storageDir.getAbsolutePath()).length() + 1);
        String database = "words";

        LayoutInflater inflater = LayoutInflater.from(situation);
        final View yourCustomView = inflater.inflate(R.layout.path, null);

        TextView t4 = yourCustomView.findViewById(R.id.textview22);
        EditText e2 = yourCustomView.findViewById(R.id.edittext10);
        EditText e3 = yourCustomView.findViewById(R.id.edittext11);

        t4.setText(storageDir.getAbsolutePath() + "/");
        e2.setText(path);
        e3.setText(database);

        AlertDialog dialog = new AlertDialog.Builder(situation)
                .setTitle("File name")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    LayoutInflater myInflater = LayoutInflater.from(situation);
                    final View myCustomView = myInflater.inflate(R.layout.progressbar, null);

                    ProgressBar p2 = myCustomView.findViewById(R.id.progressbar1);
                    TextView t37 = myCustomView.findViewById(R.id.textview78);
                    TextView t38 = myCustomView.findViewById(R.id.textview79);

                    AlertDialog myDialog = new AlertDialog.Builder(situation)
                            .setTitle("Importing CSV")
                            .setView(myCustomView)
                            .create();
                    myDialog.show();

                    Thread thread2 = new Thread(() -> {
                        String databaseName = (e3.getText()).toString();
                        ArrayList<String> databases = getTableNames();
                        if (databases.contains(databaseName)) {
                            File file = new File(storageDir, (e2.getText()).toString());
                            try {
                                CSVReader csvRead = new CSVReader(new FileReader(file));
                                db.beginTransaction();
                                try {
                                    String[] columns = csvRead.readNext();
                                    String[] nextLine = csvRead.readNext();

                                    int lines = -1;
                                    BufferedReader reader = new BufferedReader(new FileReader(file));
                                    while (reader.readLine() != null) {
                                        lines++;
                                    }

                                    reader.close();

                                    double myLine = 0.0;
                                    double myStep = lines / 100.0;
                                    do {
                                        ContentValues contentValues = new ContentValues();
                                        for (int column = 0; column < columns.length; column++) {
                                            contentValues.put("_" + columns[column] + "_", nextLine[column]);
                                        }
                                        db.insert(databaseName, null, contentValues);
                                        nextLine = csvRead.readNext();
                                        myLine++;
                                        if (myLine % myStep < 1 || myLine == 1.0) {
                                            updateProgressBar(situation, parent, p2, t37, t38, myDialog, myLine / myStep, ((int) myLine) + "/" + lines, false);
                                        }
                                    } while (nextLine != null);

                                    csvRead.close();
                                    db.setTransactionSuccessful();
                                    uiThreadRefresh(situation, parent, true);
                                    uiThreadBox("Import CSV", "Import CSV complete.", situation, parent);
                                } finally {
                                    db.endTransaction();
                                }
                            } catch (Exception e) {
                                uiThreadBox("Error", e.toString(), situation, parent);
                            } finally {
                                myDialog.dismiss();
                            }
                        } else {
                            myDialog.dismiss();
                            uiThreadBox("Error", "Table '" + databaseName + "' not found. Create a new table with the name '" + databaseName + "' at first.", situation, parent);
                        }
                    });

                    thread2.start();
                }).create();
        dialog.show();
    }

    public void exportLabels(Context situation, boolean parent)
    {
        LayoutInflater myInflater = LayoutInflater.from(situation);
        final View myCustomView = myInflater.inflate(R.layout.progressbar, null);

        ProgressBar p3 = myCustomView.findViewById(R.id.progressbar1);
        TextView t39 = myCustomView.findViewById(R.id.textview78);
        TextView t40 = myCustomView.findViewById(R.id.textview79);

        AlertDialog myDialog = new AlertDialog.Builder(situation)
                .setTitle("Exporting tags")
                .setView(myCustomView)
                .create();
        myDialog.show();

        SQLiteDatabase db = this.getReadableDatabase();

        Thread thread3 = new Thread(() -> {
            File[] inputDir = ContextCompat.getExternalFilesDirs(situation, Environment.DIRECTORY_DOCUMENTS);
            File exportDir = inputDir[0];

            File file = new File(exportDir, "tags.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                for (int turn = 0; turn < 2; turn++) {
                    Cursor curCSV = db.rawQuery("SELECT _word_, " + ((turn == 0) ? "_alphagram_" : "_anagram_") + ", _tag_, _solved_, _time_, _timestamp_, _incorrect_, _wrong_ FROM " + ((turn == 0) ? "words" : "blanks") + " WHERE _time_ > 0 OR _solved_ > 0 OR _incorrect_ > 0 OR _tag_ != \"\" OR _timestamp_ != \"\" OR _wrong_ != \"\"", null);
                    String[] columnsList = curCSV.getColumnNames();

                    if (turn == 0) {
                        for (int number = 0; number < columnsList.length; number++) {
                            columnsList[number] = columnsList[number].substring(1, columnsList[number].length() - 1);
                        }
                        csvWrite.writeNext(columnsList);
                    }

                    double myLine = 0.0;
                    double myStep = curCSV.getCount() / 100.0;
                    while (curCSV.moveToNext()) {
                        String[] arrStr = new String[columnsList.length];
                        for (int index = 0; index < columnsList.length; index++) {
                            arrStr[index] = curCSV.getString(index);
                        }
                        csvWrite.writeNext(arrStr);
                        myLine++;
                        if (myLine % myStep < 1 || myLine == 1.0) {
                            updateProgressBar(situation, parent, p3, t39, t40, myDialog, myLine / myStep, ((int) myLine) + "/" + curCSV.getCount(), false);
                        }
                    }

                    curCSV.close();
                }

                csvWrite.close();
                uiThreadBox("Export tags", "Export tags complete.\nSaved tags to " + file.getAbsolutePath() + ".", situation, parent);
            } catch (Exception sqlEx) {
                myDialog.dismiss();
                uiThreadBox("Error", sqlEx.toString(), situation, parent);
            } finally {
                myDialog.dismiss();
            }
        });

        thread3.start();
    }

    public void importLabels(Context situation, boolean parent)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        File[] inputDir = ContextCompat.getExternalFilesDirs(situation, Environment.DIRECTORY_DOCUMENTS);
        File exportDir = inputDir[0];
        File storageDir = Environment.getExternalStorageDirectory();
        String dataDir = exportDir.getAbsolutePath() + "/tags.csv";
        String path = dataDir.substring((storageDir.getAbsolutePath()).length() + 1);

        LayoutInflater inflater = LayoutInflater.from(situation);
        final View yourCustomView = inflater.inflate(R.layout.message, null);

        TextView t3 = yourCustomView.findViewById(R.id.textview21);
        EditText e1 = yourCustomView.findViewById(R.id.edittext9);

        t3.setText(storageDir.getAbsolutePath() + "/");
        e1.setText(path);

        AlertDialog dialog = new AlertDialog.Builder(situation)
                .setTitle("File name")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    LayoutInflater myInflater = LayoutInflater.from(situation);
                    final View myCustomView = myInflater.inflate(R.layout.progressbar, null);

                    ProgressBar p1 = myCustomView.findViewById(R.id.progressbar1);
                    TextView t35 = myCustomView.findViewById(R.id.textview78);
                    TextView t36 = myCustomView.findViewById(R.id.textview79);

                    AlertDialog myDialog = new AlertDialog.Builder(situation)
                            .setTitle("Importing tags")
                            .setView(myCustomView)
                            .create();
                    myDialog.show();

                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(storageDir, (e1.getText()).toString());
                            try {
                                CSVReader csvRead = new CSVReader(new FileReader(file));
                                db.beginTransaction();
                                try {
                                    String[] columns = csvRead.readNext();
                                    String[] nextLine = csvRead.readNext();

                                    int lines = -1;
                                    BufferedReader reader = new BufferedReader(new FileReader(file));
                                    while (reader.readLine() != null) {
                                        lines++;
                                    }

                                    reader.close();

                                    int wordIndex = 0;
                                    int anagramIndex = 0;
                                    ArrayList<Integer> otherIndex = new ArrayList<>();

                                    for (int column = 0; column < columns.length; column++) {
                                        if (columns[column].equals("word")) {
                                            wordIndex = column;
                                        } else if (columns[column].equals("alphagram")) {
                                            anagramIndex = column;
                                        } else {
                                            otherIndex.add(column);
                                        }
                                    }

                                    double myLine = 0.0;
                                    double myStep = lines / 100.0;
                                    do {
                                        boolean isBlank = (nextLine[anagramIndex].charAt(nextLine[anagramIndex].length() - 1) == '?');
                                        ContentValues contentValues = new ContentValues();

                                        for (int columnNumber : otherIndex) {
                                            contentValues.put("_" + columns[columnNumber] + "_", nextLine[columnNumber]);
                                        }

                                        db.update(isBlank ? "blanks" : "words", contentValues, isBlank ? "_identity_ = ?" : "_word_ = ?",
                                                new String[] {isBlank ? nextLine[wordIndex] + " " + nextLine[anagramIndex] : nextLine[wordIndex]});
                                        nextLine = csvRead.readNext();
                                        myLine++;
                                        if (myLine % myStep < 1 || myLine == 1.0) {
                                            updateProgressBar(situation, parent, p1, t35, t36, myDialog, myLine / myStep, ((int) myLine) + "/" + lines, false);
                                        }
                                    } while (nextLine != null);

                                    csvRead.close();
                                    db.setTransactionSuccessful();
                                    uiThreadRefresh(situation, parent, false);
                                    uiThreadBox("Import tags", "Import tags complete.", situation, parent);
                                } finally {
                                    db.endTransaction();
                                }
                            } catch (Exception e) {
                                uiThreadBox("Error", e.toString(), situation, parent);
                            } finally {
                                myDialog.dismiss();
                            }
                        }
                    });

                    thread1.start();
                }).create();
        dialog.show();
    }

    public int getMaximumWordLength(boolean blank)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(_length_) FROM " + (blank ? "blanks" : "words"), null);
        int maximumWordLength = 0;

        if (cursor.moveToFirst()) {
            do {
                maximumWordLength = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return ((maximumWordLength < 2) ? 58 : maximumWordLength);
    }

    public double probability(String st)
    {
        int[] frequency = new int[] {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int count = 100;
        double chance = 1;
        for (int j = 0; j < st.length(); j++)
        {
            char ch = st.charAt(j);
            int ord = ((int) ch) - 65;
            chance *= (ch == '?' ? 2 : frequency[ord]);
            chance /= count;
            if (ord >= 0 && frequency[ord] > 0) {
                frequency[ord]--;
            }
            count--;
        }
        return chance;
    }

    public void insertWord(Context myContext, boolean yourParent, HashMap<String, String> dictionary, HashMap<String, Integer> anagramsList, HashMap<String, String> lexicon, boolean joker)
    {
        String[] alphabetList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int[] frequency = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int[] point = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        boolean[] vowel = {true, false, false, false, true, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false};
        boolean[] consonant = {false, true, true, true, false, true, true, true, false, true, true, true, true, true, false, true, true, true, true, true, false, true, true, true, true, true};
        boolean[] power = {false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, true, false, false, false, false, false, false, true, false, true};

        LayoutInflater myInflater = LayoutInflater.from(myContext);
        final View myCustomView = myInflater.inflate(R.layout.progressbar, null);

        ProgressBar p5 = myCustomView.findViewById(R.id.progressbar1);
        TextView t43 = myCustomView.findViewById(R.id.textview78);
        TextView t44 = myCustomView.findViewById(R.id.textview79);

        AlertDialog myDialog = new AlertDialog.Builder(myContext)
                .setTitle("Inserting words")
                .setView(myCustomView)
                .create();
        myDialog.show();

        SQLiteDatabase db = this.getWritableDatabase();

        Thread thread5 = new Thread(() -> {
            db.beginTransaction();

            try {
                Iterator<Map.Entry<String, String>> itr = dictionary.entrySet().iterator();
                double myLine = 0.0;
                double myStep1 = dictionary.size() / (joker ? 400.0 : 40.0);
                while (itr.hasNext()) {
                    Map.Entry<String, String> entry = itr.next();
                    String word = entry.getKey();
                    char[] c = word.toCharArray();
                    Arrays.sort(c);
                    String anagram = new String(c);
                    String definition = entry.getValue();
                    StringBuilder back = new StringBuilder();
                    StringBuilder front = new StringBuilder();
                    for (char letter = 'A'; letter <= 'Z'; letter++) {
                        if (dictionary.containsKey(word + letter)) {
                            back.append(letter);
                        }
                        if (dictionary.containsKey(letter + word)) {
                            front.append(letter);
                        }
                    }

                    String lexiconList = lexicon.get(word);
                    String[] lexiconsList = lexiconList.split(",");

                    int csw24 = Integer.parseInt(lexiconsList[1]);
                    int csw21 = Integer.parseInt(lexiconsList[2]);
                    int csw19 = Integer.parseInt(lexiconsList[3]);
                    int csw15 = Integer.parseInt(lexiconsList[4]);
                    int csw12 = Integer.parseInt(lexiconsList[5]);
                    int csw07 = Integer.parseInt(lexiconsList[6]);
                    int nwl23 = Integer.parseInt(lexiconsList[7]);
                    int nwl20 = Integer.parseInt(lexiconsList[8]);
                    int nwl18 = Integer.parseInt(lexiconsList[9]);
                    int twl06 = Integer.parseInt(lexiconsList[10]);
                    int nswl23 = Integer.parseInt(lexiconsList[11]);
                    int nswl20 = Integer.parseInt(lexiconsList[12]);
                    int nswl18 = Integer.parseInt(lexiconsList[13]);
                    int cel21 = Integer.parseInt(lexiconsList[14]);
                    int wims = Integer.parseInt(lexiconsList[15]);

                    int vowels = 0;
                    int consonants = 0;
                    int points = 0;
                    int powers = 0;

                    int[] occurrence = new int[26];
                    for (int myRadix = 0; myRadix < word.length(); myRadix++) {
                        char theCharacter = word.charAt(myRadix);
                        int positionInAlphabet = theCharacter - 65;
                        occurrence[positionInAlphabet]++;
                        points += point[positionInAlphabet];

                        if (vowel[positionInAlphabet]) {
                            vowels++;
                        }

                        if (consonant[positionInAlphabet]) {
                            consonants++;
                        }

                        if (power[positionInAlphabet]) {
                            powers++;
                        }
                    }

                    if (joker) {
                        HashSet<Character> used = new HashSet<>();

                        for (int letterIndex = 0; letterIndex < word.length(); letterIndex++) {
                            char character = word.charAt(letterIndex);
                            int ascii = character - 65;
                            if (!used.contains(character)) {
                                used.add(character);
                                String subword = word.substring(0, letterIndex) + word.substring(letterIndex + 1);
                                char[] subcharacter = subword.toCharArray();
                                Arrays.sort(subcharacter);
                                String subletter = new String(subcharacter);
                                String subalphagram = subletter + "?";
                                int solutions = anagramsList.get(subalphagram);

                                ContentValues contentValues = new ContentValues();

                                contentValues.put("_word_", word);
                                contentValues.put("_length_", word.length());
                                contentValues.put("_alphagram_", anagram);
                                contentValues.put("_anagram_", subalphagram);
                                contentValues.put("_identity_", word + " " + subalphagram);
                                contentValues.put("_definition_", definition);
                                contentValues.put("_probability_", probability(subalphagram));
                                contentValues.put("_chance_", probability(word));
                                contentValues.put("_time_", 0);
                                contentValues.put("_solved_", 0);
                                contentValues.put("_back_", new String(back));
                                contentValues.put("_front_", new String(front));
                                contentValues.put("_tag_", (word.length() <= 15 && csw21 == 0) ? "New" : "");
                                contentValues.put("_page_", 0);
                                contentValues.put("_answers_", solutions);
                                contentValues.put("_csw24_", csw24);
                                contentValues.put("_csw21_", csw21);
                                contentValues.put("_csw19_", csw19);
                                contentValues.put("_csw15_", csw15);
                                contentValues.put("_csw12_", csw12);
                                contentValues.put("_csw07_", csw07);
                                contentValues.put("_nwl23_", nwl23);
                                contentValues.put("_nwl20_", nwl20);
                                contentValues.put("_nwl18_", nwl18);
                                contentValues.put("_twl06_", twl06);
                                contentValues.put("_nswl23_", nswl23);
                                contentValues.put("_nswl20_", nswl20);
                                contentValues.put("_nswl18_", nswl18);
                                contentValues.put("_cel21_", cel21);
                                contentValues.put("_wims_", wims);
                                contentValues.put("_serial_", 0);
                                contentValues.put("_position_", 0);
                                contentValues.put("_timestamp_", "");
                                contentValues.put("_incorrect_", 0);
                                contentValues.put("_wrong_", "");
                                contentValues.put("_reverse_", ((new StringBuilder(word)).reverse()).toString());
                                contentValues.put("_zetagram_", ((new StringBuilder(anagram)).reverse()).toString());
                                contentValues.put("_omegagram_", (((new StringBuilder(subletter)).reverse()).append("?")).toString());

                                for (int theRadix = 0; theRadix < 26; theRadix++) {
                                    char occurrences = (char) (theRadix + 97);
                                    contentValues.put("_no_" + occurrences + "_", theRadix == ascii ? occurrence[theRadix] - 1 : occurrence[theRadix]);
                                    contentValues.put("_total_" + occurrences + "_", occurrence[theRadix]);
                                }

                                contentValues.put("_vowels_", vowel[ascii] ? vowels - 1 : vowels);
                                contentValues.put("_consonants_", vowel[ascii] ? consonants : consonants - 1);
                                contentValues.put("_points_", points - point[ascii]);
                                contentValues.put("_power_", power[ascii] ? powers - 1 : powers);
                                contentValues.put("_total_vowels_", vowels);
                                contentValues.put("_total_consonants_", consonants);
                                contentValues.put("_total_points_", points);
                                contentValues.put("_total_power_", powers);

                                db.insert("blanks", null, contentValues);
                            }
                        }
                    }
                    else {
                        int solutions = anagramsList.get(anagram);
                        ContentValues contentValues = new ContentValues();

                        contentValues.put("_word_", word);
                        contentValues.put("_length_", word.length());
                        contentValues.put("_alphagram_", anagram);
                        contentValues.put("_definition_", definition);
                        contentValues.put("_probability_", probability(word));
                        contentValues.put("_time_", 0);
                        contentValues.put("_solved_", 0);
                        contentValues.put("_back_", new String(back));
                        contentValues.put("_front_", new String(front));
                        contentValues.put("_tag_", (word.length() <= 15 && csw21 == 0) ? "New" : "");
                        contentValues.put("_page_", 0);
                        contentValues.put("_answers_", solutions);
                        contentValues.put("_csw24_", csw24);
                        contentValues.put("_csw21_", csw21);
                        contentValues.put("_csw19_", csw19);
                        contentValues.put("_csw15_", csw15);
                        contentValues.put("_csw12_", csw12);
                        contentValues.put("_csw07_", csw07);
                        contentValues.put("_nwl23_", nwl23);
                        contentValues.put("_nwl20_", nwl20);
                        contentValues.put("_nwl18_", nwl18);
                        contentValues.put("_twl06_", twl06);
                        contentValues.put("_nswl23_", nswl23);
                        contentValues.put("_nswl20_", nswl20);
                        contentValues.put("_nswl18_", nswl18);
                        contentValues.put("_cel21_", cel21);
                        contentValues.put("_wims_", wims);
                        contentValues.put("_serial_", 0);
                        contentValues.put("_position_", 0);
                        contentValues.put("_timestamp_", "");
                        contentValues.put("_incorrect_", 0);
                        contentValues.put("_wrong_", "");
                        contentValues.put("_reverse_", ((new StringBuilder(word)).reverse()).toString());
                        contentValues.put("_zetagram_", ((new StringBuilder(anagram)).reverse()).toString());

                        for (int theRadix = 0; theRadix < 26; theRadix++) {
                            char occurrences = (char) (theRadix + 97);
                            contentValues.put("_no_" + occurrences + "_", occurrence[theRadix]);
                        }

                        contentValues.put("_vowels_", vowels);
                        contentValues.put("_consonants_", consonants);
                        contentValues.put("_points_", points);
                        contentValues.put("_power_", powers);

                        db.insert("words", null, contentValues);
                    }

                    myLine++;
                    if (myLine % myStep1 < 1 || myLine == 1.0)
                    {
                        updateProgressBar(myContext, yourParent, p5, t43, t44, myDialog, myLine / (joker ? myStep1 * 10 : myStep1), ((int) myLine) + "/" + dictionary.size(), joker);
                    }
                }

                Cursor[] anagramList = new Cursor[getMaximumWordLength(joker) - 1];
                int[] wordLength = new int[anagramList.length];
                int[] pages = new int[anagramList.length];
                int maximumPages = 0;

                for (int lengths = 0; lengths < anagramList.length; lengths++) {
                    anagramList[lengths] = (joker ? getAllBlankAnagrams(lengths + 2) : getAllRegularAnagrams(lengths + 2));
                    wordLength[lengths] = anagramList[lengths].getCount();
                    pages[lengths] = (((wordLength[lengths] - 1) / 50) + 1);

                    if (pages[lengths] > maximumPages) {
                        maximumPages = pages[lengths];
                    }
                }

                uiThreadTitle("Setting page numbers", myDialog, myContext, yourParent);
                double myStep2 = maximumPages / (joker ? 500.0 : 50.0);
                for (int positionNumber = 1; positionNumber <= maximumPages; positionNumber++) {
                    ArrayList<String> pageHash = new ArrayList<>();

                    for (int lengths = 0; lengths < anagramList.length; lengths++) {
                        if (positionNumber <= pages[lengths]) {
                            int open = (positionNumber - 1) * 50;
                            int close = Math.min(positionNumber * 50, wordLength[lengths]);

                            if (anagramList[lengths].moveToPosition(open)) {
                                do {
                                    pageHash.add(anagramList[lengths].getString(0));
                                } while (anagramList[lengths].moveToNext() && anagramList[lengths].getPosition() < close);
                            }
                        }
                    }

                    String pageString = ((((pageHash).toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
                    ContentValues values = new ContentValues();
                    values.put("_page_", positionNumber);

                    db.update(joker ? "blanks" : "words", values, (joker ? "_anagram_ IN " : "_alphagram_ IN ") + pageString,
                            new String[] {});

                    if (positionNumber % myStep2 < 1 || positionNumber == 1)
                    {
                        updateProgressBar(myContext, yourParent, p5, t43, t44, myDialog, 40 + (positionNumber / (joker ? myStep2 * 10 : myStep2)), positionNumber + "/" + maximumPages, joker);
                    }
                }

                uiThreadTitle("Setting grid numbers", myDialog, myContext, yourParent);
                double myStep3 = (joker ? 0.5 : 5.0);
                for (int cellNumber = 1; cellNumber <= 50.0; cellNumber++) {
                    ArrayList<String> cellHash = new ArrayList<>();

                    for (int lengths = 0; lengths < anagramList.length; lengths++) {
                        if (cellNumber <= wordLength[lengths]) {
                            if (anagramList[lengths].moveToPosition(cellNumber - 1)) {
                                do {
                                    cellHash.add(anagramList[lengths].getString(0));
                                } while (anagramList[lengths].move(50));
                            }
                        }
                    }

                    String cellString = ((((cellHash).toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
                    ContentValues values = new ContentValues();
                    values.put("_position_", cellNumber);

                    db.update(joker ? "blanks" : "words", values, (joker ? "_anagram_ IN " : "_alphagram_ IN ") + cellString,
                            new String[] {});

                    if (cellNumber % myStep3 < 1 || cellNumber == 1)
                    {
                        updateProgressBar(myContext, yourParent, p5, t43, t44, myDialog, 90 + (cellNumber / (joker ? myStep3 * 10 : myStep3)), cellNumber + "/50", joker);
                    }
                }

                for (Cursor cursors : anagramList) {
                    cursors.close();
                }

                if (!joker) {
                    int nightModeFlags =
                            myContext.getResources().getConfiguration().uiMode &
                                    Configuration.UI_MODE_NIGHT_MASK;
                    String white = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#000000" : "#FFFFFF");
                    String black = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#FFFFFF" : "#000000");

                    HashMap<String, String> coloursList = new HashMap<>();

                    coloursList.put("Known", "#008000");
                    coloursList.put("Unknown", "#FF0000");
                    coloursList.put("Compound", "#FF00FF");
                    coloursList.put("Prefix", "#8000FF");
                    coloursList.put("Suffix", "#0000FF");
                    coloursList.put("Plural", "#808080");
                    coloursList.put("Guessable", "#FF8000");
                    coloursList.put("Past", "#0080FF");
                    coloursList.put("Learnt", "#B97A57");
                    coloursList.put("New", "#C0C000");
                    coloursList.put("Removed", white);
                    coloursList.put("", black);

                    for (Map.Entry<String, String> enter : coloursList.entrySet()) {
                        String tag = enter.getKey();
                        String tags = coloursList.get(tag);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("_tag_", tag);
                        contentValues.put("_colour_", tags);

                        db.insert("colours", null, contentValues);
                    }

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_activity_", "Quiz");
                    contentValues.put("_rows_", 10);
                    contentValues.put("_columns_", 5);
                    contentValues.put("_size_", 11);
                    contentValues.put("_spinner_", 20);
                    contentValues.put("_loader_", 20);
                    db.insert("zoom", null, contentValues);

                    contentValues = new ContentValues();
                    contentValues.put("_activity_", "List");
                    contentValues.put("_rows_", 100);
                    contentValues.put("_columns_", 0);
                    contentValues.put("_size_", 11);
                    contentValues.put("_spinner_", 20);
                    contentValues.put("_loader_", 20);
                    db.insert("zoom", null, contentValues);

                    contentValues = new ContentValues();
                    contentValues.put("_activity_", "Grid");
                    contentValues.put("_rows_", 25);
                    contentValues.put("_columns_", 4);
                    contentValues.put("_size_", 11);
                    contentValues.put("_loader_", 20);
                    db.insert("zoom", null, contentValues);

                    ArrayList<Pair<String, String>> myPrefixes = new ArrayList<>();
                    myPrefixes.add(new Pair<>("", ""));

                    for (Pair<String, String> columnItem : myPrefixes) {
                        ContentValues prefixValues = new ContentValues();
                        prefixValues.put("_prefix_", columnItem.first);
                        prefixValues.put("_before_", columnItem.second);
                        db.insert("prefixes", null, prefixValues);
                    }

                    ArrayList<Pair<String, String>> mySuffixes = new ArrayList<>();
                    mySuffixes.add(new Pair<>("", ""));
                    mySuffixes.add(new Pair<>("-", "E"));
                    mySuffixes.add(new Pair<>("-I", "Y"));

                    for (Pair<String, String> columnItem : mySuffixes) {
                        ContentValues suffixValues = new ContentValues();
                        suffixValues.put("_suffix_", columnItem.first);
                        suffixValues.put("_after_", columnItem.second);
                        db.insert("suffixes", null, suffixValues);
                    }
                }

                db.execSQL("UPDATE " + (joker ? "blanks" : "words") + " SET _serial_ = ((_page_ - 1) * 50) + _position_");

                for (int alphabetPosition = 0; alphabetPosition < alphabetList.length; alphabetPosition++) {
                    ContentValues letterValues = new ContentValues();
                    letterValues.put("_letter_", alphabetList[alphabetPosition]);
                    letterValues.put("_frequency_", frequency[alphabetPosition]);
                    letterValues.put("_points_", point[alphabetPosition]);
                    letterValues.put("_is_vowel_", vowel[alphabetPosition]);
                    letterValues.put("_is_consonant_", consonant[alphabetPosition]);
                    letterValues.put("_is_power_", power[alphabetPosition]);
                    db.insert("letters", null, letterValues);
                }

                ContentValues alphabetValues = new ContentValues();
                alphabetValues.put("_letter_", "?");
                alphabetValues.put("_frequency_", 2);
                alphabetValues.put("_points_", 0);
                alphabetValues.put("_is_vowel_", 0);
                alphabetValues.put("_is_consonant_", 0);
                alphabetValues.put("_is_power_", 0);
                db.insert("letters", null, alphabetValues);

                uiThreadRefresh(myContext, yourParent, true);
                uiThreadBox(joker ? "Prepare blank database" : "Prepare regular database", joker ? "Blank database preparation complete." : "Regular database preparation complete.", myContext, yourParent);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                myDialog.dismiss();
            } finally {
                myDialog.dismiss();
                db.endTransaction();
            }
        });

        thread5.start();
    }

    public ArrayList<Integer> getZoom(String parentActivity)
    {
        ArrayList<Integer> zoomList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _rows_, _columns_, _size_, _spinner_, _loader_ FROM zoom WHERE _activity_ = \"" + parentActivity + "\"", null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int rows = cursor.getInt(0);
                    int dimensions = cursor.getInt(1);
                    int font = cursor.getInt(2);
                    int combo = cursor.getInt(3);
                    int loader = cursor.getInt(4);

                    zoomList.add(rows);
                    zoomList.add(dimensions);
                    zoomList.add(font);
                    zoomList.add(combo);
                    zoomList.add(loader);
                } while (cursor.moveToNext());
            }
        }
        else if (parentActivity.equals("Report")) {
            zoomList.add(100);
            zoomList.add(1);
            zoomList.add(11);
            zoomList.add(20);
            zoomList.add(20);
        }
        else {
            zoomList.add(10);
            zoomList.add(5);
            zoomList.add(11);
            zoomList.add(20);
            zoomList.add(20);
        }

        cursor.close();
        return zoomList;
    }

    public void setZoom(String parentActivity, int rows, int dimensions, int font, int combo, int loader)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM zoom WHERE _activity_ = \"" + parentActivity + "\")", null);

        int exists = 0;

        if (cursor.moveToFirst()) {
            do {
                exists = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ContentValues values = new ContentValues();
        values.put("_rows_", rows);
        values.put("_columns_", dimensions);
        values.put("_size_", font);
        values.put("_spinner_", combo);
        values.put("_loader_", loader);

        if (exists != 0) {
            db.update("zoom", values, "_activity_ = ?",
                    new String[] {parentActivity});
        }
        else {
            values.put("_activity_", parentActivity);
            db.insert("zoom", null, values);
        }
    }

    public void setMagnify(String parentActivity, int rows, int font, int combo, int loader)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM zoom WHERE _activity_ = \"" + parentActivity + "\")", null);

        int exists = 0;

        if (cursor.moveToFirst()) {
            do {
                exists = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ContentValues values = new ContentValues();
        values.put("_rows_", rows);
        values.put("_size_", font);
        values.put("_spinner_", combo);
        values.put("_loader_", loader);

        if (exists != 0) {
            db.update("zoom", values, "_activity_ = ?",
                    new String[] {parentActivity});
        }
        else {
            values.put("_activity_", parentActivity);
            db.insert("zoom", null, values);
        }
    }

    public int insertLabel(int letters, String label, String orderBy, boolean blank)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int filterSerial = getFilterSerial();

        contentValues.put("_length_", letters);
        contentValues.put("_counter_", 0);
        contentValues.put("_page_", 0);
        contentValues.put("_query_", label);
        contentValues.put("_solved_", 0);
        contentValues.put("_unsolved_", 0);
        contentValues.put("_complete_", 0);
        contentValues.put("_incomplete_", 0);
        contentValues.put("_something_", 0);
        contentValues.put("_nothing_", 0);
        contentValues.put("_partial_", 0);
        contentValues.put("_total_", 0);
        contentValues.put("_blank_", blank ? 1 : 0);
        contentValues.put("_sort_", orderBy);
        contentValues.put("_name_", "");
        contentValues.put("_serial_", filterSerial);

        db.insert("filters", null, contentValues);
        return filterSerial;
    }

    public int[] getCustomScore(String customQuery, int solvedStatus, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(_solved_), COUNT(_word_) FROM " + (blank ? "blanks" : "words") + " WHERE " + (blank ? "_anagram_" : "_alphagram_") + " IN (SELECT " + (blank ? "_anagram_" : "_alphagram_") + " FROM " + (blank ? "blanks" : "words") + " WHERE " + customQuery + " GROUP BY " + (blank ? "_anagram_" : "_alphagram_") + solvedCondition(solvedStatus) + ")", null);

        int[] answer = new int[2];

        if (cursor.moveToFirst()) {
            do {
                answer[0] = cursor.getInt(0);
                answer[1] = cursor.getInt(1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return answer;
    }

    public int[] getScore(int letters, String label, int solvedStatus, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(_solved_), COUNT(_word_) FROM " + (blank ? "blanks" : "words") + " WHERE " + (blank ? "_anagram_" : "_alphagram_") + " IN (SELECT " + (blank ? "_anagram_" : "_alphagram_") + " FROM " + (blank ? "blanks" : "words") + ((letters > 1 || !label.equals("*")) ? " WHERE " : "") + (letters > 1 ? "_length_ = " + letters : "") + ((letters > 1 && !label.equals("*")) ? " AND " : "") + (!label.equals("*") ? "_tag_ = \"" + label + "\"" : "") + " GROUP BY " + (blank ? "_anagram_" : "_alphagram_") + solvedCondition(solvedStatus) + ")", null);

        int[] answer = new int[2];

        if (cursor.moveToFirst()) {
            do {
                answer[0] = cursor.getInt(0);
                answer[1] = cursor.getInt(1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return answer;
    }

    public int getCounter(int letters, String label, int solvedStatus, String orderBy, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + quizCondition(solvedStatus) + " FROM filters WHERE _blank_ = " + (blank ? "1" : "0") + " AND _length_ = " + letters + " AND _query_ = \"" + label + "\" AND _sort_ = \"" + orderBy + "\"", null);

        String data = null;

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    data = cursor.getString(0);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return Integer.parseInt(data);
        }
        else {
            cursor.close();
            return 0;
        }
    }

    public String solvedCondition(int solvedStatus)
    {
        switch (solvedStatus)
        {
            case 0: return " HAVING SUM(_solved_) = _answers_";
            case 1: return " HAVING SUM(_solved_) < _answers_";
            case 3: return " HAVING SUM(_solved_) > 0";
            case 4: return " HAVING SUM(_solved_) = 0";
            case 5: return " HAVING (SUM(_solved_) > 0 AND SUM(_solved_) < _answers_)";
            case 6: return " HAVING (SUM(_solved_) = 0 OR SUM(_solved_) = _answers_)";
            default: return "";
        }
    }

    public String quizCondition(int solvedStatus)
    {
        switch (solvedStatus)
        {
            case 0: return "_complete_";
            case 1: return "_incomplete_";
            case 2: return "_counter_";
            case 3: return "_something_";
            case 4: return "_nothing_";
            case 5: return "_partial_";
            case 6: return "_total_";
            default: return "";
        }
    }

    public String reportCondition(int solvedStatus)
    {
        switch (solvedStatus)
        {
            case 0: return "_solved_";
            case 1: return "_unsolved_";
            case 2: return "_page_";
            default: return "";
        }
    }

    public Cursor getAllAnagrams(int letters, String label, int solvedStatus, String orderBy, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        if (orderBy.charAt(0) == ' ')
        {
            return db.rawQuery((blank ? "SELECT _anagram_ FROM blanks" : "SELECT _alphagram_ FROM words") + ((letters > 1 || !label.equals("*")) ? " WHERE " : "") + (letters > 1 ? "_length_ = " + letters : "") + ((letters > 1 && !label.equals("*")) ? " AND " : "") + (!label.equals("*") ? "_tag_ = \"" + label + "\"" : "") + (blank ? " GROUP BY _anagram_" : " GROUP BY _alphagram_") + solvedCondition(solvedStatus) + orderBy, null);
        }
        else
        {
            return db.rawQuery((blank ? "SELECT DISTINCT(_anagram_) FROM blanks" : "SELECT DISTINCT(_alphagram_) FROM words") + ((letters > 1 || !label.equals("*")) ? " WHERE " : "") + (letters > 1 ? "_length_ = " + letters : "") + ((letters > 1 && !label.equals("*")) ? " AND " : "") + (!label.equals("*") ? "_tag_ = \"" + label + "\"" : "") + solvedCondition(solvedStatus), null);
        }
    }

    public Cursor getAllBlankAnagrams(int letters)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _anagram_ FROM blanks WHERE _length_ = " + letters + " GROUP BY _anagram_ ORDER BY _probability_ DESC", null);
    }

    public Cursor getAllRegularAnagrams(int letters)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _alphagram_ FROM words WHERE _length_ = " + letters + " GROUP BY _alphagram_ ORDER BY _probability_ DESC", null);
    }

    public Cursor getCustomQuiz(String customQuery, Context activity, int solvedStatus, String orderBy, boolean blank)
    {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            if (orderBy.charAt(0) == ' ')
            {
                return db.rawQuery("SELECT " + (blank ? "_anagram_" : "_alphagram_") + " FROM " + (blank ? "blanks" : "words") + " WHERE " + customQuery + " GROUP BY " + (blank ? "_anagram_" : "_alphagram_") + solvedCondition(solvedStatus) + orderBy, null);
            }
            else
            {
                return db.rawQuery("SELECT DISTINCT(" + (blank ? "_anagram_" : "_alphagram_") + ") FROM " + (blank ? "blanks" : "words") + " WHERE " + customQuery + solvedCondition(solvedStatus), null);
            }
        }
        catch (SQLiteException e) {
            alertBox("Error", e.toString(), activity);
            return null;
        }
    }

    public Cursor getSolvedWords(int letters, int solvedStatus, String orderBy, boolean blank)
    {
        String status = "";
        if (solvedStatus == 0)
        {
            status = (letters != 1 ? " AND " : "") + "_solved_ = 1";
        }
        else if (solvedStatus == 1)
        {
            status = (letters != 1 ? " AND " : "") + "_solved_ = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery((blank ? "SELECT _identity_ FROM blanks" : "SELECT _word_ FROM words") + (letters != 1 || solvedStatus < 2 ? " WHERE " : "") + (letters != 1 ? "_length_ = " + letters : "") + status + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);
    }

    public Cursor getLabelledWords(int letters, String label, int solvedStatus, String orderBy, boolean blank)
    {
        String status = "";
        if (solvedStatus == 0)
        {
            status = "_solved_ = 1 AND ";
        }
        else if (solvedStatus == 1)
        {
            status = "_solved_ = 0 AND ";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery((blank ? "SELECT _identity_ FROM blanks WHERE " : "SELECT _word_ FROM words WHERE ") + (letters != 1 ? "_length_ = " + letters + " AND " : "") + status + "_tag_ = \"" + label + "\"" + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);
    }

    public Cursor getSqlQuery(String query, Context activity, int solvedStatus, String orderBy, boolean blank)
    {
        String status = "";
        if (solvedStatus == 0)
        {
            status = "_solved_ = 1 AND ";
        }
        else if (solvedStatus == 1)
        {
            status = "_solved_ = 0 AND ";
        }

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery((blank ? "SELECT _identity_ FROM blanks WHERE " : "SELECT _word_ FROM words WHERE ") + status + query + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);
        }
        catch (SQLiteException e) {
            alertBox("Error", e.toString(), activity);
            return null;
        }
    }

    public Pair<HashMap<String, ArrayList<String>>, Integer> getUnsolvedAnswers(ArrayList<String> jumbles, boolean blank)
    {
        HashMap<String, ArrayList<String>> answerList = new HashMap<>();
        String jumble = (((jumbles.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery((blank ? "SELECT _anagram_, _word_ FROM blanks WHERE _anagram_ IN " : "SELECT _alphagram_, _word_ FROM words WHERE _alphagram_ IN ") + jumble + " AND _solved_ = 0", null);
        int solvedAnswers = cursor.getCount();

        if (cursor.moveToFirst()) {
            do {
                String anagram = cursor.getString(0);
                String word = cursor.getString(1);

                if (answerList.containsKey(anagram))
                {
                    (answerList.get(anagram)).add(word);
                }
                else
                {
                    ArrayList<String> answersList = new ArrayList<>();
                    answersList.add(word);
                    answerList.put(anagram, answersList);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return new Pair<>(answerList, solvedAnswers);
    }

    public HashMap<String, Integer> getAllAnswers(ArrayList<String> jumbles, boolean blank)
    {
        HashMap<String, Integer> allList = new HashMap<>();

        String jumble = (((jumbles.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery((blank ? "SELECT _anagram_, COUNT(_word_) FROM blanks WHERE _anagram_ IN " : "SELECT _alphagram_, COUNT(_word_) FROM words WHERE _alphagram_ IN ") + jumble + " GROUP BY " + (blank ? "_anagram_" : "_alphagram_"), null);

        if (cursor.moveToFirst()) {
            do {
                String anagram = cursor.getString(0);
                String word = cursor.getString(1);

                allList.put(anagram, Integer.parseInt(word));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return allList;
    }

    public HashMap<String, String> getColours()
    {
        HashMap<String, String> colourList = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_, _colour_ FROM colours", null);

        if (cursor.moveToFirst()) {
            do {
                String label = cursor.getString(0);
                String colour = cursor.getString(1);

                colourList.put(label, colour);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return colourList;
    }

    public String getSolvedAnswers(String jumble, boolean blank)
    {
        StringBuilder solved = new StringBuilder();
        int total = 1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _word_, _definition_, _back_, _front_, _tag_, _length_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_ FROM " + (blank ? "blanks WHERE _anagram_ = \"" : "words WHERE _alphagram_ = \"") + jumble + "\" AND _solved_ = 1 ORDER BY _time_", null);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(0);
                String definition = cursor.getString(1);
                String back = cursor.getString(2);
                String front = cursor.getString(3);
                String label = cursor.getString(4);
                int length = cursor.getInt(5);
                int csw24 = cursor.getInt(6);
                int csw19 = cursor.getInt(7);
                int csw15 = cursor.getInt(8);
                int csw12 = cursor.getInt(9);
                int csw07 = cursor.getInt(10);
                int nwl23 = cursor.getInt(11);
                int nwl18 = cursor.getInt(12);
                int twl06 = cursor.getInt(13);
                int nswl23 = cursor.getInt(14);
                int cel21 = cursor.getInt(15);
                int wims = cursor.getInt(16);

                ArrayList<String> dictionaryList = dictionaries(length, csw24, csw19, csw15, csw12, csw07, nwl23, nwl18, twl06, nswl23, wims, cel21);
                HashMap<String, String> colours = getColours();

                if (colours.containsKey(label) || colours.containsKey("")) {
                    String colour = (colours.containsKey(label) ? colours.get(label) : colours.get(""));

                    if (total == 1) {
                        solved.append("<font color=\"").append(colour).append("\">").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.isEmpty() ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b></font>");
                    } else {
                        solved.append("<br><font color=\"").append(colour).append("\">").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.isEmpty() ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b></font>");
                    }
                } else {
                    if (total == 1) {
                        solved.append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.isEmpty() ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b>");
                    } else {
                        solved.append("<br>").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.isEmpty() ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b>");
                    }
                }

                total++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return new String(solved);
    }

    public String getUnsolvedWords(String unsolved, boolean blank)
    {
        StringBuilder unsolvedAnswers = new StringBuilder();
        int total = 1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _word_, _definition_, _back_, _front_, _length_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_ FROM " + (blank ? "blanks WHERE _anagram_ = \"" : "words WHERE _alphagram_ = \"") + unsolved + "\" AND _solved_ = 0", null);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(0);
                String definition = cursor.getString(1);
                String back = cursor.getString(2);
                String front = cursor.getString(3);
                int length = cursor.getInt(4);
                int csw24 = cursor.getInt(5);
                int csw19 = cursor.getInt(6);
                int csw15 = cursor.getInt(7);
                int csw12 = cursor.getInt(8);
                int csw07 = cursor.getInt(9);
                int nwl23 = cursor.getInt(10);
                int nwl18 = cursor.getInt(11);
                int twl06 = cursor.getInt(12);
                int nswl23 = cursor.getInt(13);
                int cel21 = cursor.getInt(14);
                int wims = cursor.getInt(15);

                ArrayList<String> dictionaryList = dictionaries(length, csw24, csw19, csw15, csw12, csw07, nwl23, nwl18, twl06, nswl23, wims, cel21);

                if (total == 1) {
                    unsolvedAnswers.append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b>");
                }
                else {
                    unsolvedAnswers.append("<br>").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b>");
                }

                total++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return new String(unsolvedAnswers);
    }

    public ArrayList<String> getDefinition(String guess, boolean blank)
    {
        ArrayList<String> hookList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _definition_, _back_, _front_, _length_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_, _tag_ FROM " + (blank ? "blanks WHERE _identity_ = \"" : "words WHERE _word_ = \"") + guess + "\"", null);

        String meaning = null;
        String back = null;
        String front = null;
        String lexicons = null;
        String label = null;

        if (cursor.moveToFirst()) {
            do {
                meaning = cursor.getString(0);
                back = cursor.getString(1);
                front = cursor.getString(2);
                int length = cursor.getInt(3);
                int csw24 = cursor.getInt(4);
                int csw19 = cursor.getInt(5);
                int csw15 = cursor.getInt(6);
                int csw12 = cursor.getInt(7);
                int csw07 = cursor.getInt(8);
                int nwl23 = cursor.getInt(9);
                int nwl18 = cursor.getInt(10);
                int twl06 = cursor.getInt(11);
                int nswl23 = cursor.getInt(12);
                int cel21 = cursor.getInt(13);
                int wims = cursor.getInt(14);
                label = cursor.getString(15);

                ArrayList<String> dictionaryList = dictionaries(length, csw24, csw19, csw15, csw12, csw07, nwl23, nwl18, twl06, nswl23, wims, cel21);
                lexicons = dictionaryList.get(0) + " " + dictionaryList.get(1) + " " + dictionaryList.get(2) + " " + dictionaryList.get(3);
            } while (cursor.moveToNext());
        }

        cursor.close();

        hookList.add(meaning);
        hookList.add(back);
        hookList.add(front);
        hookList.add(lexicons);
        hookList.add(label);

        return hookList;
    }

    public int getPage(int letters, String label, int solvedStatus, String orderBy, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + reportCondition(solvedStatus) + " FROM filters WHERE _blank_ = " + (blank ? "1" : "0") + " AND _length_ = " + letters + " AND _query_ = \"" + label + "\" AND _sort_ = \"" + orderBy + "\"", null);

        String data = null;

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    data = cursor.getString(0);
                } while (cursor.moveToNext());
            }

            cursor.close();
            return Integer.parseInt(data);
        }
        else
        {
            cursor.close();
            return 0;
        }
    }

    public void resetLabel(String label, int lengthIndex, int letters, boolean timeIndex, boolean blank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_solved_", 0);
        if (timeIndex)
        {
            values.put("_time_", 0);
        }

        StringBuilder whereClause = new StringBuilder();
        if (!label.equals("*"))
        {
            whereClause.append("_tag_ = \"").append(label).append("\"");
        }
        if (!label.equals("*") && lengthIndex == 0)
        {
            whereClause.append(" AND ");
        }
        if (lengthIndex == 0)
        {
            whereClause.append("_length_ = ").append(letters);
        }

        db.update(blank ? "blanks" : "words", values, new String(whereClause),
                new String[] {});
    }

    public void updateCounter(int letters, String label, int counter, int solvedStatus, String orderBy, boolean blank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(quizCondition(solvedStatus), counter);

        db.update("filters", values, "_length_ = ? AND _query_ = ? AND _blank_ = ? AND _sort_ = ?",
                new String[] {Integer.toString(letters), label, blank ? "1" : "0", orderBy});
    }

    public void updatePage(int letters, String label, int counter, int solvedStatus, String orderBy, boolean blank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(reportCondition(solvedStatus), counter);

        db.update("filters", values, "_length_ = ? AND _query_ = ? AND _blank_ = ? AND _sort_ = ?",
                new String[] {Integer.toString(letters), label, blank ? "1" : "0", orderBy});
    }

    public void updateTime(HashSet<String> guesses, double time, boolean submitted, String cardbox, boolean blank) {
        SQLiteDatabase db = this.getWritableDatabase();
        String guess = (((guesses.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        StringBuilder updates = new StringBuilder();

        if (submitted) {
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat iso8601Format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String simpleDateFormat = iso8601Format.format(timestamp);
            updates.append("_solved_ = 1, _timestamp_ = \"").append(simpleDateFormat).append("\", ");
        }

        if (cardbox != null) {
            updates.append("_tag_ = \"").append(cardbox).append("\", ");
        }

        db.execSQL("UPDATE " + (blank ? "blanks" : "words") + " SET " + new String(updates) + "_time_ = _time_ + " + String.format("%.3f", time) + (blank ? " WHERE _identity_ IN " : " WHERE _word_ IN ") + guess);
    }

    public void updateTag(String guess, String tag, boolean blank)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_tag_", tag);

        db.update(blank ? "blanks" : "words", values, blank ? "_identity_ = ?" : "_word_ = ?",
                new String[] {guess});
    }

    public int existLabel(int letters, String label, String orderBy, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _serial_ FROM filters WHERE _blank_ = " + (blank ? "1" : "0") + " AND _length_ = " + letters + " AND _query_ = \"" + label + "\" AND _sort_ = \"" + orderBy + "\"", null);

        int exists = 0;

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    exists = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return exists;
    }

    public String getSummary(ArrayList<String> guesses, boolean blank)
    {
        String guess = (((guesses.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _front_, _word_, _back_, _tag_ FROM " + (blank ? "blanks WHERE _anagram_ IN " : "words WHERE _alphagram_ IN ") + guess + " AND _solved_ = 1", null);

        HashMap<String, ArrayList<String>> h = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                String front = cursor.getString(0);
                String word = cursor.getString(1);
                String back = cursor.getString(2);
                String label = cursor.getString(3);

                String data = "<small>" + front + "</small> " + word + " <small>" + back + "</small>";

                if (h.containsKey(label)) {
                    (h.get(label)).add(data);
                }
                else {
                    h.put(label, new ArrayList<>());
                    (h.get(label)).add(data);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        StringBuilder revision = new StringBuilder();
        int serial = 0;

        ArrayList<String> keyList = new ArrayList<>(h.keySet());
        Collections.sort(keyList);

        for (String key : keyList)
        {
            String value = (h.get(key)).toString();
            int l = value.length();
            String aerolith = value.substring(1, l - 1);

            HashMap<String, String> colours = getColours();

            if (colours.containsKey(key) || colours.containsKey("")) {
                String colour = (colours.containsKey(key) ? colours.get(key) : colours.get(""));

                if (serial == 0) {
                    revision.append("<font color=\"").append(colour).append("\"><b>").append(key.isEmpty() ? "(No Tag)" : key).append(": ").append(aerolith).append("</b></font>");
                } else {
                    revision.append("<br><font color=\"").append(colour).append("\"><b>").append(key.isEmpty() ? "(No Tag)" : key).append(": ").append(aerolith).append("</b></font>");
                }
            } else {
                if (serial == 0) {
                    revision.append("<b>").append(key.isEmpty() ? "(No Tag)" : key).append(": ").append(aerolith).append("</b>");
                } else {
                    revision.append("<br><b>").append(key.isEmpty() ? "(No Tag)" : key).append(": ").append(aerolith).append("</b>");
                }
            }

            serial++;
        }

        return new String(revision);
    }

    public void alertBox(String title, String message, Context location)
    {
        LayoutInflater inflater = LayoutInflater.from(location);
        final View yourCustomView = inflater.inflate(R.layout.display, null);

        TextView t1 = yourCustomView.findViewById(R.id.textview13);
        t1.setText(message);

        AlertDialog dialog = new AlertDialog.Builder(location)
                .setTitle(title)
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        dialog.show();
    }

    public void uiThreadBox(String title, String message, Context location, boolean parent)
    {
        if (parent)
        {
            MainActivity homeActivity = (MainActivity) location;

            homeActivity.runOnUiThread(() -> {
                LayoutInflater inflater = LayoutInflater.from(location);
                final View yourCustomView = inflater.inflate(R.layout.display, null);

                TextView t1 = yourCustomView.findViewById(R.id.textview13);
                t1.setText(message);

                AlertDialog dialog = new AlertDialog.Builder(location)
                        .setTitle(title)
                        .setView(yourCustomView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).create();
                dialog.show();
            });
        }
        else
        {
            Report homeActivity = (Report) location;

            homeActivity.runOnUiThread(() -> {
                LayoutInflater inflater = LayoutInflater.from(location);
                final View yourCustomView = inflater.inflate(R.layout.display, null);

                TextView t1 = yourCustomView.findViewById(R.id.textview13);
                t1.setText(message);

                AlertDialog dialog = new AlertDialog.Builder(location)
                        .setTitle(title)
                        .setView(yourCustomView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).create();
                dialog.show();
            });
        }
    }

    public void messageBox(String title, String message, Context location)
    {
        LayoutInflater inflater = LayoutInflater.from(location);
        final View yourCustomView = inflater.inflate(R.layout.display, null);

        TextView t2 = yourCustomView.findViewById(R.id.textview13);
        t2.setText(Html.fromHtml(message));

        AlertDialog dialog = new AlertDialog.Builder(location)
                .setTitle(title)
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                }).create();
        dialog.show();
    }

    public void uiThreadTitle(String title, AlertDialog theDialog, Context location, boolean parent)
    {
        if (parent)
        {
            MainActivity homeActivity = (MainActivity) location;

            homeActivity.runOnUiThread(() -> theDialog.setTitle(title));
        }
        else
        {
            Report homeActivity = (Report) location;

            homeActivity.runOnUiThread(() -> theDialog.setTitle(title));
        }
    }

    public ArrayList<String> dictionaries(int wordSize, int csw24, int csw19, int csw15, int csw12, int csw07, int nwl23, int nwl18, int twl06, int nswl23, int wims, int cel21)
    {
        ArrayList<String> dictionariesList = new ArrayList<>();

        if (wordSize <= 15) {
            dictionariesList.add(csw24 == 0 ? "$" : (csw07 == 1 ? "CSW07" : (csw12 == 1 ? "CSW12" : (csw15 == 1 ? "CSW15" : (csw19 == 1 ? "CSW19" : "CSW24")))));
            dictionariesList.add(nwl23 == 0 ? "#" : (nswl23 == 0 ? "!" : (twl06 == 1 ? "TWL06" : (nwl18 == 1 ? "NWL18" : "NWL23"))));
        }
        else {
            switch (csw24) {
                case 0: dictionariesList.add("$");
                    break;
                case 1: dictionariesList.add("Fj00 CLSW");
                    break;
                case 2: dictionariesList.add("Grubbcc CLSW");
                    break;
                case 3: dictionariesList.add("Both CLSW");
                    break;
                default: dictionariesList.add("");
            }

            switch (nwl23) {
                case 0: dictionariesList.add("#");
                    break;
                case 1: dictionariesList.add("Fj00 NLWL");
                    break;
                case 2: dictionariesList.add("Grubbcc NLWL");
                    break;
                case 3: dictionariesList.add("Both NLWL");
                    break;
                default: dictionariesList.add("");
            }
        }

        dictionariesList.add(cel21 == 1 ? "CEL" : "");
        dictionariesList.add(wims == 1 ? "WIMS" : "");

        return dictionariesList;
    }

    public String addUnderscores(String argumentQuery)
    {
        String firstQuery = " " + argumentQuery + " ";
        String[] secondQuery = firstQuery.split("'");

        ArrayList<String> tablesList = getTableNames();
        ArrayList<String> regexList = getTableNames();
        HashSet<String> done = new HashSet<>();
        for (String tableName : tablesList)
        {
            String[] columnList = getAllColumns(tableName);
            for (String attribute : columnList)
            {
                if (!done.contains(attribute)) {
                    argumentQuery = argumentQuery.replaceAll(attribute.substring(1, attribute.length() - 1), attribute);
                    for (int regex = 0; regex < regexList.size(); regex++) {
                        regexList.set(regex, (regexList.get(regex)).replaceAll(attribute.substring(1, attribute.length() - 1), attribute));
                    }
                    done.add(attribute);
                }
            }
        }
        for (int regexLists = 0; regexLists < regexList.size(); regexLists++)
        {
            argumentQuery = argumentQuery.replaceAll(regexList.get(regexLists), tablesList.get(regexLists));
        }

        argumentQuery = (((argumentQuery.replace("_time_stamp", "_timestamp_"))
                .replace("un_solved_", "_unsolved_"))
                .replace("in_complete_", "_incomplete_"))
                .replace("ze_tag_ram", "_zetagram_")
                .replace("is__power_", "_is_power_");

        for (String tableName : tablesList)
        {
            String[] columnList = getAllColumns(tableName);
            for (String attribute : columnList)
            {
                argumentQuery = argumentQuery.replaceAll("_+" + attribute.substring(1, attribute.length() - 1) + "_+", attribute);
            }
        }

        String thirdQuery = " " + argumentQuery + " ";
        String[] lastQuery = thirdQuery.split("'");
        ArrayList<String> finalQuery = new ArrayList<>();
        for (int columnsArray = 0; columnsArray < lastQuery.length; columnsArray++)
        {
            finalQuery.add(columnsArray % 2 == 0 ? lastQuery[columnsArray] : secondQuery[columnsArray]);
        }
        StringBuilder ultimateQuery = new StringBuilder();
        ultimateQuery.append(finalQuery.get(0));
        for (int rowName = 1; rowName < finalQuery.size(); rowName++)
        {
            ultimateQuery.append("'").append(finalQuery.get(rowName));
        }

        return (new String(ultimateQuery)).trim();
    }

    public ArrayList<String> extract(String jumbleList, int start, String orderBy, boolean blank, boolean title)
    {
        ArrayList<String> wordList = new ArrayList<>();

        if (title) {
            wordList.add("<b>No.</b>");
            wordList.add("<b>Front</b>");
            wordList.add("<b>Word</b>");
            wordList.add("<b>Back</b>");
            wordList.add("<b>Ans</b>");
            wordList.add("<b>Anagram</b>");
            wordList.add("<b>Page</b>");
            wordList.add("<b>Tag</b>");
            wordList.add("<b>Time</b>");
            wordList.add("<b>CSW</b>");
            wordList.add("<b>NWL</b>");
            wordList.add("<b>CEL</b>");
            wordList.add("<b>WIMS</b>");
            wordList.add("<b>Solved</b>");
            wordList.add("<b>Timestamp</b>");
            wordList.add("<b>Definition</b>");
            wordList.add("<b>✗</b>");
            wordList.add("<b>Wrong</b>");
        }

        int order = start;
        HashMap<String, String> coloursMap = getColours();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _word_, _definition_, _time_, _solved_, _back_, _front_, _answers_, _page_, " + (blank ? "_anagram_" : "_alphagram_") + ", _timestamp_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_, _incorrect_, _wrong_, _tag_ FROM " + (blank ? "blanks WHERE _identity_ IN " : "words WHERE _word_ IN ") + jumbleList + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);

        if (orderBy.equals("DESC") ? cursor.moveToLast() : cursor.moveToFirst()) {
            do {
                String data = cursor.getString(0);
                String definition = cursor.getString(1);
                String time = cursor.getString(2);
                int solved = cursor.getInt(3);
                String back = cursor.getString(4);
                String front = cursor.getString(5);
                String answers = cursor.getString(6);
                String page = cursor.getString(7);
                String alphagram = cursor.getString(8);
                String timestamp = cursor.getString(9);
                int csw24 = cursor.getInt(10);
                int csw19 = cursor.getInt(11);
                int csw15 = cursor.getInt(12);
                int csw12 = cursor.getInt(13);
                int csw07 = cursor.getInt(14);
                int nwl23 = cursor.getInt(15);
                int nwl18 = cursor.getInt(16);
                int twl06 = cursor.getInt(17);
                int nswl23 = cursor.getInt(18);
                int wims = cursor.getInt(19);
                int cel21 = cursor.getInt(20);
                String incorrect = cursor.getString(21);
                String wrong = cursor.getString(22);
                String label = cursor.getString(23);

                ArrayList<String> lexicons = dictionaries(data.length(), csw24, csw19, csw15, csw12, csw07, nwl23, nwl18, twl06, nswl23, wims, cel21);

                if (coloursMap.containsKey(label) || coloursMap.containsKey("")) {
                    String colourMap = (coloursMap.containsKey(label) ? coloursMap.get(label) : coloursMap.get(""));

                    wordList.add("<font color=\"" + colourMap + "\">" + order + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b><small>" + front + "</small></b></font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b>" + data + "</b></font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b><small>" + back + "</small></b></font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + answers + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b>" + alphagram + "</b></font>");
                    wordList.add("<font color=\"" + colourMap + "\">Page " + page + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b>" + (label.isEmpty() ? "(No Tag)" : label) + "</b></font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + convert(time) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(0) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(1) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(2) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(3) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + ((solved == 0) ? "Unsolved" : "Solved") + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + timestamp + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + definition + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + incorrect + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + wrong + "</font>");
                }
                else {
                    wordList.add(Integer.toString(order));
                    wordList.add("<b><small>" + front + "</small></b>");
                    wordList.add("<b>" + data + "</b>");
                    wordList.add("<b><small>" + back + "</small></b>");
                    wordList.add(answers);
                    wordList.add("<b>" + alphagram + "</b>");
                    wordList.add("Page " + page);
                    wordList.add("<b>" + (label.isEmpty() ? "(No Tag)" : label) + "</b>");
                    wordList.add(convert(time));
                    wordList.add(lexicons.get(0));
                    wordList.add(lexicons.get(1));
                    wordList.add(lexicons.get(2));
                    wordList.add(lexicons.get(3));
                    wordList.add((solved == 0) ? "Unsolved" : "Solved");
                    wordList.add(timestamp);
                    wordList.add(definition);
                    wordList.add(incorrect);
                    wordList.add(wrong);
                }

                order++;
            } while (orderBy.equals("DESC") ? cursor.moveToPrevious() : cursor.moveToNext());
        }

        cursor.close();
        return wordList;
    }

    public void trackWrongAnswers(ArrayList<String> wrongAlphagram, String wrongWord, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String incorrectAlphagram = (((wrongAlphagram.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        Cursor cursor = db.rawQuery("SELECT _wrong_, " + (blank ? "_identity_ FROM blanks WHERE _anagram_ IN " : "_alphagram_ FROM words WHERE _alphagram_ IN ") + incorrectAlphagram, null);

        ArrayList<String> noWrongAnswers = new ArrayList<>();
        ArrayList<String> someWrongAnswers = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String first = cursor.getString(0);
                String second = cursor.getString(1);

                if (first.isEmpty()) {
                    noWrongAnswers.add(second);
                }
                else {
                    List<String> third = Arrays.asList(first.split(", "));
                    if (!third.contains(wrongWord)) {
                        someWrongAnswers.add(second);
                    }
                }
            } while (cursor.moveToNext());
        }

        String anagramMap = (((noWrongAnswers.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        String anagramsMap = (((someWrongAnswers.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        db.execSQL("UPDATE " + (blank ? "blanks" : "words") + " SET _incorrect_ = _incorrect_ + 1, _wrong_ = _wrong_ || \"" + wrongWord + "\" WHERE " + (blank ? "_identity_" : "_alphagram_") + " IN " + anagramMap);
        db.execSQL("UPDATE " + (blank ? "blanks" : "words") + " SET _incorrect_ = _incorrect_ + 1, _wrong_ = _wrong_ || \", " + wrongWord + "\" WHERE " + (blank ? "_identity_" : "_alphagram_") + " IN " + anagramsMap);

        cursor.close();
    }

    public void refresh(Context theContext, boolean parent)
    {
        if (parent)
        {
            MainActivity mainActivity = (MainActivity) theContext;
            mainActivity.refresh();
        }
        else
        {
            Report report = (Report) theContext;
            report.refresh();
        }
    }

    public void uiThreadRefresh(Context theContext, boolean parent, boolean prepared)
    {
        if (parent)
        {
            MainActivity mainActivity = (MainActivity) theContext;

            mainActivity.runOnUiThread(() -> {
                mainActivity.refresh();
                if (prepared) {
                    mainActivity.setPrepared();
                }
            });
        }
        else
        {
            Report report = (Report) theContext;

            report.runOnUiThread(() -> {
                report.refresh();
                if (prepared) {
                    report.setPrepared();
                }
            });
        }
    }

    public void resetByLabel(Context theContext, boolean parent, ArrayList<String> blankList, int maximumWordLength, int maximumBlankLength, int fontSize)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.reset, null);

        EditText e4 = yourCustomView.findViewById(R.id.edittext21);
        EditText e5 = yourCustomView.findViewById(R.id.edittext22);
        TextView t5 = yourCustomView.findViewById(R.id.textview36);

        e5.setHint("Enter a value between 2 and " + maximumWordLength);

        Spinner s12 = yourCustomView.findViewById(R.id.spinner38);
        ArrayAdapter<String> blankAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, blankList);
        blankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s12.setAdapter(blankAdapter);

        Spinner s1 = yourCustomView.findViewById(R.id.spinner11);
        List<Pair<String, String>> tagsList = getAllLabels();
        tagsList.add(0, new Pair<>("(All Tags)", null));

        s12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                e5.setHint("Enter a value between 2 and " + ((i == 0) ? maximumWordLength : maximumBlankLength));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ColourAdapter spinnerAdapter = new ColourAdapter(theContext, R.layout.colour, R.id.textview62, tagsList, parent ? (MainActivity) theContext : (Report) theContext, true, fontSize);
        s1.setAdapter(spinnerAdapter);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e4.setText("*");
                }
                else {
                    e4.setText((tagsList.get(i)).first);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final int[] lengthIndex = new int[1];
        Spinner s2 = yourCustomView.findViewById(R.id.spinner12);
        ArrayList<String> lengthList = new ArrayList<>();
        lengthList.add(0, "Specific word length");
        lengthList.add(1, "All word lengths");

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, lengthList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(lengthAdapter);

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e5.setVisibility(View.VISIBLE);
                    t5.setVisibility(View.VISIBLE);
                    lengthIndex[0] = 0;
                }
                else {
                    e5.setVisibility(View.INVISIBLE);
                    t5.setVisibility(View.INVISIBLE);
                    lengthIndex[0] = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final boolean[] timeIndex = new boolean[1];
        Spinner s3 = yourCustomView.findViewById(R.id.spinner13);
        ArrayList<String> timeList = new ArrayList<>();
        timeList.add(0, "Do not reset time");
        timeList.add(1, "Reset time");

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, timeList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s3.setAdapter(timeAdapter);

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    timeIndex[0] = false;
                }
                else {
                    timeIndex[0] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle("Reset words by tag")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String myLabel = (e4.getText()).toString();
                    String alphabets = (e5.getText()).toString();
                    int temporary = (alphabets.isEmpty() ? 0 : Integer.parseInt(alphabets));
                    resetLabel(myLabel, lengthIndex[0], temporary, timeIndex[0], s12.getSelectedItemPosition() > 0);
                    refresh(theContext, parent);
                }).create();
        dialog.show();
    }

    public boolean addLabel(String label, String hexCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("_tag_", label);
        contentValues.put("_colour_", hexCode);

        db.insert("colours", null, contentValues);
        return true;
    }

    public int renameLabel(String oldCode, String label, String colour, boolean name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_tag_", label);
        values.put("_colour_", colour);

        return db.update("colours", values, name ? "_tag_ = ?" : "_colour_ = ?",
                new String[] {oldCode});
    }

    public int deleteLabel(String oldCode, boolean name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("colours", name ? "_tag_ = ?" : "_colour_ = ?",
                new String[] {oldCode});
    }

    public void addByLabel(Context theContext, boolean parent)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.add, null);

        EditText e6 = yourCustomView.findViewById(R.id.edittext23);
        TextView t6 = yourCustomView.findViewById(R.id.textview39);
        TextView t7 = yourCustomView.findViewById(R.id.textview41);
        TextView t8 = yourCustomView.findViewById(R.id.textview43);
        TextView t9 = yourCustomView.findViewById(R.id.textview45);
        TextView t10 = yourCustomView.findViewById(R.id.textview47);
        Slider z1 = yourCustomView.findViewById(R.id.slider1);
        Slider z2 = yourCustomView.findViewById(R.id.slider2);
        Slider z3 = yourCustomView.findViewById(R.id.slider3);

        final int[] rgb = {(int) z1.getValue(), (int) z2.getValue(), (int) z3.getValue()};
        t7.setText(Integer.toString(rgb[0]));
        t8.setText(Integer.toString(rgb[1]));
        t9.setText(Integer.toString(rgb[2]));
        final String[] hexValue = {String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2])};
        t6.setText(hexValue[0]);
        t6.setTextColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));

        int nightModeFlags =
                theContext.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        String white = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#000000" : "#FFFFFF");
        int grey = t7.getCurrentTextColor();

        z1.addOnChangeListener((slider, value, fromUser) -> {
            rgb[0] = (int) value;
            t7.setText(Integer.toString(rgb[0]));
            hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
            t6.setText(hexValue[0]);
            t6.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
            t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        });

        z2.addOnChangeListener((slider, value, fromUser) -> {
            rgb[1] = (int) value;
            t8.setText(Integer.toString(rgb[1]));
            hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
            t6.setText(hexValue[0]);
            t6.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
            t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        });

        z3.addOnChangeListener((slider, value, fromUser) -> {
            rgb[2] = (int) value;
            t9.setText(Integer.toString(rgb[2]));
            hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
            t6.setText(hexValue[0]);
            t6.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
            t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle("Add new tag")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    addLabel((e6.getText()).toString(), hexValue[0]);
                    refresh(theContext, parent);
                }).create();
        dialog.show();
    }

    public void renameByLabel(Context theContext, boolean parent, boolean name, int fontSize)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.rename, null);

        EditText e7 = yourCustomView.findViewById(R.id.edittext24);
        Spinner s4 = yourCustomView.findViewById(R.id.spinner14);
        TextView t11 = yourCustomView.findViewById(R.id.textview48);
        TextView t12 = yourCustomView.findViewById(R.id.textview49);
        TextView t13 = yourCustomView.findViewById(R.id.textview50);
        TextView t14 = yourCustomView.findViewById(R.id.textview53);
        TextView t15 = yourCustomView.findViewById(R.id.textview55);
        TextView t16 = yourCustomView.findViewById(R.id.textview57);
        TextView t17 = yourCustomView.findViewById(R.id.textview59);
        TextView t18 = yourCustomView.findViewById(R.id.textview61);
        Slider z4 = yourCustomView.findViewById(R.id.slider4);
        Slider z5 = yourCustomView.findViewById(R.id.slider5);
        Slider z6 = yourCustomView.findViewById(R.id.slider6);

        t11.setText(name ? "Old Name:" : "Old Colour:");
        t12.setText(name ? "Old Colour:" : "Old Name:");
        final String[] hexValue = new String[1];
        final String[] old = new String[1];
        final int[] rgb = new int[3];

        t14.setText(String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]));
        t15.setText(Integer.toString(rgb[0]));
        t16.setText(Integer.toString(rgb[1]));
        t17.setText(Integer.toString(rgb[2]));
        t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));

        int nightModeFlags =
                theContext.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        String white = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#000000" : "#FFFFFF");
        int grey = t11.getCurrentTextColor();

        List<Pair<String, String>> spinnerList = (name ? getAllLabels() : getAllColours());
        ColourAdapter colourAdapter = new ColourAdapter(theContext, R.layout.colour, R.id.textview62, spinnerList, parent ? (MainActivity) theContext : (Report) theContext, name, fontSize);
        s4.setAdapter(colourAdapter);

        s4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String oldValue = (spinnerList.get(i)).first;
                e7.setText(oldValue);
                hexValue[0] = (spinnerList.get(i)).second;
                int hexColour = Integer.parseInt(hexValue[0].substring(1), 16);
                rgb[0] = (hexColour >> 16) & 255;
                rgb[1] = (hexColour >> 8) & 255;
                rgb[2] = hexColour & 255;
                t13.setText(name ? hexValue[0] : oldValue);
                t13.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                z4.setValue(rgb[0]);
                z5.setValue(rgb[1]);
                z6.setValue(rgb[2]);
                old[0] = (name ? oldValue : hexValue[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        z4.addOnChangeListener((slider, value, fromUser) -> {
            rgb[0] = (int) value;
            t15.setText(Integer.toString(rgb[0]));
            hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
            t14.setText(hexValue[0]);
            t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
            t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        });

        z5.addOnChangeListener((slider, value, fromUser) -> {
            rgb[1] = (int) value;
            t16.setText(Integer.toString(rgb[1]));
            hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
            t14.setText(hexValue[0]);
            t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
            t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        });

        z6.addOnChangeListener((slider, value, fromUser) -> {
            rgb[2] = (int) value;
            t17.setText(Integer.toString(rgb[2]));
            hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
            t14.setText(hexValue[0]);
            t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
            t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(name ? "Change tag colour by name" : "Rename tag by colour")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    int result = renameLabel(old[0], (e7.getText()).toString(), hexValue[0], name);
                    refresh(theContext, parent);
                }).create();
        dialog.show();
    }

    public void deleteByLabel(Context theContext, boolean parent, boolean name, int fontSize)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.delete, null);

        Spinner s5 = yourCustomView.findViewById(R.id.spinner15);
        TextView t19 = yourCustomView.findViewById(R.id.textview63);
        TextView t20 = yourCustomView.findViewById(R.id.textview64);
        TextView t21 = yourCustomView.findViewById(R.id.textview65);

        t19.setText(name ? "Tag:" : "Colour:");
        t20.setText(name ? "Colour:" : "Tag:");
        final String[] old = new String[1];

        int nightModeFlags =
                theContext.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        String white = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#000000" : "#FFFFFF");
        int grey = t19.getCurrentTextColor();

        List<Pair<String, String>> spinnerList = (name ? getAllLabels() : getAllColours());
        ColourAdapter colourAdapter = new ColourAdapter(theContext, R.layout.colour, R.id.textview62, spinnerList, parent ? (MainActivity) theContext : (Report) theContext, name, fontSize);
        s5.setAdapter(colourAdapter);

        s5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String oldValue = (spinnerList.get(i)).first;
                String hexNumber = (spinnerList.get(i)).second;
                t21.setText(name ? hexNumber : oldValue);
                t21.setTextColor(hexNumber.equals(white) ? grey : Color.parseColor(hexNumber));
                old[0] = (name ? oldValue : hexNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(name ? "Delete tag by name" : "Delete tag by colour")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    int result = deleteLabel(old[0], name);
                    refresh(theContext, parent);
                }).create();
        dialog.show();
    }

    public List<Pair<String, String>> getAllPrefixes()
    {
        ArrayList<Pair<String, String>> prefixesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _prefix_, _before_ FROM prefixes ORDER BY _prefix_", null);

        if (cursor.moveToFirst()) {
            do {
                prefixesList.add(new Pair<>(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return prefixesList;
    }

    public List<Pair<String, String>> getAllSuffixes()
    {
        ArrayList<Pair<String, String>> suffixesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _suffix_, _after_ FROM suffixes ORDER BY _suffix_", null);

        if (cursor.moveToFirst()) {
            do {
                suffixesList.add(new Pair<>(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return suffixesList;
    }

    public void getSuffix(Context theContext)
    {
        ArrayList<String> prefixList = new ArrayList<>();
        ArrayList<String> suffixList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT _prefix_, _before_ FROM prefixes ORDER BY _prefix_", null);

        if (cursor1.moveToFirst()) {
            do {
                String thePrefix = cursor1.getString(0);
                String theBefore = cursor1.getString(1);
                prefixList.add((thePrefix.isEmpty() ? "(None)" : cursor1.getString(0)) + " before " + (theBefore.isEmpty() ? "(All)" : String.join(", ", Arrays.asList((cursor1.getString(1)).split("\\s*")))));
            } while (cursor1.moveToNext());
        }

        cursor1.close();

        Cursor cursor2 = db.rawQuery("SELECT _suffix_, _after_ FROM suffixes ORDER BY _suffix_", null);

        if (cursor2.moveToFirst()) {
            do {
                String theSuffix = cursor2.getString(0);
                String theAfter = cursor2.getString(1);
                suffixList.add((theSuffix.isEmpty() ? "(None)" : cursor2.getString(0)) + " after " + (theAfter.isEmpty() ? "(All)" : String.join(", ", Arrays.asList((cursor2.getString(1)).split("\\s*")))));
            } while (cursor2.moveToNext());
        }

        cursor2.close();

        String prefixes = String.join("<br>", prefixList);
        String suffixes = String.join("<br>", suffixList);

        messageBox("View all prefixes and suffixes", "<b>Prefixes:</b><br>" + prefixes + "<br><br><b>Suffixes:</b><br>" + suffixes, theContext);
    }

    public void addSuffix(Context theContext, boolean parent, boolean suffix, int mode)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.modify, null);

        TextView t22 = yourCustomView.findViewById(R.id.textview66);
        TextView t23 = yourCustomView.findViewById(R.id.textview67);
        Spinner s6 = yourCustomView.findViewById(R.id.spinner16);
        Spinner s7 = yourCustomView.findViewById(R.id.spinner17);
        EditText e8 = yourCustomView.findViewById(R.id.edittext25);
        EditText e9 = yourCustomView.findViewById(R.id.edittext26);

        ArrayList<String> beforeList = new ArrayList<>();
        ArrayList<String> afterList = new ArrayList<>();
        final int variable[] = {0, 0};

        if (suffix)
        {
            t22.setText("Suffix:");
            t23.setText("After last letters:");
            beforeList.add("No changes to word");
            beforeList.add("Drop last letter");
            beforeList.add("Double last letter");
            afterList.add("After all last letters");
            afterList.add("After specific last letters");
            e9.setHint("(After all last letters)");
        }
        else
        {
            t22.setText("Prefix:");
            t23.setText("Before first letters:");
            beforeList.add("No changes to word");
            beforeList.add("Drop first letter");
            beforeList.add("Double first letter");
            afterList.add("Before all first letters");
            afterList.add("Before specific first letters");
            e9.setHint("(Before all first letters)");
        }

        ArrayAdapter<String> beforeAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, beforeList);
        beforeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s6.setAdapter(beforeAdapter);

        s6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> afterAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, afterList);
        afterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s7.setAdapter(afterAdapter);

        s7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[1] = i;
                if (i == 0)
                {
                    t23.setVisibility(View.INVISIBLE);
                    e9.setVisibility(View.INVISIBLE);
                }
                else
                {
                    t23.setVisibility(View.VISIBLE);
                    e9.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(suffix ? "Add new suffix" : "Add new prefix")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    if (suffix)
                    {
                        addPrefix((variable[0] == 0 ? "" : (variable[0] == 1 ? "-" : "+")) + ((e8.getText()).toString()).toUpperCase(), variable[1] == 0 ? "" : ((e9.getText()).toString()).toUpperCase(), suffix);
                    }
                    else
                    {
                        addPrefix(((e8.getText()).toString()).toUpperCase() + (variable[0] == 0 ? "" : (variable[0] == 1 ? "-" : "+")), variable[1] == 0 ? "" : ((e9.getText()).toString()).toUpperCase(), suffix);
                    }

                    if (parent && mode == 1) {
                        MainActivity home = (MainActivity) theContext;
                        home.refreshDefinition();
                    }
                }).create();
        dialog.show();
    }

    public void changeSuffix(Context theContext, boolean parent, boolean suffix, int mode)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.change, null);

        TextView t24 = yourCustomView.findViewById(R.id.textview68);
        TextView t25 = yourCustomView.findViewById(R.id.textview69);
        TextView t26 = yourCustomView.findViewById(R.id.textview70);
        TextView t27 = yourCustomView.findViewById(R.id.textview71);
        TextView t28 = yourCustomView.findViewById(R.id.textview72);
        TextView t29 = yourCustomView.findViewById(R.id.textview73);
        Spinner s8 = yourCustomView.findViewById(R.id.spinner18);
        Spinner s9 = yourCustomView.findViewById(R.id.spinner19);
        Spinner s10 = yourCustomView.findViewById(R.id.spinner20);
        EditText e10 = yourCustomView.findViewById(R.id.edittext27);
        EditText e11 = yourCustomView.findViewById(R.id.edittext28);

        List<Pair<String, String>> insertList;
        ArrayList<String> queryList = new ArrayList<>();
        ArrayList<String> beforeList = new ArrayList<>();
        ArrayList<String> afterList = new ArrayList<>();
        final int[] variable = {0, 0, 0};

        if (suffix)
        {
            insertList = getAllSuffixes();
            for (Pair<String, String> object : insertList)
            {
                queryList.add(object.first);
            }
            t24.setText("Old suffix:");
            t26.setText("After last letters:");
            t28.setText("New suffix:");
            t29.setText("After last letters:");
            beforeList.add("No changes to word");
            beforeList.add("Drop last letter");
            beforeList.add("Double last letter");
            afterList.add("After all last letters");
            afterList.add("After specific last letters");
            e11.setHint("(After all last letters)");
        }
        else
        {
            insertList = getAllPrefixes();
            for (Pair<String, String> object : insertList)
            {
                queryList.add(object.first);
            }
            t24.setText("Old prefix:");
            t26.setText("Before first letters:");
            t28.setText("New prefix:");
            t29.setText("Before first letters:");
            beforeList.add("No changes to word");
            beforeList.add("Drop first letter");
            beforeList.add("Double first letter");
            afterList.add("Before all first letters");
            afterList.add("Before specific first letters");
            e11.setHint("(Before all first letters)");
        }

        ArrayAdapter<String> queryAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, queryList);
        queryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s8.setAdapter(queryAdapter);

        s8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[0] = i;
                String prefix = queryList.get(i);
                String myColour = (insertList.get(i)).second;

                s10.setSelection(myColour.isEmpty() ? 0 : 1);
                e11.setText(myColour);

                if (suffix)
                {
                    t25.setText((!prefix.isEmpty() && prefix.charAt(0) == '+') ? (prefix.length() > 1 ? "Double last letter, " : "Double last letter") + prefix.substring(1) : ((!prefix.isEmpty() && prefix.charAt(0) == '-') ? (prefix.length() > 1 ? "Drop last letter, " : "Drop last letter") + prefix.substring(1) : (!prefix.isEmpty() ? "No changes to word, " : "No changes to word") + prefix));
                    t27.setText(myColour.isEmpty() ? "(After all last letters)" : myColour);

                    s9.setSelection((!prefix.isEmpty() && prefix.charAt(0) == '-') ? 1 : ((!prefix.isEmpty() && prefix.charAt(0) == '+') ? 2 : 0));
                    e10.setText((!prefix.isEmpty() && (prefix.charAt(0) == '+' || prefix.charAt(0) == '-')) ? prefix.substring(1) : prefix);
                }
                else
                {
                    int variables = prefix.length() - 1;
                    t25.setText((!prefix.isEmpty() && prefix.charAt(variables) == '+') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", double first letter" : "Double first letter") : ((!prefix.isEmpty() && prefix.charAt(variables) == '-') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", drop first letter" : "Drop first letter") : prefix + (!prefix.isEmpty() ? ", no changes to word" : "No changes to word")));
                    t27.setText(myColour.isEmpty() ? "(Before all first letters)" : myColour);

                    s9.setSelection((!prefix.isEmpty() && prefix.charAt(variables) == '-') ? 1 : ((!prefix.isEmpty() && prefix.charAt(variables) == '+') ? 2 : 0));
                    e10.setText((!prefix.isEmpty() && (prefix.charAt(variables) == '+' || prefix.charAt(variables) == '-')) ? prefix.substring(0, variables) : prefix);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> beforeAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, beforeList);
        beforeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s9.setAdapter(beforeAdapter);

        s9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[1] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> afterAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, afterList);
        afterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s10.setAdapter(afterAdapter);

        s10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[2] = i;
                if (i == 0)
                {
                    t29.setVisibility(View.INVISIBLE);
                    e11.setVisibility(View.INVISIBLE);
                }
                else
                {
                    t29.setVisibility(View.VISIBLE);
                    e11.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(suffix ? "Change suffix" : "Change prefix")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    if (suffix)
                    {
                        changePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, (variable[1] == 0 ? "" : (variable[1] == 1 ? "-" : "+")) + ((e10.getText()).toString()).toUpperCase(), variable[2] == 0 ? "" : ((e11.getText()).toString()).toUpperCase(), suffix);
                    }
                    else
                    {
                        changePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, ((e10.getText()).toString()).toUpperCase() + (variable[1] == 0 ? "" : (variable[1] == 1 ? "-" : "+")), variable[2] == 0 ? "" : ((e11.getText()).toString()).toUpperCase(), suffix);
                    }

                    if (parent && mode == 1) {
                        MainActivity home = (MainActivity) theContext;
                        home.refreshDefinition();
                    }
                }).create();
        dialog.show();
    }

    public void deleteSuffix(Context theContext, boolean parent, boolean suffix, int mode)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.remove, null);

        TextView t30 = yourCustomView.findViewById(R.id.textview74);
        TextView t31 = yourCustomView.findViewById(R.id.textview75);
        TextView t32 = yourCustomView.findViewById(R.id.textview76);
        TextView t33 = yourCustomView.findViewById(R.id.textview77);
        Spinner s11 = yourCustomView.findViewById(R.id.spinner9);

        List<Pair<String, String>> insertList;
        ArrayList<String> queryList = new ArrayList<>();
        final int[] variable = {0};

        if (suffix)
        {
            insertList = getAllSuffixes();
            for (Pair<String, String> object : insertList)
            {
                queryList.add(object.first);
            }
            t30.setText("Suffix:");
            t32.setText("After last letters:");
        }
        else
        {
            insertList = getAllPrefixes();
            for (Pair<String, String> object : insertList)
            {
                queryList.add(object.first);
            }
            t30.setText("Prefix:");
            t32.setText("Before first letters:");
        }

        ArrayAdapter<String> queryAdapter = new ArrayAdapter<>(theContext, android.R.layout.simple_spinner_item, queryList);
        queryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s11.setAdapter(queryAdapter);

        s11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[0] = i;
                String prefix = queryList.get(i);
                String myColour = (insertList.get(i)).second;

                if (suffix)
                {
                    t31.setText((!prefix.isEmpty() && prefix.charAt(0) == '+') ? (prefix.length() > 1 ? "Double last letter, " : "Double last letter") + prefix.substring(1) : ((!prefix.isEmpty() && prefix.charAt(0) == '-') ? (prefix.length() > 1 ? "Drop last letter, " : "Drop last letter") + prefix.substring(1) : (!prefix.isEmpty() ? "No changes to word, " : "No changes to word") + prefix));
                    t33.setText(myColour.isEmpty() ? "(After all last letters)" : myColour);
                }
                else
                {
                    int variables = prefix.length() - 1;
                    t31.setText((!prefix.isEmpty() && prefix.charAt(variables) == '+') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", double first letter" : "Double first letter") : ((!prefix.isEmpty() && prefix.charAt(variables) == '-') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", drop first letter" : "Drop first letter") : prefix + (!prefix.isEmpty() ? ", no changes to word" : "No changes to word")));
                    t33.setText(myColour.isEmpty() ? "(Before all first letters)" : myColour);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(suffix ? "Delete suffix" : "Delete prefix")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    if (suffix)
                    {
                        deletePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, suffix);
                    }
                    else
                    {
                        deletePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, suffix);
                    }

                    if (parent && mode == 1) {
                        MainActivity home = (MainActivity) theContext;
                        home.refreshDefinition();
                    }
                }).create();
        dialog.show();
    }

    public void addPrefix(String prefix, String before, boolean suffix)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(suffix ? "_suffix_" : "_prefix_", prefix);
        contentValues.put(suffix ? "_after_" : "_before_", before);

        db.insert(suffix ? "suffixes" : "prefixes", null, contentValues);
    }

    public void changePrefix(String myPrefix, String mySuffix, String prefix, String before, boolean suffix) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(suffix ? "_suffix_" : "_prefix_", prefix);
        values.put(suffix ? "_after_" : "_before_", before);

        db.update(suffix ? "suffixes" : "prefixes", values, suffix ? "_suffix_ = ? AND _after_ = ?" : "_prefix_ = ? AND _before_ = ?",
                new String[] {myPrefix, mySuffix});
    }

    public void deletePrefix(String myPrefix, String mySuffix, boolean suffix) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(suffix ? "suffixes" : "prefixes", suffix ? "_suffix_ = ? AND _after_ = ?" : "_prefix_ = ? AND _before_ = ?",
                new String[] {myPrefix, mySuffix});
    }

    public void deleteAllRecords(Context theContext, boolean parent, String theTable, int mode)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.display, null);

        TextView t34 = yourCustomView.findViewById(R.id.textview13);
        t34.setText("Deleting all rows from table '" + theTable + "'");

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle("Are you sure?")
                .setView(yourCustomView)
                .setPositiveButton("Yes", (dialog1, whichButton) -> {
                    deleteTable(theTable);
                    if (theTable.equals("colours")) {
                        refresh(theContext, parent);
                    } else {
                        if (parent && mode == 1) {
                            MainActivity home = (MainActivity) theContext;
                            home.refreshDefinition();
                        }
                    }
                })
                .setNegativeButton("No", (dialog2, whichButton) -> {
                }).create();
        dialog.show();
    }

    public void deleteTable(String myTable)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(myTable, null, null);
    }

    public String getFullDetails(String myGuess)
    {
        StringBuilder allDetails = new StringBuilder();
        HashSet<String> oneSide = new HashSet<>();

        char[] charArray = myGuess.toCharArray();
        Arrays.sort(charArray);
        String myAnagram = new String(charArray);

        List<Pair<String, String>> thePrefixes = getAllPrefixes();
        ArrayList<String> thePrefix = new ArrayList<>();
        for (Pair<String, String> rowItem : thePrefixes)
        {
            String alpha = rowItem.first;
            String beta = rowItem.second;
            boolean match = (beta.isEmpty() || beta.contains(Character.toString(myGuess.charAt(0))));

            if (!alpha.isEmpty() && alpha.charAt(alpha.length() - 1) == '+')
            {
                if (match)
                {
                    thePrefix.add("%" + alpha.substring(0, alpha.length() - 1) + myGuess.charAt(0) + myGuess);
                }
            }
            else if (!alpha.isEmpty() && alpha.charAt(alpha.length() - 1) == '-')
            {
                if (match)
                {
                    thePrefix.add("%" + alpha.substring(0, alpha.length() - 1) + myGuess.substring(1));
                }
            }
            else
            {
                if (match)
                {
                    thePrefix.add("%" + alpha + myGuess);
                }
            }
        }

        StringBuilder result1 = getFullSuffixes(thePrefix, "_word_ != \"" + myGuess + "\" AND ", false, oneSide, false);
        if (result1.length() > 0) {
            allDetails.append("<br>").append("<b>Prefixes:</b> ").append(result1);
        }

        List<Pair<String, String>> theSuffixes = getAllSuffixes();
        ArrayList<String> theSuffix = new ArrayList<>();
        for (Pair<String, String> rowItem : theSuffixes)
        {
            String alpha = rowItem.first;
            String beta = rowItem.second;
            boolean mismatch = (beta.isEmpty() || beta.contains(Character.toString(myGuess.charAt(myGuess.length() - 1))));

            if (!alpha.isEmpty() && alpha.charAt(0) == '+')
            {
                if (mismatch)
                {
                    theSuffix.add(myGuess + myGuess.charAt(myGuess.length() - 1) + alpha.substring(1) + "%");
                }
            }
            else if (!alpha.isEmpty() && alpha.charAt(0) == '-')
            {
                if (mismatch)
                {
                    theSuffix.add(myGuess.substring(0, myGuess.length() - 1) + alpha.substring(1) + "%");
                }
            }
            else
            {
                if (mismatch)
                {
                    theSuffix.add(myGuess + alpha + "%");
                }
            }
        }

        StringBuilder result2 = getFullSuffixes(theSuffix, "_word_ != \"" + myGuess + "\" AND ", false, oneSide, false);
        if (result2.length() > 0) {
            allDetails.append("<br>").append("<b>Suffixes:</b> ").append(result2);
        }

        ArrayList<String> bothSides = new ArrayList<>();
        for (Pair<String, String> prefixItem : thePrefixes)
        {
            for (Pair<String, String> suffixItem : theSuffixes)
            {
                String prefixAlpha = prefixItem.first;
                String prefixBeta = prefixItem.second;
                String suffixAlpha = suffixItem.first;
                String suffixBeta = suffixItem.second;
                boolean prefixMatch = (prefixBeta.isEmpty() || prefixBeta.contains(Character.toString(myGuess.charAt(0))));
                boolean suffixMatch = (suffixBeta.isEmpty() || suffixBeta.contains(Character.toString(myGuess.charAt(myGuess.length() - 1))));

                if (!prefixAlpha.isEmpty() && !suffixAlpha.isEmpty() && prefixAlpha.charAt(prefixAlpha.length() - 1) == '+' && suffixAlpha.charAt(0) == '+')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha.substring(0, prefixAlpha.length() - 1) + myGuess.charAt(0) + myGuess + myGuess.charAt(myGuess.length() - 1) + suffixAlpha.substring(1) + "%");
                    }
                }
                else if (!prefixAlpha.isEmpty() && !suffixAlpha.isEmpty() && prefixAlpha.charAt(prefixAlpha.length() - 1) == '+' && suffixAlpha.charAt(0) == '-')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha.substring(0, prefixAlpha.length() - 1) + myGuess.charAt(0) + myGuess.substring(0, myGuess.length() - 1) + suffixAlpha.substring(1) + "%");
                    }
                }
                else if (!prefixAlpha.isEmpty() && !suffixAlpha.isEmpty() && prefixAlpha.charAt(prefixAlpha.length() - 1) == '-' && suffixAlpha.charAt(0) == '+')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha.substring(0, prefixAlpha.length() - 1) + myGuess.substring(1) + myGuess.charAt(myGuess.length() - 1) + suffixAlpha.substring(1) + "%");
                    }
                }
                else if (!prefixAlpha.isEmpty() && !suffixAlpha.isEmpty() && prefixAlpha.charAt(prefixAlpha.length() - 1) == '-' && suffixAlpha.charAt(0) == '-')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha.substring(0, prefixAlpha.length() - 1) + myGuess.substring(1, myGuess.length() - 1) + suffixAlpha.substring(1) + "%");
                    }
                }
                else if (!prefixAlpha.isEmpty() && prefixAlpha.charAt(prefixAlpha.length() - 1) == '+')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha.substring(0, prefixAlpha.length() - 1) + myGuess.charAt(0) + myGuess + suffixAlpha + "%");
                    }
                }
                else if (!prefixAlpha.isEmpty() && prefixAlpha.charAt(prefixAlpha.length() - 1) == '-')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha.substring(0, prefixAlpha.length() - 1) + myGuess.substring(1) + suffixAlpha + "%");
                    }
                }
                else if (!suffixAlpha.isEmpty() && suffixAlpha.charAt(0) == '+')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha + myGuess + myGuess.charAt(myGuess.length() - 1) + suffixAlpha.substring(1) + "%");
                    }
                }
                else if (!suffixAlpha.isEmpty() && suffixAlpha.charAt(0) == '-')
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha + myGuess.substring(0, myGuess.length() - 1) + suffixAlpha.substring(1) + "%");
                    }
                }
                else
                {
                    if (prefixMatch && suffixMatch)
                    {
                        bothSides.add("%" + prefixAlpha + myGuess + suffixAlpha + "%");
                    }
                }
            }
        }

        StringBuilder result3 = getFullSuffixes(bothSides, "_word_ != \"" + myGuess + "\" AND ", false, oneSide, true);
        if (result3.length() > 0) {
            allDetails.append("<br>").append("<b>Extensions on both sides:</b> ").append(result3);
        }

        ArrayList<String> theAnagram = new ArrayList<>();
        theAnagram.add(myAnagram);
        StringBuilder result4 = getFullSuffixes(theAnagram, "_word_ != \"" + myGuess + "\" AND ", true, null, false);
        if (result4.length() > 0) {
            allDetails.append("<br>").append("<b>Anagrams:</b> ").append(result4);
        }

        ArrayList<String> singleLetterChange = new ArrayList<>();
        for (int myIndex = 0; myIndex < myGuess.length(); myIndex++)
        {
            singleLetterChange.add(myGuess.substring(0, myIndex) + "_" + myGuess.substring(myIndex + 1));
        }

        StringBuilder result5 = getFullSuffixes(singleLetterChange, "_word_ != \"" + myGuess + "\" AND ", false, null, false);
        if (result5.length() > 0) {
            allDetails.append("<br>").append("<b>One letter change by position:</b> ").append(result5);
        }

        ArrayList<String> singleLetterAdd = new ArrayList<>();
        char myCharacter = 0;
        for (int myIndex = 0; myIndex < myAnagram.length(); myIndex++)
        {
            char current = myAnagram.charAt(myIndex);
            if (myIndex > 0 && current == myCharacter)
            {
                continue;
            }
            singleLetterAdd.add(myAnagram.substring(0, myIndex) + myAnagram.substring(myIndex + 1));
            myCharacter = current;
        }

        StringBuilder result6 = getFullPrefixes(singleLetterAdd, true);
        if (result6.length() > 0) {
            allDetails.append("<br>").append("<b>One letter drop:</b> ").append(result6);
        }

        ArrayList<String> oneLetterDrop = new ArrayList<>();
        char previous = 0;
        for (int myIndex = 0; myIndex < myAnagram.length(); myIndex++)
        {
            char present = myAnagram.charAt(myIndex);
            if (myIndex > 0 && present == previous)
            {
                continue;
            }
            StringBuilder singleLetterDrop = new StringBuilder();
            for (int theIndex = 0; theIndex < myAnagram.length(); theIndex++)
            {
                if (theIndex == myIndex)
                {
                    continue;
                }
                singleLetterDrop.append("%").append(myAnagram.charAt(theIndex));
            }
            singleLetterDrop.append("%");
            oneLetterDrop.add(new String(singleLetterDrop));
            previous = present;
        }

        StringBuilder result7 = getFullSuffixes(oneLetterDrop, "_word_ != \"" + myGuess + "\" AND _length_ = " + myGuess.length() + " AND ", true, null, false);
        if (result7.length() > 0) {
            allDetails.append("<br>").append("<b>One letter change:</b> ").append(result7);
        }

        StringBuilder oneLetterChange = new StringBuilder();
        for (int myIndex = 0; myIndex < myAnagram.length(); myIndex++)
        {
            oneLetterChange.append("%").append(myAnagram.charAt(myIndex));
        }
        oneLetterChange.append("%");

        ArrayList<String> oneLetterAdd = new ArrayList<>();
        oneLetterAdd.add(new String(oneLetterChange));

        StringBuilder result8 = getFullSuffixes(oneLetterAdd, "_length_ = " + (myGuess.length() + 1) + " AND ", true, null, false);
        if (result8.length() > 0) {
            allDetails.append("<br>").append("<b>One letter addition:</b> ").append(result8);
        }

        return new String(allDetails);
    }

    public StringBuilder getFullPrefixes(ArrayList<String> argument, boolean myAlphagram)
    {
        StringBuilder fullDetails = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        String listItems = (((argument.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        Cursor cursor = db.rawQuery("SELECT _front_, _word_, _back_ FROM words WHERE " + (myAlphagram ? "_alphagram_" : "_word_") + " IN " + listItems + " ORDER BY _word_", null);

        int radix = 0;

        if (cursor.moveToFirst()) {
            do {
                String firstItem = cursor.getString(0);
                String secondItem = cursor.getString(1);
                String thirdItem = cursor.getString(2);

                if (radix > 0) {
                    fullDetails.append(", ");
                }

                fullDetails.append("<small>").append(firstItem).append("</small> ").append(secondItem).append(" <small>").append(thirdItem).append("</small>");
                radix++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return fullDetails;
    }

    public StringBuilder getFullSuffixes(ArrayList<String> argument, String condition, boolean myAlphagram, HashSet<String> oneSide, boolean redundant)
    {
        StringBuilder fullDetails = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder listItems = new StringBuilder(condition);
        if (argument.size() > 1)
        {
            listItems.append("(");
        }
        int rank = 0;
        for (String argumentItem : argument)
        {
            if (rank == 0)
            {
                listItems.append(myAlphagram ? "_alphagram_ LIKE \"" : "_word_ LIKE \"").append(argumentItem).append("\"");
            }
            else
            {
                listItems.append(myAlphagram ? " OR _alphagram_ LIKE \"" : " OR _word_ LIKE \"").append(argumentItem).append("\"");
            }
            rank++;
        }
        if (argument.size() > 1)
        {
            listItems.append(")");
        }

        Cursor cursor = db.rawQuery("SELECT _front_, _word_, _back_ FROM words WHERE " + listItems + " ORDER BY _word_", null);
        int radix = 0;

        if (cursor.moveToFirst()) {
            do {
                String secondItem = cursor.getString(1);

                if (!redundant || !oneSide.contains(secondItem))
                {
                    String firstItem = cursor.getString(0);
                    String thirdItem = cursor.getString(2);

                    if (radix > 0) {
                        fullDetails.append(", ");
                    }

                    fullDetails.append("<small>").append(firstItem).append("</small> ").append(secondItem).append(" <small>").append(thirdItem).append("</small>");
                    radix++;

                    if (!redundant && oneSide != null) {
                        oneSide.add(secondItem);
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return fullDetails;
    }

    public void updateProgressBar(Context yourContext, boolean parent, ProgressBar progressBar, TextView leftText, TextView rightText, AlertDialog theDialog, double percentage, String fraction, boolean joker)
    {
        if (parent)
        {
            MainActivity homeActivity = (MainActivity) yourContext;

            homeActivity.runOnUiThread(() -> {
                if (!theDialog.isShowing()) {
                    theDialog.show();
                }

                progressBar.setProgress((int) Math.round(percentage));
                leftText.setText(joker ? String.format("%.1f%%", percentage) : String.format("%.0f%%", percentage));
                rightText.setText(fraction);
            });
        }
        else
        {
            Report homeActivity = (Report) yourContext;

            homeActivity.runOnUiThread(() -> {
                if (!theDialog.isShowing()) {
                    theDialog.show();
                }

                progressBar.setProgress((int) Math.round(percentage));
                leftText.setText(joker ? String.format("%.1f%%", percentage) : String.format("%.0f%%", percentage));
                rightText.setText(fraction);
            });
        }
    }

    public String[] getAllColumns(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myCursor = db.query(tableName, null, null, null, null, null, null);
        String[] allColumns = myCursor.getColumnNames();
        myCursor.close();
        return allColumns;
    }

    public ArrayList<String> getBlankAnagrams(String inputWord) {
        ArrayList<String> blankAnagrams = new ArrayList<>();
        char[] sortedAnagram = inputWord.toCharArray();
        Arrays.sort(sortedAnagram);
        blankAnagrams.add(new String(sortedAnagram));
        HashSet<Character> over = new HashSet<>();

        for (int alphabetIndex = 0; alphabetIndex < inputWord.length(); alphabetIndex++) {
            char anagramsItem = inputWord.charAt(alphabetIndex);
            if (!over.contains(anagramsItem)) {
                over.add(anagramsItem);
                String anagramItem = inputWord.substring(0, alphabetIndex) + inputWord.substring(alphabetIndex + 1);
                char[] blankAnagram = anagramItem.toCharArray();
                Arrays.sort(blankAnagram);
                blankAnagrams.add(new String(blankAnagram) + "?");
            }
        }

        return blankAnagrams;
    }

    public ArrayList<String> getBlankAlphagrams(String inputWord) {
        ArrayList<String> blankAnagrams = new ArrayList<>();
        HashSet<Character> over = new HashSet<>();

        for (int alphabetIndex = 0; alphabetIndex < inputWord.length(); alphabetIndex++) {
            char anagramsItem = inputWord.charAt(alphabetIndex);
            if (!over.contains(anagramsItem)) {
                over.add(anagramsItem);
                String anagramItem = inputWord.substring(0, alphabetIndex) + inputWord.substring(alphabetIndex + 1);
                char[] blankAnagram = anagramItem.toCharArray();
                Arrays.sort(blankAnagram);
                blankAnagrams.add(new String(blankAnagram) + "?");
            }
        }

        return blankAnagrams;
    }

    public String convert(String time) {
        double period = Double.parseDouble(time);
        ArrayList<String> conversion = new ArrayList<>();
        int days = (int) (period / 86400);
        int hours = (int) ((period / 3600) % 24);
        int minutes = (int) ((period / 60) % 60);
        int seconds = (int) (period % 60);
        int milliseconds = (int) Math.round((period % 1) * 1000);

        if (days > 0) {
            conversion.add(days + "d");
        }
        if (hours > 0) {
            conversion.add(hours + "h");
        }
        if (minutes > 0) {
            conversion.add(minutes + "m");
        }
        if (seconds > 0) {
            conversion.add(seconds + "s");
        }
        if (milliseconds > 0) {
            conversion.add(milliseconds + "ms");
        }
        if (conversion.isEmpty()) {
            conversion.add("0ms");
        }

        return String.join(" ", conversion);
    }

    public ArrayList<Column> getTableInformation(boolean allTables) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Column> tableInfo = new ArrayList<>();
        HashSet<String> covered = new HashSet<>();

        for (String myTables : (allTables ? getTableNames() : queryTables)) {
            Cursor theCursor = db.rawQuery("PRAGMA table_info(\"" + myTables + "\")", null);
            if (theCursor.moveToFirst()) {
                do {
                    String theColumn = theCursor.getString(1);
                    if (!covered.contains(theColumn)) {
                        covered.add(theColumn);
                        String theType = theCursor.getString(2);

                        Column myColumn = new Column(theColumn, theType);
                        tableInfo.add(myColumn);
                    }
                } while (theCursor.moveToNext());
            }

            theCursor.close();
        }

        return tableInfo;
    }

    public void addFunctionalities(Context con, EditText editText, CheckBox autoUnderscores, boolean allTables, View subCustomView) {
        ArrayList<Column> tableInformation = getTableInformation(allTables);
        ArrayAdapter<Column> columnAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, tableInformation);
        columnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Button b1 = subCustomView.findViewById(R.id.button99);
        Button b2 = subCustomView.findViewById(R.id.button73);
        Button b3 = subCustomView.findViewById(R.id.button81);
        Button b4 = subCustomView.findViewById(R.id.button89);
        Button b5 = subCustomView.findViewById(R.id.button74);
        Button b6 = subCustomView.findViewById(R.id.button94);
        Button b7 = subCustomView.findViewById(R.id.button95);
        Button b8 = subCustomView.findViewById(R.id.button96);
        Button b9 = subCustomView.findViewById(R.id.button97);
        Button b10 = subCustomView.findViewById(R.id.button98);
        Button b11 = subCustomView.findViewById(R.id.button90);
        Button b12 = subCustomView.findViewById(R.id.button91);
        Button b13 = subCustomView.findViewById(R.id.button92);
        Button b14 = subCustomView.findViewById(R.id.button93);

        EditText e12 = subCustomView.findViewById(R.id.edittext32);
        EditText e13 = subCustomView.findViewById(R.id.edittext33);
        EditText e14 = subCustomView.findViewById(R.id.edittext34);
        EditText e15 = subCustomView.findViewById(R.id.edittext35);
        EditText e16 = subCustomView.findViewById(R.id.edittext36);

        Spinner s13 = subCustomView.findViewById(R.id.spinner40);
        Spinner s14 = subCustomView.findViewById(R.id.spinner41);
        Spinner s15 = subCustomView.findViewById(R.id.spinner42);
        Spinner s16 = subCustomView.findViewById(R.id.spinner43);
        Spinner s17 = subCustomView.findViewById(R.id.spinner44);
        Spinner s18 = subCustomView.findViewById(R.id.spinner45);

        b1.setOnClickListener(v -> {
            Help help = new Help();
            if (allTables) {
                messageBox("Example SQL queries", help.getSqlHelp(), con);
            }
            else {
                messageBox("Example custom queries", help.getCustomHelp(), con);
            }
        });

        b2.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            editText.append(space ? "AND" : " AND");
        });

        b3.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            editText.append(space ? "OR" : " OR");
        });

        b4.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            editText.append(space ? "NOT" : " NOT");
        });

        b5.setOnClickListener(v -> {
            editText.setText("");
        });

        b6.setOnClickListener(v -> {
            e12.setText("");
        });

        b7.setOnClickListener(v -> {
            e13.setText("");
        });

        b8.setOnClickListener(v -> {
            e14.setText("");
        });

        b9.setOnClickListener(v -> {
            e15.setText("");
        });

        b10.setOnClickListener(v -> {
            e16.setText("");
        });

        b11.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            Column columnName = tableInformation.get(s13.getSelectedItemPosition());
            String conditions = ((e12.getText()).toString()).toUpperCase();
            editText.append((space ? "" : " ") + columnName.getColumn(autoUnderscores.isChecked()) + " " + (s14.getSelectedItem()).toString() + " " + ((columnName.getType()).equalsIgnoreCase("text") ? "'" + conditions + "'" : conditions));
        });

        b12.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            Column columnName = tableInformation.get(s15.getSelectedItemPosition());
            String conditions = ((e13.getText()).toString()).toUpperCase();
            String[] conditionList = conditions.split(",\\s*");
            String conditionsList = ((columnName.getType()).equalsIgnoreCase("text") ? "('" + String.join("', '", conditionList) + "')" : "(" + String.join(", ", conditionList) + ")");
            editText.append((space ? "" : " ") + columnName.getColumn(autoUnderscores.isChecked()) + " " + (s16.getSelectedItem()).toString() + " " + conditionsList);
        });

        b13.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            Column columnName = tableInformation.get(s17.getSelectedItemPosition());
            String conditions = ((e14.getText()).toString()).toUpperCase();
            editText.append((space ? "" : " ") + columnName.getColumn(autoUnderscores.isChecked()) + " LIKE " + ((columnName.getType()).equalsIgnoreCase("text") ? "'" + conditions + "'" : conditions));
        });

        b14.setOnClickListener(v -> {
            String originalString = (editText.getText()).toString();
            boolean space = (originalString.isEmpty() || originalString.charAt(originalString.length() - 1) == ' ');
            Column columnName = tableInformation.get(s18.getSelectedItemPosition());
            String firstCondition = ((e15.getText()).toString()).toUpperCase();
            String secondCondition = ((e16.getText()).toString()).toUpperCase();
            editText.append((space ? "" : " ") + columnName.getColumn(autoUnderscores.isChecked()) + " BETWEEN " + ((columnName.getType()).equalsIgnoreCase("text") ? "'" + firstCondition + "'" : firstCondition) + " AND " + ((columnName.getType()).equalsIgnoreCase("text") ? "'" + secondCondition + "'" : secondCondition));
        });

        s13.setAdapter(columnAdapter);
        s15.setAdapter(columnAdapter);
        s17.setAdapter(columnAdapter);
        s18.setAdapter(columnAdapter);

        ArrayAdapter<String> comparatorsAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, comparators);
        comparatorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s14.setAdapter(comparatorsAdapter);

        ArrayAdapter<String> comparatorAdapter = new ArrayAdapter<>(con, android.R.layout.simple_spinner_item, comparator);
        comparatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s16.setAdapter(comparatorAdapter);
    }

    public String myFormat(String myString) {
        String[] mySplit = myString.split("_");

        for (int theSplit = mySplit.length - 1; theSplit >= 0; theSplit--) {
            if (!mySplit[theSplit].isEmpty()) {
                return (mySplit[theSplit].substring(0, 1)).toUpperCase() + (mySplit[theSplit].substring(1)).toLowerCase();
            }
        }

        return myString;
    }

    public void tileDistribution(RecyclerView recyclerView, Context theParentContext) {
        ArrayList<String> letterList = new ArrayList<>();
        String[] tileList = getAllColumns("letters");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor letterCursor = db.rawQuery("SELECT * FROM letters", null);

        for (int tileCursor = 0; tileCursor < tileList.length; tileCursor++) {
            letterList.add("<b>" + myFormat(tileList[tileCursor]) + "</b>");

            if (letterCursor.moveToFirst()) {
                do {
                    letterList.add(letterCursor.getString(tileCursor));
                } while (letterCursor.moveToNext());
            }
        }

        letterCursor.close();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(theParentContext, letterList.size() / tileList.length, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        GridAdapter gridAdapter = new GridAdapter(theParentContext, R.layout.text, letterList);
        recyclerView.setAdapter(gridAdapter);
    }

    public void letterDistribution(Context parentContext) {
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        final View yourCustomView = inflater.inflate(R.layout.grid, null);

        RecyclerView g1 = yourCustomView.findViewById(R.id.gridview3);
        tileDistribution(g1, parentContext);

        AlertDialog dialog = new AlertDialog.Builder(parentContext)
                .setTitle("Letter distribution")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                }).create();
        dialog.show();
    }

    public int getFilterSerial() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "UPDATE zoom SET _columns_ = _columns_ + 1 WHERE _activity_ = \"List\""
        );

        Cursor cursor = db.rawQuery("SELECT _columns_ FROM zoom WHERE _activity_ = \"List\"", null);
        int filterSerial = 0;

        if (cursor.moveToFirst()) {
            do {
                filterSerial = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return filterSerial;
    }

    public String getFilterName(int filterNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT _name_ FROM filters WHERE _serial_ = " + filterNumber, null);
        String filterIdentity = "";

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    filterIdentity = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return filterIdentity;
    }

    public void saveFilter(int filterNumber, String filterName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues filterValues = new ContentValues();
        filterValues.put("_name_", filterName);

        db.update("filters", filterValues, "_serial_ = ?",
                new String[] {Integer.toString(filterNumber)});
    }

    public ArrayList<Filter> loadFilter(String filterString) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Filter> filterList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT _length_, _query_, _sort_, _blank_, _name_, _serial_ FROM filters WHERE _name_ " + (filterString.isEmpty() ? "!= \"\"" : "LIKE \"" + filterString + "%\"") + " ORDER BY _name_", null);

        if (cursor.moveToFirst()) {
            do {
                int length = cursor.getInt(0);
                String query = cursor.getString(1);
                String sort = cursor.getString(2);
                int blank = cursor.getInt(3);
                String name = cursor.getString(4);
                int serial = cursor.getInt(5);

                Filter myFilter = new Filter(length, query, sort, blank, name, serial);
                filterList.add(myFilter);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return filterList;
    }

    public void emptyTable(Context theParentActivity, boolean blank)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM " + (blank ? "blanks" : "words") + ")", null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) == 0)
                {
                    alertBox("Empty " + (blank ? "'blanks'" : "'words'") + " table", "There are no rows in the " + (blank ? "'blanks'" : "'words'") + " table. Go to menu and select 'Prepare " + (blank ? "blank" : "regular") + " database' at first.", theParentActivity);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}