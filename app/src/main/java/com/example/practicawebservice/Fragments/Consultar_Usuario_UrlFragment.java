package com.example.practicawebservice.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.Entidades.Usuario;
import com.example.practicawebservice.R;
import com.example.practicawebservice.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Consultar_Usuario_UrlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Consultar_Usuario_UrlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables
    ImageView imgFoto;
    EditText campoDocumento,txtNombre,txtProfesion;
    Button btnActualizar,btnEliminar;
    ImageButton btnConsultar;

    ProgressDialog pDialog;

   // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    Bitmap bitmap;

    public Consultar_Usuario_UrlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Consultar_Usuario_UrlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Consultar_Usuario_UrlFragment newInstance(String param1, String param2) {
        Consultar_Usuario_UrlFragment fragment = new Consultar_Usuario_UrlFragment();
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
        View vista = inflater.inflate(R.layout.fragment_consultar__usuario__url, container, false);
        campoDocumento = (EditText) vista.findViewById(R.id.idCampoDocumento);
        txtNombre = (EditText) vista.findViewById(R.id.txtNombre);
        txtProfesion = (EditText) vista.findViewById(R.id.txtProfesion);
        imgFoto = (ImageView)vista.findViewById(R.id.imagenId);
       // request = Volley.newRequestQueue(getContext());

        btnConsultar = (ImageButton) vista.findViewById(R.id.btnConsultar);
        btnActualizar = (Button)vista.findViewById(R.id.btnActualizar);
        btnEliminar = (Button)vista.findViewById(R.id.btnEliminar);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarUsuario();
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceActualizar();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceEliminar();
            }
        });

        return vista;
    }

    private void consultarUsuario() {
        pDialog =new ProgressDialog(getContext());
        pDialog.setMessage("Cargando...");
        pDialog.show();
        String url = "http://192.168.0.8:82/EjemploBdRemota/wsJSONConsultarUsuarioUrl.php?documento="+campoDocumento.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();
                //Toast.makeText(getContext(),"Mensaje"+response,Toast.LENGTH_SHORT).show();

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
                    miUsuario.setRuta_imagen(jsonObject.optString("ruta_imagen"));
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Error en imagen",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                txtNombre.setText(miUsuario.getNombre());//se consige el dato y se pone
                txtProfesion.setText(miUsuario.getProfesion());

                String urlImagen = "http://192.168.0.8:82/EjemploBdRemota/"+ miUsuario.getRuta_imagen();

                Toast.makeText(getContext(), "url "+urlImagen, Toast.LENGTH_SHORT).show();

                cargarWebServiceImagen(urlImagen);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al conectar"+ error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println();
                pDialog.hide();
                Log.d("Error:", error.toString());

            }
        });

        //request.add(jsonObjectRequest);
        VolleySingleton.getInstanceVolley(getContext()).addToRequestQueue(jsonObjectRequest);

    }

    private void cargarWebServiceImagen(String urlImagen) {
        urlImagen = urlImagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            //ESte Response es de tipo BitMap
            public void onResponse(Bitmap response) {
                imgFoto.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cagar imagen", Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(imageRequest);
        VolleySingleton.getInstanceVolley(getContext()).addToRequestQueue(imageRequest);
    }

    private void webServiceActualizar() {
        pDialog=new ProgressDialog(getContext());
        pDialog.setMessage("Cargando...");
        pDialog.show();



        String url="http://192.168.0.8:82/EjemploBdRemota/wsJSONUpdateMovil.php?";


        stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response.trim().equalsIgnoreCase("actualiza")){
                    // etiNombre.setText("");
                    //  txtDocumento.setText("");
                    //   etiProfesion.setText("");
                    Toast.makeText(getContext(),"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String documento=campoDocumento.getText().toString();
                String nombre=txtNombre.getText().toString();
                String profesion=txtProfesion.getText().toString();

                String imagen=convertirImgString(bitmap);

                Map<String,String> parametros=new HashMap<>();
                parametros.put("documento",documento);
                parametros.put("nombre",nombre);
                parametros.put("profesion",profesion);
                parametros.put("imagen",imagen);

                return parametros;
            }
        };
        //request.add(stringRequest);
        VolleySingleton.getInstanceVolley(getContext()).addToRequestQueue(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    private void webServiceEliminar() {
        pDialog=new ProgressDialog(getContext());
        pDialog.setMessage("Cargando...");
        pDialog.show();


        String url="http://192.168.0.8:82/ejemploBDRemota/wsJSONDeleteMovil.php?documento="+campoDocumento.getText().toString();

        stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    txtNombre.setText("");
                    campoDocumento.setText("");
                    txtProfesion.setText("");
                    imgFoto.setImageResource(R.drawable.img_base);
                    Toast.makeText(getContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("noExiste")){
                        Toast.makeText(getContext(),"No se encuentra la persona ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getInstanceVolley(getContext()).addToRequestQueue(stringRequest);
    }
}
