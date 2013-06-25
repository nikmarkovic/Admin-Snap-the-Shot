package rs.ac.metropolitan.cs330.znamenitosti.admin;

import rs.ac.metropolitan.cs330.znamenitosti.admin.config.AsyncListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import rs.ac.metropolitan.cs330.znamenitosti.admin.dto.City;

/**
 *
 * @author nikola
 */
public class ChooseCity extends AsyncListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchingCities().execute(getServerAddress() + ":" + getServerPort() + "/cities/all");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        City city = (City) getListAdapter().getItem(position);
        Intent intent = new Intent(this, AddSight.class);
        intent.putExtra("cityId", city.getId());
        intent.putExtra("cityName", city.getName());
        startActivity(intent);
    }

    private class FetchingCities extends AsyncTask<String, Void, List<City>> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.fetching));
        }

        @Override
        protected List<City> doInBackground(String... urls) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            List<City> result = new ArrayList<City>();
            for (String url : urls) {
                try {
                    City[] cities = restTemplate.getForObject(url, City[].class);
                    result.addAll(Arrays.asList(cities));
                } catch (Exception ex) {
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<City> result) {
            dismissProgressDialog();
            setListAdapter(new ArrayAdapter<City>(ChooseCity.this, android.R.layout.simple_list_item_1, result));
        }
    }
}
