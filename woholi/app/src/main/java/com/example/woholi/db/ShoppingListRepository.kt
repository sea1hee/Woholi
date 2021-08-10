package com.example.woholi.db

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woholi.model.CheckListCategory
import com.example.woholi.model.CheckListItem
import com.example.woholi.model.CurrentUser
import com.example.woholi.model.Diary
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShoppingListRepository {


    suspend fun readShoppingList(): List<CheckListCategory>{
        val corou = CoroutineScope(Dispatchers.IO).async {
            val shoppingList :MutableList<CheckListCategory> = mutableListOf()
            val deferred = readRoutine().await().documents
            if(deferred != null) {
                for ( document in deferred){
                    val newShoppingList = CheckListCategory()
                    newShoppingList.title = document.id
                    val deffered2 = readRoutine2(document.id).await().documents
                    for (document2 in deffered2) {
                        newShoppingList.checkListItems.add(
                                CheckListItem(
                                        document2.id,
                                        document2.data?.get("isChecked").toString().toBoolean()
                                )
                        )
                    }
                    shoppingList.add(newShoppingList)
                }
            }
            shoppingList
        }

        return corou.await()

    }
    suspend fun readRoutine(): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("shoppinglist").collection("first")
                .get()
    }

    suspend fun readRoutine2(documentName: String): Task<QuerySnapshot> {
        return Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("shoppinglist").collection("first")
                .document(documentName).collection("second").get()
    }

    fun writeNewCategory(title:String){

    }


    fun writeNewContents(title:String, content:String){
        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("shoppinglist").collection("first")
                .document(title).collection("second")
                .document(content)
                .set(hashMapOf("isChecked" to false))
    }


    fun updateCheckBox(title:String, text:String, isChecked: Boolean){
        Firebase.firestore.collection("users").document(CurrentUser.uid)
                .collection("checklist").document("shoppinglist").collection("first")
                .document(title).collection("second")
                .document(text)
                .update("isChecked", isChecked)
    }
}