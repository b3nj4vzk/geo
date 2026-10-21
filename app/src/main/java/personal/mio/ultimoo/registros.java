package personal.mio.ultimoo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class registros extends AppCompatActivity {

    private EditText edtTitulo, edtDescripcion, edtFecha, edtHora;
    private Spinner spnCategoria;
    private TextView txtCoordenadas;
    private Button btnObtenerGPS, btnGuardarIncidencia;

    private double latitud = 0;
    private double longitud = 0;

    private LocationManager locationManager;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<String[]> solicitarPermisos =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    resultado -> {
                        boolean permitido =
                                Boolean.TRUE.equals(resultado.get(
                                        Manifest.permission.ACCESS_FINE_LOCATION))
                                        || Boolean.TRUE.equals(resultado.get(
                                        Manifest.permission.ACCESS_COARSE_LOCATION));

                        if (permitido) {
                            obtenerGPS();
                        } else {
                            Toast.makeText(this,
                                    "Debes permitir la ubicación para usar el GPS",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        Button btnVerIncidencias = findViewById(R.id.btnVerIncidencias);

        btnVerIncidencias.setOnClickListener(v -> {
            Intent intent = new Intent(registros.this, lista.class);
            startActivity(intent);
        });




        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtFecha = findViewById(R.id.edtFecha);
        edtHora = findViewById(R.id.edtHora);

        spnCategoria = findViewById(R.id.spnCategoria);
        txtCoordenadas = findViewById(R.id.txtCoordenadas);
        btnObtenerGPS = findViewById(R.id.btnObtenerGPS);
        btnGuardarIncidencia = findViewById(R.id.btnGuardarIncidencia);

        db = FirebaseFirestore.getInstance();

        String[] categorias = {
                "Selecciona una categoría",
                "Calles y veredas",
                "Alumbrado público",
                "Basura",
                "Seguridad",
                "Otros"
        };

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categorias
        );

        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spnCategoria.setAdapter(adaptador);

        edtFecha.setText(new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault()).format(new Date()));

        edtHora.setText(new SimpleDateFormat(
                "HH:mm", Locale.getDefault()).format(new Date()));

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        btnObtenerGPS.setOnClickListener(v -> comprobarPermisosGPS());
        btnGuardarIncidencia.setOnClickListener(v -> validarFormulario());
    }

    private void comprobarPermisosGPS() {
        boolean fino = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        boolean aproximado = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (fino || aproximado) {
            obtenerGPS();
        } else {
            solicitarPermisos.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void obtenerGPS() {
        boolean fino = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        boolean aproximado = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (!fino && !aproximado) {
            return;
        }

        try {
            Location ubicacion = null;

            if (fino && locationManager.isProviderEnabled(
                    LocationManager.GPS_PROVIDER)) {
                ubicacion = locationManager.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER);
            }

            if (ubicacion == null && (fino || aproximado)
                    && locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER)) {
                ubicacion = locationManager.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER);
            }

            if (ubicacion != null) {
                latitud = ubicacion.getLatitude();
                longitud = ubicacion.getLongitude();

                txtCoordenadas.setText(String.format(
                        Locale.getDefault(),
                        "Latitud: %.6f\nLongitud: %.6f",
                        latitud, longitud));
            } else {
                Toast.makeText(this,
                        "No hay ubicación reciente. Activa el GPS e inténtalo de nuevo.",
                        Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this,
                    "No se pudo acceder a la ubicación.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void validarFormulario() {
        String titulo = edtTitulo.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();
        String fecha = edtFecha.getText().toString().trim();
        String hora = edtHora.getText().toString().trim();

        if (titulo.isEmpty()) {
            edtTitulo.setError("Ingresa el título");
            edtTitulo.requestFocus();
            return;
        }

        if (descripcion.isEmpty()) {
            edtDescripcion.setError("Ingresa una descripción");
            edtDescripcion.requestFocus();
            return;
        }

        if (spnCategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecciona una categoría",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (fecha.isEmpty()) {
            edtFecha.setError("Ingresa la fecha");
            return;
        }

        if (hora.isEmpty()) {
            edtHora.setError("Ingresa la hora");
            return;
        }

        if (latitud == 0 && longitud == 0) {
            Toast.makeText(this,
                    "Obtén la ubicación GPS antes de guardar",
                    Toast.LENGTH_LONG).show();
            return;
        }

        guardarEnFirestore(titulo, descripcion, fecha, hora);
    }

    private void guardarEnFirestore(
            String titulo, String descripcion, String fecha, String hora) {

        btnGuardarIncidencia.setEnabled(false);

        Map<String, Object> incidencia = new HashMap<>();
        incidencia.put("titulo", titulo);
        incidencia.put("descripcion", descripcion);
        incidencia.put("categoria", spnCategoria.getSelectedItem().toString());
        incidencia.put("fecha", fecha);
        incidencia.put("hora", hora);
        incidencia.put("latitud", latitud);
        incidencia.put("longitud", longitud);

        db.collection("incidencias")
                .add(incidencia)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this,
                            "¡Incidencia guardada correctamente!",
                            Toast.LENGTH_LONG).show();

                    edtTitulo.setText("");
                    edtDescripcion.setText("");
                    spnCategoria.setSelection(0);
                    btnGuardarIncidencia.setEnabled(true);
                })
                .addOnFailureListener(e -> {
                    btnGuardarIncidencia.setEnabled(true);
                    Toast.makeText(this,
                            "No se pudo guardar. Revisa las reglas de Firestore y tu conexión.",
                            Toast.LENGTH_LONG).show();
                });
    }
}
