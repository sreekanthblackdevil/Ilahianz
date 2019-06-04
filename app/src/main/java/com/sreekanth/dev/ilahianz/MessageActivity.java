package com.sreekanth.dev.ilahianz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Adapters.MessageAdapter;
import com.sreekanth.dev.ilahianz.Fragments.APIService;
import com.sreekanth.dev.ilahianz.Notifications.Client;
import com.sreekanth.dev.ilahianz.Notifications.Data;
import com.sreekanth.dev.ilahianz.Notifications.MyResponse;
import com.sreekanth.dev.ilahianz.Notifications.Sender;
import com.sreekanth.dev.ilahianz.Notifications.Token;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.Chat;
import com.sreekanth.dev.ilahianz.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference, reference2;
    ImageButton send_btn;
    EditText send_text;
    Intent intent;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    Calendar calendar;
    TextView online;
    ValueEventListener seenListener;
    public static final int ON_PAUSE = 0;
    public static final int ON_RESUME = 1;
    APIService apiService;

    boolean notify = false;
    User myInfo = new User();
    public static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = Client.getClient("http://fcm.googleapis.com").create(APIService.class);

        profile_image = findViewById(R.id.profile_Image);
        username = findViewById(R.id.username);
        intent = getIntent();
        userid = intent.getStringExtra("userid");

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        send_btn = findViewById(R.id.send_btn);
        send_text = findViewById(R.id.send_txt);
        online = findViewById(R.id.online);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference2 = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myInfo = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                ViewSupport.setThumbProfileWithPrivacy(user, myInfo, profile_image);
                if ("online".equals(user.getStatus())) {
                    online.setVisibility(View.VISIBLE);
                    online.setText("online");
                } else {
                    online.setVisibility(View.GONE);
                }
                //readMessages(fuser.getUid(), userid);
                readMessages readMessages = new readMessages(fuser.getUid());
                new Thread(readMessages).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, UserProfile.class).putExtra("UId", userid));
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = send_text.getText().toString();
                if (!msg.isEmpty()) {
                    calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
                    String time = format.format(calendar.getTime());
                    sendMessage(fuser.getUid(), userid, msg, time);
                }
                send_text.setText("");
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seenMessage(userid);

    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender, final String receiver, String message, String time) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("time", time);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                            .child(fuser.getUid())
                            .child(userid);
                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                chatRef.child("id").setValue(userid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    final DatabaseReference chatReff = FirebaseDatabase.getInstance().getReference("Chatlist")
                            .child(userid).child(fuser.getUid());
                    chatReff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                chatReff.child("id").setValue(fuser.getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (notify) {
                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String receiver, final String username, final String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = reference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.owl, username + " : " + message, "New Message",
                            userid);

                    assert token != null;
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).
                            enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        assert response.body() != null;
                                        if (response.body().success == 1) {
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(final String myid, final String userid) {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class readMessages implements Runnable {
        String myid;

        readMessages(String myid) {
            this.myid = myid;
        }

        @Override
        public void run() {
            mChat = new ArrayList<>();

            reference = FirebaseDatabase.getInstance().getReference("Chats");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChat.clear();

                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Chat chat = snapshot.getValue(Chat.class);
                                assert chat != null;
                                if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                        chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                                    mChat.add(chat);
                                }
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                                recyclerView.setAdapter(messageAdapter);
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentUser", userid);
        editor.apply();
    }

    private void status(String status, String last_seen) {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        hashMap.put("lastSeen", last_seen);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        PR resume = new PR(ON_RESUME);
        new Thread(resume).start();
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PR pause = new PR(ON_PAUSE);
        new Thread(pause).start();
        currentUser("none");
        reference.removeEventListener(seenListener);
    }

    class PR implements Runnable {
        int type;

        PR(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            if (type == ON_PAUSE) {
                onPause();
            } else {
                onResume();
            }
        }

        private void onResume() {
            status("online", "active");
        }

        private void onPause() {
            calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
            String time = format.format(calendar.getTime());
            SimpleDateFormat format1 = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
            String date = format1.format(calendar.getTime());
            status(time, date);
        }
    }
}
