package rs.ac.metropolitan.cs330.znamenitosti.admin.config;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import rs.ac.metropolitan.cs330.znamenitosti.admin.R;

/**
 *
 * @author nikola
 */
public class AsyncListActivity extends ListActivity {

    private ProgressDialog progressDialog;

    public void showProgressDialog(CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setIndeterminate(true);
        }
        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public String getServerAddress() {
        return getString(R.string.server_address);
    }

    public String getServerPort() {
        return getString(R.string.server_port);
    }
}
