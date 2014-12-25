package com.br.soucausa.data;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.br.soucausa.util.*;
public class DataContract extends SQLiteOpenHelper {

	
	public static final int DATABASE_VERSION = 8;
	public static final String DATABASE_NAME = "soucausa.db";

	private static final String TEXT_TYPE = " TEXT";
	private static final String FLOAT_TYPE = " FLOAT";
	private static final String COMMA_SEP = ",";
	
    private static final String SQL_CREATE_ONG =
        "CREATE TABLE " + OngContract.TABLE_NAME + " (" +
        OngContract.COLUMN_NAME_ONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        OngContract.COLUMN_NAME_RAZAO_SOCIAL + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_NOME_FANTASIA + TEXT_TYPE + COMMA_SEP +     
        OngContract.COLUMN_NAME_CNPJ + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_RUA + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_NUMERO + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_COMPLEMENTO + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_EXPEDIENTE + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_CIDADE + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_UF + TEXT_TYPE + COMMA_SEP +     
        OngContract.COLUMN_NAME_BAIRRO + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_CEP + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_TELEFONE + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_FAX + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_SITE + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_DATA_FUNDACAO + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_CRCE + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_PUBLICO_ALVO + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_AREA_ATUACAO + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_EQUIPE + TEXT_TYPE + COMMA_SEP +
        OngContract.COLUMN_NAME_HABILITADA + " INTEGER "  +
        " );";
    
    private static final String SQL_CREATE_DOACOES = "CREATE TABLE IF NOT EXISTS DOACAO (" +
    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
    "CNPJ TEXT," +
    "COO TEXT," +
    "valor TEXT," +
    "data INTEGER,"+
    "causa_id INTEGER,"+
    "path TEXT,"+
    "status INTEGER"+
    ");";
	
    
	private static final String SQL_DELETE_DOACAO =
		    "DROP TABLE IF EXISTS DOACAO";
	
	public DataContract(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_DOACOES);
	}
	
	public boolean doesDatabaseExist(ContextWrapper context) {
		    File dbFile = context.getDatabasePath(DATABASE_NAME);
		    return dbFile.exists();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_DOACAO);
		this.onCreate(db);
	}

}
