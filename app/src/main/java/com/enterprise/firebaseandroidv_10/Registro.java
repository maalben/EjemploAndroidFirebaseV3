package com.enterprise.firebaseandroidv_10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    private EditText nombre, apellido, edad, correo, password1, password2;
    private Button guardar, cancelar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Persona");

        nombre = findViewById(R.id.txtnombre);
        apellido = findViewById(R.id.txtapellido);
        edad = findViewById(R.id.txtedad);
        correo = findViewById(R.id.txtcorreo);
        password1 = findViewById(R.id.txtpassword1);
        password2 = findViewById(R.id.txtpassword2);

        guardar = findViewById(R.id.btnguardar);
        cancelar = findViewById(R.id.btncancelarcrear);

        progressDialog = new ProgressDialog(this);


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";

                final String nom = nombre.getText().toString().trim();
                final String ape = apellido.getText().toString().trim();
                final String eda = edad.getText().toString().trim();
                final String cor = correo.getText().toString().trim();
                String pas1 = password1.getText().toString().trim();
                String pas2 = password2.getText().toString().trim();

                if(TextUtils.isEmpty(nom)){
                    Toast.makeText(Registro.this, "Debes escribir un nombre", Toast.LENGTH_LONG).show();
                    nombre.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(ape)){
                    Toast.makeText(Registro.this, "Debes escribir un apellido", Toast.LENGTH_LONG).show();
                    apellido.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(eda)){
                    Toast.makeText(Registro.this, "Debes escribir una edad", Toast.LENGTH_LONG).show();
                    edad.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(cor)){
                    Toast.makeText(Registro.this, "Debes escribir un correo", Toast.LENGTH_LONG).show();
                    correo.requestFocus();
                    return;
                }

                if(!cor.matches(regex)){
                    Toast.makeText(Registro.this, "El formato de correo es incorrecto", Toast.LENGTH_LONG).show();
                    correo.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(pas1)){
                    Toast.makeText(Registro.this, "Debes escribir una contraseña", Toast.LENGTH_LONG).show();
                    password1.requestFocus();
                    return;
                }

                if(pas1.length() <= 5){
                    Toast.makeText(Registro.this, "Debes usar un mínimo de 6 caracteres en la contraseña", Toast.LENGTH_LONG).show();
                    password1.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(pas2)){
                    Toast.makeText(Registro.this, "Confirma la contraseña", Toast.LENGTH_LONG).show();
                    password2.requestFocus();
                    return;
                }

                if(!pas1.equals(pas2)){
                    Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    password1.setText("");
                    password2.setText("");
                    password1.requestFocus();
                    return;
                }

                progressDialog.setMessage("Guardando registro");
                progressDialog.show();


                firebaseAuth.createUserWithEmailAndPassword(cor, pas1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    String idUnico = databaseReference.push().getKey();

                                    PersonasDTO personasDTO = new PersonasDTO(idUnico, nom, ape, eda, cor);

                                    databaseReference.child(idUnico).setValue(personasDTO);

                                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_LONG).show();

                                    nombre.setText("");
                                    apellido.setText("");
                                    edad.setText("");
                                    correo.setText("");
                                    password1.setText("");
                                    password2.setText("");

                                    finish();

                                }else{

                                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                        Toast.makeText(Registro.this, "El correo ya existe", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(Registro.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                                    }

                                }

                                progressDialog.dismiss();

                            }
                        });


            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
