package com.sreekanth.dev.ilahianz.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sreekanth.dev.ilahianz.Listeners.OnTapListener;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.Supports.ContactViewHolder;
import com.sreekanth.dev.ilahianz.model.Contact;

import java.util.Collections;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private Activity activity;
    List<Contact> item = Collections.emptyList();
    private OnTapListener onTapListener;

    public ContactAdapter(Activity activity, List<Contact> item) {
        this.activity = activity;
        this.item = item;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, final int i) {

        contactViewHolder.contact_name.setText(item.get(i).getName());
        contactViewHolder.number.setText(item.get(i).getNumber());
        contactViewHolder.position.setText(item.get(i).getPosition());

        contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTapListener != null) {
                    onTapListener.onTapViw(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void setOnTapListener(OnTapListener onTapListener) {
        this.onTapListener = onTapListener;

    }
}
