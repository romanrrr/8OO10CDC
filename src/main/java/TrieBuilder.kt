fun buildTrieForResource(path: String) : Trie {
    val trie = Trie()
    val lines = object {}.javaClass.getResourceAsStream(path)?.bufferedReader()?.readLines()
    lines?.forEach(trie::insertWord)
    return trie
}