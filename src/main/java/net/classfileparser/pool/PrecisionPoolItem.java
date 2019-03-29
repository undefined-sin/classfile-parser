package net.classfileparser.pool;

public class PrecisionPoolItem extends PoolItem {
    private final int left, right;

    public PrecisionPoolItem(int selfIndex, int left, int right, ConstantPoolType type) {
        super(selfIndex, type);
        this.left = left;
        this.right = right;

    }

    public int getRight() {
        return right;
    }

    public int getLeft() {
        return left;
    }
}
