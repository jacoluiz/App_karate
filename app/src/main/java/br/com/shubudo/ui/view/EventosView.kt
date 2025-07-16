@@ .. @@
 import androidx.compose.ui.text.style.TextAlign
 import androidx.compose.ui.text.style.TextOverflow
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
 import br.com.shubudo.SessionManager
 import br.com.shubudo.model.Evento
 import br.com.shubudo.ui.components.LoadingOverlay
 import br.com.shubudo.ui.uistate.EventoUiState
 import java.time.ZonedDateTime
@@ .. @@
 fun EventosView(
     uiState: EventoUiState,
     onReload: () -> Unit,
+    onEventClick: (String) -> Unit = {},
     onAddEventoClick: () -> Unit = {}
 ) {
     var searchQuery by remember { mutableStateOf(TextFieldValue()) }
@@ .. @@
                         }
                         items(filteredFuturos) { evento ->
-                            EventoItem(evento = evento, isPast = false)
+                            EventoItem(
+                                evento = evento, 
+                                isPast = false,
+                                onClick = { onEventClick(evento._id) }
+                            )
                         }
                     }
 
@@ .. @@
                             )
                         }
                         items(filteredPassados) { evento ->
-                            EventoItem(evento = evento, isPast = true)
+                            EventoItem(
+                                evento = evento, 
+                                isPast = true,
+                                onClick = { onEventClick(evento._id) }
+                            )
                         }
                     }
                 }
@@ .. @@
 
 @Composable
 fun EventoItem(evento: Evento, isPast: Boolean = false) {
+    EventoItem(evento = evento, isPast = isPast, onClick = {})
+}
+
+@Composable
+fun EventoItem(
+    evento: Evento, 
+    isPast: Boolean = false,
+    onClick: () -> Unit
+) {
     val cardColor = if (isPast) {
         MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
     } else {
@@ .. @@
     Card(
         modifier = Modifier
             .fillMaxWidth()
-            .clickable { },
+            .clickable { onClick() },
         elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
         colors = CardDefaults.cardColors(containerColor = cardColor),
         shape = RoundedCornerShape(16.dp)