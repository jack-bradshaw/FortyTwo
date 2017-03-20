# FortyTwo
FortyTwo is the answer to the Ultimate Question of Life, the Universe, and Everything. It's also a UI library designed for displaying multiple choice answers in Android apps, for example:

<img src="https://raw.githubusercontent.com/MatthewTamlin/FortyTwo/master/artwork/single_selection.gif" width="425"/> <img src="https://raw.githubusercontent.com/MatthewTamlin/FortyTwo/master/artwork/multiple_selection.gif" width="425"/> 

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:forty-two:1.0.0’` to your gradle build file to use the latest version. Older versions are available in the [maven repo](https://bintray.com/matthewtamlin/maven/FortyTwo).

## Usage
There are three key interfaces in this library:
- Answer: Contains the actual data to display.
- AnswerView: Displays a single answer in the UI.
- AnswerGroup: Displays multiple AnswerViews and coordinates the user’s interaction with them.

Define your answers by implementing the Answer interface or instantating one of the provided implementations.
```java
// Directly implement the interface
Answer answer1 = new Answer() {
    public CharSequence getText() {
        return "incorrect answer";
    }
    
    public boolean isCorrect() {
        return false;
    };
}
    
// Use the PojoAnswer class
Answer answer2 = new PojoAnswer("this is the right answer", true);
answer2.setText("actually I changed my mind, this answer is wrong too");
answer2.setCorrectness(false);

// Use the ImmutableAnswer class
Answer answer3 = new ImmutableAnswer("this is definitely the right answer", true);
```

Add an AnswerGroup to your layout. The SelectionLimitAnswerGroup is the only provided implementation of this interface, but the class is flexible enough to meet most needs.
```xml
<LinearLayout
	xmlns:android="http://schemas.android.com/apk/res/android"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:orientation="vertical">

    <!-- Displays the question -->
	<TextView
		android:id="@+id/question"
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:gravity="center"
		android:padding="8dp"
		android:textSize="20sp"/>

    <!-- Displays the answers -->
	<com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup
		android:id="@+id/answers"
		android:layout_width="match_parent"
		android:layout_height="wrap_content"/>

    <!-- Button for submitting -->
	<Button
		android:id="@+id/submit_button"
		style="@style/Widget.AppCompat.Button.Borderless"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_gravity="center_horizontal"
		android:text="Submit"
		android:textSize="16sp"/>
</LinearLayout>
```

The following implementations are provided for the AnswerView interface:
- `SimpleAnswerCard`: An abstract class which can be extended to make CardView based AnswerViews.
- `DecoratedAnswerCard`: A subclass of SimpleAnswerCard which can be customised by supplying one or more Decorator objects. Two implementations of the Decorator interface have been provided: ColorFadeDecorator and AlphaDecorator (both shown in the above gifs).

For further details, read the Javadoc and have a look at [the example](example/src/main/java/com/matthewtamlin/fortytwo/example).

## License
This library is licensed under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Dependencies and Attribution
This library uses the following open source libraries as level 1 dependencies:
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html), licensed under the Apache 2.0 license.
- [Android Utilities](https://github.com/MatthewTamlin/AndroidUtilities), licensed under the Apache 2.0 license.
- [Apache Commons Collections](https://commons.apache.org/proper/commons-collections/), licensed under the Apache 2.0 license. 

## Compatibility
This library is compatible with Android 12 and up.
