package fyp.sbarcoe;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager 
{
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_STOP_NAME= "StopName";
	public static final String KEY_LINE= "Line";
	public static final String KEY_STOP_ZONE= "StopZone";
	
	public static final String KEY_EMAIL= "Email";
	public static final String KEY_PASSWORD= "Password";
	public static final String KEY_BALANCE= "Balance";
	
	private static final String DATABASE_NAME = "LeapMe";
	static final String DATABASE_TABLE_LUAS = "LuasStop";
	static final String DATABASE_TABLE_USER = "User";
	
	static final String DATABASE_TABLE_DART = "DartStop";
	public static final String KEY_DART_STOP= "StopName";

	
	private static final int DATABASE_VERSION= 1;
	private static final String CREATE_DATABASE = "CREATE TABLE " + DATABASE_TABLE_LUAS + " (" +
            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		    KEY_STOP_NAME + " TEXT NOT NULL, " +	
			KEY_LINE + " TEXT NOT NULL, "+
	        KEY_STOP_ZONE + " INTEGER NOT NULL);" ;	

	private static final String CREATE_USER_TABLE = "CREATE TABLE " +DATABASE_TABLE_USER+ " (" +
	KEY_EMAIL+ " STRING PRIMARY KEY, " +
	KEY_PASSWORD + " TEXT NOT NULL, "+
	KEY_BALANCE + " TEXT NULL); ";
	
	private static final String CREATE_DART_TABLE = "CREATE TABLE " +DATABASE_TABLE_DART+ " (" +
            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_DART_STOP+ " TEXT NOT NULL, " +
	        KEY_STOP_ZONE + " INTEGER NOT NULL);" ;	
	
	private DbHelper myHelper;
	private final Context myContext ;
	private static SQLiteDatabase myDB;
	
	private static class DbHelper extends SQLiteOpenHelper
	{

		public DbHelper(Context context) 
		{			
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(CREATE_DATABASE);
			db.execSQL(CREATE_USER_TABLE);
			db.execSQL(CREATE_DART_TABLE);
			System.out.println("Database Created!") ;
			Log.w("myApp", "no network");				
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			// To Upgrade Database
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LUAS);
			onCreate(db);			
		}		
	}
	
	public DBManager(Context c)
	{
		myContext = c;
	}
	
	public DBManager open()
	{
		myHelper = new DbHelper(myContext);
		myDB = myHelper.getWritableDatabase();		
		return this;
	}
	
	public void close()
	{
		myHelper.close(); // To close DbHelper
	}
	
	public long addLuasStops(String stopName, String line, int zone)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_STOP_NAME, stopName);
		cv.put(KEY_LINE, line);
		cv.put(KEY_STOP_ZONE, zone);
		return myDB.insert(DATABASE_TABLE_LUAS, null, cv);			
	}
	public long addDartStops(String stopName, int zone)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_DART_STOP, stopName);
		cv.put(KEY_STOP_ZONE, zone);
		return myDB.insert(DATABASE_TABLE_DART, null, cv);			
	}
	public String[] getLuasStops(String line) 
	{
		String[] columns = new String[]{KEY_STOP_NAME};
		Cursor c = myDB.query(DATABASE_TABLE_LUAS, columns, "Line"+" LIKE '"+line+"%'", null, null, null, null);
		ArrayList<String> stopNames = new ArrayList<String>();
		while(c.moveToNext())
		        {			
			        stopNames.add(c.getString(0));
		        }
		String[] stopsReturn = (String[]) stopNames.toArray(new String[stopNames.size()]);

		return stopsReturn;
		
	}
	public String[] getDartStops() 
	{
		String[] columns = new String[]{KEY_DART_STOP};
		Cursor c = myDB.query(DATABASE_TABLE_DART, columns, null, null, null, null, null);
		ArrayList<String> stopNames = new ArrayList<String>();
		while(c.moveToNext())
		{			
			stopNames.add(c.getString(0));
		}
		String[] stopsReturn = (String[]) stopNames.toArray(new String[stopNames.size()]);
		
		return stopsReturn;
		
	}
	public String[] getBusStops(String line) 
	{
		String[] columns = new String[]{KEY_STOP_NAME};
		Cursor c = myDB.query(DATABASE_TABLE_LUAS, columns, "Line"+" LIKE '"+line+"%'", null, null, null, null);
		ArrayList<String> stopNames = new ArrayList<String>();
		while(c.moveToNext())
		        {			
			        stopNames.add(c.getString(0));
		        }
		String[] stopsReturn = (String[]) stopNames.toArray(new String[stopNames.size()]);

		return stopsReturn;
		
	}
	public String getLuasZone(String stopName) 
	{
		String selectQuery = "SELECT StopZone FROM LuasStop WHERE StopName = '" +stopName+ "'" ;
		Cursor c = myDB.rawQuery(selectQuery, null);
		String returnZone = "" ;
		while(c.moveToNext())
		{
			returnZone = c.getString(0);
		}
		return returnZone;
		
	}
	public String getDartZone(String stopName) 
	{
		String selectQuery = "SELECT StopZone FROM DartStop WHERE StopName = '" +stopName+ "'" ;
		Cursor c = myDB.rawQuery(selectQuery, null);
		String returnZone = "" ;
		while(c.moveToNext())
		{
			returnZone = c.getString(0);
		}
		return returnZone;
		
	}
	public String getBusZone(String stopName) 
	{
		String selectQuery = "SELECT StopZone FROM BusStop WHERE StopName = '" +stopName+ "'" ;
		Cursor c = myDB.rawQuery(selectQuery, null);
		String returnZone = "" ;
		while(c.moveToNext())
		{
			returnZone = c.getString(0);
		}
		return returnZone;
		
	}
	public String getEmail()
	{		
		String selectQuery = "SELECT Email FROM User";
		Cursor c = myDB.rawQuery(selectQuery, null);
		String rTitle = "" ;
		while(c.moveToNext())
		{
			rTitle = c.getString(0);
		}
		return rTitle;
	}

	public void insertLocalUser(String email, String password) 
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_EMAIL, email);
		cv.put(KEY_PASSWORD, password);
		myDB.insert(DATABASE_TABLE_USER, null, cv);	
	}	
}  

