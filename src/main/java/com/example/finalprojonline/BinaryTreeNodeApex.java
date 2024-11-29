package com.example.finalprojonline;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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


    private BinaryTreeNode insertRec(BinaryTreeNode node, ApexTeam team) {
        BinaryTreeNode result = node;
        if (node == null) {
            result = new BinaryTreeNode(team);
        } else if (team.getWinLossRatio() < node.team.getWinLossRatio()) {
            node.left = insertRec(node.left, team);
        } else {
            node.right = insertRec(node.right, team);
        }

        return result;
    }

    // Find the team with the highest win/loss ratio (rightmost node)
    public ApexTeam findHighestWinLoss() {
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
