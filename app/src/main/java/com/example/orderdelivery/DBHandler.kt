package com.example.orderdelivery

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.lang.Exception


class DBHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION ){

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1

        val CUSTOMERS_TABLE_NAME = "Customers"
        val COLUMN_CUSTOMERID = "customerid"
        val COLUMN_CUSTOMERNAME = "customername"
        val COLUMN_MAXCREDIT = "maxcredit"
        val COLUMN_DATEITEM = "dateitem"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CUSTOMERS_TABLE = ("CREATE TABLE $CUSTOMERS_TABLE_NAME (" +
                "$COLUMN_CUSTOMERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CUSTOMERNAME TEXT," +
                "$COLUMN_DATEITEM TEXT," +
                "$COLUMN_MAXCREDIT DOUBLE DEFAULT 0)")
        db?.execSQL(CREATE_CUSTOMERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun getCustomers(mCtx : Context) : ArrayList<Customer>{
        val qry = "Select * From $CUSTOMERS_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val customers = ArrayList<Customer>()

        if (cursor.count == 0){
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        }else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast()){
                val customer = Customer()
                customer.CustomerID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMERID))
                customer.CustomerName = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMERNAME))
                customer.DateItem = cursor.getString(cursor.getColumnIndex(COLUMN_DATEITEM))
                customer.maxcredit = cursor.getInt(cursor.getColumnIndex(COLUMN_MAXCREDIT))
                customers.add(customer)
                cursor.moveToNext()
            }
            Toast.makeText(mCtx, "${cursor.count.toString()} Item Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return customers
    }

    fun addCustomer(mCtx: Context, customer: Customer){
        val values = ContentValues()
        values.put(COLUMN_CUSTOMERNAME, customer.CustomerName)
        values.put(COLUMN_DATEITEM, customer.DateItem)
        values.put(COLUMN_MAXCREDIT, customer.maxcredit)
        val db = this.writableDatabase
        try {
            db.insert(CUSTOMERS_TABLE_NAME, null, values)
            Toast.makeText(mCtx, "Item Added", Toast.LENGTH_SHORT).show()
        } catch (e : Exception){
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteCustomer(CustomerID : Int) : Boolean{
      //  val qry = "Delete Form $CUSTOMERS_TABLE_NAME where $COLUMN_CUSTOMERID = $CustomerID"
        val db = this.writableDatabase
        var result : Boolean = false
        try {
            val cursor = db.delete(CUSTOMERS_TABLE_NAME, "$COLUMN_CUSTOMERID = ?", arrayOf(CustomerID.toString()))
            //val cursor = db.execSQL(qry)
            result = true
        }catch (e : Exception){
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }

    fun updateCustomer(id: String, CustomerName: String, maxcredit : String, DateItem: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result : Boolean = false
        contentValues.put(COLUMN_CUSTOMERNAME, CustomerName)
        contentValues.put(COLUMN_MAXCREDIT, maxcredit)
        contentValues.put(COLUMN_DATEITEM, DateItem)
        try {
            db.update(CUSTOMERS_TABLE_NAME, contentValues, "$COLUMN_CUSTOMERID = ?", arrayOf(id))
            result = true
        }catch (e : Exception){
            Log.e(ContentValues.TAG, "Error Updating")
            result = false
        }
        return result

    }
}