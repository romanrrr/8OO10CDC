fun main() {
    val trie = buildTrieForResource("words.txt")

    println("Enter letters to solve")
    //val letters = "SAGNETISD".lowercase().toCharArray()
    val letters = readLine()!!.toCharArray()
    println(searchForAllPermutations(trie, letters))
}

/*todo
Optimize the permutation generator and search.
If for letters ABCDE, combination AB can't be found in a dictionary
there is no point to generate different permutations for CDE and search for it.
*/
fun searchForAllPermutations(trie: Trie, chars: CharArray) : List<String> {
    val results = HashSet<String>()

    val n = chars.size
    val indexes = IntArray(n) { 0 }
    var i = 0

    //do we need to find all possible words or only the longest variant? E.g. 'cat' and 'cats'
    trie.searchLongestMatch(String(chars))?.let {
        results.add(it)
    }

    while (i < n) {
        if (indexes[i] < i) {
            swap(chars, if (i % 2 == 0) 0 else indexes[i], i)

            trie.searchLongestMatch(String(chars))?.let {
                results.add(it)
            }

            indexes[i]++
            i = 0
        } else {
            indexes[i] = 0
            i++
        }
    }
    return results.toList().sortedByDescending { it.length }
}

private fun swap(input: CharArray, a: Int, b: Int) {
    val tmp = input[a]
    input[a] = input[b]
    input[b] = tmp
}