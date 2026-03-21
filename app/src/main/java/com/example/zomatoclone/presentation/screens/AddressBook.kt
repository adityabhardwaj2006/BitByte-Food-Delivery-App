package com.example.zomatoclone.presentation.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.zomatoclone.data.UserDataStore
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


sealed class AddressUiState {
    object Loading : AddressUiState()
    data class Success(val address: String, val name: String) : AddressUiState()
    data class Error(val message: String) : AddressUiState()
}

sealed class SaveStatus {
    object Idle : SaveStatus()
    object Saving : SaveStatus()
    object Success : SaveStatus()
    data class Error(val message: String) : SaveStatus()
}

private const val USER_COLLECTION = "users"

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow<AddressUiState>(AddressUiState.Loading)
    val uiState: StateFlow<AddressUiState> = _uiState

    private val _saveStatus = MutableStateFlow<SaveStatus>(SaveStatus.Idle)
    val saveStatus: StateFlow<SaveStatus> = _saveStatus

    private var listener: ListenerRegistration? = null

    init {
        listenToAddress()
    }

    private fun listenToAddress() {

        val uid = auth.currentUser?.uid ?: run {
            _uiState.value = AddressUiState.Error("User not logged in")
            return
        }

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection(USER_COLLECTION)
                    .document(uid)
                    .get()
                    .await()

                val address = snapshot.getString("address") ?: ""
                val name = snapshot.getString("name") ?: ""

                _uiState.value = AddressUiState.Success(address, name)

            } catch (e: Exception) {
                _uiState.value = AddressUiState.Error("Failed to load data")
            }
        }

        // THEN attach listener
        listener = firestore.collection(USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                Log.d("AddressDebug", "Snapshot exists: ${snapshot?.exists()}")

                if (error != null) {
                    _uiState.value = AddressUiState.Error(error.message ?: "Unknown error")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {

                    val address = snapshot.getString("address") ?: ""
                    val name = snapshot.getString("name") ?: ""

                    _uiState.value = AddressUiState.Success(address, name)

                } else {
                    _uiState.value = AddressUiState.Success("", "")
                }
            }
    }

    fun updateAddress(newAddress: String) {

        val uid = auth.currentUser?.uid ?: run {
            _saveStatus.value = SaveStatus.Error("User not logged in")
            return
        }

        viewModelScope.launch {

            _saveStatus.value = SaveStatus.Saving

            try {

                firestore.collection(USER_COLLECTION)
                    .document(uid)
                    .update("address", newAddress)
                    .await()

                userDataStore.setAddress(newAddress)

                _saveStatus.value = SaveStatus.Success

            } catch (e: Exception) {

                _saveStatus.value =
                    SaveStatus.Error(e.localizedMessage ?: "Save failed")
            }
        }
    }

    fun clearSaveStatus() {
        _saveStatus.value = SaveStatus.Idle
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBook(
    viewModel: AddressViewModel = hiltViewModel()
) {
    val zomViewModel: ZomViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val saveStatus by viewModel.saveStatus.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(saveStatus) {

        when (val s = saveStatus) {

            is SaveStatus.Success -> {
                snackbarHostState.showSnackbar("Address updated successfully")
                isEditing = false
                viewModel.clearSaveStatus()
            }

            is SaveStatus.Error -> {
                snackbarHostState.showSnackbar(s.message)
                viewModel.clearSaveStatus()
            }

            else -> Unit
        }
    }

    Scaffold(

        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = if (isEditing) "Edit Address" else "My Address"
                    )
                }

            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (val state = uiState) {

                is AddressUiState.Loading -> LoadingView()

                is AddressUiState.Error -> ErrorView(
                    message = state.message,
                    onRetry = { }
                )

                is AddressUiState.Success -> {

                    AnimatedContent(
                        targetState = isEditing,
                        label = ""
                    ) { editing ->

                        if (editing) {

                            EditAddressView(
                                currentAddress = state.address,
                                isSaving = saveStatus is SaveStatus.Saving,
                                onSave = { viewModel.updateAddress(it) },
                                onCancel = { isEditing = false }
                            )

                        } else {

                            ViewAddressView(
                                name = state.name,
                                address = state.address,
                                onEdit = { isEditing = true }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ViewAddressView(
    name: String,
    address: String,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User name header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Column {
                    Text(
                        text = "Delivery Address",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Text(
                        text = name.ifEmpty { "—" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Saved Address",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(Modifier.height(12.dp))

                if (address.isEmpty()) {
                    Text(
                        text = "No address saved yet. Tap \"Change Address\" to add one.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        Button(
            onClick = onEdit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Change Address", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun EditAddressView(
    currentAddress: String,
    isSaving: Boolean,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var addressText by remember { mutableStateOf(currentAddress) }
    val isUnchanged = addressText.trim() == currentAddress.trim()
    val canSave = !isSaving && !isUnchanged && addressText.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Info banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Enter your full delivery address including street, city, state and PIN code.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Multiline address field (matches String type in Firestore)
        OutlinedTextField(
            value = addressText,
            onValueChange = { addressText = it },
            label = { Text("Full Address") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.LocationOn,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .offset(y = (-2).dp)
                )
            },
            placeholder = {
                Text(
                    "e.g. 42, MG Road, Koramangala\nBangalore, Karnataka – 560034",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp),
            shape = RoundedCornerShape(14.dp),
            minLines = 4,
            maxLines = 8,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
        )

        // Character counter
        Text(
            text = "${addressText.length} characters",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(Modifier.height(4.dp))

        // Cancel / Save
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !isSaving
            ) {
                Text("Cancel", fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { onSave(addressText.trim()) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = canSave
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        Icons.Outlined.Save,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Save", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading address…", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Text(
                "Something went wrong",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onRetry, shape = RoundedCornerShape(12.dp)) {
                Text("Retry")
            }
        }
    }
}
