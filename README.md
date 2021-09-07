
# InkCommons
Library project with various common libraries for my projects.

General badge: [![](https://jitpack.io/v/inlacou/InkCommons.svg)](https://jitpack.io/#inlacou/InkCommons)

## InkKotlinCommons
Includes some new common classes.

Import with `implementation 'com.github.inlacou.InkCommons:InkKotlinCommons:'` [![](https://jitpack.io/v/inlacou/InkCommons.svg)](https://jitpack.io/#inlacou/InkCommons)

### Lists
* FIFO - First input first output.
* FILO - First input last output.
* LIFO - Last input first output.
* LILO - Last input last output.
* NoDupleMutableList - MutableList that doesn't allow duplicates.

### Observables
* AnyObs - Execute given callback and return whatever the callback does.
* CombineSequentialObs - Tries to execute the given Observable list in the order given. Has three behaviours for when an error is detected.
    * StopSuccessfullyOnError  - Stops with a success returning retrieved data
    * StopWronglyOnError       - Stops with an error returning error
    * AlwaysTryAll             - Execute all always, and return retrieved data with nulls instead when an error happened
* CombineSequentialSingles - Tries to execute the given Single list in the order given. Has three behaviours for when an error is detected. Has the same three behaviours as CombineSequentialObs for when an error is detected.
* ReInterval - 

### Others
* Either - Scala's Either class. Explanation [here](https://levelup.gitconnected.com/3-useful-things-about-scalas-either-class-that-you-want-to-know-42adbe090e60).
* Optional - Wraps any nullable variable, allowing you to send nullable variables with it over Rx streams.
* Relevanceable - Interface to calculate relevante of a given item. It includes one algorithm for it, though more can be defined and any used.

## InkKotlinExtensions
Import with `implementation 'com.github.inlacou.InkCommons:InkKotlinExtensions:'` [![](https://jitpack.io/v/inlacou/InkCommons.svg)](https://jitpack.io/#inlacou/InkCommons)

Groups multiple kotlin extensions, usable on virtually any Java Kotlin Application, whether it be Android, Java Desktop or other. It includes the following themes:
* ByteArray
* Byte
* Calendar
* Double
* Hexadecimal
* Int
* Json
* List
* String
* Time
* Other

## InkAnimationTypes

Implementation of easetypes as Android Interpolators. Visual representation here: https://easings.net/en

Import with `implementation 'com.github.inlacou.InkCommons:InkAnimationTypes:'` [![](https://jitpack.io/v/inlacou/InkCommons.svg)](https://jitpack.io/#inlacou/InkCommons)

Provided ease types are:
<details>
  <summary>Click to expand/collapse</summary>
  
* Linear
* EaseOutQuad
* EaseInOutBounce
* EaseInCirc
* NoEase
* EaseInSine
* EaseInQuart
* EaseInBounce
* EaseOutBounce
* EaseOutQuint
* EaseInOutExpo
* EaseInElastic
* EaseInOutBack
* EaseInOutQuad
* EaseOutBack
* EaseOutExpo
* EaseOutCubic
* EaseInOutCirc
* EaseInExpo
* EaseInBack
* EaseOutCirc
* EaseInOutCubic
* EaseInCubic
* EaseInOutElastic
* EaseInOutQuint
* EaseOutSine
* EaseInQuad
* EaseOutQuart
* EaseInOutSine
* EaseInOutQuart
* EaseOutElastic
* EaseInQuint
</details>


## InkAndroidExtensions
Import with `implementation 'com.github.inlacou.InkCommons:InkAndroidExtensions:'` [![](https://jitpack.io/v/inlacou/InkCommons.svg)](https://jitpack.io/#inlacou/InkCommons)

Groups multiple kotlin extensions, usable only on Android applications. It includes the following themes:
* ActivityExtensions
* CalendarExtensions
* Extensions
* FileExtensions
* FragmentExtensions
* ImageExtensions
* JsonExtensions
* RxExtensions
* StringExtensions
* ImageViewExtensions
* RecyclerViewExtensions
* ScrollViewExtensions
* TextViewExtensions
* ViewExtensions
* WebViewExtensions

## InkBetterAndroidViews
Import with `implementation 'com.github.inlacou.InkCommons:InkBetterAndroidViews:'` [![](https://jitpack.io/v/inlacou/InkCommons.svg)](https://jitpack.io/#inlacou/InkCommons)

### Adapters
* GenericListAdapter
* GenericRvAdapter
* SimpleCheckboxRvAdapter
* SimpleRadioRvAdapter
* SimpleRvAdapter
### Textviews
* CheckedTextView
* TextViewBitmap
### RecyclerView Decorators
* BottomOffsetItemDecoration
* SimpleItemDecoration
### SnapHelpers
* StartSnapHelper
### RelativeLayouts
* SquareRelativeHeightLayout
* SquareRelativeWidthLayout
### ScrollViews
* BetterNestedScrollView
* InteractiveScrollView
### Viewpagers
* NoScrollViewPager
* WrapHeightViewPager
### Others
* DottedDividerView
* BetterSpinner

Also includes some extensions, allowing better view listener interaction through Rx:
* `View.clicks(): Observable\<View>`
* `View.longClicks(cosumeEvent: Boolean = true): Observable\<View>`
* `AutoCompleteTextView.itemClicks(): Observable\<Int>`
* `View.touchs(): Observable\<Triple<MotionEvent, Float, Float>>`
* `TextView.textChanges(): Observable\<String>`
* `CheckBox.checkedChanges(): Observable\<Boolean>`
* `View.layoutChanges(): Observable\<Triple<View, LayoutChangeObs.Dimensions, LayoutChangeObs.Dimensions>>`
* `View.longClickSpeedingFiringIntervals(breakpointsAndSpeeds: List<Pair<Int, Int>>? = null): Observable\<Long>`
