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
    // Определим переменную типа кнопка и изображение.
    Button camera_open_id;
    ImageView click_image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // По идентификатору мы можем получить каждый компонент, идентификатор которого присвоен в XML-файле, получить кнопки и изображение.
        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        publisher_input = findViewById(R.id.publisher_input);
        published_date_input = findViewById(R.id.published_date_input);
        add_button = findViewById(R.id.add_button);
        backToMain = findViewById(R.id.backToMain);
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);

        db = DatabaseHelper.getInstance(this.getApplicationContext());
        //Добавляем слушатель при нажатии на кнопку add_button (добавиь книгу)
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = title_input.getText().toString().trim();
                String author = author_input.getText().toString().trim();
                String publisher = publisher_input.getText().toString().trim();
                String published_date = published_date_input.getText().toString();

                int pubdate = 0;
                try {
                    //Задаем проверки на пустоту
                    if (author.isEmpty() && publisher.isEmpty() && title.isEmpty() && published_date.isEmpty()) {
                        //Всплывающее окно, если в строках пусто
                        Toast.makeText(getApplicationContext(), R.string.warningFillFields, Toast.LENGTH_SHORT).show();
                        return;
                        //Если дата равна нулю, то нужно проверить дату
                    } else if (pubdate == Integer.parseInt(published_date)) {
                        Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                        return;
                        //Есои длина даты короче 2 цифр, то нужно проверить правильность ввода даты
                    } else if (published_date.length() != 4) {
                        Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                        return;
                        //Если год больше, чем текущий, нужно проверить дату
                    } else if (Integer.parseInt(published_date) > Calendar.getInstance().get(Calendar.YEAR)) {
                        Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    published_date = ""; // это приведет к сбою проверки параметров заказанного количества и всплывающему сообщению
                    Toast.makeText(getApplicationContext(), R.string.check_the_date, Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addBook(title, author, publisher, Integer.parseInt(published_date));
                //Если все хорошо, то во всплывающем окне будет надпись об успешности добавления книги
                Toast.makeText(getApplicationContext(), R.string.book_added, Toast.LENGTH_SHORT).show();
                //Сделаем строки после добавления книги пустыми
                title_input.setText("");
                author_input.setText("");
                publisher_input.setText("");
                published_date_input.setText("");
            }
        });
        //Кнопка для возврата на главную страницу, пропишем слушатель
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        // Кнопка Camera_open предназначена для открытия камеры и добавления setOnClickListener в эту кнопку.
        camera_open_id.setOnClickListener(v -> {
//            // Создадим намерение камеры ACTION_IMAGE_CAPTURE, оно откроет камеру для захвата изображения.
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Запустим действие с помощью camera_intent и запросим идентификатор изображения.
            startActivityForResult(camera_intent, pic_id);
        });
    }

    @Override
//    // Этот метод поможет получить изображение
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//        // Сопоставим идентификатор изображения запроса с кодом запроса.
//
        if (requestCode == pic_id) {
//            // BitMap — это структура данных файла изображения, которая хранит изображение в памяти.
            Bitmap photo = (Bitmap) intent.getExtras().get("data");
//            // Установим изображение в imageview для отображения
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