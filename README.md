# MNIST
A simple MNIST implementation, used to label handwritten digits from 0-9. The goal of this repository was to show how to create a machine learning model in TensorFlow with Keras and then deploy it across a variety of devices (in this case, through a website and on Android devices).

## About
In this case I use a convolutional neural network to determine this. This model consists of 2 convolutional layers (with pooling), a dense layer, and a final layer of 10 outputs. A 50% dropout is applied to the final 2 layers.

## Accuracy
The model currently achieves a 99.1% accuracy on average. While not state of the art, it is good enough for this simple demo, and I do not currently have plans to improve upon going forward. This is to simply show a potential deployment of a machine learning model using TensorFlow, Keras, TensorFlow JS, and TensorFlow Lite.

## Project Structure
### Python Files
**trainer.py** is used to create and train the model, and provides some testing results at the end. The model is saved to **model.h5py**, which can then be converted to either a TensorFlow JS model or a TensorFlow Lite model (see below for more information on each of these).

**tester.py** is used to do a quick set of tests on the previously created model. It performs the same functions done at the end of creating the model.

### Web Application
The web version uses TensorFlow JS to allow a users web browser to run the model. This means that you **do not** need a server to let users run your model, but you sacrifice security in that anyone can then download and use your model offline.

The project is structured to be a very plain and simple web page, just display a canvas for users to draw on and the prediction the model makes (no confidence scores are shown, though these can also be retrieved).

A work demo of this can be seen at [my personal website](https://www.geraldmcalister.com/projects.html) under the "MNIST Demo" project. Please note that the web demo currently does not work from my website on FireFox Android mobile browsers, nor on iOS based Safari browsers, due to how canvases in iFrames work on these browsers/devices.

### Android Application
The Android application version uses TensorFlow Lite to run natively on an Android device. This provides the benefit once again of meaning that you **do not** need a server to let users run your model, but again sacrifices security as anyone can use the model offline.

The project uses an EaselView component, designed to allow users to draw on it. You can select a custom background color and pen color, and are then able to allow users to draw on it using that configuration. Note as well that there is a custom aspect ratio attribute that is used in order to ensure that the images passed to the model are correct.

**Please note that the Android application has only been tested on a Galaxy S8 running Android 8.0 Oreo.**

### Converting the Models
The **model.h5py** file that is generated from **trainer.py** is the main model that you can convert between the different types. To convert it to a TensorFlow JS compatible model, run the command **"tensorflowjs\_converter --input\_format keras model.h5py web/model"** to update the model for the web application. To convert it to a TensorFlow Lite model, run the command **"tflite\_convert --keras\_model\_file=model.h5py --output\_file=android/app/src/main/res/raw/model.tflite"** to update the model for the Android application.

## Images
![Android Screenshot 1](/screenshots/mnist-android-1.png?raw=true "Android Screenshot 1")
![Android Screenshot 2](/screenshots/mnist-android-2.png?raw=true "Android Screenshot 2")
![Web Screenshot 1](/screenshots/mnist-web-1.png?raw=true "Web Screenshot 1")
![Web Screenshot 2](/screenshots/mnist-web-2.png?raw=true "Web Screenshot 2")
