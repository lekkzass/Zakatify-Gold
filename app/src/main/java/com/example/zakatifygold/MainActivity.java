package com.example.zakatifygold;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView tvTotalGoldValue, tvUruf, tvPayableWeight, tvZakatPayableValue, tvTotalZakat;
    EditText etGoldWeight, etGoldValue;
    RadioGroup rgGoldType;
    LinearLayout layoutResult;
    Button btnCalculate, btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Zakatify Gold");
        }

        tvTotalGoldValue = findViewById(R.id.tvTotalGoldValue);
        tvUruf = findViewById(R.id.tvUruf);
        tvPayableWeight = findViewById(R.id.tvPayableWeight);
        tvZakatPayableValue = findViewById(R.id.tvZakatPayableValue);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);
        etGoldWeight = findViewById(R.id.etGoldWeight);
        etGoldValue = findViewById(R.id.etGoldValue);
        rgGoldType = findViewById(R.id.rgGoldType);
        layoutResult = findViewById(R.id.layoutResult);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        if (savedInstanceState != null) {
            etGoldWeight.setText(savedInstanceState.getString("goldWeight", ""));
            etGoldValue.setText(savedInstanceState.getString("goldValue", ""));
            rgGoldType.check(savedInstanceState.getInt("goldType", R.id.rbKeep));

            tvTotalGoldValue.setText(savedInstanceState.getString("totalGoldValue", "Total Gold Value: RM 0.00"));
            tvUruf.setText(savedInstanceState.getString("uruf", "Uruf Threshold: 0g"));
            tvPayableWeight.setText(savedInstanceState.getString("payableWeight", "Zakat Payable Weight: 0g"));
            tvZakatPayableValue.setText(savedInstanceState.getString("zakatPayableValue", "Zakat Payable Value: RM 0.00"));
            tvTotalZakat.setText(savedInstanceState.getString("totalZakat", "Total Zakat: RM 0.00"));

            boolean resultVisible = savedInstanceState.getBoolean("resultVisible", false);
            if (resultVisible) {
                layoutResult.setVisibility(View.VISIBLE);
            }
            else {
                layoutResult.setVisibility(View.GONE);
            }
        }

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double goldWeight, goldValue, uruf, totalGoldValue, payableWeight, zakatPayableValue, totalZakat;

                try {
                    goldWeight = Double.parseDouble(etGoldWeight.getText().toString());
                    goldValue = Double.parseDouble(etGoldValue.getText().toString());

                    if (goldWeight <= 0) {
                        etGoldWeight.setError("Gold weight must be greater than 0");
                        return;
                    }

                    if (goldValue <= 0) {
                        etGoldValue.setError("Gold value must be greater than 0");
                        return;
                    }

                    if (rgGoldType.getCheckedRadioButtonId() == R.id.rbKeep) {
                        uruf = 85;
                    } else {
                        uruf = 200;
                    }

                    totalGoldValue = goldWeight * goldValue;
                    payableWeight = goldWeight - uruf;

                    if (payableWeight < 0) {
                        payableWeight = 0;
                    }

                    zakatPayableValue = payableWeight * goldValue;
                    totalZakat = zakatPayableValue * 0.025;

                    DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
                    DecimalFormat weightFormat = new DecimalFormat("#,##0.##");

                    tvTotalGoldValue.setText("Total Gold Value: RM " + moneyFormat.format(totalGoldValue));
                    tvUruf.setText("Uruf Threshold: " + weightFormat.format(uruf) + "g");
                    tvPayableWeight.setText("Zakat Payable Weight: " + weightFormat.format(payableWeight) + "g");
                    tvZakatPayableValue.setText("Zakat Payable Value: RM " + moneyFormat.format(zakatPayableValue));
                    tvTotalZakat.setText("Total Zakat: RM " + moneyFormat.format(totalZakat));

                    layoutResult.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "Gold Zakat calculated successfully", Toast.LENGTH_SHORT).show();

                } catch (NumberFormatException nfe) {
                    boolean hasError = false;

                    if (etGoldWeight.getText().toString().isEmpty()) {
                        etGoldWeight.setError("Gold weight cannot be empty");
                        etGoldWeight.requestFocus();
                        hasError = true;
                    }

                    if (etGoldValue.getText().toString().isEmpty()) {
                        etGoldValue.setError("Gold value cannot be empty");
                        if (!hasError) {
                            etGoldValue.requestFocus();
                        }
                        hasError = true;
                    }

                    if (!hasError) {
                        Toast.makeText(getApplicationContext(), "Please enter valid values", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvTotalGoldValue.setText("Total Gold Value: RM 0.00");
                tvUruf.setText("Uruf Threshold: 0g");
                tvPayableWeight.setText("Zakat Payable Weight: 0g");
                tvZakatPayableValue.setText("Zakat Payable Value: RM 0.00");
                tvTotalZakat.setText("Total Zakat: RM 0.00");

                etGoldWeight.setText("");
                etGoldValue.setText("");
                rgGoldType.check(R.id.rbKeep);

                Toast.makeText(getApplicationContext(), "Form has been reset", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("goldWeight", etGoldWeight.getText().toString());
        outState.putString("goldValue", etGoldValue.getText().toString());
        outState.putInt("goldType", rgGoldType.getCheckedRadioButtonId());

        outState.putString("totalGoldValue", tvTotalGoldValue.getText().toString());
        outState.putString("uruf", tvUruf.getText().toString());
        outState.putString("payableWeight", tvPayableWeight.getText().toString());
        outState.putString("zakatPayableValue", tvZakatPayableValue.getText().toString());
        outState.putString("totalZakat", tvTotalZakat.getText().toString());

        outState.putBoolean("resultVisible", layoutResult.getVisibility() == View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuShare) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");

            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakatify Gold App");
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Yo! Check out Zakatify Gold app: https://github.com/lekkzass/Zakatify-Gold"
            );

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            } else {
                Toast.makeText(this, "No app found to share", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (selected == R.id.menuAbout) {
            Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}