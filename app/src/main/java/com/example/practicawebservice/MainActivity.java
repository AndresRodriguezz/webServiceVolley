package com.example.practicawebservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bienvenidaFragment3,R.id.consultarUsuariosFragment,R.id.desarrolladorFragment,R.id.registrarUsuariosFragment,R.id.consultarListaUsuariosFragment,R.id.consultar_Usuario_UrlFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        validarPermisos();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void validarPermisos() {
        //SE checan los permisos no se encuentren abajo, en dado caso llamar el metodo cargarDialogoRecomendacion
       if((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();

        }else{
            // Se manda al onActivityResult
            requestPermissions( new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
    }
    private void cargarDialogoRecomendacion() {
        androidx.appcompat.app.AlertDialog.Builder dialogo = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debe de aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show(); //muestra el activityResult con las especificaciones previas

    }
    //Se sobreEscribe el metodo onREquestPermissionResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if(grantResults.length==2 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                }else{
                    solicitarPermisosManual();
                    Toast.makeText(getApplicationContext(),"No se aceptaron los permisos de primera instancia",Toast.LENGTH_SHORT).show();
                }

        }

    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"Si","No"}; //cargamos las opciones
        //Se crea el cuadro de dialogo en el mismo Activity y tambien se crea el objeto alertaOpciones
        final androidx.appcompat.app.AlertDialog.Builder alertaOpciones = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        //Titulo del mensaje del cuadro de dialogo
        alertaOpciones.setTitle("Desea configurar los permisos de manera manual?");
        //Se carga el onClick listener a las opciones de nuestro alertDialog
        alertaOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Wich es el dato que recibimos como parametro
                if(opciones[which].equals("Si")){
                    Intent intent = new Intent();
                    //Asignamos a nuestro objeto la accion
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"No se aceptaron los permisos",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

            }
        });
        //Se enseña alerDialog
        alertaOpciones.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
