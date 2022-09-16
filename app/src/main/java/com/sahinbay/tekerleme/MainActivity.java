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
    int indexValue = 0;
    int indexCompareValue = 0;

    String[] list = {"ali veli ali selami ali"};
    int rastgele = (int) (Math.random() * list.length);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTekerleme = (TextView) findViewById(R.id.txtTekerleme);
        answer = (TextView) findViewById(R.id.txtSpeechInput);
        txtSoylenen = (TextView) findViewById(R.id.kullanici_konusmasi);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnContinue = (Button) findViewById(R.id.devam);

        gelen_tekerleme = list[rastgele];
        txtTekerleme.setText(gelen_tekerleme);

        btnContinue.setEnabled(false);
        // btnContinue.setClickable(false);
        // btnContinue.setAlpha(0.5f);
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
                    ArrayList<String> artik = new ArrayList<>();
                    ArrayList<Integer> yeniOnurList = new ArrayList<>();

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
                    // for (int i = 0; i < tekerleme_words.length; i++) {

                    for (int j = 0; j < soylenen_words.length; j++) {

                        if (tekerleme_words[j].equalsIgnoreCase(soylenen_words[j])) {
                            mList.add(tekerleme_words[j]);
                            Log.e("Check the matching words", "" + mList.size());
                            // Toast.makeText(getApplicationContext(), "eşleşti" + mList, Toast.LENGTH_SHORT).show();
                        } else {
                            artik.add(tekerleme_words[j]);
                            Log.e("HAYIR", "" + artik.size());
                        }
                    }
                    // listeyi görmek için log ekledim.
                    Log.e("bu liste", "" + mList);
                    // }

                    SpannableString tekelerme_str = new SpannableString(tekelerme);

                    // make the words bold
                    for (int k = 0; k < mList.size(); k++) {
                        indexValue = tekelerme.indexOf(mList.get(k));
                        // str.setSpan(new StyleSpan(Typeface.BOLD), val, val + mList.get(k).length(),
                        if (indexValue >= indexCompareValue) {
                            indexCompareValue = indexValue;
                            Log.e("tab", "" + indexCompareValue + ", bak : " + k);
                        } else {
                            indexValue = tekelerme.lastIndexOf(mList.get(k));
                            indexCompareValue = indexValue;
                            Log.e("yap", "" + indexCompareValue + ", bak : " + k);
                        }

                        yeniOnurList.add(indexCompareValue);

                        Log.e("deger", "" + indexValue);
                    }
                    Log.e("tablomun listesi", "" + yeniOnurList);

                    for (int z = 0; z < mList.size(); z++) {
                        
                        tekelerme_str.setSpan(new ForegroundColorSpan(Color.GREEN), yeniOnurList.get(z), yeniOnurList.get(z) + mList.get(z).length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // set text
                    answer.setText(tekelerme_str);
                    answer.setTextColor(Color.RED);

                    // txtTekerleme.setText(soylenen);
                    txtSoylenen.setText(user_soylenen);
                    btnContinue.setEnabled(true);
                    btnContinue.setAlpha(1.0f);

                    indexValue = 0;
                    indexCompareValue = 0;
                    // Toast.makeText(getApplicationContext(), "devam aktif", Toast.LENGTH_SHORT).show();
                    //  }
                }
                break;
            }
        }
    }
}
