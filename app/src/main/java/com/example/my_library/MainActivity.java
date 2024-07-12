//importing classes from other packages whose functionality is used in MainActivity
package com.example.my_library;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import Data.DatabaseHelper;

//Class Definition
/*MainActivity inherits from the AppCompatActivity class, which is connected above using the directive
import. The AppCompatActivity class essentially represents a single screen (page) of an application or its
visual interface. And MainActivity inherits all this functionality
 */
public class MainActivity extends AppCompatActivity {
    //Definition of variables, buttons, images, adapter for Recycler View, and text fields
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_books;
    DatabaseHelper db;
    ArrayList<String> book_id, book_title, book_author, book_publisher, book_published_date;
    BookAdapter bookAdapter;

    //MainActivity contains only one method onCreate(), in which the entire application interface is actually created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The GUI layout resource is passed to the setContentView() method
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_books = findViewById(R.id.no_data);
        //Add a listener when the user clicks on the add_button button (add a book)
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to move from the current main activity to the activity AddBookActivity
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                // start of activity
                startActivity(intent);
            }
        });

        db = DatabaseHelper.getInstance(MainActivity.this);
        //Creating lists
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_publisher = new ArrayList<>();
        book_published_date = new ArrayList<>();

        storeDataInArrays();

        // create an adapter
        bookAdapter = new BookAdapter(MainActivity.this,this, book_id, book_title, book_author,
                book_publisher, book_published_date);
        // install an adapter for the list
        recyclerView.setAdapter(bookAdapter);
        //LinearLayoutManager: arranges items in a single-column list
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllBooks();
        if(cursor.getCount() == 0){
            //The picture and text that there are no books becomes visible if there are no books in the list
            empty_imageview.setVisibility(View.VISIBLE);
            no_books.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_publisher.add(cursor.getString(3));
                book_published_date.add(cursor.getString(4));
            }
            // The picture and TextView about the absence of books becomes invisible if there are books in the list
            empty_imageview.setVisibility(View.GONE);
            no_books.setVisibility(View.GONE);
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all_books){
            //Call a dialog to confirm
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    //Alert dialog for deleting all books
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all books?");
        builder.setMessage("Are you sure you want to delete all Books?");
        //For a positive answer
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = DatabaseHelper.getInstance(MainActivity.this);
                db.deleteAllBooks();
                //Refresh Activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //For a negative answer
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
