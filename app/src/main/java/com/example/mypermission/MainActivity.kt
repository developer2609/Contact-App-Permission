package com.example.mypermission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mypermission.modern.MyButton
import com.example.mypermission.modern.MyButtonClicListener
import com.example.mypermission.modern.MySwipeHelper
import com.github.florent37.runtimepermission.kotlin.askPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*

class MainActivity : AppCompatActivity() {
     lateinit var rvAdapter: RvAdapter
    lateinit var contactList:ArrayList<Contact>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv.setHasFixedSize(true)
        val swipe=object:MySwipeHelper(this,rv,100){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
               buffer.add(
            MyButton(this@MainActivity,"Sms",50,R.drawable.ic_baseline_sms_24,Color.parseColor("#FFC107"),object :MyButtonClicListener{
                override fun onClik(position: Int) {
                    Toast.makeText(this@MainActivity, "$position", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this@MainActivity,SMSActivity::class.java)
                     intent.putExtra("key",contactList[position])
                     startActivity(intent)
                }
            })
               )

                buffer.add(

                    MyButton(this@MainActivity,"Call",30,R.drawable.ic_baseline_call_24,Color.parseColor("#FFF8CA2A"),object :MyButtonClicListener{
                        override fun onClik(position: Int) {
                            Toast.makeText(this@MainActivity, "$position", Toast.LENGTH_SHORT).show()
                                 telifonQilish(position)
//                             tel qilish funksiyasi

                        }
                    })
                )
            }

        }

        readContact()



         }

    private fun telifonQilish(position:Int){

        askPermission(Manifest.permission.CALL_PHONE){

            val phoneNumber=contactList[position].phone
            val intent=Intent(Intent(Intent.ACTION_CALL))
            intent.data=Uri.parse("tel:$phoneNumber")
              startActivity(intent)
        }.onDeclined { e->

         if (e.hasDenied()){
            AlertDialog.Builder(this)
                .setMessage(" if don't accept this apk, this program don't run  ")
                .setPositiveButton("yes"){
                 dialog,which->
                    e.askAgain();
                }
                .setNegativeButton("no"){
                    dialog,which->
                    dialog.dismiss()
                }
                .show()
         }
         if (e.hasForeverDenied()){

                     e.goToSettings()
         }


        }

    }








    @SuppressLint("Range")
    fun readContact(){
        contactList = ArrayList()
        askPermission(Manifest.permission.READ_CONTACTS){
            //all permissions already granted or just granted
            val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null)
            while (contacts!!.moveToNext()){
                val contact = Contact(
                    contacts!!.getString(contacts!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    contacts!!.getString(contacts!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )
//                val rasmUrl = contacts!!.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                contactList.add(contact)
                contactList.sortBy {  
                    it.name
                }
            }
            contacts!!.close()

            rvAdapter = RvAdapter(contactList)
            rv.adapter = rvAdapter
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }

}