

package com.sreekanth.dev.ilahianz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.ShowNotificationActivity;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.Notification;
import com.sreekanth.dev.ilahianz.model.User;
import com.sreekanth.dev.ilahianz.utils.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context context, List<Notification> list) {
        this.mContext = context;
        this.mNotification = list;
    }

    @NonNull

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notificatio_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder viewHolder, int i) {
        final Notification notification = mNotification.get(i);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(notification.getUId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                ViewSupport.setThumbProfileImage(user, viewHolder.profile_image);
                viewHolder.username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.heading.setText(notification.getHeading());
        viewHolder.type.setText(notification.getType());
        Date date;
        date = new Date(Date.parse(notification.getTime()));
        viewHolder.date.setText(new TimeAgo().getTimeAgo(date));
        viewHolder.time.setText(new TimeAgo().getTimeAgo(date));
//        if (notification.getColor() != null)
//            viewHolder.notification_bg.setCardBackgroundColor(Integer.parseInt(notification.getColor()));
        viewHolder.message.setText(notification.getContent());

        viewHolder.notification_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ShowNotificationActivity.class)
                        .putExtra("message", notification.getContent())
                        .putExtra("Uid", notification.getUId())
                        .putExtra("heading", notification.getHeading())
                        .putExtra("color", notification.getColor()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading, time, message, type, date, username;
        CardView notification_bg;
        CircleImageView profile_image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_Image);
            heading = itemView.findViewById(R.id.heading);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.message);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.date);
            notification_bg = itemView.findViewById(R.id.notification_bg);
        }
    }


}
