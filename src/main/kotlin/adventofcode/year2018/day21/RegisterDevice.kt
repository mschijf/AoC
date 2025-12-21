package adventofcode.year2018.day21

class RegisterDevice(
    private val ipRegister: Int,
    private val register: MutableList<Int> = mutableListOf(0,0,0,0,0,0)) {

    //    When the instruction pointer is bound to a register, its value is written to that register
    //    just before each instruction is executed, and the value of that register is written back to the
    //    instruction pointer immediately after each instruction finishes execution.

    fun executeOperationList(operationList: List<Operation>, magicRegister: Int, magicInstruction: Int, debug: Boolean=false): Sequence<Int> = sequence {
        var ip = 0
        val seen = LinkedHashSet<Int>()
        while (ip < operationList.size) {

            register[ipRegister] = ip
            if (debug)
                print("$ip $this ${operationList[ip]} ")

            instructionByOpcode(operationList[ip])

            if (debug)
                println("$this")

            ip = register[ipRegister]+1

            if(ip == magicInstruction) {
                if(register[magicRegister] in seen) {
                    yield(seen.last())
                    return@sequence
                }
                seen += register[magicRegister]
                yield(register[magicRegister])
            }
        }
    }

    private fun instructionByOpcode(operation: Operation) {
        instructionByName(operation.opcode, operation.a, operation.b, operation.c)
    }

    private fun instructionByName(opcode: OpCode, a: Int, b: Int, c: Int) {
        when (opcode) {
            OpCode.ADDR -> register[c] = register[a] + register[b] //addr (add register) Stores into register C the result of adding register A and register B.
            OpCode.ADDI -> register[c] = register[a] + b //addi (add immediate) stores into register C the result of adding register A and value B.

            OpCode.MULR -> register[c] = register[a] * register[b]//mulr (multiply register) stores into register C the result of multiplying register A and register B.
            OpCode.MULI -> register[c] = register[a] * b //muli (multiply immediate) stores into register C the result of multiplying register A and value B.

            OpCode.BANR -> register[c] = register[a] and register[b] //banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
            OpCode.BANI -> register[c] = register[a] and b //bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.

            OpCode.BORR -> register[c] = register[a] or register[b] //borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
            OpCode.BORI -> register[c] = register[a] or b //bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.

            OpCode.SETR -> register[c] = register[a] //setr (set register) copies the contents of register A into register C. (Input B is ignored.)
            OpCode.SETI -> register[c] = a //seti (set immediate) stores value A into register C. (Input B is ignored.)

            OpCode.GTIR -> register[c] = if (a > register[b]) 1 else 0//gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
            OpCode.GTRI -> register[c] = if (register[a] > b) 1 else 0 //gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
            OpCode.GTRR -> register[c] = if (register[a] > register[b]) 1 else 0 //gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.

            OpCode.EQIR -> register[c] = if (a == register[b]) 1 else 0 //eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
            OpCode.EQRI -> register[c] = if (register[a] == b) 1 else 0 //eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
            OpCode.EQRR -> register[c] = if (register[a] == register[b]) 1 else 0 //eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
        }
    }

    override fun toString() = "[${register.joinToString(", ")}]"
}

enum class OpCode {
    ADDR, ADDI,  MULR, MULI,  BANR, BANI, BORR, BORI,  SETR, SETI,
    GTIR, GTRI, GTRR,  EQIR, EQRI, EQRR,
}


data class Operation (val opcode: OpCode, val a: Int, val b: Int, val c: Int) {
    companion object {
        fun from (s: String): Operation {
            return s.split(" ").run{Operation(OpCode.valueOf(this[0].uppercase()), this[1].toInt(), this[2].toInt(), this[3].toInt())}
        }
    }
}