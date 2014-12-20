package com.br.soucausa.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class OngDataProvider extends ContentProvider {
	
	private DataContract ongDB;
	private static final String AUTHORITY = "com.br.data.OngDataProvider";

	private static final String BASE_PATH = "all";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH);

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		ongDB = new DataContract(getContext());
		Log.d("vini","ContentProvider onCreate");
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d("vini","ContentProvider query");

		 // Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    // Set the table
	    queryBuilder.setTables(OngContract.TABLE_NAME);


	    SQLiteDatabase db = ongDB.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection,
	        selectionArgs, null, null, sortOrder);
	    // make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
