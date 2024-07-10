package Data;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.my_library.R;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "MyLibrary.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_library";
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "book_title";
    private static final String KEY_AUTHOR = "book_author";
    private static final String KEY_PUBLISHER = "book_publisher";
    private static final String KEY_PUBLISHED_DATE = "book_published_date";

    DatabaseHelper databaseHelper;
    private static DatabaseHelper sInstance = null;
    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Create a table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT, " +
                KEY_AUTHOR + " TEXT, " +
                KEY_PUBLISHER + " TEXT, " +
                KEY_PUBLISHED_DATE + " INTEGER);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    public void addBook(String title, String author, String publisher, int published_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //catch the exception try/catch
        try {
            cv.put(KEY_TITLE, title);
            cv.put(KEY_AUTHOR, author);
            cv.put(KEY_PUBLISHER, publisher);
            cv.put(KEY_PUBLISHED_DATE, published_date);
            long result = db.insert(TABLE_NAME, null, cv);

            if (result == -1) {
                //Pop-up message for a short period of time, refer to the string resource file
                Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add book to database");
        }
    }


    //To read all books
    public Cursor readAllBooks(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    //To update book information
    public void updateBookData(String row_id, String title, String author, String publisher, String published_date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_AUTHOR, author);
        cv.put(KEY_PUBLISHER, publisher);
        cv.put(KEY_PUBLISHED_DATE, published_date);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, R.string.success_update, Toast.LENGTH_SHORT).show();
        }

    }

    //To delete one book
    public void deleteOneBook(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, R.string.failed_delete, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, R.string.success_update, Toast.LENGTH_SHORT).show();
        }
    }

    //To delete all books
    public void deleteAllBooks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
