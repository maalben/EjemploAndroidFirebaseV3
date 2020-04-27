package com.enterprise.firebaseandroidv_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private TextView correo, nombre;
    private Button cerrar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String email = getIntent().getStringExtra("email");
        databaseReference = FirebaseDatabase.getInstance().getReference("Persona");
        correo = findViewById(R.id.lblusuario);
        nombre = findViewById(R.id.lblnombre);
        cerrar = findViewById(R.id.btncerrar);

        Query query = databaseReference.orderByChild("correo").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datos : dataSnapshot.getChildren()){
                    PersonasDTO personasDTO = datos.getValue(PersonasDTO.class);
                    correo.setText(personasDTO.getCorreo());
                    nombre.setText(personasDTO.getNombre() + " " + personasDTO.getApellido());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

    }
}
