package net.classfileparser.pool;

public class MethodHandlePoolItem extends PoolItem {

    private final byte reference_kind;
    private final short reference_index;

    public MethodHandlePoolItem(int selfIndex, byte reference_kind, short reference_index, ConstantPoolType type) {
        super(selfIndex, type);
        this.reference_kind = reference_kind;
        this.reference_index = reference_index;
    }

    public byte getReference_kind() {
        return reference_kind;
    }

    public short getReference_index() {
        return reference_index;
    }
}
