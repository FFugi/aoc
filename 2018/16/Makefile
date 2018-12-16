OUT = 16.jar
INPUT = 16.kt
DATA = input1 input2

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
