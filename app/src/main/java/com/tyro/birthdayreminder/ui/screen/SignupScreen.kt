package com.tyro.birthdayreminder.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme

@Composable
fun SignupScreen() {

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var termsCheck by remember { mutableStateOf(false) }




    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    Modifier.size(70.dp), tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Create Account", color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Join us and never forget a special day" , color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(32.dp))

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = fullname,
                        onValueChange = {fullname = it},
                        placeholder = { Text("Enter your full name")},
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "") },
                    )
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = email,
                        onValueChange = {email = it},
                        placeholder = { Text("Enter your email")},
                        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "") },
                    )
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = {password = it},
                        visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = { Text("Enter your password")},
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "") },
                        trailingIcon = {
                            IconButton(onClick = {showPassword = !showPassword}) {
                                Icon(painter = painterResource(id = if(showPassword) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                    contentDescription = "")
                            }
                        },
                        supportingText = {Text("Password must be at least 6 characters long")}
                    )
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = confirmPassword,
                        onValueChange = {confirmPassword = it},
                        visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = { Text("Confirm you password")},
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "") },
                    )

                    Row(verticalAlignment = Alignment.CenterVertically){
                        Checkbox(checked = termsCheck, onCheckedChange = { termsCheck = it })
                        Text("I agree to the Terms of Service and Privacy Policy" , color = MaterialTheme.colorScheme.onBackground)
                    }
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth(),
                        enabled = termsCheck,
                        shape = RoundedCornerShape(16))
                    {
                        Text("Sign in")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.weight(1f))
                        Text(
                            text = "or sign up with",
                            modifier = Modifier.weight(1f)
                                .padding(horizontal = 12.dp),
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.weight(1f))
                    }
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16)) {
                        Icon(painter = painterResource(id = R.drawable.android_neutral_rd),
                            tint = Color.Unspecified,
                            contentDescription = "")
                        Spacer(Modifier.width(8.dp))
                        Text("Google")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()){
                        Text("Already have an account? Sign in", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview(){
    BirthdayReminderTheme {
        SignupScreen()
    }
}