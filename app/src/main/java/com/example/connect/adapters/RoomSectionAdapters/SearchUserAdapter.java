package com.example.connect.adapters.RoomSectionAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.connect.AuthenticationActivities.Helper;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.RoomMember;
import com.example.connect.R;
import com.example.connect.fragments.RoomActivityFragments.MembersFragment;
import com.example.connect.model.SearchUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.IntStream;


public final class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder> {
    private ArrayList<SearchUser> searchUsers = new ArrayList<>();
    private MembersFragment parent;
    WebSocketService wss = WebSocketService.getWebSocketService();

    @NonNull
    @Override
    public SearchUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.search_user_item, parent, false);
        return new SearchUserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserHolder holder, int position) {
        SearchUser user = this.searchUsers.get(position);
        holder.getEmailText().setText(user.getEmail());
        holder.getNameText().setText(user.getName());
        String url = "https://picsum.photos/200";
        Picasso.get().load(url).into(holder.getImageView());
        if (this.parent.members.contains(user.getSid())){
            holder.getImageView().setVisibility(View.GONE);
            user.setIsAlreadyMember(true);
            user.setInvited(false);
            return;
        }
        if (user.isInvited()){
            holder.inviteButton.setImageResource(R.drawable.ic_person_invited);
            holder.progressBar.setVisibility(View.GONE);
            holder.getInviteButton().setVisibility(View.VISIBLE);
        }
        holder.getInviteButton().setOnClickListener(v -> {
            JSONObject json = new JSONObject();
            try {
                json.put("sid",user.getSid());
                json.put("uid",wss.getAuthUser().getUid());
                json.put("rid",wss.getRoom().getRid());
                holder.getInviteButton().setVisibility(View.GONE);
                holder.getProgressBar().setVisibility(View.VISIBLE);
                wss.fireDataToServer(WebSocketService.ADD_MEMBER,json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchUsers.size();
    }

    public void updateList(ArrayList<SearchUser> searchUsers){
        this.searchUsers = searchUsers;
        this.notifyDataSetChanged();
        return;
    }

    public void updateListItem(String sid){
        IntStream.range(0,searchUsers.size()).forEach(index -> {
            if (searchUsers.get(index).getSid().equals(sid)){
                searchUsers.get(index).setInvited(true);
                notifyItemChanged(index);
            }
        });
    }

    public final ArrayList<SearchUser> getSearchUsers() {
        return this.searchUsers;
    }
//
    public SearchUserAdapter(ArrayList<SearchUser> searchUsers, MembersFragment fragment) {
        super();
        this.parent = fragment;
        this.searchUsers = searchUsers;
    }

    public class SearchUserHolder extends ViewHolder{

        ImageView inviteButton;
        TextView emailText,nameText;
        ImageView imageView;
        ProgressBar progressBar;

        public SearchUserHolder(View itemView) {
            super(itemView);
            inviteButton = itemView.findViewById(R.id.search_user_invite);
            emailText = itemView.findViewById(R.id.search_user_email);
            nameText = itemView.findViewById(R.id.search_user_name);
            imageView = itemView.findViewById(R.id.search_user_image);
            progressBar = itemView.findViewById(R.id.search_user_progress);
        }

        public ImageView getInviteButton() {
            return inviteButton;
        }

        public void setInviteButton(ImageView inviteButton) {
            this.inviteButton = inviteButton;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public void setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        public TextView getEmailText() {
            return emailText;
        }

        public void setEmailText(TextView emailText) {
            this.emailText = emailText;
        }

        public TextView getNameText() {
            return nameText;
        }

        public void setNameText(TextView nameText) {
            this.nameText = nameText;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}

