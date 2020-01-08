package com.example.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText fullname,email,password,phone;
    Button register;

    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname=findViewById(R.id.edtName);
        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.edtPassword);
        phone=findViewById(R.id.edtPhone);
        register=findViewById(R.id.btnSignUp);

        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_fullname=fullname.getText().toString();
                String str_email=email.getText().toString();
                String str_password=password.getText().toString();
                String str_phone=phone.getText().toString();

                if(TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)
                        || TextUtils.isEmpty(str_phone)){
                    Toast.makeText(RegisterActivity.this, "All fields are required !", Toast.LENGTH_SHORT).show();
                }else if(str_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters !", Toast.LENGTH_SHORT).show();
                }else{
                    register(str_fullname,str_email,str_password,str_phone);
                }
            }
        });
    }

    private void register(final String fullname, final String email, final String password, final String phone) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();


                            //reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                            reference= FirebaseDatabase.getInstance().getReference("/Users/" + userid);

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("fullname",fullname);
                            hashMap.put("phone",phone);
                            hashMap.put("imageurl","https://firebasestorage.googleapis.com/...........");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Bu E-mail ve şifreyle kayıt olunamaz !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
