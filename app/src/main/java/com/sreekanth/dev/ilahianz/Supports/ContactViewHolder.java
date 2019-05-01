package com.sreekanth.dev.ilahianz.Supports;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sreekanth.dev.ilahianz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    public TextView contact_name, position, number;
    public CircleImageView user_pic;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        contact_name = itemView.findViewById(R.id.contact_name);
        position = itemView.findViewById(R.id.position);
        number = itemView.findViewById(R.id.phone_number);
        user_pic = itemView.findViewById(R.id.logo);
    }
}
