package com.example.navigationbar.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.navigationbar.R;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.example.navigationbar.model.TeamData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.navigationbar.view.TeamAdapter;
import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {

    //View view;
    /*RecyclerView recyclerView;
    List<ModalClass> mList;
    CustomAdapter customAdapter;*/
    //MyRecyclerViewAdapter adapter;
    private FloatingActionButton addsBtn;
    private RecyclerView recv;
    private ArrayList userList;
    private TeamAdapter adapter;
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    
    public TeamsFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamsFragment newInstance(String param1, String param2) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        userList = new ArrayList();
        addsBtn = (FloatingActionButton)view.findViewById(R.id.addingBtn);
        recv = (RecyclerView)view.findViewById(R.id.mRecycler);
        adapter = new TeamAdapter(getContext(),userList);
        recv.setLayoutManager((LayoutManager)(new LinearLayoutManager(getContext())));
        recv.setAdapter((Adapter)adapter);
        addsBtn.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                TeamsFragment.this.addInfo();
            }
        }));
        //setRetainInstance(true);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    private final void addInfo() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.add_item, (ViewGroup)null);
        final EditText userName = (EditText)v.findViewById(R.id.teamName);
        final EditText userNo = (EditText)v.findViewById(R.id.teamDetails);
        Builder addDialog = new Builder(getContext());
        addDialog.setView(v);
        addDialog.setPositiveButton((CharSequence)"Ok", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialog, int $noName_1) {
                EditText editText = userName;
                String names = editText.getText().toString();
                editText = userNo;
                String number = editText.getText().toString();
                TeamsFragment.access$getTeamList$p(TeamsFragment.this).add(new TeamData(names, number));
                TeamsFragment.access$getTeamAdapter$p(TeamsFragment.this).notifyDataSetChanged();
                Toast.makeText(getContext(), (CharSequence)"Added Team Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }));
        addDialog.setNegativeButton((CharSequence)"Cancel", (android.content.DialogInterface.OnClickListener)(new android.content.DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialog, int $noName_1) {
                dialog.dismiss();
                Toast.makeText(getContext(), (CharSequence)"Cancel", Toast.LENGTH_SHORT).show();
            }
        }));
        addDialog.create();
        addDialog.show();
    }

    public static final ArrayList access$getTeamList$p(TeamsFragment $this) {
        ArrayList recv = $this.userList;
        return recv;
    }

    public static final void access$setTeamList$p(TeamsFragment $this, ArrayList var1) {
        $this.userList = var1;
    }

    public static final TeamAdapter access$getTeamAdapter$p(TeamsFragment $this) {
        TeamAdapter recv = $this.adapter;
        return recv;
    }

    public static final void access$setTeamAdapter$p(TeamsFragment $this, TeamAdapter var1) {
        $this.adapter = var1;
    }
}