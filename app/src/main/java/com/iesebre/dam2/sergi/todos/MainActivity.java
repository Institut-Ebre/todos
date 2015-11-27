package com.iesebre.dam2.sergi.todos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHARED_PREFERENCES_TODOS = "SP_TODOS";
    private static final String TODO_LIST = "todo_list";

    private Gson gson;

    public TodoArrayList tasks;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences todos = getSharedPreferences(SHARED_PREFERENCES_TODOS, 0);
        String todoList = todos.getString(TODO_LIST, null);

        /* JSON EXAMPLE

        [
         {"name":"Comprar llet", "done": true, "priority": 2},
         {"name":"Comprar pa", "done": true, "priority": 1},
         {"name":"Fer exercisi", "done": false, "priority": 3}
        ]
         */
        if (todoList == null) {
            String initial_json = "[{\"name\":\"Comprar llet\", \"done\": true, \"priority\": 2},\n" +
                    "         {\"name\":\"Comprar pa\", \"done\": true, \"priority\": 1},\n" +
                    "         {\"name\":\"Fer exercici\", \"done\": false, \"priority\": 3}]" ;
            SharedPreferences.Editor editor = todos.edit();
            editor.putString(TODO_LIST,initial_json);
            editor.commit();
            todoList = todos.getString(TODO_LIST, null);
        }


        Log.d("TAG_PROVA","*********************************************************");
        Log.d("TAG_PROVA",todoList);
        Log.d("TAG_PROVA","*********************************************************");

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


        Toolbar toolbar = (Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
}
