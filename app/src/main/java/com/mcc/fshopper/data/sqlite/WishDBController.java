package com.mcc.fshopper.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mcc.fshopper.model.WishItem;
import java.util.ArrayList;

public class WishDBController {

    private DatabaseHelper dbHelper;
    private Context mContext;
    private SQLiteDatabase database;

    public WishDBController(Context context) {
        mContext = context;
    }

    public WishDBController open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }

    public long insertWishItem(String productId, String name, String images, String price, float ratting, int orderCount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.KEY_PRODUCT_ID, productId);
        contentValue.put(DatabaseHelper.KEY_NAME, name);
        contentValue.put(DatabaseHelper.KEY_IMAGES, images);
        contentValue.put(DatabaseHelper.KEY_PRICE, price);
        contentValue.put(DatabaseHelper.KEY_LIKE_COUNT, orderCount);

        return database.insert(DatabaseHelper.TABLE_WISH, null, contentValue);
    }

    public ArrayList<WishItem> getAllWishData()
    {
        ArrayList<WishItem> wishList = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_WISH, null);
        if (cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {

                    String productId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PRODUCT_ID));
                    String name   = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NAME));
                    String images = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_IMAGES));
                    String price   = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_PRICE));
                    int likeCount= cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_LIKE_COUNT));

                    if (!productId.isEmpty()) {
                        wishList.add(new WishItem(productId, name, images, price, likeCount));
                    }
                    cursor.moveToNext();
                }
            } catch (Exception ex) {
            }
        }
        return wishList;
    }

    public int updateWishItem(long id, String productId, String name, String images, float price, float ratting, int likeCount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_PRODUCT_ID, productId);
        contentValues.put(DatabaseHelper.KEY_NAME, name);
        contentValues.put(DatabaseHelper.KEY_IMAGES, images);
        contentValues.put(DatabaseHelper.KEY_PRICE, price);
        contentValues.put(DatabaseHelper.KEY_LIKE_COUNT, likeCount);

        int updateStatus = database.update(DatabaseHelper.TABLE_WISH, contentValues,
                DatabaseHelper.KEY_ID + " = " + id, null);
        return updateStatus;
    }

    public void deleteWishItemById(String productId) {
        database.delete(DatabaseHelper.TABLE_WISH, DatabaseHelper.KEY_PRODUCT_ID + "=\"" + productId + "\"", null);
    }
    public boolean isAlreadyWished(String productId) {
        Cursor cursor = database.rawQuery("select "+DatabaseHelper.KEY_PRODUCT_ID+" from " + DatabaseHelper.TABLE_WISH + " where " + DatabaseHelper.KEY_PRODUCT_ID + "=\"" + productId + "\"", null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public void deleteAllWishData() {
        database.delete(DatabaseHelper.TABLE_WISH, null, null);
    }

    public int countWishProduct(){
        int numOfRows = (int) DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_WISH);
        return numOfRows;
    }

    private void dropWishTable() {
        String sql = "drop table " + DatabaseHelper.TABLE_WISH;
        try {
            database.execSQL(sql);
        } catch (SQLException e) {

        }
    }
//    public class CustomComparator implements Comparator<ProductResponseModel> {
//        @Override
//        public int compare(ProductResponseModel p1, ProductResponseModel p2) {
//            Long t1 = p1.postTimeStamp;
//            Long t2 = p2.postTimeStamp;
//            return t1.compareTo(t2);
//        }
//    }
}
