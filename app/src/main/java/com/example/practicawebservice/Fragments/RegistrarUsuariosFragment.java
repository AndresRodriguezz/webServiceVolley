package com.example.practicawebservice.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.R;

import org.json.JSONObject;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarUsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarUsuariosFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
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
    JsonObjectRequest jsonObjectRequest;

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
                if(txtDocumento.getText().toString().isEmpty() || txtNombre.getText().toString().isEmpty() || txtProfesion.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"Llena todos los campos",Toast.LENGTH_SHORT).show();
                }else{//si los campos estan llenos se llama el metodo cargarWebService
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
        return vista;

    }


    private void mostrarDialogOpciones() {
        final CharSequence[] opciones ={"Tomar foto","Elegir de galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(opciones[which].equals("Tomar foto")){
                    abrirCamara();
                }else{
                    if(opciones[which].equals("Elegir de galeria")){
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent,"Seleccionar"),COD_SELECIONAR);
                    }else{
                        dialog.dismiss();
                    }
                }

            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (isCreada==false){
            isCreada =miFile.mkdirs();
        }
        if(isCreada==true){
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombre;//indicamos la ruta de almacenamiento

            fileImagen =new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                //Si es version superior a Nugat para guardar imagen son estas lineas
                String authorities=getContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(getContext(),authorities,fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                //En caso de que la version sea anterior a la Nugat
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            //Se cargar el activityResult con el intent y el requestCode
            startActivityForResult(intent,COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case COD_SELECIONAR:
            Uri miPatch = data.getData();
            imgFoto.setImageURI(miPatch);
            break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("Patch",""+path);
                    }
                });
                bitmap = BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);
                break;
            }
        }

    private void cargarWebService() {

        //Se pone el ProgressDialog por si hay retardo aparezca
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();
        //Direccion que se le va a dar para hacer la peticion de los datos por JSON con las condiciones
        String url = "http://192.168.0.8:82/EjemploBdRemota/wsJSONRegistro.php?documento="+txtDocumento.getText().toString()+"&nombre="+txtNombre.getText().toString()+"&profesion="+txtProfesion.getText().toString();

        url= url.replace(" ","%20");//Esta linea es para que no solo guarde la primera palabra y guarde todo

        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        //Se llena la peticion con los datos ya llenados previamente en url
        request.add(jsonObjectRequest);//Se hace la peticion de datos
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //En caso de error
        progreso.hide();
        Toast.makeText(getContext(),"No se pudo registrar", Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        //Se pone mensaje que todo ha ido correctamente
        Toast.makeText(getContext(),"Se ha registrado exitosamente",Toast.LENGTH_SHORT).show();
        //Se limpian campos
        progreso.hide();
        txtDocumento.setText("");
        txtNombre.setText("");
        txtProfesion.setText("");


    }
}
