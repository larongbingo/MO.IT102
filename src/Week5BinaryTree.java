public class Week5BinaryTree {
    public static void main(String[] args) {
        var root = new Node();
        root.left = new Node();
        root.right = new Node();

        var root2 = new Node();
        root2.right = new Node();

        System.out.println(CountTree.countNodes(root));
        System.out.println(CountTree.countNodes(root2));

        System.out.println(BalancedTree.isTreeBalanced(root));
        System.out.println(BalancedTree.isTreeBalanced(root2));
    }
}

// No need to add data for now since both problems dont use it
class Node {
    public Node left;
    public Node right;
}

class CountTree {
    public static int countNodes(Node root) {
        if (root == null)
            return 0;

        return countNodes(root.left) + countNodes(root.right) + 1;
    }
}

class BalancedTree {
    public static boolean isTreeBalanced(Node root) {
        var left = treeHeight(root.left);
        var right = treeHeight(root.right);

        return left - right == 0;
    }

    public static int treeHeight(Node root) {
        if (root == null)
            return 0;

        int left = treeHeight(root.left);
        int right = treeHeight(root.right);

        if (left > right)
            return left + 1;
        else
            return right + 1;
    }
}
