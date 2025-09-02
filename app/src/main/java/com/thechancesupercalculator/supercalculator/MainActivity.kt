package com.thechancesupercalculator.supercalculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thechancesupercalculator.supercalculator.databinding.ActivityMainBinding
import java.util.Stack
import kotlin.math.sign

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var numberHasDot  = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        callBacks()
    }

    private fun callBacks() {
        binding.btn0.setOnClickListener {
            changeWritableText("0")
        }
        binding.btn1.setOnClickListener {
            changeWritableText("1")
        }
        binding.btn2.setOnClickListener {
            changeWritableText("2")
        }
        binding.btn3.setOnClickListener {
            changeWritableText("3")
        }
        binding.btn4.setOnClickListener {
            changeWritableText("4")
        }
        binding.btn5.setOnClickListener {
            changeWritableText("5")
        }
        binding.btn6.setOnClickListener {
            changeWritableText("6")
        }
        binding.btn7.setOnClickListener {
            changeWritableText("7")
        }
        binding.btn8.setOnClickListener {
            changeWritableText("8")
        }

        binding.btn9.setOnClickListener {
            changeWritableText("9")
        }
        binding.btnAC.setOnClickListener {
            numberHasDot = false
            binding.writeableText.text = "0"
            binding.historyText.text =""
        }
        binding.btnBack.setOnClickListener {
            deleteLastItem()
        }
        binding.btnPercent.setOnClickListener {
            changeWritableText(" % ")
        }
        binding.btnDivide.setOnClickListener {
            changeWritableText(" / ")
        }
        binding.btnMultiply.setOnClickListener {
            changeWritableText(" x ")
        }
        binding.btnPlus.setOnClickListener {
            changeWritableText(" + ")
        }
        binding.btnMinus.setOnClickListener {
            changeWritableText(" - ")
        }
        binding.btnEqual.setOnClickListener {
            val writableText = binding.writeableText.text
            if( writableText == "0" || writableText== "0." ){
                numberHasDot = false
                binding.historyText.text = "0"
                binding.writeableText.text = "0"
            }else if (writableText == "-"){
                Toast.makeText(this, "please write correct equation" , Toast.LENGTH_SHORT).show()
            }else{
                try {
                    val result = evaluate(binding.writeableText.text.toString())
                    if(result.toString() == "Infinity"){
                        Toast.makeText(this, "number divide by 0 it is wrong " , Toast.LENGTH_SHORT).show()
                    }else if (result.toString() == "NaN"){
                        Toast.makeText(this, "0 divide by 0 it is wrong " , Toast.LENGTH_SHORT).show()
                    }else{
                        numberHasDot = true
                        binding.historyText.text = binding.writeableText.text
                        binding.writeableText.text = result.toString()
                    }

                }catch (e : Exception){
                    Toast.makeText(this, "some thing wrong" , Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.btnDot.setOnClickListener {
            changeWritableText(".")
        }
        binding.btnPlusMinus.setOnClickListener {
            changeWritableText("-", true)
        }
    }

    private fun deleteLastItem(){
        var writableText = binding.writeableText.text.toString()
        val lastChar = writableText.last()
        if(writableText == "0"){

        }else if(lastChar == ' '){
            writableText = writableText.dropLast(3)
            binding.writeableText.text = writableText
        }else{
            if(lastChar == '.'){numberHasDot = false }
            writableText = writableText.dropLast(1)
            binding.writeableText.text = writableText
        }

        if(writableText == ""){
            binding.writeableText.text ="0"
        }

    }
    private fun changeWritableText(text: String, isNegativeNumber: Boolean = false) {
        var writableText = binding.writeableText.text.toString()
        val lastChar = writableText.last()
        if (text == " % " || text == " / " || text == " x " || text == " + " || text == " - "){
            numberHasDot = false
        }


        if( writableText != "0" && lastChar.isDigit() && text == "-"  ){
            var number = ""
            var countOfDigit = 0
            var isNegativeNumber = false
            for ( i in writableText.length -1 downTo 0 ){
                if(writableText[i] == ' '){break }
                else if( writableText[i] == '-'){
                    isNegativeNumber = true
                    break
                }
                number = writableText[i] + number
                countOfDigit++
            }
            if(!isNegativeNumber){
             writableText = writableText.dropLast(countOfDigit)
                binding.writeableText.text = writableText + "-" + number
            }
        }
        else if((writableText == "0" || writableText == "-")&& (text == " % " || text == " / " || text == " x " || text == " + " || text == " - ") ){
            Toast.makeText(this, this.getString(R.string.please_enter_number_first) , Toast.LENGTH_SHORT).show()
        }
        else if (lastChar == '-' && (text == " % " || text == " / " || text == " x " || text == " + " || text == " - ") ){
            Toast.makeText(this,
                getString(R.string.please_write_digit_or_delete_sign) , Toast.LENGTH_SHORT).show()
        }
        else if (lastChar == ' ' && !isNegativeNumber && (text == " % " || text == " / " || text == " x " || text == " + " || text == " - ")) {
            writableText =  writableText.dropLast(3)
            changeWriteableTextHandelFirstWrite(writableText , text)
        }else if ( (lastChar == ' ' || writableText == "0") && text == "."){
            numberHasDot = true
            changeWriteableTextHandelFirstWrite(writableText , "0.")
        }else if (lastChar.isDigit() && text == "." && numberHasDot){

        } else {
            if (text == "."){ numberHasDot = true }
            changeWriteableTextHandelFirstWrite(writableText , text)
        }



        binding.scrollResultText.post {
            binding.scrollResultText.scrollTo(0, binding.scrollResultText.getChildAt(0).height)
        }
    }

    private fun changeWriteableTextHandelFirstWrite(writableText : String , text  : String ){
        if (writableText == "0") {
            binding.writeableText.text = "$text"
        } else {
            binding.writeableText.text = writableText + "$text"
        }
    }


    fun evaluate(expression: String): Double {
        // 1) Preprocess the expression (handle percentages)
        val processed = preprocess(expression)

        // 2) Convert to postfix (Reverse Polish Notation)
        val postfix = infixToPostfix(processed)

        // 3) Evaluate postfix
        return evalPostfix(postfix)
    }

    private fun preprocess(expr: String): String {
        val regex = Regex("(\\d+(?:\\.\\d+)?)%")
        var replaced = regex.replace(expr) { match ->
            val number = match.groupValues[1].toDouble()
            (number / 100.0).toString()
        }

        // Handle "10 % 20" → "0.1 * 20"
        replaced = replaced.replace(Regex("(\\d+(?:\\.\\d+)?)\\s*%\\s*(\\d+(?:\\.\\d+)?)")) {
            val num = it.groupValues[1].toDouble() / 100.0
            "$num * ${it.groupValues[2]}"
        }

        return replaced
    }

    private fun infixToPostfix(expression: String): List<String> {
        val output = mutableListOf<String>()
        val stack = Stack<String>()

        val tokens = expression.replace("\\s+".toRegex(), " ")
            .trim().split(" ")

        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token in listOf("+", "-", "*", "/") -> {
                    while (stack.isNotEmpty() && precedence[stack.peek()] ?: 0 >= precedence[token]!!) {
                        output.add(stack.pop())
                    }
                    stack.push(token)
                }
            }
        }

        while (stack.isNotEmpty()) {
            output.add(stack.pop())
        }

        return output
    }

    private fun evalPostfix(postfix: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfix) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                token in listOf("+", "-", "*", "/") -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    val result = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        else -> throw IllegalArgumentException("Unknown operator $token")
                    }
                    stack.push(result)
                }
            }
        }

        return stack.pop()
    }



}