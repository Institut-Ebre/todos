package com.iesebre.dam2.sergi.todos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHARED_PREFERENCES_TODOS = "SP_TODOS";
    private static final String TODO_LIST = "todo_list";

    private Gson gson;

    public TodoArrayList tasks;
    private CustomListAdapter adapter;
    private String taskName;
    private String todoList="";
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Serialize our TaskArrayList to Json
        Type taskArrayListType = new TypeToken<TodoArrayList>(){}.getType();
        String serializedData = gson.toJson(tasks, taskArrayListType);

        System.out.println("Saving: " + serializedData);

        //Save tasks in SharedPreferences
        SharedPreferences preferencesReader = getSharedPreferences(SHARED_PREFERENCES_TODOS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesReader.edit();
        editor.putString(TODO_LIST, serializedData);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchJsonTodos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


//        SharedPreferences todos = getSharedPreferences(SHARED_PREFERENCES_TODOS, 0);
//        String todoList = todos.getString(TODO_LIST, null);
//
//        /* JSON EXAMPLE
//
//        [
//         {"name":"Comprar llet", "done": true, "priority": 2},
//         {"name":"Comprar pa", "done": true, "priority": 1},
//         {"name":"Fer exercici", "done": false, "priority": 3}
//         {"name":"Estudiar", "done": false, "priority": 3}
//        ]
//         */
//        if (todoList == null) {
//            String initial_json = "[\n" +
//                    "         {\"name\":\"Comprar llet\", \"done\": true, \"priority\": 2},\n" +
//                    "         {\"name\":\"Comprar pa\", \"done\": true, \"priority\": 1},\n" +
//                    "         {\"name\":\"Fer exercici\", \"done\": false, \"priority\": 3},\n" +
//                    "         {\"name\":\"Estudiar\", \"done\": false, \"priority\": 3}\n" +
//                    "        ]" ;
//            SharedPreferences.Editor editor = todos.edit();
//            editor.putString(TODO_LIST,initial_json);
//            editor.commit();
//            todoList = todos.getString(TODO_LIST, null);
//        }

        fetchJsonTodos();


//        Snackbar.make(,todoList , Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

//        Toast.makeText(this, todoList, Toast.LENGTH_LONG).show();

        /* JSON EXAMPLE

        [
         {"name":"Comprar llet", "done": true, "priority": 2},
         {"name":"Comprar pa", "done": true, "priority": 1},
         {"name":"Fer exercisi", "done": false, "priority": 3}
        ]
         */

        Toolbar toolbar = (Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fetchJsonTodos() {
        Ion.with(this)
                .load("http://acacha.github.io/json-server-todos/db_todos.json")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        todoList = result.toString();
                        Log.d("TAG_PROVA", "*********************************************************");
                        Log.d("TAG_PROVA AAAA ", todoList);
                        Log.d("TAG_PROVA", "*********************************************************");
                        updateTodoList();
                    }
                });
    }


    private void updateTodoList() {
        Type arrayTodoList = new TypeToken<TodoArrayList>() {}.getType();
        this.gson = new Gson();
        TodoArrayList temp = gson.fromJson(todoList,arrayTodoList);

        if (temp != null) {
            tasks = temp;
        } else {
            //Error TODO
        }

        ListView todoslv =
                (ListView) findViewById(R.id.todolistview);

        //We bind our arraylist of tasks to the adapter
        adapter = new CustomListAdapter(this, tasks);
        todoslv.setAdapter(adapter);

        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showEditTaskForm(View view) {

    }

    public void showAddTaskForm(View view) {
        taskName = "";
        EditText taskNameText;

        MaterialDialog dialog = new MaterialDialog.Builder(this).
                title("Afegir tasca").
                customView(R.layout.form_add_task, true).
                negativeText("Cancel·lar").
                positiveText("Afegir").
                negativeColor(Color.parseColor("#2196F3")).
                positiveColor(Color.parseColor("#2196F3")).
                onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        final TodoItem todoItem = new TodoItem();
//                        EditText et = (EditText) dialog.getCustomView().findViewById(R.id.task_title);
//                        todoItem.setName(et.toString());
                        todoItem.setName(taskName);
                        todoItem.setDone(true);
                        todoItem.setPriority(1);

                        tasks.add(todoItem);
                        adapter.notifyDataSetChanged();
                    }
                }).
                build();

                dialog.show();

        taskNameText = (EditText) dialog.getCustomView().findViewById(R.id.task_title);

        //If we name a task and it has a priority, enable positive button
        taskNameText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 taskName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
