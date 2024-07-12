package com.example.my_library;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Data.DatabaseHelper;

public class UpdateBookActivity extends AppCompatActivity {

    //Database identification, text editing fields, buttons and rows
    DatabaseHelper db;
    EditText title_input, author_input, publisher_input, published_date_input;
    Button update_button, delete_button, back_button;
    String id, title, author, publisher, published_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        title_input = findViewById(R.id.title_input_second);
        author_input = findViewById(R.id.author_input_second);
        publisher_input = findViewById(R.id.publisher_input_second);
        published_date_input = findViewById(R.id.published_date_input_second);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete);
        back_button = findViewById(R.id.Comeback);

        //First we call
        getAndSetIntentData();

        //Set the title of the action bar after the getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = DatabaseHelper.getInstance(UpdateBookActivity.this);
                title = title_input.getText().toString().trim();
                author = author_input.getText().toString().trim();
                publisher = publisher_input.getText().toString().trim();
                published_date = published_date_input.getText().toString().trim();
                db.updateBookData(id, title, author, publisher, published_date);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("publisher") && getIntent().hasExtra("published_date")){
            //Receiving data from intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            publisher = getIntent().getStringExtra("publisher");
            published_date = getIntent().getStringExtra("published_date");
            //setting data for intent
            title_input.setText(title);
            author_input.setText(author);
            publisher_input.setText(publisher);
            published_date_input.setText(published_date);
            Log.d("stev", title+" "+author+" "+publisher+ " "+ published_date);
        }else{
            Toast.makeText(this, "No books.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete book " + title + " ?");
        builder.setMessage("Are you sure you want to delete the book" + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = DatabaseHelper.getInstance(UpdateBookActivity.this);
                db.deleteOneBook(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
