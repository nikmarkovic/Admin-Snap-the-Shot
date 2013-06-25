package rs.ac.metropolitan.cs330.znamenitosti.admin;

import android.content.Intent;
import rs.ac.metropolitan.cs330.znamenitosti.admin.config.AsyncActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import rs.ac.metropolitan.cs330.znamenitosti.admin.dto.City;

/**
 *
 * @author nikola
 */
public class AddCity extends AsyncActivity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.add_city);
    }

    public void addCity(View view) {
        TextView cityNameView = (TextView) findViewById(R.id.city_name);
        String cityName = cityNameView.getText().toString();
        if (!cityName.isEmpty()) {
            new AddCityTask().execute(getServerAddress() + ":" + getServerPort() + "/cities", cityName);
        }
    }

    private class AddCityTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.adding));
        }

        @Override
        protected String doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            try {
                return restTemplate.postForObject(params[0], new City(params[1]), String.class);
            } catch (Exception ex) {
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            Toast.makeText(AddCity.this, result, Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(AddCity.this, Main.class));
        }
    }
}
