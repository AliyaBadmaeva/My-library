package com.example.my_library;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Calendar;
import Data.DatabaseHelper;

public class AddBookActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText title_input, author_input, publisher_input, published_date_input;
    Button add_button;
    Button backToMain;
    private static final int pic_id = 123;
    // Let's define a variable of type button and image.
    Button camera_open_id;
    ImageView click_image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // Using identifier we can get each component whose identifier is assigned in the XML file, get buttons and an image.
        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        publisher_input = findViewById(R.id.publisher_input);
        published_date_input = findViewById(R.id.published_date_input);
        add_button = findViewById(R.id.add_button);
        backToMain = findViewById(R.id.backToMain);
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);

        db = DatabaseHelper.getInstance(this.getApplicationContext());
        //Add a listener when the user click on the add_button button (add a book)
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = title_input.getText().toString().trim();
                String author = author_input.getText().toString().trim();
                String publisher = publisher_input.getText().toString().trim();
                String published_date = published_date_input.getText().toString();

                int pubdate = 0;
                try {
                    //Checking for emptiness
                    if (author.isEmpty() && publisher.isEmpty() && title.isEmpty() && published_date.isEmpty()) {
                        //Popup window if rows are empty
                        Toast.makeText(getApplicationContext(), R.string.warningFillFields, Toast.LENGTH_SHORT).show();
                        return;
                        //If the date is null then the user needs to check the date
                    } else if (pubdate == Integer.parseInt(published_date)) {
                        Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                        return;
                        //If the date length is shorter than 2 digits, then the user needs to check that the date is entered correctly
                    } else if (published_date.length() != 4) {
                        Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                        return;
                        //If the year is greater than the current one, the user needs to check the date
                    } else if (Integer.parseInt(published_date) > Calendar.getInstance().get(Calendar.YEAR)) {
                        Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    published_date = ""; // this will result in a failure to check the parameters of the ordered quantity and a pop-up message
                    Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addBook(title, author, publisher, Integer.parseInt(published_date));
                //If all is well, a pop-up window will indicate that the book was added successfully.
                Toast.makeText(getApplicationContext(), R.string.book_added, Toast.LENGTH_SHORT).show();
                //Let's make the lines after adding a book empty
                title_input.setText("");
                author_input.setText("");
                publisher_input.setText("");
                published_date_input.setText("");
            }
        });
        //Button to return to the main page, let's add a listener here
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        // The Camera_open button is for opening the camera and adding a setOnClickListener to that button.
        camera_open_id.setOnClickListener(v -> {
//            // Let's create an ACTION_IMAGE_CAPTURE camera intent, which will open the camera to capture an image.
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Let's launch the action using camera_intent and request the image ID.
            startActivityForResult(camera_intent, pic_id);
        });
    }

    @Override
//    // This method will help us get an image
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//        // Let's match the request image identifier with the request code.
//
        if (requestCode == pic_id) {
//            // BitMap — It is an image file data structure that stores the image in memory.
            Bitmap photo = (Bitmap) intent.getExtras().get("data");
//            // Set the image to imageview for display
            click_image_id.setImageBitmap(photo);
        }

//    }
//        if (requestCode == 100 && resultCode == pic_id) {
//            Uri imageUri = intent.getData();
//            try {
//                assert imageUri != null;
//                InputStream inoutStream = getContentResolver().openInputStream(imageUri);
//
//                // BitMap is data structure of image file which store the image in memory
//                Bitmap bitmap = BitmapFactory.decodeStream(inoutStream);
//                // Set the image in imageview for display
//                click_image_id.setImageBitmap(bitmap);
//                imageContent = getImageBytes(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
    }
}
//            try {
//                Intent objectIntent = new Intent();
//                objectIntent.setType("image/*");
//                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(objectIntent, pic_id);
//            } catch (Exception e) {
//                Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == pic_id && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                imageFilePath = data.getData();
//                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
//                click_image_id.setImageBitmap(imageToStore);
//            }
//        }
//        catch (Exception e) {
//                Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//    }
