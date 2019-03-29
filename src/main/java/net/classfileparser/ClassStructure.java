package net.classfileparser;

import net.classfileparser.buffer.BigEndianReader;
import net.classfileparser.pool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ClassStructure {
    public static final String INVALID_UTF_8_ITEM_IN_POOL = "INVALID_UTF8_ITEM_IN_POOL";
    private int magic;
    private short minor_version;
    private short major_version;
    private short constant_pool_count;
    private HashMap<Integer, PoolItem> constant_pool = new HashMap<>();
    private short access_flags;
    private short this_class;
    private short super_class;
    private short interface_count;
    private List<String> interfaces = new ArrayList<>();
    private short fields_count;
    private List<ClassMemberRef> fields = new ArrayList<>();
    private short methods_count;
    private List<ClassMemberRef> methods = new ArrayList<>();
    private short attributes_count;
    private final BigEndianReader reader;


    public ClassStructure(BigEndianReader reader) {
        this.reader = reader;
    }


    public void parse() {
        this.magic = reader.readInt();
        this.minor_version = reader.readShort();
        this.major_version = reader.readShort();
        this.constant_pool_count = reader.readShort();
        for (int i = 1; i < this.constant_pool_count; i++) {
            ConstantPoolType type = ConstantPoolType.valueOf(reader.readByte());
            switch (type) {
                case CONSTANT_Class: {
                    short name_index = reader.readShort();
                    this.constant_pool.put(i, new IndexPoolItem(i, name_index, type));
                    break;
                }
                case CONSTANT_Field_ref:
                case CONSTANT_Method_ref:
                case CONSTANT_InterfaceMethod_ref: {
                    short class_index = reader.readShort();
                    short name_and_type_index = reader.readShort();
                    this.constant_pool.put(i, new NamedIndexPoolItem(i, class_index, name_and_type_index, type));
                    break;
                }
                case CONSTANT_String: {
                    short string_index = reader.readShort();
                    this.constant_pool.put(i, new IndexPoolItem(i, string_index, type));
                    break;
                }
                case CONSTANT_Integer:
                case CONSTANT_Float:
                    int value = reader.readInt();
                    this.constant_pool.put(i, new NumberPoolItem(i, value, type));
                    break;
                case CONSTANT_Long:
                case CONSTANT_Double:
                    int low = reader.readInt();
                    int high = reader.readInt();
                    this.constant_pool.put(i, new PrecisionPoolItem(i, low, high, type));
                    break;
                case CONSTANT_NameAndType: {
                    short name_index = reader.readShort();
                    short descriptor_index = reader.readShort();
                    this.constant_pool.put(i, new NamedIndexPoolItem(i, name_index, descriptor_index, type));
                    break;
                }
                case CONSTANT_Utf8: {
                    short length = reader.readShort();
                    this.constant_pool.put(i, new Utf8PoolItem(i, type, new String(reader.readByteArr(length))));
                    break;
                }
                case CONSTANT_MethodHandle: {
                    byte reference_kind = reader.readByte();
                    short reference_index = reader.readByte();
                    this.constant_pool.put(i, new MethodHandlePoolItem(i, reference_kind, reference_index, type));
                    break;
                }
                case CONSTANT_MethodType: {
                    short descriptor_index = reader.readByte();
                    this.constant_pool.put(i, new IndexPoolItem(i, descriptor_index, type));
                    break;
                }
                case CONSTANT_InvokeDynamic: {
                    short bootstrap_method_attr_index = reader.readShort();
                    short name_and_type_index = reader.readShort();
                    this.constant_pool.put(i, new NamedIndexPoolItem(i, bootstrap_method_attr_index, name_and_type_index, type));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Invalid pool ");
            }
        }

        this.access_flags = reader.readShort();
        this.this_class = reader.readShort();
        this.super_class = reader.readShort();
        this.interface_count = reader.readShort();
        for (int i = 0; i < this.interface_count; i++) {
            short index = reader.readShort();
            IndexPoolItem e = (IndexPoolItem) this.constant_pool.get((int) index);
            interfaces.add(getNamedItemFromPool(e.getIndex()));
        }
        decodeFields();
        decodeMethods();
        this.attributes_count = reader.readShort();
        for (int i = 0; i < this.attributes_count; i++) {
            //TODO: Decode attributes
        }
    }

    private void decodeMethods() {
        this.methods_count = reader.readShort();
        decodeMemberRef(reader, methods_count, methods);
    }

    private void decodeFields() {
        this.fields_count = reader.readShort();
        decodeMemberRef(reader, fields_count, fields);
    }

    private static void decodeMemberRef(BigEndianReader reader, short fieldsCount, List<ClassMemberRef> fields) {
        for (int i = 0; i < fieldsCount; i++) {
            short access_flags = reader.readShort();
            short name_index = reader.readShort();
            short descriptor_index = reader.readShort();
            ClassMemberRef ref = new ClassMemberRef(access_flags, name_index, descriptor_index);
            short attributes_count = reader.readShort();
            ref.setNumberOfAttributes(attributes_count);
            for (int j = 0; j < attributes_count; j++) {
                short attribute_name_index = reader.readShort();
                int attribute_length = reader.readInt();
                for (int k = 0; k < attribute_length; k++) {
                    reader.readByte();
                }
            }
            fields.add(ref);
        }
    }

    public void dump() {
        log("Magic: %05x", this.magic);
        log("Minor version: %05x %d", this.minor_version, this.minor_version);
        log("Major version: %05x %d", this.major_version, this.major_version);
        log("Constant pool size: %05x %d", this.constant_pool_count, this.constant_pool_count);
        log("Access flags: %05x", this.access_flags);
        log("class %s extends %s", getClassName().toLowerCase(), getSuperClassName().toLowerCase());
        log("Interface count: %d and are the following:", this.interface_count);
        dumpInterfaces();
        log("This class has %d fields", this.fields_count);
        dumpFields();
        log("This class has %d methods", this.methods_count);
        dumpMethods();
        log("This class has %d attributes", this.attributes_count);
    }

    private void dumpMethods() {
        for (ClassMemberRef f : this.methods) {
            String descriptor = getNamedItemFromPool(f.getDescriptor_index());
            log("Found method %s %s isPublic: %s isStatic: %s , Attributes: %d", descriptor,
                    getNamedItemFromPool(f.getNameIndex()),
                    f.isPublic(),
                    f.isStatic(),
                    f.getNumberOfAttributes());
        }
    }

    private void dumpFields() {
        for (ClassMemberRef f : this.fields) {
            String descriptor = getNamedItemFromPool(f.getDescriptor_index());
            log("Found field %s %s isPublic: %s isStatic: %s, Attributes: %d", descriptor,
                    getNamedItemFromPool(f.getNameIndex()),
                    f.isPublic(),
                    f.isStatic(),
                    f.getNumberOfAttributes()
            );
        }
    }

    private void dumpInterfaces() {
        for (String name : this.interfaces) {
            log("Interface: %s", name);
        }
    }

    private String getClassName() {
        return getNamedItemFromPoolByIndex(this.this_class);
    }

    private String getSuperClassName() {
        return getNamedItemFromPoolByIndex(this.super_class);
    }

    private String getNamedItemFromPoolByIndex(int index) {
        IndexPoolItem e = (IndexPoolItem) this.constant_pool.get(index);
        return getNamedItemFromPool(e.getIndex());
    }

    public String getNamedItemFromPool(int index) {
        for (PoolItem nestedItem : this.constant_pool.values()) {
            if (nestedItem.getType().equals(ConstantPoolType.CONSTANT_Utf8)) {
                if (nestedItem.getSelfIndex() == index) {
                    return ((Utf8PoolItem) nestedItem).getText();
                }
            }
        }
        return INVALID_UTF_8_ITEM_IN_POOL;
    }

    public void log(String m, Object... args) {
        System.out.println(String.format(m, args));
    }
}
