package yota.project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Check the internet connection class
 */
class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    /**
     * Try to connect to the internet
     */
    public boolean ConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null) {
                return false;
            } else {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    /**
     * Check if there is a connection
     */
    public static boolean isConnected(Context context) {
        ConnectionDetector cd = new ConnectionDetector(context.getApplicationContext());
        Boolean isInternetPresent = cd.ConnectingToInternet();
        if (isInternetPresent) {
            return true;
        } else {
            showToast(context);
            return false;
        }
    }

    /**
     * Show the toast that there is no connection
     */
    public static void showToast(Context context) {
        Toast toast = Toast.makeText(context.getApplicationContext(), R.string.internetError, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null)
            v.setGravity(Gravity.CENTER);
        toast.show();
    }
}
