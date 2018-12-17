OUT = 17.jar
INPUT = 17.kt
DATA = input

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
