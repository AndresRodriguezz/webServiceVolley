package com.example.practicawebservice.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practicawebservice.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BienvenidaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BienvenidaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtLink;

    public BienvenidaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BienvenidaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BienvenidaFragment newInstance(String param1, String param2) {
        BienvenidaFragment fragment = new BienvenidaFragment();
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
        View vista = inflater.inflate(R.layout.fragment_bienvenida, container, false);
        // Inflate the layout for this fragment
        txtLink = vista.findViewById(R.id.txtLink);

        txtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLink();
            }
        });
        return vista;
    }

    private void abrirLink() {
        txtLink.setTextColor(getResources().getColor(R.color.colorBlanco));

        Uri uriUrl = Uri.parse("https://github.com/AndresRodriguezz/webServiceVolley");
        Intent launcherBrowser = new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(launcherBrowser);
    }
}
