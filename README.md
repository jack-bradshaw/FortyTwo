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
The following implementations are provided for the Answer interface:
- `ImmutableAnswer`: An answer where the values are set and fixed at instantiation.
- `PojoAnswer`: An answer which provides getters and setters for accessing and changing the data.

The following implementations are provided for the AnswerView interface:
- `SimpleAnswerCard`: An abstract class which can be extended to make CardView based AnswerViews.
- `DecoratedAnswerCard`: A subclass of SimpleAnswerCard which can be customised by supplying one or more Decorator objects. Two implementations of the Decorator interface have been provided: ColorFadeDecorator and AlphaDecorator (both shown in the above gifs).

The SelectionLimitAnswerGroup is the only provided implementation of the AnswerGroup interface, however it is flexible enough to suit most needs. The view can be configured to limit the number of selected answers, and it can be set to disallow selection changes when the answers have been submitted.

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
