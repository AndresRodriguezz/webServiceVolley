package com.example.practicawebservice.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarUsuariosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText txtDocumento, txtNombre, txtProfesion;
    Button btnRegistrar, btnTomarFoto;
    ImageView imgFoto;
    ProgressDialog progreso;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest; //Por metodo POST ya no utilizaremos el objeto
    StringRequest stringRequest;

    private static final int COD_SELECIONAR = 10;
    private static final int COD_FOTO = 20;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//Directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN; //Ruta de imagen de directorios
    private String path; //Almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;


    public RegistrarUsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarUsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarUsuariosFragment newInstance(String param1, String param2) {
        RegistrarUsuariosFragment fragment = new RegistrarUsuariosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_registrar_usuarios, container, false);
        // Inflate the layout for this fragment

        txtDocumento = (EditText) vista.findViewById(R.id.txtDocumento);
        txtNombre = (EditText) vista.findViewById(R.id.txtNombre);
        txtProfesion = (EditText) vista.findViewById(R.id.txtProfesion);
        btnRegistrar = (Button) vista.findViewById(R.id.btnRegistrar);
        btnTomarFoto = (Button) vista.findViewById(R.id.btnFoto);
        imgFoto = (ImageView) vista.findViewById(R.id.imgFoto);


        request = Volley.newRequestQueue(getContext());


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //condicion para checar si los campos estan vacios si lo estan se pide que se llenen
                if (txtDocumento.getText().toString().isEmpty() || txtNombre.getText().toString().isEmpty() || txtProfesion.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {//si los campos estan llenos se llama el metodo cargarWebService
                    cargarWebService();
                }


            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();

            }
        });

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });
        return vista;

    }


    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Tomar foto")) {
                    abrirCamara();
                } else {
                    if (opciones[which].equals("Elegir de galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Seleccionar"), COD_SELECIONAR);
                    } else {
                        dialog.dismiss();
                    }
                }

            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (isCreada == false) {
            isCreada = miFile.mkdirs();
        }
        if (isCreada == true) {
            Long consecutivo = System.currentTimeMillis() / 1000;
            String nombre = consecutivo.toString() + ".jpg";

            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN + File.separator + nombre;//indicamos la ruta de almacenamiento

            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Si es version superior a Nugat para guardar imagen son estas lineas
                String authorities = getContext().getPackageName() + ".provider";
                Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //En caso de que la version sea anterior a la Nugat
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            //Se cargar el activityResult con el intent y el requestCode
            startActivityForResult(intent, COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECIONAR:
                Uri miPatch = data.getData();
                imgFoto.setImageURI(miPatch);
                try {
                    //Se convierte la cadena mipath a tipo bitMap
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPatch);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("Patch", "" + path);
                    }
                });
                bitmap = BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);
                break;
        }
        bitmap = redimensionarImagen(bitmap,600,800);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth(); //consegumos el ancho de la imagen usado a bitmap
        int alto = bitmap.getHeight();//Conseguimos el alto de la imagen usando a bitmap

        if(ancho>anchoNuevo || alto>altoNuevo){//comparamos el ancho que obtenemos en la variable ancho con el ancho que recibimos lo mismo para alto
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;

            Matrix matrix = new Matrix();//instanciamos nuestro objeto de tipo Matrix
            matrix.postScale(escalaAncho,escalaAlto);//accedemos al metodo postScale le pasamos los parametros creados anteriormente

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false); //Regresamos el dato ya escalado

        }else{
            return bitmap; //si la imagen no cumple con la conddcion de arriba regresamos al bitmap tal cual.
        }
    }

    private void cargarWebService() {

        //Se pone el ProgressDialog por si hay retardo aparezca
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();
        //Direccion que se le va a dar para hacer la peticion de los datos por JSON con las condiciones con metodo POST
        String url = "http://192.168.0.8:82/EjemploBdRemota/wsJSONRegistroMovil.php?";
        //Creamos un objeto de tipo StringRequest. con el new Response ya nos da la implementacion para el metodo onResponse
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override//Es el que se encarga de recibir la respeusta del webService cuando todo esta correcto
            public void onResponse(String response) {
                progreso.hide();
                if(response.trim().equalsIgnoreCase("registra")){
                    //Se limpian los campos
                    txtDocumento.setText("");
                    txtNombre.setText("");
                    txtProfesion.setText("");
                    imgFoto.setImageResource(R.drawable.img_base);

                    Toast.makeText(getContext(), "Se ha registrado con exito", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "No se ha registrado al nuevo registro", Toast.LENGTH_SHORT).show();
                }

            }//Damos referencia al interfas ErrorListener. Se genera nuestro metodo.Aqui cuando hay algun tipo de error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Aqui hay error en la conexion web service
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progreso.hide();

            }
        }
        ){// se ponen llaves enter mas getParams.
            @Override //Nos va permitir enviar nuestros parametros al webService mediante POST
            //El contenido de este Map va a ser toda la informacion que tenemos en el formulario
            protected Map<String, String> getParams() throws AuthFailureError {
                //Construimos el llamado
                String documento = txtDocumento.getText().toString();
                String nombre = txtNombre.getText().toString();
                String profesion = txtProfesion.getText().toString();

                //creamos un metodo que se encarga de convertir la de bitmap en String
                String imagen = convertirImgString(bitmap);

                //Aimentamos a Map
                Map<String, String>parametros = new HashMap<>();
                parametros.put("documento",documento);
                parametros.put("nombre",nombre);
                parametros.put("profesion",profesion);
                parametros.put("imagen",imagen);

                return parametros;
            }
        };
        //Agregamos los datos al stringRequest
        request.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {
        //Metodo que convierte bitma a String
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        //Se comprime
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imagenByte = array.toByteArray();
        //Se codifica en bas64 retornado en String
        String imagenString  = Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }
}


