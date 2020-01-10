package com.applitools.helloworld.android;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTitleText;
    private TextView mRandomNumTextView;
    private ImageView mImageView;
    private Button mClickMeBtn;
    private View mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        fillTitle(false);
    }

    private void initViews() {
        mTitleText = findViewById(R.id.hello_text_view);
        mRandomNumTextView = findViewById(R.id.random_number_text_view);
        CheckBox randomNumCheckBox = findViewById(R.id.random_number_check_box);
        CheckBox simulateDiffsCheckBox = findViewById(R.id.simulate_diffs_check_box);
        mContainer = findViewById(R.id.image_container);
        mClickMeBtn = findViewById(R.id.click_me_btn);
        mImageView = findViewById(R.id.image);

        mClickMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContainer.setVisibility(mContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        randomNumCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    generateRandomNum();
                }
            }
        });

        simulateDiffsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mContainer.setVisibility(View.GONE);
                mImageView.setImageResource(isChecked ? R.drawable.image_2 : R.drawable.image);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mClickMeBtn.getLayoutParams();
                params.leftToLeft = isChecked ? ConstraintLayout.LayoutParams.UNSET : ConstraintLayout.LayoutParams.PARENT_ID;
                mClickMeBtn.setLayoutParams(params);
                fillTitle(isChecked);
            }
        });
    }

    private void fillTitle(boolean isChecked) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (isChecked) {
            builder.append(buildSpannableString("H", "#8b00ff"));
            builder.append(buildSpannableString("E", "#4600ff"));
            builder.append(buildSpannableString("L", "#0000ff"));
            builder.append(buildSpannableString("L", "#0080ff"));
            builder.append(buildSpannableString("O", "#00ffff"));
            builder.append(buildSpannableString(" ", "#00ff80"));
            builder.append(buildSpannableString("W", "#ffff00"));
            builder.append(buildSpannableString("O", "#ffff00"));
            builder.append(buildSpannableString("R", "#ffbf00"));
            builder.append(buildSpannableString("L", "#ff7f00"));
            builder.append(buildSpannableString("D", "#ff4000"));
            builder.append(buildSpannableString("!", "#ff0000"));
        } else {
            builder.append(buildSpannableString("H", "#ff0000"));
            builder.append(buildSpannableString("E", "#ff4000"));
            builder.append(buildSpannableString("L", "#ff7f00"));
            builder.append(buildSpannableString("L", "#ffbf00"));
            builder.append(buildSpannableString("O", "#ffff00"));
            builder.append(buildSpannableString(" ", "#ffff00"));
            builder.append(buildSpannableString("W", "#00ff80"));
            builder.append(buildSpannableString("O", "#00ffff"));
            builder.append(buildSpannableString("R", "#0080ff"));
            builder.append(buildSpannableString("L", "#0000ff"));
            builder.append(buildSpannableString("D", "#4600ff"));
            builder.append(buildSpannableString("!", "#8b00ff"));
        }

        mTitleText.setText(builder, TextView.BufferType.SPANNABLE);
    }

    private SpannableString buildSpannableString(String text, String color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, spannableString.length(), 0);
        return spannableString;
    }

    private void generateRandomNum() {
        Random random = new Random();
        int num = random.nextInt(999999);
        mRandomNumTextView.setText(String.valueOf(num));
    }
}
