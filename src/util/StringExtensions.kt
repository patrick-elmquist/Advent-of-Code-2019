package util

val String.csv: List<String> get() = this.split(",")