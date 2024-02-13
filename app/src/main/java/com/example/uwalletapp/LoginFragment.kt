package com.example.uwalletapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginFragment : Fragment() {

    private lateinit var logoImageView: ImageView
    private lateinit var uwalletText: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginSubmitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Initialize views
        logoImageView = view.findViewById(R.id.loginLogoImageView)
        uwalletText = view.findViewById(R.id.uwalletText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginSubmitButton = view.findViewById(R.id.loginSubmitButton)

        // Set click listener for the login button
        loginSubmitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate input (e.g., check if email and password are not empty)

            // Call the authenticateUser function
            authenticateUser(email, password)
        }

        return view
    }

    private fun authenticateUser(email: String, password: String) {
        // Replace "your_web_service_url" with the actual URL of your authentication endpoint
        val webServiceUrl = "your_web_service_url"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(webServiceUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                val postData = """{"email": "$email", "password": "$password"}"""
                connection.doOutput = true
                val outputStream = connection.outputStream
                outputStream.write(postData.toByteArray(Charsets.UTF_8))
                outputStream.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Authentication successful
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Authentication failed
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
