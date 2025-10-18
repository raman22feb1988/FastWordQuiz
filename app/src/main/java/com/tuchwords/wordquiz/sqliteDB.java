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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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

    public sqliteDB(Context context, int version, ArrayList<String> ultimate, boolean create) {
        super(context, DATABASE_NAME, null, version);
        // TODO Auto-generated constructor stub
        lastActivity = context;
        DATABASE_VERSION = version;
        last = ultimate;
        recreate = create;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table words(_word_ text collate nocase, _length_ integer, _alphagram_ text collate nocase, _definition_ text collate nocase, _probability_ real, _time_ real, _solved_ integer, _back_ text collate nocase, _front_ text collate nocase, _tag_ text collate nocase, _page_ integer, _answers_ integer, _csw24_ integer, _csw21_ integer, _csw19_ integer, _csw15_ integer, _csw12_ integer, _csw07_ integer, _nwl23_ integer, _nwl20_ integer, _nwl18_ integer, _twl06_ integer, _nswl23_ integer, _nswl20_ integer, _nswl18_ integer, _wims_ integer, _cel21_ integer, _serial_ integer, _position_ integer, _timestamp_ text collate nocase, _incorrect_ integer, _wrong_ text collate nocase, _reverse_ text collate nocase, _zetagram_ text collate nocase, _no_a_ integer, _no_b_ integer, _no_c_ integer, _no_d_ integer, _no_e_ integer, _no_f_ integer, _no_g_ integer, _no_h_ integer, _no_i_ integer, _no_j_ integer, _no_k_ integer, _no_l_ integer, _no_m_ integer, _no_n_ integer, _no_o_ integer, _no_p_ integer, _no_q_ integer, _no_r_ integer, _no_s_ integer, _no_t_ integer, _no_u_ integer, _no_v_ integer, _no_w_ integer, _no_x_ integer, _no_y_ integer, _no_z_ integer, _vowels_ integer, _consonants_ integer, _points_ integer, _power_ integer)"
        );
        db.execSQL(
                "create table scores(_length_ integer, _query_ text collate nocase, _blank_ integer, _complete_ integer, _incomplete_ integer, _counter_ integer, _something_ integer, _nothing_ integer, _partial_ integer, _total_ integer, _solved_ integer, _unsolved_ integer, _page_ integer, _sort_ text collate nocase)"
        );
        db.execSQL(
                "create table colours(_tag_ text collate nocase, _colour_ text collate nocase)"
        );
        db.execSQL(
                "create table zoom(_activity_ text collate nocase, _rows_ integer, _columns_ integer, _size_ integer)"
        );
        db.execSQL(
                "create table prefixes(_prefix_ text collate nocase, _before_ text collate nocase)"
        );
        db.execSQL(
                "create table suffixes(_suffix_ text collate nocase, _after_ text collate nocase)"
        );
        db.execSQL(
                "create table blanks(_word_ text collate nocase, _length_ integer, _alphagram_ text collate nocase, _definition_ text collate nocase, _probability_ real, _time_ real, _solved_ integer, _back_ text collate nocase, _front_ text collate nocase, _tag_ text collate nocase, _page_ integer, _answers_ integer, _csw24_ integer, _csw21_ integer, _csw19_ integer, _csw15_ integer, _csw12_ integer, _csw07_ integer, _nwl23_ integer, _nwl20_ integer, _nwl18_ integer, _twl06_ integer, _nswl23_ integer, _nswl20_ integer, _nswl18_ integer, _wims_ integer, _cel21_ integer, _serial_ integer, _position_ integer, _timestamp_ text collate nocase, _incorrect_ integer, _wrong_ text collate nocase, _reverse_ text collate nocase, _zetagram_ text collate nocase, _no_a_ integer, _no_b_ integer, _no_c_ integer, _no_d_ integer, _no_e_ integer, _no_f_ integer, _no_g_ integer, _no_h_ integer, _no_i_ integer, _no_j_ integer, _no_k_ integer, _no_l_ integer, _no_m_ integer, _no_n_ integer, _no_o_ integer, _no_p_ integer, _no_q_ integer, _no_r_ integer, _no_s_ integer, _no_t_ integer, _no_u_ integer, _no_v_ integer, _no_w_ integer, _no_x_ integer, _no_y_ integer, _no_z_ integer, _vowels_ integer, _consonants_ integer, _points_ integer, _power_ integer)"
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
        dropStatements.add("DROP TABLE if exists scores");
        dropStatements.add("DROP TABLE if exists colours");
        dropStatements.add("DROP TABLE if exists zoom");
        dropStatements.add("DROP TABLE if exists prefixes");
        dropStatements.add("DROP TABLE if exists suffixes");
        dropStatements.add("DROP TABLE if exists blanks");

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
            String tokens[] = sqlQuery.split("\\s+");
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

    public boolean containsWord(String myWord)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM words WHERE _word_ = \"" + myWord + "\")", null);

        int exist = 0;

        if (cursor.moveToFirst()) {
            do {
                exist = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (exist != 0);
    }

    public String getLabel(String guess)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_ FROM words WHERE _word_ = \"" + guess + "\"", null);

        String label = null;

        if (cursor.moveToFirst()) {
            do {
                label = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return label;
    }

    public String getLabelColours(Context parentContext)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _tag_, _colour_ FROM colours ORDER BY _tag_", null);

        StringBuilder labelColours = new StringBuilder("<b>");
        int line = 1;

        int nightModeFlags =
                parentContext.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        String white = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "#000000" : "#FFFFFF");

        if (cursor.moveToFirst()) {
            do {
                String label = cursor.getString(0);
                String colour = cursor.getString(1);

                if (colour.equals(white))
                {
                    if (line == 1) {
                        labelColours.append(line).append(". ").append(label.length() == 0 ? "(Default)" : label).append(": ").append(colour);
                    } else {
                        labelColours.append("<br>").append(line).append(". ").append(label.length() == 0 ? "(Default)" : label).append(": ").append(colour);
                    }
                }
                else {
                    if (line == 1) {
                        labelColours.append("<font color=\"").append(colour).append("\">").append(line).append(". ").append(label.length() == 0 ? "(Default)" : label).append(": ").append(colour).append("</font>");
                    } else {
                        labelColours.append("<br><font color=\"").append(colour).append("\">").append(line).append(". ").append(label.length() == 0 ? "(Default)" : label).append(": ").append(colour).append("</font>");
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
            schema.append(schema.length() == 0 ? tableName + "\n" + columnArray.toString() : "\n" + tableName + "\n" + columnArray.toString());
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

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
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
                            if (myLine % myStep < 1 || myLine == 1.0)
                            {
                                updateProgressBar(situation, parent, p4, t41, t42, myDialog, ((int) (myLine / myStep)), ((int) myLine) + "/" + curCSV.getCount());
                            }
                        }
                        csvWrite.close();
                        curCSV.close();
                        outputDir.append("\nSaved " + table + " table to " + file.getAbsolutePath() + ".");
                        uiThreadBox("Export CSV", "Export CSV complete." + new String(outputDir), situation, parent);
                    }
                } catch (Exception sqlEx) {
                    myDialog.dismiss();
                    uiThreadBox("Error", sqlEx.toString(), situation, parent);
                } finally {
                    myDialog.dismiss();
                }
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
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

                        Thread thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
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
                                                    updateProgressBar(situation, parent, p2, t37, t38, myDialog, ((int) (myLine / myStep)), ((int) myLine) + "/" + lines);
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
                            }
                        });

                        thread2.start();
                    }
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

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                File[] inputDir = ContextCompat.getExternalFilesDirs(situation, Environment.DIRECTORY_DOCUMENTS);
                File exportDir = inputDir[0];

                File file = new File(exportDir, "tags.csv");
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    Cursor curCSV = db.rawQuery("SELECT _word_, _tag_, _solved_, _time_ FROM words WHERE _time_ > 0 OR _tag_ != \"\"", null);
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
                        if (myLine % myStep < 1 || myLine == 1.0)
                        {
                            updateProgressBar(situation, parent, p3, t39, t40, myDialog, ((int) (myLine / myStep)), ((int) myLine) + "/" + curCSV.getCount());
                        }
                    }
                    csvWrite.close();
                    curCSV.close();
                    uiThreadBox("Export tags", "Export tags complete.\nSaved tags to " + file.getAbsolutePath() + ".", situation, parent);
                } catch (Exception sqlEx) {
                    myDialog.dismiss();
                    uiThreadBox("Error", sqlEx.toString(), situation, parent);
                } finally {
                    myDialog.dismiss();
                }
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
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

                                        double myLine = 0.0;
                                        double myStep = lines / 100.0;
                                        do {
                                            ContentValues contentValues = new ContentValues();
                                            int wordIndex = 0;
                                            for (int column = 0; column < columns.length; column++) {
                                                if (columns[column].equals("word")) {
                                                    wordIndex = column;
                                                } else {
                                                    contentValues.put("_" + columns[column] + "_", nextLine[column]);
                                                }
                                            }
                                            db.update("words", contentValues, "_word_ = ?",
                                                    new String[]{nextLine[wordIndex]});
                                            nextLine = csvRead.readNext();
                                            myLine++;
                                            if (myLine % myStep < 1 || myLine == 1.0) {
                                                updateProgressBar(situation, parent, p1, t35, t36, myDialog, ((int) (myLine / myStep)), ((int) myLine) + "/" + lines);
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
                    }
                }).create();
        dialog.show();
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
            chance *= frequency[ord];
            chance /= count;
            if (frequency[ord] > 0) {
                frequency[ord]--;
            }
            count--;
        }
        return chance;
    }

    public int insertWord(Context myContext, boolean yourParent, HashMap<String, String> dictionary, HashMap<String, Integer> anagramsList, HashMap<String, String> lexicon)
    {
        LayoutInflater myInflater = LayoutInflater.from(myContext);
        final View myCustomView = myInflater.inflate(R.layout.progressbar, null);

        ProgressBar p5 = myCustomView.findViewById(R.id.progressbar1);
        TextView t43 = myCustomView.findViewById(R.id.textview78);
        TextView t44 = myCustomView.findViewById(R.id.textview79);

        AlertDialog myDialog = new AlertDialog.Builder(myContext)
                .setTitle("Preparing database")
                .setView(myCustomView)
                .create();
        myDialog.show();

        SQLiteDatabase db = this.getWritableDatabase();
        final int[] success = {1};

        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                db.beginTransaction();

                try {
                    Iterator<Map.Entry<String, String>> itr = dictionary.entrySet().iterator();
                    double myLine = 0.0;
                    double myStep1 = dictionary.size() / 50.0;
                    while (itr.hasNext()) {
                        Map.Entry<String, String> entry = itr.next();
                        String word = entry.getKey();
                        char[] c = word.toCharArray();
                        Arrays.sort(c);
                        String anagram = new String(c);
                        int solutions = anagramsList.get(anagram);
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

                        int[] point = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
                        boolean[] vowel = {true, false, false, false, true, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false};
                        boolean[] power = {false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, true, false, false, false, false, false, false, true, false, true};

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
                            } else {
                                consonants++;
                            }

                            if (power[positionInAlphabet]) {
                                powers++;
                            }
                        }

                        for (int theRadix = 0; theRadix < 26; theRadix++) {
                            char occurrences = (char) (theRadix + 97);
                            contentValues.put("_no_" + occurrences + "_", occurrence[theRadix]);
                        }

                        contentValues.put("_vowels_", vowels);
                        contentValues.put("_consonants_", consonants);
                        contentValues.put("_points_", points);
                        contentValues.put("_power_", powers);

                        db.insert("words", null, contentValues);

                        myLine++;
                        if (myLine % myStep1 < 1 || myLine == 1.0)
                        {
                            updateProgressBar(myContext, yourParent, p5, t43, t44, myDialog, ((int) (myLine / myStep1)), ((int) myLine) + "/" + dictionary.size());
                        }
                    }

                    HashMap<Integer, ArrayList<String>> pageHash = new HashMap<>();
                    HashMap<Integer, ArrayList<String>> cellHash = new HashMap<>();

                    for (int lengths = 2; lengths <= 58; lengths++) {
                        Cursor anagramList = getAllAnagrams(lengths, "*", 2, " ORDER BY _probability_ DESC");
                        int wordLength = anagramList.getCount();
                        int pages = (((wordLength - 1) / 50) + 1);

                        for (int pageNumber = 0; pageNumber < pages; pageNumber++) {
                            if (!pageHash.containsKey(pageNumber + 1)) {
                                pageHash.put(pageNumber + 1, new ArrayList<>());
                            }

                            int open = pageNumber * 50;
                            int close = Math.min((pageNumber + 1) * 50, wordLength);

                            if (anagramList.moveToPosition(open)) {
                                do {
                                    (pageHash.get(pageNumber + 1)).add(anagramList.getString(0));
                                } while (anagramList.moveToNext() && anagramList.getPosition() < close);
                            }
                        }

                        for (int cellValue = 0; cellValue < 50; cellValue++) {
                            if (!cellHash.containsKey(cellValue + 1)) {
                                cellHash.put(cellValue + 1, new ArrayList<>());
                            }

                            if (anagramList.moveToPosition(cellValue)) {
                                do {
                                    (cellHash.get(cellValue + 1)).add(anagramList.getString(0));
                                } while (anagramList.move(50));
                            }
                        }

                        anagramList.close();
                    }

                    uiThreadTitle("Setting page numbers", myDialog, myContext, yourParent);
                    double myStep2 = pageHash.size() / 40.0;
                    for (int positionNumber = 1; positionNumber <= pageHash.size(); positionNumber++) {
                        String pageString = ((((pageHash.get(positionNumber)).toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

                        ContentValues values = new ContentValues();
                        values.put("_page_", positionNumber);

                        success[0] &= db.update("words", values, "_alphagram_ IN " + pageString,
                                new String[] {});

                        if (positionNumber % myStep2 < 1 || positionNumber == 1)
                        {
                            updateProgressBar(myContext, yourParent, p5, t43, t44, myDialog, 50 + ((int) (positionNumber / myStep2)), positionNumber + "/" + pageHash.size());
                        }
                    }

                    uiThreadTitle("Setting grid numbers", myDialog, myContext, yourParent);
                    double myStep3 = cellHash.size() / 10.0;
                    for (int cellNumber = 1; cellNumber <= cellHash.size(); cellNumber++) {
                        String cellString = ((((cellHash.get(cellNumber)).toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

                        ContentValues values = new ContentValues();
                        values.put("_position_", cellNumber);

                        success[0] &= db.update("words", values, "_alphagram_ IN " + cellString,
                                new String[] {});

                        if (cellNumber % myStep3 < 1 || cellNumber == 1)
                        {
                            updateProgressBar(myContext, yourParent, p5, t43, t44, myDialog, 90 + ((int) (cellNumber / myStep3)), cellNumber + "/" + cellHash.size());
                        }
                    }

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

                    Iterator<Map.Entry<String, String>> it = coloursList.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> enter = it.next();
                        String tag = enter.getKey();
                        String tags = coloursList.get(tag);

                        ContentValues contentValues = new ContentValues();

                        contentValues.put("_tag_", tag);
                        contentValues.put("_colour_", tags);

                        db.insert("colours", null, contentValues);
                    }

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_activity_", "Main");
                    contentValues.put("_rows_", 10);
                    contentValues.put("_columns_", 5);
                    contentValues.put("_size_", 11);
                    db.insert("zoom", null, contentValues);

                    contentValues = new ContentValues();
                    contentValues.put("_activity_", "Report");
                    contentValues.put("_rows_", 100);
                    contentValues.put("_columns_", 1);
                    contentValues.put("_size_", 11);
                    db.insert("zoom", null, contentValues);

                    ArrayList<Pair<String, String>> myPrefixes = new ArrayList<>();
                    myPrefixes.add(new Pair<>("AB", ""));
                    myPrefixes.add(new Pair<>("UN", ""));
                    myPrefixes.add(new Pair<>("DE", ""));
                    myPrefixes.add(new Pair<>("IN", ""));
                    myPrefixes.add(new Pair<>("RE", ""));
                    myPrefixes.add(new Pair<>("IM", ""));
                    myPrefixes.add(new Pair<>("IL", "L"));
                    myPrefixes.add(new Pair<>("IR", "R"));
                    myPrefixes.add(new Pair<>("DIS", ""));
                    myPrefixes.add(new Pair<>("MIS", ""));
                    myPrefixes.add(new Pair<>("NON", ""));
                    myPrefixes.add(new Pair<>("BI", ""));
                    myPrefixes.add(new Pair<>("DI", ""));
                    myPrefixes.add(new Pair<>("TRI", ""));
                    myPrefixes.add(new Pair<>("BE", ""));
                    myPrefixes.add(new Pair<>("OUT", ""));
                    myPrefixes.add(new Pair<>("OVER", ""));
                    myPrefixes.add(new Pair<>("SUB", ""));
                    myPrefixes.add(new Pair<>("CO", ""));
                    myPrefixes.add(new Pair<>("UP", ""));
                    myPrefixes.add(new Pair<>("DOWN", ""));
                    myPrefixes.add(new Pair<>("OFF", ""));
                    myPrefixes.add(new Pair<>("ANTI", ""));
                    myPrefixes.add(new Pair<>("SEMI", ""));
                    myPrefixes.add(new Pair<>("TRANS", ""));
                    myPrefixes.add(new Pair<>("GRAND", ""));
                    myPrefixes.add(new Pair<>("MULTI", ""));
                    myPrefixes.add(new Pair<>("INTER", ""));
                    myPrefixes.add(new Pair<>("INTRA", ""));
                    myPrefixes.add(new Pair<>("SUPER", ""));
                    myPrefixes.add(new Pair<>("UNDER", ""));
                    myPrefixes.add(new Pair<>("UNI", ""));
                    myPrefixes.add(new Pair<>("SOME", ""));
                    myPrefixes.add(new Pair<>("BACK", ""));
                    myPrefixes.add(new Pair<>("PRE", ""));
                    myPrefixes.add(new Pair<>("MID", ""));
                    myPrefixes.add(new Pair<>("MONO", ""));
                    myPrefixes.add(new Pair<>("MINI", ""));
                    myPrefixes.add(new Pair<>("POLY", ""));
                    myPrefixes.add(new Pair<>("POST", ""));
                    myPrefixes.add(new Pair<>("FORE", ""));
                    myPrefixes.add(new Pair<>("SIDE", ""));
                    myPrefixes.add(new Pair<>("AUTO", ""));
                    myPrefixes.add(new Pair<>("ORTHO", ""));
                    myPrefixes.add(new Pair<>("PARA", ""));
                    myPrefixes.add(new Pair<>("META", ""));
                    myPrefixes.add(new Pair<>("ISO", ""));
                    myPrefixes.add(new Pair<>("HOMO", ""));
                    myPrefixes.add(new Pair<>("HOMEO", ""));
                    myPrefixes.add(new Pair<>("HETERO", ""));

                    for (Pair<String, String> columnItem : myPrefixes) {
                        ContentValues prefixValues = new ContentValues();
                        prefixValues.put("_prefix_", columnItem.first);
                        prefixValues.put("_before_", columnItem.second);
                        db.insert("prefixes", null, prefixValues);
                    }

                    ArrayList<Pair<String, String>> mySuffixes = new ArrayList<>();
                    mySuffixes.add(new Pair<>("S", ""));
                    mySuffixes.add(new Pair<>("ES", ""));
                    mySuffixes.add(new Pair<>("-IES", "Y"));
                    mySuffixes.add(new Pair<>("+IES", ""));
                    mySuffixes.add(new Pair<>("ED", ""));
                    mySuffixes.add(new Pair<>("-ED", "E"));
                    mySuffixes.add(new Pair<>("-IED", "Y"));
                    mySuffixes.add(new Pair<>("+IED", ""));
                    mySuffixes.add(new Pair<>("+ED", ""));
                    mySuffixes.add(new Pair<>("ING", ""));
                    mySuffixes.add(new Pair<>("-ING", "E"));
                    mySuffixes.add(new Pair<>("+ING", ""));
                    mySuffixes.add(new Pair<>("ION", ""));
                    mySuffixes.add(new Pair<>("-ION", "EY"));
                    mySuffixes.add(new Pair<>("+ION", ""));
                    mySuffixes.add(new Pair<>("Y", ""));
                    mySuffixes.add(new Pair<>("-Y", "E"));
                    mySuffixes.add(new Pair<>("+Y", ""));
                    mySuffixes.add(new Pair<>("LY", ""));
                    mySuffixes.add(new Pair<>("-ILY", "Y"));
                    mySuffixes.add(new Pair<>("ER", ""));
                    mySuffixes.add(new Pair<>("-ER", "E"));
                    mySuffixes.add(new Pair<>("+ER", ""));
                    mySuffixes.add(new Pair<>("EST", ""));
                    mySuffixes.add(new Pair<>("-EST", "E"));
                    mySuffixes.add(new Pair<>("+EST", ""));
                    mySuffixes.add(new Pair<>("IER", ""));
                    mySuffixes.add(new Pair<>("-IER", "EY"));
                    mySuffixes.add(new Pair<>("+IER", ""));
                    mySuffixes.add(new Pair<>("IEST", ""));
                    mySuffixes.add(new Pair<>("-IEST", "EY"));
                    mySuffixes.add(new Pair<>("+IEST", ""));
                    mySuffixes.add(new Pair<>("FUL", ""));
                    mySuffixes.add(new Pair<>("-FUL", ""));
                    mySuffixes.add(new Pair<>("-IFUL", "Y"));
                    mySuffixes.add(new Pair<>("FULLY", ""));
                    mySuffixes.add(new Pair<>("-FULLY", ""));
                    mySuffixes.add(new Pair<>("-IFULLY", "Y"));
                    mySuffixes.add(new Pair<>("LESS", ""));
                    mySuffixes.add(new Pair<>("-ILESS", "Y"));
                    mySuffixes.add(new Pair<>("NESS", ""));
                    mySuffixes.add(new Pair<>("-INESS", "Y"));
                    mySuffixes.add(new Pair<>("ABLE", ""));
                    mySuffixes.add(new Pair<>("-ABLE", "E"));
                    mySuffixes.add(new Pair<>("-IABLE", "Y"));
                    mySuffixes.add(new Pair<>("ABLY", ""));
                    mySuffixes.add(new Pair<>("-ABLY", "E"));
                    mySuffixes.add(new Pair<>("-IABLY", "Y"));
                    mySuffixes.add(new Pair<>("LIKE", ""));
                    mySuffixes.add(new Pair<>("AGE", ""));
                    mySuffixes.add(new Pair<>("LET", ""));
                    mySuffixes.add(new Pair<>("ISH", ""));
                    mySuffixes.add(new Pair<>("-ISH", "E"));
                    mySuffixes.add(new Pair<>("+ISH", ""));
                    mySuffixes.add(new Pair<>("IST", ""));
                    mySuffixes.add(new Pair<>("-IST", "EO"));
                    mySuffixes.add(new Pair<>("+IST", ""));
                    mySuffixes.add(new Pair<>("ISM", ""));
                    mySuffixes.add(new Pair<>("-ISM", "EO"));
                    mySuffixes.add(new Pair<>("+ISM", ""));
                    mySuffixes.add(new Pair<>("DOM", ""));
                    mySuffixes.add(new Pair<>("SHIP", ""));
                    mySuffixes.add(new Pair<>("HOOD", ""));
                    mySuffixes.add(new Pair<>("UP", ""));
                    mySuffixes.add(new Pair<>("DOWN", ""));
                    mySuffixes.add(new Pair<>("OFF", ""));
                    mySuffixes.add(new Pair<>("WARD", ""));
                    mySuffixes.add(new Pair<>("SOME", ""));
                    mySuffixes.add(new Pair<>("MAN", ""));
                    mySuffixes.add(new Pair<>("WOMAN", ""));
                    mySuffixes.add(new Pair<>("MEN", ""));
                    mySuffixes.add(new Pair<>("WOMEN", ""));
                    mySuffixes.add(new Pair<>("MENT", ""));
                    mySuffixes.add(new Pair<>("-MENT", "E"));
                    mySuffixes.add(new Pair<>("-IMENT", "Y"));
                    mySuffixes.add(new Pair<>("BACK", ""));
                    mySuffixes.add(new Pair<>("FOLD", ""));
                    mySuffixes.add(new Pair<>("OUT", ""));
                    mySuffixes.add(new Pair<>("OVER", ""));
                    mySuffixes.add(new Pair<>("UNDER", ""));
                    mySuffixes.add(new Pair<>("BOY", ""));
                    mySuffixes.add(new Pair<>("SIDE", ""));
                    mySuffixes.add(new Pair<>("WISE", ""));
                    mySuffixes.add(new Pair<>("AL", ""));
                    mySuffixes.add(new Pair<>("UAL", ""));
                    mySuffixes.add(new Pair<>("+AL", ""));
                    mySuffixes.add(new Pair<>("-AL", "AE"));
                    mySuffixes.add(new Pair<>("-IAL", "Y"));
                    mySuffixes.add(new Pair<>("ALLY", ""));
                    mySuffixes.add(new Pair<>("UALLY", ""));
                    mySuffixes.add(new Pair<>("+ALLY", ""));
                    mySuffixes.add(new Pair<>("-ALLY", "AE"));
                    mySuffixes.add(new Pair<>("-IALLY", "Y"));
                    mySuffixes.add(new Pair<>("IC", ""));
                    mySuffixes.add(new Pair<>("+IC", ""));
                    mySuffixes.add(new Pair<>("-IC", "EY"));
                    mySuffixes.add(new Pair<>("ICAL", ""));
                    mySuffixes.add(new Pair<>("+ICAL", ""));
                    mySuffixes.add(new Pair<>("-ICAL", "EY"));
                    mySuffixes.add(new Pair<>("ICALLY", ""));
                    mySuffixes.add(new Pair<>("+ICALLY", ""));
                    mySuffixes.add(new Pair<>("-ICALLY", "EY"));
                    mySuffixes.add(new Pair<>("+IFUL", ""));
                    mySuffixes.add(new Pair<>("+IFULLY", ""));
                    mySuffixes.add(new Pair<>("+ILESS", ""));
                    mySuffixes.add(new Pair<>("+INESS", ""));
                    mySuffixes.add(new Pair<>("+IABLE", ""));
                    mySuffixes.add(new Pair<>("+IABLY", ""));
                    mySuffixes.add(new Pair<>("+IMENT", ""));
                    mySuffixes.add(new Pair<>("+IAL", ""));
                    mySuffixes.add(new Pair<>("OR", ""));
                    mySuffixes.add(new Pair<>("+OR", ""));
                    mySuffixes.add(new Pair<>("-OR", "E"));
                    mySuffixes.add(new Pair<>("IOR", ""));
                    mySuffixes.add(new Pair<>("+IOR", ""));
                    mySuffixes.add(new Pair<>("-IOR", "EY"));
                    mySuffixes.add(new Pair<>("OUR", ""));
                    mySuffixes.add(new Pair<>("+OUR", ""));
                    mySuffixes.add(new Pair<>("-OUR", "E"));
                    mySuffixes.add(new Pair<>("IOUR", ""));
                    mySuffixes.add(new Pair<>("+IOUR", ""));
                    mySuffixes.add(new Pair<>("-IOUR", "EY"));

                    for (Pair<String, String> columnItem : mySuffixes) {
                        ContentValues suffixValues = new ContentValues();
                        suffixValues.put("_suffix_", columnItem.first);
                        suffixValues.put("_after_", columnItem.second);
                        db.insert("suffixes", null, suffixValues);
                    }

                    db.execSQL("UPDATE words SET _serial_ = ((_page_ - 1) * 100) + _position_");
                    uiThreadRefresh(myContext, yourParent, true);
                    uiThreadBox("Prepare regular database", "Regular database preparation complete.", myContext, yourParent);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    myDialog.dismiss();
                } finally {
                    myDialog.dismiss();
                    db.endTransaction();
                }
            }
        });

        thread5.start();
        return success[0];
    }

    public ArrayList<Integer> getZoom(String parentActivity)
    {
        ArrayList<Integer> zoomList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _rows_, _columns_, _size_ FROM zoom WHERE _activity_ = \"" + parentActivity + "\"", null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int rows = cursor.getInt(0);
                    int dimensions = cursor.getInt(1);
                    int font = cursor.getInt(2);

                    zoomList.add(rows);
                    zoomList.add(dimensions);
                    zoomList.add(font);
                } while (cursor.moveToNext());
            }
        }
        else if (parentActivity.equals("Report")) {
            zoomList.add(100);
            zoomList.add(1);
            zoomList.add(11);
        }
        else {
            zoomList.add(10);
            zoomList.add(5);
            zoomList.add(11);
        }

        cursor.close();
        return zoomList;
    }

    public long setZoom(String parentActivity, int rows, int dimensions, int font)
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

        if (exists != 0) {
            return db.update("zoom", values, "_activity_ = ?",
                    new String[] {parentActivity});
        }
        else {
            values.put("_activity_", parentActivity);
            return db.insert("zoom", null, values);
        }
    }

    public long setMagnify(String parentActivity, int rows, int font)
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

        if (exists != 0) {
            return db.update("zoom", values, "_activity_ = ?",
                    new String[]{parentActivity});
        }
        else {
            values.put("_activity_", parentActivity);
            return db.insert("zoom", null, values);
        }
    }

    public boolean insertLabel(int letters, String label, String orderBy)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

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
        contentValues.put("_blank_", 0);
        contentValues.put("_sort_", orderBy);

        db.insert("scores", null, contentValues);
        return true;
    }

    public int[] getCustomScore(String customQuery, int solvedStatus)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(_solved_), COUNT(_word_) FROM words WHERE _alphagram_ IN (SELECT _alphagram_ FROM words WHERE " + customQuery + " GROUP BY _alphagram_" + solvedCondition(solvedStatus) + ")", null);

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

    public int[] getScore(int letters, String label, int solvedStatus)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(_solved_), COUNT(_word_) FROM words WHERE _alphagram_ IN (SELECT _alphagram_ FROM words" + ((letters > 1 || !label.equals("*")) ? " WHERE " : "") + (letters > 1 ? "_length_ = " + letters : "") + ((letters > 1 && !label.equals("*")) ? " AND " : "") + (!label.equals("*") ? "_tag_ = \"" + label + "\"" : "") + " GROUP BY _alphagram_" + solvedCondition(solvedStatus) + ")", null);

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

    public int getCounter(int letters, String label, int solvedStatus, String orderBy)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + quizCondition(solvedStatus) + " FROM scores WHERE _blank_ = 0 AND _length_ = " + letters + " AND _query_ = \"" + label + "\" AND _sort_ = \"" + orderBy + "\"", null);

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

    public Cursor getAllAnagrams(int letters, String label, int solvedStatus, String orderBy)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        if(orderBy.charAt(0) == ' ')
        {
            return db.rawQuery("SELECT _alphagram_ FROM words" + ((letters > 1 || !label.equals("*")) ? " WHERE " : "") + (letters > 1 ? "_length_ = " + letters : "") + ((letters > 1 && !label.equals("*")) ? " AND " : "") + (!label.equals("*") ? "_tag_ = \"" + label + "\"" : "") + " GROUP BY _alphagram_" + solvedCondition(solvedStatus) + orderBy, null);
        }
        else
        {
            return db.rawQuery("SELECT DISTINCT(_alphagram_) FROM words" + ((letters > 1 || !label.equals("*")) ? " WHERE " : "") + (letters > 1 ? "_length_ = " + letters : "") + ((letters > 1 && !label.equals("*")) ? " AND " : "") + (!label.equals("*") ? "_tag_ = \"" + label + "\"" : "") + solvedCondition(solvedStatus), null);
        }
    }

    public Cursor getCustomQuiz(String customQuery, Context activity, int solvedStatus, String orderBy)
    {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            if(orderBy.charAt(0) == ' ')
            {
                return db.rawQuery("SELECT _alphagram_ FROM words WHERE " + customQuery + " GROUP BY _alphagram_" + solvedCondition(solvedStatus) + orderBy, null);
            }
            else
            {
                return db.rawQuery("SELECT DISTINCT(_alphagram_) FROM words WHERE " + customQuery + solvedCondition(solvedStatus), null);
            }
        }
        catch (SQLiteException e) {
            alertBox("Error", e.toString(), activity);
            return null;
        }
    }

    public Cursor getSolvedWords(int letters, int solvedStatus, String orderBy)
    {
        String status = "";
        if (solvedStatus == 0)
        {
            status = (letters >= 0 ? " AND " : "") + "_solved_ = 1";
        }
        else if (solvedStatus == 1)
        {
            status = (letters >= 0 ? " AND " : "") + "_solved_ = 0";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _word_ FROM words" + (letters >= 0 || solvedStatus < 2 ? " WHERE " : "") + (letters >= 0 ? "_length_ = " + letters : "") + status + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);
    }

    public Cursor getLabelledWords(int letters, String label, int solvedStatus, String orderBy)
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
        return db.rawQuery("SELECT _word_ FROM words WHERE " + (letters >= 0 ? "_length_ = " + letters + " AND " : "") + status + "_tag_ = \"" + label + "\"" + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);
    }

    public Cursor getSqlQuery(String query, Context activity, int solvedStatus, String orderBy)
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
            return db.rawQuery("SELECT _word_ FROM words WHERE " + status + query + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);
        }
        catch (SQLiteException e) {
            alertBox("Error", e.toString(), activity);
            return null;
        }
    }

    public HashMap<String, ArrayList<String>> getUnsolvedAnswers(ArrayList<String> jumbles)
    {
        HashMap<String, ArrayList<String>> answerList = new HashMap<>();
        String jumble = (((jumbles.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _alphagram_, _word_ FROM words WHERE _alphagram_ IN " + jumble + " AND _solved_ = 0", null);

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
        return answerList;
    }

    public HashMap<String, Integer> getAllAnswers(ArrayList<String> jumbles)
    {
        HashMap<String, Integer> allList = new HashMap<>();

        String jumble = (((jumbles.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _alphagram_, COUNT(_word_) FROM words WHERE _alphagram_ IN " + jumble + " GROUP BY _alphagram_", null);

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

    public String getSolvedAnswers(String jumble)
    {
        StringBuilder solved = new StringBuilder();
        int total = 1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _word_, _definition_, _back_, _front_, _tag_, _length_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_ FROM words WHERE _alphagram_ = \"" + jumble + "\" AND _solved_ = 1 ORDER BY _time_", null);

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
                        solved.append("<font color=\"").append(colour).append("\">").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.length() == 0 ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b></font>");
                    } else {
                        solved.append("<br><font color=\"").append(colour).append("\">").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.length() == 0 ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b></font>");
                    }
                } else {
                    if (total == 1) {
                        solved.append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.length() == 0 ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b>");
                    } else {
                        solved.append("<br>").append(total).append(". <b><small>").append(front).append("</small> ").append(data).append(" <small>").append(back).append("</small></b> ").append(definition).append(" <b>").append(label.length() == 0 ? "(No Tag)" : label).append(" ").append(dictionaryList.get(0)).append(" ").append(dictionaryList.get(1)).append(" ").append(dictionaryList.get(2)).append(" ").append(dictionaryList.get(3)).append("</b>");
                    }
                }

                total++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return new String(solved);
    }

    public String getUnsolvedWords(String unsolved)
    {
        StringBuilder unsolvedAnswers = new StringBuilder();
        int total = 1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _word_, _definition_, _back_, _front_, _length_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_ FROM words WHERE _alphagram_ = \"" + unsolved + "\" AND _solved_ = 0", null);

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

    public ArrayList<String> getDefinition(String guess)
    {
        ArrayList<String> hookList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _definition_, _back_, _front_, _length_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_, _tag_ FROM words WHERE _word_ = \"" + guess + "\"", null);

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

    public int getPage(int letters, String label, int solvedStatus, String orderBy)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + reportCondition(solvedStatus) + " FROM scores WHERE _blank_ = 0 AND _length_ = " + letters + " AND _query_ = \"" + label + "\" AND _sort_ = \"" + orderBy + "\"", null);

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

    public int resetLabel(String label, int lengthIndex, int letters, boolean timeIndex) {
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

        return db.update("words", values, new String(whereClause),
                new String[] {});
    }

    public int updateCounter(int letters, String label, int counter, int solvedStatus, String orderBy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(quizCondition(solvedStatus), counter);

        return db.update("scores", values, "_length_ = ? AND _query_ = ? AND _blank_ = ? AND _sort_ = ?",
                new String[] {Integer.toString(letters), label, "0", orderBy});
    }

    public int updatePage(int letters, String label, int counter, int solvedStatus, String orderBy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(reportCondition(solvedStatus), counter);

        return db.update("scores", values, "_length_ = ? AND _query_ = ? AND _blank_ = ? AND _sort_ = ?",
                new String[] {Integer.toString(letters), label, "0", orderBy});
    }

    public int updateTime(ArrayList<String> guesses, double time, int solved) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_time_", time);
        values.put("_solved_", solved);

        if (solved == 1)
        {
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat iso8601Format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String simpleDateFormat = iso8601Format.format(timestamp);
            values.put("_timestamp_", simpleDateFormat);
        }

        String guess = (((guesses.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        return db.update("words", values, "_word_ IN " + guess,
                new String[] {});
    }

    public void updateCustomTime(ArrayList<String> guesses, double time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String guess = (((guesses.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        db.execSQL("UPDATE words SET _time_ = _time_ + " + time + " WHERE _word_ IN " + guess);
    }

    public int updateLabel(ArrayList<String> guesses, double time, int solved, String cardbox) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_time_", time);
        values.put("_solved_", solved);
        values.put("_tag_", cardbox);

        long timestamp = System.currentTimeMillis();
        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String simpleDateFormat = iso8601Format.format(timestamp);
        values.put("_timestamp_", simpleDateFormat);

        String guess = (((guesses.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        return db.update("words", values, "_word_ IN " + guess,
                new String[] {});
    }

    public int updateTag(String guess, String tag)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_tag_", tag);

        return db.update("words", values, "_word_ = ?",
                new String[] {guess});
    }

    public int updateWord(String line, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_tag_", category);

        return db.update("words", values, "_word_ = ?",
                new String[] {line});
    }

    public double getTime(String guess)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _time_ FROM words WHERE _word_ = \"" + guess + "\"", null);

        String data = null;

        if (cursor.moveToFirst()) {
            do {
                data = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return Double.parseDouble(data);
    }

    public boolean existLabel(int letters, String label, String orderBy)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS(SELECT 1 FROM scores WHERE _blank_ = 0 AND _length_ = " + letters + " AND _query_ = \"" + label + "\" AND _sort_ = \"" + orderBy + "\")", null);

        int exists = 0;

        if (cursor.moveToFirst()) {
            do {
                exists = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return (exists != 0);
    }

    public String getSummary(ArrayList<String> guesses)
    {
        String guess = (((guesses.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _front_, _word_, _back_, _tag_ FROM words WHERE _alphagram_ IN " + guess + " AND _solved_ = 1", null);

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
                    revision.append("<font color=\"").append(colour).append("\"><b>").append(key.length() == 0 ? "(No Tag)" : key).append(": ").append(aerolith).append("</b></font>");
                } else {
                    revision.append("<br><font color=\"").append(colour).append("\"><b>").append(key.length() == 0 ? "(No Tag)" : key).append(": ").append(aerolith).append("</b></font>");
                }
            } else {
                if (serial == 0) {
                    revision.append("<b>").append(key.length() == 0 ? "(No Tag)" : key).append(": ").append(aerolith).append("</b>");
                } else {
                    revision.append("<br><b>").append(key.length() == 0 ? "(No Tag)" : key).append(": ").append(aerolith).append("</b>");
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

            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
            });
        }
        else
        {
            Report homeActivity = (Report) location;

            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        dialog.show();
    }

    public void uiThreadTitle(String title, AlertDialog theDialog, Context location, boolean parent)
    {
        if (parent)
        {
            MainActivity homeActivity = (MainActivity) location;

            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    theDialog.setTitle(title);
                }
            });
        }
        else
        {
            Report homeActivity = (Report) location;

            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    theDialog.setTitle(title);
                }
            });
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
        argumentQuery = argumentQuery.replace("_time_stamp", "_timestamp_");
        argumentQuery = argumentQuery.replace("un_solved_", "_unsolved_");
        argumentQuery = argumentQuery.replace("in_complete_", "_incomplete_");
        argumentQuery = argumentQuery.replace("ze_tag_ram", "_zetagram_");
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
        ArrayList<String> finalQuery = new ArrayList();
        for (int columnsArray = 0; columnsArray < lastQuery.length; columnsArray++)
        {
            finalQuery.add(columnsArray % 2 == 0 ? lastQuery[columnsArray] : secondQuery[columnsArray]);
        }
        StringBuilder ultimateQuery = new StringBuilder();
        ultimateQuery.append(finalQuery.get(0));
        for (int rowName = 1; rowName < finalQuery.size(); rowName++)
        {
            ultimateQuery.append("'" + finalQuery.get(rowName));
        }
        String returnQuery = (new String(ultimateQuery)).trim();

        return returnQuery;
    }

    public ArrayList<String> extract(String jumbleList, int start, String orderBy)
    {
        ArrayList<String> wordList = new ArrayList<>();

        int order = start;
        HashMap<String, String> coloursMap = getColours();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _word_, _definition_, _time_, _back_, _front_, _answers_, _page_, _alphagram_, _timestamp_, _csw24_, _csw19_, _csw15_, _csw12_, _csw07_, _nwl23_, _nwl18_, _twl06_, _nswl23_, _wims_, _cel21_, _incorrect_, _wrong_, _tag_ FROM words WHERE _word_ IN " + jumbleList + (orderBy.charAt(0) == ' ' ? orderBy : ""), null);

        if (orderBy.equals("DESC") ? cursor.moveToLast() : cursor.moveToFirst()) {
            do {
                String data = cursor.getString(0);
                String definition = cursor.getString(1);
                String time = cursor.getString(2);
                String back = cursor.getString(3);
                String front = cursor.getString(4);
                String answers = cursor.getString(5);
                String page = cursor.getString(6);
                String alphagram = cursor.getString(7);
                String timestamp = cursor.getString(8);
                int csw24 = cursor.getInt(9);
                int csw19 = cursor.getInt(10);
                int csw15 = cursor.getInt(11);
                int csw12 = cursor.getInt(12);
                int csw07 = cursor.getInt(13);
                int nwl23 = cursor.getInt(14);
                int nwl18 = cursor.getInt(15);
                int twl06 = cursor.getInt(16);
                int nswl23 = cursor.getInt(17);
                int wims = cursor.getInt(18);
                int cel21 = cursor.getInt(19);
                String incorrect = cursor.getString(20);
                String wrong = cursor.getString(21);
                String label = cursor.getString(22);

                ArrayList<String> lexicons = dictionaries(data.length(), csw24, csw19, csw15, csw12, csw07, nwl23, nwl18, twl06, nswl23, wims, cel21);

                if (coloursMap.containsKey(label) || coloursMap.containsKey("")) {
                    String colourMap = (coloursMap.containsKey(label) ? coloursMap.get(label) : coloursMap.get(""));

                    wordList.add("<font color=\"" + colourMap + "\">" + order + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b><small>" + front + "</small></b></font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b>" + data + "</b></font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b><small>" + back + "</small></b></font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + answers + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b>" + alphagram + "</b></font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + time + " seconds</font>");
                    wordList.add("<font color=\"" + colourMap + "\">Page " + page + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\"><b>" + (label.length() == 0 ? "(No Tag)" : label) + "</b></font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(0) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(1) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(2) + "</font>");
                    wordList.add("<font color=\"" + colourMap + "\">" + lexicons.get(3) + "</font>");
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
                    wordList.add(time + " seconds");
                    wordList.add("Page " + page);
                    wordList.add("<b>" + (label.length() == 0 ? "(No Tag)" : label) + "</b>");
                    wordList.add(lexicons.get(0));
                    wordList.add(lexicons.get(1));
                    wordList.add(lexicons.get(2));
                    wordList.add(lexicons.get(3));
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

    public int trackWrongAnswers(String wrongAlphagram, String wrongWord)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _incorrect_, _wrong_ FROM words WHERE _alphagram_ = \"" + wrongAlphagram + "\"", null);

        int first = 0;
        String second = null;

        if (cursor.moveToFirst()) {
            first = cursor.getInt(0);
            second = cursor.getString(1);
        }

        cursor.close();

        List<String> third = Arrays.asList(second.split(", "));
        ContentValues values = new ContentValues();
        values.put("_incorrect_", first + 1);
        if (!third.contains(wrongWord)) {
            values.put("_wrong_", second.length() == 0 ? wrongWord : second + ", " + wrongWord);
        }

        return db.update("words", values, "_alphagram_ = ?",
                new String[] {wrongAlphagram});
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

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.refresh();
                    if (prepared) {
                        mainActivity.setPrepared();
                    }
                }
            });
        }
        else
        {
            Report report = (Report) theContext;

            report.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    report.refresh();
                    if (prepared) {
                        report.setPrepared();
                    }
                }
            });
        }
    }

    public void resetByLabel(Context theContext, boolean parent)
    {
        LayoutInflater inflater = LayoutInflater.from(theContext);
        final View yourCustomView = inflater.inflate(R.layout.reset, null);

        EditText e4 = yourCustomView.findViewById(R.id.edittext21);
        EditText e5 = yourCustomView.findViewById(R.id.edittext22);
        TextView t5 = yourCustomView.findViewById(R.id.textview36);

        e5.setHint("Enter a value between 2 and 58");

        Spinner s1 = yourCustomView.findViewById(R.id.spinner11);
        List<Pair<String, String>> tagsList = getAllLabels();
        tagsList.add(0, new Pair<>("(All Tags)", null));

        ColourAdapter spinnerAdapter = new ColourAdapter(theContext, R.layout.colour, R.id.textview62, tagsList, parent ? (MainActivity) theContext : (Report) theContext, true);
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

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, lengthList);
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

        ArrayAdapter<String> timeAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, timeList);
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String myLabel = (e4.getText()).toString();
                        String alphabets = (e5.getText()).toString();
                        int temporary = (alphabets.length() == 0 ? 0 : Integer.parseInt(alphabets));
                        resetLabel(myLabel, lengthIndex[0], temporary, timeIndex[0]);
                        refresh(theContext, parent);
                    }
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

        z1.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rgb[0] = (int) value;
                t7.setText(Integer.toString(rgb[0]));
                hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                t6.setText(hexValue[0]);
                t6.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        z2.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rgb[1] = (int) value;
                t8.setText(Integer.toString(rgb[1]));
                hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                t6.setText(hexValue[0]);
                t6.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        z3.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rgb[2] = (int) value;
                t9.setText(Integer.toString(rgb[2]));
                hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                t6.setText(hexValue[0]);
                t6.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t10.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle("Add new tag")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        boolean transaction = addLabel((e6.getText()).toString(), hexValue[0]);
                        refresh(theContext, parent);
                    }
                }).create();
        dialog.show();
    }

    public void renameByLabel(Context theContext, boolean parent, boolean name)
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
        ColourAdapter colourAdapter = new ColourAdapter(theContext, R.layout.colour, R.id.textview62, spinnerList, parent ? (MainActivity) theContext : (Report) theContext, name);
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

        z4.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rgb[0] = (int) value;
                t15.setText(Integer.toString(rgb[0]));
                hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                t14.setText(hexValue[0]);
                t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        z5.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rgb[1] = (int) value;
                t16.setText(Integer.toString(rgb[1]));
                hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                t14.setText(hexValue[0]);
                t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        z6.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rgb[2] = (int) value;
                t17.setText(Integer.toString(rgb[2]));
                hexValue[0] = String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
                t14.setText(hexValue[0]);
                t14.setTextColor(hexValue[0].equals(white) ? grey : Color.rgb(rgb[0], rgb[1], rgb[2]));
                t18.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(name ? "Change tag colour by name" : "Rename tag by colour")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int result = renameLabel(old[0], (e7.getText()).toString(), hexValue[0], name);
                        refresh(theContext, parent);
                    }
                }).create();
        dialog.show();
    }

    public void deleteByLabel(Context theContext, boolean parent, boolean name)
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
        ColourAdapter colourAdapter = new ColourAdapter(theContext, R.layout.colour, R.id.textview62, spinnerList, parent ? (MainActivity) theContext : (Report) theContext, name);
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int result = deleteLabel(old[0], name);
                        refresh(theContext, parent);
                    }
                }).create();
        dialog.show();
    }

    public List<Pair<String, String>> getAllPrefixes()
    {
        ArrayList<Pair<String, String>> prefixesList = new ArrayList();

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
        ArrayList<Pair<String, String>> suffixesList = new ArrayList();

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
        Cursor cursor1 = db.rawQuery("SELECT _prefix_ FROM prefixes ORDER BY _prefix_", null);

        if (cursor1.moveToFirst()) {
            do {
                String thePrefix = cursor1.getString(0);
                prefixList.add(thePrefix.length() == 0 ? "(None)" : cursor1.getString(0));
            } while (cursor1.moveToNext());
        }

        cursor1.close();

        Cursor cursor2 = db.rawQuery("SELECT _suffix_ FROM suffixes ORDER BY _suffix_", null);

        if (cursor2.moveToFirst()) {
            do {
                String theSuffix = cursor2.getString(0);
                suffixList.add(theSuffix.length() == 0 ? "(None)" : cursor2.getString(0));
            } while (cursor2.moveToNext());
        }

        cursor2.close();

        String prefixes = prefixList.toString();
        String suffixes = suffixList.toString();

        messageBox("View all prefixes and suffixes", "<b>Prefixes:</b> " + prefixes.substring(1, prefixes.length() - 1) + "<br><b>Suffixes:</b> " + suffixes.substring(1, suffixes.length() - 1), theContext);
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

        ArrayAdapter<String> beforeAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, beforeList);
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

        ArrayAdapter<String> afterAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, afterList);
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (suffix)
                        {
                            boolean transaction = addPrefix((variable[0] == 0 ? "" : (variable[0] == 1 ? "-" : "+")) + ((e8.getText()).toString()).toUpperCase(), variable[1] == 0 ? "" : ((e9.getText()).toString()).toUpperCase(), suffix);
                        }
                        else
                        {
                            boolean transaction = addPrefix(((e8.getText()).toString()).toUpperCase() + (variable[0] == 0 ? "" : (variable[0] == 1 ? "-" : "+")), variable[1] == 0 ? "" : ((e9.getText()).toString()).toUpperCase(), suffix);
                        }

                        if (parent && mode == 1) {
                            MainActivity home = (MainActivity) theContext;
                            home.refreshDefinition();
                        }
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
        final int variable[] = {0, 0, 0};

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

        ArrayAdapter<String> queryAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, queryList);
        queryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s8.setAdapter(queryAdapter);

        s8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                variable[0] = i;
                String prefix = queryList.get(i);
                String myColour = (insertList.get(i)).second;

                s10.setSelection(myColour.length() == 0 ? 0 : 1);
                e11.setText(myColour);

                if (suffix)
                {
                    t25.setText((prefix.length() > 0 && prefix.charAt(0) == '+') ? (prefix.length() > 1 ? "Double last letter, " : "Double last letter") + prefix.substring(1) : ((prefix.length() > 0 && prefix.charAt(0) == '-') ? (prefix.length() > 1 ? "Drop last letter, " : "Drop last letter") + prefix.substring(1) : (prefix.length() > 0 ? "No changes to word, " : "No changes to word") + prefix));
                    t27.setText(myColour.length() == 0 ? "(After all last letters)" : myColour);

                    s9.setSelection((prefix.length() > 0 && prefix.charAt(0) == '-') ? 1 : ((prefix.length() > 0 && prefix.charAt(0) == '+') ? 2 : 0));
                    e10.setText((prefix.length() > 0 && (prefix.charAt(0) == '+' || prefix.charAt(0) == '-')) ? prefix.substring(1) : prefix);
                }
                else
                {
                    int variables = prefix.length() - 1;
                    t25.setText((prefix.length() > 0 && prefix.charAt(variables) == '+') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", double first letter" : "Double first letter") : ((prefix.length() > 0 && prefix.charAt(variables) == '-') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", drop first letter" : "Drop first letter") : prefix + (prefix.length() > 0 ? ", no changes to word" : "No changes to word")));
                    t27.setText(myColour.length() == 0 ? "(Before all first letters)" : myColour);

                    s9.setSelection((prefix.length() > 0 && prefix.charAt(variables) == '-') ? 1 : ((prefix.length() > 0 && prefix.charAt(variables) == '+') ? 2 : 0));
                    e10.setText((prefix.length() > 0 && (prefix.charAt(variables) == '+' || prefix.charAt(variables) == '-')) ? prefix.substring(0, variables) : prefix);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> beforeAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, beforeList);
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

        ArrayAdapter<String> afterAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, afterList);
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (suffix)
                        {
                            int result = changePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, (variable[1] == 0 ? "" : (variable[1] == 1 ? "-" : "+")) + ((e10.getText()).toString()).toUpperCase(), variable[2] == 0 ? "" : ((e11.getText()).toString()).toUpperCase(), suffix);
                        }
                        else
                        {
                            int result = changePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, ((e10.getText()).toString()).toUpperCase() + (variable[1] == 0 ? "" : (variable[1] == 1 ? "-" : "+")), variable[2] == 0 ? "" : ((e11.getText()).toString()).toUpperCase(), suffix);
                        }

                        if (parent && mode == 1) {
                            MainActivity home = (MainActivity) theContext;
                            home.refreshDefinition();
                        }
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
        final int variable[] = {0};

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

        ArrayAdapter<String> queryAdapter = new ArrayAdapter(theContext, android.R.layout.simple_spinner_item, queryList);
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
                    t31.setText((prefix.length() > 0 && prefix.charAt(0) == '+') ? (prefix.length() > 1 ? "Double last letter, " : "Double last letter") + prefix.substring(1) : ((prefix.length() > 0 && prefix.charAt(0) == '-') ? (prefix.length() > 1 ? "Drop last letter, " : "Drop last letter") + prefix.substring(1) : (prefix.length() > 0 ? "No changes to word, " : "No changes to word") + prefix));
                    t33.setText(myColour.length() == 0 ? "(After all last letters)" : myColour);
                }
                else
                {
                    int variables = prefix.length() - 1;
                    t31.setText((prefix.length() > 0 && prefix.charAt(variables) == '+') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", double first letter" : "Double first letter") : ((prefix.length() > 0 && prefix.charAt(variables) == '-') ? prefix.substring(0, variables) + (prefix.length() > 1 ? ", drop first letter" : "Drop first letter") : prefix + (prefix.length() > 0 ? ", no changes to word" : "No changes to word")));
                    t33.setText(myColour.length() == 0 ? "(Before all first letters)" : myColour);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(theContext)
                .setTitle(suffix ? "Delete suffix" : "Delete prefix")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (suffix)
                        {
                            int result = deletePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, suffix);
                        }
                        else
                        {
                            int result = deletePrefix((insertList.get(variable[0])).first, (insertList.get(variable[0])).second, suffix);
                        }

                        if (parent && mode == 1) {
                            MainActivity home = (MainActivity) theContext;
                            home.refreshDefinition();
                        }
                    }
                }).create();
        dialog.show();
    }

    public boolean addPrefix(String prefix, String before, boolean suffix)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(suffix ? "_suffix_" : "_prefix_", prefix);
        contentValues.put(suffix ? "_after_" : "_before_", before);

        db.insert(suffix ? "suffixes" : "prefixes", null, contentValues);
        return true;
    }

    public int changePrefix(String myPrefix, String mySuffix, String prefix, String before, boolean suffix) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(suffix ? "_suffix_" : "_prefix_", prefix);
        values.put(suffix ? "_after_" : "_before_", before);

        return db.update(suffix ? "suffixes" : "prefixes", values, suffix ? "_suffix_ = ? AND _after_ = ?" : "_prefix_ = ? AND _before_ = ?",
                new String[] {myPrefix, mySuffix});
    }

    public int deletePrefix(String myPrefix, String mySuffix, boolean suffix) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(suffix ? "suffixes" : "prefixes", suffix ? "_suffix_ = ? AND _after_ = ?" : "_prefix_ = ? AND _before_ = ?",
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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int failure = deleteTable(theTable);
                        if (theTable.equals("colours")) {
                            refresh(theContext, parent);
                        } else {
                            if (parent && mode == 1) {
                                MainActivity home = (MainActivity) theContext;
                                home.refreshDefinition();
                            }
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        dialog.show();
    }

    public int deleteTable(String myTable)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(myTable, null, null);
    }

    public String getFullDetails(String myGuess)
    {
        StringBuilder allDetails = new StringBuilder();

        char[] charArray = myGuess.toCharArray();
        Arrays.sort(charArray);
        String myAnagram = new String(charArray);

        List<Pair<String, String>> thePrefixes = getAllPrefixes();
        ArrayList<String> thePrefix = new ArrayList<>();
        for (Pair<String, String> rowItem : thePrefixes)
        {
            String alpha = rowItem.first;
            String beta = rowItem.second;
            boolean match = (beta.length() == 0 || beta.contains(Character.toString(myGuess.charAt(0))));

            if (alpha.length() > 0 && alpha.charAt(alpha.length() - 1) == '+')
            {
                if (match)
                {
                    thePrefix.add(alpha.substring(0, alpha.length() - 1) + myGuess.charAt(0) + myGuess);
                }
            }
            else if (alpha.length() > 0 && alpha.charAt(alpha.length() - 1) == '-')
            {
                if (match)
                {
                    thePrefix.add(alpha.substring(0, alpha.length() - 1) + myGuess.substring(1));
                }
            }
            else
            {
                if (match)
                {
                    thePrefix.add(alpha + myGuess);
                }
            }
        }

        StringBuilder result1 = getFullPrefixes(thePrefix, false);
        if (result1.length() > 0) {
            allDetails.append("<br>").append("<b>Prefixes:</b> ").append(result1);
        }

        List<Pair<String, String>> theSuffixes = getAllSuffixes();
        ArrayList<String> theSuffix = new ArrayList<>();
        for (Pair<String, String> rowItem : theSuffixes)
        {
            String alpha = rowItem.first;
            String beta = rowItem.second;
            boolean mismatch = (beta.length() == 0 || beta.contains(Character.toString(myGuess.charAt(myGuess.length() - 1))));

            if (alpha.length() > 0 && alpha.charAt(0) == '+')
            {
                if (mismatch)
                {
                    theSuffix.add(myGuess + myGuess.charAt(myGuess.length() - 1) + alpha.substring(1));
                }
            }
            else if (alpha.length() > 0 && alpha.charAt(0) == '-')
            {
                if (mismatch)
                {
                    theSuffix.add(myGuess.substring(0, myGuess.length() - 1) + alpha.substring(1));
                }
            }
            else
            {
                if (mismatch)
                {
                    theSuffix.add(myGuess + alpha);
                }
            }
        }

        StringBuilder result2 = getFullPrefixes(theSuffix, false);
        if (result2.length() > 0) {
            allDetails.append("<br>").append("<b>Suffixes:</b> ").append(result2);
        }

        ArrayList<String> theAnagram = new ArrayList<>();
        theAnagram.add(myAnagram);
        StringBuilder result3 = getFullSuffixes(theAnagram, "_word_ != \"" + myGuess + "\" AND ", true);
        if (result3.length() > 0) {
            allDetails.append("<br>").append("<b>Anagrams:</b> ").append(result3);
        }

        ArrayList<String> singleLetterChange = new ArrayList<>();
        for (int myIndex = 0; myIndex < myGuess.length(); myIndex++)
        {
            singleLetterChange.add(myGuess.substring(0, myIndex) + "_" + myGuess.substring(myIndex + 1));
        }

        StringBuilder result4 = getFullSuffixes(singleLetterChange, "_word_ != \"" + myGuess + "\" AND ", false);
        if (result4.length() > 0) {
            allDetails.append("<br>").append("<b>One letter change by position:</b> ").append(result4);
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

        StringBuilder result5 = getFullPrefixes(singleLetterAdd, true);
        if (result5.length() > 0) {
            allDetails.append("<br>").append("<b>One letter drop:</b> ").append(result5);
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

        StringBuilder result6 = getFullSuffixes(oneLetterDrop, "_word_ != \"" + myGuess + "\" AND _length_ = " + myGuess.length() + " AND ", true);
        if (result6.length() > 0) {
            allDetails.append("<br>").append("<b>One letter change:</b> ").append(result6);
        }

        StringBuilder oneLetterChange = new StringBuilder();
        for (int myIndex = 0; myIndex < myAnagram.length(); myIndex++)
        {
            oneLetterChange.append("%").append(myAnagram.charAt(myIndex));
        }
        oneLetterChange.append("%");

        ArrayList<String> oneLetterAdd = new ArrayList<>();
        oneLetterAdd.add(new String(oneLetterChange));

        StringBuilder result7 = getFullSuffixes(oneLetterAdd, "_length_ = " + (myGuess.length() + 1) + " AND ", true);
        if (result7.length() > 0) {
            allDetails.append("<br>").append("<b>One letter addition:</b> ").append(result7);
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

    public StringBuilder getFullSuffixes(ArrayList<String> argument, String condition, boolean myAlphagram)
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

    public void updateProgressBar(Context yourContext, boolean parent, ProgressBar progressBar, TextView leftText, TextView rightText, AlertDialog theDialog, int percentage, String fraction)
    {
        if (parent)
        {
            MainActivity homeActivity = (MainActivity) yourContext;

            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!theDialog.isShowing()) {
                        theDialog.show();
                    }

                    progressBar.setProgress(percentage);
                    leftText.setText(percentage + "%");
                    rightText.setText(fraction);
                }
            });
        }
        else
        {
            Report homeActivity = (Report) yourContext;

            homeActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!theDialog.isShowing()) {
                        theDialog.show();
                    }

                    progressBar.setProgress(percentage);
                    leftText.setText(percentage + "%");
                    rightText.setText(fraction);
                }
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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}