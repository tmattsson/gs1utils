package se.injoin.gs1utils;

class ApplicationIdentifierTree {
    static ApplicationIdentifierTree instance = buildTree();

    static ApplicationIdentifier get(String key) {
        ApplicationIdentifierTree tree = instance;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (ch < '0' || ch > '9')
                return null;
            IdentifierOrTree node = tree.map[(int)(ch - '0')];
            if (null == node)
                return null;
            if (node.isTree()) {
                tree = node.getTree();
            } else {
                return node.getIdentifier();
            }
        }
        return null;
    }

    static class IdentifierOrTree {
        ApplicationIdentifier identifier = null;
        ApplicationIdentifierTree tree = null;

        IdentifierOrTree(ApplicationIdentifier identifier) {
            this.identifier = identifier;
        }
        IdentifierOrTree(ApplicationIdentifierTree tree) {
            this.tree = tree;
        }

        boolean isTree() {
            return null != this.tree;
        }

        ApplicationIdentifierTree getTree() {
            return this.tree;
        }

        ApplicationIdentifier getIdentifier() {
            return this.identifier;
        }
    }

    IdentifierOrTree[] map = new IdentifierOrTree[] {
            null, null, null, null, null,
            null, null, null, null, null
    };

    ApplicationIdentifierTree() {
    }

    void addLeaf(char[] key, int pos, ApplicationIdentifier identifier) {
        char ch = key[pos];
        if (ch < '0' || ch > '9')
            return;
        int mapKey = (int)(ch - '0');
        if (pos == (key.length - 1)) {
            this.map[mapKey] = new IdentifierOrTree(identifier);
        } else {
            if (null == this.map[mapKey]) {
                this.map[mapKey] = new IdentifierOrTree(new ApplicationIdentifierTree());
            }
            this.map[mapKey].getTree().addLeaf(key, pos+1, identifier);
        }
    }

    static ApplicationIdentifierTree buildTree() {
        ApplicationIdentifierTree tree = new ApplicationIdentifierTree();
        for (ApplicationIdentifier item : ApplicationIdentifier.values()) {
            tree.addLeaf(item.getKey().toCharArray(), 0, item);
        }
        return tree;
    }

}
