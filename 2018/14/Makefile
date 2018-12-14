OUT = 14.jar
INPUT = 14.kt
DATA = 846601

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) $(DATA)
