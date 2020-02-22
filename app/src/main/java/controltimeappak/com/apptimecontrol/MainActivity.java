package controltimeappak.com.apptimecontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private RequestQueue requestQueue;



    EditText passwd;
    Switch what;
    static boolean ac=true;



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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void jsonParse_post(int id) {

        what = (Switch)findViewById(R.id.what);
        what.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ac = false;
                } else {
                    ac = true;
                }}});



        String url = "http://controltimeappak.pythonanywhere.com/api/post/create";
        JSONObject js = new JSONObject();
        try {
            js.put("who",id);
            js.put("what",ac);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (ac==true) {
                            mTextViewResult.append("Zalogowano ! ;)");// + response.toString() +"\n\n");
                        }else {
                            mTextViewResult.append("Wylogowano ! ;)");// + response.toString() +"\n\n");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////



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
                                int id = employee.getInt("id");

                                mTextViewResult.append("Hello, " + firstName + " " + lastName + " nice to see you !" + "\n\n");
                                jsonParse_post(id);
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

