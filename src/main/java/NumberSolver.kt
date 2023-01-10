import java.util.*

class NumberSolver @JvmOverloads internal constructor(
    var target: Int,
    var nums: List<Int>,
    var findAllSolutions: Boolean = true
) {
    private val intRegex = """^[+-]*\d*$""".toRegex()

    var mem: HashMap<String, HashMap<Int, HashSet<String>>> = HashMap()
    var solutionFound = false

    fun solve(): Set<String>? {
        val result = solve(LinkedList(nums))
        return result[target]
    }

    private fun solve(nums: LinkedList<Int>): HashMap<Int, HashSet<String>> {
        val result = HashMap<Int, HashSet<String>>()
        if (!findAllSolutions && solutionFound) return result
        if (nums.size == 1) {
            val x = nums[0]
            addToMap(result, x, "$x")
            addToMap(result, -x, "-$x")
            return result
        }
        nums.sort()
        val numsToString = nums.joinToString(",")
        if (mem.containsKey(numsToString)) return mem[numsToString]!!
        for (subset in subsets(nums)) {
            val comp = complement(nums, subset)
            if (subset.size == 0 || comp.size == 0 || subset.size < comp.size) continue
            val s1 = solve(subset)
            val s2 = solve(comp)
            for (r1 in s1.keys) {
                addToMap(result, r1, s1[r1]!!)
                for (r2 in s2.keys) {
                    addToMap(result, r2, s2[r2]!!)
                    addToMap(result, r1 + r2, comb(s1[r1]!!, "+", s2[r2]!!))
                    addToMap(result, r1 - r2, comb(s1[r1]!!, "-", s2[r2]!!))
                    addToMap(result, r2 - r1, comb(s2[r2]!!, "-", s1[r1]!!))
                    if (r1 != 0 && r2 % r1 == 0) {
                        addToMap(result, r2 / r1, comb(s2[r2]!!, "/", s1[r1]!!))
                    }
                    if (r2 != 0 && r1 % r2 == 0) {
                        addToMap(result, r1 / r2, comb(s1[r1]!!, "/", s2[r2]!!))
                    }
                    addToMap(result, r1 * r2, comb(s1[r1]!!, "*", s2[r2]!!))
                }
            }
        }
        mem[numsToString] = result
        if (result.containsKey(target)) {
            solutionFound = true
        }
        return result
    }

    private fun subsets(nums: LinkedList<Int>): List<LinkedList<Int>> {
        val result: MutableList<LinkedList<Int>> = LinkedList()
        result.add(asLinkedList(nums[0]))
        if (nums.size == 1) {
            return result
        }
        val x = nums.removeAt(0)
        for (part in subsets(nums)) {
            val t1 = LinkedList(part)
            result.add(t1)
            val t2 = LinkedList(part)
            t2.add(0, x)
            result.add(t2)
        }
        nums.add(0, x)
        return result
    }

    private fun asLinkedList(x: Int): LinkedList<Int> {
        val result = LinkedList<Int>()
        result.add(x)
        return result
    }

    private fun complement(complete: LinkedList<Int>, subset: LinkedList<Int>): LinkedList<Int> {
        val result = LinkedList<Int>()
        var j = 0
        for (x in complete) {
            if (j == subset.size) {
                result.add(x)
            } else if (x == subset[j]) {
                j++
            } else if (x < subset[j]) {
                result.add(x)
            } else {
                println("This should never happend")
            }
        }
        return result
    }

    private fun isSingleInt(x: String): Boolean {
        return x.matches(intRegex)
    }

    private fun addToMap(map: HashMap<Int, HashSet<String>>, key: Int, solution: String) {
        if (key < 0) return
        if (!map.containsKey(key)) map[key] = HashSet()
        map[key]!!.add(solution)
    }

    private fun addToMap(map: HashMap<Int, HashSet<String>>, key: Int, solutions: HashSet<String>) {
        if (key < 0) return
        if (!map.containsKey(key)) map[key] = HashSet()
        map[key]!!.addAll(solutions)
    }

    private fun comb(s1: HashSet<String>, op: String, s2: HashSet<String>): HashSet<String> {
        val result = HashSet<String>()
        for (a in s1) {
            for (b in s2) {
                result.add(operationToString(a, op, b))
            }
        }
        return result
    }

    private fun operationToString(a: String, op: String, b: String): String {
        if (op == "+") return a + op + b
        if (isSingleInt(a) && isSingleInt(b)) return a + op + b
        if (isSingleInt(a)) return "$a$op($b)"
        return if (isSingleInt(b)) "($a)$op$b" else "($a)$op($b)"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val target = 765
            val nums = listOf(75, 25, 4, 2, 8 ,6)
            val solver = NumberSolver(target, nums)
            val solution = solver.solve()
            if (solution?.isNotEmpty() == true) {
                println("It is possible")
                for (str in solution) {
                    println(str)
                }
            } else {
                println("Its not possible")
            }
        }
    }
}