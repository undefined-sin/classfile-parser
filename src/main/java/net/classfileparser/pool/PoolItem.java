package net.classfileparser.pool;

public abstract class PoolItem {
    private final ConstantPoolType type;
    private final int selfIndex;

    public PoolItem(int selfIndex, ConstantPoolType type) {
        this.selfIndex = selfIndex;
        this.type = type;
    }

    public ConstantPoolType getType() {
        return this.type;
    }

    public int getSelfIndex() {
        return selfIndex;
    }
}
