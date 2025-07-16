@@ .. @@
 @Composable
 fun EventosView(
     uiState: EventoUiState,
     onReload: () -> Unit,
-    onAddEventoClick: () -> Unit = {}
+    onAddEventoClick: () -> Unit = {},
+    onEventClick: (String) -> Unit = {}
 ) {
     var searchQuery by remember { mutableStateOf(TextFieldValue()) }
 
@@ .. @@
                 Card(
                     modifier = Modifier
                         .fillMaxWidth()
-                        .clickable { },
+                        .clickable { onEventClick(evento._id) },
                     elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                     colors = CardDefaults.cardColors(containerColor = cardColor),
                     shape = RoundedCornerShape(16.dp)