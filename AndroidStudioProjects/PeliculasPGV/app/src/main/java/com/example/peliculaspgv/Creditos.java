package com.example.peliculaspgv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Creditos extends AppCompatActivity {

    private ArrayList<Actor> listaCreditos = new ArrayList<Actor>();
    private ListView listView;
    public static String urlCreditos = "https://api.themoviedb.org/3/movie/{ID_PELICULA}/credits?api_key={API_KEY}&language=en-US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);
        Bundle b = this.getIntent().getExtras();
        setTitle("Actores: " + b.getString("Titulo"));

        //Creamos la ventana con la información de la API de los créditos que encontramos en themoviedb.
        listView = findViewById(R.id.listView);
        urlCreditos = urlCreditos.replace("{ID_PELICULA}", String.valueOf(b.getInt("IDPelicula")));
        urlCreditos = urlCreditos.replace("{API_KEY}", MainActivity.API_KEY);
        new ObtenerCreditosAsync().execute(urlCreditos);
    }

    /*Creamos una clase para guardar los datos de la API en un ArrayList y llamamos a la clase
    AdaptadorCreditos para añadirle al list.
    El proceso es prácticamente idéntico al que usamos en el MainActivity*/
    class ObtenerCreditosAsync extends AsyncTask<String, Integer, String>
    {
        ProgressDialog progreso;

        protected void onPreExecute ()
        {
            super.onPreExecute();

            //Mostrar progress bar.
            progreso = new ProgressDialog(Creditos.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Obteniendo creditos...");
            progreso.setCancelable(false);
            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }

        protected String doInBackground(String... params)
        {
            StringBuilder result = new StringBuilder();

            try
            {
                URL urlObj = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = reader.readLine()) != null) result.append(line);

                Log.d("test", "respuesta: " + result.toString());

            }catch (Exception e)
            {
                Log.d("test", "error2: " + e.toString());
            }

            return result.toString();
        }

        protected void onProgressUpdate(Integer...a){
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(String result) {
            JSONObject resp = null;
            JSONArray creditos = null;

            try {
                resp = new JSONObject(result);
                creditos = resp.getJSONArray("cast");

                for (int i=0; i < creditos.length(); i++) {
                    JSONObject actor = creditos.getJSONObject(i);
                    if(!"acting".equalsIgnoreCase(actor.getString("known_for_department"))) continue;
                    listaCreditos.add( new Actor(
                            actor.getInt("id"),
                            actor.getString("name"),
                            actor.getString("original_name"),
                            actor.getString("profile_path")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            progreso.dismiss();

            AdaptadorCreditos adaptador = new AdaptadorCreditos(getApplicationContext(), listaCreditos);
            listView.setAdapter(adaptador);
        }
    }

    class AdaptadorCreditos extends BaseAdapter {
        Context context;
        ArrayList<Actor> arrayList;
        public static final String urlImagen1 = "https://image.tmdb.org/t/p/w185";

        public AdaptadorCreditos(Context context, ArrayList<Actor> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public int getCount() {
            return arrayList.size();
        }

        public Actor getItem(int position) {
            return arrayList.get(position);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_creditos, parent, false);
            }

            TextView titulo = (TextView) convertView.findViewById(R.id.tvTitulo);
            titulo.setText(arrayList.get(position).getName());

            TextView subtitulo = (TextView) convertView.findViewById(R.id.tvSubtitulo);
            subtitulo.setText(arrayList.get(position).getOriginalName());

            ImageView imagen = (ImageView) convertView.findViewById(R.id.list_image);
            Picasso.get().load(urlImagen1 + arrayList.get(position).getProfilePath()).into(imagen);
            imagen.setScaleType(ImageView.ScaleType.FIT_XY);

            return convertView;
        }
    }
}
