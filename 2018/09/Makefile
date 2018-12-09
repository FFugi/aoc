OUT = 09.jar
INPUT = 09.kt
DATA = input

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
