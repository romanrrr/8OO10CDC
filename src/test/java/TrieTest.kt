import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TrieTest {

    private val trie = Trie()

    @Test
    fun trieTest(){
        trie.insertWord("box")
        assertEquals(true, trie.searchExact("box"))
        assertEquals(false, trie.searchExact("bo"))
        assertEquals(false, trie.searchExact("boxes"))

        trie.insertWord("boxes")
        assertEquals(true, trie.searchExact("box"))
        assertEquals(false, trie.searchExact("bo"))
        assertEquals(true, trie.searchExact("boxes"))

        trie.insertWord("fox")
        assertEquals(true, trie.searchExact("fox"))
        assertEquals(false, trie.searchExact("fo"))
        assertEquals(false, trie.searchExact("foxes"))

        assertContains(trie.searchAll("foxes").words, "fox")
        assertContains(trie.searchAll("foxes").words, "foxes")
        assertEquals(4, trie.searchAll("foxe").longestMatch)
    }
}