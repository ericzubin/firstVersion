package com.embeddedlapps.primeraversion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Actividad que muestra la imagen del item extendida
 */
public class ActividadDetalle extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "com.herprogramacion.coches2015.extra.ID";
    public static final String VIEW_NAME_HEADER_IMAGE = "imagen_compartida";
    private UserDetails itemDetallado;
    private ImageView imagenExtendida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle);


        // Obtener el coche con el identificador establecido en la actividad principal
        //itemDetallado = Negocio.getItem(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

        imagenExtendida = (ImageView) findViewById(R.id.imagen_extendida);


        cargarImagenExtendida();
    }

    private void cargarImagenExtendida() {
        Bundle bundle = getIntent().getExtras();
        Glide.with(imagenExtendida.getContext())
                .load(R.drawable.two)
                .into(imagenExtendida);
    }


    public void onCliUbicacion(View vieww) //declarado en la vista del boton en el archivo xml
    {
        irUbicacion();
    }
    public void irUbicacion(){
        Log.d("fua Ubicacion", "fue");
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
