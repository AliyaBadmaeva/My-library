//импорт классов из других пакетов, функциональность которых используется в MainActivity
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

//Определение класса
/*MainActivity наследуется от класса AppCompatActivity, который выше подключен с помощью директивы
импорта. Класс AppCompatActivity по сути представляет отдельный экран (страницу) приложения или его
визуальный интерфейс. И MainActivity наследует весь этот функционал
 */
public class MainActivity extends AppCompatActivity {
    //Определяем переменные, кнопки, изображения, адаптер для Recycler View, а также текстовые поля
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_books;
    DatabaseHelper db;
    ArrayList<String> book_id, book_title, book_author, book_publisher, book_published_date;
    BookAdapter bookAdapter;

    //MainActivity содержит только один метод onCreate(), в котором фактически и создается весь интерфейс приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //В метод setContentView() передается ресурс разметки графического интерфейса
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_books = findViewById(R.id.no_data);
        //Добавляем слушатель при нажатии на кнопку add_button (добавиь книгу)
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Создаем интент(намерение), чтобы перейти из текущей главной активность в активность AddBookActivity
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                // запуск activity
                startActivity(intent);
            }
        });

        db = DatabaseHelper.getInstance(MainActivity.this);
        //Создаем списки
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_publisher = new ArrayList<>();
        book_published_date = new ArrayList<>();

        storeDataInArrays();

        // создаем адаптер
        bookAdapter = new BookAdapter(MainActivity.this,this, book_id, book_title, book_author,
                book_publisher, book_published_date);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(bookAdapter);
        //LinearLayoutManager: упорядочивает элементы в виде списка с одной колонкой
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
            //Картинка и текст, что книг нет становится видимой, если нет книг в списке
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
            // Картинка и TextView об отсуствии книг сттановится невидимой, если книги в списке есть
            empty_imageview.setVisibility(View.GONE);
            no_books.setVisibility(View.GONE);
        }
    }

    //Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all_books){
            //Вызываем диалог для подтверждения
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    //Алертный диалог для удаления всех книг
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all books?");
        builder.setMessage("Are you sure you want to delete all Books?");
        //Для положительного ответа
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
        //Для отрицательного ответа
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}