/*
 * Copyright 2017 Matthew Tamlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	 * On click listener method for launching the single selection activity.
	 *
	 * @param v
	 * 		the view which was clicked, not null
	 */
	public void showSingleSelectionActivity(final View v) {
		startActivity(new Intent(this, SingleSelectionActivity.class));
	}

	/**
	 * On click listener method for launching the multiple selection activity.
	 *
	 * @param v
	 */
	public void showMultipleSelectionActivity(final View v) {
		startActivity(new Intent(this, MultipleSelectionActivity.class));
	}
}