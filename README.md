# Tinta.g4

Tinta.g4 is a project I developed in college for compilers course. It uses ANTLR4 to parse code written by the user with the objective of drawing small 2D geometric objects.

## Getting Started

After cloning this project you can find a Makefile, just run ```make``` to build the project.

Now you can create a text file and write the code you wish to run (or just stick to the "Example" file).

Compile your code (this will generate 'Result_Image.py'):
	```cat yourFile | java TintaMain```
Run your compiled code:
	```python Result_Image.py```

In 'Example' file you can find comment code that shows what you can do with it.

### Prerequisites

* [ANTLR4](http://www.antlr.org/)
* OpenCV Python  ```sudo pip install opencv-python```

