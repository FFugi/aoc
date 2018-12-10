OUT = 10.jar
INPUT = 10.kt
DATA = input

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
