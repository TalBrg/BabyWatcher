package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseContacts extends AppCompatActivity {

    Button button ;
    TextView name,number;
    public  static final int RequestPermissionCode  = 1 ;
    public SharedPreferences.Editor editor;
    SharedPreferences pref;


    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    ArrayList<String> selected = new ArrayList<String>();
    int num_of_contacts;

    Intent intent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        num_of_contacts = pref.getInt("num_of_contacts", 0);

        for (int i = 0; i < num_of_contacts; i++){
            selected.add(pref.getString("name" + (i + 1), "")  + " : " + pref.getString("num" + (i + 1), ""));
        }

        button = findViewById(R.id.button);
        if (button == null){
            return;
        }
        number = (TextView)findViewById(R.id.textView);

        // Find the ListView resource.
        mainListView =  findViewById( R.id.mainListView );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, selected);

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );


        EnableRuntimePermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);

            }
        });

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                listAdapter.remove(listAdapter.getItem(arg2));
                editor.remove("num" + arg2);
                String currNum;
                String currName;
                for (int i = arg2 + 1; i <= num_of_contacts; i++){
                    currNum = pref.getString("num" + i, "");
                    currName = pref.getString("name" + i, "");
                    editor.remove("num" + i);
                    editor.remove("name" + i);
                    editor.putString("num" + (i-1), currNum);
                    editor.putString("name" + (i-1), currName);

                }
                num_of_contacts--;
                editor.putInt("num_of_contacts", num_of_contacts);
                editor.apply();
            }

        });
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ChooseContacts.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(ChooseContacts.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ChooseContacts.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            TempNumberHolder = "";

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            }
                            listAdapter.add(TempNameHolder + " : " + TempNumberHolder);
                            num_of_contacts++;
                            editor.putString("num" + num_of_contacts, TempNumberHolder);
                            editor.putString("name" + num_of_contacts, TempNameHolder);

                            editor.putInt("num_of_contacts", num_of_contacts);
                            editor.apply();
                        }

                    }
                }
                break;
        }
    }
}