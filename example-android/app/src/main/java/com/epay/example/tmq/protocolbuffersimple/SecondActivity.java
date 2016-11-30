package com.epay.example.tmq.protocolbuffersimple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epay.example.tmq.protocolbuffersimple.AddressBookProtos.*;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by tmq on 28/11/2016.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private Person person;
    private EditText edtName, edtEmail, edtPhone;
    private Button btnCancel, btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);


        Intent receiveIntent = getIntent();
        try {
            person = Person.parseFrom(receiveIntent.getByteArrayExtra(MainActivity.DATA_ARR_BYTE))
                    .toBuilder()
                    .build();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        initViews();

        setData();
    }

    private void initViews(){
        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPhone = (EditText) findViewById(R.id.edt_phone);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    private void setData(){
        if (person.isInitialized()){
            edtName.setText(person.getName());
            edtEmail.setText(person.getEmail());
            edtPhone.setText(person.getPhone(0).getNumber());
        }

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                break;
            case R.id.btn_submit:
                submitNewData();
                break;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private void submitNewData(){
        person = person.toBuilder()
                .setName(edtName.getText().toString())
                .setEmail(edtEmail.getText().toString())
                .setPhone(0, Person.PhoneNumber.newBuilder().setNumber(edtPhone.getText().toString()))
                .build();

        Intent intentResult = new Intent(this, MainActivity.class);
        intentResult.putExtra(MainActivity.DATA_ARR_BYTE, person.toByteArray());
        setResult(RESULT_OK, intentResult);
    }
}
