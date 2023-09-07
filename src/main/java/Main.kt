import kotlin.math.min

fun main() {
    val trie = buildTrieForResource("words.txt")

    println("Enter letters to solve")
    val letters = "SAGNETISD".lowercase().toCharArray()
    //val letters = readLine()!!.toCharArray()
    println(searchForAllPermutations(trie, letters).subList(0, 10))
}

fun searchForAllPermutations(trie: Trie, chars: CharArray): List<String> {
    val results = HashSet<String>()

    val size = chars.size
    chars.sort()

    var isFinished = false

    while (!isFinished) {
        val searchResult = trie.searchAll(String(chars))
        results.addAll(searchResult.words)

        var i = min(searchResult.longestMatch, size - 2)
        //by doing this we throw away all permutations from index i
        //which are not in the dictionary
        chars.sortDescending(i + 1, size)

        while (i >= 0) {
            if (chars[i] < chars[i + 1])
                break
            i--
        }

        if (i == -1) {
            isFinished = true
        } else {
            val ceilIndex = chars.findCeil(i)
            chars.swap(i, ceilIndex)
            chars.reverse(i + 1, size - 1)
        }
    }
    return results.toList().sortedByDescending { it.length }
}

private fun CharArray.swap(a: Int, b: Int) {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
}

fun CharArray.reverse(fromIndex: Int, toIndex: Int) {
    var l = fromIndex
    var r = toIndex
    while (l < r) {
        swap(l, r)
        l++
        r--
    }
}

// This function finds the index of the smallest
// character which is greater than this[fromIndex] character
// and goes after fromIndex
fun CharArray.findCeil(fromIndex: Int): Int {
    var ceilIndex = fromIndex + 1

    for (i in fromIndex + 1 until this.size){
        if (this[i] > this[fromIndex] && this[i] <= this[ceilIndex]){
            ceilIndex = i
        }
    }
    return ceilIndex
}
