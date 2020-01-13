AndExMapHelper ![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat) [![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
===================

AndExMapHelper is a library to simplify the work with Android Google Map.
This library has many features such as: 
  - get and show current location
  - add one or many markers on map
  - manage markers icon with a concept called status. you can define an arbitrary count of status that each status has its own related icon. you can change status of markers which also changes the icon of marker.
  - remove a marker from map
  - draw circle and polygone on map
  - filter markers on map by status and also distance
  - ...
  



**This library is also available at JitPack.io**

[![](https://jitpack.io/v/salehyarahmadi/AndExMapHelper.svg)](https://jitpack.io/#salehyarahmadi/AndExMapHelper)



`this library is compatible to androidx`

## Preview
![](https://github.com/salehyarahmadi/AndExAlertDialog/blob/master/andex-alert-dialog.gif)

## Setup
The simplest way to use this library is to add the library as dependency to your build.

## Gradle

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency
  
    // builde.gradle(project level)
    dependencies {    
	      classpath 'com.google.gms:google-services:4.2.0'
	}

	// builde.gradle(app level)
    dependencies {    
	      implementation 'com.github.salehyarahmadi:AndExAlertDialog:v1.0.1'
          implementation 'com.google.android.gms:play-services-maps:17.0.0'
          implementation 'com.google.android.gms:play-services-location:17.0.0'
	}
 

## Usage


    new AndExAlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveBtnText(positiveText)
        .setNegativeBtnText(negativeText)
        .setCancelableOnTouchOutside(boolean)
        .setFont(Font.IRAN_SANS)
        .setImage(image, imagePercent)
        .setEditText(true, false, hintText, InputType.TEXT_MULTI_LINE)
        .OnPositiveClicked(positiveClickListener)
        .OnNegativeClicked(negativeClickListener)
        .setTitleTextColor(color)
        .setMessageTextColor(color)
        .setButtonTextColor(color)
        .build();


    - All of above featuers is optional
    - When you set positive button text, you must set OnPositiveClicked
    - When you set negative button text, you must set OnNegativeClicked
    - for setting image, you have three options:
        1- using image url  --> .setImage("http://image-url/image.jpg" , 20)
        2 -using image file  --> .setImage(new File("image-path-on-device") , 20)
        3- using image resource id in drawable  --> .setImage(R.drawable.image , 20)
    - imagePercent is actually the width of the image in percent


        
 ## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2019 AndExAlertDialog

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
       
