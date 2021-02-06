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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput, txtTekerleme;
    private ImageButton btnSpeak;
    private Button btnContinue;
    private String gelen_tekerleme;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    String[] list = {"Dal sarkar kartal kalkar kartal kalkar dal sarkar", "Üç tunç tas kayısı hoşafı", "Adem madene gitmiş Adem madende badem yemiş , madem ki Adem madende badem yemiş , niye bize getirmemiş ."};
    int rastgele = (int) (Math.random() * list.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTekerleme = (TextView) findViewById(R.id.txtTekerleme);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnContinue = (Button) findViewById(R.id.devam);

        gelen_tekerleme = list[rastgele];
        txtTekerleme.setText(gelen_tekerleme);

        btnContinue.setEnabled(false);
        // btnContinue.setClickable(false);
        btnContinue.setAlpha(0.5f);
        btnContinue.getBackground().setAlpha(50);

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

                txtSpeechInput.setText("...");

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

                    // if (gelen_tekerleme.equalsIgnoreCase(result.get(0))) {
                    //tekerleme string
                    String tekelerme = gelen_tekerleme;
                    //Words to split
                    String[] words = tekelerme.split(" ");
                    SpannableString tekelerme_str = new SpannableString(tekelerme);
                    //main string
                    String soylenen = result.get(0);
                    //split strings by space
                    String[] splittedWords = soylenen.split(" ");
                    SpannableString str = new SpannableString(soylenen);

                    //Check the matching words
                    for (int i = 0; i < words.length; i++) {
                        for (int j = 0; j < splittedWords.length; j++) {
                            if (words[i].equalsIgnoreCase(splittedWords[j])) {
                                mList.add(words[i]);
                            }
                        }
                    }

                    //make the words bold
                    for (int k = 0; k < mList.size(); k++) {
                        int val = tekelerme.indexOf(mList.get(k));
                        // str.setSpan(new StyleSpan(Typeface.BOLD), val, val + mList.get(k).length(),
                        tekelerme_str.setSpan(new ForegroundColorSpan(Color.GREEN), val, val + mList.get(k).length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    //set text
                    TextView answer = (TextView) findViewById(R.id.txtSpeechInput);
                    answer.setTextColor(Color.RED);
                    answer.setText(tekelerme_str);

                    // txtTekerleme.setText(soylenen);
                    btnContinue.setEnabled(true);
                    btnContinue.setAlpha(1.0f);
                    Toast.makeText(getApplicationContext(), "devam aktif", Toast.LENGTH_SHORT).show();
                    //  }
                }
                break;
            }
        }
    }
}
