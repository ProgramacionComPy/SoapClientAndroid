package com.example.soapcliente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText nro1,nro2;
	private Button sumar;
	private String resultado="";
	private TareaSumar tareaSumar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nro1=(EditText) findViewById(R.id.nro1);
		nro2=(EditText) findViewById(R.id.nro2);
		sumar=(Button) findViewById(R.id.suma);
		
		sumar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tareaSumar = new TareaSumar();
				tareaSumar.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class TareaSumar extends AsyncTask<Void, Void, Boolean> {
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.
		//WebService - Opciones
	    final String NAMESPACE = "http://ws/";
	    final String URL="http://192.168.0.4:8084/ServicioWebSoap/WsProgComPy?wsdl";
	    final String METHOD_NAME = "suma";
	    final String SOAP_ACTION = "http://ws/suma";

	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    
	    request.addProperty("nro1", nro1.getText().toString());
	    request.addProperty("nro2", nro2.getText().toString());
	 
	    SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope.setOutputSoapObject(request);
		
	    HttpTransportSE ht = new HttpTransportSE(URL);
        try {
        	ht.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	        resultado=response.toString();
	        Log.i("Resultado: ",resultado);
	    }
	    catch (Exception e)
	    {
	    	Log.i("Error: ",e.getMessage());
	    	e.printStackTrace();
	        return false;
	    }	 

		return true;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if(success==false){
			Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();	
		}
		else{
			Toast.makeText(getApplicationContext(), 	"El resultado es: "+resultado, Toast.LENGTH_LONG).show();	
		}
	}

	@Override
	protected void onCancelled() {
		Toast.makeText(getApplicationContext(), 	"Error", Toast.LENGTH_LONG).show();
	}
}

}
