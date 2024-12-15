package client

@JvmInline
value class Day(val value: Int) {
    override fun toString(): String = value.toString()
}

@JvmInline
value class Year(val value: Int) {
    override fun toString(): String = value.toString()
}

@JvmInline
value class Part(val value: Int) {
    init {
        check(value == 1 || value == 2)
    }

    override fun toString(): String = value.toString()
}
