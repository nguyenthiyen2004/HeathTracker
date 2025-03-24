package com.example.healthapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.healthapp.models.ExerciseData;
import com.example.healthapp.models.HeartRateData;
import com.example.healthapp.models.SleepData;
import com.example.healthapp.models.StepData;
import com.example.healthapp.models.WaterData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "health_app.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_STEPS = "steps";
    private static final String TABLE_HEART_RATE = "heart_rate";
    private static final String TABLE_SLEEP = "sleep";
    private static final String TABLE_WATER = "water";
    private static final String TABLE_EXERCISE = "exercise";

    // Common columns
    private static final String KEY_ID = "id";
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_TIMESTAMP = "timestamp";

    // Steps table columns
    private static final String KEY_STEPS = "steps";

    // Heart rate table columns
    private static final String KEY_HEART_RATE = "heart_rate";

    // Sleep table columns
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_QUALITY = "quality";

    // Water table columns
    private static final String KEY_AMOUNT = "amount";

    // Exercise table columns
    private static final String KEY_EXERCISE_TYPE = "exercise_type";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_DURATION_MINUTES = "duration_minutes";

    // Create tables SQL
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + TABLE_STEPS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DAY + " INTEGER,"
            + KEY_MONTH + " INTEGER,"
            + KEY_YEAR + " INTEGER,"
            + KEY_STEPS + " INTEGER,"
            + KEY_TIMESTAMP + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_HEART_RATE = "CREATE TABLE " + TABLE_HEART_RATE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HEART_RATE + " INTEGER,"
            + KEY_TIMESTAMP + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_SLEEP = "CREATE TABLE " + TABLE_SLEEP + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DAY + " INTEGER,"
            + KEY_MONTH + " INTEGER,"
            + KEY_YEAR + " INTEGER,"
            + KEY_START_TIME + " INTEGER,"
            + KEY_END_TIME + " INTEGER,"
            + KEY_DURATION + " INTEGER,"
            + KEY_QUALITY + " TEXT"
            + ")";

    private static final String CREATE_TABLE_WATER = "CREATE TABLE " + TABLE_WATER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DAY + " INTEGER,"
            + KEY_MONTH + " INTEGER,"
            + KEY_YEAR + " INTEGER,"
            + KEY_AMOUNT + " INTEGER,"
            + KEY_TIMESTAMP + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_EXERCISE = "CREATE TABLE " + TABLE_EXERCISE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DAY + " INTEGER,"
            + KEY