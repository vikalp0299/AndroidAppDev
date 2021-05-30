package com.example.connect.fragments.RoomActivityFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.adapters.RoomSectionAdapters.FileAdapter.FileAdapter;
import com.example.connect.adapters.RoomSectionAdapters.FileAdapter.FileAdapter;
import com.example.connect.adapters.RoomSectionAdapters.FileAdapter.FileAdapter;
import com.example.connect.model.File;
import com.example.connect.model.File;
import com.example.connect.model.File;
import com.example.connect.model.File;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilesFragment extends Fragment implements SearchView.OnQueryTextListener {
    private ArrayList<File> files = new ArrayList<>();

    private FileAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilesFragment newInstance(String param1, String param2) {
        FilesFragment fragment = new FilesFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        addFolders();
        loadData();
        Log.d("files",files.toString());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fileAddingBtn);
        RecyclerView recyclerView = view.findViewById(R.id.fileRecycler);
        adapter = new FileAdapter(getContext(), files);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(it -> FilesFragment.this.addFileInfo());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy",files.toString());
        saveData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyView",files.toString());
       //delFolders();
        saveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onDResume",files.toString());
        saveData();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop",files.toString());
        saveData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause",files.toString());
        saveData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("onDetach",files.toString());
        saveData();
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
                adapter.setFilter(files);
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
        final ArrayList<File> filteredModelList = filter(files, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<File> filter(ArrayList<File> models, String query) {
        query = query.toLowerCase();

        final ArrayList<File> filteredModelList = new ArrayList<>();
        for (File model : models) {
            final String text = model.getFileName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;

    }

    private void addFileInfo() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.add_file_item, null);
        final EditText fileName = v.findViewById(R.id.fileTitle);
        AlertDialog.Builder addDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        addDialog.setView(v);
        addDialog.setPositiveButton("Ok", (dialog, $noName_1) -> {
            EditText editText = fileName;
            String names = editText.getText().toString();
            if(names.equals("")){
                Toast.makeText(getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
            }
            else {

                FilesFragment.access$getFileList$p(FilesFragment.this).add(new File(names,R.mipmap.file_image_round));
                FilesFragment.access$getFileAdapter$p(FilesFragment.this).notifyDataSetChanged();
                saveData();
                Toast.makeText(getContext(), "Added File Successfully", Toast.LENGTH_SHORT).show();
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

    public static ArrayList<File> access$getFileList$p(FilesFragment $this) {
        return $this.files;
    }

    public static FileAdapter access$getFileAdapter$p(FilesFragment $this) {
        return $this.adapter;
    }

    //saves data i.e, saves created rooms and any updates to it
    private void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(files);
        editor.putString("task list", json);
        editor.apply();
    }

    //loads data
    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<File>>() {}.getType();
        files = gson.fromJson(json, type);
        if (files == null) {
            files = new ArrayList<>();
        }
    }

    private void delFolders(){
        if(files.size()>2) {
            files.remove(0);
            files.remove(1);
        }
    }

    private void addFolders(){
        files.add(new File("Class Materials",R.mipmap.folder_image_round));
        files.add(new File("Old Question Papers",R.mipmap.folder_image_round));
    }

}