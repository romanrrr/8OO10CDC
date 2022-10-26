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

    fun searchLongestMatch(word: String): String? {
        var currentNode: Node = root
        val chars = word.lowercase().toCharArray()

        var wordEnd: Int? = null

        for(i in chars.indices){
            currentNode.children[chars[i]]?.let { node ->
                currentNode = node
                if(currentNode.isWord){
                    wordEnd = i
                }
            } ?: break
        }
        return wordEnd?.let {
            return String(chars.slice(0 .. it).toCharArray())
        }
    }
}