package rs.ac.metropolitan.cs330.znamenitosti.admin;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import rs.ac.metropolitan.cs330.znamenitosti.admin.config.AsyncActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import rs.ac.metropolitan.cs330.znamenitosti.admin.dto.City;
import rs.ac.metropolitan.cs330.znamenitosti.admin.dto.Sight;

/**
 *
 * @author nikola
 */
public class AddSight extends AsyncActivity {

    private City city;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText lat;
    private EditText lon;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.add_sight);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long cityId = extras.getLong("cityId");
            String cityName = extras.getString("cityName");
            city = new City(cityId, cityName);
        }
        lat = (EditText) findViewById(R.id.add_sight_latitude);
        lon = (EditText) findViewById(R.id.add_sight_longitude);
        TextView forCity = (TextView) findViewById(R.id.add_sight_city_name);
        forCity.setText(city.getName());
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat.setText(Double.toString(location.getLatitude()));
                lon.setText(Double.toString(location.getLongitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    public void getCoordinates(View view) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void addSight(View view) {
        EditText name = (EditText) findViewById(R.id.add_sight_name);
        EditText description = (EditText) findViewById(R.id.add_sight_description);
        String sightName = name.getText().toString();
        String sightDescription = description.getText().toString();
        double sightLatitude = 0d;
        double sightLongitude = 0d;
        try {
            sightLatitude = Double.parseDouble(lat.getText().toString());
            sightLongitude = Double.parseDouble(lon.getText().toString());
        } catch (Exception ex) {
        }
        if (!sightName.isEmpty() && sightLatitude != 0d && sightLongitude != 0d) {
            Sight sight = new Sight(sightName, sightDescription, sightLatitude, sightLongitude, city);
            Sight.SightForPosting.INSTANCE.sight = sight;

            startActivity(new Intent(this, AddSightImage.class));
        }
    }
}
