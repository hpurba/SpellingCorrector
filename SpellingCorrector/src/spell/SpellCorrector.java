package spell;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
    Trie trie = new Trie();
    public TreeSet<String>setOfEdits1 = new TreeSet<String>();
    public TreeSet<String>setOfEdits2 = new TreeSet<String>();


    @Override
    public void useDictionary(String dictFileName) throws IOException {
        String word;
        File inFile = new File(dictFileName);
        Scanner scan = new Scanner(inFile);
        while(scan.hasNext()){
            word = scan.next().toLowerCase();
            trie.add(word);
        }
        scan.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        setOfEdits1.clear();
        setOfEdits2.clear();

        inputWord = inputWord.toLowerCase();

        if (inputWord == null){
            return null;
        }
        String suggestedWord = new String();

        if (trie.find(inputWord) != null){
            return inputWord;
        }

        TreeSet<String> inputWordAsSet = new TreeSet<String>();
        inputWordAsSet.add(inputWord);

        // EDIT DISTANCE 1
        setOfEdits1.addAll(getSetOfEdits(inputWordAsSet));

        // If the first edit distance gave us suggested words
        if (!setOfEdits1.isEmpty()){
            suggestedWord = suggestWordWithHighestFrequency(setOfEdits1);
            if (suggestedWord != null){
                return suggestedWord;
            }
        }

        // EDIT DISTANCE 2
        setOfEdits2.addAll(getSetOfEdits(setOfEdits1));
        if (!setOfEdits2.isEmpty()){
            suggestedWord = suggestWordWithHighestFrequency(setOfEdits2);
            if (suggestedWord != null){
                return suggestedWord;
            }
        }
        return null;
    }

   // Returns a setOfEdits
    public TreeSet<String> getSetOfEdits(TreeSet<String> inputWords){
        TreeSet<String> setOfEdits = new TreeSet<String>();

        // Do deletion, transpose, alteration, and insertion
        for(String inputWord : inputWords) {
            setOfEdits.addAll(deletion(inputWord));
            setOfEdits.addAll(transpose(inputWord));
            setOfEdits.addAll(alteration(inputWord));
            setOfEdits.addAll(insertion(inputWord));
        }
        return setOfEdits;
    }

    public String suggestWordWithHighestFrequency(TreeSet<String> inputWords){
        int frequency = 0;
        String suggestedString = new String();

        for(String word : inputWords){
            INode newNode = trie.find(word);
            // if node exists
            if(newNode != null)
            {
                int newFrequency = newNode.getValue();
                if (newFrequency > frequency){
                    frequency = newFrequency;
                    suggestedString = word;
                }
            }
        }

        if (frequency > 0){
            return suggestedString;
        }
        return null;
    }

    //DELETION
    public TreeSet<String> deletion(String inputWord){
        TreeSet<String>setOfEdits = new TreeSet<String>();

        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder sb = new StringBuilder(inputWord);
            sb.deleteCharAt(i);
            String testWord = sb.toString();
            setOfEdits.add(testWord);
        }
        return setOfEdits;
    }

    //TRANSPOSE
    public TreeSet<String> transpose(String inputWord) {
        char[] c = inputWord.toCharArray(); // make inputWord an array of char
        TreeSet<String>setOfEdits = new TreeSet<String>();

        for(int i = 0; i < inputWord.length() - 1; i++){
            char tempChar = c[i];   // front
            c[i] = c[i + 1];        // front equals next
            c[i + 1] = tempChar;    // next equals front
            // completed swap
            String transposedString = new String(c);    // cast to String

            setOfEdits.add(transposedString);
            c = inputWord.toCharArray();    // reset
        }
        return setOfEdits;
    }

    //ALTERATION
    public TreeSet<String> alteration(String inputWord){
        char[] alterStringArr = inputWord.toCharArray();
        TreeSet<String>setOfEdits = new TreeSet<String>();

        for (int i = 0; i < inputWord.length(); i++){
            for(char c = 'a'; c <= 'z'; c++){
                alterStringArr[i] = c;
                String alteredWord = new String(alterStringArr);
                setOfEdits.add(alteredWord);
            }
            alterStringArr = inputWord.toCharArray();
        }
        return setOfEdits;
    }

    //INSERTION
    public TreeSet<String> insertion(String inputWord){
        TreeSet<String>setOfEdits = new TreeSet<String>();

        for(int i = 0; i < inputWord.length() + 1; i++){
            for(char c = 'a'; c <= 'z'; c++){
                StringBuilder sb = new StringBuilder(inputWord);
                sb.insert(i,c); // insertion of character c into index i
                String insertionWord = sb.toString();
                setOfEdits.add(insertionWord);
            }
        }
        return setOfEdits;
    }
}