package personal.mio.ultimoo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnRegistrar;
    private Button btnHistorial;
    private Button btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnMapa = findViewById(R.id.btnMapa);

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    registros.class
            );
            startActivity(intent);
        });

        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, lista.class);
            startActivity(intent);
        });

        btnMapa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, mapa.class);
            startActivity(intent);
        });
    }
}