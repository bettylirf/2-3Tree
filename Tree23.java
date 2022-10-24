//Tree23.java
/*****************************************
 *  	Overview: implementation of 2-3 trees
 *		Tree23 is our main class:
 *			but it depends on two classes:
 *		   		InternalNode class
 *				LeafNode class
 *			which are both extensions of the Node class.
 *		Tree23 instances store Items instances in its leaves.
 *	
 *	MEMBERS: ====================================
 *		Item class:
 *				String key;
 *				int data;
 *		Node class:
 *				Node parent;
 *				String guide;
 *		InternalNode class:
 *				Node[] child[4];	//THIS ALLOWS YOU TO WRITE LOOPS
 *				//If degree is d, then non-null children are child[0..d-1]
 *		LeafNode class:
 *				int data;
 *		Tree23 class:
 *				InternalNode root;
 *				int height;
 *	METHODS: ====================================
 *		Tree23 class:
 *				Item search(String);
 *				boolean insert(Item);
 *				Item delete(String);
 *			All these 3 methods calls the helper method,
 *				InternalNode find(String); // returns a pseudo-leaf
 *			We provide you with some debugging tool:
 *				showTree() -- prints a representation of the tree
 *				dbug(String)
 *				debug(String)
 *	Terminology:
 *		An InternalNode whose children are LeafNodes is
 *			called a "pseudo-leaf"
 *	
 *	====================================
 *	Basic Algorithms, Fall2021
 *	Professor Yap
 *	TA's Bingran Shen and Zihan Feng
 **************************************** */

import java.util.Random;

class Tree23 extends Util {
	// MEMBERS:=========================================
	/////////////////////////////////////////
		InternalNode root = new InternalNode();
		int ht; //height
	// CONSTRUCTORS:====================================
	/////////////////////////////////////////
		Tree23() {// Note: ht=0 iff n=0 (where n is number of items)
			ht = 0; } //  ht=1 implies n=1,2 or 3 
	// METHODS:=========================================
	/////////////////////////////////////////
	InternalNode find (String x){
		// find(x) returns the Internal Node u such that either contains x
		//		or where x would be inserted.  Thus u is a pseudo-leaf.
		//		The node u is NEVER null.
		// 	This is the common helper for search/insert/delete!!
		InternalNode u = root;
		for(int i = 0; i < ht - 1; i ++){
			if(x.compareTo(u.child[0].guide) <= 0)
				u = (InternalNode) u.child[0];
			else if(u.child[2] == null || x.compareTo(u.child[1].guide) <= 0)
				u = (InternalNode) u.child[1];
			else
				u = (InternalNode) u.child[2];
		}
		return u;
	}//find
	LeafNode search (String x){
		// Returns a ??null node?? if failure;
		// 		otherwise, return a LeafNode with the key x.
		InternalNode u = find(x);
		for(int i = 0; i < u.degree(); i++){
			if(x.compareTo(u.child[i].guide) == 0)
				return (LeafNode) u.child[i];
		}
		return null; // x is not found
	}//seearch
	boolean insert (Item it){
		// insert(it) returns true iff insertion is successful.
		InternalNode u = find(it.key);
		debug("The new node should be inserted at: " + u.guide);
		int dgr = u.degree();
		if(dgr == 0){//n == 0
			u.child[0] = new LeafNode(it, u);
			u.guide = u.child[0].guide;
			this.ht ++;
			return true;
		}
		// check duplicates
		for(int i = 0; i < dgr; i ++){
			if(it.key.compareTo(u.child[i].guide) == 0)
				return false;
		}
		// insert node
		LeafNode newLeaf = new LeafNode(it, u);
		u.child[dgr] = newLeaf;
		u.sortNode();
		dgr = dgr + 1;
		if(dgr == 2 || dgr == 3)
			updateParent(u);
		if(dgr == 4){
			split(u, 4);
		}
		return true; //insertion succeed
	}//insert
	Item delete (String x){
		// delete(x) returns the deleted item
		//			returns null if nothing is deleted.
		InternalNode u = find(x);
		Item removed = u.removeLeaf(x);
		if(removed == null) //x not in the tree
			return null; // delete fail
		int degree = u.degree();
		if(degree <= 1)
			reshape(u);
		return removed;
	}//delete
	void updateParent(InternalNode u){
		//update the guide of the parent node
		if(u == null || u.guide.compareTo(u.child[u.degree() - 1].guide) >= 0)
			return;
		u.guide = u.child[u.degree() - 1].guide;
		updateParent(u.parent);
	}//updateParent
	void split(InternalNode u, int degree){
		//split the node into two when the node is overfull, degree == 4
		if(degree != 4)
			return;
		InternalNode v = new InternalNode();
		v.newParent(u.child[2], u.child[3], v);
		u.child[2] = u.child[3] = null;
		u.guide = u.child[1].guide;
		if(u == root){ // u is the root
			InternalNode newRoot = new InternalNode();
			newRoot.newParent(u, v, newRoot);
			this.root = newRoot;
			this.ht++;
			return;
		}
		v.parent = u.parent;
		u = u.parent;
		degree = u.degree();
		u.child[degree] = v;
		u.sortNode();
		u.guide = u.child[degree].guide;
		split(u, degree + 1);
	}//split
	void reshape(InternalNode u){
		// debug("The node expriencing reshape is: " + u.guide);
		// debug("The 1st child of this node is: " + u.child[0].guide);
		// deals with the case when the node u is under full, degree < 2
		if(u == root){
			if(this.ht != 1){ // if the tree has more than one level
				this.root = (InternalNode)u.child[0];
				this.ht--;
			}
			if(u.child[0] == null){
				this.root = null;
				this.ht = 0;
			}
			return;
		}
		InternalNode v = u.parent;
		int index = v.getIndexOf(u);
		boolean merge = (index > 0) ? v.proposeLeft(index) : v.proposeRight(index);
		if(merge){
			if(v.degree() == 1) 
				reshape(v);
		}
		return;
	}//reshape
	// HELPERS:=========================================
	/////////////////////////////////////////
	// DO NOT CHANGE unitTest
	void unitTest (){
		// unit test for Insert+Search+Delete
		// First input: //////////////////////////////////////
		debug("\n======> Inserting Fruits:");
		String[] fruits =
			{"banana","apple","peach","orange","apple","pear","plum"};
		for (String x: fruits){
			boolean in = insert(new Item(x));
			debug("insert(" + x + ") = " + String.valueOf(in));
		}
		debug("Here is the final Fruit Tree:");
		showTree();

		debug("\n======> Inserting sqrt(3) digits:");
		// Second input: /////////////////////////////////////
		int[] input = {1, 7, 3, 2, 0, 5, 0, 8, 0};
		Tree23 t = new Tree23();
		for (int x : input){
			Item it = new Item(x);
			boolean in = t.insert(it);
			debug("insert(" + x + ") = " + String.valueOf(in));
		}
		debug("Here is the final sqrt(3) Tree:");
		t.showTree();

		// SEARCHES: //////////////////////////////////////
		debug("\n======> Searching Fruits");
		LeafNode v = search("banana");
		if (v==null) 
			debug("Fruit Tree: search(banana) fails");
		else
			debug("Fruit Tree: search(banana) succeeds");
		v = search("cherry");
		if (v==null) 
			debug("Fruit Tree: search(cherry) fails");
		else
			debug("Fruit Tree: search(cherry) succeeds");

		debug("\n======> Searching Digits");
		v = t.search(String.valueOf(3));
		if (v==null) 
			debug("Sqrt3 Tree: search(3) fails");
		else
			debug("Sqrt3 Tree: search(3) succeeds");
		v = t.search(String.valueOf(4));
		if (v==null) 
			debug("Sqrt3 Tree: search(4) fails");
		else
			debug("Sqrt3 Tree: search(4) succeeds");

		// DELETES: //////////////////////////////////////
		debug("\n=============== deleting fruits");
		delete("banana");
			debug("Fruit Tree after DELETE(banana):");
			showTree();
		delete("plum");
			debug("Fruit Tree after DELETE(plum):");
			showTree();
		delete("apricot");
			debug("Fruit Tree after DELETE(apricot):");
			showTree();
		delete("apple");
			debug("Fruit Tree after DELETE(apple):");
			showTree();
		debug("\n=============== deleting digits");
		t.delete(String.valueOf(3));
			debug("Sqrt3 Tree after DELETE(3):");
			t.showTree();
		t.delete(String.valueOf(0));
			debug("Sqrt3 Tree after DELETE(0):");
			t.showTree();
		debug("\n=============== THE END");
		}//unitTest
    void showTree () {
        // print all the keys in 23tree:
        int h = ht;
        InternalNode u = root;
        showTree(u, h, "");
        dbug("\n");
    }// showTree
    void showTree (Node u, int h, String offset) {
        // internal recursive call for showTree
        if (h == 0) {
            debug("()");
            return;
        }
        int d = ((InternalNode) u).degree();
        String increment = "G=" + String.valueOf(u.guide) + ":(";
        // Note: "G=" refers to the "guide"
        dbug(increment);
        offset = offset + tab(increment.length() - 1, '-') + "|";
        for (int i = 0; i < d; i++)
            if (h == 1) {
                Node w = ((InternalNode) u).child[i];
                LeafNode v = (LeafNode) w;
                (v.item()).dump();
                if (i == d - 1)
                    debug(")");
            } else {
                if (i > 0)
                    dbug(offset);
                showTree(((InternalNode) u).child[i], h - 1, offset);
            }
        // dbug(")");
    }// showTree
    String tab (int n, char c){
        // returns a string of length n filled with c
        char[] chars = new char[n];
        java.util.Arrays.fill(chars, c);
        return String.valueOf(chars);
    }//tab
	boolean multiInsert (Random rg, int n, int m){
		// Insert n times and then search for m.
		// returns true if m is found.
		for(int i = 0; i < n; i++){
			int x = rg.nextInt(100);
			boolean in = this.insert(new Item(x));
			debug("insert(" + x + ") = " + String.valueOf(in));
			this.showTree();
			debug("The height of the tree is: " + this.ht);
			debug("");
		}
		debug("Here is the final Tree:");
		this.showTree();
		// debug("The result of searching " + String.valueOf(m) + is )
		LeafNode u = search(String.valueOf(m));
		if(u == null)
			return false;
		return true;
	}//multiInsert
    Tree23 randomTree (Random rg, int n, int N) {
	    // Insert n times into empty tree, and return the tree.
	    // Use rg as random number generator keys in range [0,N)
	    // Keep the size of tree in Util.COUNT
	    Tree23 t = new Tree23();
	    for (int i = 0; i < n; i++) {
	        int x = rg.nextInt(N);
	        Item it = new Item(x);
	        boolean b = t.insert(it);
	        if (b)  COUNT++;
	    } // for
	    return t;
    }// randomTree
    int randomDelete (Random rg, Tree23 tt, int N) {
        // delete a random element in the tree 100 times until it's empty
        // Use rg as random number generator keys in range [0,N)
        // return the delete count if tree is empty
        int count = 0;
        Item it;
        while (count < 100 && tt.ht > 0) {
            it = tt.delete(String.valueOf(rg.nextInt(N)));
            count++;
        }
        return count;
    }// randomDelete
    void messUpTree (String key) {
        InternalNode u = find(key);
        int deg = u.degree();
        u.swapNodes(0, deg - 1);
    }// messUpTree
    String checkTree () {          
        // returns error message iff the keys are in NOT in sorted order!
        // CAREFUL: we do not check the guides
        int h = ht;
        InternalNode u = root;
        String s = checkTree(u, h, ""); // "" is the globally least key!
        // if (s==null) // May NOT be error! let the caller decide.
        // return "CHECKTREE ERROR";
        return s;
    }
    String checkTree (Node u, int h, String maxkey) {
        // internal recursive call for checkTree
        // returns null if fail; else it is the maximum seen so far!
        if (h == 0)
            return "OK, EMPTY TREE";
        int d = ((InternalNode) u).degree();
        for (int i = 0; i < d; i++)
            if (h == 1) {
                Node w = ((InternalNode) u).child[i];
                LeafNode v = (LeafNode) w;
                if (maxkey.compareTo(v.item().key) >= 0) { // error!
                    debug("CHECKTREE ERROR at leaf " + v.item().key);
                    debug(maxkey);
                    return null;
                } else
                    maxkey = v.item().key;
            } else {
                String s1 = checkTree(((InternalNode) u).child[i], h - 1, maxkey);
                if (s1 == null || maxkey.compareTo(s1) >= 0) {
                    return null;
                } else
                    maxkey = s1;
            }
        return maxkey;
    }// checkTree
	// MAIN METHOD:=========================
	/////////////////////////////////////////
	public static void main(String[] args){

		int ss = (args.length>0)? Integer.valueOf(args[0]) : 0;
		int nn = (args.length>1)? Integer.valueOf(args[1]) : 10;
		int mm = (args.length>2)? Integer.valueOf(args[2]) : 102;
		
		Random rg = (ss == 0)? new Random() : new Random(ss);
		Tree23 tt = new Tree23();

		switch (mm){
			case 0: //unit test for insert+search
				debug("==> mode 0: unit test\n");
				tt.unitTest();
				break;
			case 1: //search for "10" once
				debug("==> mode 1: random insert+search once\n");
				tt.multiInsert(rg, nn, 10);
				break;
			case 2: //search for "10" until succeeds
				debug("==> mode 2: random insert+search till success\n");
				while (!tt.multiInsert(rg, nn, 10))
					debug("\n================ Next Trial\n");
				break;
			case 3: 
				String[] treeString =
				    {"orange","apple","pear","plum","banana","apple","peach","guy","y","noy", "njd","biu"};
				for (String x: fruits){
				     boolean in = tt.insert( new Item(x));
				     debug("insert(" + x + ") = " + String.valueOf(in));
				}
				tt.showTree();
				debug("");
				String [] delete = {"banana","apple","peach","banana","pear","plum"};
				for (String x: fruits){
				    Item it = tt.delete(x);
				    debug("after delete(" + x + ") ");
				    tt.showTree();}
				break;
			case 101: // create a random tree with nn random insertions
                debug("==> mode 101: create a random tree\n");
                tt = tt.randomTree(rg, nn, 2 * nn);
                debug("Randomly generated tree of is:");
                tt.showTree();
                break;
            case 102: // create a random tree and randomly delete 100 times until it's empty
                debug("==> mode 102: create a random tree and delete till tree is empty\n");
                tt = tt.randomTree(rg, nn, 2 * nn);
                debug("Randomly generated tree of is:");
                tt.showTree();
                int count = tt.randomDelete(rg, tt, 2 * nn);
                if (tt.ht >= 1) {
                    debug("Tree non-empty after 100 random deletes, here is what's left:");
                    tt.showTree();
                }
                else
                    debug("After " + String.valueOf(count) + " deletes, tree is empty");
                break;
        }
	}//main
}//class Tree23

/*****************************************
HELPER CLASSES: Item and Node
	Item class:
		this is the data of interest to the user
		it determines the remaining details.
	Node class:
		This is the superclass of the two main work horses:
			InternalNode class
		and
			LeafNode class
		The nodes of the Tree23 is made up of these!
	We provide the InternalNode class with useful methods such as
		degree()	-- determine the degree of this node
		addLeaf		-- assumes the children are LeafNodes, and
						we want to add a new leaf.
		removeLeaf	-- converse of addLeaf
		sortNode	-- sort the children of this node
***************************************** */
// Class Item %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class Item {
		String key;
		int data;
		Item(String k, int d){
			key = k; data = d; }
		Item(String k){// Randomly generate the int data!!!
			key = k; data = (int)(10*Math.random()); }
		Item(int k){ // Use string value of k as key!!!
			key = String.valueOf(k); data = k; }
		Item(Item I){
			key = I.key; data = I.data; }
		// METHODS:
		void dump(){
			System.out.printf("<%s:%d>", key, data); }
		String stringValue(){
			return String.format("<%s:%d>", key, data); }
	}//class Item
	
// Class Node %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class Node extends Util{
	InternalNode parent;
	String guide;
}//class Node
	
// Class LeafNode %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class LeafNode extends Node {
	int data;
	LeafNode(Item I){
		parent=null; guide=I.key; data=I.data;}
	LeafNode(Item I, InternalNode u){
		parent=u; guide=I.key; data=I.data;}
	//MEMBER:
	//////////////////////////////////////////
	Item item(){
		Item it=new Item(guide, data);
		return it; }
	}//class LeafNode

// Class InternalNode %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
class InternalNode extends Node{
	//MEMBERS:
		Node[] child = new Node[4];
	//CONSTRUCTORS:
		InternalNode(){
			this(null,null,null); }
		InternalNode(Node u0, Node u1, InternalNode p){
			this(u0, u1); 
			if (u0!=null) u0.parent=this;
			if (u1!=null) u1.parent=this;}
		//======================================================
		InternalNode(InternalNode u0, InternalNode u1, InternalNode p){
			this((Node)u0, (Node)u1, p); }
		InternalNode(Node u0, Node u1){
			assert(u0==null || u1==null || u0.guide.compareTo(u1.guide)<0);
			if (u1!=null) guide = u1.guide;	
			child[0] = u0; child[1] = u1;
			} // REMEMBER to update u0.parent and u1.parent.
		InternalNode(InternalNode u0, InternalNode u1){
			this((Node)u0, (Node)u1); }
	//METHODS:
	//////////////////////////////////////////
	int degree(){ 
		// get the degree of this InternalNode
		int numOfChild = 0;
		for(int i = 0; i < 4; i++){
			if(this.child[i] != null)
				numOfChild++;
			else
				break;
		}
		// System.out.println("Degree: " + numOfChild);
		return numOfChild; }//degree
	void newParent (InternalNode u0, InternalNode u1, InternalNode p){
		// newParent(u0, u1, p) sets up p as parent of u0, u1.
		newParent((Node)u0, (Node)u1, p); }
	void newParent (Node u0, Node u1, InternalNode p){
		//assert(u0.key < u1.key)
		p.guide = u1.guide;
		p.child[0] = u0;
		p.child[1] = u1; 
		u0.parent = u1.parent = p; }//newParent
	Item removeLeaf (String x){
		// removeLeaf(x) returns the deleted item whose key is x
		//		or it returns null if no such item.
		// assert(this.child[] are leaves)
		for(int i = 0; i < this.degree(); i++){
			if(x.compareTo(this.child[i].guide) == 0){
				Item removed = ((LeafNode)child[i]).item();
				this.shiftLeft(i);
				return removed;
			}
		}
		return null; 
	}//removeLeaf
	int getIndexOf(InternalNode u){
		// returns the index c such that this->child[c]==u;
		// assert(u is a child of "this")
		for(int i = 0; i < this.degree(); i++){
			if(child[i] == u)
				return i;
		}
		return 0; 
	}//getIndexOf
	void shiftLeft (int c){ // don't forget to delete the last child
		//		this->child[c] is a hole which we must fill up;
		//		so for each i>c: child[i-1] = child[i]
		int i = c + 1;
		while(i < this.degree()){
			this.child[i - 1] = this.child[i];
			i++;
		}
		this.child[i - 1] = null;
	}//shiftLeft
	void shiftRight (int c){
		//		create a hole at child[c], so for each i>c,
		//				child[i] = child[i-1]
		//		(but start with i=degree down to i=c+1)
		// ASSERT("c < this->degree < 4");
		for(int i = this.degree(); i > c; i--)
			this.child[i] = this.child[i - 1];
	}//shiftRight
	boolean proposeLeft (int c){
		// 			ASSERT("c>0 and child[c].degree=1")
		//		return TRUE if the child[c] merges into child[c-1]
		//		return FALSE child[c] adopts a child of child[c-1].
		//	REMARK:	return TRUE means this is a non-terminal case
		InternalNode u = (InternalNode)this.child[c];
		InternalNode sib = (InternalNode)this.child[c-1];
		if(sib.degree() == 2){ //merge to left sibling
			sib.child[2] = u.child[0];
			sib.child[2].parent = sib;
			sib.guide = sib.child[2].guide;
			this.child[c] = null;
			return true;
		}
		else{ //adopt from left sibling
			u.child[1] = u.child[0];
			u.child[0] = sib.child[2];
			u.child[0].parent = u;
			sib.child[2] = null;
			sib.guide = sib.child[1].guide;
			return false;
		}
	}//proposeLeft
	boolean proposeRight (int c){
		// 			ASSERT("c+1<degree and child[c].degree=1")
		//		returns TRUE if the child[c] and child[c+1] are merged.
		//		returns FALSE if child[c] adopts a child of child[c+1].
		// 	REMARKS: under our policy, we KNOW that c==0!
		//		Also TRUE means this is a non-terminal case
		InternalNode u = (InternalNode)this.child[c];
		InternalNode sib = (InternalNode)this.child[c+1];
		if(sib.degree() == 2){ //merge to right sibling
			sib.shiftRight(0);
			sib.child[0] = u.child[0];
			sib.child[0].parent = sib;
			this.shiftLeft(0);
			sib.guide = sib.child[2].guide;
			return true;
		}
		else{ //adopt from right sibling
			u.child[1] = sib.child[0];
			u.child[1].parent = u;
			sib.shiftLeft(0);
			u.guide = u.child[1].guide;
			return false;
		}
	}//proposeRight
	// void sortNode(){
	// 	// We use swapNodes to sort the keys in an InternalNode:
	// 	int d = this.degree();
	// 	for(int i = d - 1; i > 0; i--){
	// 		for(int j = 0; j < i; j++){
	// 			if(this.child[j].guide.compareTo(this.child[j + 1].guide) > 0)
	// 				swapNodes(i, j);
	// 		}
	// 	}
	// }//SortNode
	// void swapNodes(int index_1, int index_2){
	// 	Node tmp = this.child[index_1];
	// 	this.child[index_1] = this.child[index_2];
	// 	this.child[index_2] = tmp;
	// }
	// void dump (){// print this node
	// }//dump
	void swapNodes(int u0, int u1) {// (u0,u1) <- (u1,u0)
        Node tmp = child[u1];
        child[u1] = child[u0];
        child[u0] = tmp;
    }
    void swapNodes(int u0, int u1, int u2) {// (u0,u1,u2) <- (u2,u0,u1)
        Node tmp = child[u2];
        child[u2] = child[u1];
        child[u1] = child[u0];
        child[u0] = tmp;
    }
    void swapNodes(int u0, int u1, int u2, int u3) {
        // (u0,u1,u2,u3) <- (u3,u0,u1,u2)
        Node tmp = child[u3];
        child[u3] = child[u2];
        child[u2] = child[u1];
        child[u1] = child[u0];
        child[u0] = tmp;
    }
    void sortNode() {
        // We use swapNodes to sort the keys in an InternalNode:
        if (child[1] == null)
            return;
        if (child[0].guide.compareTo(child[1].guide) > 0)
            swapNodes(0, 1);
        // assert(child0 < child1)
        if (child[2] == null)
            return;
        if (child[0].guide.compareTo(child[2].guide) > 0)
            swapNodes(0, 1, 2);
        else if (child[1].guide.compareTo(child[2].guide) > 0)
            swapNodes(1, 2);
        // assert(child0 < child1 < child2)
        if (child[3] == null)
            return;
        if (child[0].guide.compareTo(child[3].guide) > 0)
            swapNodes(0, 1, 2, 3);
        else if (child[1].guide.compareTo(child[3].guide) > 0)
            swapNodes(1, 2, 3);
        else if (child[2].guide.compareTo(child[3].guide) > 0)
            swapNodes(2, 3);
    }// SortNode
}//class InternalNode
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

