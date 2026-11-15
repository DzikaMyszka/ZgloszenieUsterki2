package com.example.zgloszenieusterki2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String number = result.getData().getStringExtra(SummaryActivity.EXTRA_NUMBER);
                    Snackbar.make(findViewById(R.id.main), "Zgłoszenie wysłane, numer: " + number, Snackbar.LENGTH_LONG).show();
                }
            });

    private EditText etName, etOpis;
    private Spinner spKategoria;
    private RadioGroup rgPriorytet;
    private CheckBox cbKontakt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etOpis = findViewById(R.id.etOpis);
        spKategoria = findViewById(R.id.spKategoria);
        rgPriorytet = findViewById(R.id.rgPriorytet);
        cbKontakt = findViewById(R.id.cbKontakt);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.kategorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategoria.setAdapter(adapter);

        findViewById(R.id.btnDalej).setOnClickListener(v -> {
            if (validate()) {
                openSummary();
            }
        });
    }

    private boolean validate() {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Podaj imię");
            etName.requestFocus();
            return false;
        }
        if (spKategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Wybierz kategorię", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgPriorytet.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Wybierz priorytet", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etOpis.getText().toString().trim().isEmpty()) {
            etOpis.setError("Opisz usterkę");
            etOpis.requestFocus();
            return false;
        }
        return true;
    }

    private void openSummary() {
        String priorytet = "brak";
        int id = rgPriorytet.getCheckedRadioButtonId();
        if (id == R.id.rbNiski) priorytet = "Niski";
        else if (id == R.id.rbSredni) priorytet = "Średni";
        else if (id == R.id.rbWysoki) priorytet = "Wysoki";

        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(SummaryActivity.EXTRA_NAME, etName.getText().toString().trim());
        intent.putExtra(SummaryActivity.EXTRA_KATEGORIA, spKategoria.getSelectedItem().toString());
        intent.putExtra(SummaryActivity.EXTRA_PRIORYTET, priorytet);
        intent.putExtra(SummaryActivity.EXTRA_OPIS, etOpis.getText().toString().trim());
        intent.putExtra(SummaryActivity.EXTRA_KONTAKT, cbKontakt.isChecked());

        launcher.launch(intent);
    }
}