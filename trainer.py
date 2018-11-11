# TensorFlow and tf.keras
import tensorflow as tf
from tensorflow import keras

# Helper libraries
import numpy as np
import matplotlib.pyplot as plt

# Load the data.
(train_images, train_labels), (test_images, test_labels) = keras.datasets.mnist.load_data()

# Create some class names.
class_names = ['0', '1', '2', '3', '4', 
               '5', '6', '7', '8', '9']

# Pre-process the data.
train_images = train_images / 255.0
test_images = test_images / 255.0
train_images = train_images.reshape(train_images.shape[0], 28, 28, 1)
test_images = test_images.reshape(test_images.shape[0], 28, 28, 1)

# Create the model.
model = keras.Sequential()
model.add(keras.layers.Conv2D(32, kernel_size=(5, 5),
                 activation='relu',
                 input_shape=(28, 28, 1)))
model.add(keras.layers.MaxPooling2D(pool_size=(2, 2)))
model.add(keras.layers.Conv2D(64, (5, 5), activation='relu'))
model.add(keras.layers.MaxPooling2D(pool_size=(2, 2)))
model.add(keras.layers.Flatten())
model.add(keras.layers.Dense(1024, activation='relu'))
model.add(keras.layers.Dropout(0.5))
model.add(keras.layers.Dense(10, activation='softmax'))
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

# Train the model.
model.fit(train_images, train_labels, epochs=5)

# Run "tensorflowjs_converter --input_format keras model.h5py web/model" to convert this to a
# Tensorflow JS comaptible model.
# Save the model.
model.save('model.h5py')

# Do a quick evaluation
test_loss, test_acc = model.evaluate(test_images, test_labels)
print('Test loss:', test_loss)
print('Test accuracy:', test_acc)

# Now get a prediction.
predictions = model.predict(test_images)

test_images = test_images.reshape(test_images.shape[0], 28, 28)
# Finally print out a graph of the first few images.
plt.figure(figsize=(10,10))
for i in range(25):
    plt.subplot(5,5,i+1)
    plt.xticks([])
    plt.yticks([])
    plt.grid(False)
    plt.imshow(test_images[i], cmap=plt.cm.binary)
    plt.xlabel(class_names[np.argmax(predictions[i])])
plt.show()
