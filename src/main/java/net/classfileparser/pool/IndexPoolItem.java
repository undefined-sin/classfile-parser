package net.classfileparser.pool;

public class IndexPoolItem extends PoolItem {

    private final short index;

    public IndexPoolItem(int selfIndex, short index, ConstantPoolType type) {
        super(selfIndex, type);
        this.index = index;
    }


    public int getIndex() {
        return index;
    }

}
