package com.example.navigationbar.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navigationbar.MyRecyclerViewAdapter;
import com.example.navigationbar.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {

    View view;
    /*RecyclerView recyclerView;
    List<ModalClass> mList;
    CustomAdapter customAdapter;*/
    MyRecyclerViewAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
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
        view = inflater.inflate(R.layout.fragment_teams, container, false);
        // data to populate the RecyclerView with
        String[] data = {"1", "2", "3", "4", "5", "6","7","8"};

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewId);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
      /*  recyclerView = v.findViewById(R.id.recyclerViewId);
        customAdapter = new CustomAdapter(mList,getContext());
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* mList = new ArrayList<>();
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));
        mList.add(new ModalClass(R.drawable.html,"Title"));*/


    }

}