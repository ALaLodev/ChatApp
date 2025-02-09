package com.alalodev.chatapp.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alalodev.chatapp.domain.GetMessageUseCase
import com.alalodev.chatapp.domain.GetUserNameUseCase
import com.alalodev.chatapp.domain.SendMessageUseCase
import com.alalodev.chatapp.domain.model.MessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val getUserNameUseCase: GetUserNameUseCase
) : ViewModel() {

    var name:String = ""

    init {
        getUserName()
        getMessages()
    }

    private fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
           name = getUserNameUseCase()
        }
    }

    private var _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageList: StateFlow<List<MessageModel>> = _messageList

    private fun getMessages(){
        viewModelScope.launch {
            getMessageUseCase().collect{
                Log.i("p", "info es $it")
                _messageList.value = it
            }
        }
    }

    fun sendMessage(msg:String){
        sendMessageUseCase(msg, name)
    }
}