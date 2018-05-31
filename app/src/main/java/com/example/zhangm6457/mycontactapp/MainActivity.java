package com.example.zhangm6457.mycontactapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editAddress;
    EditText editPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editAddress = findViewById(R.id.editText_address);
        editPhone = findViewById(R.id.editText_phone);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated myDb");
    }

    public void addData(View view){
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted = myDb.insertData(editName.getText().toString(), editAddress.getText().toString(), editPhone.getText().toString());

        if (isInserted == true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view){
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor");

        if (res.getCount() == 0){
            showMessage("Error", "No data found in database");
            Log.d("MyContactApp", "MainActivity: viewData: no data in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            for (int i = 0; i < 4; i++){
                buffer.append(res.getString(i));
                buffer.append("\n");
            }
            buffer.append("\n");
            Log.d("MyContactApp", "MainActivity: viewData: created StringBuffer");
            //Append res column 0,1,2,3 to the buffer - see Stringbuffer and Cursor's api's
            //Delimit each of the "appends" with line feed "\n"
            //four lines
        }
        showMessage("Data", buffer.toString());
    }

    private void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: showMessage: assembling AlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static final String EXTRA_MESSAGE = "com.example.zhangm6457.mycontactapp.MESSAGE";

    public void searchRecord(View view){
        Log.d("MyContactApp", "MainActivity: launching SearchActivity");
        Intent intent = new Intent(this, SearchActivity.class);

        //creating the StringBuffer to search
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: SearchRecord: received cursor");

        if (res.getCount() == 0){
            showMessage("Error", "No data found in database");
            Log.d("MyContactApp", "MainActivity: SearchRecord: no data in database");
            return;
        }

        StringBuffer buffer1 = new StringBuffer();

        boolean isTrue = false;
        while (res.moveToNext()) {
            if (res.getString(1).equals(editName.getText().toString())) {
                isTrue = true;
                buffer1.append(res.getString(1) + "\n");
                buffer1.append(res.getString(2) + "\n");
                buffer1.append(res.getString(3) + "\n" + "\n");
            }
        }
        if (isTrue == false){
            buffer1.append("Entry does not exist");
        }

        Log.d("MyContactApp", "MainActivity: SearchRecord: created StringBuffer");

        intent.putExtra(EXTRA_MESSAGE, buffer1.toString());
        startActivity(intent);
    }
}
