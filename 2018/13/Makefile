OUT = 13.jar
INPUT = 13.kt
DATA = input

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
