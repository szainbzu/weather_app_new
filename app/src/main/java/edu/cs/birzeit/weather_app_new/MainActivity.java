package edu.cs.birzeit.weather_app_new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String code = "5c0df1570193de98d2da5b872e6dbec3";
    private EditText edtCity;
    private TextView txtResult;
    private DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCity = findViewById(R.id.edtCity);
        txtResult = findViewById(R.id.txtResult);
    }

    public void btnShow_Click(View view) {
        String city = "";
        city = edtCity.getText().toString();
        if(city.equals("")){
            txtResult.setText("Enter City name");
        }
        else{
            String str = url+"?q=" + city + "&appid=" + code;
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, str,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //txtResult.setText(response);
                            String result = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject mainObj = jsonObject.getJSONObject("main");
                                double temp = mainObj.getDouble("temp") - 273.15;
                                double feelsTemp = mainObj.getDouble("feels_like") - 273.15;
                                int humidity = mainObj.getInt("humidity");
                                result = "Temperature = " + df.format(temp) + " C";
                                result+="\nFeels Like: " + df.format(feelsTemp) + " C";
                                result+= "\nHumidity: " + humidity + "%";

                                txtResult.setText(result);
                                //close keyboard
                                InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                input.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            } catch (JSONException exception) {
                                exception.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });



            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}