
/**
 * Red-Black tree of generic types
 * Nodes can be added, deleted, and searched for.
 * You can search for predecessors and successors of specific words.
 * You can search for the first and last words.
 * Rules:
 * a node is either red or black
 * the root is black
 * a black node can have no more than 1 red child
 * red nodes are always left of their parents
 * red nodes can't have red children
 * every path from a given node to the bottom has same # of black nodes
 * 
 * @author Sara Wille 
 * @version 13 November 2017: Extra Credit
 */
class RedBlackTree <K extends Comparable<K>, V> 
{
    // instance variables - replace the example below with your own
    private Node root;
    boolean BLACK = false;
    boolean RED = true;

    /**
     * Constructor for objects of class RedBlackTree
     */
    public RedBlackTree()
    {
        root = null;
    }

    /**
     * PRINTS the nodes in the tree in order.
     */
    public void printString(){
        root.printString();
    }

    /** 
     * map key to value
     * @param key   the key to place
     * @param value the value corresponding to the key
     */
    public void put(K key, V value){
        root = findAndAdd(root, key, value);
        root.setColor(BLACK);
    }

    /**
     * return key's value
     * @param key   the key to get the value of
     * @return the value corresponding to the key
     */ 
    public V get(K key){
        Node node = findKey(root, key);
        if(node != null){
            return node.value;
        }else{
            return null;
        }
    }

    /**
     * remove key-value pair
     * @param key   the key to delete
     */ 
    public void delete(K key){
        //set root to red
        root.setColor(RED);
        root = findAndDelete(root, key);
        root.setColor(BLACK);
    }    

    /** 
     * returns the tree size
     * @return the size of the tree
     */
    public int size(){
        return root.size;
    }

    /** 
     * returns the first key
     * @return the first key in the tree
     */
    public K getMinKey(){
        //leftmost key
        if(root == null){
            return null;
        }
        Node min = root;
        while(min.left != null){
            min = min.left;
        }
        return min.key;
    }

    /** 
     * returns the last key
     * @return the last key in the tree
     */
    public K getMaxKey(){
        //rightmost key
        Node max = root;
        while(max.right != null){
            max = max.right;
        }
        return max.key;
    }

    /** 
     * what key comes before this one? (return null if it's the first)
     * @param   key the key to get the predecessor of
     * @return  the predecessor's key
     */
    public K findPredecessor(K key){
        //find key
        Node predecessor = predecessorHelp(root, key);
        if(predecessor == null){
            return null;
        }else{
            return predecessor.key;
        }
    }

    /** 
     * what key comes after this one? (return null if it's the last)
     * @param   key the key to get the successor of
     * @return  the successor's key
     */
    public K findSuccessor(K key){
        //find key
        Node successor = successorHelp(root, key);
        if(successor == null){
            return null;
        }else{
            return successor.key;
        }
    }

    /**
     * finds the rank of a certain key in the tree
     * @param key   the key to get the rank of
     * @return the rank of the specified key
     */
    public int findRank(K key){
        Node node = findKey(root, key);
        return node.rank;
    }
    
    /**
     * Recalculates the ranks of every node on the tree.
     * If you want to recalculate from a certain point down, use the node's recalcRank method & give it the rank to start from (the node's parent's rank)
     */
    public void recalcRank(){
        root.recalcRank(-1);
    }

    /** 
     * searches for the key with the specified rank and returns it.
     * @param rank  the rank to search for
     * @return the key corresponding to the rank
     */
    public K select(int rank){
        return selectHelper(root, rank);
    }

    /** 
     * is key contained in the tree?
     * @param key   the key to search for
     * @return  whether or not the key is in the tree
     */
    public boolean contains(K key) { 
        return (get(key) != null); 
    }

    /** 
     * is the tree empty?
     * @return  whether the tree is empty
     */
    public boolean isEmpty() { 
        return (size()==0); 
    } 

    /**
     * is the tree balanced?
     * @return  whether the tree is balanced
     */
    public boolean isBalanced(){
        return root.isBalanced();
    }

    /**
     * helper for findPredecessor()
     */
    private Node predecessorHelp(Node node, K key){
        if(node == null){
            return null;
        }
        Node predecessor = null;
        int cmp = key.compareTo(node.key);
        if(cmp == 0){
            //we've found our key, is the predecessor below?
            if(node.left != null){
                predecessor = node.left;
                while(predecessor.right != null){
                    //while we can go right, go right
                    predecessor = predecessor.right;
                }
            } 
            return predecessor;
        }
        else if (cmp<0){ 
            predecessor = predecessorHelp(node.left, key);
        }
        else if (cmp>0){ 
            predecessor = predecessorHelp(node.right, key);
            //if you didn't find a predecessor below, this is the predecessor
            if(predecessor == null){
                predecessor = node;
            }
        }
        return predecessor;
    }

    /**
     * helper for findSuccessor()
     */
    private Node successorHelp(Node node, K key){
        if(node == null){
            return null;
        }
        Node successor = null;
        int cmp = key.compareTo(node.key);
        if(cmp == 0){
            //we've found our key, is the predecessor below?
            if(node.right != null){
                successor = node.right;
                while(successor.left != null){
                    //while we can go left, go left
                    successor = successor.left;
                }
            } 
            return successor;
        }
        else if (cmp<0){ 
            successor = successorHelp(node.left, key);

            if(successor == null){
                successor = node;
            }
        }
        else if (cmp>0){ 
            successor = successorHelp(node.right, key);       
        }
        return successor;
    }

    /**
     * helper for get()
     */
    private Node findKey(Node node, K key){
        if(node == null){
            return null;
        }
        int cmp = key.compareTo(node.key);
        if(cmp == 0){
            //we've found our key
            return node;
        }
        else if (cmp<0){ 
            node = findKey(node.left, key);
        }
        else if (cmp>0){ 
            node = findKey(node.right, key);       
        }
        return node;
    }

    /**
     * rotates the node left and returns the node which is in its' old position
     */
    private Node rotateLeft(Node node){
        Node midNode = node.right.left;
        //switch nodes
        Node newParent = node.right;
        newParent.left = node;
        node.right = midNode;
        //switch colors
        boolean tempColor = node.isRed;
        node.setColor(newParent.isRed);
        newParent.setColor(tempColor);
        //recalculate size
        node.recalcSize();
        newParent.recalcSize();
        return newParent;
    }

    /**
     * rotates the node right and returns the node which is in its' old position
     */
    private Node rotateRight(Node node){
        Node midNode = node.left.right;
        //switch nodes
        Node newParent = node.left;
        newParent.right = node;
        node.left = midNode;
        //switch colors
        boolean tempColor = node.isRed;
        node.setColor(newParent.isRed);
        newParent.setColor(tempColor);
        //recalculate size
        node.recalcSize();
        newParent.recalcSize();

        return newParent;
    }

    /**
     * helper for put()
     */
    private Node findAndAdd(Node node, K key, V value) {
        // if the current "Node" is null, make new Node

        if (node == null) {
            return new Node(key, value);
        }
        // compare and go down the correct path
        int cmp = key.compareTo(node.key);
        if (cmp<0){ 
            node.left = findAndAdd(node.left, key, value);
        }
        else if (cmp>0){ 
            node.right = findAndAdd(node.right, key, value);
        }
        else {
            node.value = value;
        }
        // going up--fix coloring issues
        if ((node.left == null || !node.left.isRed) &&node.right != null && node.right.isRed) {
            node = rotateLeft(node);
        }
        if (node.left != null && node.left.left != null && node.left.isRed && node.left.left.isRed) {
            //if my child is red and their child is red, rotate
            node = rotateRight(node);
        }
        if (node.right != null && node.left != null && node.right.isRed && node.left.isRed) {
            node.colorFlip();
        }
        // adjust the size & go home
        node.recalcSize();
        return node;
    }

    /**
     * helper for delete()
     */
    private Node findAndDelete(Node node, K key) {
        // compare and go down the correct path
        int cmp = key.compareTo(node.key);
        //this is okay because we only check to see if cmpLeft or cmpRight == 0, avoids null pointer
        int cmpLeft = 1;
        int cmpRight = 1;
        if(node.left != null){
            cmpLeft = key.compareTo(node.left.key);
        } 
        if(node.right != null){
            cmpRight = key.compareTo(node.right.key);
        }
        if(cmpLeft == 0){
            //we've found our node to delete
            //figure out if it's a leaf, has 1 child, or 2 children
            if(!node.left.isRed){
                node = makeChildRed(node);
            }

            if(node.left.left == null && node.left.right == null){
                //leaf!
                node.left = null;
            }else if(node.left.left == null && node.left.right != null){
                //has 1 child, promote them
                node.left = node.left.right;
            }else if(node.left.left != null && node.left.right == null){
                //has 1 child, promote
                node.left = node.left.left;
            }else{
                //has 2 children, promote 1
                //find the successor & delete
                if( !node.left.right.isRed){
                    node.left = makeChildRed(node.left);
                }
                node.left = deleteSuccessor(node.left, node.left, node.left.right);
                //node.left = fixColorAndSize(node.left);
            }
            if(node.left != null){
                node.left.recalcSize();
            }
        }else if(cmpRight == 0){
            //we've found our node to delete
            //figure out if it's a leaf, has 1 child, or 2 children
            if(!node.right.isRed){
                node = makeChildRed(node);
            }
            if(node.right.left == null && node.right.right == null){
                node.right = null;
            }else if(node.right.left == null && node.right.right != null){
                //has 1 child, promote
                node.right = node.right.right;
            }else if(node.right.left != null && node.right.right == null){
                //has 1 child, promote
                node.right = node.right.left;
            }else{
                //has 2 children, promote 1
                //find the successor & delete
                if( !node.right.right.isRed){
                    node.right = makeChildRed(node.right);
                }
                node.right = deleteSuccessor(node.right, node.right, node.right.right);
                //node.right = fixColorAndSize(node.right);
            }
            if(node.right != null){
                node.right.recalcSize();
            }
        }
        else if (cmp<0){ 
            //do we want to go right after this? (>0)
            int cmpNext = key.compareTo(node.left.key);
            //is one of their children the one we're looking for?
            if(node.left.right != null){
                cmpRight = key.compareTo(node.left.right.key);
            }
            if(node.left.left != null){
                cmpLeft = key.compareTo(node.left.left.key);
            }
            //check for left red right black case if we want to go right
            if(cmpNext > 0 && node.left.left != null && node.left.left.isRed && node.left.right != null && !node.left.right.isRed){
                //rotate right
                node.left = rotateRight(node.left);
            }
            else if(cmpRight == 0 && node.left.right.left != null && node.left.right.left.isRed && node.left.right.right != null && !node.left.right.right.isRed){
                //our next right will be the one, and it has left red, black right
                node.left.right = rotateRight(node.left.right);
            }else if(cmpLeft == 0 && node.left.left.left != null && node.left.left.left.isRed && node.left.left.right != null && !node.left.left.right.isRed){
                //our next left will be the one, and it has left red, black right
                node.left.left = rotateRight(node.left.left);
            }

            //make sure we're going into a red node
            if(!node.left.isRed){
                node = makeChildRed(node);
            }
            node.left = findAndDelete(node.left, key);
        }
        else if (cmp>0){ 
            //do we want to go right after moving? (>0)
            int cmpNext = key.compareTo(node.right.key);

            if(node.right.right != null){
                cmpRight = key.compareTo(node.right.right.key);
            }
            if(node.right.left != null){
                cmpLeft = key.compareTo(node.right.left.key);
            }
            //check for left red right black case if we want to go right
            if(cmpNext > 0 && node.right.left != null && node.right.left.isRed && node.right.right != null && !node.right.right.isRed){
                //rotate right
                node.right = rotateRight(node.right);
            }
            else if(cmpRight == 0 && node.right.right.left != null && node.right.right.left.isRed && node.right.right.right != null && !node.right.right.right.isRed){
                //our next right will be the one, and it has left red, black right
                node.right.right = rotateRight(node.right.right);
            }else if(cmpLeft == 0 && node.right.left.left != null && node.right.left.left.isRed && node.right.left.right != null && !node.right.left.right.isRed){
                //our next left will be the one, and it has left red, black right
                node.right.left = rotateRight(node.right.left);
            }

            //make sure right child is red
            if(!node.right.isRed){
                node = makeChildRed(node);
            }
            node.right = findAndDelete(node.right, key);
        }
        // going up--fix coloring issues
        node = fixColorAndSize(node);
        recalcRank();
        return node;
    }

    /**
     * deletes delNode and returns the node that replaces it
     */
    private Node deleteSuccessor(Node delNode, Node succParent, Node successor){
        boolean succIsLeft = true;
        if(succParent.right == successor){
            succIsLeft = false;
        }
        if(!successor.isRed){
            succParent = makeChildRed(succParent);
        }
        if(successor.left == null){
            //this is our successor
            //switch it
            K dKey = delNode.key;
            V dVal = delNode.value;
            delNode.key = successor.key;
            delNode.value = successor.value;
            successor.key = dKey;
            successor.value = dVal;
            //delete node where the successor used to be
            if(succParent.right == successor){
                succParent.right = null;
            }else{
                succParent.left = null;
            }

        }else{
            successor = deleteSuccessor(delNode, successor, successor.left);
            if(succIsLeft){
                succParent.left = successor;
            }else{
                succParent.right = successor;
            }

        } 
        succParent = fixColorAndSize(succParent);
        return succParent;
    }

    /**
     * fixes the colors and sizes of node's children
     */
    private Node fixColorAndSize(Node node){
        boolean leftConditions = (node.left == null || !node.left.isRed);

        if (leftConditions && (node.right != null) && (node.right.isRed)) {
            node = rotateLeft(node);
        }
        if (node.right != null && node.left != null && node.right.isRed && node.left.isRed) {
            node.colorFlip();
        }
        if (node.left != null && node.left.left != null && node.left.isRed && node.left.left.isRed) {
            //if my child is red and their child is red, rotate
            node = rotateRight(node);
        }
        // adjust the size & go home
        node.recalcSize();
        return node;
    }
    //only call this method if the child is not already red
    /**
     * makes node's child red if it WASN'T RED ALREADY
     */
    private Node makeChildRed(Node node){
        //if left red & right black, rotate right
        if(node.left.isRed && !node.right.isRed){
            node = rotateRight(node);
            //old right child should now be right child of right child
        }
        //if neither left nor right red, color flip
        else if(!node.left.isRed && !node.right.isRed){
            node.colorFlip();
        }
        return node;
    }
    /**
     * helper for select()
     */
    private K selectHelper(Node node, int rank){
        if(node == null){
            return null;
        }
        if(rank == node.rank){
            //we've found our key
            return node.key;
        }
        else if (rank < node.rank){ 
            return selectHelper(node.left, rank);
        }
        else if (rank > node.rank){ 
            return selectHelper(node.right, rank);       
        }
        return null;
    }

    /**
     * Write a description of class Node here.
     * 
     * @author (your name) 
     * @version (a version number or a date)
     */
    private class Node
    {
        private boolean isRed;
        public K key;
        public V value;
        public Node left;
        public Node right;
        public int size;
        public int blackNodes;
        public int rank;

        /**
         * initializes variables
         */
        public Node(K myKey, V myValue)
        {
            isRed = true;
            key = myKey;
            value = myValue;
            left = null;
            right = null;
            size = 1;
            recalcBlackNodes();
            rank = 0;
        }

        /**
         * sets the node's color
         */
        public void setColor(boolean color){
            isRed = color;
        }

        /**
         * switches the node's color
         */
        public void changeColor(){
            if(isRed){
                isRed = false;
            }else{
                isRed = true;
            }
        }

        /**
         * flips the node's color, as well as the colors of its' children
         */
        public void colorFlip(){
            changeColor();
            left.changeColor();
            right.changeColor();

        }

        /**
         * returns true if the tree from this node down is balanced
         */
        public boolean isBalanced(){
            recalcBlackNodes();
            if(right == null && left == null){
                //if this node is a leaf, it's balanced
                return true;
            }else if(right == null){
                //if right is null and left is red, it's balanced, but if left is black it's not
                if(left.isRed){
                    return true;
                }
                return false;
            }else if(left == null){
                //if left is null but right is not, it's not balanced
                return false;
            }
            else if(left.isBalanced() && right.isBalanced()){
                //if both of my children are balanced, so am I.
                return true;
            }
            return false;
        }

        /**
         * calculates how many black nodes are in the tree from this point down
         */
        public void recalcBlackNodes(){
            blackNodes = 0;
            if(left != null && right != null){
                left.recalcBlackNodes();
                right.recalcBlackNodes();
                blackNodes = left.blackNodes + right.blackNodes;

            }else if(left != null){
                left.recalcBlackNodes();
                blackNodes = left.blackNodes;
            }
            if(!isRed){
                blackNodes++;
            }
        }

        /**
         * recalculates the node's size based on the sizes of its' children
         */
        public void recalcSize(){
            if(left != null && right != null){
                size = left.size + right.size + 1;
            }else if(left != null){
                size = left.size + 1;
            } else if(right != null){
                size = right.size + 1;
            } else{
                size = 1;
            }
        }
        /**
         * calculates the rank of this node and all nodes below it, starting from curRank
         * curRank should be the rank of the node above this one, assuming it's been calculated
         */
        public int recalcRank(int curRank){
            if(size == 1){
                //I am a leaf
                rank = curRank + 1;
                return rank;
            }
            rank = left.recalcRank(curRank) + 1;
            if(right != null){
                return right.recalcRank(rank);
            } else{
                return rank;
            }
        }

        /**
         * PRINTS a string of this node and all of its' descendent nodes, in order
         */
        public void printString(){
            if(left != null && right != null){
                left.printString(); 
                System.out.println(""+key+ " " + left.key + " " + right.key + " " + isRed); 
                right.printString();
            }else if(left != null){
                left.printString(); 
                System.out.println(""+key + " "+ left.key + " " + rank + isRed); 
            } else if(right != null){
                System.out.println(""+key + " "+ right.key + " " + rank + isRed); 
                right.printString();
            } else{
                System.out.println(""+key+ " " + rank + isRed);
            }

        }
    }

}
