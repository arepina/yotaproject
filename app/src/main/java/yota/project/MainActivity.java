package yota.project;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText urlEditText;//the url
    TextView codeTextView;//the html code of the site
    Button loadButton;//button that loads the html code of the site

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlEditText = (EditText) findViewById(R.id.URLeditText);
        codeTextView = (TextView) findViewById(R.id.codeTextView);
        loadButton = (Button) findViewById(R.id.loadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEditText.getText().toString();
                if(!checkURL(url)) {//check the url for being correct
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.urlError, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    TextView tw = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (tw != null)
                        tw.setGravity(Gravity.CENTER);
                    toast.show();
                }
                else {//everything is ok with URL
                    if (ConnectionDetector.isConnected(getApplicationContext())) {//check the internet connection
                        LoadPage loadPage = new LoadPage();
                        loadPage.execute(url);
                    }
                }
            }
        });
    }

    /**
     * Check the url for being correct
     * @param url URL
     * @return if the url is correct
     */
    private boolean checkURL(String url) {
        Pattern urlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?)://|www\\.)"
                        + "(([\\w\\-]+\\.)+?([\\w\\-.~]+/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = urlPattern.matcher(url);
        return m.find();
    }

    /**
     * Load the html code of the page
     */
    class LoadPage extends AsyncTask<String, Void, String> {

        /**
         * Start the process of loading the html code of the page
         * @param params the address of the page
         * @return html code of the page
         */
        @Override
        protected String doInBackground(String... params) {
            StringBuilder page = downloadPage(params[0]);
            if (isCancelled()) return null;
            return page.toString();
        }

        /**
         * Download the html code of the page
         * @param web the address of the page
         * @return html code of the page
         */
        public StringBuilder downloadPage(String web) {
            StringBuilder stringBuffer = new StringBuilder();
            try {
                URL url = new URL(web);
                InputStream is = url.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuffer;
        }

        /**
         * Put the text of the page on the screen
         * @param result page html
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            codeTextView.setText(result);
        }

        /**
         * There was an error
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            error();
        }

        /**
         * Show error
         */
        public void error() {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.loadError, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if (v != null)
                v.setGravity(Gravity.CENTER);
            toast.show();
        }
    }
}
