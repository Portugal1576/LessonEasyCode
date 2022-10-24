package com.portugal1576.lessoneasycode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "TextWatcherTag"

class MainActivity : AppCompatActivity() {

    private lateinit var textInputEditText: TextInputEditText
    private val textWatcher: TextWatcher = object: SimpleTextWatcher(){
        override fun afterTextChanged(p0: Editable?) {
            Log.d(TAG, "afterTextChange $p0")
            val input = p0.toString()
            if (input.endsWith("@g")){
                Log.d(TAG, "programmatically set text")
                setText("${input}mail.com")
            }
        }
    }
    private fun setText(text: String){
        textInputEditText.removeTextChangedListener(textWatcher)
        textInputEditText.setTextCorrectly(text)
        textInputEditText.addTextChangedListener(textWatcher)
        textInputEditText.listenChanges{
            text ->  Log.d(TAG, text)
        }
    }

    fun TextInputEditText.listenChanges(block: (text: String) -> Unit){
        addTextChangedListener(object : SimpleTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                block.invoke(p0.toString())
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        textInputEditText = textInputLayout.editText as TextInputEditText
        textInputEditText.addTextChangedListener(textWatcher)
        textInputEditText.listenChanges {
            textInputLayout.isErrorEnabled = false
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            if (EMAIL_ADDRESS.matcher(textInputEditText.text.toString()).matches()){
                hideKeyboard(textInputEditText)
                loginButton.isEnabled = false
                Snackbar.make(loginButton, "Go to postLogin", Snackbar.LENGTH_LONG).show()
            }else {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = getString(R.string.invalid_email_message)
            }
        }

    }
}
fun AppCompatActivity.hideKeyboard(view: View){
    val imm = this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

private fun TextInputEditText.setTextCorrectly(text: CharSequence){
    setText(text)
    setSelection(text.length)
}

