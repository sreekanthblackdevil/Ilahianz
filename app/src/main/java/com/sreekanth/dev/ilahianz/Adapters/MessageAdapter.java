package com.sreekanth.dev.ilahianz.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sreekanth.dev.ilahianz.MessageActivity;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.model.Chat;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private FirebaseUser fuser;

    public MessageAdapter(MessageActivity context, List<Chat> mChat) {
        this.mContext = context;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat = mChat.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        viewHolder.times.setText(chat.getTime());

        if (i == mChat.size() - 1) {
            if (chat.isIsseen()) {
                viewHolder.seen_status.setImageResource(R.drawable.ic_action_double_check_blue);
            } else {
                viewHolder.seen_status.setImageResource(R.drawable.ic_action_check_blue);
            }
        } else {
            viewHolder.seen_status.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (TextUtils.equals(mChat.get(position).getSender(), fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView show_message;
        TextView times;
        ImageView seen_status;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            times = itemView.findViewById(R.id.time_view);
            seen_status = itemView.findViewById(R.id.seen_indicator);
        }
    }
}
