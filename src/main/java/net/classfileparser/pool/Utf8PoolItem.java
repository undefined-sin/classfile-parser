package net.classfileparser.pool;

public class Utf8PoolItem extends PoolItem {
    private final String text;

    public Utf8PoolItem(int selfIndex, ConstantPoolType type, String text) {
        super(selfIndex, type);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
