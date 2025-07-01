package com.tuchwords.wordquiz;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    sqliteDB db;
    int letters = 0;
    String label = "*";
    boolean hidden;
    boolean detail;
    boolean started;
    boolean skipUnderscores;
    ArrayList<String> jumbles;
    HashMap<String, String> dictionary;
    HashMap<String, Integer> anagramsList;
    HashMap<String, String> lexicon;
    CustomAdapter cusadapter;
    SharedPreferences pref;

    int mode = 0;
    String ultimate;

    TextView t1;
    RecyclerView g1;
    TextView t2;
    TextView t5;
    TextView t6;
    EditText e2;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    Button b6;
    Button b7;
    Button b8;
    Button b9;
    Button b10;
    Button b11;

    Cursor anagrams;
    int words;
    int score;
    int counter;
    int number;

    int rows;
    int columns;
    int font;

    // Declare the DrawerLayout, NavigationView and Toolbar
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the DrawerLayout, Toolbar and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout_main);
        toolbar = findViewById(R.id.toolbar_main);
        navigationView = findViewById(R.id.nav_view_main);

        pref = getApplicationContext().getSharedPreferences("AppData", 0);
        boolean prepared = pref.getBoolean("prepared", false);
        hidden = pref.getBoolean("hidden", false);
        detail = pref.getBoolean("detail", false);
        int version = pref.getInt("version", 1);
        Menu menu = navigationView.getMenu();

        if (hidden)
        {
            MenuItem menuItem = menu.findItem(R.id.button36);
            menuItem.setTitle("Show number of answers");
        }

        if (detail)
        {
            MenuItem menuItem = menu.findItem(R.id.button51);
            menuItem.setTitle("Hide full details");
        }

        // Create an ActionBarDrawerToggle to handle
        // the drawer's open/close state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // Add the toggle as a listener to the DrawerLayout
        drawerLayout.addDrawerListener(toggle);

        // Synchronize the toggle's state with the linked DrawerLayout
        toggle.syncState();

        // Set a listener for when an item in the NavigationView is selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // Called when an item in the NavigationView is selected.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle the selected item based on its ID
                switch (item.getItemId()) {
                    case R.id.button20:
                        // Show a Toast message for the SQL query item
                        LayoutInflater inflater1 = LayoutInflater.from(MainActivity.this);
                        final View yourCustomView1 = inflater1.inflate(R.layout.query, null);

                        TextView t3 = yourCustomView1.findViewById(R.id.textview14);
                        t3.setText(db.getSchema());

                        EditText e6 = yourCustomView1.findViewById(R.id.edittext8);

                        Button b12 = yourCustomView1.findViewById(R.id.button73);
                        b12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Help help = new Help();
                                db.messageBox("Example SQL queries", help.getSqlHelp(), MainActivity.this);
                            }
                        });

                        AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Enter your SQL query")
                                .setView(yourCustomView1)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String sqlQuery = ((e6.getText()).toString()).replace("\"", "'");
                                        db.myQuery(sqlQuery, MainActivity.this, true);

                                        refresh();
                                    }
                                }).create();
                        dialog1.show();
                        break;
                    case R.id.button21:
                        // Show a Toast message for the Custom quiz item
                        LayoutInflater inflater2 = LayoutInflater.from(MainActivity.this);
                        final View yourCustomView2 = inflater2.inflate(R.layout.query, null);

                        TextView t4 = yourCustomView2.findViewById(R.id.textview14);
                        t4.setText(db.getSchema());

                        EditText e7 = yourCustomView2.findViewById(R.id.edittext8);

                        Button b13 = yourCustomView2.findViewById(R.id.button73);
                        b13.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Help help = new Help();
                                db.messageBox("Example custom queries", help.getCustomHelp(), MainActivity.this);
                            }
                        });

                        AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("SELECT DISTINCT(alphagram) FROM words WHERE")
                                .setView(yourCustomView2)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String customQuery = ((e7.getText()).toString()).replace("\"", "'");
                                        Cursor resultSet = db.getCustomQuiz(customQuery, MainActivity.this, false);

                                        if (resultSet != null) {
                                            label = customQuery;
                                            letters = 1;
                                            ultimate = null;
                                            mode = 0;
                                            skipUnderscores = false;

                                            closeCursor();
                                            anagrams = resultSet;
                                            words = anagrams.getCount();
                                            score = db.getCustomScore(label);
                                            number = db.getCustomNumber(label);

                                            boolean exists = db.existLabel(letters, label);

                                            if (!exists) {
                                                counter = 0;
                                                db.insertLabel(letters, score, label);
                                            } else {
                                                counter = db.getCounter(letters, label);
                                            }

                                            int highest = (words - 1) / (rows * columns);
                                            if (counter > highest && words > 0) {
                                                counter = highest;
                                                db.updateCounter(letters, label, counter);
                                            }

                                            nextWord();
                                        }
                                    }
                                }).create();
                        dialog2.show();
                        break;
                    case R.id.button26:
                        // Show a Toast message for the View all tag colours item
                        String labelColours = db.getLabelColours(MainActivity.this);
                        db.messageBox("Tag colours", labelColours, MainActivity.this);
                        break;
                    case R.id.button24:
                        // Show a Toast message for the Export tags item
                        db.exportLabels(MainActivity.this, true);
                        break;
                    case R.id.button25:
                        // Show a Toast message for the Import tags item
                        db.importLabels(MainActivity.this, true);
                        break;
                    case R.id.button22:
                        // Show a Toast message for the Export CSV item
                        db.exportDB(MainActivity.this, true);
                        break;
                    case R.id.button23:
                        // Show a Toast message for the Import CSV item
                        db.importDB(MainActivity.this, true);
                        break;
                    case R.id.button34:
                        // Show a Toast message for the Change rows, columns, font size item
                        zoom();
                        break;
                    case R.id.button36:
                        // Show a Toast message for the Hide and show number of answers item
                        if (hidden) {
                            hidden = false;
                            item.setTitle("Hide number of answers");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("hidden", false);
                            editor.apply();
                            if (cusadapter != null) {
                                cusadapter.setHidden(false);
                                cusadapter.notifyDataSetChanged();
                            }
                        } else {
                            hidden = true;
                            item.setTitle("Show number of answers");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("hidden", true);
                            editor.apply();
                            if (cusadapter != null) {
                                cusadapter.setHidden(true);
                                cusadapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case R.id.button38:
                        // Show a Toast message for the Filter by tag item
                        filterByLabel();
                        break;
                    case R.id.button39:
                        // Show a Toast message for the Reset words by tag item
                        db.resetByLabel(MainActivity.this, true);
                        break;
                    case R.id.button41:
                        // Show a Toast message for the Add new tag item
                        db.addByLabel(MainActivity.this, true);
                        break;
                    case R.id.button42:
                        // Show a Toast message for the Rename tag by colour item
                        db.renameByLabel(MainActivity.this, true, false);
                        break;
                    case R.id.button43:
                        // Show a Toast message for the Change tag colour by name item
                        db.renameByLabel(MainActivity.this, true, true);
                        break;
                    case R.id.button44:
                        // Show a Toast message for the Delete single tag by name item
                        db.deleteByLabel(MainActivity.this, true, true);
                        break;
                    case R.id.button45:
                        // Show a Toast message for the Delete single tag by colour item
                        db.deleteByLabel(MainActivity.this, true, false);
                        break;
                    case R.id.button51:
                        // Show a Toast message for the Hide and show full details item
                        if (detail) {
                            detail = false;
                            item.setTitle("Show full details");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("detail", false);
                            editor.apply();
                        } else {
                            detail = true;
                            item.setTitle("Hide full details");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("detail", true);
                            editor.apply();
                        }

                        if (mode == 1) {
                            refreshDefinition();
                        }
                        break;
                    case R.id.button53:
                        // Show a Toast message for the View all prefixes and suffixes item
                        db.getSuffix(MainActivity.this);
                        break;
                    case R.id.button54:
                        // Show a Toast message for the Add new prefix item
                        db.addSuffix(MainActivity.this, true, false, mode);
                        break;
                    case R.id.button55:
                        // Show a Toast message for the Change prefix item
                        db.changeSuffix(MainActivity.this, true, false, mode);
                        break;
                    case R.id.button56:
                        // Show a Toast message for the Delete single prefix item
                        db.deleteSuffix(MainActivity.this, true, false, mode);
                        break;
                    case R.id.button57:
                        // Show a Toast message for the Add new suffix item
                        db.addSuffix(MainActivity.this, true, true, mode);
                        break;
                    case R.id.button58:
                        // Show a Toast message for the Change suffix item
                        db.changeSuffix(MainActivity.this, true, true, mode);
                        break;
                    case R.id.button59:
                        // Show a Toast message for the Delete single suffix item
                        db.deleteSuffix(MainActivity.this, true, true, mode);
                        break;
                    case R.id.button67:
                        // Show a Toast message for the Delete all tags item
                        db.deleteAllRecords(MainActivity.this, true, "colours", mode);
                        break;
                    case R.id.button68:
                        // Show a Toast message for the Delete all prefixes item
                        db.deleteAllRecords(MainActivity.this, true, "prefixes", mode);
                        break;
                    case R.id.button69:
                        // Show a Toast message for the Delete all suffixes item
                        db.deleteAllRecords(MainActivity.this, true, "suffixes", mode);
                        break;
                    case R.id.button75:
                        // Show a Toast message for the Prepare regular database item
                        promptDictionary();
                        break;
                    case R.id.button77:
                        // Show a Toast message for the Search for anagrams item
                        getAllSubanagrams(false);
                        break;
                    case R.id.button78:
                        // Show a Toast message for the Search for subanagrams item
                        getAllSubanagrams(true);
                        break;
                }

                // Close the drawer after selection
                drawerLayout.closeDrawers();
                // Indicate that the item selection has been handled
                return true;
            }
        });

        // Add a callback to handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Close the drawer if it's open
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Finish the activity if the drawer is closed
                    closeCursor();
                    finish();
                }
            }
        });

        t1 = findViewById(R.id.textview1);
        g1 = findViewById(R.id.gridview1);
        t2 = findViewById(R.id.textview4);
        t5 = findViewById(R.id.textview5);
        t6 = findViewById(R.id.textview28);
        e2 = findViewById(R.id.edittext2);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button10);
        b7 = findViewById(R.id.button11);
        b8 = findViewById(R.id.button12);
        b9 = findViewById(R.id.button15);
        b10 = findViewById(R.id.button17);
        b11 = findViewById(R.id.button19);

        db = new sqliteDB(MainActivity.this, version, null, false);

        if (prepared) {
            getWordLength();
        } else {
            promptDictionary();
        }

        ArrayList<Integer> dimensions = db.getZoom("Main");
        rows = dimensions.get(0);
        columns = dimensions.get(1);
        font = dimensions.get(2);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWordLength();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, Report.class);
                startActivity(intent1);
                closeCursor();
                finish();
            }
        });

        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e2.setText("");
            }
        });
    }

    public void promptDictionary()
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.display, null);

        TextView t8 = yourCustomView.findViewById(R.id.textview13);
        t8.setText("CSW24 or NWL23?");

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Choose your lexicon")
                .setView(yourCustomView)
                .setPositiveButton("CSW24", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.dropTable(MainActivity.this, true);
                        prepareDictionary(true);
                    }
                })
                .setNegativeButton("NWL23", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.dropTable(MainActivity.this, true);
                        prepareDictionary(false);
                    }
                }).create();
        dialog.show();
    }

    public void prepareDictionary(boolean international)
    {
        dictionary = new HashMap<>();
        anagramsList = new HashMap<>();
        lexicon = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(international ? "CSW24.txt" : "NWL23.txt"), "UTF-8"));
            while (true)
            {
                String s = reader.readLine();
                if (s == null)
                {
                    break;
                }
                else
                {
                    String[] t = s.split("=");

                    if (t.length == 1)
                    {
                        dictionary.put(t[0], "");
                    }
                    else {
                        dictionary.put(t[0], t[1]);
                    }

                    char[] jumbled = t[0].toCharArray();
                    Arrays.sort(jumbled);
                    String solution = new String(jumbled);

                    if (anagramsList.containsKey(solution))
                    {
                        anagramsList.put(solution, anagramsList.get(solution) + 1);
                    }
                    else
                    {
                        anagramsList.put(solution, 1);
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(international ? "CSW2024.txt" : "NWL2023.txt"), "UTF-8"));
            String s = bufferedReader.readLine();

            while (true)
            {
                s = bufferedReader.readLine();
                if (s == null)
                {
                    break;
                }
                else
                {
                    int comma = s.indexOf(',');
                    String w = s.substring(0, comma);
                    lexicon.put(w, s);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepareDatabase();
    }

    public void prepareDatabase()
    {
        db.insertWord(this, true, dictionary, anagramsList, lexicon);

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("prepared", true);
        editor.apply();
    }

    public void getWordLength()
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.input, null);

        EditText e1 = yourCustomView.findViewById(R.id.edittext1);
        e1.setHint("Enter a value between 2 and 58");

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Word length")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String alphabet = (e1.getText()).toString();
                        int precursor = (alphabet.length() == 0 ? 0 : Integer.parseInt(alphabet));

                        if (precursor < 2)
                        {
                            Toast.makeText(MainActivity.this, "Enter a value between 2 and 58", Toast.LENGTH_LONG).show();
                            getWordLength();
                        }
                        else
                        {
                            letters = precursor;
                            label = "*";
                            ultimate = null;
                            mode = 0;
                            skipUnderscores = false;
                            start();
                        }
                    }
                }).create();
        dialog.show();
    }

    public void wordLength(long begin, double delay, ArrayList<String> replies)
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.input, null);

        EditText e1 = yourCustomView.findViewById(R.id.edittext1);
        e1.setHint("Enter a value between 2 and 58");

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Word length")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String alphabets = (e1.getText()).toString();
                        int precursor = (alphabets.length() == 0 ? 0 : Integer.parseInt(alphabets));

                        if (precursor < 2)
                        {
                            Toast.makeText(MainActivity.this, "Enter a value between 2 and 58", Toast.LENGTH_LONG).show();
                            wordLength(begin, delay, replies);
                        }
                        else
                        {
                            mode = 0;
                            ultimate = null;
                            letters = precursor;
                            cumulativeTime(begin, delay, replies);
                            label = "*";
                            skipUnderscores = false;
                            start();
                        }
                    }
                }).create();
        dialog.show();
    }

    public void start()
    {
        closeCursor();
        anagrams = db.getAllAnagrams(letters, label);
        words = anagrams.getCount();
        score = db.getScore(letters, label);

        int actual = db.getCustomCounter(letters, label);
        if (actual != score)
        {
            score = actual;
            db.updateScore(letters, actual, label);
        }

        counter = db.getCounter(letters, label);
        number = db.getNumber(letters, label);

        int high = (words - 1) / (rows * columns);
        if (counter > high && words > 0)
        {
            counter = high;
            db.updateCounter(letters, label, counter);
        }

        nextWord();
    }

    public void nextWord()
    {
        started = true;

        long begin = System.currentTimeMillis();
        jumbles = new ArrayList<>();
        ArrayList<String> replies = new ArrayList<>();
        ArrayList<Integer> totals = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();
        HashMap<String, Integer> grid = new HashMap<>();

        int open = rows * columns * counter;
        int close = (Math.min((rows * columns * counter) + (rows * columns), words));

        if (anagrams.moveToPosition(open)) {
            do {
                String jumble = anagrams.getString(0);

                jumbles.add(jumble);
            } while (anagrams.moveToNext() && anagrams.getPosition() < close);
        }

        HashMap<String, ArrayList<String>> answers = db.getUnsolvedAnswers(jumbles);
        HashMap<String, Integer> allList = db.getAllAnswers(jumbles);
        for (int total = 0; total < jumbles.size(); total++)
        {
            String answer = jumbles.get(total);
            if (answers.containsKey(answer)) {
                ArrayList<String> answersList = answers.get(answer);
                replies.addAll(answersList);
                totals.add(answersList.size());
                for (String answerList : answersList) {
                    grid.put(answerList, total);
                }
            }
            else {
                totals.add(0);
            }
            amounts.add(allList.get(answer));
        }

        double delay = (replies.size() == 0 ? 0 : db.getTime(replies.get(0)));

        b1.setEnabled(true);
        b2.setEnabled(true);
        b4.setEnabled(true);
        b6.setEnabled(true);
        b7.setEnabled(true);
        b8.setEnabled(true);
        b9.setEnabled(true);
        b10.setEnabled(true);

        t1.setText("Page " + (counter + 1) + " out of " + (((words - 1) / (rows * columns)) + 1));
        t2.setText("Score: " + score + "/" + number);
        if (ultimate == null) {
            t5.setText("");
        }
        e2.setText("");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, Math.max(((jumbles.size() - 1) / columns) + 1, 1), GridLayoutManager.HORIZONTAL, false);
        g1.setLayoutManager(layoutManager);

        cusadapter = new CustomAdapter(MainActivity.this, R.layout.cell, jumbles, totals, amounts, columns, font);
        if (hidden)
        {
            cusadapter.setHidden(true);
        }
        g1.setAdapter(cusadapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guess = (((e2.getText()).toString()).trim()).toUpperCase();
                if (replies.contains(guess))
                {
                    t6.setText("Correct answer");
                    t6.setTextColor(Color.rgb(0, 128, 0));

                    mode = 1;
                    ultimate = guess;

                    long stop = System.currentTimeMillis();
                    double time = stop - begin;
                    time /= 1000;
                    time += (letters == 1 ? db.getTime(guess) : delay);
                    ArrayList<String> guesses = new ArrayList<>();
                    guesses.add(guess);
                    db.updateTime(guesses, time, 1);
                    ArrayList<String> hook = db.getDefinition(guess);
                    String meaning = hook.get(0);
                    String back = hook.get(1);
                    String front = hook.get(2);
                    String lexicons = hook.get(3);
                    HashMap<String, String> colourList = db.getColours();
                    String coloursList = db.getLabel(guess);
                    String amount;

                    if (colourList.containsKey(coloursList) || colourList.containsKey("")) {
                        String coloured = (colourList.containsKey(coloursList) ? colourList.get(coloursList) : colourList.get(""));
                        amount = "<font color=\"" + coloured + "\"><b><small>" + front + "</small> " + guess + " <small>" + back + "</small></b> " + meaning + " <b>" + (coloursList.length() == 0 ? "(No Tag)" : coloursList) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(guess) : "") + "</font>";
                    } else {
                        amount = "<b><small>" + front + "</small> " + guess + " <small>" + back + "</small></b> " + meaning + " <b>" + (coloursList.length() == 0 ? "(No Tag)" : coloursList) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(guess) : "");
                    }

                    t5.setText(Html.fromHtml(amount));
                    replies.remove(guess);
                    score++;
                    db.updateScore(letters, score, label);
                    int index = grid.get(guess);
                    totals.set(index, totals.get(index) - 1);
                    cusadapter.notifyItemChanged(index);
                    t2.setText("Score: " + score + "/" + number);
                }
                else
                {
                    boolean exist = db.containsWord(guess);
                    char[] character = guess.toCharArray();
                    Arrays.sort(character);
                    String wrongAnswer = new String(character);
                    if (!exist) {
                        t6.setTextColor(Color.RED);
                        if (allList.containsKey(wrongAnswer)) {
                            t6.setText("Wrong answer");
                            int wrongAnswers = db.trackWrongAnswers(wrongAnswer, guess);
                        }
                        else {
                            t6.setText("Anagram not here");
                        }
                    }
                    else {
                        t6.setTextColor(Color.rgb(0, 128, 0));
                        if (allList.containsKey(wrongAnswer)) {
                            t6.setText("Already solved");
                        }
                        else {
                            t6.setText("Anagram not here");
                        }
                    }
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                ultimate = null;

                if (counter == (words - 1) / (rows * columns)) {
                    counter = 0;
                }
                else {
                    counter++;
                }
                db.updateCounter(letters, label, counter);
                cumulativeTime(begin, delay, replies);
                nextWord();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordLength(begin, delay, replies);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                ultimate = null;

                if (counter == 0) {
                    counter = (words - 1) / (rows * columns);
                }
                else {
                    counter--;
                }
                db.updateCounter(letters, label, counter);
                cumulativeTime(begin, delay, replies);
                nextWord();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cumulativeTime(begin, delay, replies);
                Intent intent1 = new Intent(MainActivity.this, Report.class);
                startActivity(intent1);
                closeCursor();
                finish();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cumulativeTime(begin, delay, replies);
                closeCursor();
                finish();
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guess = (((e2.getText()).toString()).trim()).toUpperCase();
                if (replies.contains(guess))
                {
                    t6.setText("Yet to submit");
                    t6.setTextColor(Color.rgb(0, 128, 0));

                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    final View yourCustomView = inflater.inflate(R.layout.output, null);

                    EditText e5 = yourCustomView.findViewById(R.id.edittext5);
                    final int[] selection = {0};

                    Spinner s2 = yourCustomView.findViewById(R.id.spinner3);
                    List<RowItem> labelsList = db.getAllLabels();

                    ColourAdapter comboBoxAdapter = new ColourAdapter(MainActivity.this, R.layout.colour, R.id.textview62, labelsList, MainActivity.this, true);
                    s2.setAdapter(comboBoxAdapter);

                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selection[0]++;

                            if (selection[0] <= 1) {
                                String newTag = db.getLabel(guess);
                                e5.setText(newTag);

                                if (labelsList.contains(newTag)) {
                                    int choice = labelsList.lastIndexOf(newTag);
                                    s2.setSelection(choice);
                                }
                            }
                            else {
                                e5.setText((labelsList.get(i)).getTag());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Set tag for " + guess)
                            .setView(yourCustomView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mode = 1;
                                    ultimate = guess;

                                    t6.setText("Correct answer");
                                    t6.setTextColor(Color.rgb(0, 128, 0));

                                    String cardbox = (e5.getText()).toString();

                                    long stop = System.currentTimeMillis();
                                    double time = stop - begin;
                                    time /= 1000;
                                    time += (letters == 1 ? db.getTime(guess) : delay);;
                                    ArrayList<String> guesses = new ArrayList<>();
                                    guesses.add(guess);
                                    db.updateLabel(guesses, time, 1, cardbox);
                                    displayDefinition(cardbox);
                                    replies.remove(guess);
                                    score++;
                                    db.updateScore(letters, score, label);
                                    int index = grid.get(guess);
                                    totals.set(index, totals.get(index) - 1);
                                    cusadapter.notifyItemChanged(index);
                                    t2.setText("Score: " + score + "/" + number);
                                }
                            }).create();
                    dialog.show();
                }
                else
                {
                    boolean exist = db.containsWord(guess);
                    char[] character = guess.toCharArray();
                    Arrays.sort(character);
                    String wrongAnswer = new String(character);
                    if (!exist) {
                        t6.setTextColor(Color.RED);
                        if (allList.containsKey(wrongAnswer)) {
                            t6.setText("Wrong answer");
                            int wrongAnswers = db.trackWrongAnswers(wrongAnswer, guess);
                        }
                        else {
                            t6.setText("Anagram not here");
                        }
                    }
                    else {
                        t6.setTextColor(Color.rgb(0, 128, 0));
                        if (allList.containsKey(wrongAnswer)) {
                            t6.setText("Already solved");
                        }
                        else {
                            t6.setText("Anagram not here");
                        }
                    }
                }
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View yourCustomView = inflater.inflate(R.layout.label, null);

                EditText e3 = yourCustomView.findViewById(R.id.edittext3);
                EditText e4 = yourCustomView.findViewById(R.id.edittext4);

                Spinner s1 = yourCustomView.findViewById(R.id.spinner2);
                List<RowItem> labelList = db.getAllLabels();

                ColourAdapter spinnerAdapter = new ColourAdapter(MainActivity.this, R.layout.colour, R.id.textview62, labelList, MainActivity.this, true);
                s1.setAdapter(spinnerAdapter);

                s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        e4.setText((labelList.get(i)).getTag());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Change tag")
                        .setView(yourCustomView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String line = (((e3.getText()).toString()).trim()).toUpperCase();
                                String category = (e4.getText()).toString();
                                db.updateWord(line, category);

                                if (mode == 1) {
                                    if (line.equals(ultimate)) {
                                        displayDefinition(category);
                                    }
                                } else if (mode == 2) {
                                    char[] last = line.toCharArray();
                                    Arrays.sort(last);
                                    String order = new String(last);

                                    if (order.equals(ultimate)) {
                                        String solved = db.getSolvedAnswers(order);
                                        t5.setText(Html.fromHtml(solved));
                                    }
                                }

                                if (mode == 3)
                                {
                                    String revision = db.getSummary(jumbles);
                                    t5.setText(Html.fromHtml(revision));
                                }
                            }
                        }).create();
                dialog.show();
            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View yourCustomView = inflater.inflate(R.layout.input, null);

                EditText e1 = yourCustomView.findViewById(R.id.edittext1);
                int maximum = ((words - 1) / (rows * columns)) + 1;
                e1.setHint("Enter a value between 1 and " + maximum);

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Go to page")
                        .setView(yourCustomView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String pages = (e1.getText()).toString();
                                int page = (pages.length() == 0 ? 0 : Integer.parseInt(pages));
                                if (page < 1 || page > maximum)
                                {
                                    Toast.makeText(MainActivity.this, "Enter a value between 1 and " + maximum, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    mode = 0;
                                    ultimate = null;

                                    counter = page - 1;
                                    db.updateCounter(letters, label, counter);
                                    cumulativeTime(begin, delay, replies);
                                    nextWord();
                                }
                            }
                        }).create();
                dialog.show();
            }
        });

        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ultimate = "";
                mode = 3;
                revise();
            }
        });
    }

    public void revise()
    {
        String revision = db.getSummary(jumbles);
        t5.setText(Html.fromHtml(revision));
        db.messageBox("Page Summary", revision, MainActivity.this);
    }

    public void cumulativeTime(long begin, double delay, ArrayList<String> replies)
    {
        long stop = System.currentTimeMillis();
        double time = stop - begin;
        time /= 1000;
        if (letters == 1) {
            db.updateCustomTime(replies, time);
        } else {
            time += delay;
            db.updateTime(replies, time, 0);
        }
    }

    public void onItemClick(int i) {
        mode = 2;
        ultimate = jumbles.get(i);

        String solved = db.getSolvedAnswers(ultimate);
        t5.setText(Html.fromHtml(solved));
    }

    public void onItemLongClick(int i) {
        String unsolved = jumbles.get(i);
        String unsolvedAnswers = db.getUnsolvedWords(unsolved);

        db.messageBox("Unsolved answers", unsolvedAnswers, MainActivity.this);
    }

    public void refresh()
    {
        ArrayList<Integer> dimensions = db.getZoom("Main");
        rows = dimensions.get(0);
        columns = dimensions.get(1);
        font = dimensions.get(2);

        try
        {
            if (started) {
                t6.setText("");

                int real = (letters == 1 ? db.getCustomScore(label) : db.getCustomCounter(letters, label));
                if (real != score) {
                    score = real;
                    db.updateScore(letters, real, label);
                }

                closeCursor();
                anagrams = (letters == 1 ? db.getCustomQuiz(label, MainActivity.this, skipUnderscores) : db.getAllAnagrams(letters, label));
                words = anagrams.getCount();

                counter = db.getCounter(letters, label);
                number = (letters == 1 ? db.getCustomNumber(label) : db.getNumber(letters, label));

                int peak = (words - 1) / (rows * columns);
                if (counter > peak && words > 0) {
                    counter = peak;
                    db.updateCounter(letters, label, counter);
                }

                nextWord();
                refreshDefinition();
            }
        }
        catch (Exception e)
        {
        }
    }

    public void refreshDefinition()
    {
        if (ultimate != null)
        {
            switch(mode)
            {
                case 1:
                    String tag = db.getLabel(ultimate);
                    displayDefinition(tag);
                    break;
                case 2:
                    String solved = db.getSolvedAnswers(ultimate);
                    t5.setText(Html.fromHtml(solved));
                    break;
                case 3:
                    String revision = db.getSummary(jumbles);
                    t5.setText(Html.fromHtml(revision));
                    break;
            }
        }
    }

    public void displayDefinition(String listbox)
    {
        ArrayList<String> hook = db.getDefinition(ultimate);
        String meaning = hook.get(0);
        String back = hook.get(1);
        String front = hook.get(2);
        String lexicons = hook.get(3);

        HashMap<String, String> colours = db.getColours();
        String amount;

        if (colours.containsKey(listbox) || colours.containsKey("")) {
            String colour = (colours.containsKey(listbox) ? colours.get(listbox) : colours.get(""));
            amount = "<font color=\"" + colour + "\"><b><small>" + front + "</small> " + ultimate + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.length() == 0 ? "(No Tag)" : listbox) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(ultimate) : "") + "</font>";
        } else {
            amount = "<b><small>" + front + "</small> " + ultimate + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.length() == 0 ? "(No Tag)" : listbox) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(ultimate) : "");
        }

        t5.setText(Html.fromHtml(amount));
    }

    public void zoom()
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.zoom, null);

        EditText e8 = yourCustomView.findViewById(R.id.edittext12);
        EditText e9 = yourCustomView.findViewById(R.id.edittext13);
        EditText e10 = yourCustomView.findViewById(R.id.edittext14);

        e8.setHint("Enter a value greater than 0");
        e9.setHint("Enter a value greater than 0");
        e10.setHint("Enter a value greater than 11");

        e8.setText(Integer.toString(rows));
        e9.setText(Integer.toString(columns));
        e10.setText(Integer.toString(font));

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Change rows, columns and font size")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String old_rows = (e8.getText()).toString();
                        String old_columns = (e9.getText()).toString();
                        String old_font = (e10.getText()).toString();

                        int new_rows = (old_rows.length() == 0 ? 0 : Integer.parseInt(old_rows));
                        int new_columns = (old_columns.length() == 0 ? 0 : Integer.parseInt(old_columns));
                        int new_font = (old_font.length() == 0 ? 0 : Integer.parseInt(old_font));

                        if (new_rows < 1 || new_columns < 1 || new_font < 11)
                        {
                            Toast.makeText(MainActivity.this, "Font size should be greater than 11\nRows and columns should be greater than 0", Toast.LENGTH_LONG).show();
                            zoom();
                        }
                        else
                        {
                            long magnify = db.setZoom("Main", new_rows, new_columns, new_font);
                            refresh();
                        }
                    }
                }).create();
        dialog.show();
    }

    public void setPrepared()
    {
        boolean prepared = pref.getBoolean("prepared", false);

        if (!prepared) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("prepared", true);
            editor.apply();
        }
    }

    public void reload(ArrayList<String> lastQuery, int DATABASE_VERSION, boolean recreate)
    {
        db = new sqliteDB(this, DATABASE_VERSION, lastQuery, recreate);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("version", DATABASE_VERSION);
        editor.apply();
    }

    public void closeCursor()
    {
        if (anagrams != null && !anagrams.isClosed())
        {
            anagrams.close();
        }
    }

    public void filterByLabel()
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.sieve, null);

        EditText e11 = yourCustomView.findViewById(R.id.edittext19);
        EditText e12 = yourCustomView.findViewById(R.id.edittext20);
        TextView t7 = yourCustomView.findViewById(R.id.textview32);

        e12.setHint("Enter a value between 2 and 58");

        Spinner s3 = yourCustomView.findViewById(R.id.spinner9);
        List<RowItem> tagsList = db.getAllLabels();
        tagsList.add(0, new RowItem("(All Tags)", null));

        ColourAdapter spinnerAdapter = new ColourAdapter(MainActivity.this, R.layout.colour, R.id.textview62, tagsList, MainActivity.this, true);
        s3.setAdapter(spinnerAdapter);

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e11.setText("*");
                }
                else {
                    e11.setText((tagsList.get(i)).getTag());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final int[] lengthIndex = new int[1];
        Spinner s4 = yourCustomView.findViewById(R.id.spinner10);
        ArrayList<String> lengthList = new ArrayList<>();
        lengthList.add(0, "Specific length");
        lengthList.add(1, "All lengths");

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, lengthList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s4.setAdapter(lengthAdapter);

        s4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e12.setVisibility(View.VISIBLE);
                    t7.setVisibility(View.VISIBLE);
                    lengthIndex[0] = 0;
                }
                else {
                    e12.setVisibility(View.INVISIBLE);
                    t7.setVisibility(View.INVISIBLE);
                    lengthIndex[0] = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Filter by tag")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        label = (e11.getText()).toString();
                        String alphabets = (lengthIndex[0] == 0 ? (e12.getText()).toString() : "-1");
                        int temporary = (alphabets.length() == 0 ? 0 : Integer.parseInt(alphabets));

                        if (lengthIndex[0] == 0 && temporary < 2)
                        {
                            Toast.makeText(MainActivity.this, "Enter a value between 2 and 58 for word length", Toast.LENGTH_LONG).show();
                            filterByLabel();
                        }
                        else
                        {
                            letters = temporary;
                            boolean exist = db.existLabel(letters, label);
                            ultimate = null;
                            mode = 0;
                            skipUnderscores = false;

                            if (!exist)
                            {
                                db.insertLabel(letters, 0, label);
                            }

                            start();
                        }
                    }
                }).create();
        dialog.show();
    }

    public void getAllSubanagrams(boolean subanagram)
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.subanagram, null);

        EditText e13 = yourCustomView.findViewById(R.id.edittext29);
        EditText e14 = yourCustomView.findViewById(R.id.edittext30);

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(subanagram ? "Search for subanagrams" : "Search for anagrams")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String letter = ((e13.getText()).toString()).toUpperCase();
                        boolean flag = false;
                        for (int digits = 0; digits < letter.length(); digits++) {
                            int flags = (int) letter.charAt(digits);
                            if (flags < 65 || flags > 90) {
                                flag = true;
                                break;
                            }
                        }

                        String digit = (e14.getText()).toString();
                        int blanks = (digit.length() == 0 ? 0 : Integer.parseInt(digit));

                        if (flag) {
                            Toast.makeText(MainActivity.this, "Letters field can contain only letters", Toast.LENGTH_LONG).show();
                            getAllSubanagrams(subanagram);
                        }
                        else {
                            StringBuilder theQuery = new StringBuilder();

                            if (subanagram) {
                                int[] occurrence = new int[26];
                                for (int myRadix = 0; myRadix < letter.length(); myRadix++) {
                                    char theCharacter = letter.charAt(myRadix);
                                    occurrence[theCharacter - 65]++;
                                }

                                for (int theRadix = 0; theRadix < 26; theRadix++) {
                                    char occurrences = (char) (theRadix + 97);
                                    theQuery.append("_no_").append(occurrences).append("_ <= ").append(occurrence[theRadix] + blanks).append(" AND ");
                                }

                                for (int myIndex = 0; myIndex < 26; myIndex++) {
                                    char occurrences = (char) (myIndex + 97);
                                    theQuery.append(myIndex == 0 ? "" : " + ").append("ABS(_no_").append(occurrences).append("_ - ").append(occurrence[myIndex]).append(")");
                                }
                                theQuery.append(" <= ").append((2 * blanks) + letter.length()).append(" - _length_ ORDER BY _length_ DESC");
                            } else {
                                char[] myCharacter = letter.toCharArray();
                                Arrays.sort(myCharacter);
                                StringBuilder empties = new StringBuilder();
                                for (char myLetter : myCharacter) {
                                    empties.append("%").append(myLetter);
                                }
                                empties.append("%");
                                String empty = new String(empties);
                                theQuery.append("_length_ = ").append(letter.length() + blanks).append(" AND _alphagram_ LIKE '").append(empty).append("'");
                            }

                            System.out.println("The query: " + theQuery);
                            String customQuery = new String(theQuery);
                            Cursor resultSet = db.getCustomQuiz(customQuery, MainActivity.this, true);

                            if (resultSet != null) {
                                label = customQuery;
                                letters = 1;
                                ultimate = null;
                                mode = 0;
                                skipUnderscores = true;

                                closeCursor();
                                anagrams = resultSet;
                                words = anagrams.getCount();
                                score = db.getCustomScore(label);
                                number = db.getCustomNumber(label);

                                boolean exists = db.existLabel(letters, label);

                                if (!exists) {
                                    counter = 0;
                                    db.insertLabel(letters, score, label);
                                } else {
                                    counter = db.getCounter(letters, label);
                                }

                                int highest = (words - 1) / (rows * columns);
                                if (counter > highest && words > 0) {
                                    counter = highest;
                                    db.updateCounter(letters, label, counter);
                                }

                                nextWord();
                            }
                        }
                    }
                }).create();
        dialog.show();
    }
}