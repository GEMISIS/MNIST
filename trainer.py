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

# Create the model.
model = keras.Sequential([
    keras.layers.Flatten(input_shape=(28,28,)),
    keras.layers.Dense(512, activation=tf.nn.relu),
    keras.layers.Dropout(0.2),
    keras.layers.Dense(10, activation=tf.nn.softmax)
])
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

# Train the model.
model.fit(train_images, train_labels, epochs=5)

# Save the model.
model.save('site/js/model.h5py')

# Do a quick evaluation
test_loss, test_acc = model.evaluate(test_images, test_labels)
print('Test accuracy:', test_acc)

# Now get a prediction.
predictions = model.predict(test_images)

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
