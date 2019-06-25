package com.sreekanth.dev.ilahianz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.UserProfileActivity;
import com.sreekanth.dev.ilahianz.model.SeenList;
import com.sreekanth.dev.ilahianz.model.User;
import com.sreekanth.dev.ilahianz.utils.TimeAgo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sreekanth.dev.ilahianz.model.Literals.SP_CATEGORY;

public class SeenListAdapter extends RecyclerView.Adapter<SeenListAdapter.ViewHolder> {

    private Context mContext;
    private List<SeenList> mList;
    private int count;

    public SeenListAdapter(Context mContext, List<SeenList> mList, int count) {
        this.mContext = mContext;
        this.mList = mList;
        this.count = count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.seen_item, viewGroup);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final SeenList seenList = mList.get(i);
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(seenList.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                viewHolder.username.setText(user.getUsername());
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
                TimeAgo timeAgo = new TimeAgo().locale(mContext).with(format);
                Date date = null;
                try {
                    date = format.parse(seenList.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                viewHolder.time.setText(timeAgo.getTimeAgo(date));
                viewHolder.seen_count.setText(String.valueOf(count));
                ViewSupport.setThumbProfileWithPrivacy(user, getUserInfo(SP_CATEGORY), viewHolder.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserProfileActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, username, seen_count;
        CircleImageView profileImage;
        ImageButton info;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profile_Image);
            info = itemView.findViewById(R.id.info);
            seen_count = itemView.findViewById(R.id.view_count);
        }
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return preferences.getString(key, "none");
    }
}
