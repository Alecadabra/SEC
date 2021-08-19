# Worksheet 3

## Question 1

This will wait on the future and wait on the GUI thread, freezing it.

Fix this by making a new thread and running it to get the image and then
run the `Platform.runLater` logic with the image.

## Question 2

### (a)

Use a synchronous queue in each user and each helper.

Unbounded linked blocking queue in the AI, with the helper sending entire
conversations to it.

### (b)

## Question 3

Use multiple threads of the AI using thread pools, and use a LUT to
distinguish between users on the AI side.

## Question 4