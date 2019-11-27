package com.example.orderdelivery

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_customer.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.*
import android.widget.Toast


class AddCustomerActivity : AppCompatActivity() {

    var edDate : EditText? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)

        edDate = this.editDate

        //Create Date Listener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        edDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(this@AddCustomerActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        btnSave.setOnClickListener{
            val customer = Customer()
            if (editCustomerName.text.isEmpty()){
                Toast.makeText(this, "Enter Item Name", Toast.LENGTH_SHORT).show()
            }else if (editMaxCredit.text.isEmpty()){
                customer.maxcredit = 0
            }else if (editDate.text.isEmpty()){
                Toast.makeText(this, "Input Date", Toast.LENGTH_SHORT).show()
            }else {
                customer.CustomerName = editCustomerName.text.toString()
                customer.maxcredit = editMaxCredit.text.toString().toInt()
                customer.DateItem = edDate!!.text.toString()


                MainActivity.dbHandler.addCustomer(this, customer)
                ClearEdits()
                editCustomerName.requestFocus()
            }
        }

        btnCancel.setOnClickListener {
            ClearEdits()
            val ii = Intent(this, MainActivity::class.java)
            startActivity(ii)
        }
    }

    private fun ClearEdits(){
        editCustomerName.text.clear()
        editMaxCredit.text.clear()
    }

    private fun updateDateInView(){
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        edDate!!.setText(sdf.format(cal.getTime()))
    }

}
