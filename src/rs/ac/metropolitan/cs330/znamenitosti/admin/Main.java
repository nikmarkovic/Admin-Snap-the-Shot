package rs.ac.metropolitan.cs330.znamenitosti.admin;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends ListActivity {

    private static final String[] options = {"Dodaj grad", "Dodaj znamenitost"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, AddCity.class));
                break;
            case 1:
                startActivity(new Intent(this, ChooseCity.class));
                break;
        }
    }
}
