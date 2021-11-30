package com.example.peliculaspgv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private ArrayList<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Peliculas");

        listView = findViewById(R.id.listView);

        endPointPeliculas = endPointPeliculas.replace("{API_KEY}", API_KEY);
        new ObtenerPeliculasAsync().execute(endPointPeliculas);

        //Creamos el listener para acceder a la información de una película.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Pelicula opcionSeleccionada = (Pelicula) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, Seleccion.class);
                Bundle b = new Bundle();
                b.putInt("ID", opcionSeleccionada.getId());
                b.putString("Titulo", opcionSeleccionada.getTitle());
                b.putString("Imagen", opcionSeleccionada.getBackdrop_path());
                b.putString("Texto", opcionSeleccionada.getOverview());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Le damos función al los botones del menú.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            //Este botón actualiza la app. Sirve sobretodo si hemos cambiado de API.
            case R.id.update:
                finish();
                startActivity(getIntent());
                return true;
            case R.id.mnuConfiguracion:
                Intent i1 = new Intent(MainActivity.this, Configuration.class);
                Bundle b1 = new Bundle();
                i1.putExtras(b1);
                startActivity(i1);
                return true;
            case R.id.mnuAcerca:
                Intent i2 = new Intent(MainActivity.this, ACercaDe.class);
                Bundle b2 = new Bundle();
                i2.putExtras(b2);
                startActivity(i2);
                return true;
            case R.id.mnuSalir:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String API_KEY = "1789b6f6586a519db9d64009b040ed59";
    public static String endPointPeliculas =
            "http://api.themoviedb.org/3/discover/movie?api_key={API_KEY}";
    public static String MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    class ObtenerPeliculasAsync extends AsyncTask<String, Integer, String>
    {
        ProgressDialog progreso;

        protected void onPreExecute ()
        {
            super.onPreExecute();

            //Mostrar progress bar.
            progreso = new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Obteniendo peliculas...");
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
            JSONArray peliculas = null;

            try {
                resp = new JSONObject(result);
                peliculas = resp.getJSONArray("results");

                for (int i=0; i < peliculas.length(); i++) {
                    JSONObject pelicula = peliculas.getJSONObject(i);

                    //txtPeliculas.append( pelicula.getString("original_title") + "\n" );

                    listaPeliculas.add( new Pelicula(
                            pelicula.getInt("id"),
                            pelicula.getString("title"),
                            pelicula.getString("backdrop_path"),
                            pelicula.getString("poster_path"),
                            pelicula.getString("original_title"),
                            pelicula.getString("overview"),
                            pelicula.getDouble("popularity"),
                            pelicula.getString("release_date")));
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

            AdaptadorPelicula adaptador = new AdaptadorPelicula(getApplicationContext(),listaPeliculas);
            listView.setAdapter(adaptador);

        }
    }

    class AdaptadorPelicula extends BaseAdapter {
        Context context;
        ArrayList<Pelicula> arrayList;

        public AdaptadorPelicula(Context context, ArrayList<Pelicula> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public int getCount() {
            return arrayList.size();
        }

        public Pelicula getItem(int position) {
            return arrayList.get(position);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.lista_peliculas, parent, false);
            }

            //Fecha.
            TextView fecha = (TextView) convertView.findViewById(R.id.tvFecha);
            fecha.setText(arrayList.get(position).getRelease_date());

            //Titulo.
            TextView name = (TextView) convertView.findViewById(R.id.tvTitle);
            name.setText(arrayList.get(position).getTitle());

            //Descripción.
            TextView descripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
            descripcion.setText(arrayList.get(position).getOverview().substring(0,100) + " ... ");

            //Imagen.
            ImageView imagen = (ImageView) convertView.findViewById(R.id.list_image);
            Picasso.get().load(MOVIE_BASE_URL + arrayList.get(position).getBackdrop_path()).into(imagen);
            imagen.setScaleType(ImageView.ScaleType.FIT_XY);

            return convertView;
        }
    }
}
