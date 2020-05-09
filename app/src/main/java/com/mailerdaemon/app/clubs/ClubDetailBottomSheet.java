package com.mailerdaemon.app.clubs;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.gson.GsonBuilder;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.ChromeTab;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import vcm.github.webkit.proview.ProWebView;

public class ClubDetailBottomSheet extends BottomSheetDialogFragment {

  @BindView(R.id.club_des)
  TextView description;
  @BindView(R.id.club_members)
  TextView members;
  @BindView(R.id.club_name)
  TextView name;
  @BindView(R.id.club_edit)
  ImageView create;
  @BindView(R.id.club_fb)
  ImageView fb;
  @BindView(R.id.club_insta)
  ImageView insta;
  @BindView(R.id.club_youtube)
  ImageView youtube;
  @BindView(R.id.club_web)
  ImageView web;
  @BindView(R.id.club_icon)
  SimpleDraweeView club;
  @BindView(R.id.shimmer_view_container)
  LinearLayout linearLayout;
  @BindView(R.id.web)
  ProWebView webView;
  private int id;
  private ChromeTab chromeTab;
  private Boolean access;
  private ClubDetailModel model;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override

  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_club_detail, container, false);
    ButterKnife.bind(this, view);
    chromeTab=new ChromeTab(getContext());
    webView.setHorizontalScrollBarEnabled(true);
    club.getHierarchy().setProgressBarImage(new CircularProgressDrawable(Objects.requireNonNull(getContext())));
    assert getArguments() != null;
    id = getArguments().getInt("club_id");
    getDatabase();

    access= PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()).getApplicationContext()).getBoolean("Access",false);

    return view;
  }


  private void getDatabase() {
    FirebaseFirestore.getInstance().collection(ConstantsKt.FB_Collec_Club).document(String.format("%d",id)).get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        model= Objects.requireNonNull(task.getResult()).toObject(ClubDetailModel.class);
        setView(model);
      }else
      {
        Toast.makeText(getContext(), ConstantsKt.No_Internet,Toast.LENGTH_LONG).show();
        FirebaseFirestore.getInstance().collection(ConstantsKt.FB_Collec_Club).document(String.format("%d",id)).get(Source.CACHE).addOnSuccessListener(documentSnapshot -> setView(Objects.requireNonNull(task.getResult()).toObject(ClubDetailModel.class)));
      }
    });

  }

  private void setView (ClubDetailModel details){
    if(details!=null) {
      description.setText(details.getDescription());
      name.setText(details.getName());
      if(details.getClub()!=null)
      club.setImageURI(Uri.parse(details.getClub()));
      members.setText(details.getMembers());
      fb.setOnClickListener(v -> chromeTab.openTab(details.getFb()));
      if(!details.getInsta().isEmpty())
      {insta.setVisibility(View.VISIBLE);
      insta.setOnClickListener(v -> chromeTab.openTab(details.getInsta()));}
      if(!details.getYoutube().isEmpty())
      { youtube.setVisibility(View.VISIBLE);
      youtube.setOnClickListener(v -> chromeTab.openTab(details.getYoutube()));}
      if(!details.getWeb().isEmpty())
      { web.setVisibility(View.VISIBLE);
      web.setOnClickListener(v -> chromeTab.openTab(details.getWeb()));}
      setposts(details.getFb());
    }
      if (access) {
        create.setVisibility(View.VISIBLE);
        create.setOnClickListener(v -> {
          EditClubFragment clubFragment = new EditClubFragment();
          Bundle bundle = new Bundle();
          bundle.putInt("id", id);
          bundle.putString("data",new GsonBuilder().create().toJson(model));
          clubFragment.setArguments(bundle);
          clubFragment.show(getChildFragmentManager(), null);
        });
      }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setposts(String page){
    if(page.isEmpty())
      return;
      //webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
          String site=page;
          if(page.length()>25)
          site=site.substring(25);
//          int x=webView.getMeasuredWidth()-657;
//          int y=webView.getMeasuredHeight();
          //webView.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);

//          String s="<!DOCTYPE html>\n" +
//                  "<html>\n" +
//                  "<head>\n" +
//                  "\t<title></title>\n" +
//                  "</head>\n" +
//                  "<body style=\"height: 1000px;text-align: center\">\n" +
//                  "\t<div id='yeep' style='width: 100%;height:100%;text-align:center' >\n" +
//                  "\t</div>\n" +
//                  "</body>\n" +
//                  "\n" +
//                  "<script type=\"text/javascript\">\n" +
//                  "\tvar s = document.getElementById(\"yeep\");\n" +
//                  "\ts.innerHTML+=\"<iframe src='https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2F"+site+"&tabs=timeline&width=\"+screen.width+\"&height=\"+screen.height+\"&small_header=true&adapt_container_width=true&hide_cover=true&show_facepile=false&appId=384900825472866'  style='border:none;height:100%;width: 100%; ' scrolling='yes' frameborder='0' allowTransparency='true' allow='encrypted-media'></iframe>\";\n" +
//                  "\t//document.write(s.innerHTML);\n" +
//                  "</script>\n" +
//                  "</html>\n";
                String s="<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2F"+site+"&tabs=timeline&width="+350+"&height="+700+"&small_header=true&adapt_container_width=true&hide_cover=true&show_facepile=false&appId=384900825472866\" width=\"100%\" height=\"100%\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\" allow=\"encrypted-media\"></iframe>";
          webView.loadHtml(s);
        //}
      //});

      webView.setOnTouchListener((v, event) -> event.getAction() == MotionEvent.ACTION_BUTTON_PRESS);
    }

}
