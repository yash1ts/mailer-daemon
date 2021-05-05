package com.mailerdaemon.app.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.campusMap.MapsActivity;
import com.mailerdaemon.app.impContacts.ImpContactActivity;
import com.mailerdaemon.app.placement.PlacementActivity;
import com.mailerdaemon.app.utils.ChromeTab;
import com.mailerdaemon.app.utils.ContactFunction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements ContactFunction {
    @BindView(R.id.rv_contacts)
    RecyclerView RvContacts;
    ContactAdapter adapter;
    List<Contact> contactList=new ArrayList<>();
    @BindView(R.id.bt_contact_all)
    MaterialButton bt_see_all;
    @BindView(R.id.map_card)
    MaterialCardView bt_map;
    @BindView(R.id.exp_card)
    MaterialCardView bt_experience;
    @BindView(R.id.textView4)
    TextView placement;
    @BindView(R.id.parent_portal)
    LinearLayout bt_parent_portal;
    @BindView(R.id.calendar)
    LinearLayout bt_calander;
    @BindView(R.id.mess_menu)
    LinearLayout bt_mess;
    @BindView(R.id.holidays)
    LinearLayout bt_holidays;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        ButterKnife.bind(this,view);
        adapter=new ContactAdapter(this);
        RvContacts.setAdapter(adapter);
        contactList= new Gson().fromJson(loadJSONFromAsset("contact"), ContactModel.class).getContact();
        if(contactList!=null)
            adapter.setData(contactList);
        bt_see_all.setOnClickListener(v-> startActivity(new Intent(getActivity(),ImpContactActivity.class)));
        final long[] mLastClickTime = {0};
        bt_map.setOnClickListener(v->{
            if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
                return;
            }
            mLastClickTime[0] = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), MapsActivity.class));
        });

        placement.setOnClickListener(v -> OpenPlacement());
        bt_experience.setOnClickListener(v->{
//            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:Tp9iD7YAcKo"));
//            Intent webIntent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("https://www.youtube.com/watch?v=Tp9iD7YAcKo"));
//            try {
//                Objects.requireNonNull(getContext()).startActivity(appIntent);
//            } catch (ActivityNotFoundException ex) {
//                Objects.requireNonNull(getContext()).startActivity(webIntent);
//            }
            OpenPlacement();
        });
        bt_parent_portal.setOnClickListener(v->{
            ChromeTab tab=new ChromeTab(getContext());
            tab.openTab(ConstantsKt.Parent_Portal_Link);
        });
        bt_calander.setOnClickListener(v->{
            ChromeTab tab=new ChromeTab(getContext());
            tab.openTab(ConstantsKt.CALENDER_LINK);
        });
        bt_holidays.setOnClickListener(v->{
            ChromeTab tab=new ChromeTab(getContext());
            tab.openTab(ConstantsKt.HOLIDAY_LINK);
        });
        bt_mess.setOnClickListener(v->{
            ChromeTab tab=new ChromeTab(getContext());
            tab.openTab(ConstantsKt.MESS_LINK);

        });
        return view;
    }

    @Override
    public void makeCall(String num) {
        if(!num.trim().equals("0")){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.trim()));
            startActivity(intent);}
        else{
            Toast.makeText(getContext(),"Sorry number not available",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendMail(String s) {
        if(!s.trim().isEmpty()) {
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode(s.trim()) +
                    "?subject=" + Uri.encode("Subject") +
                    "&body=" + Uri.encode("the body of the message");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
        }
        else{
            Toast.makeText(getContext(),"Sorry email not available",Toast.LENGTH_LONG).show();
        }
    }

    public String loadJSONFromAsset(String type) {
        String json ;
        try {
            InputStream is = getActivity().getAssets().open(type+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public void OpenPlacement() {
        startActivity(new Intent(getActivity(), PlacementActivity.class));}
}