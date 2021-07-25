package ro.scoalainformala.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditTripActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "ro.scoalainformala.trips.EXTRA_IMAGE";
    public static final String EXTRA_ID = "ro.scoalainformala.trips.EXTRA_ID";
    public static final String EXTRA_TITLE = "ro.scoalainformala.trips.EXTRA_TITLE";
    public static final String EXTRA_DESTINATION = "ro.scoalainformala.trips.EXTRA_DESTINATION";
    public static final String EXTRA_PRICE = "ro.scoalainformala.trips.EXTRA_PRICE";
    public static final String EXTRA_RATING = "ro.scoalainformala.trips.EXTRA_RATING";
    public static final String EXTRA_TYPE = "ro.scoalainformala.trips.EXTRA_TYPE";
    public static final String EXTRA_START_DATE = "ro.scoalainformala.trips.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "ro.scoalainformala.trips.EXTRA_END_DATE";
    public static final String EXTRA_IS_FAVOURITE = "ro.scoalainformala.trips.EXTRA_IS_FAVOURITE";

    private ImageView editImage;
    private EditText editTitle;
    private EditText editDestination;
    private EditText editPrice;
    private EditText editRating;
    private EditText editType;
    private EditText editStartDate;
    private EditText editEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);

        editTitle = findViewById(R.id.add_edit_title);
        editImage = findViewById(R.id.add_edit_image);
        editDestination = findViewById(R.id.add_edit_destination);
        editPrice = findViewById(R.id.add_edit_price);
        editRating = findViewById(R.id.add_edit_rating);
        editType = findViewById(R.id.add_edit_type);
        editStartDate = findViewById(R.id.add_edit_start_date);
        editEndDate = findViewById(R.id.add_edit_end_date);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit trip");
            editTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editImage.setImageResource(R.drawable.blank);
            editDestination.setText(intent.getStringExtra(EXTRA_DESTINATION));
            editPrice.setText(intent.getStringExtra(EXTRA_PRICE));
            editRating.setText(intent.getStringExtra(EXTRA_RATING));
            editType.setText(intent.getStringExtra(EXTRA_TYPE));
            editStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
            editEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
        } else{
            setTitle("Add trip");
        }
    }

    private void saveTrip() {
        String title = editTitle.getText().toString();
        int image = R.drawable.blank;
        String destination = editDestination.getText().toString();
        String price = "$"+ editPrice.getText().toString();
        String rating = editRating.getText().toString();
//        String type = ((RadioButton)findViewById(editType.getCheckedRadioButtonId())).getText().toString();
//        String startDate = editStartDate.getDayOfMonth() + "." + editStartDate.getMonth() + "." + editStartDate.getYear();
//        String endDate = editEndDate.getDayOfMonth() + "." + editEndDate.getMonth() + "." + editEndDate.getYear();
        String type = editType.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();

        if(title.isEmpty() || destination.isEmpty() || rating.isEmpty() || type.isEmpty() || startDate.isEmpty() || endDate.isEmpty()){
            Toast.makeText(AddEditTripActivity.this, "Please insert title, destination, price, type, rating, start date and end date for your trip", Toast.LENGTH_LONG).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_IMAGE, image);
        data.putExtra(EXTRA_DESTINATION, destination);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_RATING, rating);
        data.putExtra(EXTRA_TYPE, type);
        data.putExtra(EXTRA_START_DATE, startDate);
        data.putExtra(EXTRA_END_DATE, endDate);
        data.putExtra(EXTRA_IS_FAVOURITE, false);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_trip:
                saveTrip();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
