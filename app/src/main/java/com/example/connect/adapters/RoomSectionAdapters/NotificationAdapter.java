package com.example.connect.adapters.RoomSectionAdapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.InvitationNotification;
import com.example.connect.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<InvitationNotification> notifications = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvitationNotification notification = notifications.get(position);
        holder.userText.setText(notification.getSenderName() + " invited you to");
        holder.teamText.setText(notification.getRoomName());
        String url = "https://picsum.photos/200";
        Picasso.get().load(url).into(holder.getNotificationImageView());
        holder.getAcceptButton().setOnClickListener(v->{
            JSONObject json = new JSONObject();
            try {
                json.put("uid", WebSocketService.getWebSocketService().getAuthUser().getUid());
                json.put("rid", notification.getRid());
                json.put("senderId",notification.getSenderId());
                WebSocketService.getWebSocketService().fireDataToServer(WebSocketService.ACCEPT_INVITATION,json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        holder.getRejectButton().setOnClickListener(v->{
            JSONObject json = new JSONObject();
            try {
                json.put("uid", WebSocketService.getWebSocketService().getAuthUser().getUid());
                json.put("rid", notification.getRid());
                json.put("senderId",notification.getSenderId());
                WebSocketService.getWebSocketService().fireDataToServer(WebSocketService.REJECT_INVITATION,json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        if (!notification.response.equals("none") && !notification.response.equals("null")){

            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] acceptColors = new int[] {
                    Color.GREEN,
                    Color.DKGRAY,
                    Color.DKGRAY,
                    Color.DKGRAY
            };

            int[] rejectColors = new int[] {
                    Color.RED,
                    Color.DKGRAY,
                    Color.DKGRAY,
                    Color.DKGRAY
            };


            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
            holder.responseButton.setText(notification.response);
            holder.responseButton.setClickable(false);
            holder.responseButton.setVisibility(View.VISIBLE);
            if (notification.response.equals("accepted")){
                holder.responseButton.setBackgroundTintList(new ColorStateList(states, acceptColors));
            }else{
                holder.responseButton.setBackgroundTintList(new ColorStateList(states, rejectColors));
            }
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public NotificationAdapter(ArrayList<InvitationNotification> notifications){
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button acceptButton, rejectButton, responseButton;
        ImageView notificationImageView;
        TextView teamText,userText;

        public Button getAcceptButton() {
            return acceptButton;
        }

        public void setAcceptButton(Button acceptButton) {
            this.acceptButton = acceptButton;
        }

        public Button getRejectButton() {
            return rejectButton;
        }

        public void setRejectButton(Button rejectButton) {
            this.rejectButton = rejectButton;
        }

        public Button getResponseButton() {
            return responseButton;
        }

        public void setResponseButton(Button responseButton) {
            this.responseButton = responseButton;
        }

        public ImageView getNotificationImageView() {
            return notificationImageView;
        }

        public void setNotificationImageView(ImageView notificationImageView) {
            this.notificationImageView = notificationImageView;
        }

        public TextView getTeamText() {
            return teamText;
        }

        public void setTeamText(TextView teamText) {
            this.teamText = teamText;
        }

        public TextView getUserText() {
            return userText;
        }

        public void setUserText(TextView userText) {
            this.userText = userText;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.notification_invite_accept);
            rejectButton = itemView.findViewById(R.id.notification_invite_reject);
            notificationImageView = itemView.findViewById(R.id.notification_user_image);
            userText = itemView.findViewById(R.id.notification_inviting_user);
            teamText = itemView.findViewById(R.id.notification_invited_teams);
            responseButton = itemView.findViewById(R.id.notification_response_button);
            responseButton.setVisibility(View.GONE);
        }
    }
}
