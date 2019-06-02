package com.sreekanth.dev.ilahianz.Supports;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.model.User;

public class ViewSupport {
    private static User user = new User();


    public static void setProfileImage(User user, final ImageView imageView) {
        assert user != null;
        if (TextUtils.equals(user.getImageURL(), "default")) {
            if (TextUtils.equals(user.getCategory(), "Student")) {
                switch (user.getGender()) {
                    case "Male":
                        imageView.setImageResource(R.mipmap.men1);
                        break;
                    case "Female":
                        imageView.setImageResource(R.mipmap.wumen4);
                        break;
                    case "Other":
                        imageView.setImageResource(R.mipmap.bysex);
                    default:
                        break;
                }
            } else {
                switch (user.getGender()) {
                    case "Male":
                        imageView.setImageResource(R.mipmap.sir);
                        break;
                    case "Female":
                        imageView.setImageResource(R.mipmap.wumen2);
                        break;
                    case "Other":
                        imageView.setImageResource(R.mipmap.bysex);
                    default:
                        break;
                }
            }
        } else {
            Picasso.get().load(user.getImageURL()).into(imageView);
        }
    }

    public static void setThumbProfileImage(User user, final ImageView imageView) {
        assert user != null;
        if (TextUtils.equals(user.getThumbnailURL(), "default")) {
            if (TextUtils.equals(user.getCategory(), "Student")) {
                switch (user.getGender()) {
                    case "Male":
                        imageView.setImageResource(R.mipmap.men1);
                        break;
                    case "Female":
                        imageView.setImageResource(R.mipmap.wumen4);
                        break;
                    case "Other":
                        imageView.setImageResource(R.mipmap.bysex);
                    default:
                        break;
                }
            } else {
                switch (user.getGender()) {
                    case "Male":
                        imageView.setImageResource(R.mipmap.sir);
                        break;
                    case "Female":
                        imageView.setImageResource(R.mipmap.wumen2);
                        break;
                    case "Other":
                        imageView.setImageResource(R.mipmap.bysex);
                    default:
                        break;
                }
            }
        } else {
            Picasso.get().load(user.getThumbnailURL()).into(imageView);
        }
    }

    public static void setProfileWithPrivacy(User user, User myInfo, ImageView profile_image) {
        if (TextUtils.equals(user.getImageURL(), "default")) {
            setProfileImage(user, profile_image);
        } else {
            if (!TextUtils.equals(user.getProfilePrivacy(), "Everyone")) {
                if (TextUtils.equals(myInfo.getCategory(), "Student") &&
                        TextUtils.equals(user.getProfilePrivacy(), "Students Only")) {
                    Picasso.get().load(user.getImageURL()).into(profile_image);
                } else if (TextUtils.equals(myInfo.getCategory(), "Teacher") &&
                        TextUtils.equals(user.getProfilePrivacy(), "Teachers Only")) {
                    Picasso.get().load(user.getImageURL()).into(profile_image);
                } else {
                    if (user.getCategory().equals("Student")) {
                        switch (user.getGender()) {
                            case "Male":
                                profile_image.setImageResource(R.mipmap.men1);
                                break;
                            case "Female":
                                profile_image.setImageResource(R.mipmap.wumen4);
                                break;
                            case "Other":
                                profile_image.setImageResource(R.mipmap.bysex);
                            default:
                                break;
                        }
                    } else {
                        switch (user.getGender()) {
                            case "Male":
                                profile_image.setImageResource(R.mipmap.sir);
                                break;
                            case "Female":
                                profile_image.setImageResource(R.mipmap.wumen2);
                                break;
                            case "Other":
                                profile_image.setImageResource(R.mipmap.bysex);
                            default:
                                break;
                        }
                    }
                }
            } else {
                Picasso.get().load(user.getImageURL()).into(profile_image);
            }
        }
    }

    public static void setProfileWithPrivacy(User user, User myInfo, ImageView profile_image, int width, int height) {
        if (TextUtils.equals(user.getImageURL(), "default")) {
            setProfileImage(user, profile_image);
        } else {
            if (!TextUtils.equals(user.getProfilePrivacy(), "Everyone")) {
                if (TextUtils.equals(myInfo.getCategory(), "Student") &&
                        TextUtils.equals(user.getProfilePrivacy(), "Students Only")) {
                    Picasso.get().load(user.getImageURL()).into(profile_image);
                } else if (TextUtils.equals(myInfo.getCategory(), "Teacher") &&
                        TextUtils.equals(user.getProfilePrivacy(), "Teachers Only")) {
                    Picasso.get().load(user.getImageURL()).into(profile_image);
                } else {
                    if (user.getCategory().equals("Student")) {
                        switch (user.getGender()) {
                            case "Male":
                                profile_image.setImageResource(R.mipmap.men1);
                                break;
                            case "Female":
                                profile_image.setImageResource(R.mipmap.wumen4);
                                break;
                            case "Other":
                                profile_image.setImageResource(R.mipmap.bysex);
                            default:
                                break;
                        }
                    } else {
                        switch (user.getGender()) {
                            case "Male":
                                profile_image.setImageResource(R.mipmap.sir);
                                break;
                            case "Female":
                                profile_image.setImageResource(R.mipmap.wumen2);
                                break;
                            case "Other":
                                profile_image.setImageResource(R.mipmap.bysex);
                            default:
                                break;
                        }
                    }
                }
            } else {
                Picasso.get().load(user.getImageURL()).resize(width, height).into(profile_image);
            }
        }
    }

    public static void setThumbProfileWithPrivacy(User user, User myInfo, ImageView profile_image) {
        if (TextUtils.equals(user.getThumbnailURL(), "default")) {
            setProfileImage(user, profile_image);
        } else {
            if (!TextUtils.equals(user.getProfilePrivacy(), "Everyone")) {
                if (TextUtils.equals(myInfo.getCategory(), "Student") &&
                        TextUtils.equals(user.getProfilePrivacy(), "Students Only")) {
                    Picasso.get().load(user.getThumbnailURL()).into(profile_image);
                } else if (TextUtils.equals(myInfo.getCategory(), "Teacher") &&
                        TextUtils.equals(user.getProfilePrivacy(), "Teachers Only")) {
                    Picasso.get().load(user.getThumbnailURL()).into(profile_image);
                } else {
                    if (user.getCategory().equals("Student")) {
                        switch (user.getGender()) {
                            case "Male":
                                profile_image.setImageResource(R.mipmap.men1);
                                break;
                            case "Female":
                                profile_image.setImageResource(R.mipmap.wumen4);
                                break;
                            case "Other":
                                profile_image.setImageResource(R.mipmap.bysex);
                            default:
                                break;
                        }
                    } else {
                        switch (user.getGender()) {
                            case "Male":
                                profile_image.setImageResource(R.mipmap.sir);
                                break;
                            case "Female":
                                profile_image.setImageResource(R.mipmap.wumen2);
                                break;
                            case "Other":
                                profile_image.setImageResource(R.mipmap.bysex);
                            default:
                                break;
                        }
                    }
                }
            } else {
                Picasso.get().load(user.getThumbnailURL()).into(profile_image);
            }
        }
    }

    public static void setAboutWithPrivacy(User user, TextView about, User myInfo) {

        if (!TextUtils.equals(user.getAboutPrivacy(), "Everyone")) {
            if (TextUtils.equals(myInfo.getCategory(), "Student") &&
                    TextUtils.equals(user.getAboutPrivacy(), "Students Only")) {
                about.setVisibility(View.VISIBLE);
                about.setText(user.getDescription());
            } else if (TextUtils.equals(myInfo.getCategory(), "Student") &&
                    TextUtils.equals(user.getAboutPrivacy(), "Teachers Only")) {
                about.setVisibility(View.VISIBLE);
                about.setText(user.getDescription());
            } else about.setVisibility(View.INVISIBLE);
        }
    }

    public static User getUserInfo() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert fuser != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return user;
    }
}

