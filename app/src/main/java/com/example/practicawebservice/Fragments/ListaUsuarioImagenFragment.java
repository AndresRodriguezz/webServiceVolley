package com.example.practicawebservice.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.Entidades.Usuario;
import com.example.practicawebservice.Entidades.UsuarioAdapter;
import com.example.practicawebservice.Entidades.UsuariosImagenUrlAdapter;
import com.example.practicawebservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaUsuarioImagenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaUsuarioImagenFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ImageView imagenSinConexion;
    ArrayList<Usuario> listaUsuarios;

    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public ListaUsuarioImagenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaUsuarioImagenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaUsuarioImagenFragment newInstance(String param1, String param2) {
        ListaUsuarioImagenFragment fragment = new ListaUsuarioImagenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_lista_usuario_imagen, container, false);
        //Instancia de la lista de los usuarios
        listaUsuarios = new ArrayList<>();

        recyclerView = (RecyclerView)vista.findViewById(R.id.idRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        imagenSinConexion = (ImageView)vista.findViewById(R.id.noInternet);
        imagenSinConexion.setVisibility(View.INVISIBLE);

        request = Volley.newRequestQueue(getContext());

        ConnectivityManager conn = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if(networkInfo!= null && networkInfo.isConnected()){
            imagenSinConexion.setVisibility(View.INVISIBLE);
            cargarWebService();

        }else {
            imagenSinConexion.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();

        }



        return vista;
    }

    private void cargarWebService() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultado...");
        progress.show();
        String url = "http://192.168.0.8:82/EjemploBdRemota/wsJSONConsultarListaImagenesUrl.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"No se pudo conectar",Toast.LENGTH_SHORT).show();
        progress.hide();
        System.out.println();
        Log.d("Error",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        progress.hide();
        Usuario usuario=null;
        JSONArray json = response.optJSONArray("usuario");
        try {

            for (int i=0;i<json.length();i++){
                usuario = new Usuario ();
                JSONObject jsonObject = null;
                //jsonObject = json.getJSONObject(i); se le implementa el Try catch
                jsonObject =json.getJSONObject(i);

                usuario.setDocumento(jsonObject.optInt("documento"));
                usuario.setNombre(jsonObject.optString("nombre"));
                usuario.setProfesion(jsonObject.optString("profesion"));
                usuario.setRuta_imagen(jsonObject.optString("ruta_imagen"));
                listaUsuarios.add(usuario);
            }
            progress.hide();
            UsuariosImagenUrlAdapter adaptar = new UsuariosImagenUrlAdapter(listaUsuarios,getContext());
            recyclerView.setAdapter(adaptar);

        }   catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"No se ha podido establecer la conexion con el servidor"+""+response,Toast.LENGTH_SHORT).show();
            progress.hide();
        }

    }

}
