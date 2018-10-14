package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            Log.d("======================", position+"");
            closeOnError();
            return;
        }

        Log.d("======================", position+"");

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView nameTV = findViewById(R.id.name_tv);
        TextView ingredientsTV = findViewById(R.id.ingredients_tv);
        TextView descriptionTV = findViewById(R.id.description_tv);
        TextView alsoKnownAsTV = findViewById(R.id.also_known_tv);
        TextView originTV = findViewById(R.id.origin_tv);

        if (sandwich != null) {
            nameTV.setText(sandwich.getMainName());
            descriptionTV.setText(sandwich.getDescription());
            originTV.setText(sandwich.getPlaceOfOrigin());

            List<String> altNames = sandwich.getAlsoKnownAs();
            if (altNames.size() > 0) {
                alsoKnownAsTV.setText("");
                for (String elem : altNames) {
                    alsoKnownAsTV.append(elem + "\n");
                }
            }

            List<String> ingredientsList = sandwich.getIngredients();
            if (ingredientsList.size() > 0) {
                ingredientsTV.setText("");
                for (String elem : ingredientsList) {
                    ingredientsTV.append(elem + "\n");
                }
            }
        } else {
            closeOnError();
        }

    }
}
