package com.example.practicawebservice.Entidades;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class UsuariosImagenUrlAdapter extends RecyclerView.Adapter<UsuariosImagenUrlAdapter.UsuariosHolder> {
    List<Usuario> listaUsuarios;
    RequestQueue request;
    Context context;

    public UsuariosImagenUrlAdapter(List<Usuario> listaUsuarios, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
        request = Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public UsuariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuario_list,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosHolder holder, int position) {
        //Llenado de datos desde aca, usando el objeto holder
        //traigame la lista de personajes en la posicion getNombre etc
        holder.txtDocuemnto.setText(listaUsuarios.get(position).getDocumento().toString());
        holder.txtNombre.setText(listaUsuarios.get(position).getNombre().toString());
        holder.txtProfesion.setText(listaUsuarios.get(position).getProfesion().toString());

        if(listaUsuarios.get(position).getRuta_imagen()!=null){

            cargarImagenWebService(listaUsuarios.get(position).getRuta_imagen(),holder);

        }else {
            Toast.makeText(context, "Error al cargan imagen", Toast.LENGTH_SHORT).show();
            holder.imagenUsuario.setImageResource(R.drawable.img_base);
        }


    }

    private void cargarImagenWebService(String ruta_imagen, final UsuariosHolder holder) {

        String urlImagen="http://192.168.0.8:82/ejemploBDRemota/"+ruta_imagen;
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
             holder.imagenUsuario.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error de imagen", Toast.LENGTH_SHORT).show();

            }
        });
        request.add(imageRequest);

    }

    @Override
    public int getItemCount() {

        return listaUsuarios.size();
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder {
        TextView txtDocuemnto, txtNombre,txtProfesion;
        ImageView imagenUsuario;

        public UsuariosHolder(@NonNull View itemView) {
            super(itemView);
            txtDocuemnto = (TextView)itemView.findViewById(R.id.txtDocumento);
            txtNombre = (TextView)itemView.findViewById(R.id.txtNombre);
            txtProfesion = (TextView)itemView.findViewById(R.id.txtProfesion);
            imagenUsuario = (ImageView)itemView.findViewById(R.id.idImagen);
        }
    }
}
