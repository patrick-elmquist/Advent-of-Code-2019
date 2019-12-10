package extension

val String.csv: List<String> get() = this.split(",")