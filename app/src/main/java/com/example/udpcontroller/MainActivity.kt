package com.example.udpcontroller

import android.R
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udpcontroller.ui.theme.UdpControllerTheme
import kotlinx.coroutines.delay
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.Charset


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UdpControllerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    app(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun app(modifier: Modifier = Modifier) {
    var state: String by remember { mutableStateOf("join") }
    var ip: String by remember { mutableStateOf("10.0.0.41") }
    var port: String by remember { mutableStateOf("8000") }
    var name: String by remember { mutableStateOf("Adam") }
    var movement: String by remember { mutableStateOf("None") }
    var jumped: Boolean by remember { mutableStateOf(false) }

    if (state == "join") {
        Column {
            TextField(value = ip, onValueChange = { ip = it }, modifier)
            TextField(value = port, onValueChange = { port = it }, modifier)
            TextField(value = name, onValueChange = { name = it }, modifier)
            TextButton(onClick = { state = "game" }) {
                Text(text = "Join")
            }
        }
    } else {
        Text("$movement   $jumped")

        Row {
            Surface(color = Color.Blue, modifier = modifier
                .fillMaxHeight(1f)
                .fillMaxWidth(0.25f)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_UP -> {
                            movement = "None"
                            sendPacket(ip, port, RocketMessage(name, movement).stringify())
                        }
                        MotionEvent.ACTION_DOWN -> {
                            movement = "Left"
                            sendPacket(ip, port, RocketMessage(name, movement).stringify())
                        }
                        else -> {}
                    }
                    true
                }
                .clickable {  })
            {
                Text(
                    text = "<--",
                    modifier = modifier.padding(24.dp)
                )
            }
            Surface(color = Color.Red, modifier = modifier
                .fillMaxHeight(1f)
                .fillMaxWidth(0.25f)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_UP -> {
                            movement = "None"
                            sendPacket(ip, port, RocketMessage(name, movement).stringify())
                        }
                        MotionEvent.ACTION_DOWN -> {
                            movement = "Right"
                            sendPacket(ip, port, RocketMessage(name, movement).stringify())
                        }
                        else -> {}
                    }
                    true
                }
                .clickable { }) {
                
                Text(
                    text = "-->",
                    modifier = modifier.padding(24.dp)
                )
            }
            Surface(
                color = Color.Green,
                shape = RoundedCornerShape(1000.dp),
                modifier = modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(1f)
                    .clickable {
                        jumped = true;
                    }
            ) {
                Text(text = "jump", modifier = modifier.size(100.dp))
            }
        }
        TextButton(onClick = { state = "join" }) {
            Text(text = "Leave", modifier)
        }
    }
}

//@Serializable
class RocketMessage ( val player: String, val movement: String ){

    fun stringify(): String {
        return "{\"player\": \"${player}\", \"movement\": \"${movement}\"}"
    }
}

suspend fun run(ip: String, port: String, name: String){
    while (true) {
//        sendPacket(ip, port, RocketMessage(name, ))
        delay(500)
    }
}

fun sendPacket(ip: String, port: String, msg: String) {
    val ds = DatagramSocket()
    val ipAddress = InetAddress.getByName(ip)
    val buffer = msg.toByteArray(Charset.defaultCharset())
    val dpSend = DatagramPacket(buffer, buffer.size, ipAddress, port.toInt());
    ds.send(dpSend);
}

@Composable
fun Arrows(modifier: Modifier = Modifier) {
    Row {
        Surface(color = Color.Blue, modifier = modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(0.25f)
            .clickable { }) {
            Text(
                text = "<--",
                modifier = modifier.padding(24.dp)
            )
        }
        Surface(color = Color.Red, modifier = modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(0.25f)
            .clickable { }) {
            Text(
                text = "-->",
                modifier = modifier.padding(24.dp)
            )
        }
        Surface(
            color = Color.Green,
            shape = RoundedCornerShape(1000.dp),
            modifier = modifier
                .fillMaxHeight(1f)
                .fillMaxWidth(1f)
                .clickable {

                }
        ) {
            Text(text = "jump", modifier = modifier.size(100.dp))
        }
    }
}

@Composable
fun joinPage(modifier: Modifier = Modifier) {
    Column {
        var ip by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        TextField(value = ip, onValueChange = { ip = it }, modifier)
        TextField(value = name, onValueChange = { name = it }, modifier)
        val scope = rememberCoroutineScope()
        TextButton(onClick = {
        }) {
            Text(text = "Join")
        }

    }
}

@Preview
@Composable
fun previewJoinPage(modifier: Modifier = Modifier) {
    joinPage()
}

