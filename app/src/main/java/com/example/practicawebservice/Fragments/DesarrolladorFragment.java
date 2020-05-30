package com.example.practicawebservice.Fragments;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.practicawebservice.R;
import com.example.practicawebservice.WebViewTwitterActivity;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DesarrolladorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesarrolladorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RequestQueue request;
    private ImageView imgFacebook,imgTwitter,imgInstagram,imgMusica,imgGitHub;
    private MediaPlayer mediaPlayer;
    private int i=0;


    public DesarrolladorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DesarrolladorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DesarrolladorFragment newInstance(String param1, String param2) {
        DesarrolladorFragment fragment = new DesarrolladorFragment();
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
        View vista = inflater.inflate(R.layout.fragment_desarrollador, container, false);
        imgFacebook = (ImageView)vista.findViewById(R.id.imgFacebook);
        imgInstagram = (ImageView)vista.findViewById(R.id.imgInstagram);
        imgTwitter = (ImageView)vista.findViewById(R.id.imgTwitter);
        imgMusica = (ImageView)vista.findViewById(R.id.andresFlow);
        imgGitHub = (ImageView)vista.findViewById(R.id.imgGitHub);

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.middle);

        request = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirFacebook();
            }
        });
        imgInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirInstagram();
            }
        });
        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTwitterPorMedioWeb();
            }
        });
        imgGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGitHub();
            }
        });
        imgMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducirMusica();
            }
        });

        return vista;

    }




    private void abrirFacebook() {
        Uri uriUrl= Uri.parse("https://www.facebook.com/andy.coffcoff/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
    private void abrirTwitterPorMedioWeb() {
        //Se Usa WebView para abrir este link
        Intent miIntent = new Intent(getContext(), WebViewTwitterActivity.class);
        startActivity(miIntent);
    }
    private void abrirInstagram() {
        Uri uriUrl = Uri.parse("https://www.instagram.com/anditititop/");
        Intent launcherBrowser = new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(launcherBrowser);
    }
    private void abrirGitHub() {
        Uri uriUrl = Uri.parse("https://github.com/AndresRodriguezz");
        Intent launcherBrowser = new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(launcherBrowser);
    }
    private void reproducirMusica() {
            if(mediaPlayer.isPlaying()) {
                Toast.makeText(getContext(), "Pause", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
            }else{
                mediaPlayer.start();
                Toast.makeText(getContext(), "Song: Dj Snake-Middle", Toast.LENGTH_SHORT).show();
            }

        }

}
