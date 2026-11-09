package com.applitools.helloworld.android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/** A list far taller than the screen, with a button and a status label at its very bottom. */
public class ScrollActivity extends AppCompatActivity {

    static final int ROWS = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        LinearLayout rows = findViewById(R.id.rows);
        int rowHeight = Math.round(56 * getResources().getDisplayMetrics().density);
        for (int i = 1; i <= ROWS; i++) {
            TextView row = new TextView(this);
            row.setText(getString(R.string.row_n, i));
            row.setTextColor(Color.BLACK);
            row.setTextSize(18);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(rowHeight / 2, 0, 0, 0);
            row.setBackgroundColor(i % 2 == 0 ? 0xFFF2F2F2 : Color.WHITE);
            rows.addView(row, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeight));
        }

        final TextView status = findViewById(R.id.bottom_status);
        findViewById(R.id.bottom_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setVisibility(View.VISIBLE);
            }
        });
    }
}
