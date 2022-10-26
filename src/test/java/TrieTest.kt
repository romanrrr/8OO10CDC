import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TrieTest {

    private val trie = Trie()

    @Test
    fun trieTest(){
        trie.insertWord("box")
        assertEquals(true, trie.search("box"))
        assertEquals(false, trie.search("bo"))
        assertEquals(false, trie.search("boxes"))

        trie.insertWord("boxes")
        assertEquals(true, trie.search("box"))
        assertEquals(false, trie.search("bo"))
        assertEquals(true, trie.search("boxes"))

        trie.insertWord("fox")
        assertEquals(true, trie.search("fox"))
        assertEquals(false, trie.search("fo"))
        assertEquals(false, trie.search("foxes"))
    }
}