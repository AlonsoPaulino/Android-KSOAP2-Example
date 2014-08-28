package com.example.luisalonso.webservices;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class WebServiceActivity extends ActionBarActivity {

    private final String NAMESPACE = "http://www.w3schools.com/webservices/";
    private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
    private final String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
    private final String METHOD_NAME = "CelsiusToFahrenheit";
    private String TAG = "PGGURU";
    private static String celcius;
    private static String fahren;
    Button b;
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        et = (EditText)findViewById(R.id.editText1);
        tv = (TextView)findViewById(R.id.tv_result);
        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (et.getText().length() != 0 && et.getText().toString() != "") {
                    celcius = et.getText().toString();
                    AsyncCallWs task = new AsyncCallWs();
                    task.execute();
                } else {
                    tv.setText("Please enter Celcius");
                }
            }
        });
    }

    private class AsyncCallWs extends AsyncTask<String, Void , Void >{
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getFahrenheit(celcius);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            tv.setText(fahren + "° F");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            tv.setText("Calculating...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }

    public void getFahrenheit(String celsius){
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Celsius");
        celsiusPI.setValue(celsius);
        celsiusPI.setType(double.class);
        request.addProperty(celsiusPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try{
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            fahren = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}