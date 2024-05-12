package ro.scoalainformala.trips

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditTripActivity : AppCompatActivity() {
    private var editImage: ImageView? = null
    private var editTitle: EditText? = null
    private var editDestination: EditText? = null
    private var editPrice: EditText? = null
    private var editRating: EditText? = null
    private var editType: EditText? = null
    private var editStartDate: EditText? = null
    private var editEndDate: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_trip)

        editImage = findViewById(R.id.add_edit_image)
        editTitle = findViewById(R.id.add_edit_title)
        editDestination = findViewById(R.id.add_edit_destination)
        editPrice = findViewById(R.id.add_edit_price)
        editRating = findViewById(R.id.add_edit_rating)
        editType = findViewById(R.id.add_edit_type)
        editStartDate = findViewById(R.id.add_edit_start_date)
        editEndDate = findViewById(R.id.add_edit_end_date)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        val intent = intent

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Trip"
            with(editImage) { this!!.setImageResource(R.drawable.blank) } // where I had to add a random blank image to have an image all the time
            with(editTitle) { this!!.setText(intent.getStringExtra(EXTRA_TITLE)) }
            with(editDestination) { this!!.setText(intent.getStringExtra(EXTRA_DESTINATION)) }
            with(editPrice) { this!!.setText(intent.getStringExtra(EXTRA_PRICE)) }
            with(editRating) { this!!.setText(intent.getStringExtra(EXTRA_RATING)) }
            with(editType) { this!!.setText(intent.getStringExtra(EXTRA_TYPE)) }
            with(editStartDate) { this!!.setText(intent.getStringExtra(EXTRA_START_DATE)) }
            with(editEndDate) { this!!.setText(intent.getStringExtra(EXTRA_END_DATE)) }
        } else {
            title = "Add Trip"
        }
    }

    private fun saveTrip() {
        val title = editTitle!!.text.toString()
        val image = R.drawable.blank
        val destination = editDestination!!.text.toString()
        val price = if (editPrice!!.text.toString().startsWith("$")) {
            editPrice!!.text.toString()
        } else {
            "$" + editPrice!!.text.toString()
        }
        val rating = editRating!!.text.toString()
        val type = editType!!.text.toString()
        val startDate = editStartDate!!.text.toString()
        val endDate = editEndDate!!.text.toString()

        if (title.trim { it <= ' ' }.isEmpty() || destination.trim { it <= ' ' }
                .isEmpty() || price.trim { it <= ' ' }.isEmpty() || rating.trim { it <= ' ' }
                .isEmpty() || type.trim { it <= ' ' }.isEmpty() || startDate.trim { it <= ' ' }
                .isEmpty() || endDate.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(
                this,
                "Please insert title, destination, price, type, rating, start date and end date for your trip",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_IMAGE, image)
        data.putExtra(EXTRA_DESTINATION, destination)
        data.putExtra(EXTRA_PRICE, price)
        data.putExtra(EXTRA_RATING, rating)
        data.putExtra(EXTRA_TYPE, type)
        data.putExtra(EXTRA_START_DATE, startDate)
        data.putExtra(EXTRA_END_DATE, endDate)
        data.putExtra(EXTRA_IS_FAVOURITE, false)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_trip -> {
                saveTrip()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_IMAGE: String = "ro.scoalainformala.trips.EXTRA_IMAGE"
        const val EXTRA_ID: String = "ro.scoalainformala.trips.EXTRA_ID"
        const val EXTRA_TITLE: String = "ro.scoalainformala.trips.EXTRA_TITLE"
        const val EXTRA_DESTINATION: String = "ro.scoalainformala.trips.EXTRA_DESTINATION"
        const val EXTRA_PRICE: String = "ro.scoalainformala.trips.EXTRA_PRICE"
        const val EXTRA_RATING: String = "ro.scoalainformala.trips.EXTRA_RATING"
        const val EXTRA_TYPE: String = "ro.scoalainformala.trips.EXTRA_TYPE"
        const val EXTRA_START_DATE: String = "ro.scoalainformala.trips.EXTRA_START_DATE"
        const val EXTRA_END_DATE: String = "ro.scoalainformala.trips.EXTRA_END_DATE"
        const val EXTRA_IS_FAVOURITE: String = "ro.scoalainformala.trips.EXTRA_IS_FAVOURITE"
    }
}
