package com.sreekanth.dev.ilahianz.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.ChatActivity;
import com.sreekanth.dev.ilahianz.MessageActivity;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.UserProfile;
import com.sreekanth.dev.ilahianz.model.User;
import com.sreekanth.dev.ilahianz.utils.FileUtil;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    User myInfo = new User();

    public UserAdapter(Context context, List<User> list) {
        this.mContext = context;
        this.mUsers = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final User user = mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        viewHolder.className.setText(user.getClassName());
        ViewSupport.setAboutWithPrivacy(user, viewHolder.about, myInfo);
        ViewSupport.setThumbProfileWithPrivacy(user, myInfo, viewHolder.profile_image);

        if (TextUtils.equals(user.getStatus(), "online")) {
            viewHolder.status.setVisibility(View.VISIBLE);
            viewHolder.status_view.setText(user.getStatus());
            viewHolder.status_view.setTextColor(Color.parseColor("#499C54"));
        } else {
            viewHolder.status_view.setText(user.getStatus());
            viewHolder.status_view.setTextColor(Color.parseColor("#C75450"));
            viewHolder.status.setVisibility(View.INVISIBLE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
        viewHolder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileView(mContext, user, myInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, status_view, className, about;
        public CircleImageView profile_image;
        View status;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    myInfo = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            className = itemView.findViewById(R.id.class_users);
            about = itemView.findViewById(R.id.about_text);
            status = itemView.findViewById(R.id.status);
            status_view = itemView.findViewById(R.id.status_view);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_Image);
        }
    }

    private void UserProfileView(Context context, final User user, User myInfo) {
        Dialog popupProfile = new Dialog(context);
        popupProfile.setContentView(R.layout.popup_user_profile);
        Objects.requireNonNull(popupProfile.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView username = popupProfile.findViewById(R.id.username);
        ImageButton save = popupProfile.findViewById(R.id.save);
        ImageButton message = popupProfile.findViewById(R.id.message);
        ImageButton call = popupProfile.findViewById(R.id.call_btn);
        ImageButton info = popupProfile.findViewById(R.id.info);
        final ImageView profile_image = popupProfile.findViewById(R.id.profile_Image);
        username.setText(user.getUsername());
        ViewSupport.setProfileWithPrivacy(user, myInfo, profile_image);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.saveImage(mContext, profile_image, user);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity chatActivity = new ChatActivity();
                chatActivity.PhoneNumber = user.getPhoneNumber();
                chatActivity.makePhoneCall(mContext);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserProfile.class).putExtra("UId", user.getId()));
            }
        });
        popupProfile.show();
    }
}
