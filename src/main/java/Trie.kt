class Trie {

    private class Node {
        val children: MutableMap<Char, Node> = HashMap()
        var isWord: Boolean = false
    }

    private val root: Node = Node()

    fun insertWord(word: String) {
        var currentNode = root;
        word.lowercase().toCharArray().forEach {
            currentNode = currentNode.children.getOrPut(it) { Node() }
        }
        currentNode.isWord = true;
    }

    fun search(word: String): Boolean {
        var currentNode: Node = root;
        word.lowercase().toCharArray().forEach {
            currentNode.children[it]?.let { node ->
                currentNode = node
            } ?: return false
        }
        return currentNode.isWord
    }
}