package com.mailerdaemon.app;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.intro_btn_next)
    ImageButton mNextBtn;

    @BindView(R.id.intro_btn_skip)
    Button mSkipBtn;
    @BindView(R.id.intro_btn_finish)
    Button mFinishBtn;
    @BindView(R.id.intro_indicator_1)
    ImageView indi1;
    @BindView(R.id.intro_indicator_2)
    ImageView indi2;
    @BindView(R.id.intro_indicator_3)
    ImageView indi3;
    @BindView(R.id.intro_indicator_4)
    ImageView indi4;
    @BindView(R.id.intro_indicator_5)
    ImageView indi5;
    @BindView(R.id.intro_indicator_6)
    ImageView indi6;
    ImageView[] indicators;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        indicators=new ImageView[]{indi1,indi2,indi3,indi4,indi5,indi6};
        int color1 = ContextCompat.getColor(this, R.color.intro_1);
        int color2 = ContextCompat.getColor(this, R.color.intro_2);
        int color3 = ContextCompat.getColor(this, R.color.intro_3);
        ArgbEvaluator evaluator=new ArgbEvaluator();
        int[] colorList = new int[]{color1, color2,color3,color1,color2,color3};
        viewPager.setAdapter(new IntroAdapter());
        updateIndicators(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == colorList.length-1 ? position : position + 1]);
                viewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);

                switch (position) {
                    case 0:
                        viewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        viewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        viewPager.setBackgroundColor(color3);
                        break;
                    case 3:
                        viewPager.setBackgroundColor(color1);
                        break;
                    case 4:
                        viewPager.setBackgroundColor(color2);
                        break;
                    case 5:
                        viewPager.setBackgroundColor(color3);
                        break;
                }
                mNextBtn.setVisibility(position == colorList.length-1 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == colorList.length-1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mNextBtn.setOnClickListener(v-> viewPager.setCurrentItem(viewPager.getCurrentItem()+1));
        mSkipBtn.setOnClickListener(v->{
            getSharedPreferences("MAIN",MODE_PRIVATE).edit().putBoolean("intro",false).apply();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });
        mFinishBtn.setOnClickListener(v->{
            getSharedPreferences("MAIN",MODE_PRIVATE).edit().putBoolean("intro",false).apply();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });
    }
    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }
}
