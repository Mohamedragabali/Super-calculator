package com.thechancesupercalculator.supercalculator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thechancesupercalculator.supercalculator.databinding.ActivityMainBinding
import java.util.Stack
import kotlin.math.sign

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var numberHasDot = false
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
            binding.historyText.text = ""
        }
        binding.btnBack.setOnClickListener {
            deleteLastItem()
        }
        binding.btnPercent.setOnClickListener {
            changeWritableText("% ")
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
            if (writableText == "-") {
                Toast.makeText(this, "please write correct equation", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val result = evaluate(binding.writeableText.text.toString())
                    if (result.toString() == "Infinity") {
                        Toast.makeText(this, "number divide by 0 it is wrong ", Toast.LENGTH_SHORT)
                            .show()
                    } else if (result.toString() == "NaN") {
                        Toast.makeText(this, "0 divide by 0 it is wrong ", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val stringResult = result.toString()
                        if (stringResult[stringResult.length - 2] == '.' && stringResult[stringResult.length - 1] == '0') {
                            numberHasDot = false
                            binding.historyText.text = binding.writeableText.text
                            binding.writeableText.text = result.toString().dropLast(2)
                        } else {
                            numberHasDot = true
                            binding.historyText.text = binding.writeableText.text
                            binding.writeableText.text = result.toString()
                        }

                    }

                } catch (e: Exception) {
                    Toast.makeText(this, "some thing wrong", Toast.LENGTH_SHORT).show()
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

    private fun deleteLastItem() {
        var writableText = binding.writeableText.text.toString()
        val lastChar = writableText.last()
        if (writableText == "0") {

        } else if (lastChar == ' ') {
            if (writableText[writableText.length - 2] == '%') {
                writableText = writableText.dropLast(2)
            } else {
                writableText = writableText.dropLast(3)
            }
            binding.writeableText.text = writableText
        } else {
            if (lastChar == '.') {
                numberHasDot = false
            }
            writableText = writableText.dropLast(1)
            binding.writeableText.text = writableText
        }

        if (writableText == "") {
            binding.writeableText.text = "0"
        }

    }

    private fun changeWritableText(text: String, isNegativeNumber: Boolean = false) {
        var writableText = binding.writeableText.text.toString()
        val lastChar = writableText.last()
        if (text == "% " || text == " / " || text == " x " || text == " + " || text == " - ") {
            numberHasDot = false
        }
        if (writableText != "0" && lastChar.isDigit() && text == "-") {
            var number = ""
            var countOfDigit = 0
            var isNegativeNumber = false
            for (i in writableText.length - 1 downTo 0) {
                if (writableText[i] == ' ') {
                    break
                } else if (writableText[i] == '-') {
                    isNegativeNumber = true
                    break
                }
                number = writableText[i] + number
                countOfDigit++
            }
            if (!isNegativeNumber) {
                writableText = writableText.dropLast(countOfDigit)
                binding.writeableText.text = writableText + "-" + number
            } else {
                writableText = writableText.dropLast(countOfDigit + 1)
                binding.writeableText.text = writableText + number
            }
        }
        else if ((writableText == "0" || writableText == "-") && (text == "% " || text == " / " || text == " x " || text == " + " || text == " - ")) {
            Toast.makeText(
                this,
                this.getString(R.string.please_enter_number_first),
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (lastChar == '-' && (text == "% " || text == " / " || text == " x " || text == " + " || text == " - ")) {
            Toast.makeText(
                this,
                getString(R.string.please_write_digit_or_delete_sign), Toast.LENGTH_SHORT
            ).show()
        }
        else if (lastChar == ' ' && writableText[writableText.length - 2] != '%' && !isNegativeNumber && (text == " / " || text == " x " || text == " + " || text == " - ")) {
            writableText = writableText.dropLast(3)
            changeWriteableTextHandelFirstWrite(writableText, text)
        }
        else if ((lastChar == ' ' || writableText == "0") && text == ".") {
            numberHasDot = true
            changeWriteableTextHandelFirstWrite(writableText, "0.")
        }
        else if ((lastChar.isDigit() && text == "." && numberHasDot) || (writableText.length > 2 && writableText[writableText.length - 2] == '%' && text == "% ")) {

        }
        else if (text == "% " && !writableText[writableText.length - 1].isDigit()) {
            Toast.makeText(
                this,
                "please enter numbers first to use %",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (writableText == "-" && text == "-") {
            changeWriteableTextHandelFirstWrite("", "0")
        }
        else {
            if (text == ".") {
                numberHasDot = true
            }
            if (text.isDigitsOnly() && writableText.length > 1 && writableText[writableText.length - 2] == '%') {
                changeWriteableTextHandelFirstWrite(writableText, "x $text")
            } else {
                changeWriteableTextHandelFirstWrite(writableText, text)
            }

        }



        binding.scrollResultText.post {
            binding.scrollResultText.scrollTo(0, binding.scrollResultText.getChildAt(0).height)
        }
    }

    private fun changeWriteableTextHandelFirstWrite(writableText: String, text: String) {
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
        var expressAfterEdit = expr.replace("% ", " %", ignoreCase = true)

        val listOFNumber = mutableListOf<Double>()
        val expression = mutableListOf<Char>()
        var isNegative = false
        var isDouble = false
        var number = ""
        var numberAfterDot = "0."
        var charBeforeSign = ' '
        var isItChar = false
        var isLastAddedNumber = false
        expressAfterEdit.forEach {
            if (charBeforeSign == ' ' && it == '-' && isLastAddedNumber == false) {
                isNegative = true
            }
            else if (it.isDigit()) {
                if (isDouble) {
                    numberAfterDot = numberAfterDot + it
                } else {
                    number = number + it
                }
            }
            else if (it == '.') {
                isDouble = true
            }
            else {
                if (it != ' ') {
                    expression.add(it)
                    isItChar = true
                    if(it != '%'){
                        isLastAddedNumber = false
                    }
                }
                else {
                    if (isItChar) {
                        isItChar = false
                    }
                    else {
                        isLastAddedNumber = true
                        val resultNumber = (number.toDouble() +
                                numberAfterDot.toDouble()) *
                                if (isNegative) {
                                    -1
                                } else {
                                    1
                                }

                        listOFNumber.add(resultNumber)
                        isNegative = false
                        isDouble = false
                        number = ""
                        numberAfterDot = "0."
                    }
                }
            }

            charBeforeSign = it
        }

        if (charBeforeSign.isDigit() || (charBeforeSign == '%' && number.isNotEmpty()) || charBeforeSign == '.') {
            val resultNumber = (number.toDouble() +
                    numberAfterDot.toDouble()) * if (isNegative) {
                -1
            } else {
                1
            }

            listOFNumber.add(resultNumber)
        }
        Log.e(
            "MY_TAG", "here " +
                    "$listOFNumber   $expression"
        )


        var finalExpression = ""
        var indexOfExprssion = 0;
        var beforeNumber = 0.0
        listOFNumber.forEach {

            var exprss =
                if (expression.size > indexOfExprssion) expression[indexOfExprssion] else ""
            if (exprss == 'x') {
                exprss = "*"
            }

            if (exprss == '%') {
                if(expression.size == 1 ){
                    finalExpression = finalExpression + " ${it / 100} "
                }
                 else if (indexOfExprssion == 0) {
                    indexOfExprssion++


                    finalExpression = finalExpression + " ${it / 100} " + if (expression[indexOfExprssion]== 'x') {
                        '*'
                    }else{
                        expression[indexOfExprssion]
                    }
                } else {
                    val lastSign = if(expression.size > indexOfExprssion+ 1 ){
                       if (expression[indexOfExprssion+1] == 'x') {
                            '*'
                        }else{
                            expression[indexOfExprssion+1]
                        }

                    }else { "" }
                    if (expression[indexOfExprssion - 1] == '+' || expression[indexOfExprssion - 1] == '-') {
                        indexOfExprssion++
                        finalExpression = finalExpression + " ${it*beforeNumber/100} " +lastSign
                    } else {
                        indexOfExprssion++
                        finalExpression = finalExpression + " ${it/100} " +lastSign
                    }
                }

            } else {
                finalExpression = finalExpression + " $it " + exprss
            }
            beforeNumber = it
            indexOfExprssion++
        }

        if (expression.size > indexOfExprssion) {
            var exprss =
                if (expression.size > indexOfExprssion) expression[indexOfExprssion] else ""
            if (exprss == 'x') {
                exprss = "*"
            }
            finalExpression = finalExpression +exprss
        }

        return finalExpression
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