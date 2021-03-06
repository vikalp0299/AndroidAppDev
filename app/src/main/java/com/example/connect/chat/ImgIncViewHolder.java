package com.example.connect.chat;

import android.view.View;


import com.example.connect.R;
import com.example.connect.chat.messages.MessageHolders;

public class ImgIncViewHolder  extends MessageHolders.IncomingImageMessageViewHolder<ModelOFMessage> {
    public View indicator;
    public ImgIncViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        indicator = itemView.findViewById(R.id.onlineIndicator);
    }

    @Override
    public void onBind(com.example.connect.chat.ModelOFMessage modelOFMessage) {
        super.onBind(modelOFMessage);
        boolean online = modelOFMessage.getUser().isOnline();

        if (online){
            indicator.setBackgroundResource(R.drawable.bubble_shape_onilne_gg);

        }else{
            indicator.setBackgroundResource(R.drawable.bubble_shape_offilne_gg);
        }
    }
}
