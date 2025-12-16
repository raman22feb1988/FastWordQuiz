package com.tuchwords.wordquiz;

import androidx.activity.OnBackPressedCallback;
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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    sqliteDB db;
    int letters = 0;
    String label = "*";
    int solvedStatus = 0;
    boolean hidden;
    boolean detail;
    int clear;
    int shuffle;
    String lastWord;
    boolean started;
    String orderBy;
    ArrayList<String> jumbles;
    HashMap<String, String> dictionary;
    HashMap<String, Integer> anagramsList;
    HashMap<String, String> lexicon;
    ArrayList<String> solvedList;
    ArrayList<String> aggregate;
    ArrayList<String> sort;
    ArrayList<String> allColumns;
    ArrayList<String> blankColumns;
    ArrayList<String> blankList;
    ArrayList<String> jokerList;
    HashMap<String, ArrayList<Integer>> grid;
    CustomAdapter cusadapter;
    SharedPreferences pref;

    int mode = 0;
    long begin = 0;
    HashSet<String> replies = new HashSet<>();
    HashSet<String> identities = new HashSet<>();
    String ultimate;
    String selectedAnagram;

    TextView t1;
    RecyclerView g1;
    TextView t2;
    TextView t5;
    TextView t6;
    TextView t4;
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
    Button b12;
    Button b13;

    Cursor anagrams;
    int words;
    int score;
    int counter;
    int number;
    int numerator;
    int denominator;
    boolean blank;

    int rows;
    int columns;
    int font;
    int combo;
    int maximumWordLength;
    int maximumBlankLength;

    // Declare the DrawerLayout, NavigationView and Toolbar
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the DrawerLayout, NavigationView and Toolbar
        drawerLayout = findViewById(R.id.drawer_layout_main);
        NavigationView navigationView = findViewById(R.id.nav_view_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);

        pref = getApplicationContext().getSharedPreferences("AppData", 0);
        boolean prepared = pref.getBoolean("prepared", false);
        hidden = pref.getBoolean("hidden", false);
        detail = pref.getBoolean("detail", false);
        int version = pref.getInt("version", 1);
        clear = pref.getInt("clear", 255);
        shuffle = pref.getInt("shuffle", 0);
        lastWord = "";
        Menu menu = navigationView.getMenu();

        if (hidden)
        {
            MenuItem menuItem = menu.findItem(R.id.button36);
            menuItem.setTitle("Show number of answers");
        }

        if (detail)
        {
            MenuItem menuItem = menu.findItem(R.id.button51);
            menuItem.setTitle("Hide similar words (Faster)");
        }

        // Create an ActionBarDrawerToggle to handle
        // the drawer's open / close state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // Add the toggle as a listener to the DrawerLayout
        drawerLayout.addDrawerListener(toggle);

        // Synchronize the toggle's state with the linked DrawerLayout
        toggle.syncState();

        // Set a listener for when an item in the NavigationView is selected
        // Called when an item in the NavigationView is selected.
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle the selected item based on its ID
            switch (item.getItemId()) {
                case R.id.button20:
                    // Show a Toast message for the SQL query item
                    LayoutInflater inflater1 = LayoutInflater.from(MainActivity.this);
                    final View yourCustomView2 = inflater1.inflate(R.layout.sqlquery, null);

                    EditText e6 = yourCustomView2.findViewById(R.id.edittext8);
                    CheckBox c3 = yourCustomView2.findViewById(R.id.checkbox3);
                    FrameLayout f2 = yourCustomView2.findViewById(R.id.framelayout2);

                    LayoutInflater subinflater2 = LayoutInflater.from(MainActivity.this);
                    final View subCustomView2 = subinflater2.inflate(R.layout.sieve, null);
                    f2.addView(subCustomView2);

                    db.addFunctionalities(MainActivity.this, e6, c3, true, subCustomView2);

                    AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Enter your SQL query")
                            .setView(yourCustomView2)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String sqlQuery = ((e6.getText()).toString()).replace("\"", "'");

                                    if (!sqlQuery.isEmpty()) {
                                        db.myQuery(c3.isChecked() ? db.addUnderscores(sqlQuery) : sqlQuery, MainActivity.this, true);
                                    }
                                }
                            }).create();
                    dialog1.show();
                    break;
                case R.id.button21:
                    // Show a Toast message for the Custom quiz item
                    LayoutInflater inflater2 = LayoutInflater.from(MainActivity.this);
                    final View yourCustomView1 = inflater2.inflate(R.layout.query, null);

                    EditText e7 = yourCustomView1.findViewById(R.id.edittext18);
                    CheckBox c2 = yourCustomView1.findViewById(R.id.checkbox2);
                    FrameLayout f1 = yourCustomView1.findViewById(R.id.framelayout1);

                    LayoutInflater subinflater1 = LayoutInflater.from(MainActivity.this);
                    final View subCustomView1 = subinflater1.inflate(R.layout.sieve, null);
                    f1.addView(subCustomView1);

                    db.addFunctionalities(MainActivity.this, e7, c2, false, subCustomView1);

                    Spinner s24 = yourCustomView1.findViewById(R.id.spinner36);
                    ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankList);
                    emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s24.setAdapter(emptyAdapter);

                    final int[] sortIndex = new int[3];
                    Spinner s16 = yourCustomView1.findViewById(R.id.spinner28);
                    ArrayAdapter<String> aggregateAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, aggregate);
                    aggregateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s16.setAdapter(aggregateAdapter);

                    Spinner s17 = yourCustomView1.findViewById(R.id.spinner29);
                    ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allColumns);
                    orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankColumns);
                    ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s17.setAdapter(orderAdapter);

                    Spinner s18 = yourCustomView1.findViewById(R.id.spinner30);
                    ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, sort);
                    sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s18.setAdapter(sortAdapter);

                    s16.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            sortIndex[0] = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    s17.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            sortIndex[1] = i;

                            if (i < 2) {
                                s16.setVisibility(View.GONE);
                            }
                            else {
                                s16.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    s18.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            sortIndex[2] = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    s17.setSelection(6);
                    s18.setSelection(1);

                    final int[] solved = {2};
                    Spinner s8 = yourCustomView1.findViewById(R.id.spinner7);
                    ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, solvedList);
                    solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s8.setAdapter(solvedAdapter);
                    s8.setSelection(2);

                    s8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            solved[0] = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("SELECT DISTINCT(alphagram) FROM words WHERE")
                            .setView(yourCustomView1)
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                String temporaryQuery = ((e7.getText()).toString()).replace("\"", "'");
                                String customQuery = (temporaryQuery.isEmpty() ? "1" : temporaryQuery);
                                boolean wildIndex = (s24.getSelectedItemPosition() > 0);
                                String orderIndex = sortBy(sortIndex, wildIndex);
                                String processingQuery = (c2.isChecked() ? db.addUnderscores(customQuery) : customQuery);
                                Cursor resultSet = db.getCustomQuiz(processingQuery, MainActivity.this, solved[0], orderIndex, wildIndex);

                                if (resultSet != null) {
                                    label = processingQuery;
                                    letters = 1;
                                    ultimate = null;
                                    selectedAnagram = null;
                                    mode = 0;
                                    solvedStatus = solved[0];
                                    orderBy = orderIndex;

                                    closeCursor();
                                    anagrams = resultSet;
                                    words = anagrams.getCount();
                                    int[] pair1 = db.getCustomScore(label, solved[0], wildIndex);
                                    score = pair1[0];
                                    number = pair1[1];

                                    boolean exists = db.existLabel(letters, label, orderBy, wildIndex);

                                    if (!exists) {
                                        counter = 0;
                                        db.insertLabel(letters, label, orderBy, wildIndex);
                                    } else {
                                        counter = db.getCounter(letters, label, solvedStatus, orderBy, wildIndex);
                                    }

                                    int highest = (words - 1) / (rows * columns);
                                    if (counter > highest && words > 0) {
                                        counter = highest;
                                        db.updateCounter(letters, label, counter, solvedStatus, orderBy, wildIndex);
                                    }

                                    nextWord(wildIndex);
                                }
                            }).create();
                    dialog2.show();

                    s24.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            dialog2.setTitle((i == 0) ? "SELECT DISTINCT(alphagram) FROM words WHERE" : "SELECT DISTINCT(anagram) FROM blanks WHERE");

                            if (i == 0) {
                                s17.setAdapter(orderAdapter);
                                s17.setSelection(6);
                                s18.setSelection(1);
                            }
                            else {
                                s17.setAdapter(ordersAdapter);
                                s17.setSelection(1);
                                s18.setSelection(0);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
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
                    db.resetByLabel(MainActivity.this, true, blankList, maximumWordLength, maximumBlankLength, combo);
                    break;
                case R.id.button41:
                    // Show a Toast message for the Add new tag item
                    db.addByLabel(MainActivity.this, true);
                    break;
                case R.id.button42:
                    // Show a Toast message for the Rename tag by colour item
                    db.renameByLabel(MainActivity.this, true, false, combo);
                    break;
                case R.id.button43:
                    // Show a Toast message for the Change tag colour by name item
                    db.renameByLabel(MainActivity.this, true, true, combo);
                    break;
                case R.id.button44:
                    // Show a Toast message for the Delete single tag by name item
                    db.deleteByLabel(MainActivity.this, true, true, combo);
                    break;
                case R.id.button45:
                    // Show a Toast message for the Delete single tag by colour item
                    db.deleteByLabel(MainActivity.this, true, false, combo);
                    break;
                case R.id.button51:
                    // Show a Toast message for the Hide and show similar words item
                    if (detail) {
                        detail = false;
                        item.setTitle("Show similar words (Slower)");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("detail", false);
                        editor.apply();
                    } else {
                        detail = true;
                        item.setTitle("Hide similar words (Faster)");
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
                    promptDictionary(true, false);
                    break;
                case R.id.button77:
                    // Show a Toast message for the Search for anagrams item
                    getAllSubanagrams(false);
                    break;
                case R.id.button78:
                    // Show a Toast message for the Search for subanagrams item
                    getAllSubanagrams(true);
                    break;
                case R.id.button82:
                    // Show a Toast message for the Prepare blank database item
                    promptDictionary(false, true);
                    break;
                case R.id.button84:
                    // Show a Toast message for the Clear answers on submit item
                    LayoutInflater inflater3 = LayoutInflater.from(MainActivity.this);
                    final View yourCustomView5 = inflater3.inflate(R.layout.clear, null);

                    CheckBox[] checkBoxes = {yourCustomView5.findViewById(R.id.checkbox5),
                            yourCustomView5.findViewById(R.id.checkbox6),
                            yourCustomView5.findViewById(R.id.checkbox7),
                            yourCustomView5.findViewById(R.id.checkbox8),
                            yourCustomView5.findViewById(R.id.checkbox9),
                            yourCustomView5.findViewById(R.id.checkbox10),
                            yourCustomView5.findViewById(R.id.checkbox11),
                            yourCustomView5.findViewById(R.id.checkbox12),
                    };

                    for (int clearIndex = 0; clearIndex < checkBoxes.length; clearIndex++) {
                        if ((clear & (1 << clearIndex)) > 0) {
                            checkBoxes[clearIndex].setChecked(true);
                        }
                    }

                    AlertDialog dialog3 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Clear answers on submit")
                            .setView(yourCustomView5)
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                int clearValue = 0;
                                for (int clearVariable = 0; clearVariable < checkBoxes.length; clearVariable++) {
                                    if (checkBoxes[clearVariable].isChecked()) {
                                        clearValue += (1 << clearVariable);
                                    }
                                }

                                clear = clearValue;
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("clear", clear);
                                editor.apply();
                            }).create();
                    dialog3.show();
                    break;
                case R.id.button87:
                    // Show a Toast message for the Shuffle anagrams by item
                    LayoutInflater inflater4 = LayoutInflater.from(MainActivity.this);
                    final View yourCustomView4 = inflater4.inflate(R.layout.shuffle, null);

                    RadioGroup r1 = yourCustomView4.findViewById(R.id.radioGroup1);
                    ((RadioButton) r1.getChildAt(shuffle)).setChecked(true);

                    AlertDialog dialog4 = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Shuffle anagrams by")
                            .setView(yourCustomView4)
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                RadioButton r2 = yourCustomView4.findViewById(r1.getCheckedRadioButtonId());
                                shuffle = r1.indexOfChild(r2);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("shuffle", shuffle);
                                editor.apply();
                                refresh();
                            }).create();
                    dialog4.show();
                    break;
                case R.id.button102:
                    // Show a Toast message for the View all tables and columns item
                    db.alertBox("View all tables and columns", db.getSchema(), MainActivity.this);
                    break;
            }

            // Close the drawer after selection
            drawerLayout.closeDrawers();
            // Indicate that the item selection has been handled
            return true;
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
        t4 = findViewById(R.id.textview27);
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
        b12 = findViewById(R.id.button86);
        b13 = findViewById(R.id.button100);

        db = new sqliteDB(MainActivity.this, version, null, false);
        db.initialize();

        ArrayList<Integer> dimensions = db.getZoom("Quiz");
        rows = dimensions.get(0);
        columns = dimensions.get(1);
        font = dimensions.get(2);
        combo = dimensions.get(3);
        maximumWordLength = db.getMaximumWordLength(false);
        maximumBlankLength = db.getMaximumWordLength(true);

        solvedList = new ArrayList<>();
        solvedList.add("Fully solved anagrams only");
        solvedList.add("Partially solved and fully unsolved only");
        solvedList.add("All anagrams");
        solvedList.add("Partially solved and fully solved only");
        solvedList.add("Fully unsolved anagrams only");
        solvedList.add("Partially solved anagrams only");
        solvedList.add("Fully solved and fully unsolved only");

        aggregate = new ArrayList<>();
        aggregate.add("first word of");
        aggregate.add("maximum of");
        aggregate.add("minimum of");
        aggregate.add("average of");
        aggregate.add("sum of");
        aggregate.add("count of");

        sort = new ArrayList<>();
        sort.add("ascending");
        sort.add("descending");

        allColumns = new ArrayList<>();
        allColumns.add("(default)");
        allColumns.add("(random)");

        blankColumns = new ArrayList<>();
        blankColumns.add("(default)");
        blankColumns.add("(random)");

        blankList = new ArrayList<>();
        blankList.add("Regular anagrams");
        blankList.add("Blank anagrams");

        jokerList = new ArrayList<>();
        jokerList.add("Regular anagrams");
        jokerList.add("Blank anagrams counting blank");
        jokerList.add("Blank anagrams without counting blank");

        for (String oneColumn : db.getAllColumns("words")) {
            allColumns.add(oneColumn.substring(1, oneColumn.length() - 1));
        }

        for (String blankColumn : db.getAllColumns("blanks")) {
            blankColumns.add(blankColumn.substring(1, blankColumn.length() - 1));
        }

        b3.setOnClickListener(view -> getWordLength());

        b5.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, Report.class);
            startActivity(intent1);
            finish();
        });

        b6.setOnClickListener(view -> finish());

        b8.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View yourCustomView = inflater.inflate(R.layout.label, null);

            EditText e3 = yourCustomView.findViewById(R.id.edittext3);
            EditText e4 = yourCustomView.findViewById(R.id.edittext4);
            Spinner s26 = yourCustomView.findViewById(R.id.spinner39);

            final ArrayList<String>[] anagramItem = new ArrayList[]{new ArrayList<>()};
            ArrayAdapter<String> anagramAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, anagramItem[0]);
            anagramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s26.setAdapter(anagramAdapter);

            e3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    anagramItem[0] = db.getBlankAnagrams((s.toString()).toUpperCase());
                    ArrayAdapter<String> alphagramAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, anagramItem[0]);
                    alphagramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s26.setAdapter(alphagramAdapter);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            Spinner s1 = yourCustomView.findViewById(R.id.spinner2);
            List<Pair<String, String>> labelList = db.getAllLabels();

            ColourAdapter spinnerAdapter = new ColourAdapter(MainActivity.this, R.layout.colour, R.id.textview62, labelList, MainActivity.this, true, combo);
            s1.setAdapter(spinnerAdapter);

            s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    e4.setText((labelList.get(i)).first);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Change tag")
                    .setView(yourCustomView)
                    .setPositiveButton("OK", (dialog5, whichButton) -> {
                        String line = (((e3.getText()).toString()).trim()).toUpperCase();
                        String category = (e4.getText()).toString();
                        int anagramIndex = s26.getSelectedItemPosition();
                        String chosenAnagram = anagramItem[0].get(anagramIndex);

                        if (anagramIndex == 0) {
                            db.updateTag(line, category, false);
                        } else {
                            db.updateTag(line + " " + chosenAnagram, category, true);
                        }

                        if (mode == 1) {
                            if (grid.containsKey(line)) {
                                if (line.equals(ultimate) && chosenAnagram.equals(selectedAnagram)) {
                                    displayDefinition(category);
                                }
                            }
                        } else if (mode == 2) {
                            if (chosenAnagram.equals(ultimate)) {
                                String solved = db.getSolvedAnswers(chosenAnagram, blank);
                                t5.setText(Html.fromHtml(solved));
                            }
                        }

                        if (mode == 3)
                        {
                            String revision = db.getSummary(jumbles, blank);
                            t5.setText(Html.fromHtml(revision));
                        }
                    }).create();
            dialog.show();
        });

        b10.setOnClickListener(view -> {
            ultimate = "";
            selectedAnagram = "";
            mode = 3;
            revise();
        });

        b11.setOnClickListener(view -> e2.setText(""));

        b12.setOnClickListener(view -> e2.setText(lastWord));

        // Add a callback to handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event here
                terminate();
            }
        });

        if (!prepared) {
            promptDictionary(false, false);
        }
    }

    public void promptDictionary(boolean deleteTable, boolean joker)
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.prompt, null);

        TextView t8 = yourCustomView.findViewById(R.id.textview14);
        t8.setText(joker ? "Preparing blank database will take 3 hours or more depending upon your device. It is recommended not to interrupt its execution in between and so you can choose to run this before you go to bed at night. If you do not want to run this now, you can click anywhere outside this dialogue box to close this dialogue box. If you want to run this now, you can choose your desired lexicon from below:\n\nCSW24 or NWL23?" :"CSW24 or NWL23?");

        CheckBox c1 = yourCustomView.findViewById(R.id.checkbox1);
        c1.setChecked(deleteTable);

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Choose your lexicon")
                .setView(yourCustomView)
                .setPositiveButton("CSW24", (dialog1, whichButton) -> {
                    if (joker) {
                        Toast.makeText(MainActivity.this, "Loading all blank anagrams into memory. Just a minute...", Toast.LENGTH_LONG).show();
                    }

                    if (c1.isChecked()) {
                        db.dropTable(MainActivity.this, true);
                    }
                    prepareDictionary(true, joker);
                })
                .setNegativeButton("NWL23", (dialog2, whichButton) -> {
                    if (joker) {
                        Toast.makeText(MainActivity.this, "Loading all blank anagrams into memory. Just a minute...", Toast.LENGTH_LONG).show();
                    }

                    if (c1.isChecked()) {
                        db.dropTable(MainActivity.this, true);
                    }
                    prepareDictionary(false, joker);
                }).create();
        dialog.show();
    }

    public void prepareDictionary(boolean international, boolean joker)
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

                    if (joker) {
                        HashSet<Character> used = new HashSet<>();

                        for (int letterIndex = 0; letterIndex < t[0].length(); letterIndex++) {
                            char character = t[0].charAt(letterIndex);
                            if (used.contains(character)) {
                                continue;
                            } else {
                                used.add(character);
                                String subword = t[0].substring(0, letterIndex) + t[0].substring(letterIndex + 1);
                                char[] subcharacter = subword.toCharArray();
                                Arrays.sort(subcharacter);
                                String solution = new String(subcharacter) + "?";

                                if (anagramsList.containsKey(solution)) {
                                    anagramsList.put(solution, anagramsList.get(solution) + 1);
                                } else {
                                    anagramsList.put(solution, 1);
                                }
                            }
                        }
                    }
                    else {
                        char[] jumbled = t[0].toCharArray();
                        Arrays.sort(jumbled);
                        String solution = new String(jumbled);

                        if (anagramsList.containsKey(solution)) {
                            anagramsList.put(solution, anagramsList.get(solution) + 1);
                        } else {
                            anagramsList.put(solution, 1);
                        }
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

            while (true)
            {
                String s = bufferedReader.readLine();
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

        prepareDatabase(joker);
    }

    public void prepareDatabase(boolean joker)
    {
        db.insertWord(this, true, dictionary, anagramsList, lexicon, joker);
    }

    public void getWordLength()
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView = inflater.inflate(R.layout.solve, null);

        EditText e1 = yourCustomView.findViewById(R.id.edittext17);
        TextView t3 = yourCustomView.findViewById(R.id.textview80);
        e1.setHint("Enter a value between 2 and " + maximumWordLength);

        Spinner s22 = yourCustomView.findViewById(R.id.spinner34);
        ArrayAdapter<String> blankAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankList);
        blankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s22.setAdapter(blankAdapter);

        final int[] sortIndex = new int[3];
        Spinner s10 = yourCustomView.findViewById(R.id.spinner22);
        ArrayAdapter<String> aggregateAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, aggregate);
        aggregateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s10.setAdapter(aggregateAdapter);

        Spinner s11 = yourCustomView.findViewById(R.id.spinner23);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankColumns);
        ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s11.setAdapter(orderAdapter);

        Spinner s12 = yourCustomView.findViewById(R.id.spinner24);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s12.setAdapter(sortAdapter);

        s22.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                e1.setHint("Enter a value between 2 and " + ((i == 0) ? maximumWordLength : maximumBlankLength));

                if (i == 0) {
                    s11.setAdapter(orderAdapter);
                    s11.setSelection(6);
                    s12.setSelection(1);
                }
                else {
                    s11.setAdapter(ordersAdapter);
                    s11.setSelection(1);
                    s12.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[1] = i;

                if (i < 2) {
                    s10.setVisibility(View.GONE);
                }
                else {
                    s10.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[2] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s11.setSelection(6);
        s12.setSelection(1);

        final int[] solved = {2};
        Spinner s5 = yourCustomView.findViewById(R.id.spinner5);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, solvedList);
        solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s5.setAdapter(solvedAdapter);
        s5.setSelection(2);

        s5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                solved[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final int[] lengthIndex = new int[1];
        Spinner s9 = yourCustomView.findViewById(R.id.spinner21);
        ArrayList<String> lengthList = new ArrayList<>();
        lengthList.add(0, "Specific word length");
        lengthList.add(1, "All word lengths");

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, lengthList);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s9.setAdapter(lengthAdapter);

        s9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e1.setVisibility(View.VISIBLE);
                    t3.setVisibility(View.VISIBLE);
                    lengthIndex[0] = 0;
                }
                else {
                    e1.setVisibility(View.INVISIBLE);
                    t3.setVisibility(View.INVISIBLE);
                    lengthIndex[0] = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Change word length")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String alphabet = (lengthIndex[0] == 0 ? (e1.getText()).toString() : "-1");
                    int precursor = (alphabet.isEmpty() ? 0 : Integer.parseInt(alphabet));
                    boolean wild = (s22.getSelectedItemPosition() > 0);

                    if (lengthIndex[0] == 0 && precursor < 2)
                    {
                        Toast.makeText(MainActivity.this, "Enter a value between 2 and " + (wild ? maximumBlankLength : maximumWordLength) + " for word length", Toast.LENGTH_LONG).show();
                        getWordLength();
                    }
                    else
                    {
                        mode = 0;
                        ultimate = null;
                        selectedAnagram = null;
                        letters = precursor;
                        label = "*";
                        solvedStatus = solved[0];
                        orderBy = sortBy(sortIndex, wild);
                        start(wild);
                    }
                }).create();
        dialog.show();
    }

    public void start(boolean old_blank)
    {
        closeCursor();

        boolean exist = db.existLabel(letters, label, orderBy, old_blank);

        if (!exist)
        {
            db.insertLabel(letters, label, orderBy, old_blank);
        }

        anagrams = db.getAllAnagrams(letters, label, solvedStatus, orderBy, old_blank);
        words = anagrams.getCount();
        int[] pair2 = db.getScore(letters, label, solvedStatus, old_blank);
        score = pair2[0];
        counter = db.getCounter(letters, label, solvedStatus, orderBy, old_blank);
        number = pair2[1];

        int high = (words - 1) / (rows * columns);
        if (counter > high && words > 0)
        {
            counter = high;
            db.updateCounter(letters, label, counter, solvedStatus, orderBy, old_blank);
        }

        nextWord(old_blank);
    }

    public void nextWord(boolean new_blank)
    {
        if (started) {
            cumulativeTime(null, false, null);
        }

        db.initialize();

        begin = System.currentTimeMillis();
        started = true;
        blank = new_blank;
        jumbles = new ArrayList<>();
        replies = new HashSet<>();
        identities = new HashSet<>();
        ArrayList<Integer> totals = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();
        denominator = 0;
        grid = new HashMap<>();

        int open = rows * columns * counter;
        int close = (Math.min((rows * columns * counter) + (rows * columns), words));

        if (orderBy.equals("DESC"))
        {
            if (anagrams.moveToPosition(words - 1 - open)) {
                do {
                    String jumble = anagrams.getString(0);
                    jumbles.add(jumble);
                } while (anagrams.moveToPrevious() && anagrams.getPosition() >= (words - close));
            }
        }
        else
        {
            if (anagrams.moveToPosition(open)) {
                do {
                    String jumble = anagrams.getString(0);
                    jumbles.add(jumble);
                } while (anagrams.moveToNext() && anagrams.getPosition() < close);
            }
        }

        HashMap<String, ArrayList<String>> answers = db.getUnsolvedAnswers(jumbles, blank);
        HashMap<String, Integer> allList = db.getAllAnswers(jumbles, blank);
        for (int total = 0; total < jumbles.size(); total++)
        {
            String answer = jumbles.get(total);
            if (answers.containsKey(answer)) {
                ArrayList<String> answersList = answers.get(answer);
                replies.addAll(answersList);
                for (String answersItem : answersList) {
                    identities.add(answersItem + " " + answer);
                }
                totals.add(answersList.size());
                for (String answerList : answersList) {
                    if (grid.containsKey(answerList)) {
                        (grid.get(answerList)).add(total);
                    }
                    else {
                        ArrayList<Integer> emptyList = new ArrayList<>();
                        emptyList.add(total);
                        grid.put(answerList, emptyList);
                    }
                }
            }
            else {
                totals.add(0);
            }
            int pageScore = allList.get(answer);
            amounts.add(pageScore);
            denominator += pageScore;
        }

        numerator = denominator - replies.size();

        b1.setEnabled(true);
        b2.setEnabled(true);
        b4.setEnabled(true);
        b7.setEnabled(true);
        b9.setEnabled(true);
        b13.setEnabled(true);

        t1.setText("Page " + (counter + 1) + " out of " + (((words - 1) / (rows * columns)) + 1));
        t2.setText("Score: " + score + "/" + number);
        t4.setText("This page: " + numerator + "/" + denominator);
        if (ultimate == null) {
            t5.setText("");
            t6.setText("");
            lastWord = "";
        }
        e2.setText("");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, Math.max(((jumbles.size() - 1) / columns) + 1, 1), GridLayoutManager.HORIZONTAL, false);
        g1.setLayoutManager(layoutManager);

        cusadapter = new CustomAdapter(MainActivity.this, R.layout.cell, jumbles, totals, amounts, columns, font, shuffle, blank);
        if (hidden)
        {
            cusadapter.setHidden(true);
        }
        g1.setAdapter(cusadapter);

        b1.setOnClickListener(view -> {
            String guess = (((e2.getText()).toString()).trim()).toUpperCase();
            if (replies.contains(guess))
            {
                t6.setText("Correct answer");
                t6.setTextColor(Color.rgb(0, 128, 0));

                if ((clear & 1) > 0) {
                    e2.setText("");
                }

                if ((clear & 2) > 0) {
                    lastWord = guess;
                }

                mode = 1;
                ultimate = guess;

                HashSet<String> guesses = new HashSet<>();
                ArrayList<Integer> anagramMap = grid.get(guess);

                for (int index : anagramMap) {
                    String blankMap = (blank ? guess + " " + jumbles.get(index) : guess);
                    guesses.add(blankMap);
                    identities.remove(blankMap);
                    totals.set(index, totals.get(index) - 1);
                    cusadapter.notifyItemChanged(index);
                }

                cumulativeTime(guesses, true, null);
                selectedAnagram = jumbles.get(anagramMap.get(anagramMap.size() - 1));
                String blankMaps = (blank ? guess + " " + selectedAnagram : guess);
                ArrayList<String> hook = db.getDefinition(blankMaps, blank);
                String meaning = hook.get(0);
                String back = hook.get(1);
                String front = hook.get(2);
                String lexicons = hook.get(3);
                HashMap<String, String> colourList = db.getColours();
                String coloursList = db.getLabel(blankMaps, blank);
                String amount;

                if (colourList.containsKey(coloursList) || colourList.containsKey("")) {
                    String coloured = (colourList.containsKey(coloursList) ? colourList.get(coloursList) : colourList.get(""));
                    amount = "<font color=\"" + coloured + "\"><b><small>" + front + "</small> " + guess + " <small>" + back + "</small></b> " + meaning + " <b>" + (coloursList.isEmpty() ? "(No Tag)" : coloursList) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(guess) : "") + "</font>";
                } else {
                    amount = "<b><small>" + front + "</small> " + guess + " <small>" + back + "</small></b> " + meaning + " <b>" + (coloursList.isEmpty() ? "(No Tag)" : coloursList) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(guess) : "");
                }

                t5.setText(Html.fromHtml(amount));
                replies.remove(guess);
                int thisPage = anagramMap.size();
                score += thisPage;
                numerator += thisPage;
                t2.setText("Score: " + score + "/" + number);
                t4.setText("This page: " + numerator + "/" + denominator);
            }
            else
            {
                boolean exist = db.containsWord(guess, blank);
                ArrayList<String> wrongAnswers = new ArrayList<>();

                if (blank) {
                    ArrayList<String> blankAnagrams = db.getBlankAlphagrams(guess);
                    for (String blankAnagram : blankAnagrams) {
                        if (allList.containsKey(blankAnagram)) {
                            wrongAnswers.add(blankAnagram);
                        }
                    }
                }
                else {
                    char[] character = guess.toCharArray();
                    Arrays.sort(character);
                    String wrongAnswer = new String(character);

                    if (allList.containsKey(wrongAnswer)) {
                        wrongAnswers.add(wrongAnswer);
                    }
                }

                if (!exist) {
                    t6.setTextColor(Color.RED);
                    if (!wrongAnswers.isEmpty()) {
                        t6.setText("Wrong answer");
                        db.trackWrongAnswers(wrongAnswers, guess, blank);

                        if ((clear & 4) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 8) > 0) {
                            lastWord = guess;
                        }
                    }
                    else {
                        t6.setText("Anagram not here");

                        if ((clear & 64) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 128) > 0) {
                            lastWord = guess;
                        }
                    }
                }
                else {
                    t6.setTextColor(Color.rgb(0, 128, 0));
                    if (!wrongAnswers.isEmpty()) {
                        t6.setText("Already solved");

                        if ((clear & 16) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 32) > 0) {
                            lastWord = guess;
                        }
                    }
                    else {
                        t6.setText("Anagram not here");

                        if ((clear & 64) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 128) > 0) {
                            lastWord = guess;
                        }
                    }
                }
            }
        });

        b2.setOnClickListener(view -> {
            mode = 0;
            ultimate = null;
            selectedAnagram = null;

            if (counter == (words - 1) / (rows * columns)) {
                counter = 0;
            }
            else {
                counter++;
            }
            db.updateCounter(letters, label, counter, solvedStatus, orderBy, blank);
            nextWord(blank);
        });

        b3.setOnClickListener(view -> getWordLength());

        b4.setOnClickListener(view -> {
            mode = 0;
            ultimate = null;
            selectedAnagram = null;

            if (counter == 0) {
                counter = (words - 1) / (rows * columns);
            }
            else {
                counter--;
            }
            db.updateCounter(letters, label, counter, solvedStatus, orderBy, blank);
            nextWord(blank);
        });

        b5.setOnClickListener(view -> {
            cumulativeTime(null, false, null);
            Intent intent1 = new Intent(MainActivity.this, Report.class);
            startActivity(intent1);
            closeCursor();
            finish();
        });

        b6.setOnClickListener(view -> terminate());

        b7.setOnClickListener(view -> {
            String guess = (((e2.getText()).toString()).trim()).toUpperCase();
            if (replies.contains(guess))
            {
                t6.setText("Yet to submit");
                t6.setTextColor(Color.rgb(0, 128, 0));

                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View yourCustomView = inflater.inflate(R.layout.output, null);

                EditText e5 = yourCustomView.findViewById(R.id.edittext5);
                final int[] selection = {0};
                ArrayList<Integer> anagramMap = grid.get(guess);

                Spinner s2 = yourCustomView.findViewById(R.id.spinner3);
                List<Pair<String, String>> labelsList = db.getAllLabels();

                ColourAdapter comboBoxAdapter = new ColourAdapter(MainActivity.this, R.layout.colour, R.id.textview62, labelsList, MainActivity.this, true, combo);
                s2.setAdapter(comboBoxAdapter);

                s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selection[0]++;

                        if (selection[0] <= 1) {
                            String newTag = db.getLabel(blank ? guess + " " + jumbles.get(anagramMap.get(anagramMap.size() - 1)) : guess, blank);
                            e5.setText(newTag);

                            if (labelsList.contains(newTag)) {
                                int choice = labelsList.lastIndexOf(newTag);
                                s2.setSelection(choice);
                            }
                        }
                        else {
                            e5.setText((labelsList.get(i)).first);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Set tag for " + guess)
                        .setView(yourCustomView)
                        .setPositiveButton("OK", (dialog1, whichButton) -> {
                            mode = 1;
                            ultimate = guess;

                            t6.setText("Correct answer");
                            t6.setTextColor(Color.rgb(0, 128, 0));

                            if ((clear & 1) > 0) {
                                e2.setText("");
                            }

                            if ((clear & 2) > 0) {
                                lastWord = guess;
                            }

                            String cardbox = (e5.getText()).toString();
                            HashSet<String> guesses = new HashSet<>();

                            for(int index : anagramMap) {
                                String blankMap = (blank ? guess + " " + jumbles.get(index) : guess);
                                guesses.add(blankMap);
                                identities.remove(blankMap);
                                totals.set(index, totals.get(index) - 1);
                                cusadapter.notifyItemChanged(index);
                            }

                            cumulativeTime(guesses, true, cardbox);
                            selectedAnagram = jumbles.get(anagramMap.get(anagramMap.size() - 1));
                            displayDefinition(cardbox);
                            replies.remove(guess);
                            int thatPage = anagramMap.size();
                            score += thatPage;
                            numerator += thatPage;
                            t2.setText("Score: " + score + "/" + number);
                            t4.setText("This page: " + numerator + "/" + denominator);
                        }).create();
                dialog.show();
            }
            else
            {
                boolean exist = db.containsWord(guess, blank);
                ArrayList<String> wrongAnswers = new ArrayList<>();

                if (blank) {
                    ArrayList<String> blankAnagrams = db.getBlankAlphagrams(guess);
                    for (String blankAnagram : blankAnagrams) {
                        if (allList.containsKey(blankAnagram)) {
                            wrongAnswers.add(blankAnagram);
                        }
                    }
                }
                else {
                    char[] character = guess.toCharArray();
                    Arrays.sort(character);
                    String wrongAnswer = new String(character);

                    if (allList.containsKey(wrongAnswer)) {
                        wrongAnswers.add(wrongAnswer);
                    }
                }

                if (!exist) {
                    t6.setTextColor(Color.RED);
                    if (!wrongAnswers.isEmpty()) {
                        t6.setText("Wrong answer");
                        db.trackWrongAnswers(wrongAnswers, guess, blank);

                        if ((clear & 4) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 8) > 0) {
                            lastWord = guess;
                        }
                    }
                    else {
                        t6.setText("Anagram not here");

                        if ((clear & 64) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 128) > 0) {
                            lastWord = guess;
                        }
                    }
                }
                else {
                    t6.setTextColor(Color.rgb(0, 128, 0));
                    if (!wrongAnswers.isEmpty()) {
                        t6.setText("Already solved");

                        if ((clear & 16) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 32) > 0) {
                            lastWord = guess;
                        }
                    }
                    else {
                        t6.setText("Anagram not here");

                        if ((clear & 64) > 0) {
                            e2.setText("");
                        }

                        if ((clear & 128) > 0) {
                            lastWord = guess;
                        }
                    }
                }
            }
        });

        b9.setOnClickListener(view -> {
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
                            int page = (pages.isEmpty() ? 0 : Integer.parseInt(pages));
                            if (page < 1 || page > maximum)
                            {
                                Toast.makeText(MainActivity.this, "Enter a value between 1 and " + maximum, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                mode = 0;
                                ultimate = null;
                                selectedAnagram = null;

                                counter = page - 1;
                                db.updateCounter(letters, label, counter, solvedStatus, orderBy, blank);
                                nextWord(blank);
                            }
                        }
                    }).create();
            dialog.show();
        });
    }

    public void revise()
    {
        String revision = ((jumbles == null) ? "" : db.getSummary(jumbles, blank));
        t5.setText(Html.fromHtml(revision));
        db.messageBox("Page Summary", revision, MainActivity.this);
    }

    public void cumulativeTime(HashSet<String> guesses, boolean submitted, String cardbox)
    {
        long stop = System.currentTimeMillis();
        double time = stop - begin;
        time /= 1000;
        db.updateTime((guesses == null) ? (blank ? identities : replies) : guesses, time, submitted, cardbox, blank);
    }

    public void onItemClick(int i) {
        mode = 2;
        ultimate = jumbles.get(i);
        selectedAnagram = ultimate;

        String solved = db.getSolvedAnswers(ultimate, blank);
        t5.setText(Html.fromHtml(solved));
    }

    public void onItemLongClick(int i) {
        String unsolved = jumbles.get(i);
        String unsolvedAnswers = db.getUnsolvedWords(unsolved, blank);

        db.messageBox("Unsolved answers", unsolvedAnswers, MainActivity.this);
    }

    public void refresh()
    {
        ArrayList<Integer> dimensions = db.getZoom("Quiz");
        rows = dimensions.get(0);
        columns = dimensions.get(1);
        font = dimensions.get(2);
        combo = dimensions.get(3);
        maximumWordLength = db.getMaximumWordLength(false);
        maximumBlankLength = db.getMaximumWordLength(true);

        try
        {
            if (started) {
                int[] pair3 = (letters == 1 ? db.getCustomScore(label, solvedStatus, blank) : db.getScore(letters, label, solvedStatus, blank));
                score = pair3[0];

                closeCursor();
                anagrams = (letters == 1 ? db.getCustomQuiz(label, MainActivity.this, solvedStatus, orderBy, blank) : db.getAllAnagrams(letters, label, solvedStatus, orderBy, blank));
                words = anagrams.getCount();

                counter = db.getCounter(letters, label, solvedStatus, orderBy, blank);
                number = pair3[1];

                int peak = (words - 1) / (rows * columns);
                if (counter > peak && words > 0) {
                    counter = peak;
                    db.updateCounter(letters, label, counter, solvedStatus, orderBy, blank);
                }

                nextWord(blank);
                refreshDefinition();
            }
        }
        catch (Exception ignored)
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
                    String tag = db.getLabel(blank ? ultimate + " " + selectedAnagram : ultimate, blank);
                    displayDefinition(tag);
                    break;
                case 2:
                    String solved = db.getSolvedAnswers(ultimate, blank);
                    t5.setText(Html.fromHtml(solved));
                    break;
                case 3:
                    String revision = db.getSummary(jumbles, blank);
                    t5.setText(Html.fromHtml(revision));
                    break;
            }
        }
    }

    public void displayDefinition(String listbox)
    {
        ArrayList<String> hook = db.getDefinition(blank ? ultimate + " " + selectedAnagram : ultimate, blank);
        String meaning = hook.get(0);
        String back = hook.get(1);
        String front = hook.get(2);
        String lexicons = hook.get(3);

        HashMap<String, String> colours = db.getColours();
        String amount;

        if (colours.containsKey(listbox) || colours.containsKey("")) {
            String colour = (colours.containsKey(listbox) ? colours.get(listbox) : colours.get(""));
            amount = "<font color=\"" + colour + "\"><b><small>" + front + "</small> " + ultimate + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.isEmpty() ? "(No Tag)" : listbox) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(ultimate) : "") + "</font>";
        } else {
            amount = "<b><small>" + front + "</small> " + ultimate + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.isEmpty() ? "(No Tag)" : listbox) + " " + lexicons + "</b>" + (detail ? db.getFullDetails(ultimate) : "");
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
        EditText e16 = yourCustomView.findViewById(R.id.edittext30);

        e8.setHint("Enter a value greater than 0");
        e9.setHint("Enter a value greater than 0");
        e10.setHint("Enter a value greater than 11");
        e16.setHint("Enter a value greater than 11");

        e8.setText(Integer.toString(rows));
        e9.setText(Integer.toString(columns));
        e10.setText(Integer.toString(font));
        e16.setText(Integer.toString(combo));

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Change rows, columns and font sizes")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String old_rows = (e8.getText()).toString();
                    String old_columns = (e9.getText()).toString();
                    String old_font = (e10.getText()).toString();
                    String old_combo = (e16.getText()).toString();

                    int new_rows = (old_rows.isEmpty() ? 0 : Integer.parseInt(old_rows));
                    int new_columns = (old_columns.isEmpty() ? 0 : Integer.parseInt(old_columns));
                    int new_font = (old_font.isEmpty() ? 0 : Integer.parseInt(old_font));
                    int new_combo = (old_combo.isEmpty() ? 0 : Integer.parseInt(old_combo));

                    StringBuilder sb = new StringBuilder();
                    if (new_rows < 1 && new_columns < 1) {
                        sb.append("Rows, columns should be ≥ 1");
                    }
                    else if (new_rows < 1) {
                        sb.append("Rows should be ≥ 1");
                    }
                    else if (new_columns < 1) {
                        sb.append("Columns should be ≥ 1");
                    }
                    if (new_font < 11 || new_combo < 11) {
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        sb.append("Font sizes should be ≥ 11");
                    }

                    if (sb.length() > 0)
                    {
                        Toast.makeText(MainActivity.this, new String(sb), Toast.LENGTH_LONG).show();
                        zoom();
                    }
                    else
                    {
                        db.setZoom("Quiz", new_rows, new_columns, new_font, new_combo);
                        refresh();
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
        final View yourCustomView = inflater.inflate(R.layout.filter, null);

        EditText e11 = yourCustomView.findViewById(R.id.edittext6);
        EditText e12 = yourCustomView.findViewById(R.id.edittext7);
        TextView t7 = yourCustomView.findViewById(R.id.textview12);
        e12.setHint("Enter a value between 2 and " + maximumWordLength);

        Spinner s23 = yourCustomView.findViewById(R.id.spinner35);
        ArrayAdapter<String> jokerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankList);
        jokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s23.setAdapter(jokerAdapter);

        final int[] sortIndex = new int[3];
        Spinner s13 = yourCustomView.findViewById(R.id.spinner25);
        ArrayAdapter<String> aggregateAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, aggregate);
        aggregateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s13.setAdapter(aggregateAdapter);

        Spinner s14 = yourCustomView.findViewById(R.id.spinner26);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankColumns);
        ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s14.setAdapter(orderAdapter);

        Spinner s15 = yourCustomView.findViewById(R.id.spinner27);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s15.setAdapter(sortAdapter);

        s23.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                e12.setHint("Enter a value between 2 and " + ((i == 0) ? maximumWordLength : maximumBlankLength));

                if (i == 0) {
                    s14.setAdapter(orderAdapter);
                    s14.setSelection(6);
                    s15.setSelection(1);
                }
                else {
                    s14.setAdapter(ordersAdapter);
                    s14.setSelection(1);
                    s15.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s13.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s14.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[1] = i;

                if (i < 2) {
                    s13.setVisibility(View.GONE);
                }
                else {
                    s13.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s15.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[2] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s14.setSelection(6);
        s15.setSelection(1);

        final int[] solved = {2};
        Spinner s6 = yourCustomView.findViewById(R.id.spinner6);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, solvedList);
        solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s6.setAdapter(solvedAdapter);
        s6.setSelection(2);

        s6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                solved[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner s3 = yourCustomView.findViewById(R.id.spinner1);
        List<Pair<String, String>> tagsList = db.getAllLabels();
        tagsList.add(0, new Pair<>("(All Tags)", null));

        ColourAdapter spinnerAdapter = new ColourAdapter(MainActivity.this, R.layout.colour, R.id.textview62, tagsList, MainActivity.this, true, combo);
        s3.setAdapter(spinnerAdapter);

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e11.setText("*");
                }
                else {
                    e11.setText((tagsList.get(i)).first);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final int[] lengthIndex = new int[1];
        Spinner s4 = yourCustomView.findViewById(R.id.spinner8);
        ArrayList<String> lengthList = new ArrayList<>();
        lengthList.add(0, "Specific word length");
        lengthList.add(1, "All word lengths");

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, lengthList);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String intermediate = (e11.getText()).toString();
                    String alphabets = (lengthIndex[0] == 0 ? (e12.getText()).toString() : "-1");
                    int temporary = (alphabets.isEmpty() ? 0 : Integer.parseInt(alphabets));
                    boolean wilds = (s23.getSelectedItemPosition() > 0);

                    if (lengthIndex[0] == 0 && temporary < 2)
                    {
                        Toast.makeText(MainActivity.this, "Enter a value between 2 and " + (wilds ? maximumBlankLength : maximumWordLength) + " for word length", Toast.LENGTH_LONG).show();
                        filterByLabel();
                    }
                    else
                    {
                        letters = temporary;
                        label = intermediate;
                        orderBy = sortBy(sortIndex, wilds);
                        ultimate = null;
                        selectedAnagram = null;
                        mode = 0;
                        solvedStatus = solved[0];
                        start(wilds);
                    }
                }).create();
        dialog.show();
    }

    public void getAllSubanagrams(boolean subanagram)
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View yourCustomView3 = inflater.inflate(R.layout.subanagram, null);

        EditText e13 = yourCustomView3.findViewById(R.id.edittext19);
        EditText e14 = yourCustomView3.findViewById(R.id.edittext20);
        EditText e15 = yourCustomView3.findViewById(R.id.edittext29);
        CheckBox c4 = yourCustomView3.findViewById(R.id.checkbox4);
        FrameLayout f3 = yourCustomView3.findViewById(R.id.framelayout3);

        LayoutInflater subinflater3 = LayoutInflater.from(MainActivity.this);
        final View subCustomView3 = subinflater3.inflate(R.layout.sieve, null);
        f3.addView(subCustomView3);

        db.addFunctionalities(MainActivity.this, e13, c4, false, subCustomView3);

        Spinner s25 = yourCustomView3.findViewById(R.id.spinner37);
        ArrayAdapter<String> subanagramAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, jokerList);
        subanagramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s25.setAdapter(subanagramAdapter);

        final int[] sortIndex = new int[3];
        Spinner s19 = yourCustomView3.findViewById(R.id.spinner31);
        ArrayAdapter<String> aggregateAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, aggregate);
        aggregateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s19.setAdapter(aggregateAdapter);

        Spinner s20 = yourCustomView3.findViewById(R.id.spinner32);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, blankColumns);
        ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s20.setAdapter(orderAdapter);

        Spinner s21 = yourCustomView3.findViewById(R.id.spinner33);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s21.setAdapter(sortAdapter);

        s19.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s20.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[1] = i;

                if (i < 2) {
                    s19.setVisibility(View.GONE);
                }
                else {
                    s19.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s21.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[2] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s25.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    s20.setAdapter(orderAdapter);
                    s20.setSelection(subanagram ? 3 : 6);

                    if (!subanagram) {
                        s21.setSelection(1);
                    }
                }
                else {
                    s20.setAdapter(ordersAdapter);
                    s20.setSelection(subanagram ? 3 : 1);

                    if (!subanagram) {
                        s21.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s20.setSelection(subanagram ? 3 : 6);
        s21.setSelection(1);

        final int[] solved = {2};
        Spinner s7 = yourCustomView3.findViewById(R.id.spinner10);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, solvedList);
        solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s7.setAdapter(solvedAdapter);
        s7.setSelection(2);

        s7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                solved[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(subanagram ? "Search for subanagrams" : "Search for anagrams")
                .setView(yourCustomView3)
                .setPositiveButton("OK", (dialog1, whichButton) -> subanagrams((((e13.getText()).toString()).trim()).toUpperCase(), (e14.getText()).toString(), subanagram, ((e15.getText()).toString()).replace("\"", "'"), c4.isChecked(), sortIndex, solved, true, s25.getSelectedItemPosition())).create();
        dialog.show();
    }

    public void subanagrams(String letterSequence, String digit, boolean subanagram, String extra, boolean autoUnderscore, int[] sortIndex, int[] solved, boolean extraSql, int blankIndex) {
        boolean flag = false;
        int blanks = 0;
        for (int digits = 0; digits < letterSequence.length(); digits++) {
            int flags = letterSequence.charAt(digits);
            if (flags == 46) {
                blanks++;
            }
            else if (flags < 65 || flags > 90) {
                flag = true;
                break;
            }
        }

        blanks += (digit.isEmpty() ? 0 : Integer.parseInt(digit));

        if (flag && extraSql) {
            Toast.makeText(MainActivity.this, "Letters field can contain only letters and dots for blanks", Toast.LENGTH_LONG).show();
            getAllSubanagrams(subanagram);
        }
        else {
            String letter;
            StringBuilder theQuery = new StringBuilder();

            if (flag) {
                letter = "";
                blanks = 0;
            }
            else {
                letter = letterSequence.replace(".", "");
            }

            if (subanagram) {
                int[] occurrence = new int[26];
                for (int myRadix = 0; myRadix < letter.length(); myRadix++) {
                    char theCharacter = letter.charAt(myRadix);
                    occurrence[theCharacter - 65]++;
                }

                for (int theRadix = 0; theRadix < 26; theRadix++) {
                    char occurrences = (char) (theRadix + 97);
                    theQuery.append(blankIndex == 1 ? "_total_" : "_no_").append(occurrences).append("_ <= ").append(occurrence[theRadix] + blanks).append(" AND ");
                }

                for (int myIndex = 0; myIndex < 26; myIndex++) {
                    char occurrences = (char) (myIndex + 97);
                    theQuery.append(myIndex == 0 ? "" : " + ").append(blankIndex == 1 ? "ABS(_total_" : "ABS(_no_").append(occurrences).append("_ - ").append(occurrence[myIndex]).append(")");
                }
                theQuery.append(" <= ").append((2 * blanks) + letter.length()).append(" - _length_");
            } else {
                char[] myCharacter = letter.toCharArray();
                Arrays.sort(myCharacter);
                StringBuilder empties = new StringBuilder();
                for (char myLetter : myCharacter) {
                    empties.append("%").append(myLetter);
                }
                empties.append("%");
                String empty = new String(empties);
                theQuery.append("_length_ = ").append(letter.length() + blanks).append(blankIndex == 2 ? " AND _anagram_ LIKE '" : " AND _alphagram_ LIKE '").append(empty).append("'");
            }

            if (!extra.isEmpty()) {
                theQuery.append(" AND (").append(autoUnderscore ? db.addUnderscores(extra) : extra).append(")");
            }

            String customQuery = new String(theQuery);
            boolean wildsIndex = (blankIndex > 0);
            String subanagramIndex = sortBy(sortIndex, wildsIndex);
            Cursor resultSet = db.getCustomQuiz(customQuery, MainActivity.this, solved[0], subanagramIndex, wildsIndex);

            if (resultSet != null) {
                label = customQuery;
                letters = 1;
                ultimate = null;
                selectedAnagram = null;
                mode = 0;
                solvedStatus = solved[0];
                orderBy = subanagramIndex;

                closeCursor();
                anagrams = resultSet;
                words = anagrams.getCount();
                int[] pair4 = db.getCustomScore(label, solved[0], wildsIndex);
                score = pair4[0];
                number = pair4[1];

                boolean exists = db.existLabel(letters, label, orderBy, wildsIndex);

                if (!exists) {
                    counter = 0;
                    db.insertLabel(letters, label, orderBy, wildsIndex);
                } else {
                    counter = db.getCounter(letters, label, solvedStatus, orderBy, wildsIndex);
                }

                int apex = (words - 1) / (rows * columns);
                if (counter > apex && words > 0) {
                    counter = apex;
                    db.updateCounter(letters, label, counter, solvedStatus, orderBy, wildsIndex);
                }

                nextWord(wildsIndex);
            }
        }
    }

    public String sortBy(int[] selection, boolean isBlank) {
        switch (selection[1]) {
            case 0: return (selection[2] == 1 ? "DESC" : "ASC");
            case 1: return " ORDER BY RANDOM()" + (selection[2] == 1 ? " DESC" : "");
            default: switch (selection[0]) {
                case 1:
                    return " ORDER BY MAX(_" + (isBlank ? blankColumns.get(selection[1]) : allColumns.get(selection[1])) + "_)" + (selection[2] == 1 ? " DESC" : "");
                case 2:
                    return " ORDER BY MIN(_" + (isBlank ? blankColumns.get(selection[1]) : allColumns.get(selection[1])) + "_)" + (selection[2] == 1 ? " DESC" : "");
                case 3:
                    return " ORDER BY AVG(_" + (isBlank ? blankColumns.get(selection[1]) : allColumns.get(selection[1])) + "_)" + (selection[2] == 1 ? " DESC" : "");
                case 4:
                    return " ORDER BY SUM(_" + (isBlank ? blankColumns.get(selection[1]) : allColumns.get(selection[1])) + "_)" + (selection[2] == 1 ? " DESC" : "");
                case 5:
                    return " ORDER BY COUNT(_" + (isBlank ? blankColumns.get(selection[1]) : allColumns.get(selection[1])) + "_)" + (selection[2] == 1 ? " DESC" : "");
                default:
                    return " ORDER BY _" + (isBlank ? blankColumns.get(selection[1]) : allColumns.get(selection[1])) + "_" + (selection[2] == 1 ? " DESC" : "");
            }
        }
    }

    public void terminate() {
        cumulativeTime(null, false, null);
        closeCursor();
        finish();
    }
}