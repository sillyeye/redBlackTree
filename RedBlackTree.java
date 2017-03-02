import java.util.*;

public class RedBlackTree {		
	private static RedBlackTree root;
	private RedBlackTree left;
	private RedBlackTree right;
	private RedBlackTree parent;
	private int color = -1;
	private int key;
	private String value;
	
	static {
		root = new RedBlackTree(root);
		root.parent = null;
	}
	
	private static final int BLACK = 0;
	private static final int RED = 1;
	
	public RedBlackTree() {
		
	}
	
	/*生成黑哨兵的构造方法*/
	public RedBlackTree(RedBlackTree node) {
		this.left = null;
		this.right = null;
		this.parent = node;
		this.color = BLACK;
		this.key = Integer.MIN_VALUE;
	}
	
/*	public RedBlackTree(int key, String value) {
		this.key = key;
		this.value = value;
		this.left = new RedBlackTree(this);
		this.right = new RedBlackTree(this);
	}*/
	
	public RedBlackTree(int color, int key, String value) {
		this.color = color;
		this.key = key;
		this.value = value;
		this.left = new RedBlackTree(this);
		this.right = new RedBlackTree(this);
	}
	
	public String toString() {
		return "[color:" + (color==0?"BLACK":"RED") + "," + key + "->" + value + "]";
	}
	
	/*判断是不是黑哨兵*/
	public static boolean isLeaf(RedBlackTree node) {
		if(node.left == null && node.right == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/*绕node向右旋转 finish*/
	public static RedBlackTree RRotation(RedBlackTree node) {
		RedBlackTree s = node.left;
		s.parent = node.parent;
		if(s.parent != null) {
			if(isLeft(node)) {
				s.parent.left = s;
			} else {
				s.parent.right = s;
			}
		}
			
		node.left = s.right;
		if(node.left != null)
			node.left.parent = node;
		s.right = node;
		node.parent = s;	
		
		if(s.parent == null)
			root = s;
		return s;
	}
	
	/*绕node向左旋转 finish*/
	public static RedBlackTree LRotation(RedBlackTree node) {
		RedBlackTree s = node.right;
		s.parent = node.parent;
		if(s.parent != null) {
			if(isLeft(node)) {
				s.parent.left = s;
			} else {
				s.parent.right = s;
			}
		}
		
		node.right = s.left;
		if(node.right != null)
			node.right.parent = node;
		s.left = node;
		node.parent = s;
		
		if(s.parent == null)
			root = s;
		return s;
	}
	
	
	/*交换两个结点颜色 finish*/
	public static void swapColor(RedBlackTree node1, RedBlackTree node2) {
		if(node1 == null || node2 == null)
			return;
		
		int c = node1.color;
		node1.color = node2.color;
		node2.color = c;
	}
	
	/*获得兄弟结点 finish*/
	public static RedBlackTree getBrother(RedBlackTree node) {
		if(isLeft(node)) {
			return node.parent.right;
		} else {
			return node.parent.left;
		}
	}
	
	/*是否是父节点的左孩子 finish*/
	public static boolean isRight(RedBlackTree node) {
		return node == node.parent.right;
	}
	
	/*是否是父节点的右孩子 finish*/
	public static boolean isLeft(RedBlackTree node) {
		return node == node.parent.left;
	}
	
	/*返回结点的孩子个数(0个 - 2个) finish*/
	public static int countOfChild(RedBlackTree node) {
		int cnt = 0;
		if(!isLeaf(node.left)) {
			cnt++;
		}
		if(!isLeaf(node.right)) {
			cnt++;
		}
		return cnt;
	}
	
	/*插入结点  finish*/
	public static void insert(int key, String value) {
	//	RedBlackTree newNode = new RedBlackTree(key, value);
		if(root == null) {
			//root为空，直接从根节点插入
			root = new RedBlackTree(BLACK, key, value);
		} else {
			RedBlackTree node = root;
			while(!isLeaf(node)) {
				if(node.key == key) {
					return;
				} else if(node.key > key) {
					node = node.left;
				} else {
					node = node.right;
				}
			}
			
			//此时node黑哨兵节点	
			node.left = new RedBlackTree(node);
			node.right = new RedBlackTree(node);
			node.color = RED;
			if(node.parent == null) {
				node.color = BLACK;
			}
			node.key = key;
			node.value = value;
			insertFix(node);
			return;
		}
	}
	
	/*从node开始进行插入调整  finish*/
	public static void insertFix(RedBlackTree node) {
		if(node.parent == null) {
			node.color = BLACK;
			return;
		}
		//node结点父节点为黑色，则不用调整
		if(node.parent.color == BLACK) {
			return;
		} else {
			//当前结点父节点也为红色，需要进行调整
			RedBlackTree p = node.parent;
			RedBlackTree uncle = getBrother(p);//node的叔父结点
			RedBlackTree s = p.parent;//node的祖父结点
			//叔父结点为红色
			if(!isLeaf(uncle) && uncle.color == RED) {
				//当父节点和叔父结点都为红色的时候，父节点和叔父结点变为黑色，
				//祖父结点变为红色，继续递归调整祖父节点
				p.color = BLACK;
				uncle.color = BLACK;
				s.color = RED;
				insertFix(s);
				return;
			} else {	
				//叔父结点为黑色			
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
	public static String delete(int key) {
		if(root == null || isLeaf(root)) {
			return null;
		} else {
			RedBlackTree node = root;
			while(!isLeaf(node)) {
				if(node.key == key) {
					/*找到key值的结点*/
					if(countOfChild(node) == 0) {
						/*当node两个孩子都为黑哨兵时*/
						RedBlackTree tmp = node.left;
						tmp.parent = node.parent;
						if(isLeft(node)) {
							tmp.parent.left = tmp;
						} else {
							tmp.parent.right = tmp;
						}
						
						if(node.color == BLACK) {
							deleteFix(tmp);
						}
						return node.value;
					}
					else if(countOfChild(node) == 1) {
						/*当node只有一个孩子结点的时候，直接用该节点替代node*/
						RedBlackTree child = null;
						if(isLeaf(node.left)) {
							child = node.right;
						} else {
							child = node.left;
						}		
						
						/*当node只有一个孩子结点的时候，直接用该节点替代node*/						
						child.parent = node.parent;
						if(isLeft(node)) {
							child.parent.left = child;
						} else {
							child.parent.right = child;
						}						
						
						if(child.color != node.color) {
							/*当node的独生子结点和自己颜色不同(即红黑搭配时)*/				 
							if(child.color == BLACK) {
								/*当独生子结点为黑色，直接替代node即可*/			
							} else {
								/*当独生子结点为红色，替代node，并且改成黑色即可*/
								child.color = BLACK;
							}
							
							return node.value;
						} else {
							/*当node的独生子结点和自己颜色相同(即都为黑色)
							  直接用子节点替换成node，然后进行调整*/
							
							/*改路径上减少了一个黑色结点，需进行deleteFix*/
							deleteFix(child);
							return node.value;
						}
					} else if(countOfChild(node) == 2) {
						/*当node有两个孩子结点时,采用中序遍历的前一个结点替代node*/
						if(isLeaf(node.left.right)) {
							/*当node的左孩子的右孩子为黑哨兵*/
							RedBlackTree child = node.left;
														
							String rslt = node.value; //
							node.value = child.value;
							node.key = child.key;
							/*用tmp(child的左孩子)替代child*/
							RedBlackTree tmp = child.left;
							tmp.parent = node;
							node.left = tmp;
							
							/*如果node的左结点即child为黑色，那么该路
							  径黑色结点个数减少一个，需要进行deleteFix*/
							if(child.color == BLACK) {
								deleteFix(tmp);
							}
							return rslt;
						} else {
							/*当node的左孩子的右孩子不为黑哨兵*/
							RedBlackTree tmp = node.left.right;
							while(!isLeaf(tmp.right)) {
								tmp = tmp.right;
							}
							/*此时，tmp结点的右孩子为黑哨兵*/
							String rslt = node.value;
							node.value = tmp.value;
							node.key = tmp.key;
							
							/*用tmp的独生子l替代tmp*/
							RedBlackTree l = tmp.left;
							l.parent = tmp.parent;
							l.parent.right = l;
							if(tmp.color == BLACK && l.color == BLACK) {
								/*当node是黑色,该路径黑色结点个数减少一个，需要deleteFix*/
								deleteFix(l);
							} else if(tmp.color == BLACK && l.color == RED) {
								l.color = BLACK;
							}
							return rslt;
						}						
					}
				} else if(node.key > key) {
					/*当此结点大于key，走左边路径*/
					node = node.left;
				} else {
					/*当此结点小于key，走右边路径*/
					node = node.right;
				}
			}
			
			/*此时未查询到key值的结点*/
			return null;
		}			
	}
	
	/*进行删除后调整 Finish*/
	public static void deleteFix(RedBlackTree node) {
		if(node == null) {
			return;
		}
		
		/*此时该路径上减少一个黑色结点，需要进行调整*/
		if(node.parent == null) {
			/*node的父节点为空(node为root根结点)*/
			return;
		} 
				
		RedBlackTree p = node.parent;//node的父节点
		RedBlackTree b = getBrother(node); //node的兄弟结点
		RedBlackTree bl = b.left;//node的左侄子
		RedBlackTree br = b.right;//node的右侄子
		if(b.color == RED) {
			/*node的兄弟结点为红色*/
			if(isLeft(node)) {
				/*交换兄弟结点和父节点颜色,绕node父节点左旋转*/
				swapColor(b, p);
				LRotation(p);
			} else {
				/*交换兄弟结点和父节点颜色,绕node父节点右旋转*/
				swapColor(b, p);
				RRotation(p);
			}
			/*此时node兄弟结点变为黑色，重新调整*/
			deleteFix(node); 
			return;
		} else {  		
			/*node的兄弟结点为黑色*/
			if(isLeft(node)) {
				/*当node为左孩子，此时br为远的侄子结点*/
				if(br.color == RED) {
					/*当br为红色，进行调整：交换p和b颜色，br改成黑色，绕p左旋转*/
					swapColor(b, p);
					br.color = BLACK;
					LRotation(p);
					return;
				} else {
					/*当node的远房侄子为黑色*/
					if(p.color == BLACK && bl.color == BLACK) {
						/*当所有父节点兄弟结点侄子结点都为黑色
						  修改兄弟结点为红色，对父节点进行deleteFix*/
						b.color = RED;
						deleteFix(p);
						return;
					} else if(p.color == RED && bl.color == BLACK) {
						/*当父节点p为红色，node近房侄子结点为黑色
						  此时直接对父节点p进行左旋转就可以*/
						  LRotation(p);
						  return;
					} else {
						/*近房侄子结点颜色为红色,此时交换兄弟结点b和
						  近房侄子bl结点颜色，对兄弟结点右旋转*/
						  swapColor(b, bl);
						  b = RRotation(b);
						  deleteFix(node);
						  return;
					}
				}
			} else {
				/*当node为右孩子，此时bl为远的侄子结点*/
				if(bl.color == RED) {
					/*当bl为红色，进行调整：交换p和b颜色，bl改成黑色，绕p右旋转*/
					swapColor(b, p);
					bl.color = BLACK;
					RRotation(p);
					return;
				} else {
					/*当node的远房侄子为黑色*/
					if(p.color == BLACK && br.color == BLACK) {
						/*当所有父节点兄弟结点侄子结点都为黑色
						  修改兄弟结点为红色，对父节点进行deleteFix*/
					} else if(p.color == RED && br.color == BLACK) {
						/*当父节点p颜色为红色，且node近房侄子br结点为黑色，
						  此时对父节点p进行右旋转*/
						  RRotation(p);
						  return;
					} else {
						/*近房侄子br结点颜色为红色，此时交换兄弟结点b和
						  近房侄子br结点颜色，对兄弟结点左旋转*/
						  swapColor(b, br);
						  LRotation(b);
						  deleteFix(node);
						  return;
					}
				}
			}
		}	
	}
	
	/*Finish*/
	public static RedBlackTree getNode(int key) {
		if(root == null || isLeaf(root))
			return null;
		
		RedBlackTree node = root;
		while(!isLeaf(node)) {
			if(node.key == key) {
				return node;
			} else if(node.key > key){
				node = node.left;
			} else {
				node = node.right;
			}
		}
		return null;
	}
	
	/*Finish*/
	public static Object get(int key) {
		if(root == null || isLeaf(root))
			return null;
		
		RedBlackTree node = root;
		while(!isLeaf(node)) {
			if(node.key == key) {
				return node.value;
			} else if(node.key > key){
				node = node.left;
			} else {
				node = node.right;
			}
		}
		return null;
	}
	
	/*Finish*/
	public static void put(int key, String value) {
		if(root == null)
			return;
		
		if(isLeaf(root)) {
			root.left = new RedBlackTree(root);
			root.right = new RedBlackTree(root);
			root.key = key;
			root.value = value;
			root.color = BLACK;
			return;
		}
		
		RedBlackTree node = getNode(key);
		if(node != null) {
			node.value = value;
		} else {
			insert(key, value);
		}
	}
	
	private static void printPath(RedBlackTree node, List<RedBlackTree> list, int num) {
		if(isLeaf(node)) {			
			list.add(node);
			System.out.println(list + " the black count:" + (num + 1));
			list.remove(node);
			return;
		} else {
			list.add(node);
			if(node.color == BLACK)
				num++;
			printPath(node.left, list, num);
			printPath(node.right, list, num);
			list.remove(node);
		}
	}
 	
	public static void main(String[] args) {		
		Random random = new Random();
		for(int i = 0;i < 200;i++) {
			int num = random.nextInt(500);
			put(num, String.valueOf('a' + num));
		}
		System.out.println();
		List<RedBlackTree> list = new ArrayList<RedBlackTree>();
		printPath(root, list, 0);
		
		for(int i = 0;i < 30;i++) {
			int num = random.nextInt(500);
			System.out.println(num + "->" + delete(num));
		}
	//	System.out.println(4 + "->" + delete(4));
		
		printPath(root, list, 0);
	}
}
