package com.example.chatapp

import android.os.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.viewModel.MessageInputViewModel


@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: MessageInputViewModel
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        AppBar()

        MessageList(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )

        MessageInput(viewModel)
    }
}
@Composable
fun AppBar(modifier : Modifier = Modifier){
    Box(modifier=modifier.padding(start = 8.dp, bottom = 8.dp)){
        Text("Chat App")
    }
}

@Composable
fun MessageInput(viewModel: MessageInputViewModel){

    var message by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // 🔥 Observe messageSent
    LaunchedEffect(viewModel.messageSent) {
        if (viewModel.messageSent) {
            focusManager.clearFocus()  // close keyboard
            viewModel.resetMessageSent()
        }
    }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message") },
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                viewModel.addMessage(message)
                message = ""
            }
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun MessageList(viewModel: MessageInputViewModel,modifier: Modifier){
    val messageList = viewModel.messageList
    LazyColumn(modifier = modifier.padding(4.dp),){
        items(messageList) { msg ->
            if (msg.role =="user"){
                UserMessage(msg.message)
            }else{
                BotMessage(msg.message)
            }
        }

    }
}
@Composable
fun UserMessage(message: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.CenterEnd

    ){
        Box(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            Text(text = message,color = MaterialTheme.colorScheme.onPrimary, textAlign = TextAlign.End)
        }

    }
}

@Composable
fun BotMessage(message: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ){
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}