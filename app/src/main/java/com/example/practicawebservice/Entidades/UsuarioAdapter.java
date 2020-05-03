package com.example.practicawebservice.Entidades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practicawebservice.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuariosHolder> {
    ArrayList<Usuario> listaUsuarios;

    public UsuarioAdapter(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
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
        if(listaUsuarios.get(position).getImagen()!=null){
            holder.imagenUsuario.setImageBitmap(listaUsuarios.get(position).getImagen());
        }else {
            holder.imagenUsuario.setImageResource(R.drawable.img_base);
        }


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
