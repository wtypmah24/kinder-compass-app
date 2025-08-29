package com.example.school_companion.ui.screens.assistant

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.chat.MessageInputCard
import com.example.school_companion.ui.list.MessagesList
import com.example.school_companion.ui.selector.ChildSelector
import com.example.school_companion.ui.selector.ThreadSelector
import com.example.school_companion.ui.viewmodel.ChatViewModel
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.UiState
import com.example.school_companion.ui.viewmodel.getOrNull
import com.example.school_companion.ui.viewmodel.onState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    navController: NavController,
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()
    val chatIdsState by chatViewModel.chatIdsState.collectAsStateWithLifecycle()

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var selectedThread by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val messages by chatViewModel.messages.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        childrenViewModel.loadChildren()
        chatViewModel.getChatIds()
    }

    LaunchedEffect(messages) {
        messages.getOrNull()?.let { list ->
            if (list.isNotEmpty()) {
                coroutine.launch {
                    listState.animateScrollToItem(list.size - 1)
                }
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "AI Assistant", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            IconButton(onClick = {
                if (selectedChild != null) {
                    selectedThread = null
                    chatViewModel.clearMessages()
                    messageText = ""
                } else {
                    Toast.makeText(
                        context,
                        "To start a new chat you need to choose a child",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Icon(Icons.Filled.Message, contentDescription = "New chat")
            }
        })
    }, bottomBar = {
        DashBoardBottomBar(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            onTabNavigate = { screen ->
                navController.navigate(screen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (childrenState is UiState.Success) {
                ChildSelector(children = (childrenState as UiState.Success).data,
                    selectedChild = selectedChild,
                    onSelect = {
                        selectedChild = it
                        selectedThread = null
                        chatViewModel.clearMessages()
                        messageText = ""
                    })
            }

            if (chatIdsState is UiState.Success) {
                ThreadSelector(threads = (chatIdsState as UiState.Success).data,
                    selectedThread = selectedThread,
                    onSelect = { tid ->
                        selectedThread = tid
                        selectedChild = null
                        if (tid != null) {
                            chatViewModel.getChatByThreadId(tid)
                        }
                    },
                    onDelete = { tid ->
                        chatViewModel.removeChatByThreadId(tid)
                        chatViewModel.getChatIds()
                        if (tid == selectedThread) chatViewModel.clearMessages()
                        selectedThread = null
                    })
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium),
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    messages.onState(
                        onLoading = { LoadingBox() },
                        onError = { msg -> ErrorBox(message = msg) },
                        onSuccess = {
                            MessagesList(
                                messages = it, listState = listState, modifier = Modifier.weight(1f)
                            )
                        }
                    )
                }
            }
            MessageInputCard(messageText = messageText,
                onMessageChange = { messageText = it },
                canSend = (selectedChild != null) || (selectedThread != null),
                onSend = {
                    chatViewModel.sendMessage(
                        messageText = messageText,
                        selectedChildId = selectedChild?.id,
                        selectedThread = selectedThread
                    )
                    messageText = ""
                })
        }
    }
}