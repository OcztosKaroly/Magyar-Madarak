package com.example.magyar_madarak.data.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.magyar_madarak.data.dao.BirdDAO;
import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.utils.ConverterUtils;

import java.util.Arrays;

@Database(entities = {Bird.class}, version = 1, exportSchema = false)
@TypeConverters({ConverterUtils.class})
public abstract class BirdRoomDatabase extends RoomDatabase {
    public abstract BirdDAO birdDao();

    private static BirdRoomDatabase instance;

    public static BirdRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (BirdRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    BirdRoomDatabase.class,
                                    "bird_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(populationCallback)
                            .build();
                }
            }
        }
        return instance;
    }

//    Ehhez a megvalósításhoz meg kell valósítani az insert műveletet.
//    Talán ez lehet a jobb megoldás, mert ilyenkor lehetne olyat csinálni, amikor van internethozzáférés,
//          akkor ha módosult a firebase adatbázis, akkor itt is módosítjuk a RoomDatabase-t.
//          Ezzel naprakészen tartva a lokális adatbázist.
    private static RoomDatabase.Callback populationCallback = new RoomDatabase.Callback() {
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitDB(instance).execute();
        }
    };

    private static class InitDB extends AsyncTask<Void, Void, Void> {
        private BirdDAO birdDAO;

        InitDB(@NonNull BirdRoomDatabase db) {
            birdDAO = db.birdDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("DATABASE", "--Insert into database???: " + Arrays.toString(voids));
//            birdDAO.insert(new Bird("...", "0."));
//            birdDAO.insert(new Bird("...", "1."));
//            birdDAO.insert(new Bird("...", "2."));

            return null;
        }
    }
}
