/*
 * Copyright (C) 2014 VenomVendor <info@VenomVendor.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.embeddedlapps.primeraversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ListAdapterHolder extends RecyclerView.Adapter<ListAdapterHolder.ViewHolder> {
    JSONParser jParser = new JSONParser();

    private final FragmentActivity mActivity;
    private final List<UserDetails> mUserDetails = new ArrayList<UserDetails>();
    OnItemClickListener mItemClickListener;
    ProgressDialog pDialog;
    private static String url_all_empleados = "http://dtech20.com/appD/promocionesDia.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_promosiones = "Promociones";
    private static final String TAG_IMG = "img";
    JSONArray Promociones = null;


    public ListAdapterHolder(FragmentActivity mActivity) {
        this.mActivity = mActivity;
        Log.d("Iniciando Metodo","LoadAll");
        new LoadAllempleados().execute();
       // createUserDetails();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.single_list_item, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
        holder.vName.setText("Name: " + mUserDetails.get(position).getName());
       // holder.imageView.setImageResource(mUserDetails.get(position).getIdIMagen());
        Glide.with(mActivity)
                .load(mUserDetails.get(position).getIdIMagen())
                .diskCacheStrategy(DiskCacheStrategy.ALL).override(600, 200) .fitCenter()
                .into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return mUserDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView vName, vSex, vId, vAge;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            vName = (TextView) view.findViewById(R.id.list_name);
            imageView=(ImageView)view.findViewById(R.id.imageView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }


    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /* ==========This Part is not necessary========= */

    /**
     * Create Random Users
     */
    /*
    private void createUserDetails() {
        for (int i = 0; i < 100; i++) {
            final UserDetails mDetails = new UserDetails();
            mDetails.setName("Name " + i);
            mDetails.setIdIMagen(R.drawable.one);
            mDetails.setIdIMagen((i % 2) == 0 ? R.drawable.two : R.drawable.one);

            mUserDetails.add(mDetails);
        }
    }
*/
    /*
     * Snippet from http://stackoverflow.com/a/363692/1008278
     */
    public static int randInt(int min , int max) {
        final Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /* ==========This Part is not necessary========= */
    class LoadAllempleados extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mActivity);
            pDialog.setMessage("Cargando Anuncios. Please wait...");
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
            JSONObject json = jParser.makeHttpRequest(url_all_empleados, "POST", params);

            // Check your log cat for JSON reponse
            Log.d("All empleados: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                //Cambiar a 1 cuando se corriga el php
                if (success == 0) {
                    // empleados found
                    // Getting Array of empleados
                    Promociones = json.getJSONArray(TAG_promosiones);
                    Log.d("Promociones",Promociones.toString());
                    // looping through All empleados
                    for (int i = 0; i < Promociones.length(); i++) {
                        JSONObject c = Promociones.getJSONObject(i);

                        // Storing each json item in variable
                        String img = c.getString(TAG_IMG);
                        Log.d("IMG: ",img);


                        final UserDetails mDetails = new UserDetails();
                        mDetails.setName("Name " + i);
                        mDetails.setIdIMagen("http://dtech20.com/appD/img/imagenesUsaurios/small/tn_"+img);
                       mUserDetails.add(mDetails);

                    }
                } else {
                    // no empleados found

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


        }

    }

}
