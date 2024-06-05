package com.example.ominformatics2.DataSource;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DbOrderModel.class}, version = 1,exportSchema = false)
public abstract class OmInformaticsDatabase extends RoomDatabase {

    private static OmInformaticsDatabase instance;
    public abstract OrderDao dao();

    public static synchronized OmInformaticsDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, OmInformaticsDatabase.class, "OmInformatics.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
