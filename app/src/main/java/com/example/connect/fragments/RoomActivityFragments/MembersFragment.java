package com.example.connect.fragments.RoomActivityFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.model.Member;
import com.example.connect.R;
import com.example.connect.adapters.RoomSectionAdapters.MemberAdapter.MemberAdapter;
import com.example.connect.fragments.BottomNavigationFragments.RoomFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MembersFragment extends Fragment implements SearchView.OnQueryTextListener{
    private ArrayList<Member> members = new ArrayList<>();
    private MemberAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MembersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MembersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MembersFragment newInstance(String param1, String param2) {
        MembersFragment fragment = new MembersFragment();
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
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.memberAddingBtn);
        RecyclerView recyclerView = view.findViewById(R.id.memberRecycler);
        adapter = new MemberAdapter(getContext(), members);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(it -> MembersFragment.this.addMemberInfo());

        return view;
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
                adapter.setFilter(members);
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
        final ArrayList<Member> filteredModelList = filter(members, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<Member> filter(ArrayList<Member> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Member> filteredModelList = new ArrayList<>();
        for (Member model : models) {
            final String text = model.getMemberName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void addMemberInfo() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.add_member_item, null);
        final EditText memberName = v.findViewById(R.id.memberName);
        final EditText memberDesc = v.findViewById(R.id.memberEmail);
        AlertDialog.Builder addDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        addDialog.setView(v);
        addDialog.setPositiveButton("Ok", (dialog, $noName_1) -> {
            EditText editText = memberName;
            String names = editText.getText().toString();
            editText = memberDesc;
            String emails = editText.getText().toString();
            if(names.equals("") || emails.equals("")){
                Toast.makeText(getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
            }
            else {

                MembersFragment.access$getMemberList$p(MembersFragment.this).add(new Member(names,emails));
                MembersFragment.access$getMemberAdapter$p(MembersFragment.this).notifyDataSetChanged();
                Toast.makeText(getContext(), "Added Member Successfully", Toast.LENGTH_SHORT).show();
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

    public static ArrayList<Member> access$getMemberList$p(MembersFragment $this) {
        return $this.members;
    }

    public static MemberAdapter access$getMemberAdapter$p(MembersFragment $this) {
        return $this.adapter;
    }
}
