package com.sahinbay.tekerleme;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView answer, txtTekerleme;
    private ImageButton btnSpeak;
    private Button btnContinue;
    private String gelen_tekerleme;
    private TextView txtSoylenen;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    int startSpan = 0;
    int endSpan = 0;

    String[] list = {"Bizde bize biz derler , sizde size siz derler , sizde bize ne derler"};
    int randomX = (int) (Math.random() * list.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTekerleme = (TextView) findViewById(R.id.txtTekerleme);
        answer = (TextView) findViewById(R.id.txtSpeechInput);
        txtSoylenen = (TextView) findViewById(R.id.kullanici_konusmasi);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnContinue = (Button) findViewById(R.id.devam);

        gelen_tekerleme = list[randomX];
        txtTekerleme.setText(gelen_tekerleme);

        btnContinue.setEnabled(false);
        btnContinue.setAlpha(0.5f);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinue.setEnabled(false);
                btnContinue.setAlpha(0.5f);
                answer.setText("...");
                answer.setTextColor(Color.WHITE);
                int rastgele = (int) (Math.random() * list.length);
                gelen_tekerleme = list[rastgele];
                txtTekerleme.setText(gelen_tekerleme);
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // txtSpeechInput.setText(result.get(0));

                    ArrayList<String> mList = new ArrayList<>();
                    ArrayList<String> onurs = new ArrayList<>();
                    ArrayList<String> othermList = new ArrayList<>();

                    // if (gelen_tekerleme.equalsIgnoreCase(result.get(0))) {
                    //tekerleme string
                    String tekelerme = gelen_tekerleme;

                    // Words to split
                    String[] tekerleme_words = tekelerme.split(" ");

                    // main string
                    String user_soylenen = result.get(0);

                    // split strings by space
                    String[] soylenen_words = user_soylenen.split(" ");
                    SpannableString soylenen_str = new SpannableString(user_soylenen);

                    // Check the matching words
                    for (int i = 0; i < tekerleme_words.length; i++) {
                        onurs.add(tekerleme_words[i]);
                    }

                    for (int i = 0; i < onurs.size(); i++) {

                        if (onurs.get(i).equalsIgnoreCase(",")) {
                            onurs.remove(",");
                            Log.e("STATE", "virgül silindi");
                        }
                    }

                    for (int j = 0; j < onurs.size(); j++) {
                        if (j < soylenen_words.length && onurs.get(j).equalsIgnoreCase(soylenen_words[j])) {
                            mList.add(onurs.get(j));
                            Log.e("matching words", "" + mList.size());
                        } else {
                            othermList.add(onurs.get(j));
                            Log.e("unmatched words", "" + othermList.size());
                        }
                    }

                    Log.e("asıl liste", "" + onurs);
                    Log.e("bu liste", "" + mList);

                    SpannableString tekelerme_str = new SpannableString(tekelerme);

                    for (int k = 0; k < mList.size(); k++) {
                        startSpan = tekelerme.indexOf(mList.get(k), endSpan);
                        ForegroundColorSpan foreColour = new ForegroundColorSpan(Color.GREEN);
                        // Need a NEW span object every loop, else it just moves the span
                        if (startSpan < 0)
                            break;
                        endSpan = startSpan + mList.get(k).length();
                        tekelerme_str.setSpan(foreColour, startSpan, endSpan,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    answer.setText(tekelerme_str);
                    if (tekelerme.contains(",")) {
                        answer.setTextColor(Color.BLUE);
                    } else if (!tekelerme.contains(",")) {
                        answer.setTextColor(Color.RED);
                    }
                    txtSoylenen.setText(user_soylenen);
                    btnContinue.setEnabled(true);
                    btnContinue.setAlpha(1.0f);

                    startSpan = 0;
                    endSpan = 0;

                    Log.e("btnContinue", "devam butonu aktif");
                }
                break;
            }
        }
    }
}
