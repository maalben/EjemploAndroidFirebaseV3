package com.enterprise.firebaseandroidv_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText usuario, clave;
    private Button ingresar, crear;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Persona");

        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.txtpassword);
        ingresar = findViewById(R.id.btningresar);
        crear = findViewById(R.id.btncrear);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
                final String email = usuario.getText().toString().trim();
                String password = clave.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Debes escribir el usuario", Toast.LENGTH_LONG).show();
                    usuario.requestFocus();
                    return;
                }

                if(!email.matches(regex)){
                    Toast.makeText(MainActivity.this, "Formato de correo incorrecto", Toast.LENGTH_LONG).show();
                    usuario.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Debes escribir la contraseña", Toast.LENGTH_LONG).show();
                    clave.requestFocus();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Query query = databaseReference.orderByChild("correo").equalTo(email);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot datos : dataSnapshot.getChildren()){
                                                PersonasDTO personasDTO = datos.getValue(PersonasDTO.class);
                                                Intent intent = new Intent(MainActivity.this, Home.class);
                                                intent.putExtra("email", personasDTO.getCorreo());
                                                usuario.setText("");
                                                clave.setText("");
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }else{
                                    Toast.makeText(MainActivity.this, "Usuario y/o clave no válidos", Toast.LENGTH_LONG).show();
                                    usuario.setText("");
                                    clave.setText("");
                                }

                            }
                        });
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
            }
        });

    }
}
