import java.util.*;

public class RedBlackTree {
	private static Comparator comparator;
	private RedBlackTree left;
	private RedBlackTree right;
	private RedBlackTree parent;
	private int color;
	private int key;
	private String value;
	
	private static final int BLACK = 0;
	private static final int RED = 1;
	
	public RedBlackTree() {
		
	}
	
	public RedBlackTree(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public RedBlackTree(int color, int key, String value) {
		this.color = color;
		this.key = key;
		this.value = value;
	}
	
	/*绕node向右旋转 finish*/
	public RedBlackTree RRotation(RedBlackTree node) {
		RedBlackTree s = node.left;
		s.parent = node.parent;
		node.left = s.right;
		if(node.left != null)
			node.left.parent = node;
		s.right = node;
		node.parent = s;	
		return s;
	}
	
	/*绕node向左旋转 finish*/
	public RedBlackTree LRotation(RedBlackTree node) {
		RedBlackTree s = node.right;
		s.parent = node.parent;
		node.right = s.left;
		if(node.right != null)
			node.right.parent = node;
		s.left = node;
		node.parent = s;
		return s;
	}
	
	
	/*交换两个结点颜色 finish*/
	public void swapColor(RedBlackTree node1, RedBlackTree node2) {
		if(node1 == null || node2 == null)
			return;
		
		int c = node1.color;
		node1.color = node2.color;
		node2.color = c;
	}
	
	/*获得兄弟结点 finish*/
	public RedBlackTree getBrother(RedBlackTree node) {
		if(isLeft(node)) {
			return node.parent.right;
		} else {
			return node.parent.left;
		}
	}
	
	/*是否是父节点的左孩子 finish*/
	public boolean isRight(RedBlackTree node) {
		return node == node.parent.right;
	}
	
	/*是否是父节点的右孩子 finish*/
	public boolean isLeft(RedBlackTree node) {
		return node == node.parent.left;
	}
	
	/*返回结点的孩子个数(0个 - 2个) finish*/
	public int countOfChild(RedBlackTree node) {
		int cnt = 0;
		if(node.left != null) {
			cnt++;
		}
		if(node.right != null) {
			cnt++;
		}
		return cnt;
	}
	
	/*插入结点  finish*/
	public RedBlackTree insert(RedBlackTree root, int key, String value) {
		RedBlackTree newNode = new RedBlackTree(key, value);
		if(root == null) {
			//root为空，直接从根节点插入
			newNode.color = BLACK;
			return newNode;
		} else {
			RedBlackTree p = null;
			RedBlackTree node = root;
			while(node != null) {
				if(comparator.compare(node, newNode) == 0) {
					return node;
				} else if(comparator.compare(node, newNode) > 0) {
					p = node;
					node = node.left;
				} else {
					p = node;
					node = node.right;
				}
			}
			
			//此时找到空节点,last为当前空节点父节点
			if(comparator.compare(p, newNode) == 0) {
				return p;
			} else if(comparator.compare(p, newNode) > 0) {
				p.left = newNode;
				newNode.parent = p;
				newNode.color = RED;
				insertFix(newNode);
				return newNode;
			} else {
				p.right = newNode;
				newNode.parent = p;
				newNode.color = RED;
				insertFix(newNode);
				return newNode;
			}
		}
	}
	
	/*从node开始进行插入调整  finish*/
	public void insertFix(RedBlackTree node) {
		//node结点父节点为黑色，则不用调整
		if(node.parent.color == BLACK) {
			return;
		} else {
			//当前结点父节点也为红色，需要进行调整
			RedBlackTree p = node.parent;
			RedBlackTree uncle = getBrother(p);
			RedBlackTree s = p.parent;
			//叔父结点为红色
			if(uncle != null && uncle.color == RED) {
				//当父节点和叔父结点都为红色的时候，父节点和叔父结点变为黑色，
				//祖父结点变为红色，继续递归调整祖父节点
				p.color = BLACK;
				uncle.color = BLACK;
				s.color = RED;
				insertFix(s);
			} else {				
				if(isLeft(p)) {
					//node父节点是祖父节点的左孩子	
					
					if(isRight(node)) {
						//此时node是右孩子，node父节点是左孩子
						//对node父节点进行左旋转
						p = LRotation(p);
						node = p.left;
					} 
					//交换node父节点p和祖父节点s的颜色,对node祖父节点s进行右旋转
					swapColor(p, s);
					RRotation(s);
				}				
				else {
					//node父节点是祖父节点的右孩子
					
					if(isLeft(node)) {
						//此时node是左孩子，node父节点是右孩子
						//对node父节点进行右旋转
						p = RRotation(p);
						node = p.right;
					} 
					//交换node父节点p和祖父节点s的颜色,对node祖父节点s进行左旋转
					swapColor(p, s);
					LRotation(s);
				}
			}
		}
	}
	
	/*删除值key的结点 toFinish*/
	public RedBlackTree delete(RedBlackTree root, int key) {
		if(root == null) {
			return root;
		} else {
			RedBlackTree p = null;
			RedBlackTree node = root;
			while(node != null) {
				if(node.key == key) {
			//		deleteFix(node);对node进行删除调整
					if(countOfChild(node) == 0) {
						/*当node没有孩子结点的时候*/
						if(isLeft(node)) {
							node.parent.left = null;
						} else {
							node.parent.right = null;
						}
						
						/*如果颜色为黑色需要进行调整，如果红色没有影响*/
						if(node.color == BLACK) {
							deleteFix(node.parent);
						}
						node.parent = null;
						return node;
					} else if(countOfChild(node) == 1) {
						/*当node只有一个孩子结点的时候，直接用该节点替代node*/
						RedBlackTree child = null;
						if(node.left == null) {
							child = node.right;
						} else {
							child = node.left;
						}						
						
						if(child.color != node.color) {
							/*当node的独生子结点和自己颜色不同(即红黑搭配时)*/				 
							if(child.color == BLACK) {
								/*当独生子结点为黑色，直接替代node即可*/	
								child.parent = node.parent;
								node.parent = null;								
							} else {
								/*当独生子结点为红色，替代node，并且改成黑色即可*/
								child.parent = node.parent;
								child.color = BLACK;
								node.parent = null;
							}
							/*改变父节点指针*/
							if(isLeft(child)) {
								child.parent.left = child;
							} else {
								child.parent.right = child;
							}
							return node;
						} else {
							/*当node的独生子结点和自己颜色相同(即都为黑色)
							  直接用子节点替换成node，然后进行调整*/
							child.parent = node.parent;
							node.parent = null;
							if(isLeft(child)) {
								child.parent.left = child;
							} else {
								child.parent.right = child;
							}
							/*改路径上减少了一个黑色结点，需进行deleteFix*/
							deleteFix(child);
							return node;
						}
					} else {
						/*当node有两个孩子结点时,采用中序遍历的前一个结点替代node*/
						if(node.left.right == null) {
							/*当node的左孩子的右孩子为空*/
							RedBlackTree child = node.left;
							child.parent = node.parent; //
							node.parent = null;
							if(isLeft(child)) {
								/*当child为左孩子*/
								child.parent.left = child;
							} else {
								/*当child为右孩子*/
								child.parent.right = child;
							}
							child.right = node.right;  //
							node.right = null;
							child.right.parent = child; 
							
							return node;
						} else {
							/*当node的左孩子的右孩子不为空*/
							RedBlackTree tmp = node.left.right;
							while(tmp.right != null) {
								tmp = tmp.right;
							}
							/*此时，tmp结点的右孩子为空*/
							tmp.parent.right = tmp.left;
							if(tmp.left != null) {
								tmp.left.parent = tmp.parent;
							}
							tmp.left = null;
							
							/*接下来交换node和tmp结点*/
							tmp.parent = node.parent;
							node.parent = null;
							if(isLeft(tmp)) {
								/*tmp是左孩子*/
								tmp.parent.left = tmp;
							} else {
								/*tmp是右孩子*/
								tmp.parent.right = tmp;
							}
							tmp.left = node.left;
							node.left = null;
							tmp.left.parent = tmp;
							tmp.right = node.right;
							node.right = null;
							tmp.right.parent = tmp;
							if(node.color == BLACK) {
								/*当node是黑色,该路径黑色结点个数减少一个，需要deleteFix*/
								deleteFix(tmp);
							}
							return node;
						}						
					}
				} else if(node.key > key) {
					p = node;
					node = node.left;
				} else {
					p = node;
					node = node.right;
				}
			}
		}			
	}
	
	/*进行删除后调整 toFinish*/
	public RedBlackTree deleteFix(RedBlackTree node) {
		
	}
	
	public static void main(String[] args) {
		
	}
}