package com.embeddedlapps.primeraversion;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Usuarios extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> empleadosList;

	// url to get all empleados list Reemplaza la IP de tu equipo o la direccion de tu servidor 
	// Si tu servidor es tu PC colocar IP Ej: "http://127.97.99.200/taller06oct/..", no colocar "http://localhost/taller06oct/.."
	private static String url_all_empleados = "http://www.dtech20.com/appD/usuarios.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_empleados = "empleados";
	private static final String TAG_CEDULA = "cedula";
	private static final String TAG_NOMBRE = "nombre";
	private static final String TAG_EMAIL= "email";
	

	// empleados JSONArray
	JSONArray empleados = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_usuarios);

		// Hashmap for ListView
		empleadosList = new ArrayList<HashMap<String, String>>();

		// Loading empleados in Background Thread
		new LoadAllempleados().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single Empleado
		// launching Edit Empleado Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String nombre = ((TextView) view.findViewById(R.id.nombre)).getText()
						.toString();
				String email = ((TextView) view.findViewById(R.id.email)).getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						EnvioMSJ.class);
				// sending pid to next activity
				in.putExtra(TAG_NOMBRE, nombre);
				in.putExtra(TAG_EMAIL, email);
				
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

	}

	// Response from Edit Empleado Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received 
			// means user edited/deleted Empleado
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all Empleado by making HTTP Request
	 * */
	class LoadAllempleados extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Usuarios.this);
			pDialog.setMessage("Cargando Empleados. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All empleados from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_empleados, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All empleados: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// empleados found
					// Getting Array of empleados
					empleados = json.getJSONArray(TAG_empleados);

					// looping through All empleados
					for (int i = 0; i < empleados.length(); i++) {
						JSONObject c = empleados.getJSONObject(i);

						// Storing each json item in variable
						String cedula = c.getString(TAG_CEDULA);
						String nombre = c.getString(TAG_NOMBRE);
						String email = c.getString(TAG_EMAIL);
								//+ " " +c.getString(TAG_APELLIDO);
						

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_CEDULA, cedula);
						map.put(TAG_NOMBRE, nombre);
						map.put(TAG_EMAIL, email);
						

						// adding HashList to ArrayList
						empleadosList.add(map);
					}
				} else {
					// no empleados found
					// Launch Add New Empleado Activity
					Intent i = new Intent(getApplicationContext(),
							GreetingActivity.class);  // aqui era new empleado en el otro ejemplo
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
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
			// dismiss the dialog after getting all empleados
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							Usuarios.this, empleadosList,
							R.layout.list_item, new String[] { TAG_CEDULA,
									TAG_NOMBRE, TAG_EMAIL },
							new int[] { R.id.cedula, R.id.nombre, R.id.email });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}