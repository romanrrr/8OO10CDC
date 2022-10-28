data class TrieSearchResult(val words: List<String>, val longestMatch: Int)

class Trie {

    private class Node {
        val children: MutableMap<Char, Node> = HashMap()
        var isWord: Boolean = false
    }

    private val root: Node = Node()

    fun insertWord(word: String) {
        var currentNode = root
        word.lowercase().toCharArray().forEach {
            currentNode = currentNode.children.getOrPut(it) { Node() }
        }
        currentNode.isWord = true
    }

    fun searchExact(word: String): Boolean {
        var currentNode: Node = root
        word.lowercase().toCharArray().forEach {
            currentNode.children[it]?.let { node ->
                currentNode = node
            } ?: return false
        }
        return currentNode.isWord
    }

    fun searchAll(word: String): TrieSearchResult {
        var currentNode: Node = root
        val chars = word.lowercase().toCharArray()

        var matchLength = 0

        val words = ArrayList<String>()

        for(i in chars.indices){
            currentNode.children[chars[i]]?.let { node ->
                currentNode = node
                matchLength += 1
                if(node.isWord){
                    words.add(String(chars.slice(0 .. i).toCharArray()))
                }

            } ?: break
        }
        return TrieSearchResult(words, matchLength)
    }
}