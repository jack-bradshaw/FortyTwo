package com.matthewtamlin.fortytwo.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Provides access to the example activities.
 */
public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

	/**
	 * On click listener method for the action button.
	 *
	 * @param v
	 * 		the view which was clicked, not null
	 */
	public void showSingleSelectionActivity(final View v) {
		startActivity(new Intent(this, SingleSelectionActivity.class));
	}
}