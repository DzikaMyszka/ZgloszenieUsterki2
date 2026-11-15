package com.example.zgloszenieusterki2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class SummaryActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.zgloszenieusterki2.NAME";
    public static final String EXTRA_KATEGORIA = "com.example.zgloszenieusterki2.KATEGORIA";
    public static final String EXTRA_PRIORYTET = "com.example.zgloszenieusterki2.PRIORYTET";
    public static final String EXTRA_OPIS = "com.example.zgloszenieusterki2.OPIS";
    public static final String EXTRA_KONTAKT = "com.example.zgloszenieusterki2.KONTAKT";
    public static final String EXTRA_NUMBER = "com.example.zgloszenieusterki2.NUMBER";

    private TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        tvSummary = findViewById(R.id.tvSummary);

        Intent i = getIntent();
        String name = i.getStringExtra(EXTRA_NAME);
        String kat = i.getStringExtra(EXTRA_KATEGORIA);
        String prio = i.getStringExtra(EXTRA_PRIORYTET);
        String opis = i.getStringExtra(EXTRA_OPIS);
        boolean kontakt = i.getBooleanExtra(EXTRA_KONTAKT, false);

        tvSummary.setText("Imię: " + name + "\nKategoria: " + kat + "\nPriorytet: " + prio + "\nOpis: " + opis + "\nKontakt: " + (kontakt ? "tak" : "nie"));

        findViewById(R.id.btnEdit).setOnClickListener(v -> finish());

        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            String number = "UST/" + (1000 + new Random().nextInt(9000));
            Intent result = new Intent();
            result.putExtra(EXTRA_NUMBER, number);
            setResult(RESULT_OK, result);
            finish();
        });

        findViewById(R.id.btnShare).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, tvSummary.getText().toString());
            try {
                startActivity(Intent.createChooser(share, "Udostępnij"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Brak aplikacji", Toast.LENGTH_SHORT).show();
            }
        });
    }
}