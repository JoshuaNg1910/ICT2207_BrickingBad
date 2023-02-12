package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class retrieveContacts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button loadContacts;

    EditText editText1;
    EditText editText2;
    NavigationView navigationView;
    Toolbar toolbar;
    View header;
    DrawerLayout drawerLayout;
    TextView listContacts;
    RecyclerView recycleView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        recycleView = findViewById(R.id.recycler_view);
        checkPermission();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.contactToolbar);
        header = navigationView.getHeaderView(0);
  /*      listContacts = findViewById(R.id.listContacts);
        loadContacts = findViewById(R.id.loadContacts);
        loadContacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loadContacts();
            }
        });*/
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(retrieveContacts.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(retrieveContacts.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else {
            getContactList();
        }
    }

    @SuppressLint("Range")
    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        Cursor cursor = getContentResolver()
                .query(uri,null,null,null,sort);

        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String id =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name  = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                    ContactModel model = new ContactModel();
                    model.setName(name);
                    model.setNumber(number);
                    arrayList.add(model);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter mainAdapter = new MainAdapter(this, this.arrayList);
        adapter = mainAdapter;
        recycleView.setAdapter(mainAdapter);
    }

 //   @Override
 //   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults){
 //       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 //       if(requestCode == 1){
 //           if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
 //               loadContacts();
 //           }
 //       }
 //   }

/*    public void loadContacts(){
        StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (cursor2.moveToNext()) {
                        @SuppressLint("Range") String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        builder.append("Contact : ").append(name).append(", Phone Number : ").append(phoneNumber).append("\n\n");
                    }

                    cursor2.close();
                }
            }
        }
        cursor.close();

        listContacts.setText(builder.toString());
    }

    public void getNameButton(View view){
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(editText2.getText().toString()));
            Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

            String stringContactName = "INVALID";
            if (cursor != null){
                if (cursor.moveToFirst()){
                    stringContactName = cursor.getString(0);
                }
            }
            editText1.setText(stringContactName);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getNumberButton(View view){
        try {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE},
                    "DISPLAY_NAME = '" + editText1.getText().toString() + "'", null, null);

            cursor.moveToFirst();
            editText2.setText(cursor.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
            editText2.setText("NA");
        }
    }
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getContactList();
            return;
        }
        else{
            Toast.makeText(retrieveContacts.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_contact:
                Intent contact = new Intent(this, retrieveContacts.class);
                startActivity(contact);
                break;
            case R.id.nav_chat:
                break;
            case R.id.nav_profile:
                Intent profile = new Intent(this, profileActivity.class);
                startActivity(profile);
                break;
            case R.id.nav_aboutus:
                Intent aboutus = new Intent(this, aboutusActivity.class);
                startActivity(aboutus);
                break;
            case R.id.nav_logout:
                SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.clear();
                editor.commit();
                Intent logout = new Intent(this, HomeActivity.class);
                startActivity(logout);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
