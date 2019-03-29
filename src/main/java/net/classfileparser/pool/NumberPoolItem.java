package net.classfileparser.pool;

public class NumberPoolItem extends PoolItem {

    private final int value;

    public NumberPoolItem(int selfIndex, int value, ConstantPoolType type) {
        super(selfIndex, type);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
