OUT = 01.jar
INPUT = calibration.kt

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) input
