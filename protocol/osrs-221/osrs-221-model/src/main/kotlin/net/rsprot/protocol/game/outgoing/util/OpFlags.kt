package net.rsprot.protocol.game.outgoing.util

/**
 * Op flags are used to hide or show certain right-click options on various
 * interactable entities.
 * This is a helper class to create
 */
@Suppress("MemberVisibilityCanBePrivate")
@JvmInline
public value class OpFlags(
    private val packed: Byte,
) {
    public constructor(
        op1: Boolean,
        op2: Boolean,
        op3: Boolean,
        op4: Boolean,
        op5: Boolean,
    ) : this(
        toInt(op1)
            .or(toInt(op2) shl 1)
            .or(toInt(op3) shl 2)
            .or(toInt(op4) shl 3)
            .or(toInt(op5) shl 4)
            .toByte(),
    )

    public val value: Int
        get() = packed.toInt()

    /**
     * Checks if an option is enabled.
     * @param op the id of the option, in range of 1 to 5 (inclusive).
     * @return whether the given option is enabled.
     * @throws IllegalArgumentException if the option is out of bounds (not in range of 1..5)
     */
    public fun isEnabled(op: Int): Boolean {
        require(op in 1..5) {
            "Unexpected op value: $op"
        }
        val index = op - 1
        val flag = 1 shl index
        return packed.toInt() and flag != 0
    }

    override fun toString(): String =
        "OpFlags(" +
            "op1=${isEnabled(1)}, " +
            "op2=${isEnabled(2)}, " +
            "op3=${isEnabled(3)}, " +
            "op4=${isEnabled(4)}, " +
            "op5=${isEnabled(5)}" +
            ")"

    public companion object {
        /**
         * A constant flag for 'show all options' on an entity.
         */
        public val ALL_SHOWN: OpFlags = OpFlags(-1)

        /**
         * A constant flag for 'show no options' on an entity.
         */
        public val NONE_SHOWN: OpFlags = OpFlags(0)

        /**
         * Turns the boolean to an integer.
         * @return 1 if the boolean is enabled, 0 otherwise.
         */
        private fun toInt(value: Boolean): Int = if (value) 1 else 0
    }
}
