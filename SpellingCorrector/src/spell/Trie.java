package spell;

public class Trie implements ITrie {
    private Node root;
    private int numNodes;
    private int numWords;
    private int hash;
    StringBuilder sb = new StringBuilder("");
    StringBuilder wb = new StringBuilder("");

    public Trie() {
        this.root = new Node();
        numNodes = 1;
        numWords = 0;
    }

    @Override
    public void add(String word) {
        hash = hash + word.hashCode();

        Node currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            if (currentNode.nodesArray[word.charAt(i) - 'a'] == null) {
                currentNode.nodesArray[word.charAt(i) - 'a'] = new Node();
                currentNode = currentNode.nodesArray[word.charAt(i) - 'a'];
                numNodes++;
            }
            else{
                currentNode = currentNode.nodesArray[word.charAt(i) - 'a'];
            }
            if (i == word.length() - 1) {
                if (currentNode.getValue() == 0){
                    numWords++;
                }
                currentNode.incrementCount();
            }
        }
    }

    // getRoot
    public Node getRoot(){ return root; }

    // The user’s input string will be compared against the Trie using the Trie.find(String). //
    @Override
    public INode find(String word) {
        Node currentNode = root;

        for (int i = 0; i < word.length(); i++) {
            if (currentNode.nodesArray[word.charAt(i) - 'a'] == null) {
                return null;
            }
            else{
                if (i == word.length() - 1) {
                    if (currentNode.nodesArray[word.charAt(i) - 'a'].getValue() > 0) {
                        return currentNode.nodesArray[word.charAt(i) - 'a'];
                    }
                    else {
                        return null;
                    }
                }
                currentNode = currentNode.nodesArray[word.charAt(i) - 'a'];
            }
        }
        return null;
    }

    @Override
    public int getNumWords() {
        return numWords;
    }

    @Override
    public int getNodeCount() {
        return numNodes;
    }

    public String toString(){
        Node currentNode = root;
        sb.setLength(0);
        for (int i = 0; i < 26; i++){
            if(currentNode.nodesArray[i] != null) {
                wb.append((char)(i + 97));
                if(currentNode.getValue() > 0){
                    sb.append(wb.toString() + "\n");
                }
                toStringHelper(currentNode.nodesArray[i]);
            }
        }
        return sb.toString();
    }

    public void toStringHelper(Node currentNode){
        boolean existsChild = false;

        for (int i = 0; i < 26; i++){
            if(currentNode.nodesArray[i] != null) {
                existsChild = true;
                if(currentNode.getValue() > 0){
                    sb.append(wb.toString() + "\n");
                }
                wb.append((char)(i + 97));
                toStringHelper(currentNode.nodesArray[i]);
            }
            if (i == 25){
                if (!existsChild){
                    sb.append(wb.toString() + "\n");
                    if( wb.length() > 0 ){
                        wb.deleteCharAt( wb.length() - 1 );
                    }
                }
            }
        }

        if( wb.length() > 0 ){
            wb.setLength(0);
        }
    }

    // gets Hash
    public int hashCode() {
        hash = hash + numWords * numNodes * this.root.getValue();
        return hash;
    }

    // EQUALS
    @Override
    public boolean equals(Object obj) {
        Trie currObj = this;

        if (obj == null) {
            return false;
        }
        else if (currObj.getClass() != obj.getClass()) {
            return false;
        }
        else {
            Trie trieObj = (Trie) obj;
            Node currObjNode = currObj.getRoot();
            Node trieObjNode = trieObj.getRoot();
            return equalsHelper(currObjNode, trieObjNode);
        }
    }

    public boolean equalsHelper(Node obj1, Node obj2){

        if ((obj1 != null && obj2 == null) || (obj1 == null && obj2 != null)){
            return false;
        }
        if (obj1 != null && obj2 != null){
            if (obj1.getValue() != obj2.getValue()){
                return false;
            }
            for (int i = 0; i < 26; i++) {
                if (!equalsHelper(obj1.nodesArray[i], obj2.nodesArray[i])){
                    return false;
                }
            }
        }
        return true;
    }
}
