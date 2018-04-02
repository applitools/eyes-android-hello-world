package helloworld.applitools.com.applitoolshelloworld;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

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

        AppCenter.start(getApplication(), "b36e95a0-2218-4c32-8a33-22c06400d1aa", Analytics.class, Crashes.class);

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

    @SuppressWarnings("StringBufferReplaceableByString")
    private void fillTitle(boolean isChecked) {
        StringBuilder builder = new StringBuilder();
        if (isChecked) {
            builder.append("<span style=\"color:#8b00ff;\">H</span>")
                    .append("<span style=\"color:#4600ff;\">E</span>")
                    .append("<span style=\"color:#0000ff;\">L</span>")
                    .append("<span style=\"color:#0080ff;\">L</span>")
                    .append("<span style=\"color:#00ffff;\">O</span>")
                    .append("<span style=\"color:#00ff80;\"> </span>")
                    .append("<span style=\"color:#ffff00;\">W</span>")
                    .append("<span style=\"color:#ffff00;\">O</span>")
                    .append("<span style=\"color:#ffbf00;\">R</span>")
                    .append("<span style=\"color:#ff7f00;\">L</span>")
                    .append("<span style=\"color:#ff4000;\">D</span>")
                    .append("<span style=\"color:#ff0000;\">!</span>");
        } else {
            builder.append("<span style=\"color:#ff0000;\">H</span>")
                    .append("<span style=\"color:#ff4000;\">E</span>")
                    .append("<span style=\"color:#ff7f00;\">L</span>")
                    .append("<span style=\"color:#ffbf00;\">L</span>")
                    .append("<span style=\"color:#ffff00;\">O</span>")
                    .append("<span style=\"color:#ffff00;\"> </span>")
                    .append("<span style=\"color:#00ff80;\">W</span>")
                    .append("<span style=\"color:#00ffff;\">O</span>")
                    .append("<span style=\"color:#0080ff;\">R</span>")
                    .append("<span style=\"color:#0000ff;\">L</span>")
                    .append("<span style=\"color:#4600ff;\">D</span>")
                    .append("<span style=\"color:#8b00ff;\">!</span>");
        }

        mTitleText.setTypeface(Typeface.SANS_SERIF);
        mTitleText.setText(Html.fromHtml(builder.toString()));
    }

    private void generateRandomNum() {
        Random random = new Random();
        int num = random.nextInt(999999);
        mRandomNumTextView.setText(String.valueOf(num));
    }
}
