package com.example.connect.fragments.BottomNavigationFragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.connect.AuthenticationActivities.Events.GetDialogsEvent;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.R;
import com.example.connect.chat.ModelOFDialog;
import com.example.connect.chat.commons.ImageLoader;
import com.example.connect.chat.dialogs.DialogsList;
import com.example.connect.chat.dialogs.DialogsListAdapter;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatFragment extends Fragment implements DialogsListAdapter.OnDialogClickListener<ModelOFDialog>, SearchView.OnQueryTextListener {


    ArrayList<ModelOFDialog> dialogsList = new ArrayList<>();
    DialogsList dialogsListview;
    DialogsListAdapter dialogsListAdapter;
    ImageLoader imageLoadergg;
    ImageView imageView;
    String url;
    WebSocketService service = WebSocketService.getWebSocketService();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotConversations(GetDialogsEvent e){
        if (e.status) dialogsListAdapter.setItems(e.dialogs);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View chatView = inflater.inflate(R.layout.fragment_chat, container, false);
        dialogsListview = chatView.findViewById(R.id.dialogsList);
        imageView = chatView.findViewById(R.id.dialogAvatar);
        url = "https://picsum.photos/200/300";
        imageLoadergg = ((imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        JSONObject json = new JSONObject();
        try {
            json.put("uid",service.getAuthUser().getUid());
            service.fireDataToServer(WebSocketService.GET_CONVERSATIONS,json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapterActivate();

        return chatView;
    }
    private void adapterActivate() {
        dialogsListAdapter = new DialogsListAdapter<>(
                R.layout.item_view_dialog_gg,
                com.example.connect.chat.ViewHolderDialogActivity.class,
                imageLoadergg
        );

        dialogsListAdapter.setOnDialogClickListener(this);
        dialogsListAdapter.setItems(dialogsList);
        Log.d("Msg",dialogsListview.toString());
        dialogsListview.setAdapter(dialogsListAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                dialogsListAdapter.setFilter(dialogsList);
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
        final ArrayList<ModelOFDialog> filteredModelList = filter(dialogsList, newText);
        dialogsListAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<ModelOFDialog> filter(ArrayList<ModelOFDialog> models, String query) {
        query = query.toLowerCase();

        final ArrayList<ModelOFDialog> filteredModelList = new ArrayList<>();
        for (ModelOFDialog model : models) {
            final String text = model.getDialogName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    @Override
    public void onDialogClick(ModelOFDialog dialog) {
        com.example.connect.chat.ActivityMainMessage.start(getContext());
    }
}