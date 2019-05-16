

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

import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.ShowNotificationActivity;
import com.sreekanth.dev.ilahianz.model.Notification;

import java.util.List;

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

        viewHolder.heading.setText(notification.getHeading());
        viewHolder.type.setText(notification.getType());
        viewHolder.date.setText(notification.getDate());
        viewHolder.time.setText(notification.getTime());
        if (notification.getColor() != null)
            viewHolder.notification_bg.setCardBackgroundColor(Integer.parseInt(notification.getColor()));
        if (notification.getContent().length() <= 30)
            viewHolder.message.setText(notification.getContent());
        else {
            String message = notification.getContent().substring(0, 30) + "...";
            viewHolder.message.setText(message);
        }

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

        TextView heading, time, message, type, date;
        CardView notification_bg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.message);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.date);
            notification_bg = itemView.findViewById(R.id.notification_bg);
        }
    }


}
