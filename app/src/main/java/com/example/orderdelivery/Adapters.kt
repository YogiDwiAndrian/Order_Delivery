package com.example.orderdelivery

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lo_customer_update.view.*
import kotlinx.android.synthetic.main.lo_customers.view.*

class CustomerAdapter(mCtx : Context, val customers : ArrayList<Customer>) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>(){

    val mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtCustomerName = itemView.txtCustomerName
        val txtMaxCredit = itemView.txtMaxCredit
        val txtDateItem = itemView.txtDateItem
        val btnUpdate = itemView.btnUpdate
        val btnDelete = itemView.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lo_customers, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: CustomerAdapter.ViewHolder, position: Int) {
        val customer : Customer = customers[position]
        holder.txtCustomerName.text = customer.CustomerName
        holder.txtMaxCredit.text = customer.maxcredit.toString()
        holder.txtDateItem.text = customer.DateItem


        holder.btnDelete.setOnClickListener{
            val customerName = customer.CustomerName

            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are You Sure to Delete : $customerName ?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteCustomer(customer.CustomerID)){
                        customers.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, customers.size)
                        Toast.makeText(mCtx, "Item $customerName Deleted", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(mCtx, "Error Deleting", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.warning_icon)
                .show()
        }

        holder.btnUpdate.setOnClickListener{


            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.lo_customer_update, null)

            val txtCustName : TextView = view.findViewById(R.id.editUpCustomerName)
            val txtMaxCredit : TextView = view.findViewById(R.id.editUpMaxCredit)
            val txtDate : TextView = view.findViewById(R.id.editUpDate)

            txtCustName.text = customer.CustomerName
            txtMaxCredit.text = customer.maxcredit.toString()
            txtDate.text = customer.DateItem


            val builder = AlertDialog.Builder(mCtx)
                .setTitle("Update Item Info.")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    val isUpdate = MainActivity.dbHandler.updateCustomer(
                        customer.CustomerID.toString(),
                        view.editUpCustomerName.text.toString(),
                        view.editUpMaxCredit.text.toString(),
                        view.editUpDate.text.toString()
                    )
                    if (isUpdate == true){
                        customers[position].CustomerName = view.editUpCustomerName.text.toString()
                        customers[position].maxcredit = view.editUpMaxCredit.text.toString().toInt()
                        customers[position].DateItem = view.editUpDate.text.toString()
                        notifyDataSetChanged()
                        Toast.makeText(mCtx, "Updated Succesfull", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(mCtx, "Error Updating", Toast.LENGTH_SHORT).show()
                    }
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                })
            val alert = builder.create()
            alert.show()
        }
    }

}