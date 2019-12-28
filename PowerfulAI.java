import java.util.ArrayList;


public class PowerfulAI implements ScrabbleAI {

    //a list that holds all of the anagram possibilties in hand that are actually a word
     public ArrayList<String>allPossibilities = new ArrayList<String>();

     private static final boolean[] ALL_TILES = {true, true, true, true, true, true, true};


     //The GateKeeper through which this Incrementalist accesses the Board.
     private GateKeeper gateKeeper;

     @Override
     public void setGateKeeper(GateKeeper gateKeeper) {
         this.gateKeeper = gateKeeper;
     }

     @Override
     public ScrabbleMove chooseMove() {
         return findMove();
     }


    //checks for blanks in hand, sets them to equal letter 'e'
     public ArrayList<Character> checkBlanks(ArrayList<Character>hand){
         for (int i =0; i<hand.size();i++){
                 char c = hand.get(i);
                 if (c == '_') {
                     hand.set(i,'e');
                 }
         }
         return hand;
     }

    //Works similar to Incrementalist; tries all words from allPossibilties list on board
     private ScrabbleMove findMove() {
         ArrayList<Character> hand = gateKeeper.getHand();
         checkBlanks(hand);
         PlayWord bestMove = null;
         int bestScore = -1;
         PrintAllWords(hand, root, hand.size());

             for (String word : allPossibilities) {
                 for (int row = 0; row < Board.WIDTH; row++) {
                     for (int col = 0; col < Board.WIDTH; col++) {
                         Location location = new Location(row, col);

                         for (Location direction : new Location[]{Location.HORIZONTAL, Location.VERTICAL}) {
                             try {
                                 gateKeeper.verifyLegality(word, location, direction);
                                 int score = gateKeeper.score(word, location, direction);
                                 if (score > bestScore) {
                                     bestScore = score;
                                     bestMove = new PlayWord(word, location, direction);
                                 }
                             } catch (IllegalMoveException e) {

                                 // It wasn't legal in particular direction; go on to the next one
                             }
                         }
                     }
                 }
             }



         if (bestMove != null) {

             return bestMove;
         }

         return new ExchangeTiles(ALL_TILES);
     }

     // inserts all of enable1.txt into the trie
     static {
         In in = new In("enable1.txt");
         root = new Trie();

         while (in.hasNextLine()) {
             String word = new String();
             word = in.readLine();
             insert(word);

         }

     }


     // Alphabet size
     static final int ALPHABET_SIZE = 26;

    // I borrowed some code from Geeks for Geeks to build this tree https://www.geeksforgeeks.org/trie-insert-and-search/
     // trie node
     static class Trie {
         Trie[] children = new Trie[ALPHABET_SIZE];
         ArrayList<String> allPossibilities = new ArrayList<String>();


         boolean isEndOfWord;


         Trie() {
             isEndOfWord = false;
             for (int i = 0; i < ALPHABET_SIZE; i++)
                 children[i] = null;
         }
     }

     static Trie root;

     //create tree
     static void insert(String key) {
         int level;
         int length = key.length();
         int index;

         Trie pCrawl = root;


         for (level = 0; level < length; level++) {
             index = key.charAt(level) - 'a';
             if (pCrawl.children[index] == null)
                 pCrawl.children[index] = new Trie();

             pCrawl = pCrawl.children[index];
         }

         // mark last node as leaf
         pCrawl.isEndOfWord = true;
     }

     // Returns true if key presents in trie, else false
     static boolean search(String key) {
         int level;
         int length = key.length();
         int index;
         Trie pCrawl = root;

         for (level = 0; level < length; level++) {
             index = key.charAt(level) - 'a';

             if (pCrawl.children[index] == null)
                 return false;


             pCrawl = pCrawl.children[index];
         }

         return (pCrawl != null && pCrawl.isEndOfWord);
     }


     void searchWord(Trie root, boolean Hash[],
                           String str)
    {
        // if we found word in trie / enable1.txt
        if (root.isEndOfWord == true)
            //System.out.println(str);
            allPossibilities.add(str);

        // traverse all child's of current root
        for (int K =0; K < ALPHABET_SIZE; K++)
        {
            if (Hash[K] == true && root.children[K] != null )
            {
                // add current character
                char c = (char) (K + 'a');

                // Recursively search reaming character
                // of word in trie
                searchWord(root.children[K], Hash, str + c);
            }
        }
    }

     // Prints all words present in enable1.txt
      void PrintAllWords(ArrayList<Character> chars, Trie root,
                               int n)
     {
         // create a 'has' array that will store all
         // present character in Arr[]
         boolean[] Hash = new boolean[ALPHABET_SIZE];

         for (int i = 0 ; i < n; i++)

             Hash[chars.get(i) - 'a'] = true;


         // temporary node
         Trie pChild = root ;

         // string to hold output words
         String str = "";

         // Traverse all elements. There are only
         // 26 character possible in char array
         for (int i = 0 ; i < ALPHABET_SIZE ; i++)
         {
             // we start searching for word in enable1.txt

             if (Hash[i] == true && pChild.children[i] != null )
             {
                 str = str+(char)(i + 'a');
                 searchWord(pChild.children[i], Hash, str);
                 str = "";
             }
         }
     }

 }





