import extension.asLongs
import extension.csv
import util.Day
import util.IntCode

// Answer #1: 23057
// Answer #2: 15156

private const val NAT_ADDRESS = 255L
private val NIC_ADDRESSES = 0L until 50L

fun main() {
    Day(n = 23) {
        answer {
            val program = lines.first().csv.asLongs()
            rebuildNetwork(program, breakOnNat = true)
        }
        answer {
            val program = lines.first().csv.asLongs()
            rebuildNetwork(program, breakOnNat = false)
        }
    }
}

private fun rebuildNetwork(program: List<Long>, breakOnNat: Boolean): Long {
    val nics = NIC_ADDRESSES.map { address -> address to IntCode(program) }
            .onEach { (address, nic) -> nic.run(address) }
            .toMap()

    var natMemory: Packet? = null
    var lastNatY: Long? = null
    while (true) {
        val packets = nics.values.flatMap { nic -> nic.outputs.toPackets() }

        val deliveredAddresses = packets.map { (address, x, y) ->
            when {
                address == NAT_ADDRESS && breakOnNat -> return y
                address == NAT_ADDRESS -> natMemory = Packet(0, x, y)
                else -> address.also { nics[address]?.run(x, y) }
            }
        }

        nics.filterKeys { it !in deliveredAddresses }
                .forEach { (_, nic) -> nic.run(-1) }

        val isIdle = packets.filter { it.address != NAT_ADDRESS }.none()
        val natPacket = natMemory
        if (isIdle && natPacket != null) {
            if (lastNatY == natPacket.y) return natPacket.y
            nics[natPacket.address]?.run(natPacket.x, natPacket.y)
            lastNatY = natPacket.y
            natMemory = null
        }
    }
}

data class Packet(val address: Long, val x: Long, val y: Long) {
    constructor(values: List<Long>) : this(values[0], values[1], values[2])
}

private fun List<Long>.toPackets() = chunked(3).map { Packet(it) }
