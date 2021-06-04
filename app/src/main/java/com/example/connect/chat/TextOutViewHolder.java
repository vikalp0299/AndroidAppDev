package com.example.connect.chat;

import android.view.View;

import com.example.connect.chat.messages.MessageHolders;


public class TextOutViewHolder extends MessageHolders.OutcomingTextMessageViewHolder<ModelOFMessage> {
    public TextOutViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(com.example.connect.chat.ModelOFMessage message) {
        super.onBind(message);

        time.setText(message.getStatus()+"  "+time.getText());
    }
}
