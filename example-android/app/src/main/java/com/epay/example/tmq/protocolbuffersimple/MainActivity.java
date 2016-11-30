package com.epay.example.tmq.protocolbuffersimple;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_EDIT_DATA = 12;
    public static final String DATA_ARR_BYTE = "data_arr_byte";

    private AddressBookProtos.Person person;

    private TextView tvPeopleInfo;

    private Button btnEditData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setData();
    }

    private void initViews(){
        tvPeopleInfo = (TextView) findViewById(R.id.tv_people_info);

        Button btnSaveFile = (Button) findViewById(R.id.btn_save_file);
//        Button btnReadFile = (Button) findViewById(R.id.btn_read_file);
        btnEditData = (Button) findViewById(R.id.btn_edit_data);

        btnSaveFile.setOnClickListener(this);
        btnEditData.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save_file:
                try {
                    writeFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
//            case R.id.btn_read_file:
//                break;
            case R.id.btn_edit_data:
                Intent mIntent = new Intent();
                mIntent.setClass(this, SecondActivity.class);
                mIntent.putExtra(DATA_ARR_BYTE, person.toByteArray());
                startActivityForResult(mIntent, REQ_EDIT_DATA);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode){
            case REQ_EDIT_DATA:
                Toast.makeText(MainActivity.this, "Result", Toast.LENGTH_SHORT).show();
                try {
                    person = AddressBookProtos.Person.parseFrom(data.getByteArrayExtra(MainActivity.DATA_ARR_BYTE))
                            .toBuilder()
                            .build();
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "ToString = " + person.toString());
                Log.i(TAG, "Array = " + person.toByteArray().toString());

                tvPeopleInfo.setText(person.getName() + "\n" + person.getEmail() + "\n" + person.getPhone(0).getNumber());
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setData(){
        person = AddressBookProtos.Person.newBuilder()
                .setId(11111)
                .setName("Tran Minh Quy")
                .setEmail("minhquylt95@gmail.com")
                .addPhone(
                        AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("0169 322 7489")
                                .setType(AddressBookProtos.Person.PhoneType.MOBILE))
                .build();

        if (person.isInitialized()) {
            Log.i(TAG, "Person is Initialized!");
            Log.i(TAG, "ToString = " + person.toString());
            Log.i(TAG, "Array = " + person.toByteArray().toString());

        }

        tvPeopleInfo.setText(person.getName() + "\n" + person.getEmail() + "\n" + person.getPhone(0).getNumber());
    }

    private void writeFile() throws IOException {
        Log.i(TAG, "Array = " + Arrays.toString(person.toByteArray()));
        Log.i(TAG, "content = " + AddressBookProtos.Person.parseFrom(person.toByteArray()).getEmail());
        String root = Environment.getExternalStorageDirectory().toString();
        File file = new File(root + "/test.txt");
        Log.i(TAG, "path = " + file.getAbsolutePath());
        file.createNewFile();

        if (file.exists()) {
            OutputStream fo = new FileOutputStream(file);
            fo.write(person.toByteArray());
            fo.close();
            Toast.makeText(this, "Write complete", Toast.LENGTH_SHORT).show();
        }
    }

}
