package controltimeappak.com.apptimecontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    EditText passwd;
    EditText id_in;
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

    private void jsonParse_post(int id, final boolean action) {

        String url = "http://controltimeappak.pythonanywhere.com/api/post/create";
        JSONObject js = new JSONObject();
        try {
            js.put("who",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (action==false){
                            mTextViewResult.append("Zalogowano! ;)");
                        }
                        else {
                            mTextViewResult.append("Wylogowano! ;)");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjReq);

    }

    private void jsonParse_get() {
        id_in = (EditText) findViewById(R.id.id_in);
        passwd = (EditText) findViewById(R.id.passwd);

        final String pass = id_in.getText().toString();
        id_in.setText("");
        String url = "http://controltimeappak.pythonanywhere.com/api/" + pass;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("person");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                int ident_2 = employee.getInt("ident");
                                String firstName = employee.getString("first_name");
                                String lastName = employee.getString("last_name");
                                int id = employee.getInt("id");
                                Boolean action = employee.getBoolean("what");
                                mTextViewResult.append("Hello, " + firstName + " " + lastName + " nice to see you !" +"\n\n");
                                String text = passwd.getText().toString();
                                int ident = Integer.parseInt(text);

                                if (ident==ident_2) {
                                    jsonParse_post(id,action);
                                }
                                else {
                                    mTextViewResult.append("Wrong PIN, try again." + "\n\n");
                                }
                                passwd.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextViewResult.append("Wrong id ! Try again." + "\n\n");
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