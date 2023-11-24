package com.example.motherload

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import android.util.Log
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Connection private constructor() {
    private val TAG = "Connection"
    private val BASE_URL = " https://test.vautard.fr/creuse_srv/"

    companion object {
        @Volatile
        private var INSTANCE: Connection? = null

        fun getInstance(): Connection {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Connection().also { INSTANCE = it }
            }
        }
    }
    fun ConectWeb (Login: String, Password: String) {
        val url = BASE_URL + "/connexion.php"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG,"ajouteMessage: Message ajouté avec succès!")
                            // ajouter fonction pour se log
                        } else {
                            Log.e(TAG, "ajouteMessage: Erreur - $status")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG,"Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG,"ajouteMessage error")
                error.printStackTrace()
            })
    }
}