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
import android.os.Bundle;
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
    int columns = 17;
    String label = "*";
    String tag = "(No Action)";
    int solvedStatus = 0;
    boolean hidden;
    boolean detail;
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

    int rows;
    int font;

    // Declare the DrawerLayout, NavigationView and Toolbar
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        // Initialize the DrawerLayout, Toolbar and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout_report);
        toolbar = findViewById(R.id.toolbar_report);
        navigationView = findViewById(R.id.nav_view_report);

        pref = getApplicationContext().getSharedPreferences("AppData", 0);
        boolean prepared = pref.getBoolean("prepared", false);
        hidden = pref.getBoolean("hidden", false);
        detail = pref.getBoolean("detail", false);
        int version = pref.getInt("version", 1);
        Menu menu = navigationView.getMenu();

        if (hidden)
        {
            MenuItem menuItem = menu.findItem(R.id.button37);
            menuItem.setTitle("Show number of answers");
        }

        if (detail)
        {
            MenuItem menuItem = menu.findItem(R.id.button52);
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
                    case R.id.button27:
                        // Show a Toast message for the SQL query item
                        LayoutInflater inflater1 = LayoutInflater.from(Report.this);
                        final View yourCustomView1 = inflater1.inflate(R.layout.sqlquery, null);

                        TextView t2 = yourCustomView1.findViewById(R.id.textview14);
                        t2.setText(db.getSchema());

                        EditText e5 = yourCustomView1.findViewById(R.id.edittext8);
                        CheckBox c3 = yourCustomView1.findViewById(R.id.checkbox3);

                        Button b9 = yourCustomView1.findViewById(R.id.button73);
                        b9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Help help = new Help();
                                db.messageBox("Example SQL queries", help.getSqlHelp(), Report.this);
                            }
                        });

                        AlertDialog dialog1 = new AlertDialog.Builder(Report.this)
                                .setTitle("Enter your SQL query")
                                .setView(yourCustomView1)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String sqlQuery = (e5.getText()).toString();

                                        if (sqlQuery.length() > 0) {
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
                        final View yourCustomView2 = inflater2.inflate(R.layout.label, null);

                        EditText e3 = yourCustomView2.findViewById(R.id.edittext3);
                        EditText e4 = yourCustomView2.findViewById(R.id.edittext4);

                        Spinner s2 = yourCustomView2.findViewById(R.id.spinner2);
                        List<Pair<String, String>> tagList = new ArrayList<>(labelsList.subList(1, labelsList.size()));

                        ColourAdapter spinnerAdapter = new ColourAdapter(Report.this, R.layout.colour, R.id.textview62, tagList, Report.this, true);
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
                                .setView(yourCustomView2)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String line = (((e3.getText()).toString()).trim()).toUpperCase();
                                        String category = (e4.getText()).toString();
                                        db.updateWord(line, category);

                                        refresh();
                                    }
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
                        db.resetByLabel(Report.this, false);
                        break;
                    case R.id.button46:
                        // Show a Toast message for the Add new tag item
                        db.addByLabel(Report.this, false);
                        break;
                    case R.id.button47:
                        // Show a Toast message for the Rename tag by colour item
                        db.renameByLabel(Report.this, false, false);
                        break;
                    case R.id.button48:
                        // Show a Toast message for the Change tag colour by name item
                        db.renameByLabel(Report.this, false, true);
                        break;
                    case R.id.button49:
                        // Show a Toast message for the Delete single tag by name item
                        db.deleteByLabel(Report.this, false, true);
                        break;
                    case R.id.button50:
                        // Show a Toast message for the Delete single tag by colour item
                        db.deleteByLabel(Report.this, false, false);
                        break;
                    case R.id.button52:
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

        t1 = findViewById(R.id.textview6);
        b1 = findViewById(R.id.button6);
        b2 = findViewById(R.id.button7);
        b3 = findViewById(R.id.button8);
        b4 = findViewById(R.id.button9);
        b5 = findViewById(R.id.button13);
        b6 = findViewById(R.id.button14);
        b7 = findViewById(R.id.button16);
        b8 = findViewById(R.id.button18);
        g1 = findViewById(R.id.gridview2);
        s3 = findViewById(R.id.spinner4);

        db = new sqliteDB(Report.this, version, null, false);

        ArrayList<Integer> dimensions = db.getZoom("Report");
        rows = dimensions.get(0);
        font = dimensions.get(2);

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

        for (String oneColumn : db.getAllColumns("words")) {
            allColumns.add(oneColumn.substring(1, oneColumn.length() - 1));
        }

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWordLength();
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Report.this, MainActivity.class);
                startActivity(intent2);
                closeCursor();
                finish();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByLabel();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCursor();
                finish();
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Report.this);
                final View yourCustomView = inflater.inflate(R.layout.query, null);

                TextView t3 = yourCustomView.findViewById(R.id.textview27);
                t3.setText(db.getSchema());

                EditText e2 = yourCustomView.findViewById(R.id.edittext18);
                CheckBox c2 = yourCustomView.findViewById(R.id.checkbox2);

                Button b10 = yourCustomView.findViewById(R.id.button74);
                b10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Help help = new Help();
                        db.messageBox("Example custom queries", help.getCustomHelp(), Report.this);
                    }
                });

                final int[] sortIndex = new int[2];
                Spinner s16 = yourCustomView.findViewById(R.id.spinner28);
                s16.setVisibility(View.GONE);

                Spinner s17 = yourCustomView.findViewById(R.id.spinner29);
                ArrayAdapter<String> orderAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, allColumns);
                orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s17.setAdapter(orderAdapter);

                Spinner s18 = yourCustomView.findViewById(R.id.spinner30);
                ArrayAdapter<String> sortAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, sort);
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
                Spinner s6 = yourCustomView.findViewById(R.id.spinner7);
                ArrayAdapter<String> solvedAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, solvedList);
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

                AlertDialog dialog = new AlertDialog.Builder(Report.this)
                        .setTitle("SELECT front, word, back, definition, time, tag FROM words WHERE")
                        .setView(yourCustomView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String temporaryQuery = ((e2.getText()).toString()).replace("\"", "'");
                                String customQuery = (temporaryQuery.length() == 0 ? "1" : temporaryQuery);
                                String orderIndex = sortBy(sortIndex);
                                String processingQuery = (c2.isChecked() ? db.addUnderscores(customQuery) : customQuery);
                                Cursor resultSet = db.getSqlQuery(processingQuery, Report.this, solved[0], orderIndex);

                                if (resultSet != null) {
                                    label = processingQuery;
                                    letters = 0;
                                    solvedStatus = solved[0];
                                    orderBy = orderIndex;

                                    closeCursor();
                                    anagrams = resultSet;
                                    words = anagrams.getCount();

                                    boolean exists = db.existLabel(letters, label, orderBy);

                                    if (!exists) {
                                        counter = 0;
                                        db.insertLabel(letters, label, orderBy);
                                    } else {
                                        counter = db.getPage(letters, label, solvedStatus, orderBy);
                                    }

                                    int highest = (words - 1) / rows;
                                    if (counter > highest && words > 0) {
                                        counter = highest;
                                        db.updatePage(letters, label, counter, solvedStatus, orderBy);
                                    }

                                    nextWord();
                                }
                            }
                        }).create();
                dialog.show();
            }
        });

        if (!prepared) {
            promptDictionary(false, false);
        }
    }

    public void getWordLength()
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView = inflater.inflate(R.layout.solve, null);

        EditText e1 = yourCustomView.findViewById(R.id.edittext17);
        TextView t8 = yourCustomView.findViewById(R.id.textview80);
        e1.setHint("Enter a value between 2 and 58");

        final int[] sortIndex = new int[2];
        Spinner s10 = yourCustomView.findViewById(R.id.spinner22);
        s10.setVisibility(View.GONE);

        Spinner s11 = yourCustomView.findViewById(R.id.spinner23);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s11.setAdapter(orderAdapter);

        Spinner s12 = yourCustomView.findViewById(R.id.spinner24);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s12.setAdapter(sortAdapter);

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
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, solvedList);
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

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, lengthList);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s9.setAdapter(lengthAdapter);

        s9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    e1.setVisibility(View.VISIBLE);
                    t8.setVisibility(View.VISIBLE);
                    lengthIndex[0] = 0;
                }
                else {
                    e1.setVisibility(View.INVISIBLE);
                    t8.setVisibility(View.INVISIBLE);
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String alphabet = (lengthIndex[0] == 0 ? (e1.getText()).toString() : "-1");
                        int precursor = (alphabet.length() == 0 ? 0 : Integer.parseInt(alphabet));

                        if (lengthIndex[0] == 0 && precursor < 2)
                        {
                            Toast.makeText(Report.this, "Enter a value between 2 and 58 for word length", Toast.LENGTH_LONG).show();
                            getWordLength();
                        }
                        else
                        {
                            letters = precursor;
                            solvedStatus = solved[0];
                            orderBy = sortBy(sortIndex);
                            label = "*";
                            start(true);
                        }
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
        e7.setHint("Enter a value between 2 and 58");

        final int[] sortIndex = new int[2];
        Spinner s13 = yourCustomView.findViewById(R.id.spinner25);
        s13.setVisibility(View.GONE);

        Spinner s14 = yourCustomView.findViewById(R.id.spinner26);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s14.setAdapter(orderAdapter);

        Spinner s15 = yourCustomView.findViewById(R.id.spinner27);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, sort);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s15.setAdapter(sortAdapter);

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
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, solvedList);
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
        labelList.add(0, new Pair<>("(All Tags)", null));

        ColourAdapter spinnerAdapter = new ColourAdapter(Report.this, R.layout.colour, R.id.textview62, labelList, Report.this, true);
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

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, lengthList);
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String intermediate = (e6.getText()).toString();
                        String alphabets = (lengthIndex[0] == 0 ? (e7.getText()).toString() : "-1");
                        int temporary = (alphabets.length() == 0 ? 0 : Integer.parseInt(alphabets));

                        if (lengthIndex[0] == 0 && temporary < 2)
                        {
                            Toast.makeText(Report.this, "Enter a value between 2 and 58 for word length", Toast.LENGTH_LONG).show();
                            filterByLabel();
                        }
                        else
                        {
                            letters = temporary;
                            label = intermediate;
                            solvedStatus = solved[0];
                            orderBy = sortBy(sortIndex);
                            start(true);
                        }
                    }
                }).create();
        dialog.show();
    }

    public void start(boolean checkExist)
    {
        closeCursor();

        if (checkExist)
        {
            boolean exist = db.existLabel(letters, label, orderBy);

            if (!exist)
            {
                db.insertLabel(letters, label, orderBy);
            }
        }

        anagrams = (label.equals("*") ? db.getSolvedWords(letters, solvedStatus, orderBy) : db.getLabelledWords(letters, label, solvedStatus, orderBy));
        words = anagrams.getCount();
        counter = db.getPage(letters, label, solvedStatus, orderBy);

        int high = (words - 1) / rows;
        if (counter > high && words > 0) {
            counter = high;
            db.updatePage(letters, label, counter, solvedStatus, orderBy);
        }

        nextWord();
    }

    public void nextWord()
    {
        started = true;

        b1.setEnabled(true);
        b2.setEnabled(true);
        b7.setEnabled(true);

        if (words > 0) {
            t1.setText("Page " + (counter + 1) + " out of " + (((words - 1) / rows) + 1));
        }
        else {
            t1.setText("Page " + (counter + 1) + " out of 1");
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
        jumbles = db.extract(jumbleList, commence + 1, orderBy);

        int open = counter * columns * rows;
        int close = Math.min((counter + 1) * columns * rows, words * columns);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, Math.max((close - open) / columns, 1), GridLayoutManager.HORIZONTAL, false);
        g1.setLayoutManager(layoutManager);

        reportAdapter = new ReportAdapter(Report.this, R.layout.word, jumbles, font);
        g1.setAdapter(reportAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (words > rows) {
                    counter--;
                    if (counter < 0) {
                        counter = (words - 1) / rows;
                    }
                    db.updatePage(letters, label, counter, solvedStatus, orderBy);
                    nextWord();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (words > rows) {
                    counter++;
                    if (counter == ((words - 1) / rows) + 1) {
                        counter = 0;
                    }
                    db.updatePage(letters, label, counter, solvedStatus, orderBy);
                    nextWord();
                }
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                int page = (pages.length() == 0 ? 0 : Integer.parseInt(pages));
                                if (page < 1 || page > maximum)
                                {
                                    Toast.makeText(Report.this, "Enter a value between 1 and " + maximum, Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    counter = page - 1;
                                    db.updatePage(letters, label, counter, solvedStatus, orderBy);
                                    nextWord();
                                }
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    public void refresh()
    {
        ArrayList<Integer> dimensions = db.getZoom("Report");
        rows = dimensions.get(0);
        font = dimensions.get(2);

        refreshSpinner();

        if (started) {
            if (letters == 0) {
                Cursor resultSet = db.getSqlQuery(label, Report.this, solvedStatus, orderBy);

                if (resultSet != null) {
                    closeCursor();
                    anagrams = resultSet;
                    words = anagrams.getCount();

                    boolean exists = db.existLabel(letters, label, orderBy);

                    if (!exists) {
                        counter = 0;
                        db.insertLabel(letters, label, orderBy);
                    } else {
                        counter = db.getPage(letters, label, solvedStatus, orderBy);
                    }

                    int peak = (words - 1) / rows;
                    if (counter > peak && words > 0) {
                        counter = peak;
                        db.updatePage(letters, label, counter, solvedStatus, orderBy);
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
        labelsList.add(0, new Pair<>("(No Action)", null));
        colourList = db.getColours();

        ColourAdapter comboBoxAdapter = new ColourAdapter(Report.this, R.layout.colour, R.id.textview62, labelsList, Report.this, true);
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

        e8.setHint("Enter a value greater than 0");
        e9.setHint("Enter a value greater than 11");

        e8.setText(Integer.toString(rows));
        e9.setText(Integer.toString(font));

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle("Change rows and font size")
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String old_rows = (e8.getText()).toString();
                        String old_font = (e9.getText()).toString();

                        int new_rows = (old_rows.length() == 0 ? 0 : Integer.parseInt(old_rows));
                        int new_font = (old_font.length() == 0 ? 0 : Integer.parseInt(old_font));

                        if (new_rows < 1 || new_font < 11)
                        {
                            Toast.makeText(Report.this, "Font size should be greater than 11\nRows should be greater than 0", Toast.LENGTH_LONG).show();
                            zoom();
                        }
                        else
                        {
                            long magnify = db.setMagnify("Report", new_rows, new_font);
                            refresh();
                        }
                    }
                }).create();
        dialog.show();
    }

    public void onItemClick(int position)
    {
        if (!tag.equals("(No Action)")) {
            int row = jumbles.size() / columns;
            int column = position % row;
            int colour = (column * columns) + 2;
            String location = jumbles.get(colour);
            char character = location.charAt(1);
            String word = ((character == 'f') ? location.substring(25, location.length() - 11) : location.substring(3, location.length() - 4));
            int situation = column * columns;
            String index = jumbles.get(situation);
            char ch = index.charAt(0);
            int serial = ((ch == '<') ? Integer.parseInt(index.substring(22, index.length() - 7)) : Integer.parseInt(index));
            db.updateTag(word, tag);
            ArrayList<String> wordsList = db.extract("(\"" + word + "\")", serial, orderBy);
            for (int cell = 0; cell < columns; cell++)
            {
                jumbles.set((column * columns) + cell, wordsList.get(cell));
                reportAdapter.notifyItemChanged((cell * row) + column);
            }
        }
    }

    public void onItemLongClick(int position)
    {
        int row = jumbles.size() / columns;
        int column = position % row;
        int myColour = (column * columns) + 2;
        String location = jumbles.get(myColour);
        char character = location.charAt(1);
        String word = ((character == 'f') ? location.substring(25, location.length() - 11) : location.substring(3, location.length() - 4));

        ArrayList<String> hook = db.getDefinition(word);
        String meaning = hook.get(0);
        String back = hook.get(1);
        String front = hook.get(2);
        String lexicons = hook.get(3);
        String listbox = hook.get(4);

        HashMap<String, String> colours = db.getColours();
        String amount;

        if (colours.containsKey(listbox) || colours.containsKey("")) {
            String colour = (colours.containsKey(listbox) ? colours.get(listbox) : colours.get(""));
            amount = "<font color=\"" + colour + "\"><b><small>" + front + "</small> " + word + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.length() == 0 ? "(No Tag)" : listbox) + " " + lexicons + "</b>" + db.getFullDetails(word) + "</font>";
        } else {
            amount = "<b><small>" + front + "</small> " + word + " <small>" + back + "</small></b> " + meaning + " <b>" + (listbox.length() == 0 ? "(No Tag)" : listbox) + " " + lexicons + "</b>" + db.getFullDetails(word);
        }

        db.messageBox("Full details for " + word, amount, Report.this);
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

        TextView t6 = yourCustomView.findViewById(R.id.textview85);
        t6.setText("CSW24 or NWL23?");

        CheckBox c1 = yourCustomView.findViewById(R.id.checkbox1);
        c1.setChecked(deleteTable);

        AlertDialog dialog = new AlertDialog.Builder(Report.this)
                .setTitle("Choose your lexicon")
                .setView(yourCustomView)
                .setPositiveButton("CSW24", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (joker) {
                            Toast.makeText(Report.this, "Loading all blank anagrams into memory. Just a minute...", Toast.LENGTH_LONG).show();
                        }

                        if (c1.isChecked()) {
                            db.dropTable(Report.this, false);
                        }
                        prepareDictionary(true, joker);
                    }
                })
                .setNegativeButton("NWL23", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (joker) {
                            Toast.makeText(Report.this, "Loading all blank anagrams into memory. Just a minute...", Toast.LENGTH_LONG).show();
                        }

                        if (c1.isChecked()) {
                            db.dropTable(Report.this, false);
                        }
                        prepareDictionary(false, joker);
                    }
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
        db.insertWord(this, false, dictionary, anagramsList, lexicon, joker);
    }

    public void getAllSubanagrams(boolean subanagram)
    {
        LayoutInflater inflater = LayoutInflater.from(Report.this);
        final View yourCustomView = inflater.inflate(R.layout.subanagram, null);

        EditText e10 = yourCustomView.findViewById(R.id.edittext19);
        EditText e11 = yourCustomView.findViewById(R.id.edittext20);
        EditText e12 = yourCustomView.findViewById(R.id.edittext29);
        CheckBox c4 = yourCustomView.findViewById(R.id.checkbox4);

        TextView t7 = yourCustomView.findViewById(R.id.textview32);
        t7.setText(db.getSchema());

        Button b11 = yourCustomView.findViewById(R.id.button81);
        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help help = new Help();
                db.messageBox("Example custom queries", help.getCustomHelp(), Report.this);
            }
        });

        final int[] sortIndex = new int[2];
        Spinner s19 = yourCustomView.findViewById(R.id.spinner31);
        s19.setVisibility(View.GONE);

        Spinner s20 = yourCustomView.findViewById(R.id.spinner32);
        ArrayAdapter<String> orderAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, allColumns);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s20.setAdapter(orderAdapter);

        Spinner s21 = yourCustomView.findViewById(R.id.spinner33);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, sort);
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

        s20.setSelection(7);
        s21.setSelection(1);

        final int[] solved = {0};
        Spinner s8 = yourCustomView.findViewById(R.id.spinner10);
        ArrayAdapter<String> solvedAdapter = new ArrayAdapter(Report.this, android.R.layout.simple_spinner_item, solvedList);
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
                .setView(yourCustomView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String letter = (((e10.getText()).toString()).trim()).toUpperCase();
                        boolean flag = false;
                        for (int digits = 0; digits < letter.length(); digits++) {
                            int flags = (int) letter.charAt(digits);
                            if (flags < 65 || flags > 90) {
                                flag = true;
                                break;
                            }
                        }

                        String digit = (e11.getText()).toString();
                        int blanks = (digit.length() == 0 ? 0 : Integer.parseInt(digit));

                        if (flag) {
                            Toast.makeText(Report.this, "Letters field can contain only letters", Toast.LENGTH_LONG).show();
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
                                theQuery.append("_length_ = ").append(letter.length() + blanks).append(" AND _alphagram_ LIKE '").append(empty).append("'");
                            }

                            String extra = ((e12.getText()).toString()).replace("\"", "'");
                            if (extra.length() > 0)
                            {
                                theQuery.append(" AND (").append(c4.isChecked() ? db.addUnderscores(extra) : extra).append(")");
                            }

                            String customQuery = new String(theQuery);
                            String subanagramIndex = sortBy(sortIndex);
                            Cursor resultSet = db.getSqlQuery(customQuery, Report.this, solved[0], subanagramIndex);

                            if (resultSet != null) {
                                label = customQuery;
                                letters = 0;
                                solvedStatus = solved[0];
                                orderBy = subanagramIndex;

                                closeCursor();
                                anagrams = resultSet;
                                words = anagrams.getCount();

                                boolean exists = db.existLabel(letters, label, orderBy);

                                if (!exists) {
                                    counter = 0;
                                    db.insertLabel(letters, label, orderBy);
                                } else {
                                    counter = db.getPage(letters, label, solvedStatus, orderBy);
                                }

                                int apex = (words - 1) / rows;
                                if (counter > apex && words > 0) {
                                    counter = apex;
                                    db.updatePage(letters, label, counter, solvedStatus, orderBy);
                                }

                                nextWord();
                            }
                        }
                    }
                }).create();
        dialog.show();
    }

    public String sortBy(int[] selection) {
        switch (selection[0]) {
            case 0: return (selection[1] == 1 ? "DESC" : "ASC");
            case 1: return " ORDER BY RANDOM()" + (selection[1] == 1 ? " DESC" : "");
            default: return " ORDER BY _" + allColumns.get(selection[0]) + "_" + (selection[1] == 1 ? " DESC" : "");
        }
    }
}