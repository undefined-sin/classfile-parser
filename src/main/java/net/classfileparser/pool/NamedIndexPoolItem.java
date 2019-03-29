package net.classfileparser.pool;

public class NamedIndexPoolItem extends IndexPoolItem {

    private short name_index;

    public NamedIndexPoolItem(int selfIndex, short index, short name_index, ConstantPoolType type) {
        super(selfIndex, index, type);
        this.name_index = name_index;
    }

    public short getName_index() {
        return name_index;
    }
}
