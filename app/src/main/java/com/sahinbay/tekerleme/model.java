package com.sahinbay.tekerleme;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class model extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String dispStr = "ali veli ali selami ali";
        TextView tv = (TextView) findViewById(R.id.txtTekerleme);
        tv.setText(dispStr);
        changeTextinView(tv, "ali", Color.RED);
    }

    private void changeTextinView(TextView tv, String target, int colour) {
        String vString = (String) tv.getText();
        int startSpan = 0, endSpan = 0;
        Spannable spanRange = new SpannableString(vString);

        while (true) {
            startSpan = vString.indexOf(target, endSpan);
            ForegroundColorSpan foreColour = new ForegroundColorSpan(colour);
            // Need a NEW span object every loop, else it just moves the span
            if (startSpan < 0)
                break;
            endSpan = startSpan + target.length();
            spanRange.setSpan(foreColour, startSpan, endSpan,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(spanRange);
    }

}