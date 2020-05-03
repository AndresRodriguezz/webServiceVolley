package com.example.practicawebservice.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.Entidades.Usuario;
import com.example.practicawebservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarUsuariosFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText campoDocumento;
    TextView txtNombre, txtProfesion;
    Button btnConsultar;
    ImageView imagenUSuario;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public ConsultarUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultarUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarUsuariosFragment newInstance(String param1, String param2) {
        ConsultarUsuariosFragment fragment = new ConsultarUsuariosFragment();
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
        View vista =inflater.inflate(R.layout.fragment_consultar_usuarios, container, false);


        campoDocumento = (EditText) vista.findViewById(R.id.idCampoDocumento);
        txtNombre = (TextView)vista.findViewById(R.id.txtNombre);
        txtProfesion = (TextView)vista.findViewById(R.id.txtProfesion);
        btnConsultar = (Button)vista.findViewById(R.id.btnConsultar);
        imagenUSuario = (ImageView)vista.findViewById(R.id.imagenId);

        request = Volley.newRequestQueue(getContext());
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarUsuario();
            }
        });


        return vista;
    }

    private void consultarUsuario() {
        String url = "http://192.168.0.9:82/EjemploBdRemota/wsJSONConsultarUsuarioImagen.php?documento="+campoDocumento.getText().toString();
        //Direccion de json para peticion
        url= url.replace(" ","%20");
        //se pone esta linea pra evitar que solo lea la primera palabra
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        //Se rellena la peticion
        request.add(jsonObjectRequest);
        //peticion por json
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"No se pudo consultar",Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(),"Mensaje"+response,Toast.LENGTH_SHORT).show();

        //Se crea un paquete de entidades el cual contiene la clase Usuario donde se ponen los setter and getter para crear un obj
        // de nuestros atributos.

        Usuario miUsuario = new Usuario();
        // creamos mi objeto miUSuario de la clase Usuario que esta en Entidades con los atributos de la clase Usuario();

        JSONArray json= response.optJSONArray("usuario");
        //Nombre del arreglo
        JSONObject jsonObject = null;

        try {
            jsonObject = json.getJSONObject(0); //posicion de referencia
            miUsuario.setNombre(jsonObject.optString("nombre"));//nombre de la columna se asigana a setNombre
            miUsuario.setProfesion(jsonObject.optString("profesion"));//nombre de la columna se asigana a setProfesion
            miUsuario.setDato(jsonObject.optString("imagen"));
        } catch (JSONException e) {
            Toast.makeText(getContext(),"Error en imagen",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        txtNombre.setText(miUsuario.getNombre());//se consige el dato y se pone
        txtProfesion.setText(miUsuario.getProfesion());
        if(miUsuario.getImagen()!=null){
            imagenUSuario.setImageBitmap(miUsuario.getImagen());
        } else{
            imagenUSuario.setImageResource(R.drawable.img_base);
        }


    }
}
