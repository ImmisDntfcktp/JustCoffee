/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 */
package com.example.android.justjava

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt


/**
 * This app displays an order form to order coffee.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private var numberOfCupsOfCoffee: Int = 1
    private var costOfCupOfCoffee: Double = 1.75
    private var costOfWhippedCream: Double = 0.2
    private var costOfChocolate: Double = 0.1
    private var whippedCream = ""
    private var chocolate = ""
    var duration = Toast.LENGTH_SHORT
    private val tooMuchCoffee by lazy {getString(R.string.too_much_coffee)}
    private val emptyOrder by lazy {getString(R.string.empty_order)}

    fun incrementCups(view: View?) {
        if (numberOfCupsOfCoffee < 100) {
            numberOfCupsOfCoffee += 1
        } else {
            Toast.makeText(applicationContext, tooMuchCoffee, duration).show()
        }
        displayQuantity(numberOfCupsOfCoffee)
    }

    fun decrementCups(view: View?) {
        if (numberOfCupsOfCoffee > 1) {
            numberOfCupsOfCoffee -= 1
        } else {
            Toast.makeText(applicationContext, emptyOrder, duration).show()
        }
        displayQuantity(numberOfCupsOfCoffee)
    }

    fun costDrink (coffeeCost: Double, Add1cost: Double, Add2cost: Double, add1: String, add2: String ): Double {
        if (add1 == "" && add2 == "") {
            return coffeeCost
        } else if (add1 !== "" && add2 == "") {
            return coffeeCost + Add1cost
        } else if (add1 == "" && add2 !== "") {
            return coffeeCost + Add2cost
        } else {
            return coffeeCost + Add1cost + Add2cost
        }
    }

    private fun createOrderSummary(nameClient: String, numberOfCups: Int, costDrink: Double, add1: String, add2: String): String {
        var totalPrice = numberOfCups * costDrink
        totalPrice = (totalPrice * 100).roundToInt() / 100.0

        val add1Formatted = if(add1.isNotEmpty()) "$add1\n" else ""
        val add2Formatted = if(add2.isNotEmpty()) "$add2\n" else ""
        val noAdds = if(add1.isEmpty() && add2.isEmpty()) "no additions\n" else ""

        return "Name: $nameClient\nQuantity: $numberOfCups\nAdd to drink:\n$add1Formatted$add2Formatted$noAdds" +
                "Cost one cup of coffee: $costDrink\nTotal:$$totalPrice\nThank you!"

    }

    /** This method is called when the order button is clicked.*/
    @SuppressLint("QueryPermissionsNeeded", "StringFormatMatches")
    fun submitOrder(view: View?) {
        var personalDataOfClient = getName()
        var costDrink = costDrink(costOfCupOfCoffee, costOfWhippedCream, costOfChocolate, whippedCream, chocolate)
        costDrink = (costDrink * 100).roundToInt() / 100.0

        val sendMail = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(
                Intent.EXTRA_TEXT, createOrderSummary(
                    personalDataOfClient, numberOfCupsOfCoffee,
                    costDrink, whippedCream, chocolate
                )
            )
            putExtra(Intent.EXTRA_SUBJECT, "Cafe App. Order by $personalDataOfClient")
        }
        if (sendMail.resolveActivity(packageManager) != null) {
            startActivity(sendMail)
        }
        val displayThanks = getString(R.string.display_thanks)
        val displayOrder = getString(R.string.display_order, numberOfCupsOfCoffee)
        displayMessage("$displayOrder\n$displayThanks")
    }

    fun getName(): String {
        val dataEditTextView: EditText = findViewById(R.id.name_description_view)
        var dataString = dataEditTextView.text.toString()
        return dataString
    }

    /** This method displays the given quantity value on the screen.*/
    private fun displayQuantity(quantity: Int) {
        val quantityTextView: TextView = findViewById(R.id.quantity_text_view)
        quantityTextView.text = "$quantity"
    }

    /** This method displays the given text on the screen.*/
    private fun displayMessage(message: String) {
        val orderSummaryTextView: TextView = findViewById(R.id.order_summary_text_view)
        orderSummaryTextView.text = message
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.topping_whipped_cream_checkbox -> {
                    if (checked) {
                        whippedCream = "whipped cream"
                    } else {
                        whippedCream = ""
                        // Remove from list
                    }
                }
                R.id.topping_chocolate_checkbox -> {
                    if (checked) {
                        chocolate = "chocolate"
                    } else {
                        chocolate = ""
                        // Remove from list
                    }
                }
            }
        }
    }

}