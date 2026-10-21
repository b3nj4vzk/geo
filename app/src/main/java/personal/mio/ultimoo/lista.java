package personal.mio.ultimoo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class lista extends AppCompatActivity {

    private ListView listaIncidencias;
    private Button btnActualizar;
    private ArrayList<String> incidencias;
    private ArrayAdapter<String> adaptador;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        listaIncidencias = findViewById(R.id.listaIncidencias);
        btnActualizar = findViewById(R.id.btnActualizar);

        db = FirebaseFirestore.getInstance();

        incidencias = new ArrayList<>();

        adaptador = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                incidencias
        );

        listaIncidencias.setAdapter(adaptador);

        cargarIncidencias();

        btnActualizar.setOnClickListener(v -> cargarIncidencias());
    }

    private void cargarIncidencias() {

        db.collection("incidencias")
                .get()
                .addOnSuccessListener(resultado -> {

                    incidencias.clear();

                    int cantidad = resultado.size();

                    Toast.makeText(
                            lista.this,
                            "Registros encontrados: " + cantidad,
                            Toast.LENGTH_LONG
                    ).show();

                    for (QueryDocumentSnapshot documento : resultado) {

                        String titulo = documento.getString("titulo");
                        String descripcion = documento.getString("descripcion");
                        String categoria = documento.getString("categoria");
                        String fecha = documento.getString("fecha");
                        String hora = documento.getString("hora");

                        String texto =
                                "Título: " + (titulo != null ? titulo : "") +
                                        "\nDescripción: " + (descripcion != null ? descripcion : "") +
                                        "\nCategoría: " + (categoria != null ? categoria : "") +
                                        "\nFecha: " + (fecha != null ? fecha : "") +
                                        "\nHora: " + (hora != null ? hora : "");

                        incidencias.add(texto);
                    }

                    adaptador.notifyDataSetChanged();

                    if (incidencias.isEmpty()) {
                        Toast.makeText(
                                lista.this,
                                "No hay incidencias para mostrar",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                })
                .addOnFailureListener(error -> {

                    Toast.makeText(
                            lista.this,
                            "Error al cargar: " + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
