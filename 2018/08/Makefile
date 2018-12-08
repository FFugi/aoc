OUT = 08.jar
INPUT = 08.kt
DATA = input

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
