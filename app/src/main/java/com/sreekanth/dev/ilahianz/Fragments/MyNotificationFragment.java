package com.sreekanth.dev.ilahianz.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Adapters.NotificationAdapter;
import com.sreekanth.dev.ilahianz.Adapters.SeenListAdapter;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.model.Notification;
import com.sreekanth.dev.ilahianz.model.SeenList;

import java.util.ArrayList;
import java.util.List;

public class MyNotificationFragment extends Fragment {
    NotificationAdapter adapter;
    SeenListAdapter sAdapter;
    RecyclerView recyclerView, recyclerView2;
    List<Notification> mNotification;
    List<SeenList> mSeenLists;
    Context mContext;
    FirebaseUser fuser;
    DatabaseReference reference;
    RelativeLayout empty_notify, progress, connection;
    private List<Notification> notifications;
    private List<SeenList> seenLists;
    int count = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_notification, container, false);
        mContext = getContext();
        recyclerView = view.findViewById(R.id.recycle_view);
        empty_notify = view.findViewById(R.id.empty);
        progress = view.findViewById(R.id.progress);
        connection = view.findViewById(R.id.connection);

        connection.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        if (Supports.Connected(mContext)) {
            connection.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        notifications = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Notice");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifications.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    assert notification != null;
                    if (fuser.getUid().equals(notification.getFrom()))
                        notifications.add(notification);
                }
                notifications();
                progress.setVisibility(View.GONE);
                connection.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


    private void notifications() {
        mNotification = new ArrayList<>();
        mNotification.clear();
        for (int i = notifications.size() - 1; i >= 0; i--) {
            mNotification.add(notifications.get(i));
        }
        adapter = new NotificationAdapter(mContext, mNotification);
        if (adapter.getItemCount() == 0) {
            empty_notify.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            connection.setVisibility(View.GONE);
        } else {
            empty_notify.setVisibility(View.GONE);
            connection.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
            progress.setVisibility(View.GONE);
        }
    }

    private void viewList() {
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.seen_list);
        seenLists = new ArrayList<>();
        recyclerView2 = dialog.findViewById(R.id.seen_list);
        final RelativeLayout no_view = dialog.findViewById(R.id.no_views);
        assert no_view != null;
        no_view.setVisibility(View.VISIBLE);
        assert recyclerView2 != null;
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seenLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SeenList seenList = snapshot.getValue(SeenList.class);
                    assert seenList != null;
                    if (!TextUtils.equals(fuser.getUid(), seenList.getId())) {
                        seenLists.add(seenList);
                        count++;
                    }
                }
                if (seenLists.size() == 0)
                    no_view.setVisibility(View.VISIBLE);
                else
                    no_view.setVisibility(View.GONE);
                setSeenLists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.show();
    }

    private void setSeenLists() {
        mSeenLists = new ArrayList<>();
        mSeenLists.clear();
        for (int i = seenLists.size() - 1; i >= 0; i--) {
            mSeenLists.add(seenLists.get(i));
        }
        sAdapter = new SeenListAdapter(mContext, mSeenLists, count);
        recyclerView2.setAdapter(sAdapter);
    }
}
