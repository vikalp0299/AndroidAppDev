package com.example.navigationbar.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.navigationbar.R;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationbar.model.TeamData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.navigationbar.adapters.TeamAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {

    Context context;
    private ArrayList<TeamData> teamList;
    private TeamAdapter adapter;

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
        loadData();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.addingBtn);
        RecyclerView recyclerView = view.findViewById(R.id.mRecycler);
        adapter = new TeamAdapter(getContext(), teamList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(it -> TeamsFragment.this.addInfo());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addInfo() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.add_item, null);
        final EditText teamName = v.findViewById(R.id.teamName);
        final EditText teamDetail = v.findViewById(R.id.teamDetails);
        Builder addDialog = new Builder(Objects.requireNonNull(getContext()));
        addDialog.setView(v);
        addDialog.setPositiveButton("Ok", (dialog, $noName_1) -> {
            EditText editText = teamName;
            String names = editText.getText().toString();
            editText = teamDetail;
            String details = editText.getText().toString();
            TeamsFragment.access$getTeamList$p(TeamsFragment.this).add(new TeamData(names, details));
            TeamsFragment.access$getTeamAdapter$p(TeamsFragment.this).notifyDataSetChanged();
            Toast.makeText(getContext(), "Added Team Successfully", Toast.LENGTH_SHORT).show();
            saveData();
            dialog.dismiss();
        });
        addDialog.setNegativeButton("Cancel", (dialog, $noName_1) -> {
            dialog.dismiss();
            Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
        });
        addDialog.create();
        addDialog.show();
    }

    public static ArrayList<TeamData> access$getTeamList$p(TeamsFragment $this) {
        return $this.teamList;
    }

    public static TeamAdapter access$getTeamAdapter$p(TeamsFragment $this) {
        return $this.adapter;
    }

    private void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(teamList);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<TeamData>>() {}.getType();
        teamList = gson.fromJson(json, type);
        if (teamList == null) {
            teamList = new ArrayList<>();
        }
    }

}