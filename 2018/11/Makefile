OUT = 11.jar
INPUT = 11.kt
DATA = 9995

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
