package com.shivam_raj.circletalk.screens.chat.chatScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivam_raj.circletalk.server.Server
import com.shivam_raj.circletalk.util.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.ID
import io.appwrite.enums.ExecutionMethod
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.tryJsonCast
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import io.appwrite.services.Realtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val databases: Databases,
    private val realtime: Realtime,
    private val functions: Functions
) : ViewModel() {
    private val _messages =
        MutableStateFlow<MutableList<Message>>(emptyList<Message>().toMutableList())
    val messages: StateFlow<MutableList<Message>> = _messages.asStateFlow()

    fun loadMessages(
        friendId: String
    ) {
        val currentId: String = Server.getUser()?.id ?: return

        try {
            Log.d("TAG", "loadMessages: here")
            realtime.subscribe(
                "databases.*.collections.*.documents.*"
            ) {
                Log.d("TASG", "loadMessages: $it")
                val message = it.payload.tryJsonCast(Message::class.java)
                Log.d("TASG", "loadMessages: $message")

                if (message != null) {
                    _messages.value.add(message)
                }
                Log.d("TASG", "loadMessages: ${_messages.value}")

            }
        } catch (e: Exception) {
            Log.d("TAG", "loadMessages:end ")
            Log.e("TAG", "loadMessages: $e")
        }
    }

    fun sendMessage(
        friendId: String,
        message: String
    ) {
        val currentId: String = Server.getUser()?.id ?: return
        viewModelScope.launch {
            try {
                databases.createDocument(
                    databaseId = "messages",
                    collectionId = generateUniqueKey(Server.getUser()?.id ?: "", friendId),
                    documentId = ID.unique(),
                    data = Message(
                        time = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT),
                        message = message,
                        senderId = currentId,
                        receiverId = friendId
                    )
                )
            } catch (e: AppwriteException) {
                if (e.type == "collection_not_found") {
                    functions.createExecution(
                        functionId = "create_message_collection",
                        async = true,
                        body = JSONObject().apply {
                            put("id", generateUniqueKey(currentId, friendId))
                            put("senderId", currentId)
                            put("receiverId", friendId)
                        }.toString(),
                        method = ExecutionMethod.PUT
                    )
                    sendMessage(friendId, message)
                    loadMessages(friendId)
                } else {
                    Log.e("TAG", "sendMessage: $e")
                }
            }
        }
    }
}

private fun generateUniqueKey(
    id1: String,
    id2: String
): String {
    val input = if (id1 > id2) "$id1-$id2" else "$id2-$id1"
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }.take(36)
}