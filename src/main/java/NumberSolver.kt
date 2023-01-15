import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class NumberSolver @JvmOverloads internal constructor(
    var target: Int,
    var nums: List<Int>,
    var findAllSolutions: Boolean = true
) {
    private val intRegex = """^[+-]*\d*$""".toRegex()

    var mem: HashMap<String, HashMap<Int, HashSet<String>>> = HashMap()
    var solutionFound = false

    fun solve(): Set<String>? {
        val result = solve(nums.toMutableList())
        return result[target]
    }

    private fun solve(nums: MutableList<Int>): HashMap<Int, HashSet<String>> {
        val result = HashMap<Int, HashSet<String>>()
        if (!findAllSolutions && solutionFound) return result
        if (nums.size == 1) {
            val x = nums[0]
            addResult(result, x, "$x")
            return result
        }
        nums.sort()
        val numsToString = nums.joinToString(",")
        if (mem.containsKey(numsToString)) return mem[numsToString]!!
        for (subset in subsets(nums)) {
            val comp = subtractList(nums, subset)
            if (subset.isEmpty() || comp.isEmpty() || subset.size < comp.size) continue
            val s1 = solve(subset.toMutableList())
            val s2 = solve(comp.toMutableList())
            for (r1 in s1.keys) {
                addResults(result, r1, s1[r1]!!)
                for (r2 in s2.keys) {
                    addResults(result, r2, s2[r2]!!)
                    if(r1 > r2) {
                        addResults(result, r1 + r2, comb(s1[r1]!!, "+", s2[r2]!!))
                        addResults(result, r1 * r2, comb(s1[r1]!!, "*", s2[r2]!!))
                    }

                    addResults(result, r1 - r2, comb(s1[r1]!!, "-", s2[r2]!!))
                    addResults(result, r2 - r1, comb(s2[r2]!!, "-", s1[r1]!!))
                    if (r1 != 0 && r2 % r1 == 0) {
                        addResults(result, r2 / r1, comb(s2[r2]!!, "/", s1[r1]!!))
                    }
                    if (r2 != 0 && r1 % r2 == 0) {
                        addResults(result, r1 / r2, comb(s1[r1]!!, "/", s2[r2]!!))
                    }
                }
            }
        }
        mem[numsToString] = result
        if (result.containsKey(target)) {
            solutionFound = true
        }
        return result
    }

    private fun subsets(nums: List<Int>): List<List<Int>> {
        val result: MutableList<List<Int>> = ArrayList()
        result.add(LinkedList<Int>().apply { add(nums[0]) })
        if (nums.size == 1) {
            return result
        }
        val temp = nums.toMutableList()
        val x = temp.removeAt(0)
        for (part in subsets(temp)) {
            result.add(ArrayList(part))
            result.add(mutableListOf(x).apply { addAll(part)})
        }
        return result
    }

    private fun subtractList(complete: List<Int>, subset: List<Int>): List<Int> {
        return complete.filter { !subset.contains(it) }
    }

    private fun isSingleInt(x: String): Boolean {
        return x.matches(intRegex)
    }

    private fun addResult(map: HashMap<Int, HashSet<String>>, key: Int, solution: String) {
        val set = map.getOrPut(key){ HashSet() }
        set.add(solution)
    }

    private fun addResults(map: HashMap<Int, HashSet<String>>, key: Int, solutions: HashSet<String>) {
        val set = map.getOrPut(key){ HashSet() }
        set.addAll(solutions)
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