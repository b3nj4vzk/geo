package personal.mio.ultimoo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class mapa extends AppCompatActivity {

    private MapView mapa;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_mapa);

        mapa = findViewById(R.id.mapa);
        db = FirebaseFirestore.getInstance();

        mapa.setMultiTouchControls(true);

        GeoPoint inicio = new GeoPoint(-36.8201, -73.0444);

        mapa.getController().setZoom(12.0);
        mapa.getController().setCenter(inicio);

        cargarIncidencias();
    }

    private void cargarIncidencias() {
        db.collection("incidencias")
                .get()
                .addOnSuccessListener(resultado -> {

                    int cantidad = 0;
                    GeoPoint primeraIncidencia = null;

                    for (QueryDocumentSnapshot documento : resultado) {

                        Double latitud = obtenerCoordenada(documento, "latitud");
                        Double longitud = obtenerCoordenada(documento, "longitud");

                        if (latitud == null || longitud == null) {
                            continue;
                        }

                        if (latitud < -90 || latitud > 90 ||
                                longitud < -180 || longitud > 180) {
                            continue;
                        }

                        String titulo = documento.getString("titulo");
                        String descripcion = documento.getString("descripcion");

                        GeoPoint ubicacion = new GeoPoint(latitud, longitud);

                        Marker marcador = new Marker(mapa);
                        marcador.setPosition(ubicacion);
                        marcador.setAnchor(
                                Marker.ANCHOR_CENTER,
                                Marker.ANCHOR_BOTTOM
                        );
                        marcador.setTitle(
                                titulo != null ? titulo : "Incidencia"
                        );
                        marcador.setSnippet(
                                descripcion != null ? descripcion : ""
                        );

                        mapa.getOverlays().add(marcador);

                        if (primeraIncidencia == null) {
                            primeraIncidencia = ubicacion;
                        }

                        cantidad++;
                    }

                    if (primeraIncidencia != null) {
                        mapa.getController().setZoom(15.0);
                        mapa.getController().animateTo(primeraIncidencia);
                    }

                    mapa.invalidate();

                    Toast.makeText(
                            mapa.this,
                            "Incidencias ubicadas en el mapa: " + cantidad,
                            Toast.LENGTH_LONG
                    ).show();
                })
                .addOnFailureListener(error -> {
                    Toast.makeText(
                            mapa.this,
                            "Error al cargar incidencias: " + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private Double obtenerCoordenada(
            QueryDocumentSnapshot documento,
            String campo
    ) {
        Object valor = documento.get(campo);

        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }

        if (valor instanceof String) {
            try {
                return Double.parseDouble((String) valor);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mapa != null) {
            mapa.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mapa != null) {
            mapa.onPause();
        }
    }
}
