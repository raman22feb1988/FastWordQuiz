package com.tuchwords.wordquiz;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
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

public class Report extends AppCompatActivity {
    sqliteDB db;
    int letters = 0;
    int columns = 20;
    String label = "*";
    String tag = "(No action)";
    int solvedStatus = 0;
    boolean hidden;
    boolean detail;
    int clear;
    int shuffle;
    boolean started;
    String orderBy;
    HashMap<String, String> dictionary;
    HashMap<String, Integer> anagramsList;
    HashMap<String, String> lexicon;
    ArrayList<String> jumbles;
    HashMap<String, String> colourList;
    List<Pair<String, String>> labelsList;
    ArrayList<String> solvedList;
    ArrayList<String> sort;
    ArrayList<String> allColumns;
    ArrayList<String> blankColumns;
    ArrayList<String> blankList;
    ArrayList<String> jokerList;
    ReportAdapter reportAdapter;
    SharedPreferences pref;

    TextView t1;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    Button b6;
    Button b7;
    Button b8;
    RecyclerView g1;
    Spinner s3;

    Cursor anagrams;
    int words;
    int counter;
    boolean blank;

    int rows;
    int font;
    int combo;
    int loader;
    int maximumWordLength;
    int maximumBlankLength;
    long filterSerial;

    // Declare the DrawerLayout, NavigationView and Toolbar
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        // Initialize the DrawerLayout, NavigationView and Toolbar
        drawerLayout = findViewById(R.id.drawer_layout_report);
        NavigationView navigationView = findViewById(R.id.nav_view_report);
        Toolbar toolbar = findViewById(R.id.toolbar_report);

        pref = getApplicationContext().getSharedPreferences("AppData", 0);
        boolean prepared = pref.getBoolean("prepared", false);
        hidden = pref.getBoolean("hidden", false);
        detail = pref.getBoolean("detail", false);
        int version = pref.getInt("version", 1);
        clear = pref.getInt("clear", 255);
        shuffle = pref.getInt("shuffle", 0);
        Menu menu = navigationView.getMenu();

        if (hidden)
        {
            MenuItem menuItem = menu.findItem(R.id.button37);
            menuItem.setTitle("Show number of answers");
        }

        if (detail)
        {
            MenuItem menuItem = menu.findItem(R.id.button52);
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
                case R.id.button18:
                    // Show a Toast message for the Custom query item
                    LayoutInflater inflater = LayoutInflater.from(Report.this);
                    final View yourCustomView1 = inflater.inflate(R.layout.query, null);

                    EditText e2 = yourCustomView1.findViewById(R.id.edittext18);
                    CheckBox c2 = yourCustomView1.findViewById(R.id.checkbox2);
                    FrameLayout f1 = yourCustomView1.findViewById(R.id.framelayout1);

                    LayoutInflater subinflater1 = LayoutInflater.from(Report.this);
                    final View subCustomView1 = subinflater1.inflate(R.layout.sieve, null);
                    f1.addView(subCustomView1);

                    db.addFunctionalities(Report.this, e2, c2, false, subCustomView1);

                    Spinner s24 = yourCustomView1.findViewById(R.id.spinner36);
                    ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankList);
                    emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s24.setAdapter(emptyAdapter);

                    final int[] sortIndex = new int[2];
                    Spinner s16 = yourCustomView1.findViewById(R.id.spinner28);
                    s16.setVisibility(View.GONE);

                    Spinner s17 = yourCustomView1.findViewById(R.id.spinner29);
                    ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, allColumns);
                    orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankColumns);
                    ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s17.setAdapter(orderAdapter);

                    Spinner s18 = yourCustomView1.findViewById(R.id.spinner30);
                    ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, sort);
                    sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s18.setAdapter(sortAdapter);

                    s17.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            sortIndex[0] = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    s18.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            sortIndex[1] = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    s17.setSelection(7);
                    s18.setSelection(1);

                    final int[] solved = {0};
                    Spinner s6 = yourCustomView1.findViewById(R.id.spinner7);
                    ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, solvedList);
                    solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s6.setAdapter(solvedAdapter);

                    s6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            solved[0] = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    AlertDialog dialog6 = new AlertDialog.Builder(Report.this)
                            .setTitle("SELECT front, word, back, definition, time, tag FROM words WHERE")
                            .setView(yourCustomView1)
                            .setPositiveButton("OK", (dialog5, whichButton) -> {
                                String temporaryQuery = ((e2.getText()).toString()).replace("\"", "'");
                                String customQuery = (temporaryQuery.isEmpty() ? "1" : temporaryQuery);
                                boolean wildIndex = (s24.getSelectedItemPosition() > 0);
                                String orderIndex = sortBy(sortIndex, wildIndex);
                                execute(c2.isChecked(), customQuery, orderIndex, solved[0], wildIndex);
                            }).create();
                    dialog6.show();

                    s24.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            dialog6.setTitle("SELECT front, word, back, definition, time, tag FROM " + ((i == 0) ? "words" : "blanks") + " WHERE");

                            if (i == 0) {
                                s17.setAdapter(orderAdapter);
                                s17.setSelection(7);
                            }
                            else {
                                s17.setAdapter(ordersAdapter);
                                s17.setSelection(10);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    break;
                case R.id.button27:
                    // Show a Toast message for the SQL query item
                    LayoutInflater inflater1 = LayoutInflater.from(Report.this);
                    final View yourCustomView2 = inflater1.inflate(R.layout.sqlquery, null);

                    EditText e5 = yourCustomView2.findViewById(R.id.edittext8);
                    CheckBox c3 = yourCustomView2.findViewById(R.id.checkbox3);
                    FrameLayout f2 = yourCustomView2.findViewById(R.id.framelayout2);

                    LayoutInflater subinflater2 = LayoutInflater.from(Report.this);
                    final View subCustomView2 = subinflater2.inflate(R.layout.sieve, null);
                    f2.addView(subCustomView2);

                    db.addFunctionalities(Report.this, e5, c3, true, subCustomView2);

                    AlertDialog dialog1 = new AlertDialog.Builder(Report.this)
                            .setTitle("Enter your SQL query")
                            .setView(yourCustomView2)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String sqlQuery = (e5.getText()).toString();

                                    if (!sqlQuery.isEmpty()) {
                                        db.myQuery(c3.isChecked() ? db.addUnderscores(sqlQuery) : sqlQuery, Report.this, false);
                                    }
                                }
                            }).create();
                    dialog1.show();
                    break;
                case R.id.button32:
                    // Show a Toast message for the View all tag colours item
                    String labelColours = db.getLabelColours(Report.this);
                    db.messageBox("Tag colours", labelColours, Report.this);
                    break;
                case R.id.button30:
                    // Show a Toast message for the Export tags item
                    db.exportLabels(Report.this, false);
                    break;
                case R.id.button31:
                    // Show a Toast message for the Import tags item
                    db.importLabels(Report.this, false);
                    break;
                case R.id.button28:
                    // Show a Toast message for the Export CSV item
                    db.exportDB(Report.this, false);
                    break;
                case R.id.button29:
                    // Show a Toast message for the Import CSV item
                    db.importDB(Report.this, false);
                    break;
                case R.id.button33:
                    // Show a Toast message for the Change tag of a word item
                    LayoutInflater inflater2 = LayoutInflater.from(Report.this);
                    final View yourCustomView5 = inflater2.inflate(R.layout.label, null);

                    EditText e3 = yourCustomView5.findViewById(R.id.edittext3);
                    EditText e4 = yourCustomView5.findViewById(R.id.edittext4);
                    Spinner s26 = yourCustomView5.findViewById(R.id.spinner39);

                    final ArrayList<String>[] anagramItem = new ArrayList[]{new ArrayList<>()};
                    ArrayAdapter<String> anagramAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, anagramItem[0]);
                    anagramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s26.setAdapter(anagramAdapter);

                    e3.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            anagramItem[0] = db.getBlankAnagrams((s.toString()).toUpperCase());
                            ArrayAdapter<String> alphagramAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, anagramItem[0]);
                            alphagramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            s26.setAdapter(alphagramAdapter);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    Spinner s2 = yourCustomView5.findViewById(R.id.spinner2);
                    List<Pair<String, String>> tagList = new ArrayList<>(labelsList.subList(1, labelsList.size()));

                    ColourAdapter spinnerAdapter = new ColourAdapter(Report.this, R.layout.colour, R.id.textview62, tagList, Report.this, true, combo);
                    s2.setAdapter(spinnerAdapter);

                    s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            e4.setText((tagList.get(i)).first);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    AlertDialog dialog2 = new AlertDialog.Builder(Report.this)
                            .setTitle("Change tag")
                            .setView(yourCustomView5)
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                String line = (((e3.getText()).toString()).trim()).toUpperCase();
                                String category = (e4.getText()).toString();
                                int anagramIndex = s26.getSelectedItemPosition();

                                if (anagramIndex == 0) {
                                    db.updateTag(line, category, false);
                                } else {
                                    db.updateTag(line + " " + anagramItem[0].get(anagramIndex), category, true);
                                }

                                refresh();
                            }).create();
                    dialog2.show();
                    break;
                case R.id.button35:
                    // Show a Toast message for the Change rows and font size item
                    zoom();
                    break;
                case R.id.button37:
                    // Show a Toast message for the Hide and show number of answers item
                    if (hidden) {
                        hidden = false;
                        item.setTitle("Hide number of answers");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("hidden", false);
                        editor.apply();
                    } else {
                        hidden = true;
                        item.setTitle("Show number of answers");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("hidden", true);
                        editor.apply();
                    }
                    break;
                case R.id.button40:
                    // Show a Toast message for the Reset words by tag item
                    db.resetByLabel(Report.this, false, blankList, maximumWordLength, maximumBlankLength, combo);
                    break;
                case R.id.button46:
                    // Show a Toast message for the Add new tag item
                    db.addByLabel(Report.this, false);
                    break;
                case R.id.button47:
                    // Show a Toast message for the Rename tag by colour item
                    db.renameByLabel(Report.this, false, false, combo);
                    break;
                case R.id.button48:
                    // Show a Toast message for the Change tag colour by name item
                    db.renameByLabel(Report.this, false, true, combo);
                    break;
                case R.id.button49:
                    // Show a Toast message for the Delete single tag by name item
                    db.deleteByLabel(Report.this, false, true, combo);
                    break;
                case R.id.button50:
                    // Show a Toast message for the Delete single tag by colour item
                    db.deleteByLabel(Report.this, false, false, combo);
                    break;
                case R.id.button52:
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
                    break;
                case R.id.button60:
                    // Show a Toast message for the View all prefixes and suffixes item
                    db.getSuffix(Report.this);
                    break;
                case R.id.button61:
                    // Show a Toast message for the Add new prefix item
                    db.addSuffix(Report.this, false, false, 0);
                    break;
                case R.id.button62:
                    // Show a Toast message for the Change prefix item
                    db.changeSuffix(Report.this, false, false, 0);
                    break;
                case R.id.button63:
                    // Show a Toast message for the Delete single prefix item
                    db.deleteSuffix(Report.this, false, false, 0);
                    break;
                case R.id.button64:
                    // Show a Toast message for the Add new suffix item
                    db.addSuffix(Report.this, false, true, 0);
                    break;
                case R.id.button65:
                    // Show a Toast message for the Change suffix item
                    db.changeSuffix(Report.this, false, true, 0);
                    break;
                case R.id.button66:
                    // Show a Toast message for the Delete single suffix item
                    db.deleteSuffix(Report.this, false, true, 0);
                    break;
                case R.id.button70:
                    // Show a Toast message for the Delete all tags item
                    db.deleteAllRecords(Report.this, false, "colours", 0);
                    break;
                case R.id.button71:
                    // Show a Toast message for the Delete all prefixes item
                    db.deleteAllRecords(Report.this, false, "prefixes", 0);
                    break;
                case R.id.button72:
                    // Show a Toast message for the Delete all suffixes item
                    db.deleteAllRecords(Report.this, false, "suffixes", 0);
                    break;
                case R.id.button76:
                    // Show a Toast message for the Prepare regular database item
                    promptDictionary(true, false);
                    break;
                case R.id.button79:
                    // Show a Toast message for the Search for anagrams item
                    getAllSubanagrams(false);
                    break;
                case R.id.button80:
                    // Show a Toast message for the Search for subanagrams item
                    getAllSubanagrams(true);
                    break;
                case R.id.button83:
                    // Show a Toast message for the Prepare blank database item
                    promptDictionary(false, true);
                    break;
                case R.id.button85:
                    // Show a Toast message for the Clear answers on submit item
                    LayoutInflater inflater3 = LayoutInflater.from(Report.this);
                    final View yourCustomView6 = inflater3.inflate(R.layout.clear, null);

                    CheckBox[] checkBoxes = {yourCustomView6.findViewById(R.id.checkbox5),
                            yourCustomView6.findViewById(R.id.checkbox6),
                            yourCustomView6.findViewById(R.id.checkbox7),
                            yourCustomView6.findViewById(R.id.checkbox8),
                            yourCustomView6.findViewById(R.id.checkbox9),
                            yourCustomView6.findViewById(R.id.checkbox10),
                            yourCustomView6.findViewById(R.id.checkbox11),
                            yourCustomView6.findViewById(R.id.checkbox12),
                    };

                    for (int clearIndex = 0; clearIndex < checkBoxes.length; clearIndex++) {
                        if ((clear & (1 << clearIndex)) > 0) {
                            checkBoxes[clearIndex].setChecked(true);
                        }
                    }

                    AlertDialog dialog3 = new AlertDialog.Builder(Report.this)
                            .setTitle("Clear answers on submit")
                            .setView(yourCustomView6)
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
                case R.id.button88:
                    // Show a Toast message for the Shuffle anagrams by item
                    LayoutInflater inflater4 = LayoutInflater.from(Report.this);
                    final View yourCustomView4 = inflater4.inflate(R.layout.shuffle, null);

                    RadioGroup r1 = yourCustomView4.findViewById(R.id.radioGroup1);
                    ((RadioButton) r1.getChildAt(shuffle)).setChecked(true);

                    AlertDialog dialog4 = new AlertDialog.Builder(Report.this)
                            .setTitle("Shuffle anagrams by")
                            .setView(yourCustomView4)
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                RadioButton r2 = yourCustomView4.findViewById(r1.getCheckedRadioButtonId());
                                shuffle = r1.indexOfChild(r2);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("shuffle", shuffle);
                                editor.apply();
                            }).create();
                    dialog4.show();
                    break;
                case R.id.button103:
                    // Show a Toast message for the View all tables and columns item
                    db.messageBox("View all tables and columns", db.getSchema(), Report.this);
                    break;
                case R.id.button105:
                    // Show a Toast message for the View letter distribution item
                    db.letterDistribution(Report.this);
                    break;
                case R.id.button107:
                    // Show a Toast message for the Load saved word list item
                    LayoutInflater inflater6 = LayoutInflater.from(Report.this);
                    final View yourCustomView8 = inflater6.inflate(R.layout.list, null);
                    EditText e16 = yourCustomView8.findViewById(R.id.edittext40);
                    CheckBox c5 = yourCustomView8.findViewById(R.id.checkbox13);
                    Button b9 = yourCustomView8.findViewById(R.id.button108);
                    Button b10 = yourCustomView8.findViewById(R.id.button109);

                    RecyclerView g2 = yourCustomView8.findViewById(R.id.gridview4);
                    RecyclerView.LayoutManager listManager = new LinearLayoutManager(Report.this, LinearLayoutManager.VERTICAL, false);
                    g2.setLayoutManager(listManager);

                    final FilterAdapter[] filterAdapter = {new FilterAdapter(Report.this, R.layout.list, db.loadFilter(""), loader, db)};
                    g2.setAdapter(filterAdapter[0]);

                    Spinner s27 = yourCustomView8.findViewById(R.id.spinner46);
                    ArrayAdapter<String> selectionAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, solvedList);
                    selectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s27.setAdapter(selectionAdapter);
                    s27.setSelection(0);

                    e16.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (c5.isChecked()) {
                                filterAdapter[0] = new FilterAdapter(Report.this, R.layout.list, db.loadFilter(s.toString()), loader, db);
                                g2.setAdapter(filterAdapter[0]);
                            }
                        }
                    });

                    b9.setOnClickListener(v -> {
                        filterAdapter[0] = new FilterAdapter(Report.this, R.layout.list, db.loadFilter((e16.getText()).toString()), loader, db);
                        g2.setAdapter(filterAdapter[0]);
                    });

                    b10.setOnClickListener(v -> {
                        e16.setText("");
                        if (c5.isChecked()) {
                            filterAdapter[0] = new FilterAdapter(Report.this, R.layout.list, db.loadFilter((e16.getText()).toString()), loader, db);
                            g2.setAdapter(filterAdapter[0]);
                        }
                    });

                    c5.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            filterAdapter[0] = new FilterAdapter(Report.this, R.layout.list, db.loadFilter((e16.getText()).toString()), loader, db);
                            g2.setAdapter(filterAdapter[0]);
                        }
                    });

                    AlertDialog dialog9 = new AlertDialog.Builder(Report.this)
                            .setTitle("Load saved word list")
                            .setView(yourCustomView8)
                            .setPositiveButton("OK", (dialog10, whichButton3) -> {
                                Filter filterObject = filterAdapter[0].getSelection();
                                if (filterObject == null) {
                                    db.alertBox("Load saved word list", "No item had been selected.", Report.this);
                                }
                                else {
                                    int numberOfLetters = filterObject.getLength();
                                    if (numberOfLetters == 0) {
                                        execute(false, filterObject.getQuery(), filterObject.getSort(), s27.getSelectedItemPosition(), filterObject.getBlank());
                                    } else {
                                        letters = numberOfLetters;
                                        label = filterObject.getQuery();
                                        solvedStatus = s27.getSelectedItemPosition();
                                        orderBy = filterObject.getSort();
                                        blank = filterObject.getBlank();
                                        start(true);
                                    }
                                }
                            }).create();
                    dialog9.show();
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

        t1 = findViewById(R.id.textview6);
        b1 = findViewById(R.id.button6);
        b2 = findViewById(R.id.button7);
        b3 = findViewById(R.id.button8);
        b4 = findViewById(R.id.button9);
        b5 = findViewById(R.id.button13);
        b6 = findViewById(R.id.button14);
        b7 = findViewById(R.id.button16);
        b8 = findViewById(R.id.button101);
        g1 = findViewById(R.id.gridview2);
        s3 = findViewById(R.id.spinner4);

        db = new sqliteDB(Report.this, version, null, false);

        ArrayList<Integer> dimensions = db.getZoom("List");
        rows = dimensions.get(0);
        font = dimensions.get(2);
        combo = dimensions.get(3);
        loader = dimensions.get(4);
        maximumWordLength = db.getMaximumWordLength(false);
        maximumBlankLength = db.getMaximumWordLength(true);

        refreshSpinner();

        solvedList = new ArrayList<>();
        solvedList.add("Solved words only");
        solvedList.add("Unsolved words only");
        solvedList.add("All words");

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

        b4.setOnClickListener(view -> {
            Intent intent2 = new Intent(Report.this, MainActivity.class);
            startActivity(intent2);
            closeCursor();
            finish();
        });

        b5.setOnClickListener(view -> filterByLabel());

        b6.setOnClickListener(view -> {
            closeCursor();
            finish();
        });

        b8.setOnClickListener(view2 -> {
            LayoutInflater inflater5 = LayoutInflater.from(Report.this);
            final View yourCustomView7 = inflater5.inflate(R.layout.edit, null);

            EditText e14 = yourCustomView7.findViewById(R.id.edittext37);
            e14.setText(db.getFilterName(filterSerial));

            AlertDialog dialog7 = new AlertDialog.Builder(Report.this)
                    .setTitle("Save word list")
                    .setView(yourCustomView7)
                    .setPositiveButton("OK", (dialog8, whichButton2) -> {
                        db.saveFilter(filterSerial, ((e14.getText()).toString()).replace("\"", "'"));
                    }).create();
            dialog7.show();
        });

        if (!prepared) {
            promptDictionary(false, false);
        }

        if (savedInstanceState != null) {
            // Restore your data from the Bundle
            letters = savedInstanceState.getInt("letters");
            label = savedInstanceState.getString("label");
            tag = savedInstanceState.getString("tag");
            solvedStatus = savedInstanceState.getInt("solvedStatus");
            started = savedInstanceState.getBoolean("started");
            orderBy = savedInstanceState.getString("orderBy");
            blank = savedInstanceState.getBoolean("blank");
            filterSerial = savedInstanceState.getLong("filterSerial");

            refresh();
        }
    }

    public void getWordLength()
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView = inflater.inflate(R.layout.solve, null);

        EditText e1 = yourCustomView.findViewById(R.id.edittext17);
        TextView t3 = yourCustomView.findViewById(R.id.textview80);
        e1.setHint("Enter a value between 2 and " + maximumWordLength);

        Spinner s22 = yourCustomView.findViewById(R.id.spinner34);
        ArrayAdapter<String> blankAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankList);
        blankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s22.setAdapter(blankAdapter);

        final int[] sortIndex = new int[2];
        Spinner s10 = yourCustomView.findViewById(R.id.spinner22);
        s10.setVisibility(View.GONE);

        Spinner s11 = yourCustomView.findViewById(R.id.spinner23);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankColumns);
        ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s11.setAdapter(orderAdapter);

        Spinner s12 = yourCustomView.findViewById(R.id.spinner24);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s12.setAdapter(sortAdapter);

        s22.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                e1.setHint("Enter a value between 2 and " + ((i == 0) ? maximumWordLength : maximumBlankLength));

                if (i == 0) {
                    s11.setAdapter(orderAdapter);
                    s11.setSelection(7);
                }
                else {
                    s11.setAdapter(ordersAdapter);
                    s11.setSelection(10);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[1] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s11.setSelection(7);
        s12.setSelection(1);

        final int[] solved = {0};
        Spinner s4 = yourCustomView.findViewById(R.id.spinner5);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, solvedList);
        solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s4.setAdapter(solvedAdapter);

        s4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, lengthList);
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

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle("Change word length")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String alphabet = (lengthIndex[0] == 0 ? (e1.getText()).toString() : "1");
                    int precursor = (alphabet.isEmpty() ? 0 : Integer.parseInt(alphabet));
                    boolean wild = (s22.getSelectedItemPosition() > 0);

                    if (lengthIndex[0] == 0 && precursor < 2)
                    {
                        Toast.makeText(Report.this, "Enter a value between 2 and " + (wild ? maximumBlankLength : maximumWordLength) + " for word length", Toast.LENGTH_LONG).show();
                        getWordLength();
                    }
                    else
                    {
                        letters = precursor;
                        solvedStatus = solved[0];
                        orderBy = sortBy(sortIndex, wild);
                        label = "*";
                        blank = wild;
                        start(true);
                    }
                }).create();
        dialog.show();
    }

    public void filterByLabel()
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView = inflater.inflate(R.layout.filter, null);

        EditText e6 = yourCustomView.findViewById(R.id.edittext6);
        EditText e7 = yourCustomView.findViewById(R.id.edittext7);
        TextView t4 = yourCustomView.findViewById(R.id.textview12);
        e7.setHint("Enter a value between 2 and " + maximumWordLength);

        Spinner s23 = yourCustomView.findViewById(R.id.spinner35);
        ArrayAdapter<String> jokerAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankList);
        jokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s23.setAdapter(jokerAdapter);

        final int[] sortIndex = new int[2];
        Spinner s13 = yourCustomView.findViewById(R.id.spinner25);
        s13.setVisibility(View.GONE);

        Spinner s14 = yourCustomView.findViewById(R.id.spinner26);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankColumns);
        ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s14.setAdapter(orderAdapter);

        Spinner s15 = yourCustomView.findViewById(R.id.spinner27);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s15.setAdapter(sortAdapter);

        s23.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                e7.setHint("Enter a value between 2 and " + ((i == 0) ? maximumWordLength : maximumBlankLength));

                if (i == 0) {
                    s14.setAdapter(orderAdapter);
                    s14.setSelection(7);
                }
                else {
                    s14.setAdapter(ordersAdapter);
                    s14.setSelection(10);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s14.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s15.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[1] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s14.setSelection(7);
        s15.setSelection(1);

        final int[] solved = {0};
        Spinner s5 = yourCustomView.findViewById(R.id.spinner6);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, solvedList);
        solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s5.setAdapter(solvedAdapter);

        s5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                solved[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner s1 = yourCustomView.findViewById(R.id.spinner1);
        List<Pair<String, String>> labelList = new ArrayList<>(labelsList.subList(1, labelsList.size()));
        labelList.add(0, new Pair<>("(All tags)", null));

        ColourAdapter spinnerAdapter = new ColourAdapter(Report.this, R.layout.colour, R.id.textview62, labelList, Report.this, true, combo);
        s1.setAdapter(spinnerAdapter);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e6.setText("*");
                }
                else {
                    e6.setText((labelList.get(i)).first);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final int[] lengthIndex = new int[1];
        Spinner s7 = yourCustomView.findViewById(R.id.spinner8);
        ArrayList<String> lengthList = new ArrayList<>();
        lengthList.add(0, "Specific word length");
        lengthList.add(1, "All word lengths");

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, lengthList);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s7.setAdapter(lengthAdapter);

        s7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e7.setVisibility(View.VISIBLE);
                    t4.setVisibility(View.VISIBLE);
                    lengthIndex[0] = 0;
                }
                else {
                    e7.setVisibility(View.INVISIBLE);
                    t4.setVisibility(View.INVISIBLE);
                    lengthIndex[0] = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle("Filter by tag")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String intermediate = (e6.getText()).toString();
                    String alphabets = (lengthIndex[0] == 0 ? (e7.getText()).toString() : "1");
                    int temporary = (alphabets.isEmpty() ? 0 : Integer.parseInt(alphabets));
                    boolean wilds = (s23.getSelectedItemPosition() > 0);

                    if (lengthIndex[0] == 0 && temporary < 2)
                    {
                        Toast.makeText(Report.this, "Enter a value between 2 and " + (wilds ? maximumBlankLength : maximumWordLength) + " for word length", Toast.LENGTH_LONG).show();
                        filterByLabel();
                    }
                    else
                    {
                        letters = temporary;
                        label = intermediate;
                        solvedStatus = solved[0];
                        orderBy = sortBy(sortIndex, wilds);
                        blank = wilds;
                        start(true);
                    }
                }).create();
        dialog.show();
    }

    public void start(boolean checkExist)
    {
        closeCursor();

        if (checkExist)
        {
            long exist = db.existLabel(letters, label, orderBy, blank);
            filterSerial = (exist == 0 ? db.insertLabel(letters, label, orderBy, blank) : exist);
        }

        anagrams = (label.equals("*") ? db.getSolvedWords(letters, solvedStatus, orderBy, blank) : db.getLabelledWords(letters, label, solvedStatus, orderBy, blank));
        words = anagrams.getCount();
        counter = db.getPage(filterSerial, solvedStatus);

        int high = (words - 1) / rows;
        if (counter > high && words > 0) {
            counter = high;
            db.updatePage(filterSerial, counter, solvedStatus);
        }

        db.emptyTable(Report.this, blank);
        nextWord();
    }

    public void nextWord()
    {
        started = true;
        b1.setEnabled(true);
        b2.setEnabled(true);
        b7.setEnabled(true);
        b8.setEnabled(true);

        if (words > 0) {
            t1.setText("Page " + (counter + 1) + " out of " + (((words - 1) / rows) + 1) + " (" + words + (words == 1 ? " word)" : " words)"));
        }
        else {
            t1.setText("Page " + (counter + 1) + " out of 1 (0 words)");
        }

        int commence = counter * rows;
        int complete = Math.min((counter + 1) * rows, words);
        ArrayList<String> jumble = new ArrayList<>();

        if (orderBy.equals("DESC"))
        {
            if (anagrams.moveToPosition(words - 1 - commence)) {
                do {
                    jumble.add(anagrams.getString(0));
                } while (anagrams.moveToPrevious() && anagrams.getPosition() >= (words - complete));
            }
        }
        else {
            if (anagrams.moveToPosition(commence)) {
                do {
                    jumble.add(anagrams.getString(0));
                } while (anagrams.moveToNext() && anagrams.getPosition() < complete);
            }
        }

        String jumbleList = (((jumble.toString()).replace("[", "(\"")).replace("]", "\")")).replace(", ", "\", \"");
        jumbles = db.extract(jumbleList, commence + 1, orderBy, blank, !jumble.isEmpty());

        int open = counter * columns * rows;
        int close = Math.min((counter + 1) * columns * rows, words * columns);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, ((close - open) / columns) + 1, GridLayoutManager.HORIZONTAL, false);
        g1.setLayoutManager(layoutManager);

        reportAdapter = new ReportAdapter(Report.this, R.layout.word, jumbles, font);
        g1.setAdapter(reportAdapter);

        b1.setOnClickListener(view -> {
            if (words > rows) {
                counter--;
                if (counter < 0) {
                    counter = (words - 1) / rows;
                }
                db.updatePage(filterSerial, counter, solvedStatus);
                nextWord();
            }
        });

        b2.setOnClickListener(view -> {
            if (words > rows) {
                counter++;
                if (counter == ((words - 1) / rows) + 1) {
                    counter = 0;
                }
                db.updatePage(filterSerial, counter, solvedStatus);
                nextWord();
            }
        });

        b7.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(Report.this);
            final View yourCustomView = inflater.inflate(R.layout.input, null);

            EditText e1 = yourCustomView.findViewById(R.id.edittext1);
            int maximum = (((words - 1) / rows) + 1);
            e1.setHint("Enter a value between 1 and " + maximum);

            AlertDialog dialog = new AlertDialog.Builder(Report.this)
                    .setTitle("Go to page")
                    .setView(yourCustomView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String pages = (e1.getText()).toString();
                            int page = (pages.isEmpty() ? 0 : Integer.parseInt(pages));
                            if (page < 1 || page > maximum)
                            {
                                Toast.makeText(Report.this, "Enter a value between 1 and " + maximum, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                counter = page - 1;
                                db.updatePage(filterSerial, counter, solvedStatus);
                                nextWord();
                            }
                        }
                    }).create();
            dialog.show();
        });
    }

    public void refresh()
    {
        ArrayList<Integer> dimensions = db.getZoom("List");
        rows = dimensions.get(0);
        font = dimensions.get(2);
        combo = dimensions.get(3);
        loader = dimensions.get(4);
        maximumWordLength = db.getMaximumWordLength(false);
        maximumBlankLength = db.getMaximumWordLength(true);

        refreshSpinner();

        if (started) {
            if (letters == 0) {
                Cursor resultSet = db.getSqlQuery(label, Report.this, solvedStatus, orderBy, blank);

                if (resultSet != null) {
                    closeCursor();
                    anagrams = resultSet;
                    words = anagrams.getCount();

                    long exists = db.existLabel(letters, label, orderBy, blank);
                    filterSerial = (exists == 0 ? db.insertLabel(letters, label, orderBy, blank) : exists);
                    counter = (exists == 0 ? 0 : db.getPage(filterSerial, solvedStatus));

                    int peak = (words - 1) / rows;
                    if (counter > peak && words > 0) {
                        counter = peak;
                        db.updatePage(filterSerial, counter, solvedStatus);
                    }

                    nextWord();
                }
            }
            else {
                start(false);
            }
        }
    }

    public void refreshSpinner()
    {
        labelsList = db.getAllLabels();
        labelsList.add(0, new Pair<>("(No action)", null));
        colourList = db.getColours();

        ColourAdapter comboBoxAdapter = new ColourAdapter(Report.this, R.layout.colour, R.id.textview62, labelsList, Report.this, true, combo);
        s3.setAdapter(comboBoxAdapter);

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tag = (labelsList.get(i)).first;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void zoom()
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView = inflater.inflate(R.layout.magnify, null);

        EditText e8 = yourCustomView.findViewById(R.id.edittext15);
        EditText e9 = yourCustomView.findViewById(R.id.edittext16);
        EditText e13 = yourCustomView.findViewById(R.id.edittext31);
        EditText e15 = yourCustomView.findViewById(R.id.edittext39);

        e8.setHint("Enter a value greater than 0");
        e9.setHint("Enter a value greater than 11");
        e13.setHint("Enter a value greater than 11");
        e15.setHint("Enter a value greater than 11");

        e8.setText(Integer.toString(rows));
        e9.setText(Integer.toString(font));
        e13.setText(Integer.toString(combo));
        e15.setText(Integer.toString(loader));

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle("Change rows and font sizes")
                .setView(yourCustomView)
                .setPositiveButton("OK", (dialog1, whichButton) -> {
                    String old_rows = (e8.getText()).toString();
                    String old_font = (e9.getText()).toString();
                    String old_combo = (e13.getText()).toString();
                    String old_loader = (e15.getText()).toString();

                    int new_rows = (old_rows.isEmpty() ? 0 : Integer.parseInt(old_rows));
                    int new_font = (old_font.isEmpty() ? 0 : Integer.parseInt(old_font));
                    int new_combo = (old_combo.isEmpty() ? 0 : Integer.parseInt(old_combo));
                    int new_loader = (old_loader.isEmpty() ? 0 : Integer.parseInt(old_loader));

                    StringBuilder sb = new StringBuilder();
                    if (new_rows < 1) {
                        sb.append("Rows should be ≥ 1");
                    }
                    if (new_font < 11 || new_combo < 11 || new_loader < 11) {
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        sb.append("Font sizes should be ≥ 11");
                    }

                    if (sb.length() > 0)
                    {
                        Toast.makeText(Report.this, new String(sb), Toast.LENGTH_LONG).show();
                        zoom();
                    }
                    else
                    {
                        db.setMagnify("List", new_rows, new_font, new_combo, new_loader);
                        refresh();
                    }
                }).create();
        dialog.show();
    }

    public void onItemClick(int position)
    {
        if (!tag.equals("(No action)")) {
            int row = jumbles.size() / columns;
            int column = position % row;

            if (column > 0) {
                int colour = (column * columns) + 2;
                String location = jumbles.get(colour);
                char character = location.charAt(1);
                String word = ((character == 'f') ? location.substring(25, location.length() - 11) : location.substring(3, location.length() - 4));

                int situation = column * columns;
                String index = jumbles.get(situation);
                char ch = index.charAt(0);
                int serial = ((ch == '<') ? Integer.parseInt(index.substring(22, index.length() - 7)) : Integer.parseInt(index));
                ArrayList<String> wordsList;

                if (blank) {
                    int myBlank = (column * columns) + 5;
                    String myLocation = jumbles.get(myBlank);
                    char myAlphabet = myLocation.charAt(1);
                    String blankAnagram = ((myAlphabet == 'f') ? myLocation.substring(25, myLocation.length() - 11) : myLocation.substring(3, myLocation.length() - 4));
                    String identity = word + " " + blankAnagram;
                    db.updateTag(identity, tag, true);
                    wordsList = db.extract("(\"" + identity + "\")", serial, orderBy, true, false);
                } else {
                    db.updateTag(word, tag, false);
                    wordsList = db.extract("(\"" + word + "\")", serial, orderBy, false, false);
                }

                for (int cell = 0; cell < columns; cell++) {
                    jumbles.set((column * columns) + cell, wordsList.get(cell));
                    reportAdapter.notifyItemChanged((cell * row) + column);
                }
            }
        }
    }

    public void onItemLongClick(int position)
    {
        int row = jumbles.size() / columns;
        int column = position % row;

        if (column > 0) {
            int myColour = (column * columns) + 2;
            String location = jumbles.get(myColour);
            char character = location.charAt(1);
            String word = ((character == 'f') ? location.substring(25, location.length() - 11) : location.substring(3, location.length() - 4));
            ArrayList<String> hook;

            if (blank) {
                int myBlank = (column * columns) + 5;
                String myLocation = jumbles.get(myBlank);
                char myAlphabet = myLocation.charAt(1);
                String blankAnagram = ((myAlphabet == 'f') ? myLocation.substring(25, myLocation.length() - 11) : myLocation.substring(3, myLocation.length() - 4));
                hook = db.getDefinition(word + " " + blankAnagram, true);
            } else {
                hook = db.getDefinition(word, false);
            }

            String meaning = hook.get(0);
            String back = hook.get(1);
            String front = hook.get(2);
            String lexicons = hook.get(3);
            String serialNumber = hook.get(4);
            String listbox = hook.get(5);

            HashMap<String, String> colours = db.getColours();
            String amount;

            if (colours.containsKey(listbox) || colours.containsKey("")) {
                String colour = (colours.containsKey(listbox) ? colours.get(listbox) : colours.get(""));
                amount = "<font color=\"" + colour + "\"><b><small>" + front + "</small> " + word + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.isEmpty() ? "(No tag)" : listbox) + " " + lexicons + "</b> " + serialNumber + db.getFullDetails(word) + "</font>";
            } else {
                amount = "<b><small>" + front + "</small> " + word + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.isEmpty() ? "(No tag)" : listbox) + " " + lexicons + "</b> " + serialNumber + db.getFullDetails(word);
            }

            db.messageBox("Similar words for " + word, amount, Report.this);
        }
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

    public void promptDictionary(boolean deleteTable, boolean joker)
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView = inflater.inflate(R.layout.prompt, null);

        TextView t2 = yourCustomView.findViewById(R.id.textview14);
        t2.setText(joker ? "Preparing blank database will take 3 hours or more depending upon your device. It is recommended not to interrupt its execution in between and so you can choose to run this before you go to bed at night. If you do not want to run this now, you can click anywhere outside this dialogue box to close this dialogue box. If you want to run this now, you can choose your desired lexicon from below:\n\nCSW24 or NWL23?" :"CSW24 or NWL23?");

        CheckBox c1 = yourCustomView.findViewById(R.id.checkbox1);
        c1.setChecked(deleteTable);

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle("Choose your lexicon")
                .setView(yourCustomView)
                .setPositiveButton("CSW24", (dialog1, whichButton) -> {
                    if (joker) {
                        Toast.makeText(Report.this, "Loading all blank anagrams into memory. Just a minute...", Toast.LENGTH_LONG).show();
                    }

                    if (c1.isChecked()) {
                        db.dropTable(Report.this, false);
                    }
                    prepareDictionary(true, joker);
                })
                .setNegativeButton("NWL23", (dialog2, whichButton) -> {
                    if (joker) {
                        Toast.makeText(Report.this, "Loading all blank anagrams into memory. Just a minute...", Toast.LENGTH_LONG).show();
                    }

                    if (c1.isChecked()) {
                        db.dropTable(Report.this, false);
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
                            if (!used.contains(character)) {
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
        db.insertWord(this, false, dictionary, anagramsList, lexicon, joker);
    }

    public void getAllSubanagrams(boolean subanagram)
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView3 = inflater.inflate(R.layout.subanagram, null);

        EditText e10 = yourCustomView3.findViewById(R.id.edittext19);
        EditText e11 = yourCustomView3.findViewById(R.id.edittext20);
        EditText e12 = yourCustomView3.findViewById(R.id.edittext29);
        CheckBox c4 = yourCustomView3.findViewById(R.id.checkbox4);
        FrameLayout f3 = yourCustomView3.findViewById(R.id.framelayout3);

        LayoutInflater subinflater3 = LayoutInflater.from(Report.this);
        final View subCustomView3 = subinflater3.inflate(R.layout.sieve, null);
        f3.addView(subCustomView3);

        db.addFunctionalities(Report.this, e10, c4, false, subCustomView3);

        Spinner s25 = yourCustomView3.findViewById(R.id.spinner37);
        ArrayAdapter<String> subanagramAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, jokerList);
        subanagramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s25.setAdapter(subanagramAdapter);

        final int[] sortIndex = new int[2];
        Spinner s19 = yourCustomView3.findViewById(R.id.spinner31);
        s19.setVisibility(View.GONE);

        Spinner s20 = yourCustomView3.findViewById(R.id.spinner32);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, blankColumns);
        ordersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s20.setAdapter(orderAdapter);

        Spinner s21 = yourCustomView3.findViewById(R.id.spinner33);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s21.setAdapter(sortAdapter);

        s20.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s21.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortIndex[1] = i;
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
                    s20.setSelection(7);
                }
                else {
                    s20.setAdapter(ordersAdapter);
                    s20.setSelection(10);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s20.setSelection(7);
        s21.setSelection(1);

        final int[] solved = {0};
        Spinner s8 = yourCustomView3.findViewById(R.id.spinner10);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_item, solvedList);
        solvedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s8.setAdapter(solvedAdapter);

        s8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                solved[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle(subanagram ? "Search for subanagrams" : "Search for anagrams")
                .setView(yourCustomView3)
                .setPositiveButton("OK", (dialog1, whichButton) -> subanagrams((((e10.getText()).toString()).trim()).toUpperCase(), (e11.getText()).toString(), subanagram, ((e12.getText()).toString()).replace("\"", "'"), c4.isChecked(), sortIndex, solved, true, s25.getSelectedItemPosition())).create();
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
            Toast.makeText(Report.this, "Letters field can contain only letters and dots for blanks", Toast.LENGTH_LONG).show();
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

            if (!extra.isEmpty())
            {
                theQuery.append(" AND (").append(autoUnderscore ? db.addUnderscores(extra) : extra).append(")");
            }

            String customQuery = new String(theQuery);
            boolean wildsIndex = (blankIndex > 0);
            String subanagramIndex = sortBy(sortIndex, wildsIndex);
            execute(false, customQuery, subanagramIndex, solved[0], wildsIndex);
        }
    }

    public void execute(boolean autoUnderscores, String permanentQuery, String orderingIndex, int solvedIndex, boolean blankQuizzes)
    {
        String processingQuery = (autoUnderscores ? db.addUnderscores(permanentQuery) : permanentQuery);
        Cursor resultSet = db.getSqlQuery(processingQuery, Report.this, solvedIndex, orderingIndex, blankQuizzes);

        if (resultSet != null) {
            label = processingQuery;
            letters = 0;
            solvedStatus = solvedIndex;
            orderBy = orderingIndex;
            blank = blankQuizzes;

            closeCursor();
            anagrams = resultSet;
            words = anagrams.getCount();

            long exists = db.existLabel(letters, label, orderBy, blank);
            filterSerial = (exists == 0 ? db.insertLabel(letters, label, orderBy, blank) : exists);
            counter = (exists == 0 ? 0 : db.getPage(filterSerial, solvedStatus));

            int highest = (words - 1) / rows;
            if (counter > highest && words > 0) {
                counter = highest;
                db.updatePage(filterSerial, counter, solvedStatus);
            }

            db.emptyTable(Report.this, blank);
            nextWord();
        }
    }

    public String sortBy(int[] selection, boolean isBlank) {
        switch (selection[0]) {
            case 0: return (selection[1] == 1 ? "DESC" : "ASC");
            case 1: return " ORDER BY RANDOM()" + (selection[1] == 1 ? " DESC" : "");
            default: return " ORDER BY _" + (isBlank ? blankColumns.get(selection[0]) : allColumns.get(selection[0])) + "_" + (selection[1] == 1 ? " DESC" : "");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save your custom data to the Bundle
        outState.putInt("letters", letters);
        outState.putString("label", label);
        outState.putString("tag", tag);
        outState.putInt("solvedStatus", solvedStatus);
        outState.putBoolean("started", started);
        outState.putString("orderBy", orderBy);
        outState.putBoolean("blank", blank);
        outState.putLong("filterSerial", filterSerial);
    }
}