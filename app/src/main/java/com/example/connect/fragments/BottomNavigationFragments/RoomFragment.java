package com.example.connect.fragments.BottomNavigationFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.connect.R;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.model.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.connect.adapters.RoomSectionAdapters.RoomAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment implements SearchView.OnQueryTextListener{

    private ArrayList<Team> teamList;
    private RoomAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    
    public RoomFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String param1, String param2) {
        RoomFragment fragment = new RoomFragment();
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
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        loadData();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.addingBtn);
        RecyclerView recyclerView = view.findViewById(R.id.mRecycler);
        adapter = new RoomAdapter(getContext(), teamList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(it -> RoomFragment.this.addInfo());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    //search filter code
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                adapter.setFilter(teamList);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true; // Return true to expand action view
            }
        });

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<Team> filteredModelList = filter(teamList, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<Team> filter(ArrayList<Team> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Team> filteredModelList = new ArrayList<>();
        for (Team model : models) {
            final String text = model.getTeamName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    //saves fragment data on different states
    @Override
    public void onResume() {
        super.onResume();
        saveData();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveData();
    }

    //dialog box for adding team
    private void addInfo() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.add_room_item, null);
        final EditText teamName = v.findViewById(R.id.roomName);
        final EditText teamDetail = v.findViewById(R.id.roomDetails);
        Builder addDialog = new Builder(Objects.requireNonNull(getContext()));
        addDialog.setView(v);
        addDialog.setPositiveButton("Ok", (dialog, $noName_1) -> {
            EditText editText = teamName;
            String names = editText.getText().toString();
            editText = teamDetail;
            String details = editText.getText().toString();
            if(names.equals("") || details.equals("")){
                Toast.makeText(getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
            }
            else {
                RoomFragment.access$getTeamList$p(RoomFragment.this).add(new Team(names, details));
                RoomFragment.access$getTeamAdapter$p(RoomFragment.this).notifyDataSetChanged();
                Toast.makeText(getContext(), "Added Team Successfully", Toast.LENGTH_SHORT).show();
                saveData();
            }
            dialog.dismiss();
        });
        addDialog.setNegativeButton("Cancel", (dialog, $noName_1) -> {
            dialog.dismiss();
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        });
        addDialog.create();
        addDialog.show();
    }

    public static ArrayList<Team> access$getTeamList$p(RoomFragment $this) {
        return $this.teamList;
    }

    public static RoomAdapter access$getTeamAdapter$p(RoomFragment $this) {
        return $this.adapter;
    }

    //saves data i.e, saves created rooms and any updates to it
    private void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(teamList);
        editor.putString("task list", json);
        editor.apply();
    }

    //loads team data on relaunching of application
    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Team>>() {}.getType();
        teamList = gson.fromJson(json, type);
        if (teamList == null) {
            teamList = new ArrayList<>();
        }
    }

}