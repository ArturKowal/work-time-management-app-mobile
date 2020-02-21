package controltimeappak.com.apptimecontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
////////////////////////////////////////////////////////////////////////////////////////////////////////////



public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;


    EditText passwd;
    Switch what;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParse = findViewById(R.id.myButton);

        mQueue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse_get();
            }
        });
    }





    private void jsonParse_get() {
        passwd = (EditText) findViewById(R.id.passwd);
        final String pass = passwd.getText().toString();
        String url = "http://controltimeappak.pythonanywhere.com/api/" + pass;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("person");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                //int initt = employee.getInt("ident");
                                String firstName = employee.getString("first_name");
                                String lastName = employee.getString("last_name");
                                //int age = employee.getInt("id");

                                mTextViewResult.append("Hello, " + firstName + " " + lastName + " nice to see you !" + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextViewResult.append("Błędny login ! Spróbuj jeszcze raz." + "\n\n");
                error.printStackTrace();
            }
        });
        mQueue.add(request);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mTextViewResult.setText("");
            }
        }, 3000);

    }
}

