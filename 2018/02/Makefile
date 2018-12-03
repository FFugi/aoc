OUT = 02.jar
INPUT = checksum.kt

default:
	kotlinc $(INPUT) -include-runtime -d $(OUT)
	java -jar $(OUT) input
