package com.embeddedlapps.primeraversion;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnvioMSJ extends Activity {

	private static String url_create_Empleado = "http://www.dtech20.com/appD/gcm.php";

	//private static String url_create_Empleado = "http://dtech20.com/pb/mens.php";

	private static final String TAG_SUCCESS = "success";

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputMsj;

	Button btnSave;

   private TextView textEmail;
	private TextView textNombre;

	private static final String TAG_NOMBRE= "nombre";
	private static final String TAG_EMAIL= "email";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviomsj);

		inputMsj = (EditText) findViewById(R.id.inputName);
		textNombre = (TextView) findViewById(R.id.textNombre);
		textEmail = (TextView) findViewById(R.id.textEmail);

// recuperando datos
		String nombre =getIntent().getStringExtra(TAG_NOMBRE);
		textNombre.setText(nombre);
		String email =getIntent().getStringExtra(TAG_EMAIL);
		textEmail.setText(email);

		//envio de datos

		Button btnCreateEmpleado = (Button) findViewById(R.id.btnCreateEmpleado);

		btnCreateEmpleado.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new Empleado in background thread
				new CreateNewEmpleado().execute();
			}
		});


		//Intent i = getIntent();

		// getting Empleado id (pid) from intent
		//cedula = i.getStringExtra(TAG_CEDULA);





	}



	class CreateNewEmpleado extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EnvioMSJ.this);
			pDialog.setMessage("Enviando Mensaje..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Empleado
		 * */
		protected String doInBackground(String... args) {
			//String name = textNombre.getText().toString();
			String emai = textEmail.getText().toString();
			String msj = inputMsj.getText().toString();


			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", emai));
			params.add(new BasicNameValuePair("msj", msj));


			// getting JSON Object
			// Note that create Empleado url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_Empleado,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created Empleado
					Intent i = new Intent(getApplicationContext(), Usuarios.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create Empleado
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}





	}

