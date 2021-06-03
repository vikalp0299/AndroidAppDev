package com.example.connect.fragments.BottomNavigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.connect.AuthenticationActivities.GettingStartedActivity;
import com.example.connect.AuthenticationActivities.WebSocketService;
import com.example.connect.Entities.DaoSession;
import com.example.connect.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    Button signOut;
    EditText name,email;
    ImageView profileImage;
    WebSocketService wss;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        signOut = view.findViewById(R.id.sign_out);
        email = view.findViewById(R.id.profile_email);
        name = view.findViewById(R.id.profile_name);
        profileImage = view.findViewById(R.id.profile_user_image);
        String url = "https://picsum.photos/200";
        Picasso.get().load(url).into(profileImage);
        wss = (WebSocketService)getActivity().getApplication();
        name.setText(wss.getAuthUser().getFirstName());
        email.setText(wss.getAuthUser().getEmail());
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DaoSession session = wss.getDaoSession();
               session.getRoomDao().deleteAll();
               session.getRoomMemberDao().deleteAll();
               session.getAuthUserDao().deleteAll();
               Intent intent = new Intent(getContext(), GettingStartedActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
               getActivity().startActivity(intent);
            }
        });
        return view;
    }
}