package com.example.bmi
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmi.ui.theme.BmiTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BmiTheme { //aplicamos el tema de nuestra app
                Surface(modifier = Modifier.fillMaxSize()) {
                    BMIScreen() //llamamos a la UI
                }
            }
        }
    }
}

@Composable
fun BMIScreen() {
    //estados para peso, altura , resultados y manejo de errores
    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var bmiResult by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    fun calculateBMI() {
        errorMessage = "" //vacíamos los errores anteriores
        bmiResult = "" //vacíamos datos anteriores
        //convertimos los datos a numeros
        val weight = weightInput.toFloatOrNull()
        val height = heightInput.toFloatOrNull()
        //validación de peso realista
        if (weight == null || weight <= 0f || weight > 300f) {
            errorMessage = "Peso inválido. Usa kg (ej: 70)"
            return
        } //validación de altura realista también
        if (height == null || height <= 0f || height > 2.5f) {
            errorMessage = "Altura inválida. Usa metros (ej: 1.75)"
            return
        }
        //calificación final
        val bmi = weight / (height * height)
        val interpretation = when {
            bmi < 18.5 -> "Bajo peso"
            bmi < 24.9 -> "Peso normal"
            bmi < 29.9 -> "Sobrepeso"
            else -> "Obesidad"
        }

        bmiResult = "Tu BMI es %.2f\nClasificación: %s".format(bmi, interpretation)
        focusManager.clearFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Calculadora de BMI", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))
        //form para los datos
        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = heightInput,
            onValueChange = { heightInput = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        //botton de submmit
        Button(
            onClick = { calculateBMI() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular BMI")
        }

        Spacer(modifier = Modifier.height(16.dp))
        //maneja el error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        //muestra el resultado si es que no tuvo errores
        if (bmiResult.isNotEmpty()) {
            Text(
                text = bmiResult,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
