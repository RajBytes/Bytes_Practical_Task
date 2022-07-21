# BytesTest

## This is the Practical Task from Sowingo.

### Definition Of Task As below.

> these are iOS designs, please convert them to the best of your ability to Andorid Material styling.

> Figma Design Screens

> https://www.figma.com/file/5Yrn3LkZHyQTtRLe4MV37i/iOS_Test?node-id=1187%3A47

### Task Challenge

> display all listed products with fetch from products endpoint

> search bar: filter list in real time through the search input on top with the name

> make changes to favorite status with a call to the favourites endpoint, but keep state changes in memory (favourite default vs favourite active)

> implement standard pull-to-refresh to reset the filter state (all products set as favourite default)

## Task Submission Includes below Points:

> Hilt Dependency Injection.

> Kotlin As Language.

> Multi Threading with Kotlin Coroutines.

> MVVM Architecture.


> As per the Definition we used in this practical some base architecture with the help of MVVM.
> With the help of Base Architecture we can utilize below Features.

1. We achieved the central Api call with different thread (IO Thread) which helps to utilize the at
   maximum level.
2. We achieved the central response handing mechanism by which we can handle the multiple success
   and failure cases.
3. We also added the common progress and toast/snackbar message display which helps to reduce the
   code rewrite and maximum utilization and single change modification features.
4. We also added the some useful implementation like **UnAuthorize User**, **Retry Policy**, **No
   Internet** etc.,
5. We also added the Generic Type Utilization with ViewModel and Data Binding where we do not have
   to write small piece of code everytime.
6. We also used the Generic RecycleView Implementation where we do not have to write the each time a
   new adapter to bind with Recycleview but we just have to assign the Object list and single item
   layout.

## Screens in Application.

1. Splash Screen.
2. Product List Screen.

> Below I am attaching the video for the reference on same.

[![Everything Is AWESOME](https://i9.ytimg.com/vi_webp/-TgDiIVFsSA/mqdefault.webp?sqp=CMiN5ZYG&rs=AOn4CLB1k9bVHvx4Cc8YslYA90fJAaUeEg)](https://youtu.be/-TgDiIVFsSA "Everything Is AWESOME")


