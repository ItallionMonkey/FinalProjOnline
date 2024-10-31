package com.example.finalprojonline;

class BinaryTreeNode {
    ApexTeam team;
    BinaryTreeNode left, right;

    public BinaryTreeNode(ApexTeam team) {
        this.team = team;
        this.left = null;
        this.right = null;
    }
}

class BinarySearchTree {
    private BinaryTreeNode root;

    // Insert an Apex team based on win/loss ratio
    public void insert(ApexTeam team) {
        root = insertRec(root, team);
    }
    //
    private BinaryTreeNode insertRec(BinaryTreeNode node, ApexTeam team) {
        if (node == null) {
            return new BinaryTreeNode(team);
        }

        if (team.getWinLossRatio() < node.team.getWinLossRatio()) {
            node.left = insertRec(node.left, team);
        } else {
            node.right = insertRec(node.right, team);
        }

        return node;
    }

    // Find the team with the highest win/loss ratio (rightmost node)
    public ApexTeam findHighestKDTeam() {
        if (root == null) {
            return null;
        }

        BinaryTreeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.team;
    }
}
