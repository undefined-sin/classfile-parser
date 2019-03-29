package net.classfileparser.pool;

public enum ConstantPoolType {
    CONSTANT_Class(7),
    CONSTANT_Field_ref(9),
    CONSTANT_Method_ref(10),
    CONSTANT_InterfaceMethod_ref(11),
    CONSTANT_String(8),
    CONSTANT_Integer(3),
    CONSTANT_Float(4),
    CONSTANT_Long(5),
    CONSTANT_Double(6),
    CONSTANT_NameAndType(12),
    CONSTANT_Utf8(1),
    CONSTANT_MethodHandle(15),
    CONSTANT_MethodType(16),
    CONSTANT_InvokeDynamic(18);

    byte value;

    ConstantPoolType(int value) {
        this.value = (byte) value;
    }

    public static ConstantPoolType valueOf(byte tag) {
        for (ConstantPoolType item : ConstantPoolType.values()) {
            if (item.value == tag) {
                return item;
            }
        }
        return null;
    }
}
