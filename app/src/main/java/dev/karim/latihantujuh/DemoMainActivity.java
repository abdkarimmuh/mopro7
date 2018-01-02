package dev.karim.latihantujuh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by Karim on 11/14/2017.
 */

public class DemoMainActivity extends AppCompatActivity {
    CursorAdapter aa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo);
        ListView lv = (ListView) findViewById(R.id.listView);
        // Using database via helper class
        BookHelper helper = new BookHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        // Query data
        String[] projection = {"_id", "title", "author"};
        Cursor c = db.query("book_entries", projection, null, null, null, null, null);
        aa = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, new String[]{"title", "author"},
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ArrayList<String> data = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()){
            String title = c.getString(c.getColumnIndex("title"));
            data.add(title);
            c.moveToNext();
        }

        if (data.isEmpty()){
            data.add("No book entries, please add");
        }
        // Use listView to display data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(aa);
        registerForContextMenu(lv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.contex_delete){
            delete_book(info.id);
        } else if (item.getItemId() == R.id.contex_update){
            update_book(info.id);
        }
        return true;
    }

    private void delete_book (long id){
        BookHelper helper = new BookHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete("book_entries", "_id=?", new String[]{String.valueOf(id)});
        Cursor x = db.query("book_entries", new String[]{"_id", "title", "author"}, null, null, null, null, null);
        aa.changeCursor(x);
        aa.notifyDataSetChanged();
    }

    private void update_book (long id){
        BookHelper helper = new BookHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor e = db.query("book_entries",
                            new String[]{"title", "author"},
                            "_id=?",
                            new String[]{String.valueOf(id)},
                            null, null, null);
        e.moveToFirst();
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("_id",id);
        intent.putExtra("title",e.getString(e.getColumnIndex("title")));
        intent.putExtra("author",e.getString(e.getColumnIndex("author")));
        startActivity(intent);
    }
}
